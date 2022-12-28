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

package se.inera.intyg.common.fk7263.model.converter;

import se.inera.intyg.common.fk7263.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryAktivitetsbegransningar;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryArbetsformogaBedomning;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryArbetslivsinriktadRehabilitering;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryAvstangningSmittskydd;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryBehandlingEllerAtgard;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryDiagnos;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryFunktionsnedsattning;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryIntygetBaserasPa;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryPatientensArbetsformagaBedoms;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategoryRekommendationer;
import se.inera.intyg.common.fk7263.model.converter.certificate.category.CategorySjukdomforlopp;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionAktivitetsbegransningar;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionArbetslivsinriktadRehabilitering;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionAvstangningSmittskydd;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionBehandlingEllerAtgardAnnanAtgard;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionBehandlingEllerAtgardSjukvard;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionFortydligandeDiagnos;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionFunktionsnedsattning;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionHuvuddiagnoskod;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionIntygetBaserasPaAnnat;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionIntygetBaserasPaJournaluppgifter;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionIntygetBaserasPaTelefonkontakt;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionIntygetBaserasPaUndersokning;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionPatientensArbetsformogaBedoms;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionRekommendationerKontaktMedAf;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionRekommendationerKontaktMedFhv;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionRekommendationerOvrigt;
import se.inera.intyg.common.fk7263.model.converter.certificate.question.QuestionSjukdomsforlopp;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;


public class InternalToCertificate {

    public static Certificate convert(Fk7263Utlatande internalCertificate, CertificateMessagesProvider messagesProvider) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate))
            .addElement(
                CategoryAvstangningSmittskydd.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionAvstangningSmittskydd.toCertificate(internalCertificate.isAvstangningSmittskydd(), index++)
            )
            .addElement(
                CategoryDiagnos.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionHuvuddiagnoskod.toCertificate(internalCertificate.getDiagnosKod(), index++, messagesProvider)
            )
            .addElement(
                QuestionFortydligandeDiagnos.toCertificate(internalCertificate.getDiagnosBeskrivning(),
                    internalCertificate.getDiagnosBeskrivning1(), internalCertificate.getDiagnosBeskrivning2(),
                    internalCertificate.getDiagnosBeskrivning3(), index++,
                    messagesProvider)
            )
            .addElement(
                CategorySjukdomforlopp.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionSjukdomsforlopp.toCertificate(internalCertificate.getSjukdomsforlopp(), index++)
            )
            .addElement(
                CategoryFunktionsnedsattning.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionFunktionsnedsattning.toCertificate(internalCertificate.getFunktionsnedsattning(), index++)
            )
            .addElement(
                CategoryIntygetBaserasPa.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionIntygetBaserasPaUndersokning.toCertificate(internalCertificate.getUndersokningAvPatienten(), index++)
            )
            .addElement(
                QuestionIntygetBaserasPaTelefonkontakt.toCertificate(internalCertificate.getTelefonkontaktMedPatienten(), index++)
            )
            .addElement(
                QuestionIntygetBaserasPaJournaluppgifter.toCertificate(internalCertificate.getJournaluppgifter(), index++)
            )
            .addElement(
                QuestionIntygetBaserasPaAnnat.toCertificate(internalCertificate.getAnnanReferens(), index++)
            )
            .addElement(
                CategoryAktivitetsbegransningar.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionAktivitetsbegransningar.toCertificate(internalCertificate.getAktivitetsbegransning(), index++)
            )
            .addElement(
                CategoryRekommendationer.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionRekommendationerKontaktMedAf.toCertificate(internalCertificate.isRekommendationKontaktArbetsformedlingen(), index++,
                    messagesProvider)
            )
            .addElement(
                QuestionRekommendationerKontaktMedFhv.toCertificate(internalCertificate.isRekommendationKontaktForetagshalsovarden(),
                    index++, messagesProvider)
            )
            .addElement(
                QuestionRekommendationerOvrigt.toCertificate(internalCertificate.getRekommendationOvrigt(), index++, messagesProvider)
            )
            .addElement(
                CategoryBehandlingEllerAtgard.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionBehandlingEllerAtgardSjukvard.toCertificate(internalCertificate.getAtgardInomSjukvarden(), index++,
                    messagesProvider)
            )
            .addElement(
                QuestionBehandlingEllerAtgardAnnanAtgard.toCertificate(internalCertificate.getAnnanAtgard(), index++, messagesProvider)
            )
            .addElement(
                CategoryArbetslivsinriktadRehabilitering.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionArbetslivsinriktadRehabilitering.toCertificate(internalCertificate.getRehabilitering(), index++)
            )
            .addElement(
                CategoryPatientensArbetsformagaBedoms.toCertificate(index++, messagesProvider)
            )
            .addElement(
                QuestionPatientensArbetsformogaBedoms.toCertificate(internalCertificate.isArbetsloshet(),
                    internalCertificate.isForaldrarledighet(), internalCertificate.isNuvarandeArbete(),
                    internalCertificate.getNuvarandeArbetsuppgifter(), index++, messagesProvider)
            )
            .addElement(
                CategoryArbetsformogaBedomning.toCertificate(index++, messagesProvider)
            )
            .build();
    }
}
