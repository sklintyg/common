/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00251.pdf;

import static se.inera.intyg.common.pdf.renderer.PrintConfig.UTSK001_BODY;
import static se.inera.intyg.common.pdf.renderer.PrintConfig.UTSK001_HEADER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.af00251.support.AF00251EntryPoint;
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

    private static final String PDF_SUMMARY_HEADER = "Arbetsförmedlingens intyg för arbetsmarknadspolitiska program";
    private static final String PDF_LOGOTYPE_CLASSPATH_URI = "Af_logo_rgb_270px@2x.png";
    private static final String PDF_UP_MODEL_CLASSPATH_URI_TEMPLATE = "af00251-uv-viewmodel.v%s.js";

    private static final Logger LOG = LoggerFactory.getLogger(PdfGenerator.class);

    private static final String INFO_SIGNED_TEXT = "Detta är en utskrift av ett elektroniskt intyg. Intyget har signerats "
        + "elektroniskt av intygsutfärdaren.";
    private static final String INFO_UTKAST_TEXT = "Detta är en utskrift av ett elektroniskt intygsutkast och ska INTE "
        + "skickas till Arbetsförmedlingen.";
    private static final String SENT_TEXT = "Notera att intyget redan har skickats till Arbetsförmedlingen.";

    private static final String CERTIFICATE_FILE_PREFIX = "af_lakarintyg_arbetsmarknadspolitiskt_program_";

    // CHECKSTYLE:OFF ParameterNumber
    public PdfResponse generatePdf(String intygsId, String jsonModel, String majorVersion, Personnummer personId, IntygTexts intygTexts,
        List<Status> statuses,
        ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus, String footerAppName) throws ModuleException {

        try {
            String cleanedJson = cleanJsonModel(jsonModel);
            String upJsModel = loadUvViewConfig(majorVersion);
            byte[] logoData = loadLogotype();

            boolean isUtkast = UtkastStatus.DRAFT_COMPLETE == utkastStatus || UtkastStatus.DRAFT_INCOMPLETE == utkastStatus;
            boolean isLockedUtkast = UtkastStatus.DRAFT_LOCKED == utkastStatus;
            boolean isMakulerad = statuses != null && statuses.stream().anyMatch(s -> CertificateState.CANCELLED.equals(s.getType()));

            PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(intygsId)
                .withIntygsNamn(AF00251EntryPoint.MODULE_NAME)
                .withIntygsKod(AF00251EntryPoint.ISSUER_TYPE_ID)
                .withPersonnummer(personId.getPersonnummerWithDash())
                .withInfoText(buildInfoText(isUtkast || isLockedUtkast, statuses))
                .withSummary(new Summary()
                    .add(PDF_SUMMARY_HEADER, intygTexts.getTexter().get("FRM_1.RBK"))
                    .add(UTSK001_HEADER, UTSK001_BODY))
                .withLeftMarginTypText(AF00251EntryPoint.ISSUER_TYPE_ID + " - Fastställd av Arbetsförmedlingen")
                .withUtfardarLogotyp(logoData)
                .withIsUtkast(isUtkast)
                .withIsLockedUtkast(isLockedUtkast)
                .withIsMakulerad(isMakulerad)
                .withApplicationOrigin(applicationOrigin)
                .withFooterAppName(footerAppName)
                .build();

            byte[] data = new UVRenderer().startRendering(printConfig, intygTexts, buildFilename());
            return new PdfResponse(data, buildFilename());
        } catch (IOException e) {
            LOG.error("Error generating PDF for AF00251: " + e.getMessage());
            throw new ModuleException("Error generating PDF for AF00251: " + e.getMessage());
        }
    }
    // CHECKSTYLE:ON ParameterNumber

    private String buildInfoText(boolean isUtkast, List<Status> statuses) {
        StringBuilder buf = new StringBuilder();
        buf.append(isUtkast ? INFO_UTKAST_TEXT : INFO_SIGNED_TEXT);
        if (statuses != null && statuses.stream().anyMatch(status -> CertificateState.SENT == status.getType())) {
            buf.append("\n").append(SENT_TEXT);
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

    // af_medicinskt_utlatande_åå_mm_dd_ttmm
    private String buildFilename() {
        LocalDateTime now = LocalDateTime.now();
        return CERTIFICATE_FILE_PREFIX + now.format(DateTimeFormatter.ofPattern("yy_MM_dd_HHmm")) + ".pdf";
    }

    private JsonNode toIntygJsonNode(String jsonModel) throws IOException {
        return new ObjectMapper().readTree(jsonModel);
    }
}
