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

package se.inera.intyg.common.luse.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.luse.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luse.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.luse.v1.model.converter.certificate.question.QuestionMotiveringTillInteBaseratPaUndersokning;
import se.inera.intyg.common.luse.v1.model.converter.certificate.question.QuestionUtlatandeBaseratPa;
import se.inera.intyg.common.luse.v1.model.internal.LuseUtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;

@Component(value = "certificateToInternalFk7800")
public class CertificateToInternal {


    public LuseUtlatandeV1 convert(Certificate certificate, LuseUtlatandeV1 internalCertificate) {
        return LuseUtlatandeV1.builder()
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
            .setAnnatGrundForMUBeskrivning(
                QuestionAnnatBeskrivning.toInternal(certificate)
            )
            .setMotiveringTillInteBaseratPaUndersokning(
                QuestionMotiveringTillInteBaseratPaUndersokning.toInternal(certificate)
            )
            .build();
    }
}
