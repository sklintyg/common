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
package se.inera.intyg.common.pdf.eventhandler;

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

import static se.inera.intyg.common.pdf.model.UVComponent.SVAR_FONT_SIZE;
import static se.inera.intyg.common.pdf.renderer.UVRenderer.PAGE_MARGIN_LEFT;
import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

public class IntygFooter implements IEventHandler {

    private static final float PADDING = 5.0f;
    public static final float LINE_WIDTH = 0.5f;
    public static final float LINE_Y_OFFSET = millimetersToPoints(15);

    private PdfFont svarFont;

    public IntygFooter(PdfFont svarFont) {
        this.svarFont = svarFont;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof PdfDocumentEvent)) {
            return;
        }
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
                page.newContentStreamBefore(), page.getResources(), pdf);

        Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
        canvas.setFont(svarFont).setFontSize(SVAR_FONT_SIZE);
        canvas.showTextAligned("Utskriften skapades med Webcert - en tjänst som drivs av Inera\nwww.inera.se",
                millimetersToPoints(PAGE_MARGIN_LEFT),
                millimetersToPoints(pageSize.getBottom() + PADDING), TextAlignment.LEFT);

        pdfCanvas.moveTo(millimetersToPoints(PAGE_MARGIN_LEFT), LINE_Y_OFFSET);
        pdfCanvas.lineTo(page.getPageSize().getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT), LINE_Y_OFFSET);
        pdfCanvas.setLineWidth(LINE_WIDTH);
        pdfCanvas.stroke();
        pdfCanvas.release();
    }
}
