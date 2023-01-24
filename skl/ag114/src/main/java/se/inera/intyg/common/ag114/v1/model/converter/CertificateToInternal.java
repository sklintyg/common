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

package se.inera.intyg.common.ag114.v1.model.converter;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionAnledningTillKontakt;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionArbetsformagaTrotsSjukdom;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionArbetsformagaTrotsSjukdomBeskrivning;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionDiagnos;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionIntygetBaseratPa;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionKontaktMedArbetsgivaren;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionNedsattArbetsformaga;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionNuvarandeArbete;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionOnskaFormedlaDiagnos;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionOvrigaUpplysningar;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionSysselsattningTyp;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;


@Component(value = "certificateToInternalAg1-14")
public class CertificateToInternal {

    private final WebcertModuleService webcertModuleService;

    public CertificateToInternal(@Autowired(required = false) WebcertModuleService webcertModuleService) {
        this.webcertModuleService = webcertModuleService;
    }

    public Ag114UtlatandeV1 convert(Certificate certificate, Ag114UtlatandeV1 internalCertificate) {
        return
            Ag114UtlatandeV1.builder()
                .setId(internalCertificate.getId())
                .setTextVersion(internalCertificate.getTextVersion())
                .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
                .setUndersokningAvPatienten(
                    QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1))
                .setTelefonkontaktMedPatienten(
                    QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                )
                .setJournaluppgifter(
                    QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                )
                .setAnnatGrundForMU(
                    QuestionIntygetBaseratPa.toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                )
                .setAnnatGrundForMUBeskrivning(
                    QuestionAnnatBeskrivning.toInternal(certificate)
                )
                .setSysselsattning(
                    QuestionSysselsattningTyp.toInternal()
                )
                .setNuvarandeArbete(
                    QuestionNuvarandeArbete.toInternal(certificate)
                )
                .setOnskarFormedlaDiagnos(
                    QuestionOnskaFormedlaDiagnos.toInternal(certificate)
                )
                .setDiagnoser(
                    QuestionDiagnos.toInternal(certificate, webcertModuleService)
                )
                .setNedsattArbetsformaga(
                    QuestionNedsattArbetsformaga.toInternal(certificate)
                )
                .setArbetsformagaTrotsSjukdom(
                    QuestionArbetsformagaTrotsSjukdom.toInternal(certificate)
                )
                .setArbetsformagaTrotsSjukdomBeskrivning(
                    QuestionArbetsformagaTrotsSjukdomBeskrivning.toInternal(certificate)
                )
                .setOvrigaUpplysningar(
                    QuestionOvrigaUpplysningar.toInternal(certificate)
                )
                .setKontaktMedArbetsgivaren(
                    QuestionKontaktMedArbetsgivaren.toInternal(certificate)
                )
                .setAnledningTillKontakt(
                    QuestionAnledningTillKontakt.toInternal(certificate)
                )
                .build();
    }
}
