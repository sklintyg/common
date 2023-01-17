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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR11_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR12_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR13_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR14_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR15_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR16_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR17_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR18_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR1_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR2_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR3_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR4_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR5_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR6_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR7_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR8_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR9_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_TEXT_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisableSubElement;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.BedomningKorkortstyp;

public class QuestionBedomningUppfyllerBehorighetskrav {

    public static CertificateDataElement toCertificate(Bedomning bedomning, int index, CertificateTextProvider texts) {
        final var uppfyllerBehorighetskrav = bedomning != null && bedomning.getUppfyllerBehorighetskrav() != null
            ? bedomning.getUppfyllerBehorighetskrav() : null;
        return CertificateDataElement.builder()
            .id(BEDOMNING_SVAR_ID)
            .parent(BEDOMNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_TEXT_ID))
                    .description(texts.get(BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DESCRIPTION_ID))
                    .layout(Layout.INLINE)
                    .list(
                        List.of(
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR12.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR12_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR13.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR13_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR14.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR14_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR15.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR15_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR16.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR16_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR17.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR17_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR18.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR18_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR1.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR1_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR2.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR2_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR3.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR3_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR4.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR4_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR5.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR5_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR6.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR6_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR7.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR7_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR8.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR8_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR9.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR9_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR11.getType())
                                .label(texts.get(BEDOMNING_KORKORTSBEHORIGHET_VAR11_LABEL_ID))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(
                        uppfyllerBehorighetskrav != null ? uppfyllerBehorighetskrav.stream()
                            .map(QuestionBedomningUppfyllerBehorighetskrav::getValueCode)
                            .collect(Collectors.toList()) : Collections.emptyList()
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(BEDOMNING_SVAR_ID)
                        .expression(multipleOrExpression(getLicenceOptions(true).toArray(new String[0])))
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
                        .questionId(BEDOMNING_SVAR_ID)
                        .expression(multipleOrExpression(getLicenceOptions(false).toArray(new String[0])))
                        .id(List.of(BedomningKorkortstyp.VAR11.getType()))
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
                        .questionId(BEDOMNING_SVAR_ID)
                        .expression(singleExpression(BedomningKorkortstyp.VAR11.getType()))
                        .id(getLicenceOptions(false))
                        .build()
                }
            )
            .build();
    }

    private static List<String> getLicenceOptions(boolean includeUndecided) {
        final var licenseOptions = new ArrayList<>(List.of(BedomningKorkortstyp.VAR12.getType(), BedomningKorkortstyp.VAR13.getType(),
            BedomningKorkortstyp.VAR14.getType(), BedomningKorkortstyp.VAR15.getType(), BedomningKorkortstyp.VAR16.getType(),
            BedomningKorkortstyp.VAR17.getType(), BedomningKorkortstyp.VAR18.getType(), BedomningKorkortstyp.VAR1.getType(),
            BedomningKorkortstyp.VAR2.getType(), BedomningKorkortstyp.VAR3.getType(), BedomningKorkortstyp.VAR4.getType(),
            BedomningKorkortstyp.VAR5.getType(), BedomningKorkortstyp.VAR6.getType(), BedomningKorkortstyp.VAR7.getType(),
            BedomningKorkortstyp.VAR8.getType(), BedomningKorkortstyp.VAR9.getType()));

        if (includeUndecided) {
            licenseOptions.add(BedomningKorkortstyp.VAR11.getType());
        }
        return licenseOptions;
    }


    private static CertificateDataValueCode getValueCode(BedomningKorkortstyp bedomningKorkortstyp) {
        return CertificateDataValueCode.builder()
            .id(bedomningKorkortstyp.getType())
            .code(bedomningKorkortstyp.getType())
            .build();
    }

    public static Set<BedomningKorkortstyp> toInternal(Certificate certificate) {
        final var certificateDataValueCodes = codeListValue(certificate.getData(), BEDOMNING_SVAR_ID);
        if (certificateDataValueCodes.isEmpty()) {
            return EnumSet.noneOf(BedomningKorkortstyp.class);
        }

        final var uppfyllerKorkortsbehorigheter = certificateDataValueCodes.stream()
            .map(QuestionBedomningUppfyllerBehorighetskrav::getBedomningKorkortstyp)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        if (uppfyllerKorkortsbehorigheter.isEmpty()) {
            return EnumSet.noneOf(BedomningKorkortstyp.class);
        }
        return EnumSet.copyOf(uppfyllerKorkortsbehorigheter);
    }

    private static BedomningKorkortstyp getBedomningKorkortstyp(CertificateDataValueCode certificateDataValueCode) {
        return certificateDataValueCode.getId() != null && !certificateDataValueCode.getId().isEmpty()
            ? BedomningKorkortstyp.fromCode(certificateDataValueCode.getId()) : null;
    }
}
