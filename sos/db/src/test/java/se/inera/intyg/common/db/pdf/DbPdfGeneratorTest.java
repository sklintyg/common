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
package se.inera.intyg.common.db.pdf;

import static org.junit.Assert.assertEquals;
import static se.inera.intyg.common.db.pdf.DbPdfGenerator.DEFAULT_PDF_TEMPLATE;
import static se.inera.intyg.common.sos_parent.pdf.AbstractSoSPdfGenerator.PDF_PATH_PROPERTY_KEY;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

import se.inera.intyg.common.db.model.internal.DbUtlatande;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * Created by marced on 2017-10-11.
 */
@RunWith(MockitoJUnitRunner.class)
public class DbPdfGeneratorTest {

    protected IntygTexts intygTexts;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Before
    public void initTexts() throws IOException {
        Properties props = new Properties();
        props.put(PDF_PATH_PROPERTY_KEY, DEFAULT_PDF_TEMPLATE);
        intygTexts = new IntygTexts("1.0", "", null, null, null,
                null, props);
    }

    @Test
    public void testGenerate() throws Exception {
        testSingleScenario("DbPdfGenerator/dbUtlatande-all-true.json", "all-true");
        testSingleScenario("DbPdfGenerator/dbUtlatande-all-false.json", "all-false");
        testSingleScenario("DbPdfGenerator/dbUtlatande-utkast.json", "utkast");
    }

    @Test
    public void testCompareAcroFields() throws Exception {
        final File jsonFile = new ClassPathResource("DbPdfGenerator/dbUtlatande-all-true.json").getFile();
        DbUtlatande intyg = objectMapper.readValue(jsonFile, DbUtlatande.class);

        byte[] generatorResult = new DbPdfGenerator(intyg, intygTexts, new ArrayList<>(), false).getBytes();

        final AcroFields expectedFields = readAcroFields("DbPdfGenerator/dbUtlatande-all-true.pdf");

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : generatedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the expected",
                    expectedFields.getField(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    private void testSingleScenario(String jsonPath, String testName) throws Exception {
        final File file = new ClassPathResource(jsonPath).getFile();
        DbUtlatande intyg = objectMapper.readValue(file, DbUtlatande.class);
        byte[] generatorResult = new DbPdfGenerator(intyg, intygTexts, new ArrayList<>(), true).getBytes();
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-" + testName);
    }

    private AcroFields readAcroFields(String pathToPdf) throws IOException {
        PdfReader pdfReader = new PdfReader(new ClassPathResource(pathToPdf).getFile().getAbsolutePath());
        return pdfReader.getAcroFields();
    }

    private void writePdfToFile(byte[] pdf, ApplicationOrigin origin, String namingPrefix) throws IOException {
        String dir = "build/tmp";

        File file = new File(String.format("%s/%s-%s-%s", dir, origin.name(), namingPrefix, "-generator.pdf"));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

}
