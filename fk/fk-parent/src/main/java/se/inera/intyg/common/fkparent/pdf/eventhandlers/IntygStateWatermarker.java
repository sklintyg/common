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
package se.inera.intyg.common.fkparent.pdf.eventhandlers;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Creates a watermark text on each page depending on intyg state (draft/cancelled)
 * Created by marced on 2017-02-21.
 */
public class IntygStateWatermarker extends PdfPageEventHelper {

    private static final Font FONT = new Font(Font.FontFamily.HELVETICA, 100f, Font.NORMAL, BaseColor.GRAY);
    private static final String DRAFT_WATERMARK_TEXT = "UTKAST";
    private static final String CANCELLED_WATERMARK_TEXT = "MAKULERAT";
    private static final String LOCKED_DRAFT_WATERMARK_TEXT = "LÅST UTKAST";
    private static final int ROTATION = 45;
    private static final float FILL_OPACITY = 0.5f;

    private Phrase watermark;

    public IntygStateWatermarker(boolean isUtkast, boolean isMakulerad, boolean isLocked) {
        if (isLocked) {
            watermark = new Phrase(LOCKED_DRAFT_WATERMARK_TEXT, FONT);
        } else if (isUtkast) {
            watermark = new Phrase(DRAFT_WATERMARK_TEXT, FONT);
        } else if (isMakulerad) {
            watermark = new Phrase(CANCELLED_WATERMARK_TEXT, FONT);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (watermark == null) {
            // Nothing to do here
            return;
        }

        PdfContentByte canvas = writer.getDirectContent();
        // temporary set a new fillstate to allow transparancy output
        canvas.saveState();
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(FILL_OPACITY);
        canvas.setGState(gs1);

        // Center the watermark text
        final Rectangle pageSize = writer.getPageSize();
        final float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
        final float y = (pageSize.getTop() + pageSize.getBottom()) / 2;

        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, x, y, ROTATION);
        canvas.restoreState();
    }
}
