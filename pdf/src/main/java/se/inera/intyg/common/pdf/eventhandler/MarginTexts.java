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
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import se.inera.intyg.common.pdf.renderer.PrintConfig;

import static se.inera.intyg.common.pdf.renderer.UVRenderer.PAGE_MARGIN_LEFT;
import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

/**
 * Renders the texts in the right and left margins.
 */
public class MarginTexts implements IEventHandler {

    private static final float FONT_SIZE = 10f;
    private final PrintConfig printConfig;
    private final PdfFont svarFont;

    public MarginTexts(PrintConfig printConfig, PdfFont svarFont) {
        this.printConfig = printConfig;
        this.svarFont = svarFont;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof PdfDocumentEvent)) {
            return;
        }
        final var  docEvent = (PdfDocumentEvent) event;
        final var  pdf = docEvent.getDocument();
        final var  page = docEvent.getPage();
        final var  pageSize = page.getPageSize();
        final var  pdfCanvas = new PdfCanvas(
            page.newContentStreamBefore(), page.getResources(), pdf);

        try (Canvas canvas = new Canvas(pdfCanvas, pageSize)) {
            canvas.setFont(svarFont).setFontSize(FONT_SIZE);

            // Left margin
            canvas.showTextAligned(printConfig.getLeftMarginTypText(),
                millimetersToPoints(PAGE_MARGIN_LEFT / 2),
                millimetersToPoints(PAGE_MARGIN_LEFT), TextAlignment.LEFT, VerticalAlignment.MIDDLE, (float) Math.PI / 2);

            // Right margin, visas endast för signerat intyg, ej heller på sista sidan om det är ett informationsblad.
            if (!printConfig.isUtkast() && !printConfig.isLockedUtkast() && renderIntygsId(pdf, page)) {
                canvas.showTextAligned("Intygs-ID: " + printConfig.getIntygsId(),
                    pageSize.getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT / 2),
                    millimetersToPoints(PAGE_MARGIN_LEFT), TextAlignment.LEFT, VerticalAlignment.MIDDLE, (float) Math.PI / 2);
            }
        }
    }

    private boolean renderIntygsId(PdfDocument pdf, PdfPage page) {
        if (printConfig.hasSummaryPage()) {
            return pdf.getPageNumber(page) != pdf.getNumberOfPages();
        }
        return true;
    }
}
