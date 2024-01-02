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
package se.inera.intyg.common.sos_parent.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

/**
 * Created by marced on 2017-10-16.
 */
public abstract class AbstractSoSPdfGenerator {

    public static final String PDF_PATH_PROPERTY_KEY = "pdfPath";

    protected static final String ELECTRONIC_COPY_WATERMARK_TEXT = "Detta är en utskrift av ett elektroniskt intyg. \n"
        + "Intyget har signerats elektroniskt av intygsutfärdaren.";
    protected static final String WEBCERT_MARGIN_TEXT = "Intyget är utskrivet från Webcert.";
    protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Constants used for watermarking
    private static final int MARK_AS_COPY_HEIGTH = 30;
    private static final int MARK_AS_COPY_WIDTH = 250;
    private static final int MARK_AS_COPY_START_X = 274;
    private static final int MARK_AS_COPY_START_Y = 702;
    private static final int WATERMARK_TEXT_PADDING = 4;
    private static final int WATERMARK_FONTSIZE = 10;
    private static final Font INTYG_STATEWATERMARK_FONT = new Font(Font.FontFamily.HELVETICA, 100f, Font.NORMAL, BaseColor.GRAY);
    private static final String INTYG_STATEWATERMARK_DRAFT_TEXT = "UTKAST";
    private static final String INTYG_STATEWATERMARK_CANCELLED_TEXT = "MAKULERAT";
    private static final String INTYG_STATEWATERMARK_LOCKED_TEXT = "LÅST UTKAST";
    private static final int INTYG_STATEWATERMARK_ROTATION = 45;
    private static final float INTYG_STATEWATERMARK_FILL_OPACITY = 0.5f;
    // Constants for printing ID and origin in right margin
    private static final int MARGIN_TEXT_START_X = 565;
    private static final int MARGIN_TEXT_START_Y = 27;
    private static final int MARGIN_TEXT_FONTSIZE = 7;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSoSPdfGenerator.class);
    protected AcroFields fields;
    protected ByteArrayOutputStream outputStream;

    public static boolean isMakulerad(List<Status> statuses) {
        return statuses != null && statuses.stream().filter(Objects::nonNull)
            .anyMatch(s -> CertificateState.CANCELLED.equals(s.getType()));
    }

    public static boolean ersatterTidigareIntyg(Relation relation) {
        return (relation != null) && (relation.getRelationKod() == RelationKod.ERSATT);
    }

    protected PdfReader getPdfReader(SosUtlatande utlatande, IntygTexts intygTexts, String defaultTemplatePath) throws Exception {
        String pdfPath = getPdfPathFromIntygVersion(utlatande, intygTexts, defaultTemplatePath);
        LOGGER.debug("Resolved pdfTemplate as " + pdfPath);
        return new PdfReader(pdfPath);
    }

    protected String getPdfPathFromIntygVersion(Utlatande utlatande, IntygTexts intygTexts, String defaultPath)
        throws SoSPdfGeneratorException {
        String textVersion = utlatande.getTextVersion();
        if (textVersion == null) {
            return defaultPath;
        }
        if (intygTexts == null) {
            return defaultPath;
        }
        return intygTexts.getProperties().getProperty(PDF_PATH_PROPERTY_KEY, defaultPath);
    }

    /**
     * Simple util method that helps translade a Boolean instance state to 3 possible value, one for null and others for
     * true/false.
     */
    protected String getRadioValueFromBoolean(Boolean booleanValue, String whenNull, String whenTrue, String whenFalse) {
        if (booleanValue == null) {
            return whenNull;
        } else {
            return booleanValue ? whenTrue : whenFalse;
        }

    }

    protected void fillText(String fieldId, InternalDate date) {
        try {
            assert fields.getFieldType(fieldId) == AcroFields.FIELD_TYPE_TEXT;
            if (date != null) {
                fields.setField(fieldId, date.getDate());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not fill field '" + fieldId + "' with value '" + date + "'", e);
        }
    }

    protected void fillText(String fieldId, String text) {
        try {
            assert fields.getFieldType(fieldId) == AcroFields.FIELD_TYPE_TEXT;
            if (text != null) {
                fields.setField(fieldId, text);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not fill field '" + fieldId + "' with value '" + text + "'", e);
        }
    }

    protected void checkRadioField(String fieldId, String value) {
        try {
            assert fields.getFieldType(fieldId) == AcroFields.FIELD_TYPE_RADIOBUTTON;
            fields.setField(fieldId, value, true);
        } catch (IOException | DocumentException e) {
            throw new IllegalArgumentException("Could not check radiofield '" + fieldId + "'", e);
        }
    }

    protected void checkCheckboxField(String fieldId, String value) {
        try {
            assert fields.getFieldType(fieldId) == AcroFields.FIELD_TYPE_CHECKBOX;
            fields.setField(fieldId, value, true);
        } catch (IOException | DocumentException e) {
            throw new IllegalArgumentException("Could not check checkboxfield '" + fieldId + "'", e);
        }
    }

    // Adjusts the position of the upper edge (array index 3) of a form field (InternalDate parameter)
    protected void adjustAndFill(String fieldId, InternalDate date, double adjustment) {
        if (date != null) {
            PdfArray fieldCoordinates = getFieldCoordinates(fieldId);
            //noinspection CheckStyle
            fieldCoordinates.set(3, new PdfNumber(fieldCoordinates.getAsNumber(3).doubleValue() - adjustment));
            fillText(fieldId, date);
        }
    }

    // Adjusts the position of the upper edge (array index 3) of a form field (String parameter)
    protected void adjustAndFill(String fieldId, String value, double adjustment) {
        if (value != null) {
            PdfArray fieldCoordinates = getFieldCoordinates(fieldId);
            //noinspection CheckStyle
            fieldCoordinates.set(3, new PdfNumber(fieldCoordinates.getAsNumber(3).doubleValue() - adjustment));
            fillText(fieldId, value);
        }
    }

    // Returns array holding the coordinates of the specified form field
    protected PdfArray getFieldCoordinates(String fieldId) {
        AcroFields.Item field = fields.getFieldItem(fieldId);
        PdfDictionary pdfDictionary = field.getWidget(0);
        return pdfDictionary.getAsArray(PdfName.RECT);
    }

    protected double getFieldWidth(String fieldId) {
        PdfArray fieldCoordinates = getFieldCoordinates(fieldId);
        return fieldCoordinates.getAsNumber(2).doubleValue() - fieldCoordinates.getAsNumber(0).doubleValue();
    }

    protected String truncateTextIfNeeded(String text, String fieldId) throws IOException, DocumentException {
        AcroFields.Item field = fields.getFieldItem(fieldId);
        PdfDictionary pdfDictionary = field.getWidget(0);
        String[] fontValues = pdfDictionary.getAsString(PdfName.DA).toString().split(" ");
        BaseFont bf = BaseFont.createFont();
        Font font = new Font(bf, Integer.parseInt(fontValues[1]));
        double fieldWidth = getFieldWidth(fieldId);
        if (text != null && !text.isEmpty()) {
            int rightIndex = text.length() - 1;
            float textWidth = font.getCalculatedBaseFont(true).getWidthPoint(text, font.getCalculatedSize());
            float symbolWidth = font.getCalculatedBaseFont(true).getWidthPoint(" ... ", font.getCalculatedSize());
            while (textWidth > fieldWidth) {
                rightIndex--;
                textWidth = symbolWidth
                    + font.getCalculatedBaseFont(true).getWidthPoint(text.substring(0, rightIndex), font.getCalculatedSize());
            }
            if (rightIndex != text.length() - 1) {
                if (text.charAt(rightIndex) == ' ') {
                    rightIndex--;
                }
                return text.substring(0, rightIndex) + "...";
            }
        }
        return text;
    }

    // Mark this document as a copy of an electronically signed document
    protected void markAsElectronicCopy(PdfStamper pdfStamper) throws DocumentException, IOException {
        mark(pdfStamper, ELECTRONIC_COPY_WATERMARK_TEXT, MARK_AS_COPY_START_X, MARK_AS_COPY_START_Y, MARK_AS_COPY_HEIGTH,
            MARK_AS_COPY_WIDTH);
    }

    protected void mark(PdfStamper pdfStamper, String watermarkText, int startX, int startY, int height, int width)
        throws DocumentException, IOException {
        PdfContentByte addOverlay;
        addOverlay = pdfStamper.getOverContent(1);
        addOverlay.saveState();
        addOverlay.setColorFill(CMYKColor.WHITE);
        addOverlay.setColorStroke(CMYKColor.RED);
        addOverlay.rectangle(startX, startY, width, height);
        addOverlay.stroke();
        addOverlay.restoreState();

        // Do text
        addOverlay = pdfStamper.getOverContent(1);
        ColumnText ct = new ColumnText(addOverlay);
        BaseFont bf = BaseFont.createFont();
        Font font = new Font(bf, WATERMARK_FONTSIZE);
        int llx = startX + WATERMARK_TEXT_PADDING;
        int lly = startY + WATERMARK_TEXT_PADDING;
        int urx = llx + width - 2 * WATERMARK_TEXT_PADDING;
        int ury = lly + height - 2 * WATERMARK_TEXT_PADDING;
        Phrase phrase = new Phrase(watermarkText, font);
        ct.setSimpleColumn(phrase, llx, lly, urx, ury, WATERMARK_FONTSIZE, Element.ALIGN_LEFT | Element.ALIGN_TOP);
        ct.go();
    }

    protected void addIntygStateWatermark(PdfStamper stamper, int nrPages, boolean isUtkast, boolean isMakulerad,
        boolean isLocked) {
        Phrase watermark;

        if (isLocked) {
            watermark = new Phrase(INTYG_STATEWATERMARK_LOCKED_TEXT, INTYG_STATEWATERMARK_FONT);
        } else if (isUtkast) {
            watermark = new Phrase(INTYG_STATEWATERMARK_DRAFT_TEXT, INTYG_STATEWATERMARK_FONT);
        } else if (isMakulerad) {
            watermark = new Phrase(INTYG_STATEWATERMARK_CANCELLED_TEXT, INTYG_STATEWATERMARK_FONT);
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
            gs1.setFillOpacity(INTYG_STATEWATERMARK_FILL_OPACITY);
            over.setGState(gs1);

            // Center the watermark text
            pageSize = over.getPdfDocument().getPageSize();
            final float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
            final float y = (pageSize.getTop() + pageSize.getBottom()) / 2;

            ColumnText.showTextAligned(over, Element.ALIGN_CENTER, watermark, x, y, INTYG_STATEWATERMARK_ROTATION);
            over.restoreState();
        }
    }

    protected void createRightMarginText(PdfStamper pdfStamper, int numberOfPages, String id, String text)
        throws DocumentException, IOException {
        PdfContentByte addOverlay;
        BaseFont bf = BaseFont.createFont();
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

    public String generatePdfFilename(LocalDateTime tidpunkt, String prefix) {
        String utskriftsTidpunkt = tidpunkt.format(DateTimeFormatter.ofPattern("yy-MM-dd_HHmm"));
        return String.format("%s_%s.pdf", prefix, utskriftsTidpunkt);
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

}
