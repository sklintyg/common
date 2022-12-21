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

package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PSYKISK_UTVECKLINGSSTORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID_28;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PSYKISK_UTVECKLINGSSTORNING_DELSVAR_JSON_ID_28;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PSYKISK_UTVECKLINGSSTORNING_DELSVAR_TEXT_ID_28;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_JA_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_NEJ_TEXT;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_bas.v7.model.internal.Utvecklingsstorning;

public class QuestionPsykiskUtvecklingsstorning {

    public static CertificateDataElement toCertificate(Utvecklingsstorning utvecklingsstorning, int index, CertificateTextProvider textProvider) {
        var psykiskUtvecklingsstorning = utvecklingsstorning != null ? utvecklingsstorning.getPsykiskUtvecklingsstorning() : null;

        return CertificateDataElement.builder()
            .index(index)
            .id(PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID_28)
            .parent(PSYKISK_UTVECKLINGSSTORNING_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(PSYKISK_UTVECKLINGSSTORNING_DELSVAR_JSON_ID_28)
                    .text(textProvider.get(PSYKISK_UTVECKLINGSSTORNING_DELSVAR_TEXT_ID_28))
                    .selectedText(SVAR_JA_TEXT)
                    .unselectedText(SVAR_NEJ_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(PSYKISK_UTVECKLINGSSTORNING_DELSVAR_JSON_ID_28)
                    .selected(psykiskUtvecklingsstorning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID_28)
                        .expression(singleExpression(PSYKISK_UTVECKLINGSSTORNING_DELSVAR_JSON_ID_28))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID_28, PSYKISK_UTVECKLINGSSTORNING_DELSVAR_JSON_ID_28);
    }
}
