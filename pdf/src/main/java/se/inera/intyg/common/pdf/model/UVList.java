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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.google.common.base.Strings;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

/**
 * Renders a UVList. This is a complex component that can render values based both on pure
 * string-based lookups in the model or by executing javascript functions defined in the
 * uvConfig.
 */
public class UVList extends UVComponent {

    private boolean useLabelKeyForPrint = false;

    public UVList(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        List<String> results = new ArrayList<>();

        String labelKey = (String) currentUvNode.get(LABEL_KEY);
        String separator = (String) currentUvNode.get("separator");

        Object listKeyObj = currentUvNode.get("listKey");
        Object modelPropObj = currentUvNode.get(MODEL_PROP);

        useLabelKeyForPrint = currentUvNode.get("useLabelKeyForPrint") == null
            ? false : (Boolean) currentUvNode.get("useLabelKeyForPrint");

        // First, resolve the actual values using either String- or function based resolving and push the textual values
        // into a list.

        // Listkey can be both a ScriptObjectMirror or a simple string
        if (listKeyObj instanceof String) {
            handleListKeyAsString(results, labelKey, (String) listKeyObj, modelPropObj);
        } else if (listKeyObj instanceof ScriptObjectMirror) {
            handleListKeyAsScriptObjectMirror(results, labelKey, (ScriptObjectMirror) listKeyObj, modelPropObj);
        }

        if (results.isEmpty()) {
            String noValueText = renderer.getText((String) currentUvNode.get("noValue"));
            if (!StringUtils.isEmpty(noValueText)) {
                results.add(noValueText);
            }
        }

        // RENDER the results.
        renderListResult(parent, results, separator);

        return true;
    }

    /**
     * Handle listKey as ScriptObjectMirror, where the modelProp in its turn may be a String or a ScriptObjectMirror.
     */
    private void handleListKeyAsScriptObjectMirror(List<String> results, String labelKey, ScriptObjectMirror listKeyObj,
        Object modelPropObj) {
        ScriptObjectMirror listKey = listKeyObj;
        if (modelPropObj instanceof String) {
            buildListResultFromStringModelProp(results, listKey, (String) modelPropObj, labelKey);
        } else if (modelPropObj instanceof ScriptObjectMirror) {
            ScriptObjectMirror modelProps = (ScriptObjectMirror) modelPropObj;
            if (modelProps.isArray()) {
                buildListResultFromArray(results, labelKey, listKey, modelProps);
            }
        }
    }

    /**
     * When the listKey is a string, we inspect the modelProp which may be either String or ScriptObjectMirror.
     */
    private void handleListKeyAsString(List<String> results, String labelKey, String listKey, Object modelPropObj) {

        if (modelPropObj instanceof String) {
            Object eval = renderer.evalValueFromModel((String) modelPropObj);

            if (eval instanceof ScriptObjectMirror) {
                ScriptObjectMirror evalSOM = (ScriptObjectMirror) eval;

                if (evalSOM.isArray()) {
                    for (Object value : evalSOM.values()) {
                        ScriptObjectMirror valueSOM = (ScriptObjectMirror) value;
                        Object resolvedListKey = valueSOM.get(listKey);
                        String textKey = labelKey.replaceAll("\\{var\\}", (String) resolvedListKey);
                        results.add(renderer.getText(textKey));
                    }
                } else {
                    throw new IllegalStateException("Unhandled result type class in uv-list: " + evalSOM.getClassName()
                        + " expected Array");
                }
            } else {
                throw new IllegalStateException("Unhandled evaluation class in uv-list: " + eval.getClass().getName()
                    + " expected ScriptObjectMirror");
            }

        } else if (modelPropObj instanceof ScriptObjectMirror) {
            throw new IllegalStateException("Unhandled modelPropObj class in uv-list: " + modelPropObj.getClass().getName()
                + " expected String");
        }
    }

    private void renderListResult(Div parent, List<String> results, String separator) {
        if (results.isEmpty()) {
            renderEjAngivet(parent);
        } else if (!Strings.isNullOrEmpty(separator)) {
            // If there's a separator, render as signle value with separator.
            renderListResultWithSeparator(parent, results, separator);
        } else {
            // Otherwise, render as bullet list.
            renderListResultAsBulletList(parent, results);
        }
    }

    private void renderListResultAsBulletList(Div parent, List<String> results) {
        com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
        list.setMarginLeft(ELEM_MARGIN_LEFT_POINTS);
        list.setMarginRight(ELEM_MARGIN_RIGHT_POINTS);
        list.setKeepTogether(true);
        results.stream().forEach(result -> {
            ListItem listItem = new ListItem(result);
            listItem.setFont(renderer.svarFont);
            listItem.setFontSize(SVAR_FONT_SIZE);
            list.add(listItem);
        });
        parent.add(list);
    }

    private void renderListResultWithSeparator(Div parent, List<String> results, String separator) {
        // If there's a separator specified, render as comma-separated string
        parent.setKeepTogether(true);
        parent.add(new Paragraph(results.stream().collect(Collectors.joining(separator))).setItalic()
            .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
            .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
            .setFont(renderer.svarFont)
            .setFontSize(SVAR_FONT_SIZE)
            .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
        parent.setKeepTogether(false);
    }

    private void buildListResultFromArray(List<String> results, String labelKey, ScriptObjectMirror listKey,
        ScriptObjectMirror modelProps) {
        int index = 0;
        for (Object val : modelProps.values()) {

            if (listKey.isFunction()) {
                Object evaluatedValue = renderer.evalValueFromModel((String) val);

                // Only supports boolean returns.
                if (evaluatedValue instanceof Boolean) {
                    boolean evalBool = (boolean) evaluatedValue;
                    if (evalBool) {
                        Object result = listKey.call(null, evaluatedValue, index);

                        if (result != null) {
                            Integer i;
                            if (result instanceof Integer) {
                                i = ((Integer) result);
                            } else if (result instanceof Double) {
                                i = ((Double) result).intValue();
                            } else {
                                throw new IllegalStateException("UNHANDLED result type from list call: " + result.getClass().getName());
                            }
                            String textKey = labelKey.replaceAll("\\{var\\}", "" + i);
                            String value = renderer.getText(textKey);
                            results.add(value);
                        }
                    }
                    // Skip null values, if no results will lead to EJ_ANGIVET
                } else if (evaluatedValue != null) {
                    throw new IllegalStateException("UNHANDLED evaluated value type in uv-list: " + evaluatedValue.getClass().getName());
                }
                index++;
            } else {
                throw new IllegalStateException("UNHANDLED listKey type in uv-list: " + listKey.getClassName());
            }
        }
    }

    private void buildListResultFromStringModelProp(List<String> results, ScriptObjectMirror listKey,
        String modelPropObj, String labelKey) {
        Object eval = renderer.evalValueFromModel(modelPropObj);

        // Lists are tricky. Check if the listKey is a function
        if (listKey.isFunction()) {
            // This is weird, but we need to invoke the function one time per evalValueFromModel.
            ScriptObjectMirror evaluatedModelProp = (ScriptObjectMirror) eval;

            if (evaluatedModelProp == null) {
                results.add(EJ_ANGIVET_STR);
            } else {
                int index = 0;
                for (Object o : evaluatedModelProp.values()) {
                    Object result = listKey.call(null, o, index++);
                    if (result != null) {
                        if (useLabelKeyForPrint) {
                            String textKey = labelKey.replaceAll("\\{var\\}", (String) result);
                            results.add(renderer.getText(textKey));
                        } else {
                            results.add((String) result);
                        }
                    }
                }
            }
        } else {
            throw new IllegalStateException("UNHANDLED listKey type in uv-list: " + listKey.getClassName());
        }
    }

}
