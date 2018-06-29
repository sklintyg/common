package se.inera.intyg.common.pdf.model;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

public class UVList extends UVComponent {
    public UVList(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        List<String> results = new ArrayList<>();

        String labelKey = (String) currentUvNode.get("labelKey");
        String separator = (String) currentUvNode.get("separator");

        Object listKeyObj = currentUvNode.get("listKey");

        // Listkey can be both a ScriptObjectMirror or a simple string
        if (listKeyObj instanceof String) {

        }
        if (listKeyObj instanceof ScriptObjectMirror) {
            ScriptObjectMirror listKey = (ScriptObjectMirror) listKeyObj;

            Object modelPropObj = currentUvNode.get("modelProp");

            if (modelPropObj instanceof String) {
                buildListResultFromStringModelProp(results, listKey, (String) modelPropObj);
            } else if (modelPropObj instanceof ScriptObjectMirror) {
                ScriptObjectMirror modelProps = (ScriptObjectMirror) modelPropObj;
                if (modelProps.isArray()) {
                    buildListResultFromArray(results, labelKey, listKey, modelProps);
                }
            }
        }


        // RENDER the results.
        renderListResult(parent, results, separator);

    }

    private void renderListResult(Div parent, List<String> results, String separator) {
        if (!Strings.isNullOrEmpty(separator)) {
            // If there's a separator, render as signle value with separator.
            renderListResultWithSeparator(parent, results, separator);
        } else {
            // Otherwise, render as bullet list.
            renderListResultAsBulletList(parent, results);
        }
    }

    private void renderListResultAsBulletList(Div parent, List<String> results) {
        com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
        results.stream().forEach(result -> {
            ListItem listItem = new ListItem(result);
            list.add(listItem);
        });
        parent.add(list);
    }

    private void renderListResultWithSeparator(Div parent, List<String> results, String separator) {
        // If there's a separator specified, render as comma-separated string
        parent.add(new Paragraph(results.stream().collect(Collectors.joining(separator))).setItalic()
                .setMarginRight(millimetersToPoints(10f))
                .setMarginLeft(millimetersToPoints(5f))
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE)
                .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
    }

    private void buildListResultFromArray(List<String> results, String labelKey, ScriptObjectMirror listKey,
            ScriptObjectMirror modelProps) {
        int index = 0;
        for (Object val : modelProps.values()) {

            if (listKey.isFunction()) {
                Object eval = renderer.eval((String) val);
                if (eval instanceof Boolean) {
                    boolean evalBool = (boolean) eval;
                    if (evalBool) {
                        Object result = listKey.call(null, eval, index++);

                        if (result != null) {
                            if (result instanceof Double) {
                                Integer i = ((Double) result).intValue();
                                String textKey = labelKey.replaceAll("\\{var\\}", "" + i);
                                String value = renderer.getText(textKey);
                                results.add(value);
                            }
                        }
                    }
                }
            }

        }
    }

    private void buildListResultFromStringModelProp(List<String> results, ScriptObjectMirror listKey, String modelPropObj) {
        Object eval = renderer.eval(modelPropObj);

        // Lists are tricky. Check if the listKey is a function

        if (listKey.isFunction()) {
            // This is weird, but we need to invoke the function one time per eval.
            ScriptObjectMirror evaluatedModelProp = (ScriptObjectMirror) eval;

            int index = 0;
            for (Object o : evaluatedModelProp.values()) {
                Object result = listKey.call(null, o, index++);
                if (result != null) {
                    results.add((String) result);
                }
            }
        }

    }
}
