package se.inera.intyg.common.pdf.renderer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.pdf.UPComponent;
import se.inera.intyg.common.services.texts.model.IntygTexts;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class UnifiedRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedRenderer.class);

    private Document document;
    private ScriptObjectMirror jsIntygModel;

    public byte[] render(String intygJsonModel, String upJsModel, IntygTexts intygTexts) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(bos);

            // Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            document = new Document(pdf);

            // Initialize script engine
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

            // Parse JSON intyg into JS object
            jsIntygModel = (ScriptObjectMirror) engine.eval("JSON.parse('" + intygJsonModel + "');");

            // Load unified print JS model
            InputStreamReader inputStreamReader = new InputStreamReader(IOUtils.toInputStream(upJsModel, Charset.forName("UTF-8")));
            engine.eval(inputStreamReader);
            ScriptObjectMirror viewConfig = (ScriptObjectMirror) engine.eval("viewConfig");

            // Fix all standard stuff...

            // Start traversing the jsIntygModel.
            if (!viewConfig.getClassName().equalsIgnoreCase("Array")) {
                throw new IllegalArgumentException("Root viewConfig must be of type Array");
            }

            UPComponent root = new ComponentTreeFactory(jsIntygModel).decorateWithComponentTree(viewConfig);


            document.add(new Paragraph("Läkarutlåtande för Sjukersättning").setFontSize(20f));
            document.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
