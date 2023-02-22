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

import se.inera.intyg.common.lisjp.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryAtgarder;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryBedomning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryDiagnos;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryFunktionsnedsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryGrundForMU;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryKontakt;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryMedicinskaBehandlingar;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategoryOvrigt;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategorySmittbararpenning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.category.CategorySysselsattning;
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
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionPrognosTimePeriod;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionSysselsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionSysselsattningYrke;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;

public final class InternalToCertificate {

    private InternalToCertificate() {

    }

    public static Certificate convert(LisjpUtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        var index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
            .addElement(CategorySmittbararpenning.toCertificate(index++, texts))
            .addElement(QuestionAvstangningSmittskydd.toCertificate(internalCertificate.getAvstangningSmittskydd(), index++, texts))
            .addElement(CategoryGrundForMU.toCertificate(index++, texts))
            .addElement(QuestionIntygetBaseratPa.toCertificate(internalCertificate, index++, texts))
            .addElement(
                QuestionAnnatGrundForMUBeskrivning.toCertificate(internalCertificate.getAnnatGrundForMUBeskrivning(), index++, texts))
            .addElement(
                QuestionMotiveringEjUndersokning.toCertificate(internalCertificate.getMotiveringTillInteBaseratPaUndersokning(), index++))
            .addElement(CategorySysselsattning.toCertificate(index++, texts))
            .addElement(QuestionSysselsattning.toCertificate(internalCertificate.getSysselsattning(), index++, texts))
            .addElement(QuestionSysselsattningYrke.toCertificate(internalCertificate.getNuvarandeArbete(), index++, texts))
            .addElement(CategoryDiagnos.toCertificate(index++, texts))
            .addElement(QuestionDiagnoser.toCertificate(internalCertificate.getDiagnoser(), index++, texts))
            .addElement(CategoryFunktionsnedsattning.toCertificate(index++, texts))
            .addElement(QuestionFunktionsnedsattning.toCertificate(internalCertificate.getFunktionsnedsattning(),
                internalCertificate.getFunktionsKategorier(), index++, texts))
            .addElement(QuestionAktivitetsbegransningar.toCertificate(internalCertificate.getAktivitetsbegransning(),
                internalCertificate.getAktivitetsKategorier(), index++, texts))
            .addElement(CategoryMedicinskaBehandlingar.toCertificate(index++, texts))
            .addElement(QuestionPagaendeBehandling.toCertificate(internalCertificate.getPagaendeBehandling(), index++, texts))
            .addElement(QuestionPlaneradBehandling.toCertificate(internalCertificate.getPlaneradBehandling(), index++, texts))
            .addElement(CategoryBedomning.toCertificate(index++, texts))
            .addElement(QuestionBehovAvSjukskrivning.toCertificate(internalCertificate.getSjukskrivningar(), index++, texts,
                internalCertificate.getGrundData().getRelation()))
            .addElement(
                QuestionMotiveringTidigtStartdatum.toCertificate(internalCertificate.getMotiveringTillTidigtStartdatumForSjukskrivning(),
                    index++))
            .addElement(
                QuestionForsakringsmedicinsktBeslutsstod.toCertificate(internalCertificate.getForsakringsmedicinsktBeslutsstod(), index++,
                    texts))
            .addElement(QuestionArbetstidsforlaggning.toCertificate(internalCertificate.getArbetstidsforlaggning(), index++, texts))
            .addElement(
                QuestionMotiveringArbetstidsforlaggning.toCertificate(internalCertificate.getArbetstidsforlaggningMotivering(), index++,
                    texts))
            .addElement(QuestionArbetsresor.toCertificate(internalCertificate.getArbetsresor(), index++, texts))
            .addElement(QuestionPrognos.toCertificate(internalCertificate.getPrognos(), index++, texts))
            .addElement(QuestionPrognosTimePeriod.toCertificate(internalCertificate.getPrognos(), index++, texts))
            .addElement(CategoryAtgarder.toCertificate(index++, texts))
            .addElement(QuestionAtgarder.toCertificate(internalCertificate.getArbetslivsinriktadeAtgarder(), index++, texts))
            .addElement(
                QuestionAtgarderBeskrivning.toCertificate(internalCertificate.getArbetslivsinriktadeAtgarderBeskrivning(), index++, texts))
            .addElement(CategoryOvrigt.toCertificate(index++, texts))
            .addElement(QuestionOvrigt.toCertificate(internalCertificate.getOvrigt(), index++, texts))
            .addElement(CategoryKontakt.toCertificate(index++, texts))
            .addElement(QuestionKontakt.toCertificate(internalCertificate.getKontaktMedFk(), index++, texts))
            .addElement(QuestionKontaktBeskrivning.toCertificate(internalCertificate.getAnledningTillKontakt(), index, texts))
            .build();
    }
}
