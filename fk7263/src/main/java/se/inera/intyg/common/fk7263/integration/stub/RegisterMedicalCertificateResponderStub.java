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
package se.inera.intyg.common.fk7263.integration.stub;

import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.MAKULERAD;
import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.MAKULERAD_NEJ;
import static se.inera.intyg.common.support.stub.MedicalCertificatesStore.PERSONNUMMER;

import java.util.HashMap;
import java.util.Map;
import jakarta.xml.ws.WebServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.fk7263.model.converter.TransportToInternal;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.stub.MedicalCertificatesStore;

/**
 * @author par.wenaker
 */
// CHECKSTYLE:OFF LineLength
@Transactional
@WebServiceProvider(targetNamespace = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificate:3:rivtabp20",
    serviceName = "RegisterMedicalCertificateResponderService",
    wsdlLocation = "classpath:interactions/RegisterMedicalCertificateInteraction/RegisterMedicalCertificateInteraction_3.1_rivtabp20.wsdl")
public class RegisterMedicalCertificateResponderStub implements RegisterMedicalCertificateResponderInterface {
// CHECKSTYLE:OFF LineLength

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMedicalCertificateResponderStub.class);

    @Autowired(required = false)
    private MedicalCertificatesStore medicalCertificatesStore;

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress,
        RegisterMedicalCertificateType request) {

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();

        try {
            Fk7263Utlatande utlatande = TransportToInternal.convert(request.getLakarutlatande());
            String id = utlatande.getId();

            if ("error".equals(id)) {
                throw new RuntimeException("A runtime exception");
            }
            Map<String, String> props = new HashMap<>();
            props.put(PERSONNUMMER, utlatande.getGrundData().getPatient().getPersonId().getPersonnummer());
            props.put(MAKULERAD, MAKULERAD_NEJ);

            LOGGER.info("STUB Received request");
            medicalCertificatesStore.addCertificate(id, props);
        } catch (ConverterException e) {
            response.setResult(ResultOfCallUtil.failResult("Unable to convert certificate to internal format"));
            return response;
        }
        response.setResult(ResultOfCallUtil.okResult());
        return response;
    }

}
