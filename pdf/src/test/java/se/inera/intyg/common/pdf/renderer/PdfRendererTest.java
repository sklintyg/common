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
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import se.inera.intyg.common.pdf.LayoutComponent;
import se.inera.intyg.common.services.texts.model.IntygTexts;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

public class PdfRendererTest {

    private PdfRenderer testee;

    private JsonNode loadJsonModel() {
        ClassPathResource classPathResource = new ClassPathResource("luseModel.json");
        try {
            InputStream inputStream = classPathResource.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Test
    public void testPdfRenderer() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("luse.json");
        InputStream inputStream = classPathResource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        LayoutComponent[] components = objectMapper.readValue(inputStream, LayoutComponent[].class);

        testee = new PdfRenderer(loadJsonModel(), loadTexts(), Arrays.asList(components));

        byte[] data = testee.startRender();

        try (FileOutputStream fos = new FileOutputStream("luse-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IntygTexts loadTexts() {

        try {
            Document e = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new ClassPathResource("texterMU_LUSE_v1.0.xml").getInputStream());
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
}
