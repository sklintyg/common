/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.rest;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.ts_bas.model.converter.InternalToTransport;
import se.inera.intyg.common.ts_bas.model.converter.TransportToInternal;
import se.inera.intyg.common.ts_bas.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.model.internal.TsBasUtlatande;
import se.inera.intyg.common.ts_bas.pdf.PdfGenerator;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten, Mina-Intyg & Webcert).
 *
 */
public class TsBasModuleApi extends TsParentModuleApi<TsBasUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(TsBasModuleApi.class);

    @Autowired(required = false)
    @Qualifier("tsBasSendCertificateClient")
    private SendTSClient sendTsBasClient;

    @Autowired(required = false)
    @Qualifier("tsBasXslTransformer")
    private XslTransformer xslTransformer;

    @Autowired(required = false)
    @Qualifier("tsBasTransformBeforeSending")
    private Boolean transformXmlBeforeSendingToRecipient;

    public TsBasModuleApi() {
        super(TsBasUtlatande.class);
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
            throws ModuleException {
        TsBasUtlatande utlatande = getInternal(internalModel);
        IntygTexts texts = getTexts(TsBasEntryPoint.MODULE_ID, utlatande.getTextVersion());

        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        return new PdfGenerator().generatePdf(utlatande.getId(), internalModel, personId, texts, statuses, applicationOrigin,
                utkastStatus);
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        String payload = transformXmlBeforeSendingToRecipient ? xslTransformer.transform(xmlBody) : xmlBody;

        try {
            SOAPMessage response = sendTsBasClient.registerCertificate(payload, logicalAddress);
            SOAPEnvelope contents = response.getSOAPPart().getEnvelope();
            if (contents.getBody().hasFault()) {
                throw new ExternalServiceCallException(contents.getBody().getFault().getTextContent());
            }
        } catch (Exception e) {
            LOG.error("Error in sendCertificateToRecipient with msg: " + e.getMessage(), e);
            throw new ModuleException("Error in sendCertificateToRecipient.", e);
        }
    }

    @Override
    public TsBasUtlatande getUtlatandeFromXml(String xml) throws ModuleException {
        try {
            return transportToInternal(JAXB.unmarshal(new StringReader(xml), RegisterCertificateType.class).getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    protected Intyg utlatandeToIntyg(TsBasUtlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected String getSchematronFileName() {
        return TsBasEntryPoint.SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(TsBasUtlatande utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected TsBasUtlatande transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }
}
