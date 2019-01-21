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
package se.inera.intyg.common.ts_tstrk1062.v1.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.transformer.XslTransformerFactory;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.ts_parent.integration.SendTSClientFactory;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intyg.common.ts_tstrk1062.support.TsTstrk1062EntryPoint;
import se.inera.intyg.common.ts_tstrk1062.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.ts_tstrk1062.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.ts_tstrk1062.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.TsTstrk1062UtlatandeV1;
import se.inera.intyg.common.ts_tstrk1062.v1.pdf.PdfGenerator;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXB;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.*;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten, Mina-Intyg & Webcert).
 */
@Component("moduleapi.ts-tstrk1062.v1")
public class TsTstrk1062ModuleApiV1 extends TsParentModuleApi<TsTstrk1062UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(TsTstrk1062ModuleApiV1.class);

    @Autowired(required = false)
    @Qualifier("sendTSClientFactory")
    private SendTSClientFactory sendTSClientFactory;

    @Autowired(required = false)
    @Qualifier("tsTstrk1062XslTransformerFactory")
    private XslTransformerFactory xslTransformerFactory;

    @Autowired(required = false)
    @Qualifier("tsTstrk1062RegisterCertificateVersion")
    private String registerCertificateVersion;

    private SendTSClient sendTsTstrk1062Client;

    public TsTstrk1062ModuleApiV1() {
        super(TsTstrk1062UtlatandeV1.class);
    }

    @PostConstruct
    public void init() {
        if (registerCertificateVersion == null) {
            registerCertificateVersion = TsParentModuleApi.REGISTER_CERTIFICATE_VERSION3;
        }

        if (sendTSClientFactory != null) {
            sendTsTstrk1062Client = sendTSClientFactory.get(registerCertificateVersion);
        } else {
            LOG.debug("SendTSClientFactory is not injected. RegisterCertificate messages cannot be sent to recipient");
        }
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
            throws ModuleException {
        TsTstrk1062UtlatandeV1 utlatande = getInternal(internalModel);
        IntygTexts texts = getTexts(TsTstrk1062EntryPoint.MODULE_ID, utlatande.getTextVersion());

        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        return new PdfGenerator().generatePdf(utlatande.getId(), internalModel, personId, texts, statuses, applicationOrigin,
                utkastStatus);
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        try {
            String transformedPayload = transformPayload(xmlBody);
            SOAPMessage response = sendTsTstrk1062Client.registerCertificate(transformedPayload, logicalAddress);
            SOAPEnvelope contents = response.getSOAPPart().getEnvelope();
            if (contents.getBody().hasFault()) {
                throw new ExternalServiceCallException(contents.getBody().getFault().getTextContent());
            }
        } catch (Exception e) {
            LOG.error("Error in sendCertificateToRecipient with msg: {}", e.getMessage());
            throw new ModuleException("Error in sendCertificateToRecipient.", e);
        }
    }

    @Override
    public TsTstrk1062UtlatandeV1 getUtlatandeFromXml(String xmlBody) throws ModuleException {
        try {
            String xml = xmlBody;
            return transportToInternal(JAXB.unmarshal(new StringReader(xml), RegisterCertificateType.class).getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    public String updateBeforeViewing(String internalModel, Patient patient) {
        return internalModel;
    }

    @Override
    protected Intyg utlatandeToIntyg(TsTstrk1062UtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected RegisterCertificateValidator getRegisterCertificateValidator() {
        return new RegisterCertificateValidator(TsTstrk1062EntryPoint.SCHEMATRON_FILE);
    }

    @Override
    protected RegisterCertificateType internalToTransport(TsTstrk1062UtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected TsTstrk1062UtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    String transformPayload(String xmlBody) throws ModuleException {
        return xmlBody;
    }

    @Override
    public PatientDetailResolveOrder getPatientDetailResolveOrder() {
        List<ResolveOrder> adressStrat = Arrays.asList(PARAMS, PU);
        List<ResolveOrder> avlidenStrat = Arrays.asList(PARAMS_OR_PU);
        List<ResolveOrder> otherStrat = Arrays.asList(PU, PARAMS);

        return new PatientDetailResolveOrder(null, adressStrat, avlidenStrat, otherStrat);
    }
}
