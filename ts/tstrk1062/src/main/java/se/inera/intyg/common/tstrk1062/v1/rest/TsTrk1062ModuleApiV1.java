/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1062.v1.rest;

import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.*;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint;
import se.inera.intyg.common.tstrk1062.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.tstrk1062.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.tstrk1062.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosKodad;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.inera.intyg.common.tstrk1062.v1.pdf.PdfGenerator;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component("moduleapi.tstrk1062.v1")
public class TsTrk1062ModuleApiV1 extends TsParentModuleApi<TsTrk1062UtlatandeV1> {

    public static final String SCHEMATRON_FILE = "tstrk1062.v1.sch";

    private static final Logger LOG = LoggerFactory.getLogger(TsTrk1062ModuleApiV1.class);

    @Autowired
    private PdfGenerator pdfGenerator;

    public TsTrk1062ModuleApiV1() {
        super(TsTrk1062UtlatandeV1.class);
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        TsTrk1062UtlatandeV1 utlatande = getInternal(internalModel);
        IntygTexts texts = getTexts(TsTrk1062EntryPoint.MODULE_ID, utlatande.getTextVersion());

        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        return pdfGenerator.generatePdf(utlatande.getId(), internalModel, personId, texts, statuses, applicationOrigin,
            utkastStatus);
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        if (Strings.isNullOrEmpty(xmlBody)) {
            throw new ModuleException("Request does not contain the original xml");
        }

        if (Strings.isNullOrEmpty(logicalAddress)) {
            throw new ModuleException("Request does not contain a logical address");
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
    public TsTrk1062UtlatandeV1 getUtlatandeFromXml(String xml) throws ModuleException {
        try {
            return transportToInternal(JAXB.unmarshal(new StringReader(xml), RegisterCertificateType.class).getIntyg());
        } catch (Exception e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    protected Intyg utlatandeToIntyg(TsTrk1062UtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande, moduleService);
    }

    @Override
    protected RegisterCertificateValidator getRegisterCertificateValidator() {
        return new RegisterCertificateValidator(SCHEMATRON_FILE);
    }

    @Override
    protected RegisterCertificateType internalToTransport(TsTrk1062UtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande, moduleService);
    }

    @Override
    protected TsTrk1062UtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected TsTrk1062UtlatandeV1 decorateDiagnoserWithDescriptions(TsTrk1062UtlatandeV1 utlatande) {
        final List<DiagnosKodad> diagnosKodadList = utlatande.getDiagnosKodad();
        if (diagnosKodadList != null && diagnosKodadList.size() > 0) {
            List<DiagnosKodad> decoratedDiagnoser = diagnosKodadList.stream()
                .map(diagnos -> DiagnosKodad.create(diagnos.getDiagnosKod(),
                    diagnos.getDiagnosKodSystem(),
                    diagnos.getDiagnosBeskrivning(),
                    moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem()),
                    diagnos.getDiagnosArtal()))
                .collect(Collectors.toList());
            return utlatande.toBuilder().setDiagnosKodad(decoratedDiagnoser).build();
        } else {
            return utlatande;
        }
    }

    @Override
    public PatientDetailResolveOrder getPatientDetailResolveOrder() {
        List<ResolveOrder> adressStrat = Arrays.asList(PARAMS, PU);
        List<ResolveOrder> otherStrat = Arrays.asList(PU, PARAMS);

        return new PatientDetailResolveOrder(null, adressStrat, otherStrat);
    }
}
