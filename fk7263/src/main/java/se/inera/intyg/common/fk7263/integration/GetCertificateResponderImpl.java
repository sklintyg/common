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
package se.inera.intyg.common.fk7263.integration;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.dom.DOMResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;
import org.w3c.dom.Document;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.rivtabp20.v1.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.CertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter.ModelConverter;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.util.logging.LogMarkers;
import se.inera.intyg.schemas.contract.Personnummer;

/**
 * @author andreaskaltenbach
 */
public class GetCertificateResponderImpl implements
    GetCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCertificateResponderImpl.class);

    @Autowired(required = false)
    private ModuleContainerApi moduleContainer;

    @Override
    public GetCertificateResponseType getCertificate(AttributedURIType logicalAddress, GetCertificateRequestType request) {
        GetCertificateResponseType response = new GetCertificateResponseType();

        String certificateId = request.getCertificateId();

        if (certificateId == null || certificateId.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing certificateId '.");
            response.setResult(ResultOfCallUtil.failResult("Validation error: missing certificateId"));
            return response;
        }

        final String nationalIdentityNumber = request.getNationalIdentityNumber();
        if (nationalIdentityNumber == null || nationalIdentityNumber.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing nationalIdentityNumber '.");
            response.setResult(ResultOfCallUtil.failResult("Validation error: missing nationalIdentityNumber"));
            return response;
        }
        Personnummer personnummer = Personnummer.createPersonnummer(nationalIdentityNumber).get();

        CertificateHolder certificate = null;

        try {
            certificate = moduleContainer.getCertificate(certificateId, personnummer, true);
            if (certificate.isRevoked()) {
                response.setResult(ResultOfCallUtil.infoResult(String.format("Certificate '%s' has been revoked", certificateId)));
            } else {
                response.setMeta(ModelConverter.toCertificateMetaType(certificate));
                attachCertificateDocument(certificate, response);
                response.setResult(ResultOfCallUtil.okResult());
            }
        } catch (InvalidCertificateException e) {
            response.setResult(ResultOfCallUtil.failResult(e.getMessage()));
        }

        return response;
    }

    protected void attachCertificateDocument(CertificateHolder certificate, GetCertificateResponseType response) {
        try {
            JAXBElement<RegisterMedicalCertificateType> el = XmlMarshallerHelper.unmarshal(
                certificate.getOriginalCertificate());
            DOMResult domResult = new DOMResult();
            XmlMarshallerHelper.marshaller().marshal(el, domResult);
            CertificateType certificateType = new CertificateType();
            certificateType.getAny().add(((Document) domResult.getNode()).getDocumentElement());
            response.setCertificate(certificateType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
