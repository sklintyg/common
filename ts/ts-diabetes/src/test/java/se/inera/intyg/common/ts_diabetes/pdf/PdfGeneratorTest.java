/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.ts_diabetes.model.internal.TsDiabetesUtlatande;
import se.inera.intyg.common.ts_diabetes.utils.Scenario;
import se.inera.intyg.common.ts_diabetes.utils.ScenarioFinder;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class PdfGeneratorTest {

    private static final String TRANSPORTSTYRELSEN_RECIPIENT_ID = "TRANSP";
    private static final String HSVARD_RECIPIENT_ID = "HSVARD";
    @InjectMocks
    private PdfGeneratorImpl pdfGen = new PdfGeneratorImpl(true);

    @Mock
    private IntygTextsService intygTexts;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    private List<Status> defaultStatuses;

    @Test
    public void testGeneratePdf() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            byte[] pdf = pdfGen.generatePDF(scenario.asInternalModel(), defaultStatuses, ApplicationOrigin.MINA_INTYG, false);
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario);
        }
    }

    @Test
    public void testGenerateDraftPdf() throws Exception {
        final TsDiabetesUtlatande tsBasUtlatande = objectMapper
                .readValue(new ClassPathResource("PdfGenerator/ts-diabetes-utkast-utlatande.json").getFile(), TsDiabetesUtlatande.class);
        byte[] pdf = pdfGen.generatePDF(tsBasUtlatande, defaultStatuses, ApplicationOrigin.WEBCERT, true);
        writePdfToFile(pdf, "webcert-utkast");

        pdf = pdfGen.generatePDF(tsBasUtlatande, defaultStatuses, ApplicationOrigin.MINA_INTYG, true);
        writePdfToFile(pdf, "minaintyg-utkast");
    }

    @Test
    public void testGenerateMakuleratPdf() throws Exception {
        final TsDiabetesUtlatande tsBasUtlatande = objectMapper.readValue(
                new ClassPathResource("PdfGenerator/ts-diabetes-utlatande.json").getFile(),
                TsDiabetesUtlatande.class);
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, TRANSPORTSTYRELSEN_RECIPIENT_ID, LocalDateTime.now()));
        // generate makulerat version
        statuses.add(new Status(CertificateState.CANCELLED, HSVARD_RECIPIENT_ID, LocalDateTime.now()));
        byte[] pdf = pdfGen.generatePDF(tsBasUtlatande, statuses, ApplicationOrigin.WEBCERT, false);
        writePdfToFile(pdf, "webcert-makulerat");

        pdf = pdfGen.generatePDF(tsBasUtlatande, statuses, ApplicationOrigin.MINA_INTYG, false);
        writePdfToFile(pdf, "minaintyg-makulerat");
    }

    private void writePdfToFile(byte[] pdf, String prefix) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(
                String.format("%s/%s_%s.pdf", dir, prefix, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    private void writePdfToFile(byte[] pdf, Scenario scenario) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s_%s.pdf", dir, scenario.getName(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }
}
