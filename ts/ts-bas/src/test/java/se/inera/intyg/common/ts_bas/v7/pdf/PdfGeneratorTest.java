/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v7.pdf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_bas.v7.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class PdfGeneratorTest {

    private final PdfGenerator testee = new PdfGenerator();

    private final WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Test
    public void testGeneratePdf() throws IOException, ModuleException {
        IntygTextsTsBasRepositoryTestHelper intygsTextRepositoryHelper = new IntygTextsTsBasRepositoryTestHelper();
        intygsTextRepositoryHelper.update();
        IntygTexts intygTexts = intygsTextRepositoryHelper.getTexts("ts-bas", "7.0");

        String jsonModel = IOUtils.toString(new ClassPathResource("v7/PdfGenerator/utlatande.json").getInputStream(),
            StandardCharsets.UTF_8);
        PdfResponse pdfResponse = testee.generatePdf(UUID.randomUUID().toString(), jsonModel,
            Personnummer.createPersonnummer("19121212-1212").get(), intygTexts,
            new ArrayList<>(), ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED, "footerAppName");
        assertNotNull(pdfResponse);
        Pattern p = Pattern.compile("^lakarintyg_transportstyrelsen_[\\d]{2}-[\\d]{2}-[\\d]{2}_[\\d]{4}\\.pdf$");
        assertTrue("Filename must match regexp.", p.matcher(pdfResponse.getFilename()).matches());
    }

    @Test
    public void testGeneratePdfUtkast() throws IOException, ModuleException {
        IntygTextsTsBasRepositoryTestHelper intygsTextRepositoryHelper = new IntygTextsTsBasRepositoryTestHelper();
        intygsTextRepositoryHelper.update();
        IntygTexts intygTexts = intygsTextRepositoryHelper.getTexts("ts-bas", "7.0");

        String jsonModel = IOUtils.toString(new ClassPathResource("v7/PdfGenerator/utkast_utlatande.json").getInputStream(),
            StandardCharsets.UTF_8);
        PdfResponse pdfResponse = testee.generatePdf(UUID.randomUUID().toString(), jsonModel,
            Personnummer.createPersonnummer("19121212-1212").get(), intygTexts,
            new ArrayList<>(), ApplicationOrigin.WEBCERT, UtkastStatus.SIGNED, "footerAppName");
        assertNotNull(pdfResponse);
        Pattern p = Pattern.compile("^lakarintyg_transportstyrelsen_[\\d]{2}-[\\d]{2}-[\\d]{2}_[\\d]{4}\\.pdf$");
        assertTrue("Filename must match regexp.", p.matcher(pdfResponse.getFilename()).matches());
    }
}
