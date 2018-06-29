package se.inera.intyg.common.pdf.renderer;

import com.fasterxml.jackson.databind.JsonNode;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.pdf.UPComponent;
import se.inera.intyg.common.services.texts.model.IntygTexts;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

// CHECKSTYLE:OFF MagicNumber
public final class UnifiedRenderer {

    private static final float KATEGORI_FONT_SIZE = 14f;
    private static final float FRAGA_DELFRAGA_FONT_SIZE = 12f;
    private static final float SVAR_FONT_SIZE = 12f;

    private Color white = new DeviceRgb(255, 255, 255);
    private Color black = new DeviceRgb(0, 0, 0);
    private Color ineraBlue = new DeviceRgb(67, 121, 154);

    private Color wcColor07 = new DeviceRgb(33, 33, 33);
    private Color wcColor09 = new DeviceRgb(106, 106, 106);

    private PdfFont kategoriFont;
    private PdfFont fragaDelFragaFont;
    private PdfFont svarFont;

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedRenderer.class);

    private Document document;
    private ScriptObjectMirror jsIntygModel;
    private IntygTexts intygTexts;
    private JsonNode jsonModel;

    private UnifiedRenderer() {

    }

    public static UnifiedRenderer getInstance() {
        return new UnifiedRenderer();
    }

    public byte[] startRendering(String intygJsonModel, String upJsModel, IntygTexts intygTexts, JsonNode jsonModel) {
        this.intygTexts = intygTexts;
        this.jsonModel = jsonModel;

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
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

            // Parse JSON intyg into JS object
            jsIntygModel = (ScriptObjectMirror) engine.eval("JSON.parse('" + intygJsonModel + "');");
            engine.put("jsIntygModel", jsIntygModel);
            // Load unified print JS model
            InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(upJsModel, Charset.forName("UTF-8")));
            engine.eval(inputStreamReader);
            ScriptObjectMirror viewConfig = (ScriptObjectMirror) engine.eval("viewConfig");

            // Fix all standard stuff...

            // Start traversing the jsIntygModel.
            if (!viewConfig.getClassName().equalsIgnoreCase("Array")) {
                throw new IllegalArgumentException("Root viewConfig must be of type Array");
            }

            UPComponent root = new ComponentTreeFactory(engine, jsIntygModel).decorateWithComponentTree(viewConfig);
            Div rootDiv = new Div();
            render(root.getComponents(), rootDiv);

            document.add(rootDiv);
            document.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // CHECKSTYLE:OFF MethodLength
    public void render(List<UPComponent> components, Div parent) {

        for (int index = 0; index < components.size(); index++) {
            UPComponent component = components.get(index);
            // Each component goes into a new div.
            Div div = new Div();

            // Start render component
            switch (component.getType()) {
            case "uv-kategori":
                renderKategori(component, div);
                break;
            case "uv-fraga":
                renderUvFraga(component, div);
                break;
            case "uv-del-fraga":
                renderUvDelfraga(component, div);
                break;
            case "uv-simple-value":
                String value = getSimpleValue(component.getModelProp().get(0));
                renderUvSimpleValue(div, value);
                break;
            case "uv-kodverk-value":
                renderUvKodverkValue(component, div);
                break;
            case "uv-boolean-statement":
            case "uv-boolean-value":
                renderUvBoolean(component, div);
                break;
            case "uv-table":
                renderUvTable(component, div);
                break;
            case "uv-list":
                renderUvList(component, div);
                break;
            case "uv-alert-value":
                break;
            case "uv-skapad-av":
                //renderUvSkapadAv(component, div);
                break;
            case "uv-tillaggsfragor":
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + component.getType());
            }

            // Recurse into the graph
            render(component.getComponents(), div);

            // Check if DIV will fit on page?

            // After coming back from a recurse, check if we should do any
            // post-render stuff before continuing.
            parent.add(div);
            if (component.getType().equals("uv-kategori")) {
                // Add a spacer to the parent div _after_
                parent.add(new Div().setMarginTop(16f));
            }
        }
    }

    private void renderKategori(UPComponent component, Div div) {
        String kategori = getText(component.getLabelKey());

        Div borderDiv = new Div();
        div.setBorder(new SolidBorder(black, 0.5f));

        // Setting -0.5f padding makes sure the borders are drawn on top of each other... ugly.
        div.setPadding(-0.5f);

        borderDiv.add(new Paragraph(kategori.toUpperCase())
                .setMarginTop(0f)
                .setMarginBottom(0f)
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(kategoriFont)
                .setFontSize(KATEGORI_FONT_SIZE)
                .setFontColor(wcColor07)
                .setBackgroundColor(white));
        borderDiv.setBorder(new SolidBorder(black, 0.5f));
        borderDiv.setKeepTogether(true);
        div.add(borderDiv);
    }


    private void renderUvFraga(UPComponent component, Div div) {
        String fraga = getText(component.getLabelKey());
        div.add(new Paragraph(fraga)
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(fragaDelFragaFont)
                .setFontColor(wcColor07)
                .setFontSize(FRAGA_DELFRAGA_FONT_SIZE)
        );
    }

    private void renderUvDelfraga(UPComponent component, Div div) {
        if (component.isRender()) {
            String delFraga = getText(component.getLabelKey());
            if (delFraga != null) {
                div.add(new Paragraph(delFraga)
                        .setMarginRight(millimetersToPoints(10f))
                        .setMarginLeft(millimetersToPoints(5f))
                        .setFont(fragaDelFragaFont)
                        .setFontColor(wcColor09)
                        .setFontSize(FRAGA_DELFRAGA_FONT_SIZE)
                        .setPadding(0f).setMarginTop(0f)
                        .setMarginBottom(0f));
            }
        }
    }

    private void renderUvKodverkValue(UPComponent component, Div div) {

        List<String> kvParts = new ArrayList<>();
        for (int a = 0; a < component.getKvLabelKeys().size(); a++) {
            String currentModelProp = component.getKvModelProps().get(a);
            String currentLabelKey = component.getKvLabelKeys().get(a);

            String nestedValue = getNestedValue(currentModelProp);
            String textKey = currentLabelKey.replaceAll("\\{var\\}", nestedValue);
            String value = getText(textKey);
            kvParts.add(value);
        }

        div.add(new Paragraph(kvParts.stream().collect(Collectors.joining(" ")))
                .setItalic()
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(svarFont)
                .setFontSize(SVAR_FONT_SIZE)
                .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
    }

    private void renderUvList(UPComponent component, Div div) {
        // var == KV_FKMU_0002.ARBETSSOKANDE.RBK
        JsonNode jsonNode = null;
        String modelProp = component.getModelProp().get(0);
        if (modelProp.contains(".")) {
            String[] parts = modelProp.split("\\.");
            JsonNode currentNode = jsonModel;
            for (String part : parts) {
                currentNode = currentNode.get(part);
            }
            jsonNode = currentNode;
        } else {
            jsonNode = jsonModel.get(modelProp);
        }
        // JsonNode jsonNode = jsonModel.get(component.getModelProp().get(0)); // TODO FIX
        // Should always be array node
        if (jsonNode.isArray()) {

            com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
            list.setFont(svarFont)
                    .setFontSize(SVAR_FONT_SIZE).setPadding(0f).setMarginTop(0f).setMarginBottom(0f)
                    .setMarginRight(millimetersToPoints(10f))
                    .setMarginLeft(millimetersToPoints(5f));

            Iterator<JsonNode> elements = jsonNode.elements();
            //while (elements.hasNext()) {
            for (String textValue : component.getListKeyResults()) {
                String textKey = component.getLabelKey().replaceAll("\\{var\\}", textValue);
                String listItemText = getText(textKey);

                ListItem listItem = new ListItem(listItemText);
                list.add(listItem);
            }
            div.add(list);
        }

    }

    private void renderUvSkapadAv(UPComponent component, Div div) {
        /*
         * Intyget är utfärdat och signerat av:
         * Karin Persson
         * Tel: 054203408
         * nmt_vg1_ve1, nmt_vg1
         * Bryggaregatan 11, 65340 Karlstad
         */
        div.setMarginTop(0f)
                .setMarginBottom(0f)
                .setPadding(0)

                .setBold()
                .setFontSize(10f)
                .setFontColor(white)
                .setBackgroundColor(ineraBlue);

        div.add(new Paragraph("Intyget är utfärdat och signerat av:"));
        Table skapadAv = new Table(new float[] { 12f, 12f });
        skapadAv.setWidth(400f);
        skapadAv.addHeaderCell(
                new Cell().setPadding(0f)
                        .setBorder(Border.NO_BORDER)
                        .add(
                                new Paragraph(getNestedValue(component.getModelProp() + ".fullstandigtNamn")).setFontSize(10f)
                                        .setBold()));
        skapadAv.addHeaderCell(
                new Cell().setPadding(0f)
                        .setBorder(Border.NO_BORDER)
                        .add(
                                new Paragraph(getNestedValue(component.getModelProp() + ".vardenhet.enhetsnamn") + " "
                                        + getNestedValue(component.getModelProp() + ".vardenhet.vardgivare.vardgivarnamn"))
                                                .setFontSize(10f).setBold()));

        skapadAv.addCell(
                new Cell().setPadding(0f).setBorder(Border.NO_BORDER).add(
                        new Paragraph("Tel: " + getNestedValue(component.getModelProp() + ".vardenhet.telefonnummer"))
                                .setFontSize(10f)));

        skapadAv.addCell(
                new Cell().setPadding(0f).setBorder(Border.NO_BORDER).add(
                        new Paragraph(getNestedValue(component.getModelProp() + ".vardenhet.postadress") + ", "
                                + getNestedValue(component.getModelProp() + ".vardenhet.postnummer") + " "
                                + getNestedValue(component.getModelProp() + ".vardenhet.postort")).setFontSize(10f)));

        div.add(skapadAv);
    }

    private void renderUvTable(UPComponent component, Div div) {
        int cols = component.getHeaders().size();
        float[] colsWidths = new float[cols];
        for (int a = 0; a < cols; a++) {
            colsWidths[a] = 12f;
        }

        Table table = new Table(colsWidths).setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f));

        table.setWidth(millimetersToPoints(160));

        // Render headers with tabs
        for (String header : component.getHeaders()) {
            if (header.endsWith(".RBK")) {
                table.addHeaderCell(
                        new Cell()
                                .setBorder(Border.NO_BORDER)
                                .setBorderBottom(new SolidBorder(0.5f))
                                .add(
                                        new Paragraph(getText(header))
                                                .setFont(fragaDelFragaFont)
                                                .setFontSize(FRAGA_DELFRAGA_FONT_SIZE)));
            } else {
                table.addHeaderCell(
                        new Cell().setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)).add(
                                new Paragraph(header))
                                .setFont(fragaDelFragaFont)
                                .setFontSize(FRAGA_DELFRAGA_FONT_SIZE));
            }
        }

        JsonNode values = jsonModel.get(component.getModelProp().get(0));
        for (JsonNode rowValue : values) {
            for (String valueProp : component.getValueProps()) {

                if (valueProp.contains("{") && valueProp.contains("}")) {
                    // Extract ugly way.
                    String prop = valueProp.substring(valueProp.indexOf("{") + 1, valueProp.indexOf("}"));
                    JsonNode jsonNode = rowValue.get(prop);
                    String s = valueProp.replaceAll("\\{" + prop + "\\}", jsonNode.textValue());
                    String someText = getText(s);
                    table.addCell(
                            new Cell().setBorder(Border.NO_BORDER).add(
                                    new Paragraph(someText)
                                            .setFont(svarFont)
                                            .setFontSize(SVAR_FONT_SIZE)));

                } else {
                    if (valueProp.contains(".")) {
                        String resolvedText = getNestedValue(rowValue, valueProp);

                        table.addCell(
                                new Cell().setBorder(Border.NO_BORDER).add(
                                        new Paragraph(resolvedText)
                                                .setFont(svarFont)
                                                .setFontSize(SVAR_FONT_SIZE)));
                    } else {
                        table.addCell(
                                new Cell().setBorder(Border.NO_BORDER).add(
                                        new Paragraph(rowValue.get(valueProp).textValue())
                                                .setFont(svarFont)
                                                .setFontSize(SVAR_FONT_SIZE)));
                    }

                }

            }

        }
        div.add(table);
    }

    private void renderUvBoolean(UPComponent component, Div div) {
        String booleanValue = getBooleanValue(component.getModelProp().get(0));
        div.add(new Paragraph(booleanValue)
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(svarFont)
                .setFontSize(SVAR_FONT_SIZE));
    }

    private void renderUvSimpleValue(Div div, String value) {
        div.add(new Paragraph(value).setItalic()
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(svarFont)
                .setFontSize(SVAR_FONT_SIZE)
                .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
    }








    private String getText(String labelKey) {
        try {
            return intygTexts.getTexter().get(labelKey);
        } catch (Exception e) {
            return "";
        }
    }

    private boolean hasValue(String cleanedExpression) {
        return jsonModel.get(cleanedExpression) != null && !jsonModel.get(cleanedExpression).textValue().isEmpty();
    }

    private String getBooleanValue(String modelProp) {
        JsonNode valueNode = jsonModel.get(modelProp);
        if (valueNode != null) {
            return valueNode.booleanValue() ? "Ja" : "Nej";
        }
        return "";
    }

    private String getSimpleValue(String modelProp) {
        JsonNode valueNode = jsonModel.get(modelProp);
        return valueNode != null ? valueNode.textValue() : "Ej angivet";
    }

    private String getNestedValue(String modelProp) {
        // Split by dots.
        if (!modelProp.contains(".")) {
            return getSimpleValue(modelProp);
        }
        return getNestedValue(jsonModel, modelProp);
    }

    private String getNestedValue(JsonNode rowValue, String valueProp) {
        String resolvedText = "";
        String[] parts = valueProp.split("\\.");
        JsonNode currentNode = rowValue;
        for (String part : parts) {
            currentNode = currentNode.get(part);
        }
        if (currentNode == null) {
            throw new IllegalStateException("Could not resolve property: " + valueProp);
        }
        if (currentNode.isBoolean()) {
            return currentNode.booleanValue() ? "Ja" : "Nej";
        }
        if (currentNode.isBoolean()) {
            resolvedText = currentNode.booleanValue() ? "Ja" : "Nej";
        }
        if (currentNode.isTextual()) {
            resolvedText = currentNode.textValue();
        }
        return resolvedText;
    }

    private PdfFont loadFont(String name) {
        try {
            byte[] fontData = IOUtils.toByteArray(new ClassPathResource(name).getInputStream());
            return PdfFontFactory.createFont(fontData, "Winansi", true); // Cp1250
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load font: " + e.getMessage());
        }
    }

}
// CHECKSTYLE:ON MagicNumber
