/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v4.rest;

import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import jakarta.xml.bind.JAXB;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
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
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.FillType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.CertificateToInternal;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.InternalToCertificate;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.InternalToTransport;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.TransportToInternal;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.pdf.PdfGenerator;
import se.inera.intyg.common.ts_diabetes.v4.testability.TsDiabetesV4TestabilityCertificateTestdataProvider;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

@Component(value = "moduleapi.ts-diabetes.v4")
public class TsDiabetesModuleApiV4 extends TsParentModuleApi<TsDiabetesUtlatandeV4> {

    public static final String SCHEMATRON_FILE = "tstrk1031.v4.sch";

    private static final Logger LOG = LoggerFactory.getLogger(TsDiabetesModuleApiV4.class);
    private Map<String, String> validationMessages;

    @Autowired
    private InternalToCertificate internalToCertificate;

    @Autowired
    private CertificateToInternal certificateToInternal;

    @Autowired(required = false)
    private SummaryConverter summaryConverter;

    @Autowired(required = false)
    private UnitMapperUtil unitMapperUtil;

    @Value("${pdf.footer.app.name.text:1177 intyg}")
    private String pdfFooterAppName;

    public TsDiabetesModuleApiV4() {
        super(TsDiabetesUtlatandeV4.class);
        init();
    }

    private void init() {
        try {
            final var inputStream1 = new ClassPathResource("/common/messages.js").getInputStream();
            final var inputStream2 = new ClassPathResource("ts-diabetes-messages.js").getInputStream();
            validationMessages = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        } catch (IOException exception) {
            LOG.error("Error during initialization. Could not read messages files");
            throw new RuntimeException("Error during initialization. Could not read messages files", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        TsDiabetesUtlatandeV4 tsDiabetesUtlatandeV4 = getInternal(internalModel);
        IntygTexts texts = getTexts(TsDiabetesEntryPoint.MODULE_ID, tsDiabetesUtlatandeV4.getTextVersion());

        Personnummer personId = tsDiabetesUtlatandeV4.getGrundData().getPatient().getPersonId();
        return new PdfGenerator().generatePdf(tsDiabetesUtlatandeV4.getId(), internalModel, personId, texts, statuses, applicationOrigin,
            utkastStatus, pdfFooterAppName);
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        if (xmlBody == null || Strings.isNullOrEmpty(logicalAddress)) {
            throw new ModuleException("Request does not contain the original xml");
        }
        RegisterCertificateType request = JAXB.unmarshal(new StringReader(xmlBody), RegisterCertificateType.class);

        try {
            RegisterCertificateResponseType response = registerCertificateResponderInterface.registerCertificate(logicalAddress, request);

            handleResponse(response, request);
        } catch (SOAPFaultException e) {
            throw new ExternalServiceCallException(e);
        }
    }

    @Override
    public Utlatande getUtlatandeFromXml(String xml) throws ModuleException {
        try {
            return transportToInternal(JAXB.unmarshal(new StringReader(xml), RegisterCertificateType.class).getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    protected Intyg utlatandeToIntyg(TsDiabetesUtlatandeV4 utlatande)
        throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected RegisterCertificateValidator getRegisterCertificateValidator() {
        return new RegisterCertificateValidator(SCHEMATRON_FILE);
    }

    @Override
    protected RegisterCertificateType internalToTransport(TsDiabetesUtlatandeV4 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected TsDiabetesUtlatandeV4 transportToInternal(Intyg intyg)
        throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    private TsDiabetesUtlatandeV4 decorateWithSignature(TsDiabetesUtlatandeV4 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        List<CVType> types = new ArrayList<>();
        try {
            for (Svar svar : intyg.getSvar()) {
                if (INTYG_AVSER_SVAR_ID.equals(svar.getId())) {
                    for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                        if (INTYG_AVSER_DELSVAR_ID.equals(delsvar.getId())) {
                            CVType cv = TransportConverterUtil.getCVSvarContent(delsvar);
                            if (cv != null) {
                                types.add(cv);
                            }
                        }
                    }
                }
            }
        } catch (ConverterException e) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: {}", intyg.getIntygsId().getExtension(), e.getMessage());
            return null;
        }

        if (types.isEmpty()) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: Found no types.", intyg.getIntygsId().getExtension());
            return null;
        }

        return types.stream()
            .map(cv -> KorkortsbehorighetKod.fromCode(cv.getCode()))
            .map(KorkortsbehorighetKod::getDescription)
            .collect(Collectors.joining(", "));
    }

    @Override
    public String updateAfterSigning(String jsonModel, String signatureXml) throws ModuleException {
        if (signatureXml == null) {
            return jsonModel;
        }
        String base64EncodedSignatureXml = Base64.getEncoder().encodeToString(signatureXml.getBytes(Charset.forName("UTF-8")));
        return updateInternalAfterSigning(jsonModel, base64EncodedSignatureXml);
    }

    private String updateInternalAfterSigning(String internalModel, String base64EncodedSignatureXml)
        throws ModuleException {
        try {
            TsDiabetesUtlatandeV4 utlatande = decorateWithSignature(getInternal(internalModel), base64EncodedSignatureXml);
            return toInternalModelResponse(utlatande);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model with signature", e);
        }
    }

    @Override
    public PatientDetailResolveOrder getPatientDetailResolveOrder() {
        List<ResolveOrder> otherStrat = Arrays.asList(ResolveOrder.PU, ResolveOrder.PARAMS);

        return new PatientDetailResolveOrder(null, ImmutableList.of(), otherStrat);
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson,
        TypeAheadProvider typeAheadProvider) throws ModuleException {
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
        if (utlatande instanceof TsDiabetesUtlatandeV4) {
            return toInternalModelResponse(utlatande);
        }
        final var message = utlatande == null ? "null" : utlatande.getClass().toString();
        throw new IllegalArgumentException(
            "Utlatande was not instance of class TsDiabetesUtlatandeV4, utlatande was instance of class: " + message);
    }

    @Override
    public String getUpdatedJsonWithTestData(String model, FillType fillType, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var certificate = getCertificateFromJson(model, typeAheadProvider);
        TestabilityToolkit.fillCertificateWithTestData(certificate, fillType, new TsDiabetesV4TestabilityCertificateTestdataProvider());
        return getJsonFromCertificate(certificate, model);
    }

    @Override
    protected TsDiabetesUtlatandeV4 getInternal(String internalModel) throws ModuleException {
        try {
            final var tsDiabetesUtlatandeV4 = objectMapper.readValue(internalModel, TsDiabetesUtlatandeV4.class);
            unitMapperUtil.decorateWithMappedCareProvider(tsDiabetesUtlatandeV4);
            return tsDiabetesUtlatandeV4;
        } catch (IOException e) {
            throw new ModuleException("Could not read internal model", e);
        }
    }
}