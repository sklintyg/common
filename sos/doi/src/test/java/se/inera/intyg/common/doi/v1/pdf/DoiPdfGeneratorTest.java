/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.doi.v1.pdf;

import static org.junit.Assert.assertEquals;
import static se.inera.intyg.common.doi.v1.pdf.DoiPdfGenerator.DEFAULT_PDF_TEMPLATE;
import static se.inera.intyg.common.sos_parent.pdf.AbstractSoSPdfGenerator.PDF_PATH_PROPERTY_KEY;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * Created by marced on 2017-10-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class DoiPdfGeneratorTest {

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
        testSingleScenario("v1/DoiPdfGenerator/doiUtlatande-default.json", "default", UtkastStatus.SIGNED);
        testSingleScenario("v1/DoiPdfGenerator/doiUtlatande-utkast.json", "utkast", UtkastStatus.DRAFT_COMPLETE);
        testSingleScenario("v1/DoiPdfGenerator/doiUtlatande-utkast.json", "utkast-locked", UtkastStatus.DRAFT_LOCKED);
    }

    @Test
    public void testCompareAcroFields() throws Exception {
        final File jsonFile = new ClassPathResource("v1/DoiPdfGenerator/doiUtlatande-default.json").getFile();
        DoiUtlatandeV1 intyg = objectMapper.readValue(jsonFile, DoiUtlatandeV1.class);

        byte[] generatorResult = new DoiPdfGenerator(intyg, intygTexts, new ArrayList<>(), UtkastStatus.SIGNED, false).getBytes();

        final AcroFields expectedFields = readAcroFields("v1/DoiPdfGenerator/doiUtlatande-default.pdf");

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
/*        for (String fieldKey : generatedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the expected",
                expectedFields.getField(fieldKey), generatedFields.getField(fieldKey));
        }
 */
    }

    private void testSingleScenario(String jsonPath, String testName, UtkastStatus utkastStatus) throws Exception {
        final File file = new ClassPathResource(jsonPath).getFile();
        DoiUtlatandeV1 intyg = objectMapper.readValue(file, DoiUtlatandeV1.class);
        byte[] generatorResult = new DoiPdfGenerator(intyg, intygTexts, new ArrayList<>(), utkastStatus).getBytes();
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
