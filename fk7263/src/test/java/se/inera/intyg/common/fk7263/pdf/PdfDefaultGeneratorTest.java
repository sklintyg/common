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
package se.inera.intyg.common.fk7263.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.fk7263.utils.Scenario;
import se.inera.intyg.common.fk7263.utils.ScenarioFinder;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author andreaskaltenbach
 */
public class PdfDefaultGeneratorTest {

    private static final String HSVARD_RECIPIENT_ID = "HSVARD";
    private static File fk7263Pdf;
    private static File fk7263Json;
    private static File fk7263falt9bortaJson;
    private static File expectedPdfContent;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    @BeforeClass
    public static void readFiles() throws IOException {
        fk7263Pdf = new ClassPathResource("PdfGeneratorTest/utlatande.pdf").getFile();
        fk7263Json = new ClassPathResource("PdfGeneratorTest/utlatande.json").getFile();
        fk7263falt9bortaJson = new ClassPathResource("PdfGeneratorTest/falt9borta.json").getFile();
        expectedPdfContent = new ClassPathResource("PdfGeneratorTest/expectedPdfContent.json").getFile();
    }

    @Test
    public void testWCFields() throws IOException, PdfGeneratorException {

        @SuppressWarnings("unchecked")
        Map<String, String> pdfContent = objectMapper.readValue(expectedPdfContent, Map.class);

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<>(), ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED, false)
            .getBytes();
        AcroFields expectedFields = readExpectedFields();

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the expected",
                pdfContent.get(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    @Test
    public void testMIFields() throws IOException, PdfGeneratorException {

        @SuppressWarnings("unchecked")
        Map<String, String> pdfContent = objectMapper.readValue(expectedPdfContent, Map.class);

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<>(), ApplicationOrigin.MINA_INTYG, UtkastStatus.SIGNED, false)
            .getBytes();
        AcroFields expectedFields = readExpectedFields();

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the expected",
                pdfContent.get(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    @Test
    public void testWCWritePdf() throws Exception {

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED,
            false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-normal");
    }

    @Test
    public void testWCWriteUtkastPdf() throws Exception {

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);
        intyg.getGrundData().setSigneringsdatum(null);
        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT,
            UtkastStatus.DRAFT_COMPLETE, false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-UTKAST-normal");
    }

    @Test
    public void testWCWriteLockedUtkastPdf() throws Exception {

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);
        intyg.getGrundData().setSigneringsdatum(null);
        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT,
            UtkastStatus.DRAFT_LOCKED, false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-UTKAST-låst");
    }

    @Test
    public void testWCWriteMakuleradPdf() throws Exception {

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        //Add a makulerat state
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.CANCELLED, HSVARD_RECIPIENT_ID, LocalDateTime.now()));

        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, statuses, ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED, false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-MAKULERAT-normal");
    }

    @Test
    public void testMIWritePdf() throws Exception {

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.MINA_INTYG, UtkastStatus.SIGNED,
            false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.MINA_INTYG, "-normal");
    }


    /**
     * This test creates a new document to compare against. The new document ends up in the projects target root.
     */
    @Test
    public void testWCGenerateFromScenarios() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            byte[] pdf = new PdfDefaultGenerator(scenario.asInternalModel(), new ArrayList<Status>(), ApplicationOrigin.WEBCERT,
                UtkastStatus.SIGNED, false).getBytes();
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario, ApplicationOrigin.WEBCERT);
        }
    }

    /**
     * This test creates a new document to compare against. The new document ends up in the projects target root.
     */
    @Test
    public void testMIGenerateFromScenarios() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            byte[] pdf = new PdfDefaultGenerator(scenario.asInternalModel(), new ArrayList<Status>(), ApplicationOrigin.MINA_INTYG,
                UtkastStatus.SIGNED, false).getBytes();
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario, ApplicationOrigin.MINA_INTYG);
        }
    }


    @Test
    public void testWCWithFalt9Borta() throws Exception {
        Fk7263Utlatande intyg = objectMapper.readValue(fk7263falt9bortaJson, Fk7263Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED,
            false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "field9missing");
    }

    @Test
    public void testMIWithFalt9Borta() throws Exception {
        Fk7263Utlatande intyg = objectMapper.readValue(fk7263falt9bortaJson, Fk7263Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.MINA_INTYG, UtkastStatus.SIGNED,
            false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.MINA_INTYG, "field9missing");
    }

    @Test
    public void pdfGenerationRemovesFormFields() throws IOException, PdfGeneratorException {
        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);
        //Flatten the fields
        byte[] generatorResult = new PdfDefaultGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED,
            true).getBytes();

        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        assertEquals(0, generatedFields.getFields().size());
    }

    /**
     * This test assert that a user can print a Intyg of type FK7263 even if it hasn't yet been sent to FK.
     * - The target property of a Status object is null in this scenario.
     * - The type property of a Status object is anything but CertificateState.SENT
     */
    @Test
    public void testWCIntygIsSignedButNotSentToFK() throws Exception {
        // Given
        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.RECEIVED, null, LocalDateTime.now()));

        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, statuses, ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED, false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "default-signed-NOT-sent-to-fk");
    }

    /**
     * This test assert that a user can print a Intyg of type FK7263 after it has been sent to FK.
     * - The target property of a Status object is FK in this scenario.
     * - The type property of a Status object is CertificateState.SENT
     */
    @Test
    public void testWCIntygIsSignedAndSentToFK() throws Exception {
        // Given
        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, Fk7263EntryPoint.DEFAULT_RECIPIENT_ID, LocalDateTime.now()));

        // generate PDF
        byte[] generatorResult = new PdfDefaultGenerator(intyg, statuses, ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED, false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-signed-AND-sent-to-fk");
    }

    private void writePdfToFile(byte[] pdf, Scenario scenario, ApplicationOrigin origin) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s-%s-default-generator.pdf", dir, origin.name() + "-" + scenario.getName(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    private void writePdfToFile(byte[] pdf, ApplicationOrigin origin, String namingPrefix) throws IOException {
        String dir = "build/tmp";
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s-%s-%s", dir, origin.name(), namingPrefix, "-default-generator.pdf"));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    private AcroFields readExpectedFields() throws IOException {
        PdfReader pdfReader = new PdfReader(fk7263Pdf.getAbsolutePath());
        return pdfReader.getAcroFields();
    }
}
