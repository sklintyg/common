/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.pdf.renderer.UVRenderer.PAGE_MARGIN_BOTTOM_WITH_SIGNBOX;
import static se.inera.intyg.common.pdf.renderer.UVRenderer.PAGE_MARGIN_LEFT;
import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;

import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import se.inera.intyg.common.pdf.renderer.PrintConfig;

/**
 * Renders the texts in the right and left margins.
 */
// CHECKSTYLE:OFF MagicNumber
public class SignBox implements IEventHandler {

    private static final float FONT_SIZE = 10f;
    private static final float LINE_WIDTH = 0.5f;
    private final PrintConfig printConfig;
    private PdfFont svarFont;

    public SignBox(PrintConfig printConfig, PdfFont svarFont) {
        this.printConfig = printConfig;
        this.svarFont = svarFont;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof PdfDocumentEvent)) {
            return;
        }
        if (!printConfig.showSignBox()) {
            return;
        }
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
            page.newContentStreamBefore(), page.getResources(), pdf);

        Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
        canvas.setFont(svarFont).setFontSize(FONT_SIZE);
        pdfCanvas.setLineWidth(LINE_WIDTH);

        if (shouldRender(pdf, page)) {
            canvas.showTextAligned("Signatur",
                pageSize.getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT) - millimetersToPoints(13f),
                pageSize.getBottom() + PAGE_MARGIN_BOTTOM_WITH_SIGNBOX + 17f,
                TextAlignment.LEFT, VerticalAlignment.MIDDLE, 0);

            final float width = 50f;
            final float height = 15f;
            final float x = (page.getPageSize().getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT) - millimetersToPoints(width));
            final float y = pageSize.getBottom() + PAGE_MARGIN_BOTTOM_WITH_SIGNBOX + 25f;
            pdfCanvas.rectangle(x, y, millimetersToPoints(width), millimetersToPoints(height));
            pdfCanvas.stroke();
            pdfCanvas.release();
        }
    }

    private boolean shouldRender(PdfDocument pdf, PdfPage page) {
        return pdf.getPageNumber(page) != pdf.getNumberOfPages();
    }
    // CHECKSTYLE:ON MagicNumber
}
