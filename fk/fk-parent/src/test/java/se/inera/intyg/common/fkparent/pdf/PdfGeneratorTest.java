/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.fkparent.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.fkparent.pdf.eventhandlers.PageNumberingEventHandler;
import se.inera.intyg.common.fkparent.pdf.model.FkLabel;
import se.inera.intyg.common.fkparent.pdf.model.FkPage;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.support.services.BefattningService;

/**
 * Created 24/11-16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class PdfGeneratorTest {

    private static final String TITLE = "title";

    private FkPdfDefinition buildPdfDefinition() throws java.lang.Exception {
        FkPdfDefinition def = new FkPdfDefinition();
        def.addPageEvent(new PageNumberingEventHandler());

        FkPage page = new FkPage();
        FkLabel mainHeader = new FkLabel("Test Header")
            .size(40, 12f);
        page.getChildren().add(mainHeader);

        def.addChild(page);
        return def;
    }

    @Test
    public void testGeneratePdf() throws Exception {
        FkPdfDefinition def = buildPdfDefinition();
        byte[] pdfFileBytes = PdfGenerator.generatePdf(def, TITLE);
        assertNotNull(pdfFileBytes);
        assertFalse(Arrays.equals(pdfFileBytes, new byte[0]));
    }

    @Test
    public void testGeneratePdfName() throws Exception {
        final String PREFIX = "test";
        final LocalDateTime tidpunkt = LocalDateTime.now();

        String expected = PREFIX + "_" + tidpunkt.format(DateTimeFormatter.ofPattern("yy-MM-dd_HHmm")) + ".pdf";

        String actual = PdfGenerator.generatePdfFilename(tidpunkt, PREFIX);

        assertEquals(expected, actual);
    }

}
