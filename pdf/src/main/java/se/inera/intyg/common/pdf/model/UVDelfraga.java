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

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

/**
 * Renders a Delfraga element.
 */
public class UVDelfraga extends UVComponent {

    public UVDelfraga(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public boolean render(Div parent, ScriptObjectMirror currentUvNode) {
        String labelKey = (String) currentUvNode.get(LABEL_KEY);

        // Check if we have hide/show expressions
        boolean render = show(currentUvNode);
        if (render) {
            String delFraga = labelKey != null ? renderer.getText(labelKey) : "";
            if (delFraga != null) {
                parent.add(new Paragraph(delFraga)
                    .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
                    .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
                    .setFont(renderer.fragaDelFragaFont)
                    .setFontColor(WC_COLOR_09)
                    .setFontSize(FRAGA_DELFRAGA_FONT_SIZE)
                    .setPadding(0f)
                    .setMarginTop(0f)
                    .setMarginBottom(0f)
                    .setKeepTogether(true)
                );
            }
            // Allow page breaks (separating delfraga title from answer)
            parent.setKeepTogether(false);
        }
        return render;
    }
}
