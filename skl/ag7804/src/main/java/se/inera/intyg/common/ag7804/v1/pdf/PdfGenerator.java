/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.ag7804.v1.pdf;

import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_JSON_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.ag7804.support.Ag7804EntryPoint;
import se.inera.intyg.common.pdf.model.Summary;
import se.inera.intyg.common.pdf.renderer.PrintConfig;
import se.inera.intyg.common.pdf.renderer.UVRenderer;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.schemas.contract.Personnummer;

public class PdfGenerator {

    protected static final String CERTIFICATE_FILE_BASE_NAME = "arbetsgivarintyg_";
    protected static final String MINIMAL_CERTIFICATE_FILE_PREFIX = "minimalt_" + CERTIFICATE_FILE_BASE_NAME;
    private static final String PDF_SUMMARY_HEADER = Ag7804EntryPoint.MODULE_NAME;
    private static final String PDF_LOGOTYPE_CLASSPATH_URI = "skl_logo.png";
    private static final String PDF_UP_MODEL_CLASSPATH_URI_TEMPLATE = "ag7804-uv-viewmodel.v%s.js";
    private static final Logger LOG = LoggerFactory.getLogger(PdfGenerator.class);

    private static final String INFO_SIGNED_TEXT_COMPLETE = "Detta är en utskrift av ett elektroniskt intyg. Intyget har "
        + "signerats elektroniskt av intygsutfärdaren.";

    private static final String INFO_SIGNED_TEXT_CUSTOMIZED = "Detta är en anpassad utskrift av ett elektroniskt intyg. "
        + "Viss information i intyget har valts bort. Intyget har signerats elektroniskt av intygsutfärdaren.";

    private static final String INFO_UTKAST_TEXT = "Detta är en utskrift av ett elektroniskt intygsutkast och "
        + "ska INTE skickas till arbetsgivaren.";

    private static final String SKIP_SYMBOL = "!";
    private static final String OPTIONAL_FIELD_FORMEDLA_DIAGNOSER = ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100;
    private static final String OPTIONAL_FIELD_DIAGNOSER = DIAGNOS_SVAR_JSON_ID_6;
    private static final String OPTIONAL_FIELD_DIAGNOSER_REPLACEMENT_TEXT = "På patientens begäran uppges inte diagnos";

    // CHECKSTYLE:OFF ParameterNumber
    public PdfResponse generatePdf(String intygsId, String jsonModel, String majorVersion, Personnummer personId, IntygTexts intygTexts,
        List<Status> statuses,
        ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus, List<String> optionalFields) throws ModuleException {

        try {
            String cleanedJson = cleanJsonModel(jsonModel);
            String upJsModel = loadUvViewConfig(majorVersion);
            byte[] logoData = loadLogotype();

            boolean isUtkast = UtkastStatus.DRAFT_COMPLETE == utkastStatus || UtkastStatus.DRAFT_INCOMPLETE == utkastStatus;
            boolean isLockedUtkast = UtkastStatus.DRAFT_LOCKED == utkastStatus;
            boolean isMakulerad = statuses != null && statuses.stream().anyMatch(s -> CertificateState.CANCELLED.equals(s.getType()));

            Map<String, String> modelPropReplacements = buildModelPropReplacements(optionalFields);

            PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(intygsId)
                .withIntygsNamn(Ag7804EntryPoint.MODULE_NAME)
                .withIntygsKod(Ag7804EntryPoint.ISSUER_TYPE_ID)
                .withPersonnummer(personId.getPersonnummerWithDash())
                .withInfoText(buildInfoText(isUtkast || isLockedUtkast, modelPropReplacements.isEmpty()))
                .withSummary(new Summary().add(PDF_SUMMARY_HEADER, intygTexts.getTexter().get("FRM_1.RBK")))
                .withLeftMarginTypText(intygTexts.getProperties().getProperty("formId"))
                .withUtfardarLogotyp(logoData)
                .withIsUtkast(isUtkast)
                .withIsLockedUtkast(isLockedUtkast)
                .withIsMakulerad(isMakulerad)
                .withApplicationOrigin(applicationOrigin)
                .withSignBox(true)
                .withSignatureLine(true)
                .withModelPropReplacements(modelPropReplacements)
                .build();

            byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
            return new PdfResponse(data, buildFilename(optionalFields != null && optionalFields.size() > 0));
        } catch (IOException e) {
            LOG.error("Error generating PDF for AG7804: " + e.getMessage());
            throw new ModuleException("Error generating PDF for AG7804: " + e.getMessage());
        }
    }
    // CHECKSTYLE:ON ParameterNumber

    /**
     * Build a map of modelProp names and a replacement text to be used when rendering it.
     */
    private Map<String, String> buildModelPropReplacements(List<String> optionalFields) {
        Map<String, String> overrides = new HashMap<>();
        if (optionalFields != null) {

            // Override diagnos field(s) ?
            if (optionalFields.stream().anyMatch(s -> s.equals(SKIP_SYMBOL + OPTIONAL_FIELD_DIAGNOSER))) {
                overrides.put(OPTIONAL_FIELD_DIAGNOSER, OPTIONAL_FIELD_DIAGNOSER_REPLACEMENT_TEXT);
            }
            if (optionalFields.stream().anyMatch(s -> s.equals(SKIP_SYMBOL + OPTIONAL_FIELD_FORMEDLA_DIAGNOSER))) {
                overrides.put(OPTIONAL_FIELD_FORMEDLA_DIAGNOSER, OPTIONAL_FIELD_DIAGNOSER_REPLACEMENT_TEXT);
            }
        }
        return overrides;
    }

    private String buildInfoText(boolean isUtkast, boolean isComplete) {
        if (isUtkast) {
            return INFO_UTKAST_TEXT;
        }

        StringBuilder buf = new StringBuilder();

        if (isComplete) {
            buf.append(INFO_SIGNED_TEXT_COMPLETE);
        } else {
            buf.append(INFO_SIGNED_TEXT_CUSTOMIZED);
        }

        return buf.toString();
    }

    private byte[] loadLogotype() throws IOException {
        return IOUtils.toByteArray(new ClassPathResource(PDF_LOGOTYPE_CLASSPATH_URI).getInputStream());
    }

    private String cleanJsonModel(String jsonModel) throws IOException {
        JsonNode intygJsonNode = toIntygJsonNode(jsonModel);
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        if (Strings.isNullOrEmpty(cleanedJson)) {
            throw new IllegalArgumentException("Cannot generate PDF, supplied intyg JSON model is null or empty.");
        }
        return cleanedJson;
    }

    private String loadUvViewConfig(String majorVersion) throws IOException {
        String templateUriPath = String.format(PDF_UP_MODEL_CLASSPATH_URI_TEMPLATE, majorVersion);
        String upJsModel = IOUtils.toString(new ClassPathResource(templateUriPath).getInputStream(),
            Charset.forName("UTF-8"));
        if (Strings.isNullOrEmpty(upJsModel)) {
            throw new IllegalArgumentException("Cannot generate PDF, UV viewConfig not found on classpath: " + templateUriPath);
        }
        return upJsModel;
    }

    private String buildFilename(boolean pdfEmployer) {
        LocalDateTime now = LocalDateTime.now();
        return (pdfEmployer ? MINIMAL_CERTIFICATE_FILE_PREFIX
            : CERTIFICATE_FILE_BASE_NAME) + now.format(DateTimeFormatter.ofPattern("yy_MM_dd_HHmm")) + ".pdf";
    }

    private JsonNode toIntygJsonNode(String jsonModel) throws IOException {
        return new ObjectMapper().readTree(jsonModel);
    }

}
