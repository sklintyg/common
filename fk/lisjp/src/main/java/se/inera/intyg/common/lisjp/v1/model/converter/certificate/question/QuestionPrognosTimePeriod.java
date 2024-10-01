/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_ID_39;

import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionPrognosTimePeriod extends AbstractQuestionPrognosTimePeriod {

    public static CertificateDataElement toCertificate(Prognos prognos, int index,
        CertificateTextProvider texts) {
        final var timePeriodConfigProvider = new QuestionPrognosTimePeriodConfigProvider(
            convertValue(prognos)
        );
        return toCertificate(timePeriodConfigProvider, PROGNOS_BESKRIVNING_DELSVAR_ID_39, PROGNOS_SVAR_ID_39, index, texts);
    }

    private static QuestionPrognosTimePeriodValue convertValue(Prognos prognos) {
        return prognos != null && prognos.getDagarTillArbete() != null ? new QuestionPrognosTimePeriodValue(
            prognos.getDagarTillArbete().getId()) : null;
    }
}
