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
package se.inera.intyg.common.fkparent.pdf.model;

import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.common.fkparent.pdf.PdfConstants;

/**
 * A specialized component type that inspects the entire model for overflowed text fields and renders then on separate
 * page(s).
 * NOTE: An FkOverflowPage must be added last in the model, since it depends on the fact that FkOverflowableValueFields
 * reports overflowable text when actually rendered.
 *
 * Created by marced on 2016-10-24.
 */
public class FkOverflowPage extends FkPage {

    private static final float FULL_WIDTH = 100f;

    private FkPdfDefinition model;
    private float indentationLeft = 2f;
    private float indentationRight = 2f;

    public FkOverflowPage(String pageTitle, FkPdfDefinition model) {
        super(pageTitle);
        this.model = model;
    }

    public FkOverflowPage(String pageTitle, FkPdfDefinition model, float indentationLeft, float indentationRight) {
        super(pageTitle, indentationLeft);
        this.model = model;
        this.indentationLeft = Utilities.millimetersToPoints(indentationLeft);
        this.indentationRight = Utilities.millimetersToPoints(indentationRight);
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {
        final List<FkOverflowableValueField> fkOverflowableValueFields = model.collectOverflowingComponents();
        // Skip if nothing to do here..
        if (fkOverflowableValueFields.size() < 1) {
            return;
        }

        super.render(document, writer, x, y);

        renderOverflowingItems(document, fkOverflowableValueFields);
    }

    private void renderOverflowingItems(Document document, List<FkOverflowableValueField> overflowingComponents) throws DocumentException {
        // We wrap all overflowing items within a table as rows so that we can control that a row/cell is kept on the same page if possible.
        // Otherwise it's very probable that a page-break occurs between label and text.
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(FULL_WIDTH);

        table.setSplitLate(false);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        for (FkOverflowableValueField item : overflowingComponents) {
            Paragraph p = new Paragraph();
            p.setIndentationLeft(indentationLeft);
            p.setIndentationRight(indentationRight);
            p.setKeepTogether(true);

            p.add(Chunk.NEWLINE);
            p.add(new Phrase(item.getLabel(), PdfConstants.FONT_FRAGERUBRIK));
            p.add(Chunk.NEWLINE);

            p.add(new Phrase(item.getOverFlowingText(), PdfConstants.FONT_VALUE_TEXT_ARIAL_COMPATIBLE));

            // Needed to make the indentation work. Otherwise the Paragraph will be cast to a Phrase which has no indentation
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(p);

            table.addCell(cell);

        }
        document.add(table);
    }
}
