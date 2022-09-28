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

package se.inera.intyg.common.db.v1.model.converter.certificate;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DODSPLATS_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_QUESTION_DESCRIPTION_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;

import java.util.Arrays;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;

public class QuestionDodsplatsBoende {

    public static CertificateDataElement toCertificate(DodsplatsBoende dodsplatsBoende, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DODSPLATS_BOENDE_DELSVAR_ID)
            .parent(DODSDATUM_DODSPLATS_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(DODSPLATS_BOENDE_QUESTION_TEXT_ID))
                    .description(texts.get(DODSPLATS_BOENDE_QUESTION_DESCRIPTION_ID))
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id(DodsplatsBoende.SJUKHUS.name())
                                .label(DodsplatsBoende.SJUKHUS.getBeskrivning())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(DodsplatsBoende.ORDINART_BOENDE.name())
                                .label(DodsplatsBoende.ORDINART_BOENDE.getBeskrivning())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(DodsplatsBoende.SARSKILT_BOENDE.name())
                                .label(DodsplatsBoende.SARSKILT_BOENDE.getBeskrivning())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(DodsplatsBoende.ANNAN.name())
                                .label(DodsplatsBoende.ANNAN.getBeskrivning())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                dodsplatsBoende != null ? CertificateDataValueCode.builder()
                    .id(dodsplatsBoende.name())
                    .code(dodsplatsBoende.name())
                    .build() : CertificateDataValueCode.builder().build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DODSPLATS_BOENDE_DELSVAR_ID)
                        .expression(
                            multipleOrExpression(
                                singleExpression(DodsplatsBoende.SJUKHUS.name()),
                                singleExpression(DodsplatsBoende.ORDINART_BOENDE.name()),
                                singleExpression(DodsplatsBoende.SARSKILT_BOENDE.name()),
                                singleExpression(DodsplatsBoende.ANNAN.name())
                            )
                        )
                        .build()
                }
            )
            .build();
    }

    public static DodsplatsBoende toInternal(Certificate certificate) {
        final var codeValueString = codeValue(certificate.getData(), DODSPLATS_BOENDE_DELSVAR_ID);
        if (codeValueString == null) {
            return null;
        }
        return DodsplatsBoende.valueOf(codeValueString);
    }
}
