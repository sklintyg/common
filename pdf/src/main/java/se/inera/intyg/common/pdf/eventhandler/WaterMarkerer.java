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
package se.inera.intyg.common.pdf.eventhandler;

import com.itextpdf.kernel.colors.DeviceRgb;
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
 * Adds watermark (if applicable).
 */
public class WaterMarkerer implements IEventHandler {

    private static final String DRAFT_WATERMARK_TEXT = "UTKAST";
    private static final String CANCELLED_WATERMARK_TEXT = "MAKULERAT";
    private static final String LOCKED_DRAFT_WATERMARK_TEXT = "LÅST UTKAST";

    private static final int ROTATION = 45;
    private static final float FILL_OPACITY = 0.5f;
    private static final float WATERMARK_FONT_SIZE = 100f;

    private static final DeviceRgb WATERMARK_COLOR = new DeviceRgb(128, 128, 128);

    private PrintConfig printConfig;
    private PdfFont watermarkFont;

    public WaterMarkerer(PrintConfig printConfig, PdfFont watermarkFont) {
        this.printConfig = printConfig;
        this.watermarkFont = watermarkFont;
    }

    @Override
    public void handleEvent(Event event) {
        if (!printConfig.isUtkast() && !printConfig.isLockedUtkast() && !printConfig.isMakulerad()) {
            return;
        }

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
        canvas.setFont(watermarkFont);
        canvas.setFontSize(WATERMARK_FONT_SIZE);
        canvas.setFontColor(WATERMARK_COLOR, FILL_OPACITY);

        final float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
        final float y = (pageSize.getTop() + pageSize.getBottom()) / 2;

        canvas.showTextAligned(resolveText(),
                x,
                y, TextAlignment.CENTER, VerticalAlignment.MIDDLE, (float) Math.toRadians(ROTATION));

    }

    private String resolveText() {
        if (printConfig.isMakulerad()) {
            return CANCELLED_WATERMARK_TEXT;
        }
        if (printConfig.isLockedUtkast()) {
            return LOCKED_DRAFT_WATERMARK_TEXT;
        }
        if (printConfig.isUtkast()) {
            return DRAFT_WATERMARK_TEXT;
        }
        throw new IllegalStateException("This state should never be.");
    }
}
