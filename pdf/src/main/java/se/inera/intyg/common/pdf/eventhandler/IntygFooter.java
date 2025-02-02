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
package se.inera.intyg.common.pdf.eventhandler;

import static se.inera.intyg.common.pdf.model.UVComponent.SVAR_FONT_SIZE;
import static se.inera.intyg.common.pdf.renderer.UVRenderer.PAGE_MARGIN_LEFT;
import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.properties.TextAlignment;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * Renders the footer elements.
 */
public class IntygFooter implements IEventHandler {

    private static final float PADDING = 5.0f;
    private static final float LINE_WIDTH = 0.5f;
    private static final float LINE_Y_OFFSET = millimetersToPoints(15);

    private static final String WEBCERT_APP_NAME = "Webcert";
    private String minaIntygAppName;

    private final PdfFont svarFont;
    private final ApplicationOrigin applicationOrigin;

    public IntygFooter(PdfFont svarFont, ApplicationOrigin applicationOrigin, String footerAppName) {
        this.svarFont = svarFont;
        this.applicationOrigin = applicationOrigin;
        this.minaIntygAppName = footerAppName;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof PdfDocumentEvent)) {
            return;
        }

        String appName;
        switch (applicationOrigin) {
            case WEBCERT:
                appName = WEBCERT_APP_NAME;
                break;
            case MINA_INTYG:
                appName = minaIntygAppName;
                break;
            default:
                throw new IllegalStateException("Unhandled ApplicationOrigin: " + applicationOrigin);
        }

        final var docEvent = (PdfDocumentEvent) event;
        final var pdf = docEvent.getDocument();
        final var page = docEvent.getPage();
        final var pageSize = page.getPageSize();
        final var pdfCanvas = new PdfCanvas(
            page.newContentStreamBefore(), page.getResources(), pdf);

        try (Canvas canvas = new Canvas(pdfCanvas, pageSize)) {
            canvas.setFont(svarFont).setFontSize(SVAR_FONT_SIZE);
            canvas.showTextAligned("Utskriften skapades med " + appName + " - en tjänst som drivs av Inera AB\nwww.inera.se",
                millimetersToPoints(PAGE_MARGIN_LEFT),
                millimetersToPoints(pageSize.getBottom() + PADDING), TextAlignment.LEFT);

            pdfCanvas.moveTo(millimetersToPoints(PAGE_MARGIN_LEFT), LINE_Y_OFFSET);
            pdfCanvas.lineTo(page.getPageSize().getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT), LINE_Y_OFFSET);
            pdfCanvas.setLineWidth(LINE_WIDTH);
            pdfCanvas.stroke();
            pdfCanvas.release();
        }
    }
}
