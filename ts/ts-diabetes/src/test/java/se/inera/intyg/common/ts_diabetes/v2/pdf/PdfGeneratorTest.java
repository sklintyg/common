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
package se.inera.intyg.common.ts_diabetes.v2.pdf;

import static org.junit.Assert.assertNotNull;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.utils.Scenario;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioFinder;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class PdfGeneratorTest {

    private static final String TRANSPORTSTYRELSEN_RECIPIENT_ID = "TRANSP";
    private static final String HSVARD_RECIPIENT_ID = "HSVARD";
    @InjectMocks
    private PdfGeneratorImpl pdfGen = new PdfGeneratorImpl();

    @Mock
    private IntygTextsService intygTexts;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    private List<Status> defaultStatuses;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(pdfGen, "formFlattening", true);
    }

    public PdfGeneratorTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGeneratePdf() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            byte[] pdf = pdfGen.generatePDF(scenario.asInternalModel(), defaultStatuses, ApplicationOrigin.MINA_INTYG, UtkastStatus.SIGNED);
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario);
        }
    }

    @Test
    public void testGenerateDraftPdf() throws Exception {
        final TsDiabetesUtlatandeV2 tsBasUtlatande = objectMapper
            .readValue(new ClassPathResource("v2/PdfGenerator/ts-diabetes-utkast-utlatande.json").getFile(), TsDiabetesUtlatandeV2.class);
        byte[] pdf = pdfGen.generatePDF(tsBasUtlatande, defaultStatuses, ApplicationOrigin.WEBCERT, UtkastStatus.DRAFT_COMPLETE);
        writePdfToFile(pdf, "webcert-utkast");
    }

    @Test
    public void testGenerateLockedDraftPdf() throws Exception {
        final TsDiabetesUtlatandeV2 tsBasUtlatande = objectMapper
            .readValue(new ClassPathResource("v2/PdfGenerator/ts-diabetes-utkast-utlatande.json").getFile(), TsDiabetesUtlatandeV2.class);
        byte[] pdf = pdfGen.generatePDF(tsBasUtlatande, defaultStatuses, ApplicationOrigin.WEBCERT, UtkastStatus.DRAFT_LOCKED);
        writePdfToFile(pdf, "webcert-locked");
    }

    @Test
    public void testGenerateMakuleratPdf() throws Exception {
        final TsDiabetesUtlatandeV2 tsBasUtlatande = objectMapper.readValue(
            new ClassPathResource("v2/PdfGenerator/ts-diabetes-utlatande.json").getFile(),
            TsDiabetesUtlatandeV2.class);
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, TRANSPORTSTYRELSEN_RECIPIENT_ID, LocalDateTime.now()));
        // generate makulerat version
        statuses.add(new Status(CertificateState.CANCELLED, HSVARD_RECIPIENT_ID, LocalDateTime.now()));
        byte[] pdf = pdfGen.generatePDF(tsBasUtlatande, statuses, ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED);
        writePdfToFile(pdf, "webcert-makulerat");

        pdf = pdfGen.generatePDF(tsBasUtlatande, statuses, ApplicationOrigin.MINA_INTYG, UtkastStatus.SIGNED);
        writePdfToFile(pdf, "minaintyg-makulerat");
    }

    private void writePdfToFile(byte[] pdf, String prefix) throws IOException {
        String dir = "build/tmp";
        File file = new File(
            String.format("%s/%s_%s.pdf", dir, prefix, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    private void writePdfToFile(byte[] pdf, Scenario scenario) throws IOException {
        String dir = "build/tmp";
        File file = new File(String.format("%s/%s_%s.pdf", dir, scenario.getName(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }
}
