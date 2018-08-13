/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00213.pdf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.af00213.support.Af00213EntryPoint;
import se.inera.intyg.common.pdf.renderer.PrintConfig;
import se.inera.intyg.common.pdf.renderer.UVRenderer;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.schemas.contract.Personnummer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PdfGenerator {

    private static final String PDF_SUMMARY_HEADER = "Arbetsförmedlingens medicinska utlåtande";
    private static final String PDF_LOGOTYPE_CLASSPATH_URI = "Af_logo_rgb_270px@2x.png";
    private static final String PDF_UP_MODEL_CLASSPATH_URI = "af00213-uv-viewmodel.js";

    private static final Logger LOG = LoggerFactory.getLogger(PdfGenerator.class);
    private static final String INFO_TEXT = "Detta är en utskrift av ett elektroniskt intyg.";


    private static final String CERTIFICATE_FILE_PREFIX = "af_medicinskt_utlatande_";

    public PdfResponse generatePdf(String jsonModel, Personnummer personId, IntygTexts intygTexts) throws ModuleException {

        try {
            String cleanedJson = cleanJsonModel(jsonModel);
            String upJsModel = loadUvViewConfig();
            byte[] logoData = loadLogotype();

            PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                    .withIntygJsonModel(cleanedJson)
                    .withUpJsModel(upJsModel)
                    .withIntygsId(UUID.randomUUID().toString())
                    .withIntygsNamn(Af00213EntryPoint.MODULE_NAME)
                    .withIntygsKod(Af00213EntryPoint.ISSUER_TYPE_ID)
                    .withPersonnummer(personId.getPersonnummerWithDash())
                    .withInfoText(INFO_TEXT)
                    .withSummaryHeader(PDF_SUMMARY_HEADER)
                    .withSummaryText(intygTexts.getTexter().get("FRM_1.RBK"))
                    .withLeftMarginTypText(Af00213EntryPoint.ISSUER_TYPE_ID)
                    .withUtfardarLogotyp(logoData)
                    .build();

            byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
            return new PdfResponse(data, buildFilename());
        } catch (IOException e) {
            LOG.error("Error generating PDF for AF00213: " + e.getMessage());
            throw new ModuleException("Error generating PDF for AF00213: " + e.getMessage());
        }
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

    // af_medicinskt_utlatande_åå_mm_dd_ttmm
    private String buildFilename() {
        LocalDateTime now = LocalDateTime.now();
        return CERTIFICATE_FILE_PREFIX + now.format(DateTimeFormatter.ofPattern("yy_MM_dd_HHmm")) + ".pdf";
    }

    private JsonNode toIntygJsonNode(String jsonModel) throws IOException {
        return new ObjectMapper().readTree(jsonModel);
    }
}
