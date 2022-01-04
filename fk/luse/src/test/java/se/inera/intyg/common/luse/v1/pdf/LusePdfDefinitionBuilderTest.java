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
package se.inera.intyg.common.luse.v1.pdf;

import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.support.FkAbstractModuleEntryPoint;
import se.inera.intyg.common.luse.v1.model.internal.LuseUtlatandeV1;
import se.inera.intyg.common.services.texts.IntygTextsServiceImpl;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * Generate variants of a LUSE pdf, partly to see that make sure no exceptions occur but mainly for manual visual inspection
 * of the resulting pdf files, as we don't have any way of programmatically assert the content of the pdf.
 *
 * @author marced
 */
@RunWith(MockitoJUnitRunner.class)
public class LusePdfDefinitionBuilderTest {

    protected static final String TEXT_VERSION_1_0 = "1.0";
    protected static final String TEXT_VERSION_1_1 = "1.1";
    protected static final String TEXT_VERSION_1_2 = "1.2";

    private static final String FKASSA_RECIPIENT_ID = "FKASSA";
    private static final String HSVARD_RECIPIENT_ID = "HSVARD";
    private ObjectMapper objectMapper = new CustomObjectMapper();

    private IntygTextsServiceImpl intygTextsService;
    private List<LuseUtlatandeV1> intygList = new ArrayList<>();

    @Mock
    FkAbstractModuleEntryPoint entryPoint;

    @InjectMocks
    private LusePdfDefinitionBuilder lusePdfDefinitionBuilder = new LusePdfDefinitionBuilder();

    @Before
    public void initTexts() throws IOException {
        intygTextsService = new IntygTextsServiceImpl();
        IntygTextsLuseRepositoryTestHelper intygsTextRepositoryHelper = new IntygTextsLuseRepositoryTestHelper();
        intygsTextRepositoryHelper.update();
        ReflectionTestUtils.setField(intygTextsService, "repo", intygsTextRepositoryHelper);

        intygList.add(
            objectMapper.readValue(new ClassPathResource("v1/PdfGeneratorTest/minimalt_utlatande.json").getFile(),
                    LuseUtlatandeV1.class));
        intygList.add(
            objectMapper.readValue(new ClassPathResource("v1/PdfGeneratorTest/fullt_utlatande.json").getFile(),
                    LuseUtlatandeV1.class));
        intygList.add(objectMapper
            .readValue(new ClassPathResource("v1/PdfGeneratorTest/overfyllnad_utlatande.json").getFile(),
                    LuseUtlatandeV1.class));
        intygList.add(objectMapper.readValue(
                new ClassPathResource("v1/PdfGeneratorTest/overfyllnad_cornercases_utlatande.json").getFile(),
                LuseUtlatandeV1.class));

    }

    @Test
    public void testGenerateNotSentToFK() throws Exception {
        generate("unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_0,
                UtkastStatus.SIGNED);
        generate("unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0,
                UtkastStatus.SIGNED);
        generate("unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_1,
                UtkastStatus.SIGNED);
        generate("unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1,
                UtkastStatus.SIGNED);
        generate("unsent", new ArrayList<>(), ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_2,
                UtkastStatus.SIGNED);
        generate("unsent", new ArrayList<>(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2,
                UtkastStatus.SIGNED);
    }

    @Test
    public void testGenerateAlreadySentTOFK() throws Exception {
        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status(CertificateState.SENT, FKASSA_RECIPIENT_ID, LocalDateTime.now()));

        generate("sent", statuses, ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_0, UtkastStatus.SIGNED);
        generate("sent", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0, UtkastStatus.SIGNED);
        generate("sent", statuses, ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_1, UtkastStatus.SIGNED);
        generate("sent", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1, UtkastStatus.SIGNED);
        generate("sent", statuses, ApplicationOrigin.MINA_INTYG, TEXT_VERSION_1_2, UtkastStatus.SIGNED);
        generate("sent", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2, UtkastStatus.SIGNED);

        //generate makulerat version
        statuses.clear();
        statuses.add(new Status(CertificateState.CANCELLED, HSVARD_RECIPIENT_ID, LocalDateTime.now()));
        generate("sent-makulerat", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0,
                UtkastStatus.SIGNED);
        generate("sent-makulerat", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1,
                UtkastStatus.SIGNED);
        generate("sent-makulerat", statuses, ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2,
                UtkastStatus.SIGNED);
    }

    @Test
    public void testGeneratePdfForUtkast() throws Exception {
        LuseUtlatandeV1 utkast = objectMapper
            .readValue(new ClassPathResource("v1/PdfGeneratorTest/utkast_utlatande.json").getFile(),
                    LuseUtlatandeV1.class);

        generate(utkast, "utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0,
                UtkastStatus.DRAFT_COMPLETE);
        generate(utkast, "utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1,
                UtkastStatus.DRAFT_COMPLETE);
        generate(utkast, "utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2,
                UtkastStatus.DRAFT_COMPLETE);
    }

    @Test
    public void testGeneratePdfForLockedUtkast() throws Exception {
        LuseUtlatandeV1 utkast = objectMapper
            .readValue(new ClassPathResource("v1/PdfGeneratorTest/utkast_utlatande.json").getFile(),
                    LuseUtlatandeV1.class);

        generate(utkast, "låst-utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_0,
                UtkastStatus.DRAFT_LOCKED);
        generate(utkast, "låst-utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_1,
                UtkastStatus.DRAFT_LOCKED);
        generate(utkast, "låst-utkast", Lists.newArrayList(), ApplicationOrigin.WEBCERT, TEXT_VERSION_1_2,
                UtkastStatus.DRAFT_LOCKED);
    }

    private void generate(String scenarioName, List<Status> statuses, ApplicationOrigin origin, String textVersion,
                          UtkastStatus utkastStatus)
            throws PdfGeneratorException, IOException {
        for (LuseUtlatandeV1 intyg : intygList) {
            generate(intyg, scenarioName, statuses, origin, textVersion, utkastStatus);
        }
    }

    private void generate(LuseUtlatandeV1 utlatandeV1, String scenarioName, List<Status> statuses,
                          ApplicationOrigin origin, String textVersion, UtkastStatus utkastStatus)
            throws PdfGeneratorException, IOException {
        FkPdfDefinition pdfDefinition = lusePdfDefinitionBuilder
                .buildPdfDefinition(utlatandeV1, statuses, origin,
                        intygTextsService.getIntygTextsPojo("luse", textVersion), utkastStatus);
        byte[] generatorResult = PdfGenerator.generatePdf(pdfDefinition);

        assertNotNull(generatorResult);
        writePdfToFile(generatorResult, origin, scenarioName, utlatandeV1.getId());
    }

    private void writePdfToFile(byte[] pdf, ApplicationOrigin origin, String scenarioName, String namingPrefix)
            throws IOException {
        String dir = "build/tmp";

        File file = new File(String.format("%s/%s-%s-%s-%s", dir, origin.name(), scenarioName, namingPrefix,
                "luse.pdf"));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

}
