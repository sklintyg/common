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
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.icfCodeValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.icfTextValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAnnatGrundForMUBeskrivning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAvstangningSmittskydd;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionBehovAvSjukskrivning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionForsakringsmedicinsktBeslutsstod;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionFunktionsnedsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionIntygetBaseratPa;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionMotiveringEjUndersokning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionMotiveringTidigtStartdatum;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionPagaendeBehandling;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionPlaneradBehandling;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionSysselsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionSysselsattningYrke;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

public final class CertificateToInternal {


    private CertificateToInternal() {
    }

    public static LisjpUtlatandeV1 convert(Certificate certificate, LisjpUtlatandeV1 internalCertificate,
        WebcertModuleService moduleService) {
        final var telefonkontakt = getGrundForMUTelefonkontakt(certificate);
        final var journaluppgifter = getGrundForMUJournaluppgifter(certificate);
        final var annat = getGrundForMUAnnat(certificate);
        final var motiveringTillInteBaseratPaUndersokning = getMotiveringTillInteBaseratPaUndersokning(certificate);
        final var funktionsnedsattningIcfKoder = getFunktionsnedsattningIcfKoder(certificate);
        final var aktivitetsbegransning = getAktivitetsbegransning(certificate);
        final var aktivitetsBegransningIcfKoder = getAktivitetsbegransningIcfKoder(certificate);
        final var arbetstidsforlaggning = getArbetstidsforlaggning(certificate);
        final var arbetstidsforlaggningMotivering = getArbetstidsforlaggningMotivering(certificate);
        final var arbetsresor = getArbetsresor(certificate);
        final var prognos = getPrognos(certificate);
        final var atgarder = getAtgarder(certificate);
        final var atgarderBeskrivning = getAtgarderBeskrivning(certificate);
        final var ovrigt = getOvrigt(certificate);
        final var kontakt = getKontakt(certificate);
        final var kontaktBeskrivning = getKontaktBeskrivning(certificate);

        return LisjpUtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
            .setAvstangningSmittskydd(QuestionAvstangningSmittskydd.toInternal(certificate))
            .setAnnatGrundForMUBeskrivning(QuestionAnnatGrundForMUBeskrivning.toInternal(certificate))
            .setUndersokningAvPatienten(QuestionMotiveringEjUndersokning.toInternal(certificate))
            .setSysselsattning(QuestionSysselsattning.toInternal(certificate))
            .setNuvarandeArbete(QuestionSysselsattningYrke.toInternal(certificate))
            .setDiagnoser(QuestionDiagnoser.toInternal(certificate, moduleService))
            .setFunktionsnedsattning(QuestionFunktionsnedsattning.toInternal(certificate))
            .setPagaendeBehandling(QuestionPagaendeBehandling.toInternal(certificate))
            .setPlaneradBehandling(QuestionPlaneradBehandling.toInternal(certificate))
            .setSjukskrivningar(QuestionBehovAvSjukskrivning.toInternal(certificate))
            .setMotiveringTillTidigtStartdatumForSjukskrivning(QuestionMotiveringTidigtStartdatum.toInternal(certificate))
            .setForsakringsmedicinsktBeslutsstod(QuestionForsakringsmedicinsktBeslutsstod.toInternal(certificate))
            .setTelefonkontaktMedPatienten(telefonkontakt)
            .setJournaluppgifter(journaluppgifter)
            .setAnnatGrundForMU(annat)
            .setMotiveringTillInteBaseratPaUndersokning(motiveringTillInteBaseratPaUndersokning)
            .setFunktionsKategorier(funktionsnedsattningIcfKoder)
            .setAktivitetsbegransning(aktivitetsbegransning)
            .setAktivitetsKategorier(aktivitetsBegransningIcfKoder)
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

    private static InternalDate getGrundForMUTelefonkontakt(Certificate certificate) {
        return QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1);
    }

    private static InternalDate getGrundForMUJournaluppgifter(Certificate certificate) {
        return QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1);
    }

    private static InternalDate getGrundForMUAnnat(Certificate certificate) {
        return QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1);
    }

    private static String getMotiveringTillInteBaseratPaUndersokning(Certificate certificate) {
        return textValue(certificate.getData(), GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
            MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1);
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
}

