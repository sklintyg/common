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

package se.inera.intyg.common.ag7804.v1.model.converter.certificate.question;


import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_BEDOMNING;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateRangeListValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.getInternalLocalDateInterval;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.AbstractQuestionBehovAvSjukskrivning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.model.common.internal.Relation;

public class QuestionBehovAvSjukskrivning extends AbstractQuestionBehovAvSjukskrivning {

    public static CertificateDataElement toCertificate(List<Sjukskrivning> list, int index,
        CertificateTextProvider texts, Relation relation) {
        return toCertificate(
            new QuestionBehovAvSjukskrivningConfigProvider(
                relation != null ? relation.getRelationKod() : null,
                relation != null ? relation.getSistaGiltighetsDatum() : null,
                relation != null ? relation.getSistaSjukskrivningsgrad() : null,
                convertValues(list)),
            BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32,
            CATEGORY_BEDOMNING, index,
            texts, relation);
    }

    private static List<SjukskrivningValue> convertValues(List<Sjukskrivning> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
            .map(QuestionBehovAvSjukskrivning::toSjukskrivningsValue)
            .collect(Collectors.toList());
    }

    private static SjukskrivningValue toSjukskrivningsValue(Sjukskrivning sjukskrivning) {
        return new SjukskrivningValue(sjukskrivning.getPeriod(), Objects.requireNonNull(sjukskrivning.getSjukskrivningsgrad()).getId());
    }

    public static List<Sjukskrivning> toInternal(Certificate certificate) {
        var list = dateRangeListValue(certificate.getData(), BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

        return list.stream().map(
            QuestionBehovAvSjukskrivning::getSjukskrivning).collect(Collectors.toList());
    }

    private static Sjukskrivning getSjukskrivning(CertificateDataValueDateRange item) {
        return Sjukskrivning.create(SjukskrivningsGrad.fromId(item.getId()),
            getInternalLocalDateInterval(item));
    }
}
