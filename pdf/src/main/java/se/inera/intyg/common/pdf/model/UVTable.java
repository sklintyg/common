package se.inera.intyg.common.pdf.model;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

public class UVTable extends UVComponent {

    public UVTable(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        List<String> headerLabels = buildHeaderLabels(currentUvNode);

        Table table = new Table(headerLabels.size()).setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f));

        // Render headers with tabs
        renderHeaders(headerLabels, table);

        // Tables rows are tricky. Value props may be strings or functions.
        ScriptObjectMirror valuePropsObj = (ScriptObjectMirror) currentUvNode.get("valueProps");

        if (!valuePropsObj.isArray()) {
            throw new IllegalArgumentException("Table valueProps must be of type array.");
        }

        String modelProp = (String) currentUvNode.get("modelProp");
        ScriptObjectMirror modelValue = (ScriptObjectMirror) renderer.eval(modelProp);


        List<List<String>> data = new ArrayList<>();

        // Iterate over the modelValue if array.
        if (modelValue.isArray()) {

            // Start rows
            for (Object value : modelValue.values()) {

                // Start columns
                for (Object valueProp : valuePropsObj.values()) {
                    List<String> columnValues = new ArrayList<>();
                    if (valueProp instanceof String) {
                        handleStringValueProp(modelProp, (ScriptObjectMirror) value, (String) valueProp, columnValues);
                    }
                    if (valueProp instanceof ScriptObjectMirror) {
                        ScriptObjectMirror valuePropSOM = (ScriptObjectMirror) valueProp;
                        if (valuePropSOM.isFunction()) {
                            System.out.println("FOUND FUNC");
                        }
                    }
                    data.add(columnValues);
                }
            }
        } else if (modelValue.getClassName().equalsIgnoreCase("OBJECT")) {
            List<String> colProps = fromStringArray(currentUvNode.get("colProps"));
            // Start rows. colProps are rows...

            List<String> columnValues = new ArrayList<>();
            int row = 0;

            for (String colProp : colProps) {
                // Start cols
                int col = 0;
                for (Object valueProp : valuePropsObj.values()) {
                    Object o = modelValue.get(colProp);

                    ScriptObjectMirror som = (ScriptObjectMirror) o;
                    ScriptObjectMirror function = (ScriptObjectMirror) valueProp;
                    Object result = function.call(null, som, row, col++, colProp);
                    if (result != null) {
                        columnValues.add(result.toString());
                    } else {
                        columnValues.add("");
                    }
                }
                row++;
            }
            data.add(columnValues);
        }

        // RENDER
        renderTableData(table, data);

        parent.add(table);
    }

    private void handleStringValueProp(String modelProp, ScriptObjectMirror value, String valueStr, List<String> columnValues) {

        if (valueStr.contains("{") && valueStr.contains("}")) {
            // Extract the string between...
            String extracted = valueStr.substring(valueStr.indexOf("{") + 1, valueStr.indexOf("}"));
            String result = (String) renderer.findInModel(value, extracted);
            String textKey = valueStr.replaceAll("\\{" + extracted + "\\}", result);
            if (textKey != null) {
                String text = renderer.getText(textKey);
                columnValues.add(text);
            } else {
                columnValues.add("");
                System.err.println("Null value for: " + modelProp + "." + extracted);
            }

        } else {
            Object result = renderer.findInModel(value, valueStr);
            if (result != null) {
                columnValues.add(result.toString());
            } else {
                columnValues.add("");
                System.err.println("Null value for: " + modelProp + "." + valueStr);
            }
        }
    }

    private void renderTableData(Table table, List<List<String>> data) {
        for (List<String> row: data) {
            for (String col : row) {
                table.addCell(new Cell().setBorder(Border.NO_BORDER).add(
                                    new Paragraph(col)));
            }
        }
    }

    private void renderHeaders(List<String> headerLabels, Table table) {
        for (String header : headerLabels) {
            table.addHeaderCell(
                        new Cell()
                                .setBorder(Border.NO_BORDER)
                                .setBorderBottom(new SolidBorder(0.5f))
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
