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

import se.inera.intyg.common.db.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionAntraffadDod;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionBarn;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionDodsdatum;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionDodsdatumSakert;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionDodsplatsBoende;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionDodsplatsKommun;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionExplosivtAvlagsnat;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionExplosivtImplantat;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionIdentitetenStyrkt;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionOsakertDodsdatum;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionPolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionUndersokningYttre;
import se.inera.intyg.common.db.v1.model.converter.certificate.question.QuestionUndersokningsdatum;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.support.facade.model.Certificate;

public class CertificateToInternal {

    public static DbUtlatandeV1 convert(Certificate certificate, DbUtlatandeV1 internalCertificate) {
        final var builder = DbUtlatandeV1.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
            .setIdentitetStyrkt(QuestionIdentitetenStyrkt.toInternal(certificate))
            .setAntraffatDodDatum(QuestionAntraffadDod.toInternal(certificate))
            .setDodsplatsKommun(QuestionDodsplatsKommun.toInternal(certificate))
            .setDodsplatsBoende(QuestionDodsplatsBoende.toInternal(certificate))
            .setBarn(QuestionBarn.toInternal(certificate))
            .setExplosivImplantat(QuestionExplosivtImplantat.toInternal(certificate))
            .setExplosivAvlagsnat(QuestionExplosivtAvlagsnat.toInternal(certificate))
            .setUndersokningYttre(QuestionUndersokningYttre.toInternal(certificate))
            .setUndersokningDatum(QuestionUndersokningsdatum.toInternal(certificate))
            .setPolisanmalan(QuestionPolisanmalan.toInternal(certificate));

        final var dodsdatumSakert = QuestionDodsdatumSakert.toInternal(certificate);
        builder.setDodsdatumSakert(dodsdatumSakert);
        if (dodsdatumSakert) {
            builder.setDodsdatum(QuestionDodsdatum.toInternal(certificate));
        } else {
            builder.setDodsdatum(QuestionOsakertDodsdatum.toInternal(certificate));
        }
        return builder.build();
    }
}
