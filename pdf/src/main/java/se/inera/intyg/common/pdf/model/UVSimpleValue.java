/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

/**
 * Renders a uv-simple-value.
 */
public class UVSimpleValue extends UVComponent {

    private static final float SIMPLEVALUE_MARGIN_BOTTOM = 5f;

    public UVSimpleValue(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        // Handle any modelPropOverride for this "modelProp"
        if (handleModelPropOveride(parent, (String) currentUvNode.get(MODEL_PROP))) {
            return true;
        }
        String modelProp = (String) currentUvNode.get(MODEL_PROP);
        Object value = renderer.evalValueFromModel(modelProp);
        Object unit = currentUvNode.get("unit");
        StringBuilder outputText = new StringBuilder();
        if (value != null) {
            outputText.append(value.toString());
            if (unit != null) {
                outputText.append(" ");
                outputText.append(unit.toString());
            }
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
