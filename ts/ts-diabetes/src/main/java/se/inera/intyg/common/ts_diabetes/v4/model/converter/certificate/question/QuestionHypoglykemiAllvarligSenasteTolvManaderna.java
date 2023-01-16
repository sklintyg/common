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
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SVAR_JA_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SVAR_NEJ_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;

public class QuestionHypoglykemiAllvarligSenasteTolvManaderna {

    public static CertificateDataElement toCertificate(Hypoglykemi hypoglykemi, int index, CertificateTextProvider textProvider) {
        final var allvarligSenasteTolvManaderna =
            hypoglykemi != null && hypoglykemi.getAllvarligSenasteTolvManaderna() != null
                ? hypoglykemi.getAllvarligSenasteTolvManaderna() : null;
        return CertificateDataElement.builder()
            .id(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID)
            .parent(HYPOGLYKEMI_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_DELSVAR_ID)
                    .text(textProvider.get(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TEXT_ID))
                    .selectedText(textProvider.get(SVAR_JA_TEXT_ID))
                    .unselectedText(textProvider.get(SVAR_NEJ_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID)
                    .selected(allvarligSenasteTolvManaderna)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(INTYG_AVSER_SVAR_ID)
                        .expression(multipleOrExpression(IntygAvserKategori.VAR1.name(), IntygAvserKategori.VAR2.name(),
                            IntygAvserKategori.VAR3.name(), IntygAvserKategori.VAR4.name(), IntygAvserKategori.VAR5.name(),
                            IntygAvserKategori.VAR6.name(), IntygAvserKategori.VAR7.name(), IntygAvserKategori.VAR8.name(),
                            IntygAvserKategori.VAR9.name())
                        )
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID)
                        .expression(singleExpression(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID,
            HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID);
    }
}
