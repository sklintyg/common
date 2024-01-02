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
package se.inera.intyg.common.lisjp.v1.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.services.texts.IntygTextsServiceImpl;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepositoryImpl;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * Created by marced on 2017-03-08.
 */

@ContextConfiguration(classes = {BaseLisjpPdfDefinitionBuilderTest.TestConfiguration.class})
@TestPropertySource(properties = {
    "texts.file.directory=classpath:v1/text",
})
public abstract class BaseLisjpPdfDefinitionBuilderTest {

    protected List<LisjpUtlatandeV1> intygList = new ArrayList<>();

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    IntygTextsServiceImpl intygTextsService;

    @Autowired
    IntygTextsRepositoryImpl repo;

    @Before
    public void initIntyg() throws IOException {

        intygList.add(
            objectMapper.readValue(new ClassPathResource("v1/PdfGeneratorTest/minimalt_utlatande.json").getFile(), LisjpUtlatandeV1.class));
        intygList.add(
            objectMapper.readValue(new ClassPathResource("v1/PdfGeneratorTest/maximalt_utlatande.json").getFile(), LisjpUtlatandeV1.class));
        intygList.add(objectMapper
            .readValue(new ClassPathResource("v1/PdfGeneratorTest/tillaggsfragor_utlatande.json").getFile(), LisjpUtlatandeV1.class));
    }

    void writePdfToFile(byte[] pdf, ApplicationOrigin origin, String scenarioName, String namingPrefix, String textVersion)
        throws IOException {
        String dir = "build/tmp";
        File file = new File(String.format("%s/%s-%s-%s-v%s-%s", dir, origin.name(), scenarioName, namingPrefix, textVersion, "lisjp.pdf"));
        file.getParentFile().mkdirs();
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    //Expose autowire candidates to Spring
    @Configuration
    static class TestConfiguration {

        @Bean
        public ResourceLoader resourceLoader() {
            return new DefaultResourceLoader();
        }

        @Bean
        public IntygTextsRepository repo() {
            return new IntygTextsRepositoryImpl();
        }

        @Bean
        public IntygTextsService intygTextsService() {
            return new IntygTextsServiceImpl();
        }

        @Bean
        public CustomObjectMapper customObjectMapper() {
            return new CustomObjectMapper();
        }
    }

}
