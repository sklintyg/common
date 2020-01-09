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

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Renders a Kodverk value.
 */
public class UVKodverkValue extends UVComponent {

    public UVKodverkValue(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        List<String> kvModelProps = fromStringArray(currentUvNode.get("kvModelProps"));
        List<String> kvLabelKeys = fromStringArray(currentUvNode.get("kvLabelKeys"));

        List<String> kvParts = new ArrayList<>();
        for (int a = 0; a < kvLabelKeys.size(); a++) {
            String currentModelProp = kvModelProps.get(a);
            String currentLabelKey = kvLabelKeys.get(a);

            String nestedValue = (String) renderer.evalValueFromModel(currentModelProp);
            if (nestedValue != null) {
                String textKey = currentLabelKey.replaceAll("\\{var\\}", nestedValue);
                String value = renderer.getText(textKey);
                kvParts.add(value);
            }
        }

        if (kvParts.isEmpty()) {
            renderEjAngivet(parent);
        } else {
            parent.add(new Paragraph(kvParts.stream().collect(Collectors.joining(" ")))
                .setItalic()
                .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
                .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
                .setFont(renderer.svarFont)
                .setFontSize(SVAR_FONT_SIZE)
                .setPadding(0f).setMarginTop(0f).setMarginBottom(0f));
        }
        return true;
    }
}
