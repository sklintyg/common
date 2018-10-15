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
package se.inera.intyg.common.pdf.renderer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class UVRendererTest {

    private static final String PNR = "19121212-1212";

    private static final String INFO_TEXT_TS = "Detta är en utskrift av ett elektroniskt intyg. Intyget har signerats elektroniskt av intygsutfärdaren. Notera att intyget "
            + "redan har skickats till Transportstyrelsen.";
    private static final String INFO_TEXT_FK = "Detta är en utskrift av ett elektroniskt intyg. Intyget har signerats elektroniskt av intygsutfärdaren. Notera att intyget "
            + "redan har skickats till Försäkringskassan.";
    private static final String INFO_TEXT_AF = "Detta är en utskrift av ett elektroniskt intyg.";

    @Test
    public void testTsBas() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("tsbas/intyg.tsbas.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("tsbas/tsbas-up.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("tsbas/texterTS_TSTRK_1007_v6.8.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("transportstyrelsen-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(UUID.randomUUID().toString())
                .withIntygsNamn("Transportstyrelsens läkarintyg")
                .withIntygsKod("TSTRK1007")
                .withPersonnummer(PNR)
                .withInfoText(INFO_TEXT_TS)
                .withHasSummaryPage(true)
                .withSummaryHeader("Om Transportstyrelsens läkarintyg")
                .withSummaryText("Lorem ipsum")
                .withLeftMarginTypText("TSTRK1007 (U08) 160114")
                .withUtfardarLogotyp(logoData)
                .withApplicationOrigin(ApplicationOrigin.WEBCERT)
                .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/tsbas-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLisjp() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("lisjp/intyg.lisjp.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("lisjp/lisjp-up.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("lisjp/texterMU_LISJP_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("forsakringskassan-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(UUID.randomUUID().toString())
                .withIntygsNamn("Läkarintyg för sjukpenning")
                .withIntygsKod("FK7800")
                .withPersonnummer(PNR)
                .withInfoText(INFO_TEXT_FK)
                .withHasSummaryPage(true)
                .withSummaryHeader("Om försäkringskassans läkarintyg")
                .withSummaryText("Lorem ipsum")
                .withLeftMarginTypText("FK7800 180214")
                .withUtfardarLogotyp(logoData)
                .withApplicationOrigin(ApplicationOrigin.WEBCERT)
                .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/lisjp-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAf00213() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("af00213/intyg.af00213.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("af00213/af00213-uv-viewmodel.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("af00213/texterMU_AF00213_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("af-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(UUID.randomUUID().toString())
                .withIntygsNamn("Arbetsförmedlingens medicinska utlåtande")
                .withIntygsKod("AF00213")
                .withPersonnummer(PNR)
                .withInfoText(INFO_TEXT_AF)
                .withHasSummaryPage(true)
                .withSummaryHeader("Arbetsförmedlingens medicinska utlåtande")
                .withSummaryText("Lorem ipsum")
                .withLeftMarginTypText("AF00213")
                .withUtfardarLogotyp(logoData)
                .withApplicationOrigin(ApplicationOrigin.WEBCERT)
                .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/af00213-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAf00213WithoutSummaryPage() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("af00213/intyg.af00213.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("af00213/af00213-uv-viewmodel.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("af00213/texterMU_AF00213_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("af-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(UUID.randomUUID().toString())
                .withIntygsNamn("Arbetsförmedlingens medicinska utlåtande")
                .withIntygsKod("AF00213")
                .withPersonnummer(PNR)
                .withInfoText(INFO_TEXT_AF)
                .withHasSummaryPage(false)
                .withSummaryHeader("Arbetsförmedlingens medicinska utlåtande")
                .withSummaryText("Lorem ipsum")
                .withLeftMarginTypText("AF00213")
                .withUtfardarLogotyp(logoData)
                .withApplicationOrigin(ApplicationOrigin.WEBCERT)
                .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/af00213-no-summary.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAf00213OvrigtEjAngivet() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("af00213/intyg.af00213-noovrigt.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("af00213/af00213-uv-viewmodel.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("af00213/texterMU_AF00213_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("af-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(UUID.randomUUID().toString())
                .withIntygsNamn("Arbetsförmedlingens medicinska utlåtande")
                .withIntygsKod("AF00213")
                .withPersonnummer(PNR)
                .withInfoText(INFO_TEXT_AF)
                .withHasSummaryPage(true)
                .withSummaryHeader("Arbetsförmedlingens medicinska utlåtande")
                .withSummaryText("Lorem ipsum")
                .withLeftMarginTypText("AF00213")
                .withUtfardarLogotyp(logoData)
                .withApplicationOrigin(ApplicationOrigin.WEBCERT)
                .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/af00213-noovrigt.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAf00213Tecken() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("af00213/intyg.af00213-tecken.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("af00213/af00213-uv-viewmodel.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("af00213/texterMU_AF00213_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("af-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(UUID.randomUUID().toString())
                .withIntygsNamn("Arbetsförmedlingens medicinska utlåtande")
                .withIntygsKod("AF00213")
                .withPersonnummer(PNR)
                .withInfoText(INFO_TEXT_AF)
                .withHasSummaryPage(true)
                .withSummaryHeader("Arbetsförmedlingens medicinska utlåtande")
                .withSummaryText("Lorem ipsum")
                .withLeftMarginTypText("AF00213")
                .withUtfardarLogotyp(logoData)
                .withApplicationOrigin(ApplicationOrigin.WEBCERT)
                .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/af00213-tecken.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAf00213LangaSvar() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("af00213/intyg.af00213-langasvar.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("af00213/af00213-uv-viewmodel.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("af00213/texterMU_AF00213_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("af-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(UUID.randomUUID().toString())
                .withIntygsNamn("Arbetsförmedlingens medicinska utlåtande")
                .withIntygsKod("AF00213")
                .withPersonnummer(PNR)
                .withInfoText(INFO_TEXT_AF)
                .withHasSummaryPage(true)
                .withSummaryHeader("Arbetsförmedlingens medicinska utlåtande")
                .withSummaryText("Lorem ipsum")
                .withLeftMarginTypText("AF00213")
                .withUtfardarLogotyp(logoData)
                .withApplicationOrigin(ApplicationOrigin.WEBCERT)
                .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/af00213-langasvar.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAf00213Makulerad() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("af00213/intyg.af00213.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("af00213/af00213-uv-viewmodel.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("af00213/texterMU_AF00213_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("af-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
                .withIntygJsonModel(cleanedJson)
                .withUpJsModel(upJsModel)
                .withIntygsId(UUID.randomUUID().toString())
                .withIntygsNamn("Arbetsförmedlingens medicinska utlåtande")
                .withIntygsKod("AF00213")
                .withPersonnummer(PNR)
                .withInfoText(INFO_TEXT_AF)
                .withSummaryHeader("Arbetsförmedlingens medicinska utlåtande")
                .withSummaryText("Lorem ipsum")
                .withLeftMarginTypText("AF00213")
                .withUtfardarLogotyp(logoData)
                .withIsMakulerad(true)
                .withApplicationOrigin(ApplicationOrigin.WEBCERT)
                .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/af00213-makulerad.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonNode loadAndCleanIntygJson(String intygJsonFile) throws IOException {
        InputStream inputStream = loadJsonModel(intygJsonFile);
        String jsonModel = IOUtils.toString(inputStream, Charset.forName("UTF-8"));
        return new ObjectMapper().readTree(jsonModel);
    }

    private IntygTexts loadTexts(String fileName) {

        try {
            Document e = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new ClassPathResource(fileName).getInputStream());
            Element root = e.getDocumentElement();
            String version = root.getAttribute("version");
            String intygsTyp = root.getAttribute("typ").toLowerCase();

            SortedMap texts = getTexter(root);

            Properties prop = new Properties();
            prop.putAll(ImmutableMap.of("formId", "FK 7802 (001 F 001) Fastställd av Försäkringskassan", "blankettId", "7802",
                    "blankettVersion", "01"));

            return new IntygTexts(version, intygsTyp, null, null, texts, null, prop);
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
    }

    private SortedMap<String, String> getTexter(Element element) {
        SortedMap<String, String> texts = new TreeMap<>();
        NodeList textsList = element.getElementsByTagName("text");
        for (int i = 0; i < textsList.getLength(); i++) {
            Element textElement = (Element) textsList.item(i);
            texts.put(textElement.getAttribute("id"), textElement.getTextContent());
        }
        return texts;
    }

    private InputStream loadJsonModel(String intygJsonFile) {
        ClassPathResource classPathResource = new ClassPathResource(intygJsonFile);
        try {
            InputStream inputStream = classPathResource.getInputStream();
            return inputStream;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
