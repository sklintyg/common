/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1062.v1.pdf;

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
import org.springframework.stereotype.Component;
import se.inera.intyg.common.pdf.model.Summary;
import se.inera.intyg.common.pdf.renderer.PrintConfig;
import se.inera.intyg.common.pdf.renderer.UVRenderer;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint;
import se.inera.intyg.schemas.contract.Personnummer;

@Component("tstrk1062.v1.PdfGenerator")
public class PdfGenerator {

    private static final String PDF_LOGOTYPE_CLASSPATH_URI = "pdf/transportstyrelsens_logotyp_rgb.png";
    private static final String PDF_UP_MODEL_CLASSPATH_URI = "pdf/tstrk1062-uv-viewmodel.v1.js";

    private static final Logger LOG = LoggerFactory.getLogger(PdfGenerator.class);

    private static final String INFO_SIGNED_TEXT = "Detta är en utskrift av ett elektroniskt intyg. "
        + "Intyget har signerats elektroniskt av intygsutfärdaren.";
    private static final String INFO_UTKAST_TEXT = "Detta är en utskrift av ett elektroniskt intygsutkast och ska INTE "
        + "skickas till Transportstyrelsen.";
    private static final String SENT_TEXT = "Notera att intyget redan har skickats till Transportstyrelsen.";

    private static final String CERTIFICATE_FILE_PREFIX = "lakarintyg_transportstyrelsen_";

    public PdfResponse generatePdf(String intygsId, String jsonModel, Personnummer personId, IntygTexts intygTexts, List<Status> statuses,
        ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus, String footerAppName) throws ModuleException {

        try {
            String cleanedJson = cleanJsonModel(jsonModel);
            String upJsModel = loadUvViewConfig();
            byte[] logoData = loadLogotype();

            boolean isUtkast = UtkastStatus.getDraftStatuses().contains(utkastStatus);
            boolean isLockedUtkast = UtkastStatus.DRAFT_LOCKED == utkastStatus;
            boolean isMakulerad = statuses != null && statuses.stream().anyMatch(s -> CertificateState.CANCELLED.equals(s.getType()));

            PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(intygsId)
                .withIntygsNamn(TsTrk1062EntryPoint.MODULE_NAME)
                .withIntygsKod(KvIntygstyp.TSTRK1062.getCodeValue())
                .withPersonnummer(personId.getPersonnummerWithDash())
                .withInfoText(buildInfoText(isUtkast || isLockedUtkast, statuses))
                .withSummary(new Summary()
                    .add("", getCleanModuleDescription(intygTexts))
                    .add(UTSK001_HEADER, UTSK001_BODY))
                .withLeftMarginTypText(KvIntygstyp.TSTRK1062.getCodeValue() + " - Fastställd av Transportstyrelsen")
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
            LOG.error("Error generating PDF for tstrk1062: " + e.getMessage());
            throw new ModuleException("Error generating PDF for tstrk1062: " + e.getMessage());
        }
    }

    private String getCleanModuleDescription(IntygTexts intygTexts) {
        String rawText = intygTexts.getTexter().get("FRM_1.RBK");
        return rawText != null ? rawText.replace("<LINK:transportstyrelsen>", "www.transportstyrelsen.se") : rawText;
    }

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

    private String loadUvViewConfig() throws IOException {
        String upJsModel = IOUtils.toString(new ClassPathResource(PDF_UP_MODEL_CLASSPATH_URI).getInputStream(),
            Charset.forName("UTF-8"));
        if (Strings.isNullOrEmpty(upJsModel)) {
            throw new IllegalArgumentException("Cannot generate PDF, UV viewConfig not found on classpath: " + PDF_UP_MODEL_CLASSPATH_URI);
        }
        return upJsModel;
    }

    private String buildFilename() {
        LocalDateTime now = LocalDateTime.now();
        return CERTIFICATE_FILE_PREFIX + now.format(DateTimeFormatter.ofPattern("yy-MM-dd_HHmm")) + ".pdf";
    }

    private JsonNode toIntygJsonNode(String jsonModel) throws IOException {
        return new ObjectMapper().readTree(jsonModel);
    }
}
