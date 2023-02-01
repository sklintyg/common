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
package se.inera.intyg.common.ts_bas.v6.rest;

import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PARAMS;
import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PU;
import static se.inera.intyg.common.support.modules.transformer.XslTransformerUtil.isRegisterCertificateV3;
import static se.inera.intyg.common.support.modules.transformer.XslTransformerUtil.isRegisterTsBas;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.stereotype.Component;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter.ModelConverter;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.messages.DefaultCertificateMessagesProvider;
import se.inera.intyg.common.services.messages.MessagesParser;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.util.TestabilityToolkit;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.FillType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.common.ts_bas.v6.model.converter.InternalToCertificate;
import se.inera.intyg.common.ts_bas.v6.model.converter.InternalToTransport;
import se.inera.intyg.common.ts_bas.v6.model.converter.TransportToInternal;
import se.inera.intyg.common.ts_bas.v6.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;
import se.inera.intyg.common.ts_bas.v6.model.transformer.TsBasTransformerType;
import se.inera.intyg.common.ts_bas.v6.pdf.PdfGenerator;
import se.inera.intyg.common.ts_bas.v6.testability.TsBasV6TestabilityUtlatandeTestDataProvider;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.ts_parent.integration.SendTSClientFactory;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten, Mina-Intyg & Webcert).
 */
@Component("moduleapi.ts-bas.v6")
public class TsBasModuleApiV6 extends TsParentModuleApi<TsBasUtlatandeV6> {

    private static final Logger LOG = LoggerFactory.getLogger(TsBasModuleApiV6.class);

    @Autowired(required = false)
    @Qualifier("sendTSClientFactory")
    private SendTSClientFactory sendTSClientFactory;

    @Autowired(required = false)
    @Qualifier("tsBasRegisterCertificateVersion")
    private String registerCertificateVersion;
    @Autowired
    private InternalToCertificate internalToCertificate;

    @Autowired(required = false)
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;
    private SendTSClient sendTsBasClient;
    private Map<String, String> validationMessages;

    public TsBasModuleApiV6() {
        super(TsBasUtlatandeV6.class);
        initMessages();
    }

    private void initMessages() {
        try {
            final var inputStream1 = new ClassPathResource("/META-INF/resources/webjars/common/webcert/messages.js").getInputStream();
            final var inputStream2
                = new ClassPathResource("/META-INF/resources/webjars/ts-bas/webcert/views/messages.js").getInputStream();
            validationMessages = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        } catch (IOException exception) {
            LOG.error("Error during initialization. Could not read messages files");
            throw new RuntimeException("Error during initialization. Could not read messages files", exception);
        }
    }

    @PostConstruct
    public void init() {
        if (registerCertificateVersion == null) {
            registerCertificateVersion = TsParentModuleApi.REGISTER_CERTIFICATE_VERSION3;
        }

        if (sendTSClientFactory != null) {
            sendTsBasClient = sendTSClientFactory.get(registerCertificateVersion);
        } else {
            LOG.debug("SendTSClientFactory is not injected. RegisterCertificate messages cannot be sent to recipient");
        }
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        TsBasUtlatandeV6 utlatande = getInternal(internalModel);
        IntygTexts texts = getTexts(TsBasEntryPoint.MODULE_ID, utlatande.getTextVersion());

        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        return new PdfGenerator().generatePdf(utlatande.getId(), internalModel, personId, texts, statuses, applicationOrigin,
            utkastStatus);
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        try {
            String transformedPayload = transformPayload(xmlBody);
            SOAPMessage response = sendTsBasClient.registerCertificate(transformedPayload, logicalAddress);
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
    public TsBasUtlatandeV6 getUtlatandeFromXml(String xmlBody) throws ModuleException {
        try {
            String xml = xmlBody;
            if (isRegisterTsBas(xml)) {
                xml = TsBasTransformerType.TRANSPORT_TO_V3.getTransformer().transform(xml);
            }

            JAXBElement<RegisterCertificateType> el = XmlMarshallerHelper.unmarshal(xml);
            return transportToInternal(el.getValue().getIntyg());
        } catch (ConverterException | MarshallingFailureException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    public String updateBeforeViewing(String internalModel, Patient patient) throws ModuleException {
        try {
            Utlatande utlatande = this.getInternal(internalModel);

            String fullName = utlatande.getGrundData().getPatient().getFullstandigtNamn();
            String firstName = utlatande.getGrundData().getPatient().getFornamn();
            String middleName = utlatande.getGrundData().getPatient().getMellannamn();
            String lastName = utlatande.getGrundData().getPatient().getEfternamn();
            String address = utlatande.getGrundData().getPatient().getPostadress();
            String county = utlatande.getGrundData().getPatient().getPostort();
            String zipCode = utlatande.getGrundData().getPatient().getPostnummer();

            WebcertModelFactoryUtil.populateWithPatientInfo(utlatande.getGrundData(), patient);

            utlatande.getGrundData().getPatient().setFullstandigtNamn(fullName);
            utlatande.getGrundData().getPatient().setFornamn(firstName);
            utlatande.getGrundData().getPatient().setMellannamn(middleName);
            utlatande.getGrundData().getPatient().setEfternamn(lastName);
            utlatande.getGrundData().getPatient().setPostadress(address);
            utlatande.getGrundData().getPatient().setPostort(county);
            utlatande.getGrundData().getPatient().setPostnummer(zipCode);

            return this.toInternalModelResponse(utlatande);
        } catch (ConverterException | ModuleException var4) {
            throw new ModuleException("Error while updating internal model", var4);
        }
    }

    @Override
    protected Intyg utlatandeToIntyg(TsBasUtlatandeV6 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected RegisterCertificateValidator getRegisterCertificateValidator() {
        return new RegisterCertificateValidator(TsBasEntryPoint.SCHEMATRON_FILE_V6);
    }

    @Override
    protected RegisterCertificateType internalToTransport(TsBasUtlatandeV6 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected TsBasUtlatandeV6 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    String transformPayload(String xmlBody) throws ModuleException {
        // Ta reda på om innehållet är på formatet
        // 'RegisterTsBas' eller 'RegisterCertificate V3'
        if (isRegisterTsBas(xmlBody)) {
            if (shouldTransformToV1()) {
                return TsBasTransformerType.TRANSPORT_TO_V1.getTransformer().transform(xmlBody);
            } else if (shouldTransformToV3()) {
                return TsBasTransformerType.TRANSPORT_TO_V3.getTransformer().transform(xmlBody);
            } else {
                String msg = String.format("Error in sendCertificateToRecipient. Cannot decide type of transformer."
                    + "Property registercertificate.version = '%s'", registerCertificateVersion);
                throw new ModuleException(msg);
            }
        } else if (isRegisterCertificateV3(xmlBody)) {
            if (shouldTransformToV1()) {
                // Here we need to transform from V3 to V1
                return TsBasTransformerType.V3_TO_V1.getTransformer().transform(xmlBody);
            }
        }

        // Input is already at V3 format and doesn't, we don't need to transform
        return xmlBody;
    }

    private boolean shouldTransformToV1() {
        return registerCertificateVersion != null && registerCertificateVersion.equals(REGISTER_CERTIFICATE_VERSION1);
    }

    private boolean shouldTransformToV3() {
        return registerCertificateVersion != null && registerCertificateVersion.equals(REGISTER_CERTIFICATE_VERSION3);
    }

    @Override
    public PatientDetailResolveOrder getPatientDetailResolveOrder() {
        List<ResolveOrder> adressStrat = Arrays.asList(PARAMS, PU);
        List<ResolveOrder> otherStrat = Arrays.asList(PU, PARAMS);

        return new PatientDetailResolveOrder(null, adressStrat, otherStrat);
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        AttributedURIType uri = new AttributedURIType();
        uri.setValue(logicalAddress);

        RevokeMedicalCertificateRequestType request = JAXB.unmarshal(new StreamSource(new StringReader(xmlBody)),
            RevokeMedicalCertificateRequestType.class);
        RevokeMedicalCertificateResponseType response = revokeCertificateClient.revokeMedicalCertificate(uri, request);
        if (response.getResult().getResultCode().equals(ResultCodeEnum.ERROR)) {
            String message = "Revoke sent to " + logicalAddress + " failed with error: " + response.getResult().getErrorText();
            LOG.error(message);
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException {
        RevokeMedicalCertificateRequestType request = new RevokeMedicalCertificateRequestType();
        request.setRevoke(ModelConverter.buildRevokeTypeFromUtlatande(utlatande, meddelande));

        JAXBElement<RevokeMedicalCertificateRequestType> el = new ObjectFactory().createRevokeMedicalCertificateRequest(request);
        return XmlMarshallerHelper.marshal(el);
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var certificateTextProvider = getTextProvider(internalCertificate.getTyp(), internalCertificate.getTextVersion());
        return internalToCertificate.convert(internalCertificate, certificateTextProvider);
    }

    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        return DefaultCertificateMessagesProvider.create(validationMessages);
    }

    @Override
    public String getJsonFromUtlatande(Utlatande utlatande) throws ModuleException {
        if (utlatande instanceof TsBasUtlatandeV6) {
            return toInternalModelResponse(utlatande);
        }
        final var message = utlatande == null ? "null" : utlatande.getClass().toString();
        throw new IllegalArgumentException(
            "Utlatande was not instance of class TsBasUtlatandeV6, utlatande was instance of class: " + message);
    }

    @Override
    public String getUpdatedJsonWithTestData(String model, FillType fillType, TypeAheadProvider typeAheadProvider) throws ModuleException {
        try {
            final var utlatande = (TsBasUtlatandeV6) getUtlatandeFromJson(model);
            final var updatedUtlatande = TestabilityToolkit.getUtlatandeWithTestData(utlatande, fillType,
                new TsBasV6TestabilityUtlatandeTestDataProvider());
            return getJsonFromUtlatande(updatedUtlatande);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
