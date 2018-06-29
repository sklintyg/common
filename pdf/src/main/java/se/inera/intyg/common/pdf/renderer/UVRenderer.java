package se.inera.intyg.common.pdf.renderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.model.UVAlertValue;
import se.inera.intyg.common.pdf.model.UVBooleanValue;
import se.inera.intyg.common.pdf.model.UVDelfraga;
import se.inera.intyg.common.pdf.model.UVFraga;
import se.inera.intyg.common.pdf.model.UVKategori;
import se.inera.intyg.common.pdf.model.UVKodverkValue;
import se.inera.intyg.common.pdf.model.UVList;
import se.inera.intyg.common.pdf.model.UVSimpleValue;
import se.inera.intyg.common.pdf.model.UVSkapadAv;
import se.inera.intyg.common.pdf.model.UVTable;
import se.inera.intyg.common.services.texts.model.IntygTexts;

public class UVRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(UVRenderer.class);

    public PdfFont kategoriFont;
    public PdfFont fragaDelFragaFont;
    public PdfFont svarFont;

    private Document document;
    private ScriptObjectMirror jsIntygModel;
    private IntygTexts intygTexts;

    private ScriptEngine engine;

    public byte[] startRendering(String intygJsonModel, String upJsModel, IntygTexts intygTexts) {
        this.intygTexts = intygTexts;

        this.kategoriFont = loadFont("Roboto-Medium.woff2");
        this.fragaDelFragaFont = loadFont("Roboto-Medium.woff2");
        this.svarFont = loadFont("Roboto-Regular.woff2");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(bos);

            // Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            document = new Document(pdf, PageSize.A4);

            // Initialize script engine
            engine = new ScriptEngineManager().getEngineByName("nashorn");

            // Bind the $filter function
            engine.eval(new InputStreamReader(new ClassPathResource("customfilter.js").getInputStream()));

            // Parse JSON intyg into JS object
            jsIntygModel = (ScriptObjectMirror) engine.eval("JSON.parse('" + intygJsonModel + "');");
            engine.put("jsIntygModel", jsIntygModel);

            // Load unified print JS model
            InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(upJsModel, Charset.forName("UTF-8")));
            engine.eval(inputStreamReader);
            ScriptObjectMirror viewConfig = (ScriptObjectMirror) engine.eval("viewConfig");
            engine.put("viewConfig", viewConfig);

            Div rootDiv = new Div();
            for (Object o : viewConfig.values()) {
                render(rootDiv, (ScriptObjectMirror) o);
            }

            document.add(rootDiv);
            document.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ScriptObjectMirror getIntygModel() {
        return jsIntygModel;
    }

    private void render(Div rootDiv, ScriptObjectMirror currentUvNode) {

        Div currentDiv = new Div();
        String type = (String) currentUvNode.get("type");
        switch (type) {
        case "uv-kategori":
            new UVKategori(this)
                    .render(currentDiv, currentUvNode);
            break;
        case "uv-fraga":
            new UVFraga(this)
                    .render(currentDiv, currentUvNode);
            break;
        case "uv-del-fraga":
            new UVDelfraga(this)
                    .render(currentDiv, currentUvNode);
            break;
        case "uv-simple-value":
            new UVSimpleValue(this)
                    .render(currentDiv, currentUvNode);
            break;
        case "uv-kodverk-value":
            new UVKodverkValue(this).render(currentDiv, currentUvNode);
            break;
        case "uv-boolean-statement":
        case "uv-boolean-value":
            new UVBooleanValue(this).render(currentDiv, currentUvNode);
            break;
        case "uv-list":
            new UVList(this).render(currentDiv, currentUvNode);
            break;
        case "uv-table":
            new UVTable(this).render(currentDiv, currentUvNode);
            break;
        case "uv-alert-value":
            new UVAlertValue(this).render(currentDiv, currentUvNode);
            break;
        case "uv-skapad-av":
            new UVSkapadAv(this).render(currentDiv, currentUvNode);
            break;
        }

        // Recurse into sub-components
        if (currentUvNode.containsKey("components")) {
            Object components = currentUvNode.get("components");
            ScriptObjectMirror array = (ScriptObjectMirror) components;
            for (Map.Entry<String, Object> entry : array.entrySet()) {
                render(currentDiv, (ScriptObjectMirror) entry.getValue());
            }
        }
        rootDiv.add(currentDiv);
        if ("uv-kategori".equalsIgnoreCase(type)) {
            // Add a spacer to the parent div _after_
            rootDiv.add(new Div().setMarginTop(16f));
        }
    }

    private PdfFont loadFont(String name) {
        try {
            byte[] fontData = IOUtils.toByteArray(new ClassPathResource(name).getInputStream());
            return PdfFontFactory.createFont(fontData, "Winansi", true); // Cp1250
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load font: " + e.getMessage());
        }
    }

    public String getText(String labelKey) {
        try {
            return intygTexts.getTexter().get(labelKey);
        } catch (Exception e) {
            return null; // "missing key: " + labelKey;
        }
    }

    public Object eval(String modelProp) {
        try {
            return engine.eval("jsIntygModel." + modelProp);
        } catch (ScriptException e) {
            return null;
        }
    }

    public Object findInModel(ScriptObjectMirror model, String modelProp) {
        engine.put("model", model);
        try {
            Object result = engine.eval("model." + modelProp);
            engine.put("model", null);
            return result;
        } catch (ScriptException e) {
            return "ERROR";
        }
    }
}
