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
package se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PAVERKAN_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionFunktionsnedsattningPaverkan {

    private static final short LIMIT = 3500;

    public static CertificateDataElement toCertificate(String debut, int index, CertificateTextProvider textProvider) {

        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(CertificateDataConfigTextArea.builder()
                .id(FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16)
                .text(textProvider.get(FUNKTIONSNEDSATTNING_PAVERKAN_TEXT_ID))
                .build()
            )
            .value(CertificateDataTextValue.builder()
                .id(FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16)
                .text(debut)
                .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16))
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16)
                        .limit(LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16, FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16);
    }
}
