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
package se.inera.intyg.common.fkparent.pdf.eventhandlers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import se.inera.intyg.common.fkparent.pdf.PdfConstants;

/**
 * A generic page numbering event handler.
 */
// CHECKSTYLE:OFF MagicNumber
public class PageNumberingEventHandler extends PdfPageEventHelper {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 16;

    private static final float DEFAULT_MARGIN_LEFT = 181f;
    private static final float DEFAULT_MARGIN_TOP = 8f;

    private float marginTop = DEFAULT_MARGIN_TOP;
    private float marginLeft = DEFAULT_MARGIN_LEFT;
    /**
     * The template with the issueInfoTemplate number of pages.
     */
    private PdfTemplate total;

    /**
     * Constructs a new instance that uses the default placement of the page numbering, e.g.
     * {@link PageNumberingEventHandler#DEFAULT_MARGIN_LEFT} mm from the left and
     * {@link PageNumberingEventHandler#DEFAULT_MARGIN_TOP} mm from the top.
     *
     */
    public PageNumberingEventHandler() {
        // Intentionally empty, use this when you want the default margins.
    }

    /**
     * Use this constructor to optionally override the placing of the page numbering. Uses margin from the left and the
     * top
     * of the page, in millimeters.
     *
     * @param marginLeft
     *            Margin from the left edge of the page, in millimeters.
     * @param marginTop
     *            Margin from the top of the page, in millimeters.
     */
    public PageNumberingEventHandler(float marginLeft, float marginTop) {
        this.marginTop = marginTop;
        this.marginLeft = marginLeft;
    }

    /**
     * Creates the PdfTemplate that will hold the issueInfoTemplate number of pages.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(WIDTH, HEIGHT);
    }

    /**
     * Adds a header to every page.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        try {
            PdfPTable table = new PdfPTable(2);

            table.setTotalWidth(Utilities.millimetersToPoints(20));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(new Phrase(String.valueOf(writer.getPageNumber()), PdfConstants.FONT_PAGE_NUMBERING));

            PdfPCell cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(marginLeft),
                    document.getPageSize().getTop() - Utilities.millimetersToPoints(marginTop),
                    writer.getDirectContent());

        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Fills out the issueInfoTemplate number of pages before the document is closed.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // CHECKSTYLE:OFF MagicNumber
        ColumnText.showTextAligned(total,
                Element.ALIGN_LEFT,
                new Phrase("(" + Integer.toString(writer.getPageNumber()) + ")", PdfConstants.FONT_PAGE_NUMBERING), 0,
                Utilities.millimetersToPoints(1f),
                0);
    }
}
