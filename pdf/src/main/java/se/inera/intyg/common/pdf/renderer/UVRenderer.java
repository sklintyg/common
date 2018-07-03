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

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.pdf.eventhandler.IntygFooter;
import se.inera.intyg.common.pdf.eventhandler.IntygHeader;
import se.inera.intyg.common.pdf.eventhandler.MarginTexts;
import se.inera.intyg.common.pdf.eventhandler.PageNumberEvent;
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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import static se.inera.intyg.common.pdf.model.UVComponent.FRAGA_DELFRAGA_FONT_SIZE;
import static se.inera.intyg.common.pdf.model.UVComponent.SVAR_FONT_SIZE;
import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

/**
 * Renders PDFs using iText7 based on the uv view configs.
 */
// CHECKSTYLE:OFF MagicNumber
public class UVRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(UVRenderer.class);
    public static final float TOP_MARGIN_SUMMARY_PAGE = millimetersToPoints(42f);

    public static final Color WC_COLOR_11 = new DeviceRgb(0xDA, 0x44, 0x53);

    // In millimeters
    public static final float PAGE_MARGIN_LEFT = 20f;
    public static final float PAGE_MARGIN_BOTTOM = 40f;
    public static final float PAGE_MARGIN_TOP = 58f;
    private static final float MARGIN_BETWEEN_KATEGORIER = 5f;

    public PdfFont kategoriFont;
    public PdfFont fragaDelFragaFont;
    public PdfFont svarFont;

    private ScriptObjectMirror jsIntygModel;
    private IntygTexts intygTexts;

    private ScriptEngine engine;

    private PdfImageXObject observandumIcon;

    public byte[] startRendering(PrintConfig printConfig, IntygTexts intygTexts) {
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

            // Load icon for observandum
            this.observandumIcon = new PdfImageXObject(
                    ImageDataFactory.create(IOUtils.toByteArray(new ClassPathResource("obs-icon.png").getInputStream())));

            // Initialize event handlers for header, footer etc.
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE,
                    new IntygHeader(printConfig, kategoriFont, fragaDelFragaFont, svarFont));
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE,
                    new IntygFooter(svarFont));
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE,
                    new MarginTexts(printConfig, svarFont));

            PageNumberEvent pageNumberEvent = new PageNumberEvent(svarFont);
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE,
                    pageNumberEvent);

            // Initialize document
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(millimetersToPoints(PAGE_MARGIN_TOP), millimetersToPoints(PAGE_MARGIN_LEFT),
                    millimetersToPoints(PAGE_MARGIN_BOTTOM), millimetersToPoints(PAGE_MARGIN_LEFT));

            // Initialize script engine
            engine = new ScriptEngineManager().getEngineByName("nashorn");

            // Bind the $filter function and other custom functions declared in uvViewConfig.
            engine.eval(new InputStreamReader(new ClassPathResource("customfilter.js").getInputStream(), Charset.forName("UTF-8")));

            // Parse JSON intyg into JS object
            jsIntygModel = (ScriptObjectMirror) engine.eval("JSON.parse('" + printConfig.getIntygJsonModel() + "');");
            engine.put("jsIntygModel", jsIntygModel);

            // Load unified print JS model
            InputStreamReader inputStreamReader = new InputStreamReader(
                    IOUtils.toInputStream(printConfig.getUpJsModel()), Charset.forName("UTF-8"));
            engine.eval(inputStreamReader);
            ScriptObjectMirror viewConfig = (ScriptObjectMirror) engine.eval("viewConfig");
            engine.put("viewConfig", viewConfig);

            Div rootDiv = new Div();
            for (Object o : viewConfig.values()) {
                render(rootDiv, (ScriptObjectMirror) o);
            }

            document.add(rootDiv);

            // Final page.
            renderSummaryPage(printConfig, document);

            pageNumberEvent.writeTotal(pdf);

            document.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void renderSummaryPage(PrintConfig printConfig, Document document) {
        document.setTopMargin(TOP_MARGIN_SUMMARY_PAGE);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        Div summaryDiv = new Div();
        summaryDiv.add(new Paragraph(printConfig.getSummaryHeader())
                .setMarginBottom(0f)
                .setFont(fragaDelFragaFont)
                .setFontSize(FRAGA_DELFRAGA_FONT_SIZE));
        summaryDiv.add(new Paragraph(printConfig.getSummaryText())
                .setMarginTop(0f)
                // .setMarginLeft(millimetersToPoints(PAGE_MARGIN_LEFT))
                .setFont(svarFont)
                .setFontSize(SVAR_FONT_SIZE));
        document.add(summaryDiv);
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
            rootDiv.add(new Div().setMarginTop(millimetersToPoints(MARGIN_BETWEEN_KATEGORIER)));
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

    /**
     * Resolves a text from the intygTexts.
     */
    public String getText(String labelKey) {
        try {
            return intygTexts.getTexter().get(labelKey);
        } catch (Exception e) {
            return null; // "missing key: " + labelKey;
        }
    }

    /**
     * Resolves a value from the jsIntygModel already bound to the Nashorn engine.
     */
    public Object evalValueFromModel(String modelProp) {
        try {
            return engine.eval("jsIntygModel." + modelProp);
        } catch (ScriptException e) {
            return null;
        }
    }

    /**
     * Uses nashorn eval(..) to resolve the value of a model property in the given ScriptObjectMirror model.
     *
     * Works by binding the supplied model to the engine context, executing the engine.eval on it and the removing
     * the model again.
     */
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

    public PdfImageXObject getObservandumIcon() {
        return this.observandumIcon;
    }
    // CHECKSTYLE:ON MagicNumber
}
