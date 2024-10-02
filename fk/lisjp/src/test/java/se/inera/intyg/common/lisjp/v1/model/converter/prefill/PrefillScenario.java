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
package se.inera.intyg.common.lisjp.v1.model.converter.prefill;

import jakarta.xml.bind.JAXBElement;
import java.io.IOException;
import org.apache.cxf.helpers.IOUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v3.CreateDraftCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

public class PrefillScenario {

    private static final String PREFILL_RESOURCES_PATH = "classpath:/v1/prefill/";
    private static final String TRANSPORT_MODEL_EXT = ".xml";
    private static final String INTERNAL_MODEL_EXT = ".json";


    private Forifyllnad forifyllnad;
    private LisjpUtlatandeV1 utlatande;


    public PrefillScenario(String scenarioName) {
        try {
            this.utlatande = getAsUtlatande(scenarioName);
            this.forifyllnad = getForifyllnadFromCreateDraftRequest(scenarioName);
        } catch (IOException e) {
            throw new AssertionError("Failed to load scenario resources for scenario " + scenarioName, e);
        }


    }

    public Forifyllnad getForifyllnad() {
        return forifyllnad;
    }

    public LisjpUtlatandeV1 getUtlatande() {
        return utlatande;
    }

    private String fileToString(String location) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        try {
            Resource resource = context.getResource(location);
            return IOUtils.toString(resource.getInputStream());
        } finally {
            context.close();
        }
    }

    private Forifyllnad getForifyllnadFromCreateDraftRequest(String scenarioFileName) throws IOException {
        JAXBElement<CreateDraftCertificateType> rct =
            XmlMarshallerHelper.unmarshal(
                fileToString(PREFILL_RESOURCES_PATH + scenarioFileName + TRANSPORT_MODEL_EXT));
        return rct.getValue().getIntyg().getForifyllnad();
    }

    private LisjpUtlatandeV1 getAsUtlatande(String scenarioFileName) throws IOException {

        return new CustomObjectMapper().readValue(
            fileToString(PREFILL_RESOURCES_PATH + scenarioFileName + INTERNAL_MODEL_EXT), LisjpUtlatandeV1.class);

    }
}
