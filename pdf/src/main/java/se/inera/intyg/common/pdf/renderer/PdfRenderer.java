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
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.pdf.LayoutComponent;
import se.inera.intyg.common.services.texts.model.IntygTexts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 *
 */

// CHECKSTYLE:OFF MagicNumber
public class PdfRenderer {

    Color white = new DeviceRgb(255, 255, 255);
    Color black = new DeviceRgb(0, 0, 0);
    Color ineraBlue = new DeviceRgb(67, 121, 154);

    private JsonNode jsonModel;
    private IntygTexts intygTexts;
    private List<LayoutComponent> components;
    private Document document;

    public PdfRenderer(JsonNode jsonModel, IntygTexts intygTexts, List<LayoutComponent> components) {
        this.jsonModel = jsonModel;
        this.intygTexts = intygTexts;
        this.components = components;
    }

    public byte[] startRender() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(bos);

            // Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            document = new Document(pdf);

            ImageData imageData = ImageDataFactory.create(IOUtils.toByteArray(new ClassPathResource("Inera-Logo.png").getInputStream()));
            Image ineraLogotype = new Image(imageData);
            ineraLogotype.scale(0.08f, 0.08f);
            ineraLogotype.setFixedPosition(490, 780);
            document.add(ineraLogotype);
            // Add paragraph to the document

            document.add(new Paragraph("Läkarutlåtande för Sjukersättning").setFontSize(20f));
            Div div = new Div();
            div.setWidth(800);
            div.add(new Paragraph("Personnummer").setFontSize(8f)
                    .add("\n")
                    .add("19121212-1212"))
                    .add(new Paragraph("Intygs-id").add("\n").add(UUID.randomUUID().toString()).setFontSize(8f));
            div.setTextAlignment(TextAlignment.RIGHT);
            // div.setBorder(border);
            document.add(div);
            Div root = new Div();
            render(this.components, root);
            document.add(root);

            Border border = new SolidBorder(1f);
            border.setColor(ineraBlue);

            // Close document
            document.close();

            // document.setPageSize(PageSize.A4);
            // document.setMargins(model.getPageMargins()[0], model.getPageMargins()[1], model.getPageMargins()[2],
            // model.getPageMargins()[3]);

            // Add preference to viewer applications to NOT scale when printing (it's just a hint, the user can change this)

            // Add specified event handlers

            // Leave actual rendering to the model, giving it starting x/y offset of top-left corner.
            // model.render(document, writer, 0f, Utilities.pointsToMillimeters(document.getPageSize().getTop()));

            // Finish off by closing the document (this will invoke any page event handlers)
            document.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create PDF", e);
        }

    }

    private void renderStaticContent() throws IOException {

    }

    // CHECKSTYLE:OFF MethodLength
    public void render(List<LayoutComponent> components, Div parent) {

        for (LayoutComponent component : components) {
            // Each component goes into a new div.
            Div div = new Div();

            // Start render component
            switch (component.getType()) {
            case "uv-kategori":
                String kategori = getText(component.getLabelKey());

                div.add(new Paragraph(kategori)
                        .setMarginTop(0f)
                        .setMarginBottom(0f)
                        .setPadding(4f)
                        .setBold()
                        .setFontSize(12f)
                        .setFontColor(white)
                        .setBackgroundColor(ineraBlue));
                div.setBorder(new SolidBorder(ineraBlue, 1f));
                div.setKeepTogether(true);

                break;

            case "uv-fraga":
                String fraga = getText(component.getLabelKey());
                div.add(new Paragraph(fraga)
                        .setMarginLeft(8f)
                        .setBold()
                        .setFontSize(10f));
                // div.setBorderBottom(new SolidBorder(0.5f));
                div.setPadding(8f);

                break;

            case "uv-del-fraga":
                if (component.getHideExpression() != null) {
                    String hideExpression = component.getHideExpression();
                    // Try to find a sub-component (peek ahead) with the hideExpression
                    String cleanedExpression = hideExpression.startsWith("!") ? hideExpression.substring(1) : hideExpression;

                    boolean renderMe = false;
                    Iterator<LayoutComponent> i = component.getComponents().iterator();
                    while (i.hasNext()) {
                        LayoutComponent subComponent = i.next();
                        if (subComponent.getModelProp().equals(cleanedExpression)) {
                            boolean hasValue = hasValue(cleanedExpression);
                            if (hideExpression.startsWith("!") && !hasValue) {
                                // Hide if NOT present
                                renderMe = false;
                                i.remove();
                                System.out.println("REMOVE: " + hideExpression);
                            }
                            break;
                        }
                    }

                    if (!renderMe) {
                        break;
                    }

                }

                String delFraga = getText(component.getLabelKey());
                if (delFraga != null) {
                    div.add(new Paragraph(delFraga).setMarginLeft(8f).setBold().setFontSize(8f).setPadding(0f).setMarginTop(0f)
                            .setMarginBottom(0f));
                }
                break;
            case "uv-simple-value":
                String value = getSimpleValue(component.getModelProp());
                div.add(new Paragraph(value).setItalic().setMarginLeft(8f).setFontSize(8f)
                        .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
                break;
            case "uv-boolean-statement":
            case "uv-boolean-value":
                String booleanValue = getBooleanValue(component.getModelProp());
                div.add(new Paragraph(booleanValue).setMarginLeft(8f).setFontSize(8f));
                break;
            case "uv-table":

                int cols = component.getHeaders().size();
                float[] colsWidths = new float[cols];
                for (int a = 0; a < cols; a++) {
                    colsWidths[a] = 12f;
                }

                Table table = new Table(colsWidths).setMarginLeft(8f);

                // table.setWidth(100);

                // Render headers with tabs
                for (String header : component.getHeaders()) {
                    if (header.endsWith(".RBK")) {
                        table.addHeaderCell(
                                new Cell()
                                        .setBorder(Border.NO_BORDER)
                                        .setBorderBottom(new SolidBorder(0.5f))
                                        .add(
                                                new Paragraph(getText(header)).setFontSize(8f).setBold()));
                    } else {
                        table.addHeaderCell(
                                new Cell().setBorder(Border.NO_BORDER).setBorderBottom(new SolidBorder(0.5f)).add(
                                        new Paragraph(header)).setFontSize(8f).setBold());
                    }
                }

                JsonNode values = jsonModel.get(component.getModelProp());
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
                                            new Paragraph(someText).setFontSize(8f)));

                        } else {
                            table.addCell(
                                    new Cell().setBorder(Border.NO_BORDER).add(
                                            new Paragraph(rowValue.get(valueProp).textValue()).setFontSize(8f)));

                        }

                    }

                }
                div.add(table);

                break;

            case "uv-skapad-av":
                /*
                 * Intyget är utfärdat och signerat av:
                 * Karin Persson
                 * Tel: 054203408
                 * nmt_vg1_ve1, nmt_vg1
                 * Bryggaregatan 11, 65340 Karlstad
                 */
                div.setMarginTop(0f)
                        .setMarginBottom(0f)
                        .setPadding(16f)

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
                break;
            default:
                System.out.println(component.getType());
                break;
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
    // CHECKSTYLE:ON MethodLength

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

        String[] parts = modelProp.split("\\.");
        JsonNode currentNode = jsonModel;
        for (String part : parts) {
            currentNode = currentNode.get(part);

        }
        if (currentNode == null) {
            throw new IllegalStateException("Could not resolve properly: " + modelProp);
        }
        if (currentNode.isBoolean()) {
            return currentNode.booleanValue() ? "Ja" : "Nej";
        }
        if (currentNode.isTextual()) {
            return currentNode.textValue();
        }
        throw new IllegalArgumentException("Cannot fetch nested value of type " + currentNode.getNodeType().name());
    }

    private String getText(String labelKey) {
        try {
            return intygTexts.getTexter().get(labelKey);
        } catch (Exception e) {
            return "";
        }
    }
}
// CHECKSTYLE:ON MagicNumber
