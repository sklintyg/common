/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.fkparent.pdf.model;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.common.fkparent.pdf.PdfConstants;

/**
 * An implementation of a generic label component. Besides the common configuration properties, other adjustments can be
 * made to padding and leading etc.
 *
 */
public class FkLabel extends PdfComponent<FkLabel> {

    private final String label;
    private int verticalAlignment = PdfPCell.ALIGN_MIDDLE;
    private int horizontalAlignment = PdfPCell.ALIGN_LEFT;
    private Font font = PdfConstants.FONT_INLINE_FIELD_LABEL;
    private float fixedLeading = 0.0f;
    private float multipliedLeading = 1.0f;
    private float topPadding = 1f;
    private BaseColor backgroundColor = null;
    private boolean backgroundRounded = false;

    public FkLabel(String label) {
        this.label = label;
    }

    public FkLabel withHorizontalAlignment(int alignment) {
        this.horizontalAlignment = alignment;
        return this;
    }

    public FkLabel withVerticalAlignment(int alignment) {
        this.verticalAlignment = alignment;
        return this;
    }

    public FkLabel withFont(Font font) {
        this.font = font;
        return this;
    }

    public FkLabel withLeading(float fixedLeading, float multipliedLeading) {
        this.fixedLeading = fixedLeading;
        this.multipliedLeading = multipliedLeading;
        return this;
    }

    public FkLabel withTopPadding(float padding) {
        this.topPadding = padding;
        return this;
    }

    public FkLabel backgroundColor(int r, int g, int b) {
        this.backgroundColor = new BaseColor(r, g, b);
        return this;
    }

    public FkLabel backgroundRounded(boolean backgroundRounded) {
        this.backgroundRounded = backgroundRounded;
        return this;
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(width));

        // labelCell
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));

        // Make sure the tablecell has the correct height
        labelCell.setFixedHeight(Utilities.millimetersToPoints(height));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setUseAscender(true); // needed to make vertical alignment correct
        labelCell.setHorizontalAlignment(horizontalAlignment);
        labelCell.setVerticalAlignment(verticalAlignment);
        labelCell.setPaddingTop(Utilities.millimetersToPoints(topPadding));
        labelCell.setLeading(fixedLeading, multipliedLeading);
        if (backgroundColor != null) {
            if (backgroundRounded) {
                labelCell.setCellEvent(new RoundedBorder(backgroundColor));
            } else {
                labelCell.setBackgroundColor(backgroundColor);
            }
        }

        table.addCell(labelCell);

        table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(x),
                Utilities.millimetersToPoints(y), writer.getDirectContent());

        super.render(document, writer, x, y);
    }

    /**
     * Implementation of PdfPCellEvent that renders a colored background with Rounded borders for the attached PdfPCell.
     *
     * Uses sensible defaults, but may need to be augmented with builder or constructor for fine-granular control of
     * offsets
     * and rounding.
     *
     * The color is read from the outer class.
     */
    private static class RoundedBorder implements PdfPCellEvent {

        private static final float DEFAULT_LEFT_OFFSET = -8f;
        private static final float DEFAULT_BOTTOM_OFFSET = 8f;
        private static final float WIDTH_PADDING = 16f;
        private static final float HEIGHT_PADDING = 0f;
        private static final float CORDER_RADIUS = 12f;

        private float leftOffset = DEFAULT_LEFT_OFFSET, bottomOffset = DEFAULT_BOTTOM_OFFSET, widthPadding = WIDTH_PADDING,
                heightPadding = HEIGHT_PADDING, cornerRadius = CORDER_RADIUS;
        private BaseColor backgroundColor;

        RoundedBorder(BaseColor backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        @Override
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
            cb.setColorFill(backgroundColor);
            cb.roundRectangle(
                    rect.getLeft() + leftOffset,
                    rect.getBottom() + bottomOffset,
                    rect.getWidth() + widthPadding,
                    rect.getHeight() + heightPadding,
                    cornerRadius);
            cb.fill();
        }
    }

}
