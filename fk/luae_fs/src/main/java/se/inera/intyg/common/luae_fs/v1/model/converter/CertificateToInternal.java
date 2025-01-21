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
package se.inera.intyg.common.luae_fs.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionFunktionsnedsattningDebut;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionFunktionsnedsattningPaverkan;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionKannedomOmPatient;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionKontaktAnledning;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionKontaktOnskas;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionMotiveringTillInteBaseratPaUndersokning;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionUnderlag;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionUnderlagFinns;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionUtlatandeBaseratPa;
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

@Component(value = "certificateToInternalFK7802")
public class CertificateToInternal {

    private final WebcertModuleService webcertModuleService;

    public CertificateToInternal(@Autowired(required = false) WebcertModuleService webcertModuleService) {
        this.webcertModuleService = webcertModuleService;
    }

    public LuaefsUtlatandeV1 convert(Certificate certificate, LuaefsUtlatandeV1 internalCertificate) {
        return LuaefsUtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
            .setUndersokningAvPatienten(QuestionUtlatandeBaseratPa.toInternal(certificate,
                GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1))
            .setJournaluppgifter(QuestionUtlatandeBaseratPa.toInternal(certificate,
                GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1))
            .setAnhorigsBeskrivningAvPatienten(QuestionUtlatandeBaseratPa.toInternal(certificate,
                GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1))
            .setAnnatGrundForMU(
                QuestionUtlatandeBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1))
            .setAnnatGrundForMUBeskrivning(QuestionAnnatBeskrivning.toInternal(certificate))
            .setMotiveringTillInteBaseratPaUndersokning(QuestionMotiveringTillInteBaseratPaUndersokning.toInternal(certificate))
            .setKannedomOmPatient(QuestionKannedomOmPatient.toInternal(certificate))
            .setUnderlagFinns(QuestionUnderlagFinns.toInternal(certificate))
            .setUnderlag(QuestionUnderlag.toInternal(certificate))
            .setDiagnoser(QuestionDiagnoser.toInternal(certificate, webcertModuleService))
            .setFunktionsnedsattningDebut(QuestionFunktionsnedsattningDebut.toInternal(certificate))
            .setFunktionsnedsattningPaverkan(QuestionFunktionsnedsattningPaverkan.toInternal(certificate))
            .setOvrigt(QuestionOvrigt.toInternal(certificate))
            .setKontaktMedFk(QuestionKontaktOnskas.toInternal(certificate))
            .setAnledningTillKontakt(QuestionKontaktAnledning.toInternal(certificate))
            .build();
    }
}
