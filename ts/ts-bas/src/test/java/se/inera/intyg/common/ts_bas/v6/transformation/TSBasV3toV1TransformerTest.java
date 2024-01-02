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
package se.inera.intyg.common.ts_bas.v6.transformation;

import static java.util.Arrays.asList;
import static org.junit.Assert.fail;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.collection.pair.Pair;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;

public class TSBasV3toV1TransformerTest {

    private static final String V3_UTLATANDE_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_3.3.xsd";
    private static final String V3_UTLATANDE_SIG_SCHEMA = "core_components/xmldsig-core-schema_0.1.xsd";
    private static final String V3_UTLATANDE_TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_3.2.xsd";
    private static final String V3_UTLATANDE_TYPES_EXT_SCHEMA_32 = "core_components/clinicalprocess_healthcond_certificate_3.2_ext.xsd";
    private static final String V3_UTLATANDE_TYPES_EXT_SCHEMA_34 = "core_components/clinicalprocess_healthcond_certificate_3.4_ext.xsd";
    private static final String V3_REGISTER_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_3.1.xsd";
    private static final String V1_TS_BAS_SCHEMA = "specializations/TS-Bas/ts-bas_model.xsd";
    private static final String V1_CORE_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_1.0.xsd";
    private static final String V1_TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_1.0.xsd";
    private static final String V1_REGISTER_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_1.0.xsd";

    private static Schema v3Schema;

    private static Schema v1Schema;

    @BeforeClass
    public static void initV3Schema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(V3_REGISTER_SCHEMA);
        schemaValidatorBuilder.registerResource(V3_UTLATANDE_SCHEMA);
        schemaValidatorBuilder.registerResource(V3_UTLATANDE_TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(V3_UTLATANDE_TYPES_EXT_SCHEMA_32);
        schemaValidatorBuilder.registerResource(V3_UTLATANDE_TYPES_EXT_SCHEMA_34);
        schemaValidatorBuilder.registerResource(V3_UTLATANDE_SIG_SCHEMA);
        v3Schema = schemaValidatorBuilder.build(rootSource);
    }

    @BeforeClass
    public static void initV1Schema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(V1_REGISTER_SCHEMA);
        schemaValidatorBuilder.registerResource(V1_CORE_SCHEMA);
        schemaValidatorBuilder.registerResource(V1_TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(V1_TS_BAS_SCHEMA);

        v1Schema = schemaValidatorBuilder.build(rootSource);
    }

    @Test
    public void testTransformation() throws Exception {
        List<String> testFiles = asList("valid-diabetes-typ2-kost.xml",
            "valid-korrigerad-synskarpa.xml", "valid-maximal.xml", "valid-minimal.xml",
            "valid-persontransport.xml", "valid-sjukhusvard.xml", "valid-utan-korrigerad-synskarpa.xml");

        XslTransformer transformer = new XslTransformer("xsl/V3ToV1.xsl");

        for (String xmlFile : testFiles) {
            String xmlContents = Resources.toString(getResource("v6/scenarios/rivtav3/" + xmlFile), Charsets.UTF_8);
            List v3Result = validate(v3Schema, xmlContents);
            if (!v3Result.isEmpty()) {
                fail(xmlFile + " failed to validate against schema v3 with errors " + v3Result.toString());
            }

            String result = transformer.transform(xmlContents);

            List v1Results = validate(v1Schema, result);
            if (!v1Results.isEmpty()) {
                fail(xmlFile + " failed to validate against schema v1 with errors " + v1Results.toString());
            }
        }
    }

    private static List<SAXParseException> validate(Schema schema, String xml) {
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));

        Pair<Validator, ArrayList<SAXParseException>> validatorObject = setupValidator(schema);
        Validator validator = validatorObject.getFirst();
        ArrayList<SAXParseException> exceptions = validatorObject.getSecond();
        try {
            validator.validate(xmlSource);
        } catch (Exception ex) {
            ex.printStackTrace();
            return exceptions;
        }
        return exceptions;
    }

    private static Pair<Validator, ArrayList<SAXParseException>> setupValidator(Schema v1Schema) {
        Validator validator = v1Schema.newValidator();
        final ArrayList<SAXParseException> exceptions = new ArrayList<>();
        Pair<Validator, ArrayList<SAXParseException>> ret = new Pair<>(validator, exceptions);
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) {
                exceptions.add(exception);
            }

            @Override
            public void fatalError(SAXParseException exception) {
                exceptions.add(exception);
            }

            @Override
            public void error(SAXParseException exception) {
                exceptions.add(exception);
            }
        });
        return ret;
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

}
