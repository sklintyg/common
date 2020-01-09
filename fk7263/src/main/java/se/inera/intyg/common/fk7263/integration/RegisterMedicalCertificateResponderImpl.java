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
package se.inera.intyg.common.fk7263.integration;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.fk7263.model.converter.TransportToInternal;
import se.inera.intyg.common.fk7263.model.converter.util.ConverterUtil;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.validator.ProgrammaticTransportValidator;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.validate.CertificateValidationException;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.util.logging.LogMarkers;

public class RegisterMedicalCertificateResponderImpl implements RegisterMedicalCertificateResponderInterface {

    public static final String CERTIFICATE_ALREADY_EXISTS = "Certificate already exists";

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMedicalCertificateResponderImpl.class);

    private ObjectFactory objectFactory;

    @Autowired(required = false)
    private ModuleContainerApi moduleContainer;

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        objectFactory = new ObjectFactory();
    }

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress,
        RegisterMedicalCertificateType registerMedicalCertificate) {
        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();

        try {
            validateTransport(registerMedicalCertificate);

            Fk7263Utlatande utlatande = TransportToInternal.convert(registerMedicalCertificate.getLakarutlatande());

            String xml = xmlToString(registerMedicalCertificate);
            CertificateHolder certificateHolder = ConverterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);

            moduleContainer.certificateReceived(certificateHolder);

            response.setResult(ResultOfCallUtil.okResult());

        } catch (CertificateAlreadyExistsException e) {
            response.setResult(ResultOfCallUtil.infoResult(CERTIFICATE_ALREADY_EXISTS));
            String certificateId = registerMedicalCertificate.getLakarutlatande().getLakarutlatandeId();
            String issuedBy = registerMedicalCertificate.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId()
                .getExtension();
            LOGGER.warn(LogMarkers.VALIDATION,
                "Validation warning for intyg " + certificateId + " issued by " + issuedBy + ": Certificate already exists - ignored.");

        } catch (CertificateValidationException | ConverterException e) {
            response.setResult(ResultOfCallUtil.failResult(e.getMessage()));
            LOGGER.error(LogMarkers.VALIDATION, e.getMessage());

        } catch (InvalidCertificateException e) {
            response.setResult(ResultOfCallUtil.applicationErrorResult("Invalid certificate ID"));
            String certificateId = registerMedicalCertificate.getLakarutlatande().getLakarutlatandeId();
            String issuedBy = registerMedicalCertificate.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId()
                .getExtension();
            LOGGER.error(LogMarkers.VALIDATION, "Failed to create Certificate with id " + certificateId + " issued by " + issuedBy
                + ": Certificate ID already exists for another person.");

        } catch (Exception e) {
            LOGGER.error("Error in Webservice: ", e);
            throw new RuntimeException(e);
        }

        return response;
    }

    private void validateTransport(RegisterMedicalCertificateType registerMedicalCertificate) throws CertificateValidationException {
        List<String> validationErrors = new ProgrammaticTransportValidator(registerMedicalCertificate.getLakarutlatande()).validate();
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }

    private String xmlToString(RegisterMedicalCertificateType registerMedicalCertificate) {
        JAXBElement<RegisterMedicalCertificateType> requestElement = objectFactory
            .createRegisterMedicalCertificate(registerMedicalCertificate);
        return XmlMarshallerHelper.marshal(requestElement);
    }
}
