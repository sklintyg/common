/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v2.integration;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.validate.CertificateValidationException;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.TransportToInternalConverter;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.util.ConverterUtil;
import se.inera.intyg.common.ts_diabetes.v2.validator.transport.TransportValidatorInstance;
import se.inera.intyg.common.ts_parent.integration.ResultTypeUtil;
import se.inera.intyg.common.util.logging.LogMarkers;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.ObjectFactory;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;

public class RegisterTSDiabetesResponderImpl implements RegisterTSDiabetesResponderInterface {

    public static final String CERTIFICATE_ALREADY_EXISTS = "Certificate already exists";

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterTSDiabetesResponderImpl.class);

    @Autowired(required = false)
    private ModuleContainerApi moduleContainer;

    private ObjectFactory objectFactory;
    private TransportValidatorInstance validator = new TransportValidatorInstance();

    @PostConstruct
    public void initializeJaxbContext() {
        objectFactory = new ObjectFactory();
    }

    @Override
    public RegisterTSDiabetesResponseType registerTSDiabetes(String logicalAddress, RegisterTSDiabetesType parameters) {
        RegisterTSDiabetesResponseType response = new RegisterTSDiabetesResponseType();

        try {
            validate(parameters);

            String xml = marshal(parameters);

            TsDiabetesUtlatandeV2 utlatande = TransportToInternalConverter.convert(parameters.getIntyg());
            CertificateHolder certificateHolder = ConverterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);

            moduleContainer.certificateReceived(certificateHolder);

            response.setResultat(ResultTypeUtil.okResult());

        } catch (CertificateAlreadyExistsException ce) {
            response.setResultat(ResultTypeUtil.infoResult(CERTIFICATE_ALREADY_EXISTS));
            String certificateId = parameters.getIntyg().getIntygsId();
            String issuedBy = parameters.getIntyg().getGrundData().getSkapadAv().getVardenhet().getEnhetsId().getExtension();
            LOGGER.warn(LogMarkers.VALIDATION, "Validation warning for intyg " + certificateId + " issued by " + issuedBy
                + ": Certificate already exists - ignored.");
        } catch (InvalidCertificateException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "Invalid certificate ID"));
            String certificateId = parameters.getIntyg().getIntygsId();
            String issuedBy = parameters.getIntyg().getGrundData().getSkapadAv().getVardenhet().getEnhetsId().getExtension();
            LOGGER.error(LogMarkers.VALIDATION, "Failed to create Certificate with id " + certificateId + " issued by " + issuedBy
                + ": Certificate ID already exists for another person.");

        } catch (CertificateValidationException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
            LOGGER.error(LogMarkers.VALIDATION, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error in Webservice: ", e);
            throw new RuntimeException(e);
        }

        return response;
    }

    private void validate(RegisterTSDiabetesType parameters) throws CertificateValidationException {
        List<String> validationErrors = validator.validate(parameters.getIntyg());
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }

    private String marshal(RegisterTSDiabetesType parameters) {
        return XmlMarshallerHelper.marshal(objectFactory.createRegisterTSDiabetes(parameters));
    }
}
