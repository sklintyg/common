/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.db.v1.validator;

import com.google.common.base.Charsets;
import com.helger.commons.debug.GlobalDebug;
import com.helger.schematron.svrl.SVRLHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.db.v1.rest.DbModuleApiV1;
import se.inera.intyg.common.db.v1.utils.Scenario;
import se.inera.intyg.common.db.v1.utils.ScenarioFinder;
import se.inera.intyg.common.db.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static se.inera.intyg.common.sos_parent.validator.ValidatorTestUtil.getInternalValidationErrorString;
import static se.inera.intyg.common.sos_parent.validator.ValidatorTestUtil.getTransportValidationErrorString;
import static se.inera.intyg.common.sos_parent.validator.ValidatorTestUtil.getXmlFromModel;

@RunWith(Parameterized.class)
public class InternalValidatorResultMatchesSchematronValidatorTest {

    // Used for labeling tests.
    private static String name;

    private static InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl();

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    private Scenario scenario;
    private boolean shouldFail;

    public InternalValidatorResultMatchesSchematronValidatorTest(String name, Scenario scenario, boolean shouldFail) {
        this.scenario = scenario;
        this.shouldFail = shouldFail;
        InternalValidatorResultMatchesSchematronValidatorTest.name = name;
    }

    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        Collection<Object[]> ret = ScenarioFinder.getInternalScenarios("fail-*").stream()
            .map(u -> new Object[]{u.getName(), u, true})
            .collect(Collectors.toList());
        ret.addAll(ScenarioFinder.getInternalScenarios("pass-*").stream()
            .map(u -> new Object[]{u.getName(), u, false})
            .collect(Collectors.toList()));
        return ret;
    }

    private static void doInternalAndSchematronValidation(Scenario scenario, boolean fail) throws Exception {
        DbUtlatandeV1 utlatandeFromJson = fail ?
            scenario.asInternalModel() :
            InternalValidatorTest.setupUtlatandeDates(scenario.asInternalModel());

        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);

        RegisterCertificateType intyg = scenario.asTransportModel();
        String convertedXML = getXmlFromModel(intyg);

        RegisterCertificateValidator validator = new RegisterCertificateValidator(DbModuleApiV1.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));

        String internalValidationErrors = getInternalValidationErrorString(internalValidationResponse);

        String transportValidationErrors = getTransportValidationErrorString(result);

        doAssertions(fail, internalValidationResponse, result, internalValidationErrors, transportValidationErrors);
    }

    private static void doAssertions(boolean fail, ValidateDraftResponse internalValidationResponse, SchematronOutputType result,
        String internalValidationErrors, String transportValidationErrors) {
        if (fail) {
            assertTrue(String.format("File: %s, Internal validation, expected ValidationStatus.INVALID", name),
                internalValidationResponse.getStatus().equals(ValidationStatus.INVALID));

            assertTrue(String.format("File: %s, Schematronvalidation, expected errors > 0", name),
                SVRLHelper.getAllFailedAssertions(result).size() > 0);
        } else {
            assertTrue(String.format("File: %s, Internal validation, expected ValidationStatus.VALID \n Validation-errors: %s", name,
                internalValidationErrors), internalValidationResponse.getStatus().equals(ValidationStatus.VALID));
            assertTrue(String.format("File: %s, Schematronvalidation, expected 0 errors \n Validation-errors: %s", name,
                transportValidationErrors), SVRLHelper.getAllFailedAssertions(result).size() == 0);
        }
    }

    @Test
    public void testScenarios() throws Exception {
        doInternalAndSchematronValidation(scenario, shouldFail);
    }
}
