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
package se.inera.intyg.common.pdf.model;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;
import java.util.List;

/**
 * Renders a uv-simple-value.
 */
public class UVTemplateString extends UVComponent {

    private static final float SIMPLEVALUE_MARGIN_BOTTOM = 5f;
    public UVTemplateString(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        String modelProp = (String) currentUvNode.get(MODEL_PROP);
        List<String> variables = fromStringArray(currentUvNode.get("variables"));
        String template = (String) currentUvNode.get("template");
        StringBuilder outputText = new StringBuilder();

        if (variables.size() > 0 && template != null) {
            String replaced = template.replaceAll("\\{\\d}", "%s");
            outputText.append(String.format(replaced, variables.stream().map(it ->
                    (String) renderer.evalValueFromModel(modelProp + "." + it)).toArray()));
        } else {
            outputText.append(UVComponent.EJ_ANGIVET_STR);
        }

        parent.add(new Paragraph(outputText.toString()).setItalic()
                .setMarginBottom(millimetersToPoints(SIMPLEVALUE_MARGIN_BOTTOM))
                .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
                .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE)
                .setPadding(0f).setMarginTop(0f).setMarginBottom(0f)
                .setKeepTogether(false)
                );

        return true;
    }
}
