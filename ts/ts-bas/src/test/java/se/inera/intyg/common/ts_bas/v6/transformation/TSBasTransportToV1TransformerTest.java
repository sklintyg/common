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
package se.inera.intyg.common.ts_bas.v6.transformation;

import static java.util.Arrays.asList;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;

public class TSBasTransportToV1TransformerTest {

    private static final String INTYGSTJANSTER_UTLATANDE_SCHEMA = "core_components/se_intygstjanster_services_1.0.xsd";

    private static final String INTYGSTJANSTER_UTLATANDE_TYPES_SCHEMA = "core_components/se_intygstjanster_services_types_1.0.xsd";

    private static final String INTYGSTJANSTER_REGISTER_SCHEMA = "interactions/RegisterTSBasInteraction/RegisterTSBasResponder_1.0.xsd";

    private static final String V1_TS_BAS_SCHEMA = "specializations/TS-Bas/ts-bas_model.xsd";

    private static final String V1_CORE_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_1.0.xsd";

    private static final String V1_TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_1.0.xsd";

    private static final String v1_REGISTER_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_1.0.xsd";

    private static Schema intygstjansterSchema;

    private static Schema v1Schema;

    @BeforeClass
    public static void initIntygstjansterSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(INTYGSTJANSTER_REGISTER_SCHEMA);
        schemaValidatorBuilder.registerResource(INTYGSTJANSTER_UTLATANDE_SCHEMA);
        schemaValidatorBuilder.registerResource(INTYGSTJANSTER_UTLATANDE_TYPES_SCHEMA);
        intygstjansterSchema = schemaValidatorBuilder.build(rootSource);
    }

    @BeforeClass
    public static void initV1Schema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(v1_REGISTER_SCHEMA);
        schemaValidatorBuilder.registerResource(V1_CORE_SCHEMA);
        schemaValidatorBuilder.registerResource(V1_TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(V1_TS_BAS_SCHEMA);
        v1Schema = schemaValidatorBuilder.build(rootSource);
    }

    @Test
    public void testTransformation() throws Exception {
        List<String> testFiles = asList("xsl.xml", "ts-bas-max.xml", "valid-diabetes-typ2-kost.xml",
            "valid-korrigerad-synskarpa.xml", "valid-maximal.xml", "valid-minimal.xml",
            "valid-persontransport.xml", "valid-sjukhusvard.xml", "valid-utan-korrigerad-synskarpa.xml",
            "valid-no-dash-personid-extension.xml");

        XslTransformer transformer = new XslTransformer("xsl/transportToV1.xsl");

        for (String xmlFile : testFiles) {
            String xmlContents = Resources.toString(getResource("v6/scenarios/transport/" + xmlFile), Charsets.UTF_8);

            if (!validateIntygstjansterXSD(xmlContents)) {
                fail();
            }

            String result = transformer.transform(xmlContents);

            if (!validateClinicalXSD(result)) {
                fail();
            }
        }
    }

    private static boolean validateIntygstjansterXSD(String xml) {
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        try {
            intygstjansterSchema.newValidator().validate(xmlSource);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean validateClinicalXSD(String xml) {
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        try {
            v1Schema.newValidator().validate(xmlSource);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

}
