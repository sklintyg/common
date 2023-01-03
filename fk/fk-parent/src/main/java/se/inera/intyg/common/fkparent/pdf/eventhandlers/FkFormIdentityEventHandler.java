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
package se.inera.intyg.common.fkparent.pdf.eventhandlers;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.util.StringUtils;
import se.inera.intyg.common.fkparent.pdf.PdfConstants;

// CHECKSTYLE:OFF MagicNumber

/**
 * Stamps the fk form issue version info on each page (left side margin).
 */
public class FkFormIdentityEventHandler extends PdfPageEventHelper {

    private static final float ROTATION = 90f;
    private final String blankettVersion;
    private String formId;
    private String formIdRow2 = null;
    private String blankettId;
    private float formidX = Utilities.millimetersToPoints(12f);
    private float formidXWith2Rows = Utilities.millimetersToPoints(9f);
    private float formidY = Utilities.millimetersToPoints(8.5f);
    private float scanidX = Utilities.millimetersToPoints(12f);
    private float scanidY = Utilities.millimetersToPoints(118f);

    public FkFormIdentityEventHandler(String formId, String blankettId, String blankettVersion) {
        this.formId = formId;
        this.blankettId = blankettId;
        this.blankettVersion = blankettVersion;
    }

    public FkFormIdentityEventHandler(String formId, String formIdRow2, String blankettId, String blankettVersion) {
        this(formId, blankettId, blankettVersion);
        this.formIdRow2 = formIdRow2;
    }

    /**
     * Stamps the fk issue info on each page.
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContentUnder();
        boolean is2Rows = !StringUtils.isEmpty(formIdRow2);
        float firstRowX = is2Rows ? formidXWith2Rows : formidX;

        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(formId, PdfConstants.FONT_FORM_ID_LABEL), firstRowX, formidY,
            ROTATION);
        if (is2Rows) {
            ColumnText
                .showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(formIdRow2, PdfConstants.FONT_FORM_ID_LABEL), formidX, formidY,
                    ROTATION);
        }

        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
            new Phrase(buildPageScanId(blankettId, blankettVersion, writer.getPageNumber()), PdfConstants.FONT_PAGESCAN_ID), scanidX,
            scanidY, ROTATION);

    }

    public String buildPageScanId(String blankettId, String blankettVersion, int pageNumber) {
        /*******************************************************
         * (Hittat i FK7800_001_F_001.pdf i extraherad xfa xml definition) rad 445
         * "XXXX" + "A" + "B" + "VV" *
         * *
         * XXXX = blankettnummer, t.ex. 7263 *
         * A = 1 vid enkelsidig utskrift, 2 vid dubbelsidig *
         * B = sidnummer *
         * VV = versionsnummer på skanningsmallen
         *
         * //OBS! Sätt rätt värden nedan
         * var pageNum = xfa.layout.page(this);
         * if (pageNum &lt;= 9)
         * {
         * pageNum = "0" + pageNum;
         * }
         * skanningsid.rawValue = "7800" + pageNum + "01";
         ********************************************************/
        return blankettId + String.format("%02d", pageNumber) + blankettVersion;
    }

}
// CHECKSTYLE:ON MagicNumber
