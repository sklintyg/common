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
package se.inera.intyg.common.pdf.renderer;

import static se.inera.intyg.common.pdf.renderer.PrintConfig.UTSK001_BODY;
import static se.inera.intyg.common.pdf.renderer.PrintConfig.UTSK001_HEADER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import se.inera.intyg.common.pdf.model.Summary;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.services.BefattningService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class UVRendererTest {

    private static final String PNR = "19121212-1212";

    private static final String INFO_TEXT_TS =
        "Detta är en utskrift av ett elektroniskt intyg. Intyget har signerats elektroniskt av intygsutfärdaren. Notera att intyget "
            + "redan har skickats till Transportstyrelsen.";
    private static final String INFO_TEXT_AG =
        "Detta är en utskrift av ett elektroniskt intyg. Intyget har signerats elektroniskt av intygsutfärdaren. Notera att intyget "
            + "redan har skickats till Transportstyrelsen.";
    private static final String INFO_TEXT_FK =
        "Detta är en utskrift av ett elektroniskt intyg. Intyget har signerats elektroniskt av intygsutfärdaren. Notera att intyget "
            + "redan har skickats till Försäkringskassan.";
    private static final String INFO_TEXT_AF = "Detta är en utskrift av ett elektroniskt intyg.";

    @Test
    public void testMinimalAg7804() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("ag7804/ag7804-minimal.v1.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("ag7804/ag7804-uv-viewmodel.v1.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("ag7804/texterMU_AG7804_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("skl_logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("Intygsnamnet")
            .withIntygsKod("AG7804")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_AG)
            .withSummary(new Summary().add("Lite om intyget", "Lorem ipsum").add(UTSK001_HEADER, UTSK001_BODY))
            .withLeftMarginTypText("AG7804 vänstermaginaltext")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.WEBCERT)
            .withSignBox(true)
            .withSignatureLine(true)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/ag7804-minimal-v1.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMaximalAg7804() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("ag7804/ag7804-maximal.v1.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("ag7804/ag7804-uv-viewmodel.v1.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("ag7804/texterMU_AG7804_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("skl_logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("Intygsnamnet")
            .withIntygsKod("AG7804")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_AG)
            .withSummary(new Summary().add("Lite om intyget", "Lorem ipsum").add(UTSK001_HEADER, UTSK001_BODY))
            .withLeftMarginTypText("AG7804 vänstermaginaltext")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.WEBCERT)
            .withSignBox(true)
            .withSignatureLine(true)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/ag7804-maximal-v1.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMaximalOptionalfieldsAg7804() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("ag7804/ag7804-maximal.v1.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("ag7804/ag7804-uv-viewmodel.v1.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("ag7804/texterMU_AG7804_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("skl_logo.png").getInputStream());
        Map<String, String> replacementConfig = new HashMap<>();
        replacementConfig.put("diagnoser", "Denna text är istället för diagnoserna!");
        replacementConfig.put("onskarFormedlaDiagnos", "Denna text är istället för diagnoserna!");
        replacementConfig.put("funktionsnedsattning", "Denna text är istället för funktionsnedsattning!");
        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("Intygsnamnet")
            .withIntygsKod("AG7804")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_AG)
            .withSummary(new Summary().add("Lite om intyget", "Lorem ipsum").add(UTSK001_HEADER, UTSK001_BODY))
            .withLeftMarginTypText("AG7804 vänstermaginaltext")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.WEBCERT)
            .withSignBox(true)
            .withSignatureLine(true)
            .withModelPropReplacements(replacementConfig)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/ag7804-maximal-optionalfields-v1.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testAg114TomtUtkast() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("ag114/ag114-pdf-utkast.v1.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("ag114/ag1-14-uv-viewmodel.v1.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("ag114/pdftestMU_AG114_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("skl_logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("Intygsnamnet (AG1-14)")
            .withIntygsKod("AG114")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_AG)
            .withSummary(new Summary().add("Lite om intyget", "Lorem ipsum").add(UTSK001_HEADER, UTSK001_BODY))
            .withLeftMarginTypText("AG1-14")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.WEBCERT)
            .withSignBox(true)
            .withSignatureLine(true)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/ag1-14v1-generic-tomt-utkast.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAg114() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("ag114/ag114-pdf.v1.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("ag114/ag1-14-uv-viewmodel.v1.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("ag114/pdftestMU_AG114_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("skl_logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("Intygsnamnet (AG1-14)")
            .withIntygsKod("AG114")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_AG)
            .withSummary(new Summary().add("Lite om intyget", "Lorem ipsum").add(UTSK001_HEADER, UTSK001_BODY))
            .withLeftMarginTypText("AG1-14")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.WEBCERT)
            .withSignBox(true)
            .withSignatureLine(true)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/ag1-14v1-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAg114EmployerPdfWithoutDiagnose() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("ag114/ag114-pdf.v1.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("ag114/ag1-14-uv-viewmodel.v1.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("ag114/pdftestMU_AG114_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("skl_logo.png").getInputStream());

        Map<String, String> replacementConfig = new HashMap<>();
        replacementConfig.put("diagnoser", "Denna text är istället för diagnoserna!");
        replacementConfig.put("onskarFormedlaDiagnos", "Denna text är istället för diagnoserna!");
        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("Intygsnamnet (AG1-14)")
            .withIntygsKod("AG114")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_AG)
            .withSummary(new Summary().add("Lite om intyget", "Lorem ipsum").add(UTSK001_HEADER, UTSK001_BODY))
            .withLeftMarginTypText("AG1-14")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.MINA_INTYG)
            .withSignBox(true)
            .withSignatureLine(true)
            .withModelPropReplacements(replacementConfig)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/ag1-14v1-employer-no-diagnose.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTsDiabetes() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("tsdiabetes/intyg.tsdiabetes.v3.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("tsdiabetes/ts-up.v3.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("tsdiabetes/texterTS_TSTRK_1031_v3.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("transportstyrelsen-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("Transportstyrelsens läkarintyg")
            .withIntygsKod("TSTRK1031")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_TS)
            .withSummary(new Summary().add("Om Transportstyrelsens läkarintyg diabetes", "Lorem ipsum").add(UTSK001_HEADER, UTSK001_BODY))
            .withLeftMarginTypText("TSTRK1031 (U03) 181024")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.MINA_INTYG)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/tsdiabetesv3-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyTsDiabetes() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("tsdiabetes/empty.tsdiabetes.v3.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("tsdiabetes/ts-up.v3.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("tsdiabetes/texterTS_TSTRK_1031_v3.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("transportstyrelsen-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("Transportstyrelsens läkarintyg")
            .withIntygsKod("TSTRK1031")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_TS)
            .withSummary(new Summary().add("Om Transportstyrelsens läkarintyg diabetes", "Lorem ipsum"))
            .withLeftMarginTypText("TSTRK1031 (U03) 181024")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.WEBCERT)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/tsdiabetesv3-empty-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            .withSummary(new Summary().add("Om Transportstyrelsens läkarintyg", "Lorem ipsum"))
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
    public void testEmptyTsBasUtkast() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("tsbas/empty.tsbas.json");
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
            .withSummary(new Summary().add("Om Transportstyrelsens läkarintyg", "Lorem ipsum"))
            .withLeftMarginTypText("TSTRK1007 (U08) 160114")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.WEBCERT)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/tsbas-empty.pdf")) {
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
            .withIntygsKod("FK7804")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_FK)
            .withSummary(new Summary().add("Om försäkringskassans läkarintyg",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."))
            .withLeftMarginTypText("FK7804 (001 F 001) Fastst\u00E4lld av F\u00F6rs\u00E4kringskassan")
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
            .withSummary(new Summary().add("Arbetsförmedlingens medicinska utlåtande", "Lorem ipsum"))
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
            .withSummary(new Summary().add("Arbetsförmedlingens medicinska utlåtande", "Lorem ipsum"))
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
            .withSummary(new Summary().add("Arbetsförmedlingens medicinska utlåtande", "Lorem ipsum"))
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
            .withSummary(new Summary().add("Arbetsförmedlingens medicinska utlåtande", "Lorem ipsum"))
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

    @Test
    public void testTstrk1009() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("tstrk1009/tstrk1009.v1.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("tstrk1009/tstrk1009-uv-viewmodel.v1.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("tstrk1009/texterTS_TSTRK_1009_v1.0.xml");
        byte[] logoData = IOUtils.toByteArray(new ClassPathResource("transportstyrelsen-logo.png").getInputStream());

        PrintConfig printConfig = PrintConfig.PrintConfigBuilder.aPrintConfig()
            .withIntygJsonModel(cleanedJson)
            .withUpJsModel(upJsModel)
            .withIntygsId(UUID.randomUUID().toString())
            .withIntygsNamn("TSTRRK1009 namn")
            .withIntygsKod("TSTRK1009")
            .withPersonnummer(PNR)
            .withInfoText(INFO_TEXT_AF)
            .withLeftMarginTypText("TSTRK1009 Left side text")
            .withUtfardarLogotyp(logoData)
            .withApplicationOrigin(ApplicationOrigin.WEBCERT)
            .build();

        byte[] data = new UVRenderer().startRendering(printConfig, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("build/tmp/tstrk1009.pdf")) {
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
