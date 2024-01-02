/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import se.inera.intyg.common.lisjp.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAktivitetsbegransningar;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAnnatGrundForMUBeskrivning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionArbetsresor;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionArbetstidsforlaggning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAtgarder;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAtgarderBeskrivning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionAvstangningSmittskydd;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionBehovAvSjukskrivning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionForsakringsmedicinsktBeslutsstod;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionFunktionsnedsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionIntygetBaseratPa;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionKontakt;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionKontaktBeskrivning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionMotiveringArbetstidsforlaggning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionMotiveringEjUndersokning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionMotiveringTidigtStartdatum;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionPagaendeBehandling;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionPlaneradBehandling;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionPrognos;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionSysselsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionSysselsattningYrke;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

public final class CertificateToInternal {


    private CertificateToInternal() {
    }

    public static LisjpUtlatandeV1 convert(Certificate certificate, LisjpUtlatandeV1 internalCertificate,
        WebcertModuleService moduleService) {
        return LisjpUtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
            .setAvstangningSmittskydd(QuestionAvstangningSmittskydd.toInternal(certificate))
            .setAnnatGrundForMUBeskrivning(QuestionAnnatGrundForMUBeskrivning.toInternal(certificate))
            .setSysselsattning(QuestionSysselsattning.toInternal(certificate))
            .setNuvarandeArbete(QuestionSysselsattningYrke.toInternal(certificate))
            .setDiagnoser(QuestionDiagnoser.toInternal(certificate, moduleService))
            .setFunktionsnedsattning(QuestionFunktionsnedsattning.toInternalTextValue(certificate))
            .setPagaendeBehandling(QuestionPagaendeBehandling.toInternal(certificate))
            .setPlaneradBehandling(QuestionPlaneradBehandling.toInternal(certificate))
            .setSjukskrivningar(QuestionBehovAvSjukskrivning.toInternal(certificate))
            .setMotiveringTillTidigtStartdatumForSjukskrivning(QuestionMotiveringTidigtStartdatum.toInternal(certificate))
            .setForsakringsmedicinsktBeslutsstod(QuestionForsakringsmedicinsktBeslutsstod.toInternal(certificate))
            .setArbetstidsforlaggning(QuestionArbetstidsforlaggning.toInternal(certificate))
            .setArbetstidsforlaggningMotivering(QuestionMotiveringArbetstidsforlaggning.toInternal(certificate))
            .setArbetsresor(QuestionArbetsresor.toInternal(certificate))
            .setPrognos(QuestionPrognos.toInternal(certificate))
            .setArbetslivsinriktadeAtgarder(QuestionAtgarder.toInternal(certificate))
            .setArbetslivsinriktadeAtgarderBeskrivning(QuestionAtgarderBeskrivning.toInternal(certificate))
            .setOvrigt(QuestionOvrigt.toInternal(certificate))
            .setKontaktMedFk(QuestionKontakt.toInternal(certificate))
            .setAnledningTillKontakt(QuestionKontaktBeskrivning.toInternal(certificate))
            .setAktivitetsbegransning(QuestionAktivitetsbegransningar.toInternalTextValue(certificate))
            .setMotiveringTillInteBaseratPaUndersokning(QuestionMotiveringEjUndersokning.toInternal(certificate))
            .setUndersokningAvPatienten(QuestionIntygetBaseratPa.toInternal(certificate,
                GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1))
            .setTelefonkontaktMedPatienten(
                QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1))
            .setJournaluppgifter(
                QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1))
            .setAnnatGrundForMU(QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1))
            .setFunktionsKategorier(QuestionFunktionsnedsattning.toInternalCodeValue(certificate))
            .setAktivitetsKategorier(QuestionAktivitetsbegransningar.toInternalCodeValue(certificate))
            .build();
    }
}

