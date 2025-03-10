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

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV10_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV1_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV2_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV3_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV4_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV5_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV6_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV7_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV8_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV9_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_SVAR_TEXT_ID;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvser;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvserKategori;

public class QuestionIntygetAvser {

    public static CertificateDataElement toCertificate(IntygAvser intygAvser, int index,
        CertificateTextProvider texts) {
        final var intygetAvserKategorier = intygAvser != null && intygAvser.getKorkortstyp() != null ? intygAvser.getKorkortstyp() : null;
        return CertificateDataElement.builder()
            .id(INTYG_AVSER_SVAR_ID_1)
            .parent(INTYG_AVSER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(INTYG_AVSER_SVAR_TEXT_ID))
                    .description(texts.get(INTYG_AVSER_SVAR_DESCRIPTION_ID))
                    .layout(Layout.INLINE)
                    .list(
                        List.of(
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV1.name())
                                .label(texts.get(INTYG_AVSER_IAV1_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV2.name())
                                .label(texts.get(INTYG_AVSER_IAV2_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV3.name())
                                .label(texts.get(INTYG_AVSER_IAV3_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV4.name())
                                .label(texts.get(INTYG_AVSER_IAV4_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV5.name())
                                .label(texts.get(INTYG_AVSER_IAV5_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV6.name())
                                .label(texts.get(INTYG_AVSER_IAV6_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV7.name())
                                .label(texts.get(INTYG_AVSER_IAV7_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV8.name())
                                .label(texts.get(INTYG_AVSER_IAV8_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV9.name())
                                .label(texts.get(INTYG_AVSER_IAV9_LABEL_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(IntygAvserKategori.IAV10.name())
                                .label(texts.get(INTYG_AVSER_IAV10_LABEL_ID))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(
                        intygetAvserKategorier != null ? intygetAvserKategorier.stream()
                            .map(QuestionIntygetAvser::getValueCode)
                            .collect(Collectors.toList()) : Collections.emptyList()
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(INTYG_AVSER_SVAR_ID_1)
                        .expression(
                            multipleOrExpressionWithExists(IntygAvserKategori.IAV1.name(), IntygAvserKategori.IAV2.name(),
                                IntygAvserKategori.IAV3.name(), IntygAvserKategori.IAV4.name(), IntygAvserKategori.IAV5.name(),
                                IntygAvserKategori.IAV6.name(), IntygAvserKategori.IAV7.name(), IntygAvserKategori.IAV8.name(),
                                IntygAvserKategori.IAV9.name(), IntygAvserKategori.IAV10.name())
                        )
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataValueCode getValueCode(IntygAvserKategori intygAvserKategori) {
        return CertificateDataValueCode.builder()
            .id(intygAvserKategori.name())
            .code(intygAvserKategori.name())
            .build();
    }

    public static IntygAvser toInternal(Certificate certificate) {
        final var certificateDataValueCodes = codeListValue(certificate.getData(), INTYG_AVSER_SVAR_ID_1);
        if (certificateDataValueCodes.isEmpty()) {
            return IntygAvser.create(EnumSet.noneOf(IntygAvserKategori.class));
        }

        final var intygAvserKategoris = certificateDataValueCodes.stream()
            .map(QuestionIntygetAvser::getIntygAvserKategori)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        if (intygAvserKategoris.isEmpty()) {
            return IntygAvser.create(EnumSet.noneOf(IntygAvserKategori.class));
        }
        return IntygAvser.create(EnumSet.copyOf(intygAvserKategoris));
    }

    private static IntygAvserKategori getIntygAvserKategori(CertificateDataValueCode certificateDataValueCode) {
        return certificateDataValueCode.getId() != null && !certificateDataValueCode.getId().isEmpty()
            ? IntygAvserKategori.valueOf(certificateDataValueCode.getId()) : null;
    }
}
