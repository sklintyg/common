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
package se.inera.intyg.common.ts_diabetes.v2.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * Created by marced on 2017-02-23.
 */
public abstract class BasePdfGenerator<T extends Utlatande> {

    public static final String WEBCERT_MARGIN_TEXT = "Intyget är utskrivet från Webcert.";
    private static final Font FONT = new Font(Font.FontFamily.HELVETICA, 100f, Font.NORMAL, BaseColor.GRAY);
    private static final String DRAFT_WATERMARK_TEXT = "UTKAST";
    private static final String CANCELLED_WATERMARK_TEXT = "MAKULERAT";
    private static final String LOCKED_WATERMARK_TEXT = "LÅST UTKAST";
    private static final int ROTATION = 45;
    private static final float FILL_OPACITY = 0.5f;
    // Constants for printing ID and origin in left margin
    private static final int MARGIN_TEXT_START_X = 46;
    private static final int MARGIN_TEXT_START_Y = 137;
    private static final int MARGIN_TEXT_FONTSIZE = 7;

    public static boolean isMakulerad(List<Status> statuses) {
        return statuses != null && statuses.stream().filter(Objects::nonNull)
            .anyMatch(s -> CertificateState.CANCELLED.equals(s.getType()));
    }


    public void addWatermark(PdfStamper stamper, int nrPages, boolean isUtkast, boolean isMakulerad, boolean isLocked) {
        Phrase watermark;

        if (isLocked) {
            watermark = new Phrase(LOCKED_WATERMARK_TEXT, FONT);
        } else if (isUtkast) {
            watermark = new Phrase(DRAFT_WATERMARK_TEXT, FONT);
        } else if (isMakulerad) {
            watermark = new Phrase(CANCELLED_WATERMARK_TEXT, FONT);
        } else {
            return;
        }

        PdfContentByte over;
        Rectangle pageSize;
        // loop over every page
        for (int i = 1; i <= nrPages; i++) {

            over = stamper.getOverContent(i);
            over.saveState();
            PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(FILL_OPACITY);
            over.setGState(gs1);

            // Center the watermark text
            pageSize = over.getPdfDocument().getPageSize();
            final float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
            final float y = (pageSize.getTop() + pageSize.getBottom()) / 2;

            ColumnText.showTextAligned(over, Element.ALIGN_CENTER, watermark, x, y, ROTATION);
            over.restoreState();
        }
    }

    protected void createLeftMarginText(PdfStamper pdfStamper, int numberOfPages, String id, ApplicationOrigin applicationOrigin,
        String minaIntygMarginText)
        throws DocumentException, IOException {
        PdfContentByte addOverlay;
        BaseFont bf = BaseFont.createFont();

        String text = WEBCERT_MARGIN_TEXT;
        if (applicationOrigin.equals(ApplicationOrigin.MINA_INTYG)) {
            text = minaIntygMarginText;
        }
        // Do text
        for (int i = 1; i <= numberOfPages; i++) {
            addOverlay = pdfStamper.getOverContent(i);
            addOverlay.saveState();
            addOverlay.beginText();
            addOverlay.setFontAndSize(bf, MARGIN_TEXT_FONTSIZE);
            addOverlay.setTextMatrix(0, 1, -1, 0, MARGIN_TEXT_START_X, MARGIN_TEXT_START_Y);
            addOverlay.showText(String.format("Intygs-ID: %s. %s", id, text));
            addOverlay.endText();
            addOverlay.restoreState();
        }
    }
}
