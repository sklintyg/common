/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

import java.util.List;
import java.util.stream.Stream;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

/**
 * Renders a uv-template-string.
 */
public class UVTemplateString extends UVComponent {

    private static final String SCRIPT_PROPERTY_VARIABLES = "variables";
    private static final String SCRIPT_PROPERTY_TEMPLATE = "template";

    public UVTemplateString(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        String modelProp = (String) currentUvNode.get(MODEL_PROP);
        List<String> variables = fromStringArray(currentUvNode.get(SCRIPT_PROPERTY_VARIABLES));
        String template = (String) currentUvNode.get(SCRIPT_PROPERTY_TEMPLATE);
        StringBuilder outputText = new StringBuilder();

        if (variables.size() > 0 && template != null) {
            final Object[] values = variables.stream()
                .map(it -> renderer.evalValueFromModel(modelProp + "." + it))
                .toArray();

            boolean hasAtleastOneValue = Stream.of(values).anyMatch(value -> value != null);
            if (hasAtleastOneValue) {
                final Object[] resolvedValues = Stream.of(values)
                    .map(value -> (value != null) ? value.toString() : EJ_ANGIVET_STR)
                    .toArray();

                String replaced = template.replaceAll("\\{\\d}", "%s");
                outputText.append(String.format(replaced, resolvedValues));
            } else {
                outputText.append(EJ_ANGIVET_STR);
            }

        } else {
            outputText.append(EJ_ANGIVET_STR);
        }

        parent.add(new Paragraph(outputText.toString()).setItalic()
            .setMarginBottom(0f)
            .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
            .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
            .setFont(renderer.svarFont)
            .setFontSize(SVAR_FONT_SIZE)
            .setPadding(0f).setMarginTop(0f)
            .setKeepTogether(false));

        return true;
    }

}
