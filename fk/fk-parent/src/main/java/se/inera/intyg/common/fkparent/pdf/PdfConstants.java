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
package se.inera.intyg.common.fkparent.pdf;

import static com.itextpdf.text.pdf.BaseFont.createFont;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import java.io.IOException;

/**
 * Common constants and fonts that is used in FK SIT-type PDFs.
 *
 * @author marced.
 */
// CHECKSTYLE:OFF MagicNumber
// CHECKSTYLE:OFF LineLength
public final class PdfConstants {

    public static final String ELECTRONIC_COPY_WATERMARK_TEXT = "Detta är en utskrift av ett elektroniskt intyg. Intyget har signerats elektroniskt av intygsutfärdaren.";
    public static final String MINIMAL_ELECTRONIC_COPY_WATERMARK_TEXT = "Detta är en utskrift av ett elektroniskt intyg med minimalt innehåll. Det uppfyller sjuklönelagens krav, om inget annat regleras i kollektivavtal. Det minimala intyget kan ge arbetsgivaren sämre möjligheter att bedöma behovet av rehabilitering än ett fullständigt intyg. Intyget har signerats elektroniskt av intygsutfärdaren.";

    /**
     * * Fonts that will be used in FK SIT-type PDFs.
     */

    private static final String ARIAL_REGULAR_COMPATIBLE_FONT_PATH = "/fonts/LiberationSans-Regular.ttf";
    private static final String ARIAL_BOLD_COMPATIBLE_FONT_PATH = "/fonts/LiberationSans-Bold.ttf";

    private static final String TREBUCHET_REGULAR_COMPATIBLE_FONT_PATH = "/fonts/FiraSans-Regular.ttf";

    public static final Font FONT_FRAGERUBRIK;
    public static final Font FONT_FRAGERUBRIK_SMALL;
    public static final Font FONT_PAGETITLE;
    public static final Font FONT_VALUE_TEXT;
    public static final Font FONT_VALUE_TEXT_ARIAL_COMPATIBLE;
    public static final Font FONT_VALUE_TEXT_OVERFLOWINFO_ARIAL_COMPATIBLE;

    public static final Font FONT_INLINE_FIELD_LABEL;
    public static final Font FONT_INLINE_FIELD_LABEL_LARGE;
    public static final Font FONT_INLINE_FIELD_LABEL_SMALL;

    public static final Font FONT_BOLD_10;
    public static final Font FONT_BOLD_9;
    public static final Font FONT_BOLD_8;
    public static final Font FONT_PAGESCAN_ID;
    public static final Font FONT_FORM_ID_LABEL;

    public static final Font FONT_DIAGNOSE_CODE;
    public static final Font FONT_STAMPER_LABEL;
    public static final Font FONT_PAGE_NUMBERING;

    static {
        try {
            FONT_FRAGERUBRIK = new Font(createFont(ARIAL_BOLD_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11f,
                Font.NORMAL);
            FONT_FRAGERUBRIK_SMALL = new Font(createFont(ARIAL_BOLD_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10f,
                Font.NORMAL);
            FONT_PAGETITLE = new Font(createFont(ARIAL_BOLD_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11f,
                Font.NORMAL);

            FONT_VALUE_TEXT = new Font(createFont(TREBUCHET_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10.5f,
                Font.NORMAL);
            FONT_VALUE_TEXT_ARIAL_COMPATIBLE = new Font(createFont(ARIAL_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED), 10f, Font.NORMAL);
            FONT_VALUE_TEXT_OVERFLOWINFO_ARIAL_COMPATIBLE = new Font(createFont(ARIAL_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED), 10f, Font.BOLD);

            FONT_PAGESCAN_ID = new Font(createFont(TREBUCHET_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9f,
                Font.NORMAL);
            FONT_FORM_ID_LABEL = new Font(createFont(TREBUCHET_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 7f,
                Font.NORMAL);
            FONT_DIAGNOSE_CODE = new Font(createFont(ARIAL_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11f,
                Font.NORMAL);

            FONT_STAMPER_LABEL = new Font(createFont(TREBUCHET_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 8f,
                Font.NORMAL);
            FONT_PAGE_NUMBERING = new Font(createFont(TREBUCHET_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                10.5f, Font.NORMAL);

            FONT_INLINE_FIELD_LABEL = new Font(createFont(ARIAL_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                8.5f, Font.NORMAL);
            FONT_INLINE_FIELD_LABEL_SMALL = new Font(createFont(ARIAL_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                8f, Font.NORMAL);
            FONT_INLINE_FIELD_LABEL_LARGE = new Font(createFont(ARIAL_REGULAR_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                10f, Font.NORMAL);

            FONT_BOLD_10 = new Font(createFont(ARIAL_BOLD_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10f, Font.NORMAL);
            FONT_BOLD_9 = new Font(createFont(ARIAL_BOLD_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9f, Font.NORMAL);
            FONT_BOLD_8 = new Font(createFont(ARIAL_BOLD_COMPATIBLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 8f, Font.NORMAL);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Failed to initialize fonts", e);
        }
    }

    private PdfConstants() {
    }
}
// CHECKSTYLE:ON LineLength
// CHECKSTYLE:ON MagicNumber
