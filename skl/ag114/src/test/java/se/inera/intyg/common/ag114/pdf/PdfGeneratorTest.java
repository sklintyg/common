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
package se.inera.intyg.common.ag114.pdf;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.schemas.contract.Personnummer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.common.ag114.pdf.PdfGenerator.CERTIFICATE_FILE_PREFIX;

/**
 * Created by marced on 2018-11-02.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class PdfGeneratorTest {

    private PdfGenerator testee = new PdfGenerator();

    @Test
    public void testGeneratePdf() throws IOException, ModuleException {
        IntygTextsAg114RepositoryTestHelper intygsTextRepositoryHelper = new IntygTextsAg114RepositoryTestHelper();
        intygsTextRepositoryHelper.update();
        IntygTexts intygTexts = intygsTextRepositoryHelper.getTexts(Ag114EntryPoint.MODULE_ID, "1.0");

        String jsonModel = IOUtils.toString(new ClassPathResource("v1/internal/scenarios/pass-minimal.json").getInputStream(),
            Charset.forName("UTF-8"));
        PdfResponse pdfResponse = testee
            .generatePdf(UUID.randomUUID().toString(), jsonModel, "1", Personnummer.createPersonnummer("19121212-1212").get(), intygTexts,
                new ArrayList<>(), ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED, null);
        assertNotNull(pdfResponse);
        Pattern p = Pattern.compile("^" + CERTIFICATE_FILE_PREFIX + "[\\d]{2}_[\\d]{2}_[\\d]{2}_[\\d]{4}\\.pdf$");
        assertTrue("Filename must match regexp.", p.matcher(pdfResponse.getFilename()).matches());
    }

}
