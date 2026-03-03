/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.fkparent.integration.stub;

import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.MAKULERAD;
import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.MAKULERAD_NEJ;
import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.PERSONNUMMER;

import jakarta.xml.ws.WebServiceProvider;
import java.util.HashMap;
import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.support.stub.MedicalCertificatesStore;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

/**
 * Stub implementation of {@link RegisterCertificateResponderInterface} (RIV TA RegisterCertificate v3)
 * used in integration-test and development environments (Spring profiles {@code dev} and {@code it-fk-stub}).
 *
 * <p>Certificates received are stored in an optional {@link MedicalCertificatesStore} bean (if present
 * in the application context) so that integration tests can inspect what was registered. A call to the
 * logical address {@code "fail-adress"} (sic) is treated as an intentional error path.
 *
 * <p>This class is the Java replacement for the
 * {@code <bean id="register-fk-stub" class="...RegisterCertificateResponderStub"/>} declaration inside
 * the {@code <beans profile="dev,it-fk-stub">} block of
 * {@code fk-parent/src/main/resources/module-config.xml} (Step C.4).
 * The CXF endpoint registration is handled by {@link FkParentStubConfig}.
 */
@SchemaValidation
@WebServiceProvider(targetNamespace = "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3")
public final class RegisterCertificateResponderStub implements RegisterCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateResponderStub.class);

    /**
     * Sending to this logical address triggers the error path in integration tests.
     */
    private static final String FAILURE_ADRESS = "fail-adress";

    private MedicalCertificatesStore store = new MedicalCertificatesStore();

    @Override
    public RegisterCertificateResponseType registerCertificate(String logicalAddress, RegisterCertificateType parameters) {
        LOGGER.debug("fk-parent RegisterCertificate responding");
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        ResultType resultType = new ResultType();

        try {
            if (logicalAddress.equals(FAILURE_ADRESS)) {
                throw new IllegalArgumentException("logicalAdress " + FAILURE_ADRESS + " is meant to fail!");
            }
            HashMap<String, String> properties = new HashMap<>();
            Intyg intyg = parameters.getIntyg();
            String pnr = intyg.getPatient().getPersonId().getExtension();
            String certificateteId = intyg.getIntygsId().getExtension();
            properties.put(MAKULERAD, MAKULERAD_NEJ);
            properties.put(PERSONNUMMER, pnr);
            store.addCertificate(certificateteId, properties);
            resultType.setResultCode(ResultCodeType.OK);
        } catch (Exception e) {
            LOGGER.debug("fk-parent RegisterCertificate got exception: ", e);
            resultType.setResultCode(ResultCodeType.ERROR);
        }
        response.setResult(resultType);
        return response;
    }

}
