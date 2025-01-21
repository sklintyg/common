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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_ANNAT_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_C1E_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_C1_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_CE_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_C_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_D1E_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_D1_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_DE_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_D_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_INTE_TA_STALLNING_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_TAXI_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_TEXT_ID;

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
import se.inera.intyg.common.ts_bas.v7.model.internal.Bedomning;
import se.inera.intyg.common.ts_bas.v7.model.internal.BedomningKorkortstyp;

public class QuestionBedomningKorkortsTyp {

    public static CertificateDataElement toCertificate(Bedomning bedomning, int index, CertificateTextProvider textProvider) {
        final var bedomningKorkortstyp = bedomning != null && bedomning.getKorkortstyp() != null ? bedomning.getKorkortstyp() : null;
        return CertificateDataElement.builder()
            .id(LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(textProvider.get(LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_TEXT_ID))
                    .layout(Layout.INLINE)
                    .list(
                        List.of(
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR1.name())
                                .label(textProvider.get(KORKORT_C1_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR2.name())
                                .label(textProvider.get(KORKORT_C1E_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR3.name())
                                .label(textProvider.get(KORKORT_C_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR4.name())
                                .label(textProvider.get(KORKORT_CE_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR5.name())
                                .label(textProvider.get(KORKORT_D1_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR6.name())
                                .label(textProvider.get(KORKORT_D1E_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR7.name())
                                .label(textProvider.get(KORKORT_D_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR8.name())
                                .label(textProvider.get(KORKORT_DE_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR9.name())
                                .label(textProvider.get(KORKORT_TAXI_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR10.name())
                                .label(textProvider.get(KORKORT_ANNAT_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(BedomningKorkortstyp.VAR11.name())
                                .label(textProvider.get(KORKORT_INTE_TA_STALLNING_LABEL_ID))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(
                        bedomningKorkortstyp != null ? bedomningKorkortstyp.stream()
                            .map(QuestionBedomningKorkortsTyp::getValueCode)
                            .collect(Collectors.toList()) : Collections.emptyList()
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID)
                        .expression(
                            multipleOrExpressionWithExists(
                                BedomningKorkortstyp.VAR1.name(),
                                BedomningKorkortstyp.VAR2.name(),
                                BedomningKorkortstyp.VAR3.name(),
                                BedomningKorkortstyp.VAR4.name(),
                                BedomningKorkortstyp.VAR5.name(),
                                BedomningKorkortstyp.VAR6.name(),
                                BedomningKorkortstyp.VAR7.name(),
                                BedomningKorkortstyp.VAR8.name(),
                                BedomningKorkortstyp.VAR9.name(),
                                BedomningKorkortstyp.VAR10.name(),
                                BedomningKorkortstyp.VAR11.name()
                            )
                        )
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
                        .questionId(LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID)
                        .expression(exists(BedomningKorkortstyp.VAR11.name()))
                        .id(
                            List.of(
                                BedomningKorkortstyp.VAR1.name(),
                                BedomningKorkortstyp.VAR2.name(),
                                BedomningKorkortstyp.VAR3.name(),
                                BedomningKorkortstyp.VAR4.name(),
                                BedomningKorkortstyp.VAR5.name(),
                                BedomningKorkortstyp.VAR6.name(),
                                BedomningKorkortstyp.VAR7.name(),
                                BedomningKorkortstyp.VAR8.name(),
                                BedomningKorkortstyp.VAR9.name(),
                                BedomningKorkortstyp.VAR10.name()
                            )
                        )
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
                        .questionId(LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID)
                        .expression(multipleOrExpressionWithExists(
                            BedomningKorkortstyp.VAR1.name(),
                            BedomningKorkortstyp.VAR2.name(),
                            BedomningKorkortstyp.VAR3.name(),
                            BedomningKorkortstyp.VAR4.name(),
                            BedomningKorkortstyp.VAR5.name(),
                            BedomningKorkortstyp.VAR6.name(),
                            BedomningKorkortstyp.VAR7.name(),
                            BedomningKorkortstyp.VAR8.name(),
                            BedomningKorkortstyp.VAR9.name(),
                            BedomningKorkortstyp.VAR10.name()))
                        .id(List.of(
                            BedomningKorkortstyp.VAR11.name()
                        ))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataValueCode getValueCode(BedomningKorkortstyp bedomningKorkortstyp) {
        return CertificateDataValueCode.builder()
            .id(bedomningKorkortstyp.name())
            .code(bedomningKorkortstyp.name())
            .build();
    }

    public static Set<BedomningKorkortstyp> toInternal(Certificate certificate) {
        final var certificateDataValueCodes = codeListValue(certificate.getData(), LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID);
        if (certificateDataValueCodes.isEmpty()) {
            return EnumSet.noneOf(BedomningKorkortstyp.class);
        }

        final var korkortsTyp = certificateDataValueCodes.stream()
            .map(QuestionBedomningKorkortsTyp::getKorkortsTyp)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        if (korkortsTyp.isEmpty()) {
            return EnumSet.noneOf(BedomningKorkortstyp.class);
        }
        return EnumSet.copyOf(korkortsTyp);
    }

    private static BedomningKorkortstyp getKorkortsTyp(CertificateDataValueCode certificateDataValueCode) {
        return certificateDataValueCode.getId() != null && !certificateDataValueCode.getId().isEmpty()
            ? BedomningKorkortstyp.valueOf(certificateDataValueCode.getId()) : null;
    }
}
