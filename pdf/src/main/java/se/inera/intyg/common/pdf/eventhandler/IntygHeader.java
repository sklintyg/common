/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.pdf.model.UVComponent.KATEGORI_FONT_SIZE;
import static se.inera.intyg.common.pdf.model.UVComponent.SVAR_FONT_SIZE;
import static se.inera.intyg.common.pdf.renderer.UVRenderer.PAGE_MARGIN_LEFT;
import static se.inera.intyg.common.pdf.renderer.UVRenderer.WC_COLOR_11;
import static se.inera.intyg.common.pdf.util.UnifiedPdfUtil.millimetersToPoints;

import com.google.common.base.Strings;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import se.inera.intyg.common.pdf.renderer.PrintConfig;

/**
 * Renders the header elements.
 */
public class IntygHeader implements IEventHandler {

    // All constants related to positioning should be in points.
    private static final float LOGOTYPE_Y_TOP_OFFSET = millimetersToPoints(15f);
    private static final float UTSKRIFTSDATUM_HEADER_Y_TOP_OFFSET = millimetersToPoints(15f);
    private static final float UTSKRIFTSDATUM_VALUE_Y_TOP_OFFSET = millimetersToPoints(19f);
    private static final float LOGOTYPE_MAX_HEIGHT = millimetersToPoints(15f);
    private static final float LOGOTYPE_MAX_WIDTH = millimetersToPoints(35f);
    private static final float INTYG_NAME_Y_TOP_OFFSET = millimetersToPoints(35f);
    private static final float LINE_WIDTH = 0.5f;
    private static final float HR_Y_TOP_OFFSET = millimetersToPoints(38f);
    private static final float INTYG_FONT_SIZE = 14f;
    private static final float RED_SQUARE_PADDING = millimetersToPoints(5f);
    private static final float RED_SQUARE_Y_TOP_OFFSET = millimetersToPoints(48f);
    private static final float DEFAULT_PADDING = millimetersToPoints(5f);
    private static final float SPACING_POINTS = 3f;
    private static final float THOUSAND = 1000.0f;
    private static final int WIDTH_SCALE_THRESHOLD = 3;

    private final PrintConfig printConfig;
    private final PdfImageXObject logo;
    private final PdfFont kategoriFont;
    private final PdfFont fragaDelFragaFont;
    private final PdfFont svarFont;

    public IntygHeader(PrintConfig printConfig, PdfFont kategoriFont, PdfFont fragaDelFragaFont, PdfFont svarFont) {
        this.printConfig = printConfig;
        this.logo = new PdfImageXObject(ImageDataFactory.create(printConfig.getUtfardarLogotyp()));
        this.kategoriFont = kategoriFont;
        this.fragaDelFragaFont = fragaDelFragaFont;
        this.svarFont = svarFont;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof PdfDocumentEvent)) {
            return;
        }

        final var docEvent = (PdfDocumentEvent) event;
        final var pdf = docEvent.getDocument();
        final var page = docEvent.getPage();

        final var pageSize = page.getPageSize();
        final var pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
        final var canvas = new Canvas(pdfCanvas, pageSize);

        renderLogotype(pageSize, pdfCanvas);
        renderUtskriftsDatum(pageSize, canvas);

        if (isNotSummaryPage(pdf, page)) {
            renderPersonnummer(pageSize, canvas);
        }

        renderIntygNameAndCode(pageSize, canvas);
        renderHorizontalLine(pageSize, pdfCanvas);

        if (isNotSummaryPage(pdf, page)) {
            renderRedSquare(page, pageSize, canvas);
        }

        pdfCanvas.release();
    }

    private boolean isNotSummaryPage(PdfDocument pdf, PdfPage page) {
        if (printConfig.hasSummaryPage()) {
            return pdf.getPageNumber(page) != pdf.getNumberOfPages();
        }
        return true;
    }

    private void renderLogotype(Rectangle pageSize, PdfCanvas pdfCanvas) {
        final var logoWidth = logo.getWidth();

        if (logoWidth <= LOGOTYPE_MAX_WIDTH) {
            return;
        }

        final var logoHeight = logo.getHeight();
        final var widthHeightRatio = logoWidth / logoHeight;
        final var logoX = millimetersToPoints(PAGE_MARGIN_LEFT);

        if (widthHeightRatio < WIDTH_SCALE_THRESHOLD) {
            final var logoY = pageSize.getTop() - LOGOTYPE_Y_TOP_OFFSET - LOGOTYPE_MAX_HEIGHT + DEFAULT_PADDING;
            final var rectangle = PdfXObject.calculateProportionallyFitRectangleWithHeight(logo, logoX, logoY, LOGOTYPE_MAX_HEIGHT);
            pdfCanvas.addXObjectFittedIntoRectangle(logo, rectangle);

        } else {
            final var ratio = LOGOTYPE_MAX_WIDTH / logoWidth;
            final var logoY = pageSize.getTop() - LOGOTYPE_Y_TOP_OFFSET - (logoHeight * ratio);
            final var rectangle = PdfXObject.calculateProportionallyFitRectangleWithWidth(logo, logoX, logoY, LOGOTYPE_MAX_WIDTH);
            pdfCanvas.addXObjectFittedIntoRectangle(logo, rectangle);
        }
    }

    private void renderUtskriftsDatum(Rectangle pageSize, Canvas canvas) {
        canvas.setFont(fragaDelFragaFont).setFontSize(SVAR_FONT_SIZE);
        canvas.showTextAligned("Utskriftsdatum",
            pageSize.getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT),
            pageSize.getTop() - UTSKRIFTSDATUM_HEADER_Y_TOP_OFFSET, TextAlignment.RIGHT);
        canvas.setFont(svarFont).setFontSize(SVAR_FONT_SIZE);
        canvas.showTextAligned(LocalDate.now().format(DateTimeFormatter.ISO_DATE),
            pageSize.getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT),
            pageSize.getTop() - UTSKRIFTSDATUM_VALUE_Y_TOP_OFFSET, TextAlignment.RIGHT);
    }

    private void renderPersonnummer(Rectangle pageSize, Canvas canvas) {
        float measuredUtskiftsDatum = measureTextWidth("Utskriftsdatum", SVAR_FONT_SIZE, fragaDelFragaFont);
        canvas.setFont(fragaDelFragaFont).setFontSize(SVAR_FONT_SIZE);
        canvas.showTextAligned("Person- /samordningsnr",
            pageSize.getWidth() - (measuredUtskiftsDatum + DEFAULT_PADDING + millimetersToPoints(PAGE_MARGIN_LEFT)),
            pageSize.getTop() - UTSKRIFTSDATUM_HEADER_Y_TOP_OFFSET, TextAlignment.RIGHT);
        canvas.setFont(svarFont).setFontSize(SVAR_FONT_SIZE);
        canvas.showTextAligned(printConfig.getPersonnummer(),
            pageSize.getWidth() - (measuredUtskiftsDatum + DEFAULT_PADDING + millimetersToPoints(PAGE_MARGIN_LEFT)),
            pageSize.getTop() - UTSKRIFTSDATUM_VALUE_Y_TOP_OFFSET, TextAlignment.RIGHT);
    }

    private void renderIntygNameAndCode(Rectangle pageSize, Canvas canvas) {
        Paragraph intygsnamn = new Paragraph(printConfig.getIntygsNamn());
        intygsnamn.setFont(kategoriFont).setFontSize(INTYG_FONT_SIZE);
        canvas.showTextAligned(intygsnamn,
            millimetersToPoints(PAGE_MARGIN_LEFT),
            pageSize.getTop() - INTYG_NAME_Y_TOP_OFFSET, TextAlignment.LEFT);
        float calculatedWidth = measureTextWidth(printConfig.getIntygsNamn(), INTYG_FONT_SIZE, kategoriFont);

        // Intygskod
        Paragraph intygskod = new Paragraph("(" + getIntygsKod(printConfig) + ")");
        intygskod.setFont(svarFont).setFontSize(KATEGORI_FONT_SIZE);
        canvas.showTextAligned(intygskod,
            millimetersToPoints(PAGE_MARGIN_LEFT) + calculatedWidth + SPACING_POINTS,
            pageSize.getTop() - INTYG_NAME_Y_TOP_OFFSET, TextAlignment.LEFT);
    }

    private String getIntygsKod(PrintConfig printConfig) {
        return printConfig.getIntygsKod()
            + (Strings.isNullOrEmpty(printConfig.getIntygsVersion()) ? "" : " " + printConfig.getIntygsVersion());

    }

    private void renderHorizontalLine(Rectangle pageSize, PdfCanvas pdfCanvas) {
        pdfCanvas.moveTo(millimetersToPoints(PAGE_MARGIN_LEFT), pageSize.getTop() - HR_Y_TOP_OFFSET);
        pdfCanvas.lineTo(pageSize.getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT),
            pageSize.getTop() - HR_Y_TOP_OFFSET);
        pdfCanvas.setLineWidth(LINE_WIDTH);
        pdfCanvas.stroke();
    }

    private void renderRedSquare(PdfPage page, Rectangle pageSize, Canvas canvas) {
        Paragraph paragraph = new Paragraph(printConfig.getInfoText());
        paragraph.setBorder(new SolidBorder(WC_COLOR_11, 1f));
        paragraph.setFont(svarFont).setFontSize(SVAR_FONT_SIZE);
        paragraph.setPadding(millimetersToPoints(2f));
        paragraph.setPaddingLeft(RED_SQUARE_PADDING);
        paragraph.setPaddingRight(RED_SQUARE_PADDING);
        // set a max width that is margin x 2 plus padding x 2 minus line width.
        paragraph.setMaxWidth(
            page.getPageSize().getWidth() - millimetersToPoints(PAGE_MARGIN_LEFT * 2) - (DEFAULT_PADDING * 2)
                - 2f);
        canvas.showTextAligned(paragraph,
            millimetersToPoints(PAGE_MARGIN_LEFT),
            pageSize.getTop() - RED_SQUARE_Y_TOP_OFFSET, TextAlignment.LEFT, VerticalAlignment.MIDDLE);
    }

    private float measureTextWidth(String text, float fontSize, PdfFont font) {
        GlyphLine glyphLine = font.createGlyphLine(text);

        int width = 0;
        for (int i = 0; i < glyphLine.size(); i++) {
            Glyph glyph = glyphLine.get(i);
            width += glyph.getWidth();
        }
        return width * fontSize / THOUSAND;
    }
}
