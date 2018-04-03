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
package se.inera.intyg.common.support.modules.converter;

import com.google.common.base.Strings;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.validate.SamordningsnummerValidator;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.ArbetsplatsKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Befattning;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.HsaId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateTypeFormatEnum;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PersonId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvRelation;
import se.riv.clinicalprocess.healthcond.certificate.v3.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.MeddelandeReferens;
import se.riv.clinicalprocess.healthcond.certificate.v3.Patient;
import se.riv.clinicalprocess.healthcond.certificate.v3.Relation;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Vardgivare;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static se.inera.intyg.common.support.Constants.ARBETSPLATS_KOD_OID;
import static se.inera.intyg.common.support.Constants.BEFATTNING_KOD_OID;
import static se.inera.intyg.common.support.Constants.HSA_ID_OID;
import static se.inera.intyg.common.support.Constants.KV_RELATION_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.PERSON_ID_OID;
import static se.inera.intyg.common.support.Constants.SAMORDNING_ID_OID;

/**
 * Provides utility methods for converting domain objects from internal Java format to transport format.
 */
public final class InternalConverterUtil {

    private static final String NOT_AVAILABLE = "N/A";
    private static final int DATE_PARSE_SECTIONS = 3;
    private static final String GENERAL_DATE_FORMAT = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

    private InternalConverterUtil() {
    }

    /**
     * Converts the internal (Utlatande) to transport (Intyg).
     *
     * @param source              the source Utlatande
     * @param extendedPatientInfo if the certificate should support patients with sekretessmarkering
     * @return the converted Intyg
     */
    public static Intyg getIntyg(Utlatande source, boolean extendedPatientInfo) {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(getIntygsId(source));
        intyg.setVersion(getTextVersion(source));
        intyg.setSigneringstidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkickatTidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkapadAv(getSkapadAv(source.getGrundData().getSkapadAv()));
        intyg.setPatient(getPatient(source.getGrundData().getPatient(), extendedPatientInfo));
        decorateWithRelation(intyg, source);
        return intyg;
    }

    /**
     * Converts a internal representation of hosPersonal to transport.
     *
     * @param hoSPersonal the interal version of the hosPersonal
     * @return the converted transport representation
     */
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

    /**
     * Converts a internal version of a personnummer to transport.
     *
     * @param pnr the internal version
     * @return a transport representation of the personnummer
     */
    public static PersonId getPersonId(Personnummer pnr) {
        PersonId personId = new PersonId();
        personId.setRoot(SamordningsnummerValidator.isSamordningsNummer(Optional.of(pnr)) ? SAMORDNING_ID_OID : PERSON_ID_OID);
        personId.setExtension(pnr.getPersonnummer());
        return personId;
    }

    /**
     * Returns a transport version of the id of the certificate.
     *
     * @param source the source Utlatande
     * @return a transport representation of the id
     */
    public static IntygId getIntygsId(Utlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getId());
        return intygId;
    }

    /**
     * Returns a transport representation of a String as a HSA-id.
     *
     * @param id the String containing the code of the HsaId
     * @return the transport version of the HsaId
     */
    public static HsaId getHsaId(String id) {
        HsaId hsaId = new HsaId();
        hsaId.setRoot(HSA_ID_OID);
        hsaId.setExtension(id);
        return hsaId;
    }

    /**
     * Returns an internalDate as a String.
     *
     * @param internalDate the source date
     * @return a safe String to use as a date in transport
     */
    public static String getInternalDateContent(InternalDate internalDate) {
        return internalDate.isValidDate() ? internalDate.asLocalDate().toString() : internalDate.toString();
    }
    /**
     * Returns an internalDate as a String where unfilled information is completed with zeros.
     *
     * @param internalDate the source date
     * @return the String representation of the date
     */
    public static String getInternalDateContentFillWithZeros(InternalDate internalDate) {
        return internalDate.isValidDate() ? internalDate.asLocalDate().toString() : fillWithZeros(internalDate);
    }

    /**
     * Wrap the code in transport layer object.
     *
     * @param sourceArbetsplatsKod the code
     * @return the resulting transport layer object
     */
    public static ArbetsplatsKod getArbetsplatsKod(String sourceArbetsplatsKod) {
        ArbetsplatsKod arbetsplatsKod = new ArbetsplatsKod();
        arbetsplatsKod.setRoot(ARBETSPLATS_KOD_OID);
        arbetsplatsKod.setExtension(sourceArbetsplatsKod);
        return arbetsplatsKod;
    }

    /**
     * Returns a MeddelandeReferens which contain the relation information.
     *
     * @param utlatande the source Utlatande
     * @param type      the type of the relation
     * @return the transport version of the reference in Ärendekommunikation
     */
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

    /**
     * Only add a svar if it is neither empty String or null.
     *
     * @param svars      the object where the svar will be saved
     * @param svarsId    the id of the svar
     * @param delsvarsId the id of the delsvar
     * @param content    the content which should be checked
     */
    public static void addIfNotBlank(List<Svar> svars, String svarsId, String delsvarsId, String content) {
        if (!Strings.nullToEmpty(content).trim().isEmpty()) {
            svars.add(aSvar(svarsId).withDelsvar(delsvarsId, content).build());
        }
    }

    /**
     * Only add a svar if it is not null.
     *
     * @param svars      the object where the svar will be saved
     * @param svarsId    the id of the svar
     * @param delsvarsId the id of the delsvar
     * @param content    the content which should be checked
     */
    public static void addIfNotNull(List<Svar> svars, String svarsId, String delsvarsId, Boolean content) {
        if (content != null) {
            svars.add(aSvar(svarsId).withDelsvar(delsvarsId, content.toString()).build());
        }
    }

    /**
     * Creates a DatePeriodType from a from and to date.
     *
     * @param from the beginning of the period
     * @param tom  the end of the period
     * @return the DatePeriodType which contain the from and to date
     */
    public static JAXBElement<DatePeriodType> aDatePeriod(LocalDate from, LocalDate tom) {
        DatePeriodType period = new DatePeriodType();
        period.setStart(from);
        period.setEnd(tom);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:3", "datePeriod"),
                DatePeriodType.class, null, period);
    }

    /**
     * Creates a PartialDateType from a specified format and source Temporal.
     * <p>
     * A Temporal is the new superclass of timerelated objects in Java 8.
     *
     * @param format  the desired format
     * @param partial the source Temporal
     * @return the PartialDateType which contain the Temporal
     */
    public static JAXBElement<PartialDateType> aPartialDate(PartialDateTypeFormatEnum format, Temporal partial) {
        PartialDateType partialDate = new PartialDateType();
        partialDate.setFormat(format);
        partialDate.setValue(partial);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:3", "partialDate"),
                PartialDateType.class, null, partialDate);
    }

    /**
     * Constructs a CVType.
     *
     * @param codeSystem  the CodeSystem of the CVType
     * @param code        the Code of the CVType
     * @param displayName the DisplayName of the CVType (optional)
     * @return the CVType
     */
    public static JAXBElement<CVType> aCV(String codeSystem, String code, String displayName) {
        CVType cv = new CVType();
        cv.setCodeSystem(codeSystem);
        cv.setCode(code);
        cv.setDisplayName(displayName);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:3", "cv"),
                CVType.class, null, cv);
    }

    /**
     * Construct a SvarBuilder.
     *
     * @param id the id of the Svar to be constructed
     * @return the builder which are to be filled with additional information
     */
    public static SvarBuilder aSvar(String id) {
        return new SvarBuilder(id, null);
    }

    /**
     * Construct a SvarBuilder with an additional instance number.
     *
     * @param id      the id of the Svar to be constructed
     * @param instans the instance number of Svar to be constructed
     * @return the builder which are to be filled with additional information
     */
    public static SvarBuilder aSvar(String id, Integer instans) {
        return new SvarBuilder(id, instans);
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

    private static Vardgivare getVardgivare(se.inera.intyg.common.support.model.common.internal.Vardgivare sourceVardgivare) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivareId(getHsaId(sourceVardgivare.getVardgivarid()));
        vardgivare.setVardgivarnamn(emptyStringIfNull(sourceVardgivare.getVardgivarnamn()));
        return vardgivare;
    }

    private static Patient getPatient(se.inera.intyg.common.support.model.common.internal.Patient sourcePatient,
                                      boolean extendedPatientInfo) {

        String pnr = sourcePatient.getPersonId().getPersonnummer();
        Personnummer personnummer = Personnummer.createPersonnummer(pnr).get();

        Patient patient = new se.riv.clinicalprocess.healthcond.certificate.v3.Patient();
        patient.setPersonId(getPersonId(personnummer));

        if (extendedPatientInfo) {
            patient.setEfternamn(emptyStringIfNull(sourcePatient.getEfternamn()));
            patient.setFornamn(emptyStringIfNull(sourcePatient.getFornamn()));
            patient.setMellannamn(sourcePatient.getMellannamn());
            patient.setPostadress(emptyStringIfNull(sourcePatient.getPostadress()));
            patient.setPostnummer(emptyStringIfNull(sourcePatient.getPostnummer()));
            patient.setPostort(emptyStringIfNull(sourcePatient.getPostort()));
        } else {
            patient.setEfternamn("");
            patient.setFornamn("");
            patient.setPostadress("");
            patient.setPostnummer("");
            patient.setPostort("");
        }
        return patient;
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

    private static String fillWithZeros(InternalDate internalDate) {
        StringBuilder sb = internalDate.toString().isEmpty() ? new StringBuilder("0000")
                : new StringBuilder(internalDate.toString());
        for (int i = 0; i < DATE_PARSE_SECTIONS; i++) {
            if (!sb.toString().matches(GENERAL_DATE_FORMAT)) {
                sb.append("-00");
            }
        }
        return sb.toString();
    }

    private static String emptyStringIfNull(String s) {
        return s != null ? s : "";
    }

    /**
     * Builder class which are used to construct a Svar with or without a Delsvar.
     * <p>
     * Typical usage is to use {@link InternalConverterUtil#aSvar(String)} or {@link InternalConverterUtil#aSvar(String, Integer)}
     * and then chain {@link SvarBuilder#withDelsvar(String, Object)} and then finally calling {@link SvarBuilder#build()}.
     */
    public static class SvarBuilder {
        public List<Delsvar> delSvars = new ArrayList<>();
        private String id;
        private Integer instans;

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

        /**
         * Builder method which are used to add a {@link Delsvar} to a {@link ArrayList} <{@link Delsvar}>.
         * If the content is null or empty, the method does not add
         * the delsvar to DelsvarsList
         *
         * @param delsvarsId the id of the delsvar.
         * @param content the content to add to the Delsvar.
         * @return SvarBuilder
         */
        public SvarBuilder withDelsvar(String delsvarsId, Object content) {

            if (content != null) {
                Delsvar delsvar = new Delsvar();
                delsvar.setId(delsvarsId);
                delsvar.getContent().add(content);
                delSvars.add(delsvar);
            }
            return this;
        }
    }
}
