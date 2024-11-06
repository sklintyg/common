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
package se.inera.intyg.common.ts_bas.v7.rest;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.stereotype.Component;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.messages.DefaultCertificateMessagesProvider;
import se.inera.intyg.common.services.messages.MessagesParser;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.util.TestabilityToolkit;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.FillType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.common.ts_bas.v7.model.converter.CertificateToInternal;
import se.inera.intyg.common.ts_bas.v7.model.converter.InternalToCertificate;
import se.inera.intyg.common.ts_bas.v7.model.converter.InternalToTransport;
import se.inera.intyg.common.ts_bas.v7.model.converter.TransportToInternal;
import se.inera.intyg.common.ts_bas.v7.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.common.ts_bas.v7.pdf.PdfGenerator;
import se.inera.intyg.common.ts_bas.v7.testability.TsBasV7TestabilityCertificateTestdataProvider;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.ts_parent.integration.SendTSClientFactory;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten, Mina-Intyg & Webcert).
 */
@Component("moduleapi.ts-bas.v7")
public class TsBasModuleApiV7 extends TsParentModuleApi<TsBasUtlatandeV7> {

    private static final Logger LOG = LoggerFactory.getLogger(TsBasModuleApiV7.class);

    @Autowired(required = false)
    @Qualifier("sendTSClientFactory")
    private SendTSClientFactory sendTSClientFactory;

    @Autowired(required = false)
    @Qualifier("tsBasRegisterCertificateVersion")
    private String registerCertificateVersion;

    @Autowired(required = false)
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;

    private SendTSClient sendTsBasClient;
    private Map<String, String> validationMessages;
    @Autowired
    private InternalToCertificate internalToCertificate;

    @Autowired
    private CertificateToInternal certificateToInternal;
    @Autowired(required = false)
    private SummaryConverter summaryConverter;

    @Value("${pdf.footer.app.name.text:1177 intyg}")
    private String pdfFooterAppName;

    public TsBasModuleApiV7() {
        super(TsBasUtlatandeV7.class);
        initMessages();
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

    private void initMessages() {
        try {
            final var inputStream1 = new ClassPathResource("/common/messages.js").getInputStream();
            final var inputStream2 = new ClassPathResource("ts-bas-messages.js").getInputStream();
            validationMessages = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        } catch (IOException exception) {
            LOG.error("Error during initialization. Could not read messages files");
            throw new RuntimeException("Error during initialization. Could not read messages files", exception);
        }
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        TsBasUtlatandeV7 utlatande = getInternal(internalModel);
        IntygTexts texts = getTexts(TsBasEntryPoint.MODULE_ID, utlatande.getTextVersion());

        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        return new PdfGenerator().generatePdf(utlatande.getId(), internalModel, personId, texts, statuses, applicationOrigin,
            utkastStatus, pdfFooterAppName);
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        try {
            SOAPMessage response = sendTsBasClient.registerCertificate(xmlBody, logicalAddress);
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
    public TsBasUtlatandeV7 getUtlatandeFromXml(String xmlBody) throws ModuleException {
        try {
            JAXBElement<RegisterCertificateType> el = XmlMarshallerHelper.unmarshal(xmlBody);
            return transportToInternal(el.getValue().getIntyg());
        } catch (ConverterException | MarshallingFailureException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    protected Intyg utlatandeToIntyg(TsBasUtlatandeV7 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected RegisterCertificateValidator getRegisterCertificateValidator() {
        return new RegisterCertificateValidator(TsBasEntryPoint.SCHEMATRON_FILE_V7);
    }

    @Override
    protected RegisterCertificateType internalToTransport(TsBasUtlatandeV7 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected TsBasUtlatandeV7 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    public String updateAfterSigning(String jsonModel, String signatureXml) throws ModuleException {
        if (signatureXml == null) {
            return jsonModel;
        }
        String base64EncodedSignatureXml = Base64.getEncoder().encodeToString(signatureXml.getBytes(Charset.forName("UTF-8")));
        return updateInternalAfterSigning(jsonModel, base64EncodedSignatureXml);
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var certificateTextProvider = getTextProvider(internalCertificate.getTyp(), internalCertificate.getTextVersion());
        final var certificate = internalToCertificate.convert(internalCertificate, certificateTextProvider);
        final var certificateSummary = summaryConverter.convert(this, getIntygFromUtlatande(internalCertificate));
        certificate.getMetadata().setSummary(certificateSummary);
        return certificate;
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var updateInternalCertificate = certificateToInternal.convert(certificate, internalCertificate);
        return toInternalModelResponse(updateInternalCertificate);
    }

    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        return DefaultCertificateMessagesProvider.create(validationMessages);
    }

    @Override
    public String getJsonFromUtlatande(Utlatande utlatande) throws ModuleException {
        if (utlatande instanceof TsBasUtlatandeV7) {
            return toInternalModelResponse(utlatande);
        }
        final var message = utlatande == null ? "null" : utlatande.getClass().toString();
        throw new IllegalArgumentException(
            "Utlatande was not instance of class TsBasUtlatandeV7, utlatande was instance of class: " + message);
    }

    @Override
    public String getUpdatedJsonWithTestData(String model, FillType fillType, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var certificate = getCertificateFromJson(model, typeAheadProvider);
        TestabilityToolkit.fillCertificateWithTestData(certificate, fillType, new TsBasV7TestabilityCertificateTestdataProvider());
        return getJsonFromCertificate(certificate, model);
    }

    private String updateInternalAfterSigning(String internalModel, String base64EncodedSignatureXml)
        throws ModuleException {
        try {
            TsBasUtlatandeV7 utlatande = decorateWithSignature(getInternal(internalModel), base64EncodedSignatureXml);
            return toInternalModelResponse(utlatande);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model with signature", e);
        }
    }

    private TsBasUtlatandeV7 decorateWithSignature(TsBasUtlatandeV7 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

}
