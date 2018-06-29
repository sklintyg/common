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
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import se.inera.intyg.common.services.texts.model.IntygTexts;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

public class UnifiedPrintModelReaderTest {

    private final static Logger LOG = LoggerFactory.getLogger(UnifiedPrintModelReaderTest.class);

    private ScriptEngine engine = null;
    private ScriptObjectMirror jsIntygModel;

    private UnifiedRenderer unifiedRenderer = UnifiedRenderer.getInstance();

    @Test
    public void testRenderUnifiedLijsp() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("intyg.lisjp.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("lisjp-up.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("texterMU_LISJP_v1.0.xml");

        byte[] data = unifiedRenderer.startRendering(cleanedJson, upJsModel, intygTexts, intygJsonNode);
        try (FileOutputStream fos = new FileOutputStream("lisjp-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRenderUnifiedTsBas() throws IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("intyg.tsbas.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        ClassPathResource cpr = new ClassPathResource("tsbas-up.js");
        String upJsModel = IOUtils.toString(cpr.getInputStream(), Charset.forName("UTF-8"));

        IntygTexts intygTexts = loadTexts("texterTS_TSTRK_1007_v6.8.xml");

        byte[] data = unifiedRenderer.startRendering(cleanedJson, upJsModel, intygTexts, intygJsonNode);
        try (FileOutputStream fos = new FileOutputStream("tsbas-generic.pdf")) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Test
    public void readModel() throws ScriptException, IOException {
        JsonNode intygJsonNode = loadAndCleanIntygJson("intyg.lisjp.json");
        String cleanedJson = new ObjectMapper().writeValueAsString(intygJsonNode);

        engine = new ScriptEngineManager().getEngineByName("nashorn");

        jsIntygModel = (ScriptObjectMirror) engine.eval("JSON.parse('" + cleanedJson + "');");

        ClassPathResource cpr = new ClassPathResource("lisjp-up.js");
        InputStreamReader inputStreamReader = new InputStreamReader(cpr.getInputStream());
        engine.eval(inputStreamReader);

        Object viewConfig = engine.eval("viewConfig");
        dumpTree((ScriptObjectMirror) viewConfig);
    }

    private JsonNode loadAndCleanIntygJson(String intygJsonFile) throws IOException {
        InputStream inputStream = loadJsonModel(intygJsonFile);
        String jsonModel = IOUtils.toString(inputStream, Charset.forName("UTF-8"));
        return new ObjectMapper().readTree(jsonModel);
    }


    private int indent = 0;

    private void dumpTree(ScriptObjectMirror mirror) {
        indent+=4;
        if (mirror.getClassName().equals("Array")) {
            mirror.entrySet().stream().forEach(entry -> {
                if (entry.getValue() instanceof ScriptObjectMirror) {
                    dumpTree((ScriptObjectMirror) entry.getValue());
                } else {
                    log(entry.getKey() + " => " + entry.getValue());
                }
            });
        }
        if (mirror.getClassName().equals("Object")) {

            mirror.entrySet().stream().forEach(entry -> {

                if (entry.getValue() instanceof ScriptObjectMirror) {
                    ScriptObjectMirror value = (ScriptObjectMirror) entry.getValue();
                    if (value.getClassName().equalsIgnoreCase("Function")) {
                        LOG.info("FOUND FUNC");
                        Object res = value.call(null, jsIntygModel);
                        boolean isTrue = isTrue(res);
                    } else {
                        dumpTree((ScriptObjectMirror) entry.getValue());
                    }
                } else {
                    log(entry.getKey() + " => " + entry.getValue());
                }

            });

        }
        indent-=4;
    }

    private boolean isTrue(Object res) {
        if (res instanceof Undefined) {
            return false;
        }
        if (res instanceof Boolean) {
            return ((Boolean) res).booleanValue();
        }
        return false;
    }

    private void log(String msg) {
        StringBuilder buf = new StringBuilder();
        for (int a = 0; a < indent; a++) {
            buf.append(" ");
        }
        buf.append(msg);
        LOG.info(buf.toString());
    }

    private void printObjectMirror(ScriptObjectMirror mirror) {
        LOG.info(mirror.getClassName() + ": " + Arrays.toString(mirror.getOwnKeys(true)));
        mirror.entrySet().stream().forEach( entry -> printObjectMirror((ScriptObjectMirror) entry.getValue()));
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


