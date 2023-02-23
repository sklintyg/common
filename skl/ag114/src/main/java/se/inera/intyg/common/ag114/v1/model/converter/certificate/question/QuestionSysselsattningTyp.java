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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_SYSSELSATTNING_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_LABEL_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;

import java.util.List;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisable;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;

public class QuestionSysselsattningTyp {

    public static CertificateDataElement toCertificate(int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(TYP_AV_SYSSELSATTNING_SVAR_ID)
            .index(index)
            .parent(CATEGORY_SYSSELSATTNING_ID)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(textProvider.get(TYP_AV_SYSSELSATTNING_SVAR_TEXT_ID))
                    .list(
                        List.of(
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                                .label(textProvider.get(TYP_AV_SYSSELSATTNING_SVAR_LABEL_ID))
                                .build()
                        )
                    )
                    .build()

            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(
                        List.of(
                            CertificateDataValueCode.builder()
                                .id(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                                .code(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                                .build()
                        )
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationDisable.builder()
                        .questionId(TYP_AV_SYSSELSATTNING_SVAR_ID)
                        .expression(exists(SysselsattningsTyp.NUVARANDE_ARBETE.getId()))
                        .build()
                }
            )
            .build();
    }

    public static List<Sysselsattning> toInternal() {
        return List.of(
            Sysselsattning.create(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)
        );
    }
}
