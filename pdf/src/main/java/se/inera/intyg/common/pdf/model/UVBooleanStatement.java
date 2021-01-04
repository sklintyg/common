/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

/**
 * Renders a Boolean modelvalue as 'Ja' if true else 'Ej angivet'.
 */
public class UVBooleanStatement extends UVComponent {

    public UVBooleanStatement(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        // Handle any modelPropOverride for this "modelProp"
        if (handleModelPropOveride(parent, (String) currentUvNode.get(MODEL_PROP))) {
            return true;
        }
        String displayValue = getBooleanStatementValue((String) currentUvNode.get(MODEL_PROP));
        parent.add(new Paragraph(displayValue).setItalic()
            .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
            .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
            .setMarginTop(0f)
            .setFont(renderer.svarFont)
            .setFontSize(SVAR_FONT_SIZE));

        return true;
    }

    /**
     * Resolves the boolean value of the supplied modelProp and returns 'Ja' or 'Ej angivet'.
     */
    private String getBooleanStatementValue(String modelProp) {
        Object eval = renderer.evalValueFromModel(modelProp);
        if (eval != null) {
            if (eval instanceof Boolean) {
                return (Boolean) eval ? "Ja" : EJ_ANGIVET_STR;
            }
        }
        return EJ_ANGIVET_STR;
    }
}
