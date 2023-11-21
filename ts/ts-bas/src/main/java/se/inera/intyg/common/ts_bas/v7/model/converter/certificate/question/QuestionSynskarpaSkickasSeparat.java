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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_HOGER_OGA_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_VANSTER_OGA_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_JA_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_NEJ_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_HEADER_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARDEN_FOR_SYNSKARPA_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisable;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;

public class QuestionSynskarpaSkickasSeparat {

    public static CertificateDataElement toCertificate(Syn syn, int index, CertificateTextProvider textProvider) {
        final var synskarpaSkickaSeparat = syn != null ? syn.getSynskarpaSkickasSeparat() : null;
        return CertificateDataElement.builder()
            .id(SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID)
            .parent(SYNKARPA_SKICKAS_SEPARAT_HEADER_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(SYNKARPA_SKICKAS_SEPARAT_JSON_ID)
                    .label(textProvider.get(SYNKARPA_SKICKAS_SEPARAT_SVAR_TEXT_ID))
                    .selectedText(SVAR_JA_TEXT)
                    .unselectedText(SVAR_NEJ_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(SYNKARPA_SKICKAS_SEPARAT_JSON_ID)
                    .selected(synskarpaSkickaSeparat)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationDisable.builder()
                        .questionId(VARDEN_FOR_SYNSKARPA_ID)
                        .expression(
                            multipleOrExpressionWithExists(
                                VANSTER_OGA_UTAN_KORREKTION_JSON_ID, VANSTER_OGA_MED_KORREKTION_JSON_ID,
                                HOGER_OGA_UTAN_KORREKTION_JSON_ID, HOGER_OGA_MED_KORREKTION_JSON_ID,
                                BINOKULART_UTAN_KORREKTION_JSON_ID,
                                BINOKULART_MED_KORREKTION_JSON_ID))
                        .build(),
                    CertificateDataValidationDisable.builder()
                        .questionId(VARDEN_FOR_SYNSKARPA_ID)
                        .expression(
                            multipleOrExpression(
                                KONTAKTLINSER_VANSTER_OGA_JSON_ID,
                                KONTAKTLINSER_HOGER_OGA_DELSVAR_JSON_ID))
                        .build()
                }

            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID, SYNKARPA_SKICKAS_SEPARAT_JSON_ID);
    }

}
