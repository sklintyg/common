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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;

/**
 * Generic PDF renderer that delegates (almost) all rendering logic to a given model.
 * The aim is that it could be used for rendering all SIT type PDFs.
 *
 * Created by marced on 30/09/16.
 */
// CHECKSTYLE:OFF MagicNumber
public final class PdfGenerator {

    private PdfGenerator() {
    }

    public static byte[] generatePdf(FkPdfDefinition model) throws PdfGeneratorException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {

            Document document = new Document();
            document.setPageSize(PageSize.A4);
            document.setMargins(model.getPageMargins()[0], model.getPageMargins()[1], model.getPageMargins()[2], model.getPageMargins()[3]);

            PdfWriter writer = PdfWriter.getInstance(document, bos);

            //Add preference to viewer applications to NOT scale when printing (it's just a hint, the user can change this)
            writer.addViewerPreference(PdfName.PRINTSCALING, PdfName.NONE);

            // Add specified event handlers
            for (PdfPageEventHelper eventHelper : model.getPageEvents()) {
                writer.setPageEvent(eventHelper);
            }

            document.open();

            //Leave actual rendering to the model, giving it starting x/y offset of top-left corner.
            model.render(document, writer, 0f, Utilities.pointsToMillimeters(document.getPageSize().getTop()));

            // Finish off by closing the document (this will invoke any page event handlers)
            document.close();

        } catch (DocumentException | RuntimeException e) {
            throw new PdfGeneratorException("Failed to create PDF", e);
        }

        return bos.toByteArray();
    }

    public static String generatePdfFilename(LocalDateTime tidpunkt, String fileNamePrefix) {
        final String utskriftsTidpunkt = tidpunkt.format(DateTimeFormatter.ofPattern("yy-MM-dd_HHmm"));
        return String.format("%s_%s.pdf", fileNamePrefix, utskriftsTidpunkt);
    }
}
