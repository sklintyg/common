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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.lisjp.v1.model.converter.certificate.util.QuestionPrognosCommonProvider.getCodeOptionalDropdowns;
import static se.inera.intyg.common.lisjp.v1.model.converter.certificate.util.QuestionPrognosCommonProvider.getMandatoryValidation;

import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionPrognos extends AbstractQuestionPrognos {

    public static CertificateDataElement toCertificate(Prognos prognos, int index,
        CertificateTextProvider texts) {
        final var prognosConfigProvider = new QuestionPrognosConfigProvider(
            getCodeOptionalDropdowns(texts),
            getMandatoryValidation(),
            convertValue(prognos)
        );
        return toCertificate(prognosConfigProvider, PROGNOS_SVAR_ID_39, BEDOMNING_CATEGORY_ID, index, texts);
    }

    private static QuestionPrognosValue convertValue(Prognos prognos) {
        return (prognos != null && prognos.getTyp() != null) ? new QuestionPrognosValue(prognos.getTyp().getId()) : null;
    }

    public static Prognos toInternal(Certificate certificate) {
        final var toInternalValues = toInternal(certificate, PROGNOS_SVAR_ID_39, PROGNOS_BESKRIVNING_DELSVAR_ID_39);

        if (toInternalValues == null) {
            return null;
        }
        return Prognos.create(getPrognosType(toInternalValues[0]), getPrognosDays(toInternalValues[1]));
    }

    private static PrognosTyp getPrognosType(String type) {
        return type != null ? PrognosTyp.fromId(type) : null;
    }

    private static PrognosDagarTillArbeteTyp getPrognosDays(String days) {
        return days != null && !days.isEmpty() ? PrognosDagarTillArbeteTyp.fromId(days) : null;
    }
}
