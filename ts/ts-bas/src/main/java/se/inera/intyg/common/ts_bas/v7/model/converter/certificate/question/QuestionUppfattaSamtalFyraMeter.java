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
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BALANSRUBBNINGAR_YRSEL_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_JA_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_NEJ_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UPPFATTA_SAMTALSTAMMA_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UPPFATTA_SAMTALSTAMMA_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UPPFATTA_SAMTALSTAMMA_SVAR_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_bas.v7.model.internal.HorselBalans;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvserKategori;

public class QuestionUppfattaSamtalFyraMeter {

    public static CertificateDataElement toCertificate(HorselBalans horselBalans, int index, CertificateTextProvider textProvider) {
        final var uppfattaSamtal = horselBalans != null ? horselBalans.getSvartUppfattaSamtal4Meter() : null;
        return CertificateDataElement.builder()
            .id(UPPFATTA_SAMTALSTAMMA_SVAR_ID)
            .parent(BALANSRUBBNINGAR_YRSEL_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(UPPFATTA_SAMTALSTAMMA_JSON_ID)
                    .text(textProvider.get(UPPFATTA_SAMTALSTAMMA_SVAR_TEXT_ID))
                    .selectedText(SVAR_JA_TEXT)
                    .unselectedText(SVAR_NEJ_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(UPPFATTA_SAMTALSTAMMA_JSON_ID)
                    .selected(uppfattaSamtal)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UPPFATTA_SAMTALSTAMMA_SVAR_ID)
                        .expression(exists(UPPFATTA_SAMTALSTAMMA_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(INTYG_AVSER_SVAR_ID_1)
                        .expression(multipleOrExpressionWithExists(
                            IntygAvserKategori.IAV5.name(), IntygAvserKategori.IAV6.name(), IntygAvserKategori.IAV7.name(),
                            IntygAvserKategori.IAV8.name(), IntygAvserKategori.IAV9.name()))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), UPPFATTA_SAMTALSTAMMA_SVAR_ID, UPPFATTA_SAMTALSTAMMA_JSON_ID);
    }

}
