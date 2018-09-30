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

import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import se.inera.intyg.common.pdf.renderer.UVRenderer;

import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

/**
 * Renders an alert component consisting of a table where cell1 contains an icon and the other cell contains the
 * alert text.
 */
public class UVAlertValue extends UVComponent {

    // Points
    private static final float ALERT_TABLE_PADDING = 10f;
    private static final float ALERT_TABLE_MARGIN = 5f;

    private static final float ALERT_IMAGE_SIZE = 16f;


    public UVAlertValue(UVRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Div parent, ScriptObjectMirror currentUvNode) {
        String labelKey = (String) currentUvNode.get(LABEL_KEY);
        if (labelKey == null) {
            return;
        }

        // Check if we have hide/show expressions
        if (show(currentUvNode)) {
            String delFraga = renderer.getText(labelKey);

            PdfImageXObject observandumIcon = renderer.getObservandumIcon();

            Table table = new Table(2)
                    .setPadding(millimetersToPoints(ALERT_TABLE_PADDING))
                    .setMargin(millimetersToPoints(ALERT_TABLE_MARGIN))
                    .setKeepTogether(true);
            table.setBackgroundColor(wcColor02);

            Cell iconCell = new Cell();
            iconCell.setBorder(Border.NO_BORDER);
            iconCell.add(new Image(observandumIcon, ALERT_IMAGE_SIZE));
            table.addCell(iconCell);

            Paragraph paragraph = new Paragraph();
            paragraph = paragraph
                    .add(delFraga)
                    .setFont(renderer.fragaDelFragaFont)
                    .setFontColor(wcColor05)
                    .setFontSize(FRAGA_DELFRAGA_FONT_SIZE);
            Cell textCell = new Cell();
            textCell.setBorder(Border.NO_BORDER);
            textCell.add(paragraph);
            table.addCell(textCell);

            if (delFraga != null) {
                parent.add(table);
            }
        }
    }
}
