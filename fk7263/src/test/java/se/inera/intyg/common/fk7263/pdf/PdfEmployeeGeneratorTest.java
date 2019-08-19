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
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.utils.Scenario;
import se.inera.intyg.common.fk7263.utils.ScenarioFinder;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.LocalDateInterval;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author andreaskaltenbach
 */
public class PdfEmployeeGeneratorTest {

    private static File fk7263Json;
    private static File expectedPdfContentEmployerWC;
    private static File expectedPdfContentEmployerMIFull;
    private static File expectedPdfContentEmployerMIMinimal;
    private static File fk7263Pdf;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    @BeforeClass
    public static void readFiles() throws IOException {
        fk7263Json = new ClassPathResource("PdfGeneratorTest/utlatande.json").getFile();
        expectedPdfContentEmployerWC = new ClassPathResource("PdfGeneratorTest/expectedPdfContentEmployerWC.json").getFile();
        expectedPdfContentEmployerMIFull = new ClassPathResource("PdfGeneratorTest/expectedPdfContentEmployerMIFull.json").getFile();
        expectedPdfContentEmployerMIMinimal = new ClassPathResource("PdfGeneratorTest/expectedPdfContentEmployerMIMinimal.json").getFile();
        fk7263Pdf = new ClassPathResource("PdfGeneratorTest/utlatande.pdf").getFile();
    }

    @Test
    public void testPdfFileName() throws Exception {
        final Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("191212121212").get());
        intyg.getGrundData().setPatient(patient);
        intyg.setGiltighet(new LocalDateInterval(LocalDate.parse("2016-08-15"), LocalDate.parse("2016-10-30")));

        LocalDateTime tidpunkt = LocalDateTime.now();
        String formattedTidpunkt = tidpunkt.format(DateTimeFormatter.ofPattern("yy-MM-dd_HHmm"));
        // generate PDF
        final PdfEmployeeGenerator pdfEmployeeGenerator = new PdfEmployeeGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.MINA_INTYG, null, UtkastStatus.SIGNED);

        assertEquals("lakarintyg_fk7263_" + formattedTidpunkt + ".pdf", pdfEmployeeGenerator.generatePdfFilename(tidpunkt, false));
        assertEquals("minimalt_lakarintyg_fk7263_" + formattedTidpunkt + ".pdf", pdfEmployeeGenerator.generatePdfFilename(tidpunkt, true));

        assertTrue("Pdf should be customized", pdfEmployeeGenerator.isCustomized());
    }

    /**
     * This test creates a new document to compare against. The new document ends up in the project's target root.
     */
    @Test
    public void testWCGenerateFromScenarios() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            byte[] pdf = new PdfEmployeeGenerator(scenario.asInternalModel(), new ArrayList<Status>(), ApplicationOrigin.WEBCERT, null, UtkastStatus.SIGNED).getBytes();
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario, ApplicationOrigin.WEBCERT);
        }
    }

    /**
     * This test creates a new document to compare against. The new document ends up in the project's target root.
     */
    @Test
    public void testMIGenerateFromScenarios() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            byte[] pdf = new PdfEmployeeGenerator(scenario.asInternalModel(), new ArrayList<Status>(), ApplicationOrigin.MINA_INTYG, null, UtkastStatus.SIGNED).getBytes();
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario, ApplicationOrigin.MINA_INTYG);
        }
    }

    @Test
    public void testWCFieldsWhenGeneratingEmployerCopy() throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> pdfContent = objectMapper.readValue(expectedPdfContentEmployerWC, Map.class);

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        // generate PDF
        byte[] generatorResult = new PdfEmployeeGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, null, UtkastStatus.SIGNED, false).getBytes();
        AcroFields expectedFields = readExpectedFields();

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the",
                    pdfContent.get(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    @Test
    public void testWCWriteEmployerCopy() throws Exception {
        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfEmployeeGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, null, UtkastStatus.SIGNED).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-normal");
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
        byte[] generatorResult = new PdfEmployeeGenerator(intyg, statuses, ApplicationOrigin.WEBCERT, null, UtkastStatus.SIGNED).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-signed-NOT-sent-to-fk");
    }

    /**
     * This test assert that a user can print a Intyg of type FK7263 after it has been sent to FK.
     * - The target property of a Status object is FK in this scenario.
     * - The type property of a Status object is CertificateState.SENT
     *
     * @throws Exception
     */
    @Test
    public void testWCIntygIsSignedAndSentToFKThenGeneratePDF() throws Exception {
        // Given
        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, "FK", LocalDateTime.now()));

        // generate PDF
        byte[] generatorResult = new PdfEmployeeGenerator(intyg, statuses, ApplicationOrigin.WEBCERT, null, UtkastStatus.SIGNED, false).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-signed-AND_sent-to-fk");
    }

    @Test
    public void testMIEmployerCopyWithNoOptionalFieldsSelected() throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> pdfContent = objectMapper.readValue(expectedPdfContentEmployerMIMinimal, Map.class);

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        List<String> optionalFields = new ArrayList<>();

        byte[] generatorResult = new PdfEmployeeGenerator(intyg, new ArrayList<>(), ApplicationOrigin.MINA_INTYG, optionalFields, UtkastStatus.SIGNED, false).getBytes();

        // Get all available fields in a fk7263 pdf
        AcroFields expectedFields = readExpectedFields();

        // read actual/expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the",
                    pdfContent.get(fieldKey), generatedFields.getField(fieldKey));
        }
        writePdfToFile(generatorResult, ApplicationOrigin.MINA_INTYG, "-no-optional-fields");
    }

    @Test
    public void testMIEmployerCopyWithAllOptionalFieldsSelected() throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> pdfContent = objectMapper.readValue(expectedPdfContentEmployerMIFull, Map.class);

        Fk7263Utlatande intyg = objectMapper.readValue(fk7263Json, Fk7263Utlatande.class);

        // generate PDF
        List<String> allFields = Stream.of(EmployeeOptionalFields.values()).map(EmployeeOptionalFields::value).collect(Collectors.toList());

        byte[] generatorResult = new PdfEmployeeGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.MINA_INTYG, allFields, UtkastStatus.SIGNED, false).getBytes();

        // Get all available fields in a fk7263 pdf
        AcroFields expectedFields = readExpectedFields();

        // read actual/expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the",
                    pdfContent.get(fieldKey), generatedFields.getField(fieldKey));
        }
        writePdfToFile(generatorResult, ApplicationOrigin.MINA_INTYG, "-all-optional-fields");
    }

    private void writePdfToFile(byte[] pdf, Scenario scenario, ApplicationOrigin origin) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        System.err.println(dir);
        if (dir == null) {
            return;
        }

        File file = new File(
                String.format("%s/%s-%s-employee-generator.pdf", dir, origin.name() + "-" + scenario.getName(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    private void writePdfToFile(byte[] pdf, ApplicationOrigin origin, String namingPrefix) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s-%s-%s", dir, origin.name(), namingPrefix, "-employee-generator.pdf"));
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
