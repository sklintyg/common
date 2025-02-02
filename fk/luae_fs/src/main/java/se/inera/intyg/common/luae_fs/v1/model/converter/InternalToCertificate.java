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

import org.springframework.stereotype.Component;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.category.CategoryDiagnos;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.category.CategoryFunktionsnedsattning;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.category.CategoryGrundForMU;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.category.CategoryKontakt;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.category.CategoryOvrigt;
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
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;

@Component(value = "internalToCertificateFK7802")
public class InternalToCertificate {

    public Certificate convert(LuaefsUtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
            .addElement(CategoryGrundForMU.toCertificate(index++, texts))
            .addElement(QuestionUtlatandeBaseratPa.toCertificate(internalCertificate.getUndersokningAvPatienten(),
                internalCertificate.getJournaluppgifter(), internalCertificate.getAnhorigsBeskrivningAvPatienten(),
                internalCertificate.getAnnatGrundForMU(), index++, texts))
            .addElement(QuestionAnnatBeskrivning.toCertificate(internalCertificate.getAnnatGrundForMUBeskrivning(), index++, texts))
            .addElement(QuestionMotiveringTillInteBaseratPaUndersokning.toCertificate(
                internalCertificate.getMotiveringTillInteBaseratPaUndersokning(), index++, texts))
            .addElement(QuestionKannedomOmPatient.toCertificate(internalCertificate.getKannedomOmPatient(), index++, texts))
            .addElement(QuestionUnderlagFinns.toCertificate(internalCertificate.getUnderlagFinns(), index++, texts))
            .addElement(QuestionUnderlag.toCertificate(internalCertificate.getUnderlag(), index++, texts))
            .addElement(CategoryDiagnos.toCertificate(index++, texts))
            .addElement(QuestionDiagnoser.toCertificate(internalCertificate.getDiagnoser(), index++, texts))
            .addElement(CategoryFunktionsnedsattning.toCertificate(index++, texts))
            .addElement(QuestionFunktionsnedsattningDebut.toCertificate(internalCertificate.getFunktionsnedsattningDebut(), index++, texts))
            .addElement(QuestionFunktionsnedsattningPaverkan.toCertificate(internalCertificate.getFunktionsnedsattningPaverkan(), index++,
                texts))
            .addElement(CategoryOvrigt.toCertificate(index++, texts))
            .addElement(QuestionOvrigt.toCertificate(internalCertificate.getOvrigt(), index++, texts))
            .addElement(CategoryKontakt.toCertificate(index++, texts))
            .addElement(QuestionKontaktOnskas.toCertificate(internalCertificate.getKontaktMedFk(), index++, texts))
            .addElement(QuestionKontaktAnledning.toCertificate(internalCertificate.getAnledningTillKontakt(), index, texts))
            .build();
    }
}
