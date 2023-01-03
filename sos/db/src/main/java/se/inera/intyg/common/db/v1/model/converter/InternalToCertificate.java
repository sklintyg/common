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
package se.inera.intyg.common.db.v1.model.converter;

import java.time.LocalDate;
import java.util.List;
import se.inera.intyg.common.db.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.db.v1.model.converter.certificate.category.CategoryExplosivtImplantat;
import se.inera.intyg.common.db.v1.model.converter.certificate.category.CategoryPolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.category.CategoryUndersokningYttre;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionExplosivtAvlagsnat;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionExplosivtImplantat;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionPolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionPrefillMessagePolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionPrintMessagePolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionUndersokningYttre;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionUndersokningsdatum;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.sos_parent.model.converter.certificate.category.CategoryBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.category.CategoryDodsdatumDodsplats;
import se.inera.intyg.common.sos_parent.model.converter.certificate.category.CategoryKompletterandePatientuppgifter;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionAntraffadDod;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionAutoFillMessageAfter28DaysBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionAutoFillMessageWithin28DaysBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsdatum;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsdatumSakert;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplats;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplatsBoende;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplatsKommun;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionIdentitetenStyrkt;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionOsakertDodsdatum;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;

public class InternalToCertificate {

    private InternalToCertificate() {

    }

    public static Certificate convert(DbUtlatandeV1 internalCertificate, CertificateTextProvider texts, List<String> municipalities) {
        int index = 0;

        return CertificateBuilder.create()
            .metadata(
                MetaDataGrundData.toCertificate(internalCertificate, texts)
            )
            .addElement(
                CategoryKompletterandePatientuppgifter.toCertificate(index++, texts)
            )
            .addElement(
                QuestionIdentitetenStyrkt.toCertificate(internalCertificate.getIdentitetStyrkt(), index++, texts)
            )
            .addElement(
                CategoryDodsdatumDodsplats.toCertificate(index++, texts)
            )
            .addElement(
                QuestionDodsdatumSakert.toCertificate(internalCertificate.getDodsdatumSakert(), index++, texts)
            )
            .addElement(
                QuestionDodsdatum.toCertificate(getDodsdatumValue(internalCertificate), index++, texts)
            )
            .addElement(
                QuestionOsakertDodsdatum.toCertificate(getDodsdatumOsakertValue(internalCertificate), index++, texts)
            )
            .addElement(
                QuestionAntraffadDod.toCertificate(toLocalDate(internalCertificate.getAntraffatDodDatum()), index++, texts)
            )
            .addElement(
                QuestionDodsplats.toCertificate(index++, texts)
            )
            .addElement(
                QuestionDodsplatsKommun.toCertificate(municipalities, internalCertificate.getDodsplatsKommun(), index++, texts)
            )
            .addElement(
                QuestionDodsplatsBoende.toCertificate(internalCertificate.getDodsplatsBoende(), index++, texts)
            )
            .addElement(
                CategoryBarn.toCertificate(index++, texts)
            )
            .addElement(
                QuestionBarn.toCertificate(internalCertificate.getGrundData().getPatient().getPersonId(),
                    internalCertificate.getBarn(), index++, texts)
            )
            .addElement(
                QuestionAutoFillMessageWithin28DaysBarn.toCertificate(internalCertificate.getGrundData().getPatient().getPersonId(),
                    index++, texts)
            )
            .addElement(
                QuestionAutoFillMessageAfter28DaysBarn.toCertificate(internalCertificate.getGrundData().getPatient().getPersonId(),
                    index++, texts)
            )
            .addElement(
                CategoryExplosivtImplantat.toCertificate(index++, texts)
            )
            .addElement(
                QuestionExplosivtImplantat.toCertificate(internalCertificate.getExplosivImplantat(), index++, texts)
            )
            .addElement(
                QuestionExplosivtAvlagsnat.toCertificate(internalCertificate.getExplosivAvlagsnat(), index++, texts)
            )
            .addElement(
                CategoryUndersokningYttre.toCertificate(index++, texts)
            )
            .addElement(
                QuestionUndersokningYttre.toCertificate(internalCertificate.getUndersokningYttre(), index++, texts)
            )
            .addElement(
                QuestionUndersokningsdatum.toCertificate(toLocalDate(internalCertificate.getUndersokningDatum()), index++, texts)
            )
            .addElement(
                CategoryPolisanmalan.toCertificate(index++, texts)
            )
            .addElement(
                QuestionPolisanmalan.toCertificate(internalCertificate.getPolisanmalan(), index++, texts)
            )
            .addElement(
                QuestionPrefillMessagePolisanmalan.toCertificate(index++, texts)
            )
            .addElement(
                QuestionPrintMessagePolisanmalan.toCertificate(internalCertificate.getPolisanmalan(), index++, texts)
            )
            .build();
    }

    private static LocalDate getDodsdatumValue(DbUtlatandeV1 internalCertificate) {
        if (isDodsdatumSakert(internalCertificate.getDodsdatumSakert())) {
            return toLocalDate(internalCertificate.getDodsdatum());
        }
        return null;
    }

    private static String getDodsdatumOsakertValue(DbUtlatandeV1 internalCertificate) {
        if (isDodsdatumSakert(internalCertificate.getDodsdatumSakert())) {
            return null;
        }
        return internalCertificate.getDodsdatum() != null ? internalCertificate.getDodsdatum().toString() : null;
    }

    private static Boolean isDodsdatumSakert(Boolean dodsdatumSakert) {
        return dodsdatumSakert != null && dodsdatumSakert;
    }

    private static LocalDate toLocalDate(InternalDate internalDate) {
        return (internalDate != null && internalDate.isValidDate()) ? internalDate.asLocalDate() : null;
    }
}
