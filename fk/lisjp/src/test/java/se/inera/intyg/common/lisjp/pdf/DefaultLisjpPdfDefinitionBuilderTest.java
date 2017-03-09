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
package se.inera.intyg.common.lisjp.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.common.services.texts.IntygTextsServiceImpl;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Generate variants of a LISJP pdf, partly to see that make sure no exceptions occur but mainly for manual visual inspection
 * of the resulting pdf files, as we don't have any way of programmatically assert the content of the pdf.
 */
public class DefaultLisjpPdfDefinitionBuilderTest extends BaseLisjpPdfDefinitionBuilderTest{

    private DefaultLisjpPdfDefinitionBuilder lisjpPdfDefinitionBuilder = new DefaultLisjpPdfDefinitionBuilder();

    @Test
    public void testGenerateNotSentToFK() throws Exception {
        generate("default-unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG);
        generate("default-unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT);
    }

    @Test
    public void testGenerateAlreadySentTOFK() throws Exception {
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, PartKod.FKASSA.getValue(), LocalDateTime.now()));

        generate("default-sent", statuses, ApplicationOrigin.MINA_INTYG);
        generate("default-sent", statuses, ApplicationOrigin.WEBCERT);

        //generate makulerat version
        statuses.clear();
        statuses.add(new Status(CertificateState.CANCELLED, PartKod.HSVARD.getValue(), LocalDateTime.now()));
        generate("default-sent-makulerat", statuses, ApplicationOrigin.WEBCERT);
    }

    private void generate(String scenarioName, List<Status> statuses, ApplicationOrigin origin) throws PdfGeneratorException, IOException {
        for (LisjpUtlatande intyg : intygList) {
            FkPdfDefinition pdfDefinition = lisjpPdfDefinitionBuilder.buildPdfDefinition(intyg, statuses, origin, intygTexts);
            byte[] generatorResult = PdfGenerator
                    .generatePdf(pdfDefinition);

            assertNotNull(generatorResult);

            writePdfToFile(generatorResult, origin, scenarioName, intyg.getId());
        }
    }



}
