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
package se.inera.intyg.common.ts_diabetes.v2.rest;

import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PARAMS;
import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PU;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ws.rs.NotSupportedException;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
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
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.modules.support.facade.FillType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_diabetes.v2.integration.RegisterTSDiabetesResponderImpl;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.InternalToCertificate;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.InternalToTransportConverter;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.TransportToInternalConverter;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.pdf.PdfGenerator;
import se.inera.intyg.common.ts_diabetes.v2.pdf.PdfGeneratorException;
import se.inera.intyg.common.ts_diabetes.v2.testability.TSTRK1031TestabilityTestDataDecorator;
import se.inera.intyg.common.ts_diabetes.v2.util.TSDiabetesCertificateMetaTypeConverter;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten and Mina-Intyg).
 *
 * @author Gustav Norbäcker, R2M
 */
@Component(value = "moduleapi.ts-diabetes.v2")
public class TsDiabetesModuleApiV2 extends TsParentModuleApi<TsDiabetesUtlatandeV2> {


    private static final Logger LOG = LoggerFactory.getLogger(TsDiabetesModuleApiV2.class);
    private Map<String, String> validationMessages;
    @Autowired
    private InternalToCertificate internalToCertificate;
    @Autowired(required = false)
    @Qualifier("sendTsDiabetesClient")
    private SendTSClient sendTsDiabetesClient;

    @Autowired(required = false)
    @Qualifier("diabetesGetClient")
    private GetTSDiabetesResponderInterface diabetesGetClient;

    @Autowired(required = false)
    @Qualifier("diabetesRegisterClient")
    private RegisterTSDiabetesResponderInterface diabetesRegisterClient;

    @Autowired(required = false)
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;

    @Autowired(required = false)
    @Qualifier("tsDiabetesXslTransformer")
    private XslTransformer xslTransformer;

    private JAXBContext jaxbContext;

    @Autowired
    private PdfGenerator<TsDiabetesUtlatandeV2> pdfGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    public TsDiabetesModuleApiV2() {
        super(TsDiabetesUtlatandeV2.class);
        initMessages();
    }

    private void initMessages() {
        try {
            final var inputStream1 = new ClassPathResource("/META-INF/resources/webjars/common/webcert/messages.js").getInputStream();
            final var inputStream2
                = new ClassPathResource("/META-INF/resources/webjars/ts-diabetes/webcert/views/messages.js").getInputStream();
            validationMessages = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        } catch (IOException exception) {
            LOG.error("Error during initialization. Could not read messages files");
            throw new RuntimeException("Error during initialization. Could not read messages files", exception);
        }
    }

    @PostConstruct
    void init() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterTSDiabetesType.class);
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        RegisterTSDiabetesType request = InternalToTransportConverter.convert(getInternal(internalModel));

        RegisterTSDiabetesResponseType response = diabetesRegisterClient.registerTSDiabetes(logicalAddress, request);

        // check whether call was successful or not
        if (response.getResultat().getResultCode() == ResultCodeType.INFO) {
            throw new ExternalServiceCallException(response.getResultat().getResultText(),
                RegisterTSDiabetesResponderImpl.CERTIFICATE_ALREADY_EXISTS.equals(response.getResultat().getResultText())
                    ? ErrorIdEnum.VALIDATION_ERROR
                    : ErrorIdEnum.APPLICATION_ERROR);
        } else if (response.getResultat().getResultCode() == ResultCodeType.ERROR) {
            throw new ExternalServiceCallException(response.getResultat().getErrorId() + " : " + response.getResultat().getResultText());
        }
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        String transformedPayload = xslTransformer.transform(xmlBody);

        try {
            SOAPMessage response = sendTsDiabetesClient.registerCertificate(transformedPayload, logicalAddress);
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
    public CertificateResponse getCertificate(String certificateId, String logicalAddress, String recipientId) throws ModuleException {
        GetTSDiabetesType type = new GetTSDiabetesType();
        type.setIntygsId(certificateId);

        GetTSDiabetesResponseType diabetesResponseType = diabetesGetClient.getTSDiabetes(logicalAddress, type);

        switch (diabetesResponseType.getResultat().getResultCode()) {
            case INFO:
            case OK:
                return convert(diabetesResponseType, false);
            case ERROR:
                switch (diabetesResponseType.getResultat().getErrorId()) {
                    case REVOKED:
                        return convert(diabetesResponseType, true);
                    case VALIDATION_ERROR:
                        throw new ModuleException("GetTSDiabetes WS call: VALIDATION_ERROR :"
                            + diabetesResponseType.getResultat().getResultText());
                    default:
                        throw new ModuleException(
                            "GetTSDiabetes WS call: ERROR :" + diabetesResponseType.getResultat().getResultText());
                }
        }
        throw new ModuleException("GetTSDiabetes WS call: ERROR :" + diabetesResponseType.getResultat().getResultText());
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        AttributedURIType uri = new AttributedURIType();
        uri.setValue(logicalAddress);

        RevokeMedicalCertificateRequestType request = JAXB.unmarshal(new StreamSource(new StringReader(xmlBody)),
            RevokeMedicalCertificateRequestType.class);
        RevokeMedicalCertificateResponseType response = revokeCertificateClient.revokeMedicalCertificate(uri, request);
        if (!response.getResult().getResultCode().equals(ResultCodeEnum.OK)) {
            String message = "Could not send revoke to " + logicalAddress;
            LOG.error(message);
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException {
        RevokeMedicalCertificateRequestType request = new RevokeMedicalCertificateRequestType();
        request.setRevoke(ModelConverter.buildRevokeTypeFromUtlatande(utlatande, meddelande));

        JAXBElement<RevokeMedicalCertificateRequestType> el =
            new ObjectFactory().createRevokeMedicalCertificateRequest(request);
        return XmlMarshallerHelper.marshal(el);
    }

    // FIXME: There exists invalid RegisterTSDiabetes XML files in the database (invalid root element name and namespace).
    // use old fashion marshalling to handle those until the database has been corrected.
    @Override
    public TsDiabetesUtlatandeV2 getUtlatandeFromXml(String xml) throws ModuleException {
        try {
            RegisterTSDiabetesType jaxbObject = JAXB.unmarshal(new StringReader(xml),
                RegisterTSDiabetesType.class);
            return TransportToInternalConverter.convert(jaxbObject.getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    private CertificateResponse convert(GetTSDiabetesResponseType diabetesResponseType, boolean revoked) throws ModuleException {
        try {
            TsDiabetesUtlatandeV2 utlatande = TransportToInternalConverter.convert(diabetesResponseType.getIntyg());
            String internalModel = toInternalModelResponse(utlatande);
            CertificateMetaData metaData = TSDiabetesCertificateMetaTypeConverter.toCertificateMetaData(diabetesResponseType.getMeta(),
                diabetesResponseType.getIntyg());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
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
    protected Intyg utlatandeToIntyg(TsDiabetesUtlatandeV2 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected RegisterCertificateValidator getRegisterCertificateValidator() {
        return null;
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        return ValidateXmlResponse.createValidResponse();
    }

    @Override
    protected RegisterCertificateType internalToTransport(TsDiabetesUtlatandeV2 utlatande) throws ConverterException {
        throw new NotSupportedException();
    }

    @Override
    protected TsDiabetesUtlatandeV2 transportToInternal(Intyg intyg) throws ConverterException {
        throw new NotSupportedException();
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        try {
            return new PdfResponse(pdfGenerator.generatePDF(getInternal(internalModel), statuses, applicationOrigin, utkastStatus),
                pdfGenerator.generatePdfFilename(getInternal(internalModel)));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for certificate!", e);
        }
    }

    @Override
    public PatientDetailResolveOrder getPatientDetailResolveOrder() {
        List<ResolveOrder> adressStrat = Arrays.asList(PARAMS, PU);
        List<ResolveOrder> otherStrat = Arrays.asList(PU, PARAMS);

        return new PatientDetailResolveOrder(null, adressStrat, otherStrat);
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson,
        TypeAheadProvider typeAheadProvider) throws ModuleException, IOException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var certificateTextProvider = getTextProvider(internalCertificate.getTyp(), internalCertificate.getTextVersion());
        return internalToCertificate.convert(internalCertificate, certificateTextProvider);
    }

    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        return DefaultCertificateMessagesProvider.create(validationMessages);
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJsonFromUtlatande(Utlatande utlatande) throws ModuleException {
        if (utlatande instanceof TsDiabetesUtlatandeV2) {
            return toInternalModelResponse(utlatande);
        }
        final var message = utlatande == null ? "null" : utlatande.getClass().toString();
        throw new IllegalArgumentException(
            "Utlatande was not instance of class TsDiabetesUtlatandeV2, utlatande was instance of class: " + message);
    }

    @Override
    public String getUpdatedJsonWithTestData(String model, FillType fillType, TypeAheadProvider typeAheadProvider) throws ModuleException {
        try {
            final var utlatande = (TsDiabetesUtlatandeV2) getUtlatandeFromJson(model);
            TestabilityToolkit.decorateCertificateWithTestData(utlatande, fillType, new TSTRK1031TestabilityTestDataDecorator());
            return getJsonFromUtlatande(utlatande);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
