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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.util;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_HALFTEN;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL;

import java.util.List;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.AbstractQuestionBehovAvSjukskrivning.QuestionBehovAvSjukskrivningConfigProvider;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.AbstractQuestionBehovAvSjukskrivning.SjukskrivningValue;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CheckboxDateRange;
import se.inera.intyg.common.support.model.common.internal.Relation;

public class QuestionBehovAvSjukskrivningProvider {

    public static QuestionBehovAvSjukskrivningConfigProvider getConfigProvider(List<SjukskrivningValue> list, CertificateTextProvider texts,
        Relation relation) {
        return new QuestionBehovAvSjukskrivningConfigProvider(
            getCheckboxMultipleCodes(texts),
            relation != null ? relation.getRelationKod().name() : null,
            relation != null ? relation.getSistaGiltighetsDatum() : null,
            relation != null ? relation.getSistaSjukskrivningsgrad() : null,
            getMandatoryValidation(),
            list
        );
    }

    private static List<CheckboxDateRange> getCheckboxMultipleCodes(CertificateTextProvider texts) {
        return List.of(
            CheckboxDateRange.builder()
                .id(SjukskrivningsGrad.NEDSATT_1_4.getId())
                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL))
                .build(),
            CheckboxDateRange.builder()
                .id(SjukskrivningsGrad.NEDSATT_HALFTEN.getId())
                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_HALFTEN))
                .build(),
            CheckboxDateRange.builder()
                .id(SjukskrivningsGrad.NEDSATT_3_4.getId())
                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL))
                .build(),
            CheckboxDateRange.builder()
                .id(SjukskrivningsGrad.HELT_NEDSATT.getId())
                .label(texts.get(BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT))
                .build()
        );
    }


    private static String[] getMandatoryValidation() {
        return new String[]{
            SjukskrivningsGrad.NEDSATT_1_4.getId(),
            SjukskrivningsGrad.NEDSATT_HALFTEN.getId(),
            SjukskrivningsGrad.NEDSATT_3_4.getId(),
            SjukskrivningsGrad.HELT_NEDSATT.getId()
        };
    }
}
