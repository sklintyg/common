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

package se.inera.intyg.common.db.v1.model.converter;

import java.time.LocalDate;
import se.inera.intyg.common.db.v1.model.converter.certificate.CategoryBarn;
import se.inera.intyg.common.db.v1.model.converter.certificate.CategoryDodsdatumDodsplats;
import se.inera.intyg.common.db.v1.model.converter.certificate.CategoryExplosivtImplantat;
import se.inera.intyg.common.db.v1.model.converter.certificate.CategoryKompletterandePatientuppgifter;
import se.inera.intyg.common.db.v1.model.converter.certificate.CategoryPolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.CategoryUndersokningYttre;
import se.inera.intyg.common.db.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionAntraffadDod;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionBarn;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionDodsdatum;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionDodsdatumSakert;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionDodsplatsBoende;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionDodsplatsKommun;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionExplosivtAvlagsnat;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionExplosivtImplantat;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionIdentitetenStyrkt;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionOsakertDodsdatum;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionPolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionPrefillMessagePolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionPrintMessagePolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionUndersokningYttre;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionUndersokningsdatum;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;

public class InternalToCertificate {

    private InternalToCertificate() {

    }

    public static Certificate convert(DbUtlatandeV1 internalCertificate, CertificateTextProvider texts) {
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
                QuestionDodsplatsKommun.toCertificate(internalCertificate.getDodsplatsKommun(), index++, texts)
            )
            .addElement(
                QuestionDodsplatsBoende.toCertificate(internalCertificate.getDodsplatsBoende(), index++, texts)
            )
            .addElement(
                CategoryBarn.toCertificate(index++, texts)
            )
            .addElement(
                QuestionBarn.toCertificate(internalCertificate.getBarn(), index++, texts)
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
                QuestionPrintMessagePolisanmalan.toCertificate(index++, texts)
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
