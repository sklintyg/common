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
package se.inera.intyg.common.lisjp.v1.pdf;

import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
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
 * inspection of the resulting pdf files, as we don't have any way of programmatically assert the content of the pdf.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultLisjpPdfDefinitionBuilderTest extends BaseLisjpPdfDefinitionBuilderTest {

    protected static final String TEXT_VERSION_1_0 = "1.0";
    protected static final String TEXT_VERSION_1_1 = "1.1";
    protected static final String TEXT_VERSION_1_2 = "1.2";

    private final DefaultLisjpPdfDefinitionBuilder lisjpPdfDefinitionBuilder = new DefaultLisjpPdfDefinitionBuilder();

    @Test
    public void testGenerateNotSentToFK() throws Exception {
        generate("default-unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_0, UtkastStatus.SIGNED);
        generate("default-unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0, UtkastStatus.SIGNED);
        generate("default-unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_1, UtkastStatus.SIGNED);
        generate("default-unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1, UtkastStatus.SIGNED);
        generate("default-unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_2, UtkastStatus.SIGNED);
        generate("default-unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2, UtkastStatus.SIGNED);
    }

    @Test
    public void testGenerateAlreadySentTOFK() throws Exception {
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, "FKASSA", LocalDateTime.now()));

        generate("default-sent", statuses, ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_0, UtkastStatus.SIGNED);
        generate("default-sent", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0, UtkastStatus.SIGNED);
        generate("default-sent", statuses, ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_1, UtkastStatus.SIGNED);
        generate("default-sent", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1, UtkastStatus.SIGNED);
        generate("default-sent", statuses, ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_2, UtkastStatus.SIGNED);
        generate("default-sent", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2, UtkastStatus.SIGNED);

        // generate makulerat version
        statuses.clear();
        statuses.add(new Status(CertificateState.CANCELLED, "HSVARD", LocalDateTime.now()));
        generate("default-sent-makulerat", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0, UtkastStatus.SIGNED);
        generate("default-sent-makulerat", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1, UtkastStatus.SIGNED);
        generate("default-sent-makulerat", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2, UtkastStatus.SIGNED);
    }

    @Test
    public void testGenerateLockedUtkastPdf() throws Exception {
        LisjpUtlatandeV1 utkast = objectMapper
            .readValue(new ClassPathResource("v1/PdfGeneratorTest/utkast_utlatande.json").getFile(), LisjpUtlatandeV1.class);

        generate(utkast, "låst-utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0,
            UtkastStatus.DRAFT_LOCKED);
        generate(utkast, "låst-utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1,
            UtkastStatus.DRAFT_LOCKED);
        generate(utkast, "låst-utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2,
            UtkastStatus.DRAFT_LOCKED);
    }

    @Test
    public void testGenerateUtkastPdf() throws Exception {
        LisjpUtlatandeV1 utkast = objectMapper
            .readValue(new ClassPathResource("v1/PdfGeneratorTest/utkast_utlatande.json").getFile(), LisjpUtlatandeV1.class);

        generate(utkast, "utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0,
            UtkastStatus.DRAFT_COMPLETE);
        generate(utkast, "utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1,
            UtkastStatus.DRAFT_COMPLETE);
        generate(utkast, "utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2,
            UtkastStatus.DRAFT_COMPLETE);
    }

    private void generate(String scenarioName, List<Status> statuses, ApplicationOrigin origin, String textVersion,
        UtkastStatus utkastStatus)
        throws PdfGeneratorException, IOException {
        for (LisjpUtlatandeV1 intyg : intygList) {
            generate(intyg, scenarioName, statuses, origin, textVersion, utkastStatus);
        }
    }

    private void generate(LisjpUtlatandeV1 utlatandeV1, String scenarioName, List<Status> statuses, ApplicationOrigin origin,
        String textVersion, UtkastStatus utkastStatus)
        throws PdfGeneratorException, IOException {
        FkPdfDefinition pdfDefinition = lisjpPdfDefinitionBuilder
            .buildPdfDefinition(utlatandeV1, statuses, origin,
                intygTextsService.getIntygTextsPojo("lisjp", textVersion), utkastStatus);
        byte[] generatorResult = PdfGenerator.generatePdf(pdfDefinition);

        assertNotNull(generatorResult);
        writePdfToFile(generatorResult, origin, scenarioName, utlatandeV1.getId(), textVersion);
    }

}
