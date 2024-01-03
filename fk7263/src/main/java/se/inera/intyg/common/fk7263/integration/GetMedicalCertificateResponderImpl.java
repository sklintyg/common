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
package se.inera.intyg.common.fk7263.integration;

import javax.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateRequestType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateResponseType;
import se.inera.intyg.common.fk7263.schemas.clinicalprocess.healthcond.certificate.converter.ModelConverter;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.v1.utils.ResultTypeUtil;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.schemas.contract.Personnummer;

/**
 * @author andreaskaltenbach
 */
public class GetMedicalCertificateResponderImpl implements GetMedicalCertificateResponderInterface {

    public static final String HSVARD = "HSVARD";
    private static final Logger LOGGER = LoggerFactory.getLogger(GetMedicalCertificateResponderImpl.class);
    @Autowired(required = false)
    private ModuleContainerApi moduleContainer;

    @Override
    public GetMedicalCertificateResponseType getMedicalCertificate(String logicalAddress,
        GetMedicalCertificateRequestType request) {

        GetMedicalCertificateResponseType response = new GetMedicalCertificateResponseType();

        String certificateId = request.getCertificateId();
        Personnummer nationalIdentityNumber = request.getNationalIdentityNumber() != null
            ? Personnummer.createPersonnummer(request.getNationalIdentityNumber()).get()
            : null;

        CertificateHolder certificate = null;

        try {
            certificate = moduleContainer.getCertificate(certificateId, nationalIdentityNumber, false);
            if (!Fk7263EntryPoint.MODULE_ID.equalsIgnoreCase(certificate.getType())) {
                throw new InvalidCertificateException(certificateId, nationalIdentityNumber);
            }
            if (nationalIdentityNumber != null && !certificate.getCivicRegistrationNumber().equals(nationalIdentityNumber)) {
                response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "nationalIdentityNumber mismatch"));
                return response;
            }
            if (HSVARD.equals(request.getPart()) && certificate.isDeletedByCareGiver()) {
                response.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR,
                    String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                response.setMeta(ModelConverter.toCertificateMetaType(certificate));
                attachCertificateDocument(certificate, response);
                moduleContainer.logCertificateRetrieved(certificate.getId(), certificate.getType(), certificate.getCareUnitId(),
                    request.getPart());
                if (certificate.isRevoked()) {
                    response.setResult(
                        ResultTypeUtil.errorResult(ErrorIdType.REVOKED,
                            String.format("Certificate '%s' has been revoked", certificateId)));
                } else {
                    response.setResult(ResultTypeUtil.okResult());
                }
            }
        } catch (InvalidCertificateException e) {
            response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
        }
        return response;

    }

    protected void attachCertificateDocument(CertificateHolder certificate, GetMedicalCertificateResponseType response) {
        try {

            JAXBElement<RegisterMedicalCertificateType> el =
                XmlMarshallerHelper.unmarshal(certificate.getOriginalCertificate());

            response.setLakarutlatande(el.getValue().getLakarutlatande());

        } catch (Exception e) {
            LOGGER.error("Error while converting in getMedicalCertificate for id: {} with stacktrace: {}", certificate.getId(),
                e.getStackTrace());
            throw new RuntimeException(e);
        }
    }
}
