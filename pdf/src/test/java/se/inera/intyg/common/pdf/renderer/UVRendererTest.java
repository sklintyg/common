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

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

public class UVRendererTest {

    @Test
    public void testTsBas() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("tsbas/intyg.tsbas.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("tsbas/tsbas-up.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("tsbas/texterTS_TSTRK_1007_v6.8.xml");

        byte[] data = new UVRenderer().startRendering(cleanedJson, upJsModel, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("tsbas-generic.pdf")) {
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

        byte[] data = new UVRenderer().startRendering(cleanedJson, upJsModel, intygTexts);
        try (FileOutputStream fos = new FileOutputStream("lisjp-generic.pdf")) {
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
