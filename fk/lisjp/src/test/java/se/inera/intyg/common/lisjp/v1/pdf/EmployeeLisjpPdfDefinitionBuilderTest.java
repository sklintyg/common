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
package se.inera.intyg.common.lisjp.v1.pdf;

import static org.junit.Assert.assertNotNull;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_AKTIVITETSBEGRANSNING;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_ANLEDNING_TILL_FKKONTAKT;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_ARBETSTIDSFORLAGGNING_MOTIVERING;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_DIAGNOSER;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_FORSAKRINGSMEDICINSKS_BESLUTSTOD;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_FUNKTIONSNADSATTNING;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_GRUND_FOR_MU;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_KONTAKT_MED_FK;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_OVRIGT;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_PAGAENDE_BEHANDLING;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_PLANERAD_BEHANDLING;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_SMITTSKYDD;
import static se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder.OPT_SYSSELSATTNING_EJ_NUVARANDEARBETE;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * Generate variants of a LISJP pdf, partly to see that make sure no exceptions occur but mainly for manual visual
 * inspection
 * of the resulting pdf files, as we don't have any easy way of programmatically assert the content of the pdf.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeLisjpPdfDefinitionBuilderTest extends BaseLisjpPdfDefinitionBuilderTest {

    private static final String HSVARD_RECIPIENT_ID = "HSVARD";
    List<String> allOptionalFields = Arrays.asList(
        OPT_SMITTSKYDD,
        OPT_GRUND_FOR_MU,
        OPT_SYSSELSATTNING_EJ_NUVARANDEARBETE,
        OPT_FUNKTIONSNADSATTNING,
        OPT_AKTIVITETSBEGRANSNING,
        OPT_AKTIVITETSBEGRANSNING,
        OPT_DIAGNOSER,
        OPT_PAGAENDE_BEHANDLING,
        OPT_PLANERAD_BEHANDLING,
        OPT_FORSAKRINGSMEDICINSKS_BESLUTSTOD,
        OPT_ARBETSTIDSFORLAGGNING_MOTIVERING,
        OPT_OVRIGT,
        OPT_KONTAKT_MED_FK,
        OPT_ANLEDNING_TILL_FKKONTAKT,
        "9001",
        "9002");

    @Test
    public void testGenerateWithAllOptionalFields() throws Exception {

        List<Status> statuses = new ArrayList<>();
        generate("employee-all-optional", statuses, ApplicationOrigin.MINA_INTYG, allOptionalFields);

        // generate makulerat version
        statuses.clear();
        statuses.add(new Status(CertificateState.CANCELLED, HSVARD_RECIPIENT_ID, LocalDateTime.now()));
        generate("employee-makulerat", statuses, ApplicationOrigin.WEBCERT, allOptionalFields);
    }

    @Test
    public void testGenerateWithUnselectedOptionalField() throws Exception {

        List<Status> statuses = new ArrayList<>();
        generate("employee-unselected-optional-diagnose", statuses, ApplicationOrigin.MINA_INTYG, Arrays.asList("!" + OPT_DIAGNOSER));
        generate("employee-minimal", statuses, ApplicationOrigin.WEBCERT, null);

    }

    private void generate(String scenarioName, List<Status> statuses, ApplicationOrigin origin, List<String> optionalFields)
        throws PdfGeneratorException, IOException {
        EmployeeLisjpPdfDefinitionBuilder employeePdfBuilder = new EmployeeLisjpPdfDefinitionBuilder(optionalFields);
        for (LisjpUtlatandeV1 intyg : intygList) {
            FkPdfDefinition pdfDefinition = employeePdfBuilder
                .buildPdfDefinition(intyg, statuses, origin, intygTextsService.getIntygTextsPojo("lisjp", "1.0"), UtkastStatus.SIGNED);
            byte[] generatorResult = PdfGenerator
                .generatePdf(pdfDefinition);

            assertNotNull(generatorResult);

            writePdfToFile(generatorResult, origin, scenarioName, intyg.getId(), "1.0");
        }
    }

}
