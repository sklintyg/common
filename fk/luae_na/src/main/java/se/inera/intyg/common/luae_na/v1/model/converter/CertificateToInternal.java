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

package se.inera.intyg.common.luae_na.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import se.inera.intyg.common.luae_na.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionAktivitetsbegransningar;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnosForNyBedomning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnosgrund;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFormagaTrotsBegransning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionForslagTillAtgard;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningAnnan;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningBalansKoordination;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningIntellektuell;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningKommunikation;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningKoncentration;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningPsykisk;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningSynHorselTal;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionKannedomOmPatient;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionKontaktAnledning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionKontaktOnskas;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingAvslutadBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingPagaendeBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingPlaneradBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingSubstansintag;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskaForutsattningarForArbete;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMotiveringTillInteBaseratPaUndersokning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionNyBedomningDiagnosgrund;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionSjukdomsforlopp;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlag;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlagBaseratPa;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlagFinns;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;


public class CertificateToInternal {

    public static LuaenaUtlatandeV1 convert(Certificate certificate, LuaenaUtlatandeV1 internalCertificate,
        WebcertModuleService moduleService) {
        return LuaenaUtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
            .setUndersokningAvPatienten(QuestionUnderlagBaseratPa.toInternal(certificate,
                GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1))
            .setJournaluppgifter(QuestionUnderlagBaseratPa.toInternal(certificate,
                GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1))
            .setAnhorigsBeskrivningAvPatienten(QuestionUnderlagBaseratPa.toInternal(certificate,
                GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1))
            .setAnnatGrundForMU(
                QuestionUnderlagBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1))
            .setAnnatGrundForMUBeskrivning(QuestionAnnatBeskrivning.toInternal(certificate))
            .setMotiveringTillInteBaseratPaUndersokning(QuestionMotiveringTillInteBaseratPaUndersokning.toInternal(certificate))
            .setKannedomOmPatient(QuestionKannedomOmPatient.toInternal(certificate))
            .setUnderlagFinns(QuestionUnderlagFinns.toInternal(certificate))
            .setUnderlag(QuestionUnderlag.toInternal(certificate))
            .setSjukdomsforlopp(QuestionSjukdomsforlopp.toInternal(certificate))
            .setDiagnoser(QuestionDiagnoser.toInternal(certificate, moduleService))
            .setDiagnosgrund(QuestionDiagnosgrund.toInternal(certificate))
            .setNyBedomningDiagnosgrund(QuestionNyBedomningDiagnosgrund.toInternal(certificate))
            .setDiagnosForNyBedomning(QuestionDiagnosForNyBedomning.toInternal(certificate))
            .setFunktionsnedsattningIntellektuell(QuestionFunktionsnedsattningIntellektuell.toInternal(certificate))
            .setFunktionsnedsattningKommunikation(QuestionFunktionsnedsattningKommunikation.toInternal(certificate))
            .setFunktionsnedsattningKoncentration(QuestionFunktionsnedsattningKoncentration.toInternal(certificate))
            .setFunktionsnedsattningPsykisk(QuestionFunktionsnedsattningPsykisk.toInternal(certificate))
            .setFunktionsnedsattningSynHorselTal(QuestionFunktionsnedsattningSynHorselTal.toInternal(certificate))
            .setFunktionsnedsattningBalansKoordination(QuestionFunktionsnedsattningBalansKoordination.toInternal(certificate))
            .setFunktionsnedsattningAnnan(QuestionFunktionsnedsattningAnnan.toInternal(certificate))
            .setAktivitetsbegransning(QuestionAktivitetsbegransningar.toInternal(certificate))
            .setAvslutadBehandling(QuestionMedicinskBehandlingAvslutadBehandling.toInternal(certificate))
            .setPagaendeBehandling(QuestionMedicinskBehandlingPagaendeBehandling.toInternal(certificate))
            .setPlaneradBehandling(QuestionMedicinskBehandlingPlaneradBehandling.toInternal(certificate))
            .setSubstansintag(QuestionMedicinskBehandlingSubstansintag.toInternal(certificate))
            .setMedicinskaForutsattningarForArbete(QuestionMedicinskaForutsattningarForArbete.toInternal(certificate))
            .setFormagaTrotsBegransning(QuestionFormagaTrotsBegransning.toInternal(certificate))
            .setForslagTillAtgard(QuestionForslagTillAtgard.toInternal(certificate))
            .setOvrigt(QuestionOvrigt.toInternal(certificate))
            .setKontaktMedFk(QuestionKontaktOnskas.toInternal(certificate))
            .setAnledningTillKontakt(QuestionKontaktAnledning.toInternal(certificate))
            .build();
    }
}
