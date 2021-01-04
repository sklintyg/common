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
package se.inera.intyg.common.fk7263.model.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.validate.SamordningsnummerValidator;

public final class InternalToTransport {

    private static final String SAMSJUKLIGHET_FORELIGGER = "Samsjuklighet föreligger";

    private InternalToTransport() {
    }

    public static RegisterMedicalCertificateType getJaxbObject(Fk7263Utlatande source) throws ConverterException {
        RegisterMedicalCertificateType register = new RegisterMedicalCertificateType();
        register.setLakarutlatande(new LakarutlatandeType());
        if (source.getId() != null) {
            register.getLakarutlatande().setLakarutlatandeId(source.getId());
        }
        register.getLakarutlatande().setTypAvUtlatande(Fk7263EntryPoint.MODULE_DESCRIPTION);

        register.getLakarutlatande().setKommentar(buildKommentar(source));

        register.getLakarutlatande().setSigneringsdatum(source.getGrundData().getSigneringsdatum());
        register.getLakarutlatande().setSkickatDatum(source.getGrundData().getSigneringsdatum());
        register.getLakarutlatande().setPatient(patientToJaxb(source.getGrundData().getPatient()));
        register.getLakarutlatande().setSkapadAvHosPersonal(hosPersonalToJaxb(source.getGrundData().getSkapadAv()));

        if (!Strings.isNullOrEmpty(source.getSjukdomsforlopp())) {
            register.getLakarutlatande().setBedomtTillstand(sjukdomsforloppToJaxb(source.getSjukdomsforlopp()));
        }

        if (!Strings.isNullOrEmpty(source.getDiagnosKod())) {
            register.getLakarutlatande()
                .setMedicinsktTillstand(toMedicinsktTillstand(source.getDiagnosKod(), source.getDiagnosKodsystem1()));
            buildMedicinsktTillstandBeskrivning(register.getLakarutlatande().getMedicinsktTillstand(), source);
        }

        convertAktiviteter(register, source);

        convertReferenser(register, source);

        convertVardkontakter(register, source);

        if (!Strings.isNullOrEmpty(source.getFunktionsnedsattning())) {
            register.getLakarutlatande().getFunktionstillstand()
                .add(toFunktionstillstand(source.getFunktionsnedsattning(), TypAvFunktionstillstand.KROPPSFUNKTION));
        }

        // add arbetsformaga to aktivitetsbegransing
        FunktionstillstandType aktivitetsbegransing = toFunktionstillstand(source.getAktivitetsbegransning(),
            TypAvFunktionstillstand.AKTIVITET);
        aktivitetsbegransing.setArbetsformaga(toArbetsformaga(source));

        register.getLakarutlatande().getFunktionstillstand().add(aktivitetsbegransing);

        return register;
    }

    private static void buildMedicinsktTillstandBeskrivning(MedicinsktTillstandType tillstand, Fk7263Utlatande source) {
        List<String> parts = new ArrayList<>();
        // Beskrivning huvuddiagnos
        if (!Strings.isNullOrEmpty(source.getDiagnosBeskrivning1())) {
            parts.add(source.getDiagnosBeskrivning1());
        }
        if (source.getSamsjuklighet() != null && source.getSamsjuklighet()) {
            parts.add(SAMSJUKLIGHET_FORELIGGER);
        }
        // Bidiagnoser
        String bidiagnoser = buildDiagnoser(source);
        if (!Strings.isNullOrEmpty(bidiagnoser)) {
            parts.add(bidiagnoser);
        }
        // Text från fält2
        if (!Strings.isNullOrEmpty(source.getDiagnosBeskrivning())) {
            parts.add(source.getDiagnosBeskrivning());
        }
        if (!parts.isEmpty()) {
            tillstand.setBeskrivning(Joiner.on(". ").join(parts));
        } else {
            tillstand.setBeskrivning(null);
        }
    }

    private static String buildDiagnoser(Fk7263Utlatande source) {
        String diagnos1 = "";
        String diagnos2 = "";

        if (!Strings.isNullOrEmpty(source.getDiagnosKod2()) && !Strings.isNullOrEmpty(source.getDiagnosBeskrivning2())) {
            diagnos1 = source.getDiagnosKod2() + " " + source.getDiagnosBeskrivning2();
        }

        if (!Strings.isNullOrEmpty(source.getDiagnosKod3()) && !Strings.isNullOrEmpty(source.getDiagnosBeskrivning3())) {
            diagnos2 = source.getDiagnosKod3() + " " + source.getDiagnosBeskrivning3();
        }
        return Joiner.on(". ").skipNulls().join(Stream.of(diagnos1, diagnos2).map(Strings::emptyToNull).toArray());
    }

    private static String buildKommentar(Fk7263Utlatande source) {
        String annanRef = null;
        String prognosBedomning = null;
        String ovrigKommentar = null;
        String arbetstidsforlaggning = null;

        // Falt4b
        if (!Strings.isNullOrEmpty(source.getAnnanReferensBeskrivning())) {
            annanRef = "4b: " + source.getAnnanReferensBeskrivning();
        }
        // Falt8b

        if (!buildArbetstidsforlaggning(source).isEmpty()) {
            arbetstidsforlaggning = "8b: " + buildArbetstidsforlaggning(source);
        }
        // Falt10
        if (!Strings.isNullOrEmpty(source.getArbetsformagaPrognosGarInteAttBedomaBeskrivning())) {
            prognosBedomning = "10: " + source.getArbetsformagaPrognosGarInteAttBedomaBeskrivning();
        }
        if (!Strings.isNullOrEmpty(source.getKommentar())) {
            ovrigKommentar = source.getKommentar();
        }
        String ret = Joiner.on(". ").skipNulls().join(annanRef, arbetstidsforlaggning, prognosBedomning, ovrigKommentar);
        return !Strings.isNullOrEmpty(ret) ? ret : null;
    }

    private static String buildArbetstidsforlaggning(Fk7263Utlatande source) {
        List<String> parts = new ArrayList<>();
        if (!Strings.isNullOrEmpty(source.getNedsattMed25Beskrivning())) {
            parts.add(source.getNedsattMed25Beskrivning());
        }
        if (!Strings.isNullOrEmpty(source.getNedsattMed50Beskrivning())) {
            parts.add(source.getNedsattMed50Beskrivning());
        }
        if (!Strings.isNullOrEmpty(source.getNedsattMed75Beskrivning())) {
            parts.add(source.getNedsattMed75Beskrivning());
        }
        return Joiner.on(". ").join(parts);
    }

    private static FunktionstillstandType toFunktionstillstand(String source, TypAvFunktionstillstand tillstand) {
        FunktionstillstandType funktionstillstandType = new FunktionstillstandType();
        funktionstillstandType.setTypAvFunktionstillstand(tillstand);
        funktionstillstandType.setBeskrivning(source);
        return funktionstillstandType;
    }

    private static ArbetsformagaType toArbetsformaga(Fk7263Utlatande source) throws ConverterException {

        ArbetsformagaType arbetsformagaType = new ArbetsformagaType();

        if (!Strings.isNullOrEmpty(source.getArbetsformagaPrognos())) {
            arbetsformagaType.setMotivering(source.getArbetsformagaPrognos());
        }

        if (source.getPrognosBedomning() != null) {
            switch (source.getPrognosBedomning()) {
                case arbetsformagaPrognosGarInteAttBedoma:
                    arbetsformagaType.setPrognosangivelse(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA);
                    break;
                case arbetsformagaPrognosJa:
                    arbetsformagaType.setPrognosangivelse(Prognosangivelse.ATERSTALLAS_HELT);
                    break;
                case arbetsformagaPrognosJaDelvis:
                    arbetsformagaType.setPrognosangivelse(Prognosangivelse.ATERSTALLAS_DELVIS);
                    break;
                case arbetsformagaPrognosNej:
                    arbetsformagaType.setPrognosangivelse(Prognosangivelse.INTE_ATERSTALLAS);
                    break;
            }
        }

        arbetsformagaType.getSysselsattning().addAll(convertSysselsattnings(source));

        // attach arbetsuppgift if available
        if (source.isNuvarandeArbete() && !Strings.isNullOrEmpty(source.getNuvarandeArbetsuppgifter())) {
            ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
            arbetsuppgift.setTypAvArbetsuppgift(source.getNuvarandeArbetsuppgifter());
            arbetsformagaType.setArbetsuppgift(arbetsuppgift);
        }
        if (source.getNedsattMed25() != null) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();
            nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_4);
            nedsattningType.setVaraktighetFrom(source.getNedsattMed25().fromAsLocalDate());
            nedsattningType.setVaraktighetTom(source.getNedsattMed25().tomAsLocalDate());
            arbetsformagaType.getArbetsformagaNedsattning().add(nedsattningType);
        }
        if (source.getNedsattMed50() != null) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();
            nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_2);
            nedsattningType.setVaraktighetFrom(source.getNedsattMed50().fromAsLocalDate());
            nedsattningType.setVaraktighetTom(source.getNedsattMed50().tomAsLocalDate());
            arbetsformagaType.getArbetsformagaNedsattning().add(nedsattningType);
        }
        if (source.getNedsattMed75() != null) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();
            nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_3_4);
            nedsattningType.setVaraktighetFrom(source.getNedsattMed75().fromAsLocalDate());
            nedsattningType.setVaraktighetTom(source.getNedsattMed75().tomAsLocalDate());
            arbetsformagaType.getArbetsformagaNedsattning().add(nedsattningType);
        }
        if (source.getNedsattMed100() != null) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();
            nedsattningType.setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
            nedsattningType.setVaraktighetFrom(source.getNedsattMed100().fromAsLocalDate());
            nedsattningType.setVaraktighetTom(source.getNedsattMed100().tomAsLocalDate());
            arbetsformagaType.getArbetsformagaNedsattning().add(nedsattningType);
        }

        return arbetsformagaType;
    }

    private static List<SysselsattningType> convertSysselsattnings(Fk7263Utlatande source) {
        List<SysselsattningType> sysselsattningTypes = new ArrayList<>();

        if (source.isNuvarandeArbete()) {
            SysselsattningType sysselsattningType = new SysselsattningType();
            sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
            sysselsattningTypes.add(sysselsattningType);
        }

        if (source.isArbetsloshet()) {
            SysselsattningType sysselsattningType = new SysselsattningType();
            sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.ARBETSLOSHET);
            sysselsattningTypes.add(sysselsattningType);
        }

        if (source.isForaldrarledighet()) {
            SysselsattningType sysselsattningType = new SysselsattningType();
            sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.FORALDRALEDIGHET);
            sysselsattningTypes.add(sysselsattningType);
        }
        return sysselsattningTypes;
    }

    private static void convertVardkontakter(RegisterMedicalCertificateType register, Fk7263Utlatande source) {
        if (source == null) {
            return;
        }

        List<VardkontaktType> vardkontaktTypes = new ArrayList<>();
        if (source.getUndersokningAvPatienten() != null) {
            VardkontaktType vardkontaktType = new VardkontaktType();
            vardkontaktType.setVardkontaktstid(source.getUndersokningAvPatienten().asLocalDate());
            vardkontaktType.setVardkontakttyp(Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);
            vardkontaktTypes.add(vardkontaktType);
        }

        if (source.getTelefonkontaktMedPatienten() != null) {
            VardkontaktType vardkontaktType = new VardkontaktType();
            vardkontaktType.setVardkontaktstid(source.getTelefonkontaktMedPatienten().asLocalDate());
            vardkontaktType.setVardkontakttyp(Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);
            vardkontaktTypes.add(vardkontaktType);
        }
        if (!vardkontaktTypes.isEmpty()) {
            register.getLakarutlatande().getVardkontakt().addAll(vardkontaktTypes);
        }
    }

    private static void convertReferenser(RegisterMedicalCertificateType register, Fk7263Utlatande source) {
        if (source == null) {
            return;
        }

        List<ReferensType> referensTypes = new ArrayList<>();
        if (source.getJournaluppgifter() != null) {
            ReferensType referensType = new ReferensType();
            referensType.setDatum(source.getJournaluppgifter().asLocalDate());
            referensType.setReferenstyp(Referenstyp.JOURNALUPPGIFTER);
            referensTypes.add(referensType);
        }

        if (source.getAnnanReferens() != null) {
            ReferensType referensType = new ReferensType();
            referensType.setDatum(source.getAnnanReferens().asLocalDate());
            referensType.setReferenstyp(Referenstyp.ANNAT);
            referensTypes.add(referensType);
        }
        if (!referensTypes.isEmpty()) {
            register.getLakarutlatande().getReferens().addAll(referensTypes);
        }
    }

    /**
     * Build AktivitetTypes from internal model.
     *
     * @param source Utlatande
     * @return List of AktivitetType
     */
    private static void convertAktiviteter(RegisterMedicalCertificateType register, Fk7263Utlatande source) {
        if (source == null) {
            return;
        }

        List<AktivitetType> aktivitets = new ArrayList<>();

        if (source.isAvstangningSmittskydd()) {
            AktivitetType smittskydd = new AktivitetType();
            smittskydd.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
            aktivitets.add(smittskydd);
        }

        if (source.isRekommendationKontaktArbetsformedlingen()) {
            AktivitetType arbetsformedlingen = new AktivitetType();
            arbetsformedlingen.setAktivitetskod(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);
            aktivitets.add(arbetsformedlingen);
        }

        if (source.isRekommendationKontaktForetagshalsovarden()) {
            AktivitetType foretagshalsovarden = new AktivitetType();
            foretagshalsovarden.setAktivitetskod(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);
            aktivitets.add(foretagshalsovarden);
        }

        if (source.isRekommendationOvrigtCheck() || !Strings.isNullOrEmpty(source.getRekommendationOvrigt())) {
            AktivitetType ovrigt = new AktivitetType();
            ovrigt.setAktivitetskod(Aktivitetskod.OVRIGT);
            ovrigt.setBeskrivning(source.getRekommendationOvrigt());
            aktivitets.add(ovrigt);
        }

        if (source.getAtgardInomSjukvarden() != null) {
            AktivitetType atgard = new AktivitetType();
            atgard.setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
            atgard.setBeskrivning(source.getAtgardInomSjukvarden());
            aktivitets.add(atgard);
        }

        if (source.getAnnanAtgard() != null) {
            AktivitetType annanAtgard = new AktivitetType();
            annanAtgard.setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
            annanAtgard.setBeskrivning(source.getAnnanAtgard());
            aktivitets.add(annanAtgard);
        }

        if (source.getRehabilitering() != null) {
            AktivitetType rehab = new AktivitetType();
            switch (source.getRehabilitering()) {
                case rehabiliteringGarInteAttBedoma:
                    rehab.setAktivitetskod(Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
                    break;
                case rehabiliteringAktuell:
                    rehab.setAktivitetskod(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
                    break;
                case rehabiliteringEjAktuell:
                    rehab.setAktivitetskod(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL);
                    break;
            }
            aktivitets.add(rehab);
        }

        if (source.isRessattTillArbeteAktuellt()) {
            AktivitetType ressatt = new AktivitetType();
            ressatt.setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
            aktivitets.add(ressatt);
        }

        if (source.isRessattTillArbeteEjAktuellt()) {
            AktivitetType ressatt = new AktivitetType();
            ressatt.setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
            aktivitets.add(ressatt);
        }

        if (source.isKontaktMedFk()) {
            AktivitetType fk = new AktivitetType();
            fk.setAktivitetskod(Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL);
            aktivitets.add(fk);
        }

        if (!aktivitets.isEmpty()) {
            register.getLakarutlatande().getAktivitet().addAll(aktivitets);
        }
    }

    private static MedicinsktTillstandType toMedicinsktTillstand(String diagnoskod, String diagnosKodsystem) {
        MedicinsktTillstandType tillstand = new MedicinsktTillstandType();
        String codeSystem = diagnosKodsystem != null ? Diagnoskodverk.valueOf(diagnosKodsystem).getCodeSystem() : null;
        String codeSystemName = diagnosKodsystem != null ? Diagnoskodverk.valueOf(diagnosKodsystem).getCodeSystemName() : null;

        tillstand.setTillstandskod(createCD(diagnoskod, codeSystem, codeSystemName));
        return tillstand;
    }

    private static CD createCD(String code, String codeSystem, String codeSystemName) {
        CD cd = new CD();
        cd.setCode(code);
        if (codeSystem != null) {
            cd.setCodeSystem(codeSystem);
        }
        if (codeSystemName != null) {
            cd.setCodeSystemName(codeSystemName);
        } else {
            cd.setCodeSystemName(Diagnoskodverk.ICD_10_SE.getCodeSystemName());
        }
        return cd;
    }

    private static BedomtTillstandType sjukdomsforloppToJaxb(String source) {
        BedomtTillstandType tillstand = new BedomtTillstandType();
        tillstand.setBeskrivning(source);
        return tillstand;
    }

    private static EnhetType vardenhetToTransportJaxb(Vardenhet source) {
        if (source == null) {
            return null;
        }

        EnhetType enhet = new EnhetType();
        enhet.setEnhetsnamn(source.getEnhetsnamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setEpost(source.getEpost());

        II arbetsplatskod = new II();
        arbetsplatskod.setExtension(source.getArbetsplatsKod());
        arbetsplatskod.setRoot(Constants.ARBETSPLATS_KOD_OID);
        enhet.setArbetsplatskod(arbetsplatskod);

        II id = new II();
        id.setExtension(source.getEnhetsid());
        id.setRoot(Constants.HSA_ID_OID);
        enhet.setEnhetsId(id);

        enhet.setVardgivare(vardgivareToJaxb(source.getVardgivare()));
        return enhet;
    }

    private static VardgivareType vardgivareToJaxb(Vardgivare source) {
        if (source == null) {
            return null;
        }

        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        II id = new II();
        id.setExtension(source.getVardgivarid());
        id.setRoot(Constants.HSA_ID_OID);
        vardgivare.setVardgivareId(id);
        return vardgivare;
    }

    private static HosPersonalType hosPersonalToJaxb(HoSPersonal skapadAv) {
        HosPersonalType personal = new HosPersonalType();
        personal.setForskrivarkod(skapadAv.getForskrivarKod());
        personal.setFullstandigtNamn(skapadAv.getFullstandigtNamn());
        II id = new II();
        id.setExtension(skapadAv.getPersonId());
        id.setRoot(Constants.HSA_ID_OID);
        personal.setPersonalId(id);
        personal.setEnhet(vardenhetToTransportJaxb(skapadAv.getVardenhet()));

        return personal;
    }

    private static PatientType patientToJaxb(Patient source) {
        II id = new II();
        id.setRoot(SamordningsnummerValidator.isSamordningsNummer(Optional.of(source.getPersonId()))
            ? Constants.SAMORDNING_ID_OID
            : Constants.PERSON_ID_OID);
        id.setExtension(source.getPersonId().getPersonnummerWithDash());

        PatientType patientType = new PatientType();
        patientType.setPersonId(id);

        return patientType;
    }
}
