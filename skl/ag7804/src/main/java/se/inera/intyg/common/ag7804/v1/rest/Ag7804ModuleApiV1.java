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
package se.inera.intyg.common.ag7804.v1.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.ag7804.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.ag7804.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.agparent.rest.AgParentModuleApi;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.ag7804.v1")
public class Ag7804ModuleApiV1 extends AgParentModuleApi<Ag7804UtlatandeV1> {

    public static final String SCHEMATRON_FILE = "ag7804.v1.sch";
    private static final Logger LOG = LoggerFactory.getLogger(Ag7804ModuleApiV1.class);

    private static final String CERTIFICATE_FILE_PREFIX = "arbetsgivarintyg";
    private static final String MINIMAL_CERTIFICATE_FILE_PREFIX = "minimalt_arbetsgivarintyg";

    public Ag7804ModuleApiV1() {
        super(Ag7804UtlatandeV1.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
            throws ModuleException {
        Ag7804UtlatandeV1 utlatande = getInternal(internalModel);
        // TODO:implement
        return null;
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {
        // TODO: implement
        return null;
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(Ag7804UtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected Ag7804UtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(Ag7804UtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected Ag7804UtlatandeV1 decorateDiagnoserWithDescriptions(Ag7804UtlatandeV1 utlatande) {
        if (utlatande.getDiagnoser() != null) {
            List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
                    .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                            moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
                    .collect(Collectors.toList());
            return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
        } else {
            return utlatande;
        }
    }

    @Override
    protected Ag7804UtlatandeV1 decorateWithSignature(Ag7804UtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        try {
            return transportToInternal(intyg).getSjukskrivningar().stream()
                    .map(Sjukskrivning::getPeriod)
                    .sorted(Comparator.comparing(InternalLocalDateInterval::fromAsLocalDate))
                    .reduce((a, b) -> new InternalLocalDateInterval(a.getFrom(), b.getTom()))
                    .map(interval -> interval.getFrom().toString() + " - " + interval.getTom().toString())
                    .orElse(null);

        } catch (ConverterException e) {
            throw new ModuleException("Could not convert Intyg to Utlatande and as a result could not get additional info", e);
        }
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
            throws ModuleException {
        try {
            if (!Ag7804UtlatandeV1.class.isInstance(template)) {
                LOG.error("Could not create a new internal Webcert model using template of wrong type");
                throw new ModuleConverterException("Could not create a new internal Webcert model using template of wrong type");
            }

            Ag7804UtlatandeV1 internal = (Ag7804UtlatandeV1) template;

            // Null out applicable fields
            Ag7804UtlatandeV1 renewCopy = internal.toBuilder()
                    .setKontaktMedAg(false)
                    .setAnledningTillKontakt(null)
                    .setUndersokningAvPatienten(null)
                    .setTelefonkontaktMedPatienten(null)
                    .setJournaluppgifter(null)
                    .setAnnatGrundForMU(null)
                    .setAnnatGrundForMUBeskrivning(null)
                    .setPrognos(null)
                    .setSjukskrivningar(new ArrayList<>())
                    .setArbetstidsforlaggning(null)
                    .setArbetstidsforlaggningMotivering(null)
                    .build();

            Relation relation = draftCopyHolder.getRelation();
            Optional<LocalDate> lastDateOfLastIntyg = internal.getSjukskrivningar().stream()
                    .sorted((s1, s2) -> s2.getPeriod().getTom().asLocalDate().compareTo(s1.getPeriod().getTom().asLocalDate()))
                    .map(sjukskrivning -> sjukskrivning.getPeriod().getTom().asLocalDate())
                    .findFirst();
            relation.setSistaGiltighetsDatum(lastDateOfLastIntyg.orElse(LocalDate.now()));
            Optional<Sjukskrivning.SjukskrivningsGrad> lastSjukskrivningsgradOfLastIntyg = internal.getSjukskrivningar().stream()
                    .sorted((s1, s2) -> s2.getPeriod().getTom().asLocalDate().compareTo(s1.getPeriod().getTom().asLocalDate()))
                    .map(sjukskrivning -> sjukskrivning.getSjukskrivningsgrad())
                    .findFirst();
            lastSjukskrivningsgradOfLastIntyg.ifPresent(grad -> relation.setSistaSjukskrivningsgrad(grad.getLabel()));

            draftCopyHolder.setRelation(relation);

            return toInternalModelResponse(webcertModelFactory.createCopy(draftCopyHolder, renewCopy));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

}
