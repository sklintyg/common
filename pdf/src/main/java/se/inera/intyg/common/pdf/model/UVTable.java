/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.pdf.model;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.NashornException;
//import jdk.nashorn.internal.runtime.ECMAException;
//import jdk.nashorn.internal.runtime.Undefined;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

/**
 * The table component is somewhat complex since table data can be either property- or function-based.
 */
public class UVTable extends UVComponent {

    private static final float DEFAULT_TABLE_WIDTH_MM = 140f;

    public UVTable(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        String modelProp = (String) currentUvNode.get(MODEL_PROP);

        // Handle any modelPropOverride for this "modelProp"
        if (handleModelPropOveride(parent, modelProp)) {
            return true;
        }

        List<String> headerLabels = buildHeaderLabels(currentUvNode);

        Table table = new Table(headerLabels.size())
            .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
            .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
            .setWidth(millimetersToPoints(DEFAULT_TABLE_WIDTH_MM))
            .setKeepTogether(true);

        // Render headers with tabs
        renderHeaders(headerLabels, table);

        // Tables rows are tricky. Value props may be strings or functions.
        ScriptObjectMirror valuePropsObj = (ScriptObjectMirror) currentUvNode.get("valueProps");

        if (!valuePropsObj.isArray()) {
            throw new IllegalArgumentException("Table valueProps must be of type array.");
        }

        ScriptObjectMirror modelValue = (ScriptObjectMirror) renderer.evalValueFromModel(modelProp);

        List<List<String>> data = new ArrayList<>();

        // Iterate over the modelValue if array.
        if (modelValue != null && modelValue.isArray()) {

            // Start rows
            for (Object value : modelValue.values()) {

                // Start columns
                List<String> columnValues = new ArrayList<>();
                for (Object valueProp : valuePropsObj.values()) {

                    if (valueProp instanceof String) {
                        handleStringValueProp(modelProp, (ScriptObjectMirror) value, (String) valueProp, columnValues);
                    }
                    if (valueProp instanceof ScriptObjectMirror) {
                        ScriptObjectMirror valuePropSOM = (ScriptObjectMirror) valueProp;
                        throw new IllegalStateException(
                            "Found unhandled ScriptObjectMirror in UVTable for valueProp " + valuePropSOM.getClassName());
                    }

                }
                data.add(columnValues);
            }

        } else if (modelValue != null && modelValue.getClassName().equalsIgnoreCase("OBJECT")) {
            List<String> colProps = fromStringArray(currentUvNode.get("colProps"));

            // Start rows. colProps are rows... very confusing...
            int row = 0;
            for (String colProp : colProps) {
                List<String> columnValues = new ArrayList<>();
                // Start cols
                int col = 0;
                for (Object valueProp : valuePropsObj.values()) {
                    Object o = modelValue.get(colProp);

                    ScriptObjectMirror som = (ScriptObjectMirror) o;
                    ScriptObjectMirror function = (ScriptObjectMirror) valueProp;
                    Object result;
                    try {
                        result = function.call(null, som, row, col++, colProp);
                    } catch (NashornException/*ECMAException*/ e) {
                        result = EJ_ANGIVET_STR;
                    }
                    if (result != null && !(result.toString().equals("undefined")/*instanceof Undefined*/)) {
                        String text = renderer.getText(result.toString());
                        if (text != null) {
                            columnValues.add(text);
                        } else {
                            columnValues.add(result.toString());
                        }
                    } else if (result != null && result.toString().equals("undefined") /*instanceof Undefined*/) {
                        columnValues.add(EJ_ANGIVET_STR);
                    } else {
                        columnValues.add("");
                    }
                }
                row++;
                data.add(columnValues);
            }
        }

        // RENDER
        if (hasValuesInTable(data)) {
            renderTableData(table, data, headerLabels);
            parent.add(table);
        } else {
            parent.add(new Paragraph(EJ_ANGIVET_STR).setItalic()
                .setMarginBottom(0f)
                .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
                .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE)
                .setPadding(0f).setMarginTop(0f)
                .setKeepTogether(false));
        }

        return true;
    }

    private boolean hasValuesInTable(List<List<String>> tableData) {
        return tableData.stream().anyMatch(row -> row.stream().anyMatch(col -> !Strings.isNullOrEmpty(col) && !col.equals(EJ_ANGIVET_STR)));
    }

    private void handleStringValueProp(String modelProp, ScriptObjectMirror value, String valueStr, List<String> columnValues) {

        if (valueStr.contains("{") && valueStr.contains("}")) {
            // Extract the string between...
            String extracted = valueStr.substring(valueStr.indexOf("{") + 1, valueStr.indexOf("}"));
            String result = (String) renderer.findInModel(value, extracted);
            String textKey = valueStr.replaceAll("\\{" + extracted + "\\}", result);

            String text = renderer.getText(textKey);
            if (!Strings.isNullOrEmpty(text)) {
                columnValues.add(text);
            } else {
                columnValues.add("");
            }
        } else {
            Object result = renderer.findInModel(value, valueStr);
            if (result != null) {
                columnValues.add(result.toString());
            } else {
                columnValues.add("");
            }
        }
    }

    private void renderTableData(Table table, List<List<String>> data, List<String> headerLabels) {
        for (List<String> row : data) {
            int columnIndex = 0;
            for (String col : row) {
                Paragraph paragraph = new Paragraph(col)
                    .setFont(renderer.svarFont)
                    .setFontSize(FRAGA_DELFRAGA_FONT_SIZE);

                // Weird extra rule - if first column has no header text, make the values bold.
                if (columnIndex == 0 && headerLabels.get(0).isEmpty()) {
                    paragraph.setFont(renderer.fragaDelFragaFont);
                }
                table.addCell(new Cell().setBorder(Border.NO_BORDER).add(paragraph));
                columnIndex++;
            }
        }
    }

    private void renderHeaders(List<String> headerLabels, Table table) {
        for (String header : headerLabels) {
            table.addHeaderCell(
                new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setBorderBottom(new SolidBorder(DEFAULT_BORDER_WIDTH))
                    .add(
                        new Paragraph(header)
                            .setFont(renderer.fragaDelFragaFont)
                            .setFontSize(FRAGA_DELFRAGA_FONT_SIZE)));
        }
    }

    private List<String> buildHeaderLabels(ScriptObjectMirror currentUvNode) {
        List<String> headerLabels = new ArrayList<>();
        for (String header : fromStringArray(currentUvNode.get("headers"))) {
            if (header.endsWith(".RBK")) {
                headerLabels.add(renderer.getText(header));
            } else {
                String text = renderer.getText(header);
                if (text != null) {
                    headerLabels.add(text);
                } else {
                    headerLabels.add(header);
                }
            }
        }
        return headerLabels;
    }
}
