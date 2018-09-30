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

import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

/**
 * Renders a kategori. Note that we wrap the actual kategori iText Div for correct border rendering.
 */
public class UVKategori extends UVComponent {

    public UVKategori(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        String labelKey = (String) currentUvNode.get(LABEL_KEY);
        String kategori = renderer.getText(labelKey);

        Div borderDiv = new Div();
        parent.setBorder(new SolidBorder(wcColor07, DEFAULT_BORDER_WIDTH));

        borderDiv.add(new Paragraph(kategori.toUpperCase())
                .setMarginTop(0f)
                .setMarginBottom(0f)
                .setMarginRight(ELEM_MARGIN_RIGHT_POINTS)
                .setMarginLeft(ELEM_MARGIN_LEFT_POINTS)
                .setFont(renderer.kategoriFont)
                .setFontSize(KATEGORI_FONT_SIZE)
                .setFontColor(wcColor07)
                .setPaddingTop(1f)
                .setPaddingBottom(1f))
                .setKeepTogether(true);
        borderDiv.setBorderBottom(new SolidBorder(wcColor07, DEFAULT_BORDER_WIDTH));
        parent.setKeepTogether(true);
        parent.add(borderDiv);
    }
}
