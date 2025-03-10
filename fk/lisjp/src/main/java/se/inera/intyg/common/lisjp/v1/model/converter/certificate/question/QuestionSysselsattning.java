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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionSysselsattning extends AbstractQuestionSysselsattning {

    public static CertificateDataElement toCertificate(List<Sysselsattning> value, int index,
        CertificateTextProvider texts) {
        final var configProvider = new QuestionSyssesattningConfigProvider(
            convertValue(value)
        );
        return toCertificate(configProvider, TYP_AV_SYSSELSATTNING_SVAR_ID_28, SYSSELSATTNING_CATEGORY_ID, index, texts);
    }

    private static List<QuestionSysselsattningValue> convertValue(List<Sysselsattning> value) {
        if (value == null) {
            return Collections.emptyList();
        }
        return value.stream()
            .map(sysselsattning -> new QuestionSysselsattningValue(Objects.requireNonNull(sysselsattning.getTyp()).getId()))
            .collect(Collectors.toList());
    }

    public static List<Sysselsattning> toInternal(Certificate certificate) {
        return toInternal(certificate, TYP_AV_SYSSELSATTNING_SVAR_ID_28);
    }
}
