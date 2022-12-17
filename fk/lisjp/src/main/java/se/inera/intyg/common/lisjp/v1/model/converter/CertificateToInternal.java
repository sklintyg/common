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
package se.inera.intyg.common.lisjp.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_JSON_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateListValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateRangeListValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.grundData;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.icfCodeValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.icfTextValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

public final class CertificateToInternal {


    private CertificateToInternal() {
    }

    public static LisjpUtlatandeV1 convert(Certificate certificate, LisjpUtlatandeV1 internalCertificate,
        WebcertModuleService moduleService) {
        final var avstangningSmittskydd = getAvstangningSmittskydd(certificate);
        final var undersokningAvPatienten = getGrundForMUUndersokningAvPatienten(certificate);
        final var telefonkontakt = getGrundForMUTelefonkontakt(certificate);
        final var journaluppgifter = getGrundForMUJournaluppgifter(certificate);
        final var annat = getGrundForMUAnnat(certificate);
        final var annatGrundForMUBeskrivning = getAnnatGrundForMUBeskrivning(certificate);
        final var motiveringTillInteBaseratPaUndersokning = getMotiveringTillInteBaseratPaUndersokning(certificate);
        final var sysselsattning = getSysselsattning(certificate);
        final var nuvarandeArbete = getNuvarandeArbete(certificate);
        final var diagnos = getDiagnos(certificate, moduleService);
        final var funktionsnedsattning = getFunktionsnedsattning(certificate);
        final var funktionsnedsattningIcfKoder = getFunktionsnedsattningIcfKoder(certificate);
        final var aktivitetsbegransning = getAktivitetsbegransning(certificate);
        final var aktivitetsBegransningIcfKoder = getAktivitetsbegransningIcfKoder(certificate);
        final var pagaendeBehandling = getPagaendeBehandling(certificate);
        final var planeradBehandling = getPlaneradBehandling(certificate);
        final var motiveringTillTidigtStartdatum = getMotiveringTidigtStartdatum(certificate);
        final var forsakringsmedicinsktBeslutsstod = getForsakringsmedicinsktBeslutsstod(certificate);
        final var arbetstidsforlaggning = getArbetstidsforlaggning(certificate);
        final var arbetstidsforlaggningMotivering = getArbetstidsforlaggningMotivering(certificate);
        final var arbetsresor = getArbetsresor(certificate);
        final var prognos = getPrognos(certificate);
        final var atgarder = getAtgarder(certificate);
        final var atgarderBeskrivning = getAtgarderBeskrivning(certificate);
        final var ovrigt = getOvrigt(certificate);
        final var kontakt = getKontakt(certificate);
        final var kontaktBeskrivning = getKontaktBeskrivning(certificate);
        final var sjukskrivningar = getSjukskrivningar(certificate);
        final var grundData = getGrundData(certificate.getMetadata(), internalCertificate.getGrundData());

        return LisjpUtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(grundData)
            .setAvstangningSmittskydd(avstangningSmittskydd)
            .setUndersokningAvPatienten(undersokningAvPatienten)
            .setTelefonkontaktMedPatienten(telefonkontakt)
            .setJournaluppgifter(journaluppgifter)
            .setAnnatGrundForMU(annat)
            .setAnnatGrundForMUBeskrivning(annatGrundForMUBeskrivning)
            .setMotiveringTillInteBaseratPaUndersokning(motiveringTillInteBaseratPaUndersokning)
            .setSysselsattning(sysselsattning)
            .setNuvarandeArbete(nuvarandeArbete)
            .setDiagnoser(diagnos)
            .setFunktionsnedsattning(funktionsnedsattning)
            .setFunktionsKategorier(funktionsnedsattningIcfKoder)
            .setAktivitetsbegransning(aktivitetsbegransning)
            .setAktivitetsKategorier(aktivitetsBegransningIcfKoder)
            .setPagaendeBehandling(pagaendeBehandling)
            .setPlaneradBehandling(planeradBehandling)
            .setSjukskrivningar(sjukskrivningar)
            .setMotiveringTillTidigtStartdatumForSjukskrivning(motiveringTillTidigtStartdatum)
            .setForsakringsmedicinsktBeslutsstod(forsakringsmedicinsktBeslutsstod)
            .setArbetstidsforlaggning(arbetstidsforlaggning)
            .setArbetstidsforlaggningMotivering(arbetstidsforlaggningMotivering)
            .setArbetsresor(arbetsresor)
            .setPrognos(prognos)
            .setArbetslivsinriktadeAtgarder(atgarder)
            .setArbetslivsinriktadeAtgarderBeskrivning(atgarderBeskrivning)
            .setOvrigt(ovrigt)
            .setKontaktMedFk(kontakt)
            .setAnledningTillKontakt(kontaktBeskrivning)
            .build();
    }

    private static Boolean getAvstangningSmittskydd(Certificate certificate) {
        return booleanValue(certificate.getData(), AVSTANGNING_SMITTSKYDD_SVAR_ID_27, AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27);
    }

    private static InternalDate getGrundForMUUndersokningAvPatienten(Certificate certificate) {
        return getInternalDate(certificate, GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1,
            GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1);
    }

    private static InternalDate getGrundForMUTelefonkontakt(Certificate certificate) {
        return getInternalDate(certificate, GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1,
            GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1);
    }

    private static InternalDate getGrundForMUJournaluppgifter(Certificate certificate) {
        return getInternalDate(certificate, GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1,
            GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1);
    }

    private static InternalDate getGrundForMUAnnat(Certificate certificate) {
        return getInternalDate(certificate, GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1,
            GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1);
    }

    private static InternalDate getInternalDate(Certificate certificate, String questionId, String itemId) {
        final var dateValue = dateListValue(certificate.getData(), questionId, itemId);
        return dateValue != null ? new InternalDate(dateValue) : null;
    }

    private static String getAnnatGrundForMUBeskrivning(Certificate certificate) {
        return textValue(certificate.getData(), GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
            GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1);
    }

    private static String getMotiveringTillInteBaseratPaUndersokning(Certificate certificate) {
        return textValue(certificate.getData(), GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
            MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1);
    }

    private static List<Sysselsattning> getSysselsattning(Certificate certificate) {
        var codeList = codeListValue(certificate.getData(), TYP_AV_SYSSELSATTNING_SVAR_ID_28);
        return codeList
            .stream()
            .map(
                code -> Sysselsattning.create(Sysselsattning.SysselsattningsTyp.fromId(code.getId()))
            )
            .collect(Collectors.toList());
    }

    private static String getNuvarandeArbete(Certificate certificate) {
        return textValue(certificate.getData(), NUVARANDE_ARBETE_SVAR_ID_29, NUVARANDE_ARBETE_SVAR_JSON_ID_29);
    }

    private static List<Diagnos> getDiagnos(Certificate certificate, WebcertModuleService moduleService) {
        return QuestionDiagnoser.toInternal(certificate, moduleService);
    }

    private static String getFunktionsnedsattning(Certificate certificate) {
        return icfTextValue(certificate.getData(), FUNKTIONSNEDSATTNING_SVAR_ID_35, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35);
    }

    private static List<String> getFunktionsnedsattningIcfKoder(Certificate certificate) {
        return icfCodeValue(certificate.getData(), FUNKTIONSNEDSATTNING_SVAR_ID_35, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35);
    }

    private static String getAktivitetsbegransning(Certificate certificate) {
        return icfTextValue(certificate.getData(), AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17);
    }

    private static List<String> getAktivitetsbegransningIcfKoder(Certificate certificate) {
        return icfCodeValue(certificate.getData(), AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17);
    }

    private static String getPagaendeBehandling(Certificate certificate) {
        return textValue(certificate.getData(), PAGAENDEBEHANDLING_SVAR_ID_19, PAGAENDEBEHANDLING_SVAR_JSON_ID_19);
    }

    private static String getPlaneradBehandling(Certificate certificate) {
        return textValue(certificate.getData(), PLANERADBEHANDLING_SVAR_ID_20, PLANERADBEHANDLING_SVAR_JSON_ID_20);
    }

    private static List<Sjukskrivning> getSjukskrivningar(Certificate certificate) {
        var list = dateRangeListValue(certificate.getData(), BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
        return list.stream().map(
            item -> Sjukskrivning.create(
                SjukskrivningsGrad.fromId(item.getId()), new InternalLocalDateInterval(
                    new InternalDate(item.getFrom()), new InternalDate(item.getTo())
                )
            )
        ).collect(Collectors.toList());
    }

    private static String getMotiveringTidigtStartdatum(Certificate certificate) {
        return textValue(certificate.getData(), BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32,
            MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID);
    }

    private static String getForsakringsmedicinsktBeslutsstod(Certificate certificate) {
        return textValue(certificate.getData(), FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37,
            FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37);
    }

    private static Boolean getArbetstidsforlaggning(Certificate certificate) {
        return booleanValue(certificate.getData(), ARBETSTIDSFORLAGGNING_SVAR_ID_33, ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33);
    }

    private static String getArbetstidsforlaggningMotivering(Certificate certificate) {
        return textValue(certificate.getData(), ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33,
            ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33);
    }

    private static Boolean getArbetsresor(Certificate certificate) {
        return booleanValue(certificate.getData(), ARBETSRESOR_SVAR_ID_34, ARBETSRESOR_SVAR_JSON_ID_34);
    }

    private static Prognos getPrognos(Certificate certificate) {
        var codeType = codeValue(certificate.getData(), PROGNOS_SVAR_ID_39);
        var codeDays = codeValue(certificate.getData(), PROGNOS_BESKRIVNING_DELSVAR_ID_39);

        if (codeType == null && codeDays == null) {
            return null;
        }

        return Prognos.create(getPrognosType(codeType), getPrognosDays(codeDays));
    }

    private static PrognosTyp getPrognosType(String type) {
        return type != null ? PrognosTyp.fromId(type) : null;
    }

    private static PrognosDagarTillArbeteTyp getPrognosDays(String days) {
        return days != null && !days.isEmpty() ? PrognosDagarTillArbeteTyp.fromId(days) : null;
    }

    private static List<ArbetslivsinriktadeAtgarder> getAtgarder(Certificate certificate) {
        var codeList = codeListValue(certificate.getData(), ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
        return codeList
            .stream()
            .map(
                code -> ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.fromId(code.getId()))
            )
            .collect(Collectors.toList());
    }

    private static String getAtgarderBeskrivning(Certificate certificate) {
        return textValue(certificate.getData(), ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44,
            ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44);
    }

    private static String getOvrigt(Certificate certificate) {
        return textValue(certificate.getData(), OVRIGT_SVAR_ID_25, OVRIGT_SVAR_JSON_ID_25);
    }

    private static Boolean getKontakt(Certificate certificate) {
        return booleanValue(certificate.getData(), KONTAKT_ONSKAS_SVAR_ID_26, KONTAKT_ONSKAS_SVAR_JSON_ID_26);
    }

    private static String getKontaktBeskrivning(Certificate certificate) {
        return textValue(certificate.getData(), ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26, ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26);
    }

    private static GrundData getGrundData(CertificateMetadata metadata, GrundData grundData) {
        return grundData(metadata, grundData);
    }
}

