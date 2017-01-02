/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.converter;

import static se.inera.intyg.common.support.Constants.*;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.services.BefattningService;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.Patient;
import se.riv.clinicalprocess.healthcond.certificate.v2.Relation;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Vardgivare;

/**
 * Provides utility methods for converting domain objects from internal Java format to transport format.
 */
public final class InternalConverterUtil {

    private static final String NOT_AVAILABLE = "N/A";

    private InternalConverterUtil() {
    }

    public static Intyg getIntyg(Utlatande source) {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(getIntygsId(source));
        intyg.setVersion(getTextVersion(source));
        intyg.setSigneringstidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkickatTidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkapadAv(getSkapadAv(source.getGrundData().getSkapadAv()));
        intyg.setPatient(getPatient(source.getGrundData().getPatient()));
        decorateWithRelation(intyg, source);
        return intyg;
    }

    public static HosPersonal getSkapadAv(HoSPersonal hoSPersonal) {
        HosPersonal skapadAv = new HosPersonal();
        skapadAv.setPersonalId(getHsaId(hoSPersonal.getPersonId()));
        skapadAv.setFullstandigtNamn(hoSPersonal.getFullstandigtNamn());
        skapadAv.setForskrivarkod(hoSPersonal.getForskrivarKod());
        skapadAv.setEnhet(getEnhet(hoSPersonal.getVardenhet()));
        for (String sourceBefattning : hoSPersonal.getBefattningar()) {
            Befattning befattning = new Befattning();
            befattning.setCodeSystem(BEFATTNING_KOD_OID);
            befattning.setCode(sourceBefattning);
            befattning.setDisplayName(BefattningService.getDescriptionFromCode(sourceBefattning).orElse(null));
            skapadAv.getBefattning().add(befattning);
        }
        for (String sourceKompetens : hoSPersonal.getSpecialiteter()) {
            Specialistkompetens kompetens = new Specialistkompetens();
            /*
             * INTYG-2875: Due to HSA sending speciality codes and names in two incoherent lists we only keep speciality
             * names, hence code is not available.
             */
            kompetens.setCode(NOT_AVAILABLE);
            kompetens.setDisplayName(sourceKompetens);
            skapadAv.getSpecialistkompetens().add(kompetens);
        }
        return skapadAv;
    }

    private static Enhet getEnhet(Vardenhet sourceVardenhet) {
        Enhet vardenhet = new Enhet();
        vardenhet.setEnhetsId(getHsaId(sourceVardenhet.getEnhetsid()));
        vardenhet.setEnhetsnamn(emptyStringIfNull(sourceVardenhet.getEnhetsnamn()));
        vardenhet.setPostnummer(emptyStringIfNull(sourceVardenhet.getPostnummer()));
        vardenhet.setPostadress(emptyStringIfNull(sourceVardenhet.getPostadress()));
        vardenhet.setPostort(emptyStringIfNull(sourceVardenhet.getPostort()));
        vardenhet.setTelefonnummer(emptyStringIfNull(sourceVardenhet.getTelefonnummer()));
        vardenhet.setEpost(sourceVardenhet.getEpost());
        vardenhet.setVardgivare(getVardgivare(sourceVardenhet.getVardgivare()));
        vardenhet.setArbetsplatskod(getArbetsplatsKod(sourceVardenhet.getArbetsplatsKod()));
        return vardenhet;
    }

    public static ArbetsplatsKod getArbetsplatsKod(String sourceArbetsplatsKod) {
        ArbetsplatsKod arbetsplatsKod = new ArbetsplatsKod();
        arbetsplatsKod.setRoot(ARBETSPLATS_KOD_OID);
        arbetsplatsKod.setExtension(sourceArbetsplatsKod);
        return arbetsplatsKod;
    }

    private static Vardgivare getVardgivare(se.inera.intyg.common.support.model.common.internal.Vardgivare sourceVardgivare) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivareId(getHsaId(sourceVardgivare.getVardgivarid()));
        vardgivare.setVardgivarnamn(emptyStringIfNull(sourceVardgivare.getVardgivarnamn()));
        return vardgivare;
    }

    private static Patient getPatient(se.inera.intyg.common.support.model.common.internal.Patient sourcePatient) {
        Patient patient = new se.riv.clinicalprocess.healthcond.certificate.v2.Patient();
        patient.setEfternamn(sourcePatient.getEfternamn());
        patient.setFornamn(emptyStringIfNull(sourcePatient.getFornamn()));
        patient.setMellannamn(sourcePatient.getMellannamn());
        patient.setPersonId(getPersonId(new Personnummer(sourcePatient.getPersonId().getPersonnummer())));
        patient.setPostadress(emptyStringIfNull(sourcePatient.getPostadress()));
        patient.setPostnummer(emptyStringIfNull(sourcePatient.getPostnummer()));
        patient.setPostort(emptyStringIfNull(sourcePatient.getPostort()));
        return patient;
    }

    public static PersonId getPersonId(Personnummer pnr) {
        PersonId personId = new PersonId();
        personId.setRoot(pnr.isSamordningsNummer() ? SAMORDNING_ID_OID : PERSON_ID_OID);
        personId.setExtension(pnr.getPersonnummerWithoutDash());
        return personId;
    }

    public static IntygId getIntygsId(Utlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getId());
        return intygId;
    }

    private static String getTextVersion(Utlatande source) {
        return emptyStringIfNull(source.getTextVersion());
    }

    private static void decorateWithRelation(Intyg intyg, Utlatande source) {
        if (source.getGrundData().getRelation() == null || source.getGrundData().getRelation().getRelationKod() == null) {
            return;
        }
        Relation relation = new Relation();

        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getGrundData().getRelation().getRelationIntygsId());

        TypAvRelation typAvRelation = new TypAvRelation();
        typAvRelation.setCode(source.getGrundData().getRelation().getRelationKod().value());
        typAvRelation.setCodeSystem(KV_RELATION_CODE_SYSTEM);
        typAvRelation.setDisplayName(source.getGrundData().getRelation().getRelationKod().getKlartext());

        relation.setIntygsId(intygId);
        relation.setTyp(typAvRelation);

        intyg.getRelation().add(relation);
    }

    public static HsaId getHsaId(String id) {
        HsaId hsaId = new HsaId();
        hsaId.setRoot(HSA_ID_OID);
        hsaId.setExtension(id);
        return hsaId;
    }

    private static String emptyStringIfNull(String s) {
        return s != null ? s : "";
    }

    public static MeddelandeReferens getMeddelandeReferensOfType(Utlatande utlatande, RelationKod type) {
        if (utlatande.getGrundData().getRelation() == null || !type.equals(utlatande.getGrundData().getRelation().getRelationKod())) {
            return null;
        }

        MeddelandeReferens mr = new MeddelandeReferens();
        mr.setMeddelandeId(utlatande.getGrundData().getRelation().getMeddelandeId());
        if (utlatande.getGrundData().getRelation().getReferensId() != null) {
            mr.setReferensId(utlatande.getGrundData().getRelation().getReferensId());
        }
        return mr;
    }

    public static void addIfNotBlank(List<Svar> svars, String svarsId, String delsvarsId, String content) {
        if (!StringUtils.isBlank(content)) {
            svars.add(aSvar(svarsId).withDelsvar(delsvarsId, content).build());
        }
    }

    public static void addIfNotNull(List<Svar> svars, String svarsId, String delsvarsId, Boolean content) {
        if (content != null) {
            svars.add(aSvar(svarsId).withDelsvar(delsvarsId, content.toString()).build());
        }
    }

    public static JAXBElement<DatePeriodType> aDatePeriod(LocalDate from, LocalDate tom) {
        DatePeriodType period = new DatePeriodType();
        period.setStart(from);
        period.setEnd(tom);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "datePeriod"), DatePeriodType.class, null,
                period);
    }

    public static JAXBElement<PartialDateType> aPartialDate(PartialDateTypeFormatEnum format, Temporal partial) {
        PartialDateType partialDate = new PartialDateType();
        partialDate.setFormat(format);
        partialDate.setValue(partial);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "partialDate"), PartialDateType.class, null,
                partialDate);
    }

    public static JAXBElement<CVType> aCV(String codeSystem, String code, String displayName) {
        CVType cv = new CVType();
        cv.setCodeSystem(codeSystem);
        cv.setCode(code);
        cv.setDisplayName(displayName);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "cv"), CVType.class, null, cv);
    }

    public static SvarBuilder aSvar(String id) {
        return new SvarBuilder(id, null);
    }

    public static SvarBuilder aSvar(String id, Integer instans) {
        return new SvarBuilder(id, instans);
    }

    public static class SvarBuilder {
        private String id;
        private Integer instans;
        public List<Delsvar> delSvars = new ArrayList<>();

        SvarBuilder(String id, Integer instans) {
            this.id = id;
            this.instans = instans;
        }

        public Svar build() {
            Svar svar = new Svar();
            svar.setId(id);
            svar.setInstans(instans);
            svar.getDelsvar().addAll(delSvars);
            return svar;
        }

        public SvarBuilder withDelsvar(String delsvarsId, Object content) {
            Delsvar delsvar = new Delsvar();
            delsvar.setId(delsvarsId);
            delsvar.getContent().add(content);
            delSvars.add(delsvar);
            return this;
        }
    }
}
