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
package se.inera.intyg.common.fk7263.model.converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.model.internal.PrognosBedomning;
import se.inera.intyg.common.fk7263.model.internal.Rehabilitering;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.LocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.schemas.contract.Personnummer;

/**
 * Converts se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande Jaxb structure to
 * External model.
 *
 * @author marced
 */
public final class TransportToInternal {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportToInternal.class);

    private TransportToInternal() {
    }

    public static Fk7263Utlatande convert(LakarutlatandeType source) throws ConverterException {
        LOGGER.debug("Converting transport to internal model");

        Fk7263Utlatande utlatande = new Fk7263Utlatande();
        utlatande.setId(source.getLakarutlatandeId());
        utlatande.setTyp(Fk7263EntryPoint.MODULE_ID);

        if (source.getKommentar() != null && !source.getKommentar().isEmpty()) {
            utlatande.setKommentar(source.getKommentar());
        }
        utlatande.setGrundData(populateWithMetaData(source));

        populateWithDiagnose(utlatande, source.getMedicinsktTillstand());

        if (source.getBedomtTillstand() != null) {
            utlatande.setSjukdomsforlopp(source.getBedomtTillstand().getBeskrivning());
        }

        // Initiate list of ArbetsformagaNedsattningType for building Gilitighet later
        List<ArbetsformagaNedsattningType> nedsattningar = new ArrayList<>();

        for (FunktionstillstandType funktionstillstand : source.getFunktionstillstand()) {
            if (funktionstillstand.getArbetsformaga() != null) {
                // Populate list of ArbetsformagaNedsattningType
                nedsattningar.addAll(funktionstillstand.getArbetsformaga().getArbetsformagaNedsattning());

                populateWithArbetsformaga(utlatande, funktionstillstand);

                if (funktionstillstand.getArbetsformaga().getSysselsattning() != null) {
                    populateWithSysselsattning(utlatande, funktionstillstand.getArbetsformaga().getSysselsattning());
                }

                if (utlatande.isNuvarandeArbete() && funktionstillstand.getArbetsformaga().getArbetsuppgift() != null) {
                    utlatande.setNuvarandeArbetsuppgifter(funktionstillstand.getArbetsformaga().getArbetsuppgift().getTypAvArbetsuppgift());
                }

                populateWithPrognos(utlatande, funktionstillstand.getArbetsformaga());
            }

            populateWithFunktionstillstand(utlatande, funktionstillstand);
        }

        // Finally determine Giltighet using the list
        utlatande.setGiltighet(buildGiltighet(nedsattningar));

        for (AktivitetType aktivitetType : source.getAktivitet()) {
            populateWithAktivitetType(utlatande, aktivitetType);
        }

        for (ReferensType referensType : source.getReferens()) {
            populateWithReferens(utlatande, referensType);
        }

        for (VardkontaktType vardkontaktType : source.getVardkontakt()) {
            populateWithVardkontakt(utlatande, vardkontaktType);
        }

        return utlatande;
    }

    private static LocalDateInterval buildGiltighet(List<ArbetsformagaNedsattningType> source) {
        return new LocalDateInterval(getValidFromDate(source), getValidToDate(source));
    }

    public static LocalDate getValidToDate(List<ArbetsformagaNedsattningType> source) {
        LocalDate toDate = null;
        for (ArbetsformagaNedsattningType nedsattning : source) {
            LocalDateInterval nextObservationsperiod = new LocalDateInterval(nedsattning.getVaraktighetFrom(),
                nedsattning.getVaraktighetTom());
            if (toDate == null || toDate.isBefore(nextObservationsperiod.getFrom())) {
                toDate = nextObservationsperiod.getTom();
            }
        }
        return toDate;
    }

    public static LocalDate getValidFromDate(List<ArbetsformagaNedsattningType> source) {
        LocalDate fromDate = null;
        for (ArbetsformagaNedsattningType nedsattning : source) {
            LocalDateInterval nextObservationsperiod = new LocalDateInterval(nedsattning.getVaraktighetFrom(),
                nedsattning.getVaraktighetTom());
            if (fromDate == null || fromDate.isAfter(nextObservationsperiod.getFrom())) {
                fromDate = nextObservationsperiod.getFrom();
            }
        }
        return fromDate;
    }

    private static void populateWithVardkontakt(Fk7263Utlatande utlatande, VardkontaktType source) {
        switch (source.getVardkontakttyp()) {
            case MIN_TELEFONKONTAKT_MED_PATIENTEN:
                utlatande.setTelefonkontaktMedPatienten(new InternalDate(source.getVardkontaktstid()));
                break;
            case MIN_UNDERSOKNING_AV_PATIENTEN:
                utlatande.setUndersokningAvPatienten(new InternalDate(source.getVardkontaktstid()));
                break;
        }
    }

    private static void populateWithReferens(Fk7263Utlatande utlatande, ReferensType source) throws ConverterException {
        switch (source.getReferenstyp()) {
            case ANNAT:
                utlatande.setAnnanReferens(new InternalDate(source.getDatum()));
                break;
            case JOURNALUPPGIFTER:
                utlatande.setJournaluppgifter(new InternalDate(source.getDatum()));
                break;
        }
    }

    private static void populateWithAktivitetType(Fk7263Utlatande utlatande, AktivitetType source) throws ConverterException {
        if (source.getAktivitetskod() == null) {
            return;
        }
        switch (source.getAktivitetskod()) {
            case PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN:
                utlatande.setAtgardInomSjukvarden(source.getBeskrivning());
                break;
            case PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD:
                utlatande.setAnnanAtgard(source.getBeskrivning());
                break;
            case ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
                utlatande.setRehabilitering(Rehabilitering.rehabiliteringAktuell);
                break;
            case ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL:
                utlatande.setRehabilitering(Rehabilitering.rehabiliteringEjAktuell);
                break;
            case GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
                utlatande.setRehabilitering(Rehabilitering.rehabiliteringGarInteAttBedoma);
                break;
            case PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN:
                utlatande.setRekommendationKontaktForetagshalsovarden(true);
                break;
            case PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN:
                utlatande.setRekommendationKontaktArbetsformedlingen(true);
                break;
            case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT:
                utlatande.setRessattTillArbeteAktuellt(true);
                break;
            case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT:
                utlatande.setRessattTillArbeteEjAktuellt(true);
                break;
            case KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL:
                utlatande.setKontaktMedFk(true);
                break;
            case AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA:
                utlatande.setAvstangningSmittskydd(true);
                break;
            case OVRIGT:
                utlatande.setRekommendationOvrigtCheck(true);
                utlatande.setRekommendationOvrigt(source.getBeskrivning());
                break;
        }

    }

    private static void populateWithSysselsattning(Fk7263Utlatande utlatande, List<SysselsattningType> sysselsattnings)
        throws ConverterException {
        for (SysselsattningType sysselsattning : sysselsattnings) {
            switch (sysselsattning.getTypAvSysselsattning()) {
                case NUVARANDE_ARBETE:
                    utlatande.setNuvarandeArbete(true);
                    break;
                case ARBETSLOSHET:
                    utlatande.setArbetsloshet(true);
                    break;
                case FORALDRALEDIGHET:
                    utlatande.setForaldrarledighet(true);
                    break;
            }
        }
    }

    private static void populateWithDiagnose(Fk7263Utlatande utlatande, MedicinsktTillstandType source) throws ConverterException {
        if (source == null) {
            return;
        }
        utlatande.setDiagnosBeskrivning(source.getBeskrivning());
        utlatande.setDiagnosKod(source.getTillstandskod().getCode());
        Diagnoskodverk kodverk = Diagnoskodverk.getEnumByCodeSystem(source.getTillstandskod().getCodeSystem());
        if (source.getTillstandskod().getCodeSystem() != null && kodverk != null) {
            utlatande.setDiagnosKodsystem1(kodverk.name());
        } else {
            utlatande.setDiagnosKodsystem1(Diagnoskodverk.ICD_10_SE.name());
        }
    }

    /**
     * Create IntyMetadata object with information regarding Patient, SkapadAv and Skickat- and SkapatDatum.
     *
     * @param source {@link LakarutlatandeType}
     * @return {@link GrundData}
     */
    private static GrundData populateWithMetaData(LakarutlatandeType source) {
        GrundData metadata = new GrundData();
        metadata.setPatient(convertPatient(source.getPatient()));
        metadata.setSigneringsdatum(source.getSigneringsdatum());
        metadata.setSkapadAv(convertSkapadAv(source.getSkapadAvHosPersonal()));
        return metadata;
    }

    /**
     * Create Internal HoSPersonal from transport format.
     *
     * @param source HosPersonalType
     * @return HoSPersonal
     */
    private static HoSPersonal convertSkapadAv(HosPersonalType source) {
        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setForskrivarKod(source.getForskrivarkod());
        skapadAv.setFullstandigtNamn(source.getFullstandigtNamn());
        skapadAv.setPersonId(source.getPersonalId().getExtension());
        skapadAv.setVardenhet(convertVardenhet(source.getEnhet()));
        return skapadAv;
    }

    /**
     * Create Vardenhet from transportformat.
     *
     * @param source EnhetType
     * @return Vardenhet
     */
    private static Vardenhet convertVardenhet(EnhetType source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setArbetsplatsKod(source.getArbetsplatskod().getExtension());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setEnhetsid(source.getEnhetsId().getExtension());
        vardenhet.setEnhetsnamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    /**
     * Create Internal Vardgivare from transportformat.
     *
     * @param source VardgivareType
     * @return Vardgivare
     */
    private static Vardgivare convertVardgivare(VardgivareType source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getVardgivareId().getExtension());
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        return vardgivare;
    }

    /**
     * Create Internal Patient from transport format.
     *
     * @param source PatientType
     * @return Patient
     */
    private static Patient convertPatient(PatientType source) {
        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer(source.getPersonId().getExtension()).get());
        return patient;
    }

    private static void populateWithFunktionstillstand(Fk7263Utlatande utlatande, FunktionstillstandType source) throws ConverterException {

        switch (source.getTypAvFunktionstillstand()) {
            case AKTIVITET:
                if (source.getBeskrivning() != null && !source.getBeskrivning().isEmpty()) {
                    utlatande.setAktivitetsbegransning(source.getBeskrivning());
                }
                break;
            case KROPPSFUNKTION:
                if (source.getBeskrivning() != null && !source.getBeskrivning().isEmpty()) {
                    utlatande.setFunktionsnedsattning(source.getBeskrivning());
                }
                break;
        }
    }

    private static void populateWithPrognos(Fk7263Utlatande utlatande, ArbetsformagaType source) throws ConverterException {

        if (source.getPrognosangivelse() != null || source.getMotivering() != null) {
            if (source.getPrognosangivelse() != null) {
                switch (source.getPrognosangivelse()) {
                    case ATERSTALLAS_DELVIS:
                        utlatande.setPrognosBedomning(PrognosBedomning.arbetsformagaPrognosJaDelvis);
                        break;
                    case ATERSTALLAS_HELT:
                        utlatande.setPrognosBedomning(PrognosBedomning.arbetsformagaPrognosJa);
                        break;
                    case DET_GAR_INTE_ATT_BEDOMMA:
                        utlatande.setPrognosBedomning(PrognosBedomning.arbetsformagaPrognosGarInteAttBedoma);
                        break;
                    case INTE_ATERSTALLAS:
                        utlatande.setPrognosBedomning(PrognosBedomning.arbetsformagaPrognosNej);
                        break;
                }
            }
            utlatande.setArbetsformagaPrognos(source.getMotivering());
        }
    }

    /**
     * Convert nedsattning to formaga. Note: kind of backwards, but meaning is opposite in transport model vs domain
     * model..
     *
     * @param source source
     */
    private static void populateWithArbetsformaga(Fk7263Utlatande utlatande, FunktionstillstandType source) {
        for (ArbetsformagaNedsattningType nedsattning : source.getArbetsformaga().getArbetsformagaNedsattning()) {
            if (nedsattning.getNedsattningsgrad() != null && nedsattning.getVaraktighetFrom() != null
                && nedsattning.getVaraktighetTom() != null) {
                switch (nedsattning.getNedsattningsgrad()) {
                    case HELT_NEDSATT:
                        utlatande.setNedsattMed100(makeInterval(nedsattning));
                        break;
                    case NEDSATT_MED_3_4:
                        utlatande.setNedsattMed75(makeInterval(nedsattning));
                        break;
                    case NEDSATT_MED_1_2:
                        utlatande.setNedsattMed50(makeInterval(nedsattning));
                        break;
                    case NEDSATT_MED_1_4:
                        utlatande.setNedsattMed25(makeInterval(nedsattning));
                        break;
                }
            }
        }
    }

    private static InternalLocalDateInterval makeInterval(ArbetsformagaNedsattningType source) {
        InternalLocalDateInterval observationsperiod = new InternalLocalDateInterval();
        observationsperiod.setFrom(new InternalDate(source.getVaraktighetFrom()));
        observationsperiod.setTom(new InternalDate(source.getVaraktighetTom()));
        return observationsperiod;
    }
}
