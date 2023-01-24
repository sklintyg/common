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

import org.springframework.stereotype.Component;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.MetaDataGrundData;

import se.inera.intyg.common.ag114.v1.model.converter.certificate.category.CategoryArbetsformaga;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.category.CategoryBedomning;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.category.CategoryDiagnos;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.category.CategoryGrundForMedicinsktUnderlag;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.category.CategoryKontakt;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.category.CategoryOvrigt;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.category.CategorySysselsattning;
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
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;

@Component(value = "internalToCertificateAg1-14")
public class InternalToCertificate {

    public Certificate convert(Ag114UtlatandeV1 internalCertificate, CertificateTextProvider textProvider) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, textProvider))
            .addElement(
                CategoryGrundForMedicinsktUnderlag.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionIntygetBaseratPa.toCertificate(
                    internalCertificate.getUndersokningAvPatienten(), internalCertificate.getTelefonkontaktMedPatienten(),
                    internalCertificate.getJournaluppgifter(), internalCertificate.getAnnatGrundForMU(), index++, textProvider
                )
            )
            .addElement(
                QuestionAnnatBeskrivning.toCertificate(internalCertificate.getAnnatGrundForMUBeskrivning(), index++, textProvider)
            )
            .addElement(
                CategorySysselsattning.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionSysselsattningTyp.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionNuvarandeArbete.toCertificate(internalCertificate.getNuvarandeArbete(), index++, textProvider)
            )
            .addElement(
                CategoryDiagnos.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionOnskaFormedlaDiagnos.toCertificate(internalCertificate.getOnskarFormedlaDiagnos(), index++, textProvider)
            )
            .addElement(
                QuestionDiagnos.toCertificate(internalCertificate.getDiagnoser(), index++, textProvider)
            )
            .addElement(
                CategoryArbetsformaga.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionNedsattArbetsformaga.toCertificate(internalCertificate.getNedsattArbetsformaga(), index++, textProvider)
            )
            .addElement(
                QuestionArbetsformagaTrotsSjukdom.toCertificate(internalCertificate.getArbetsformagaTrotsSjukdom(), index++, textProvider)
            )
            .addElement(
                QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate(internalCertificate.getArbetsformagaTrotsSjukdomBeskrivning(),
                    index++, textProvider)
            )
            .addElement(
                CategoryBedomning.toCertificate(index++, textProvider)
            )
            .addElement(
                CategoryOvrigt.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionOvrigaUpplysningar.toCertificate(internalCertificate.getOvrigaUpplysningar(), index++, textProvider)
            )
            .addElement(
                CategoryKontakt.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionKontaktMedArbetsgivaren.toCertificate(internalCertificate.getKontaktMedArbetsgivaren(), index++, textProvider)
            )
            .addElement(
                QuestionAnledningTillKontakt.toCertificate(internalCertificate.getAnledningTillKontakt(), index++, textProvider)
            )
            .build();
    }
}
