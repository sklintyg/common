/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00251.v1.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.af00251.pdf.PdfGenerator;
import se.inera.intyg.common.af00251.support.AF00251EntryPoint;
import se.inera.intyg.common.af00251.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.af00251.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.af00251.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.af00251.v1.model.internal.Sjukfranvaro;
import se.inera.intyg.common.af_parent.rest.AfParentModuleApi;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.af00251.v1")
public class AF00251ModuleApiV1 extends AfParentModuleApi<AF00251UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(AF00251ModuleApiV1.class);
    public static final String SCHEMATRON_FILE = "af00251.v1.sch";

    public AF00251ModuleApiV1() {
        super(AF00251UtlatandeV1.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        AF00251UtlatandeV1 intyg = getInternal(internalModel);

        final AF00251UtlatandeV1 filtreratUtlatande = filterUncheckedSjukfranvaro(intyg);

        final String filtreradInternalModel = toInternalModelResponse(filtreratUtlatande);

        IntygTexts texts = getTexts(AF00251EntryPoint.MODULE_ID, filtreratUtlatande.getTextVersion());

        Personnummer personId = filtreratUtlatande.getGrundData()
            .getPatient()
            .getPersonId();
        return new PdfGenerator().generatePdf(filtreratUtlatande.getId(), filtreradInternalModel, "1", personId, texts, statuses,
            applicationOrigin, utkastStatus);
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {
        return null;
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(AF00251UtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected AF00251UtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(AF00251UtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected AF00251UtlatandeV1 decorateWithSignature(AF00251UtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder()
            .setSignature(base64EncodedSignatureXml)
            .build();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        try {
            String additionalInfo = null;
            final AF00251UtlatandeV1 af00251UtlatandeV1 = transportToInternal(intyg);
            if (af00251UtlatandeV1.getSjukfranvaro() != null && af00251UtlatandeV1.getSjukfranvaro().size() > 0) {
                additionalInfo = af00251UtlatandeV1.getSjukfranvaro().stream()
                    .map(Sjukfranvaro::getPeriod)
                    .sorted(Comparator.comparing(InternalLocalDateInterval::fromAsLocalDate))
                    .reduce((a, b) -> new InternalLocalDateInterval(a.getFrom(), b.getTom()))
                    .map(interval -> interval.getFrom().toString() + " - " + interval.getTom().toString())
                    .orElse(null);
            }
            return additionalInfo;

        } catch (ConverterException e) {
            throw new ModuleException("Could not convert Intyg to Utlatande and as a result could not get additional info", e);
        }
    }

    @Override
    public String updateBeforeSigning(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        final AF00251UtlatandeV1 utlatandeV1 = getInternal(internalModel);

        final AF00251UtlatandeV1 filtreratUtlatande = filterUncheckedSjukfranvaro(utlatandeV1);

        return super.updateBeforeSigning(toInternalModelResponse(filtreratUtlatande), hosPerson, signingDate);
    }

    AF00251UtlatandeV1 filterUncheckedSjukfranvaro(AF00251UtlatandeV1 utlatandeV1) {
        final List<Sjukfranvaro> filtreradSjukfranvaro = utlatandeV1.getSjukfranvaro()
            .stream()
            .filter(sjukfranvaro -> nullToFalse(sjukfranvaro.getChecked()))
            .collect(Collectors.toList());

        return utlatandeV1.toBuilder()
            .setSjukfranvaro(filtreradSjukfranvaro)
            .build();
    }

    boolean nullToFalse(Boolean value) {
        if (value == null) {
            return false;
        }
        return value;
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
        throws ModuleException {
        try {
            if (!AF00251UtlatandeV1.class.isInstance(template)) {
                LOG.error("Could not create a new internal Webcert model using template of wrong type");
                throw new ModuleConverterException("Could not create a new internal Webcert model using template of wrong type");
            }

            AF00251UtlatandeV1 internal = (AF00251UtlatandeV1) template;

            // Null out applicable fields
            AF00251UtlatandeV1 renewCopy = internal.toBuilder()
                .setSjukfranvaro(null)
                .setUndersokningsDatum(null)
                .setAnnatDatum(null)
                .setAnnatBeskrivning(null)
                .build();

            Relation relation = draftCopyHolder.getRelation();
            if (internal.getSjukfranvaro() != null) {
                Optional<LocalDate> lastDateOfLastIntyg = internal.getSjukfranvaro().stream()
                    .sorted((s1, s2) -> s2.getPeriod().getTom().asLocalDate().compareTo(s1.getPeriod().getTom().asLocalDate()))
                    .map(sjukskrivning -> sjukskrivning.getPeriod().getTom().asLocalDate())
                    .findFirst();
                relation.setSistaGiltighetsDatum(lastDateOfLastIntyg.orElse(LocalDate.now()));
                Optional<Integer> lastSjukskrivningsgradOfLastIntyg = internal.getSjukfranvaro().stream()
                    .sorted((s1, s2) -> s2.getPeriod().getTom().asLocalDate().compareTo(s1.getPeriod().getTom().asLocalDate()))
                    .map(sjukskrivning -> sjukskrivning.getNiva())
                    .findFirst();
                lastSjukskrivningsgradOfLastIntyg.ifPresent(grad -> relation.setSistaSjukskrivningsgrad(grad.toString()));
            }
            draftCopyHolder.setRelation(relation);

            return toInternalModelResponse(webcertModelFactory.createCopy(draftCopyHolder, renewCopy));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }
}
