package se.inera.intyg.common.pdf.handler;

import java.io.IOException;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

public class PageNumberHandler implements IEventHandler {
    protected String country;

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfPage page = docEvent.getPage();
        int pageNum = docEvent.getDocument().getPageNumber(page);
        PdfCanvas canvas = new PdfCanvas(page);
        canvas.beginText();
        try {
            canvas.setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12);
        } catch (IOException e) {
            e.printStackTrace();
        }
        canvas.moveText(34, 803);
        canvas.showText(country);
        canvas.moveText(450, 0);
        canvas.showText(String.format("Sida %d(", pageNum));
        canvas.endText();
        canvas.stroke();
       // canvas.addXObject(template, 0, 0);
        canvas.release();
    }

    public void setHeader(String country) {
        this.country = country;
    }
}
