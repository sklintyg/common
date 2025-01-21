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
package se.inera.intyg.common.db.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;

import java.util.Arrays;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;

public class QuestionUndersokningYttre {

    public static CertificateDataElement toCertificate(Undersokning undersokning, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(UNDERSOKNING_YTTRE_DELSVAR_ID)
            .parent(UNDERSOKNING_YTTRE_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(UNDERSOKNING_YTTRE_QUESTION_TEXT_ID))
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id(Undersokning.JA.name())
                                .label(Undersokning.JA.getBeskrivning())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(Undersokning.UNDERSOKNING_SKA_GORAS.name())
                                .label(Undersokning.UNDERSOKNING_SKA_GORAS.getBeskrivning())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN.name())
                                .label(Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN.getBeskrivning())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                undersokning != null ? CertificateDataValueCode.builder()
                    .id(undersokning.name())
                    .code(undersokning.name())
                    .build() : CertificateDataValueCode.builder().build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UNDERSOKNING_YTTRE_DELSVAR_ID)
                        .expression(
                            multipleOrExpressionWithExists(
                                Undersokning.JA.name(),
                                Undersokning.UNDERSOKNING_SKA_GORAS.name(),
                                Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN.name()
                            )
                        )
                        .build()
                }
            )
            .build();
    }

    public static Undersokning toInternal(Certificate certificate) {
        final var codeValueString = codeValue(certificate.getData(), UNDERSOKNING_YTTRE_DELSVAR_ID);
        if (codeValueString == null) {
            return null;
        }
        return Undersokning.valueOf(codeValueString);
    }
}
