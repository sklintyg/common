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
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_FORETAG_ELLER_TJANSTEKORT_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_FORSAKRAN_KAP18_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_ID_KORT_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_KORKORT_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_PASS_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_PERS_KANNEDOM_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID_2;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_TEXT_ID;

import java.util.Arrays;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvIdKontroll;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;

public class QuestionIdentitetStyrktGenom {

    public static CertificateDataElement toCertificate(IdKontroll idKontroll, int index, CertificateTextProvider textProvider) {
        final var identitetStyrktGenom = idKontroll != null && idKontroll.getTyp() != null ? idKontroll.getTyp() : null;
        return CertificateDataElement.builder()
            .id(IDENTITET_STYRKT_GENOM_SVAR_ID_2)
            .index(index)
            .parent(IDENTITET_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(textProvider.get(IDENTITET_STYRKT_GENOM_TEXT_ID))
                    .description(textProvider.get(IDENTITET_STYRKT_GENOM_DESCRIPTION_ID))
                    .layout(Layout.COLUMNS)
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id(IdKontrollKod.ID_KORT.getCode())
                                .label(textProvider.get(IDENTITET_STYRKT_GENOM_ID_KORT_TEXT_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(IdKontrollKod.KORKORT.getCode())
                                .label(textProvider.get(IDENTITET_STYRKT_GENOM_KORKORT_TEXT_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(IdKontrollKod.FORSAKRAN_KAP18.getCode())
                                .label(textProvider.get(IDENTITET_STYRKT_GENOM_FORSAKRAN_KAP18_TEXT_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getCode())
                                .label(textProvider.get(IDENTITET_STYRKT_GENOM_FORETAG_ELLER_TJANSTEKORT_TEXT_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(IdKontrollKod.PERS_KANNEDOM.getCode())
                                .label(textProvider.get(IDENTITET_STYRKT_GENOM_PERS_KANNEDOM_TEXT_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(IdKontrollKod.PASS.getCode())
                                .label(textProvider.get(IDENTITET_STYRKT_GENOM_PASS_TEXT_ID))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                identitetStyrktGenom != null ? CertificateDataValueCode.builder()
                    .id(identitetStyrktGenom.getCode())
                    .code(identitetStyrktGenom.getCode())
                    .build() : CertificateDataValueCode.builder().build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(IDENTITET_STYRKT_GENOM_SVAR_ID_2)
                        .expression(
                            multipleOrExpression(
                                IdKontrollKod.ID_KORT.getCode(),
                                IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getCode(),
                                IdKontrollKod.KORKORT.getCode(),
                                IdKontrollKod.PERS_KANNEDOM.getCode(),
                                IdKontrollKod.FORSAKRAN_KAP18.getCode(),
                                IdKontrollKod.PASS.getCode()
                            )
                        )
                        .build()
                }
            )
            .build();
    }

    public static IdKontroll toInternal(Certificate certificate) {
        final var codeValue = codeValue(certificate.getData(), IDENTITET_STYRKT_GENOM_SVAR_ID_2);
        if (codeValue == null || codeValue.isEmpty()) {
            return IdKontroll.create(null);
        }
        return IdKontroll.create(KvIdKontroll.fromCode(codeValue));
    }
}