/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.pdf.eventhandler;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import static se.inera.intyg.common.pdf.model.UVComponent.SVAR_FONT_SIZE;
import static se.inera.intyg.common.pdf.renderer.UVRenderer.PAGE_MARGIN_LEFT;
import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

/**
 * Renders the page number. Note that quirky placeholder stuff going on since we don't know the total number of pages
 * until the full PDF have been rendered.
 */
public class PageNumberEvent implements IEventHandler {

    private static final float PAGE_NUMBER_Y_OFFSET = 15f;
    private static final float SIDE = 20f;
    private static final float DESCENT = 3;

    private final PdfFont svarFont;
    private final PdfFormXObject placeholder;

    public PageNumberEvent(PdfFont svarFont) {
        this.svarFont = svarFont;
        placeholder = new PdfFormXObject(new Rectangle(0, 0, SIDE, SIDE));
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof PdfDocumentEvent)) {
            return;
        }
        final var  docEvent = (PdfDocumentEvent) event;
        final var  pdf = docEvent.getDocument();
        final var  page = docEvent.getPage();
        final var  pageNumber = pdf.getPageNumber(page);
        final var  pageSize = page.getPageSize();
        final var  pdfCanvas = new PdfCanvas(
            page.newContentStreamBefore(), page.getResources(), pdf);
        try (Canvas canvas = new Canvas(pdfCanvas, pageSize)) {
            Paragraph p = new Paragraph()
                .add("Sida ").add(pageNumber + "(")
                .setFont(svarFont).setFontSize(SVAR_FONT_SIZE);
            canvas.showTextAligned(p, pageSize.getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT + 2), PAGE_NUMBER_Y_OFFSET,
                TextAlignment.RIGHT);
            pdfCanvas.addXObjectAt(placeholder, pageSize.getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT + 2),
                PAGE_NUMBER_Y_OFFSET - DESCENT);
            pdfCanvas.release();
        }
    }

    public void writeTotal(PdfDocument pdf) {
        Canvas canvas = new Canvas(placeholder, pdf);
        canvas.setFont(svarFont).setFontSize(SVAR_FONT_SIZE);
        canvas.showTextAligned(pdf.getNumberOfPages() + ")",
            0, DESCENT, TextAlignment.LEFT);
    }
}
