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
package se.inera.intyg.common.tstrk1009.v1.rest;

import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PARAMS;
import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PU;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.soap.SOAPFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intyg.common.tstrk1009.support.Tstrk1009EntryPoint;
import se.inera.intyg.common.tstrk1009.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.tstrk1009.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.tstrk1009.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;
import se.inera.intyg.common.tstrk1009.v1.pdf.PdfGenerator;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten, Mina-Intyg & Webcert).
 */
@Component("moduleapi.tstrk1009.v1")
public class Tstrk1009ModuleApiV1 extends TsParentModuleApi<Tstrk1009UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(Tstrk1009UtlatandeV1.class);

    public Tstrk1009ModuleApiV1() {
        super(Tstrk1009UtlatandeV1.class);
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        try {
            Tstrk1009UtlatandeV1 utlatande = getInternal(internalModel);
            IntygTexts texts = getTexts(Tstrk1009EntryPoint.MODULE_ID, utlatande.getTextVersion());
            Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
            return new PdfGenerator().generatePdf(
                utlatande.getId(), internalModel, personId, texts,
                statuses, applicationOrigin, utkastStatus);
        } catch (Exception e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate", e);
        }
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        if (xmlBody == null || Strings.isNullOrEmpty(logicalAddress)) {
            throw new ModuleException("Request does not contain the original xml");
        }
        JAXBElement<RegisterCertificateType> el = XmlMarshallerHelper.unmarshal(xmlBody);
        RegisterCertificateType request = el.getValue();

        try {
            RegisterCertificateResponseType response = registerCertificateResponderInterface.registerCertificate(logicalAddress, request);

            handleResponse(response, request);
        } catch (SOAPFaultException e) {
            throw new ExternalServiceCallException(e);
        }
    }

    @Override
    public Tstrk1009UtlatandeV1 getUtlatandeFromXml(String xmlBody) throws ModuleException {
        try {
            JAXBElement<RegisterCertificateType> el = XmlMarshallerHelper.unmarshal(xmlBody);
            return transportToInternal(el.getValue().getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    protected Intyg utlatandeToIntyg(Tstrk1009UtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected RegisterCertificateValidator getRegisterCertificateValidator() {
        return new RegisterCertificateValidator(Tstrk1009EntryPoint.SCHEMATRON_FILE);
    }

    @Override
    protected RegisterCertificateType internalToTransport(Tstrk1009UtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected Tstrk1009UtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    public PatientDetailResolveOrder getPatientDetailResolveOrder() {
        List<ResolveOrder> adressStrat = Arrays.asList(PARAMS, PU);
        List<ResolveOrder> otherStrat = Arrays.asList(PU, PARAMS);

        return new PatientDetailResolveOrder(null, adressStrat, otherStrat);
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        List<CVType> types = new ArrayList<>();
        try {
            for (Svar svar : intyg.getSvar()) {
                if (INTYG_AVSER_SVAR_ID_1.equals(svar.getId())) {
                    for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                        if (INTYG_AVSER_DELSVAR_ID_1.equals(delsvar.getId())) {
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
            .map(cv -> Korkortsbehorighet.fromCode(cv.getCode()))
            .map(Korkortsbehorighet::getValue)
            .collect(Collectors.joining(", "));
    }

    @Override
    public String getUtlatandeToInternalModelResponse(Utlatande utlatande) {
        throw new UnsupportedOperationException();
    }
}
