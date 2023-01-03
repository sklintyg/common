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
package se.inera.intyg.common.fkparent.integration.stub;

import org.apache.cxf.annotations.SchemaValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.common.support.stub.MedicalCertificatesStore;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

import javax.xml.ws.WebServiceProvider;
import java.util.HashMap;

import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.MAKULERAD;
import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.MAKULERAD_NEJ;
import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.PERSONNUMMER;

@SchemaValidation
@WebServiceProvider(targetNamespace = "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3")
public final class RegisterCertificateResponderStub implements RegisterCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateResponderStub.class);

    //Configuring a recipient with this logicalAdress will return error
    private static final String FAILURE_ADRESS = "fail-adress";

    @Autowired(required = false)
    private MedicalCertificatesStore store;

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
