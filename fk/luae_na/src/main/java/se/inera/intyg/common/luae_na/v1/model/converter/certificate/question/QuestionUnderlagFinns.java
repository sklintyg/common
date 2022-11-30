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

package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_DELSVAR_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_SELECTED_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_UNSELECTED_TEXT;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionUnderlagFinns {

    public static CertificateDataElement toCertificate(Boolean underlagFinns, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(UNDERLAGFINNS_DELSVAR_ID_3)
            .parent(GRUNDFORMU_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigBoolean.builder()
                    .id(UNDERLAGFINNS_SVAR_JSON_ID_3)
                    .text(texts.get(UNDERLAGFINNS_SVAR_TEXT))
                    .selectedText(UNDERLAGFINNS_SELECTED_TEXT)
                    .unselectedText(UNDERLAGFINNS_UNSELECTED_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(UNDERLAGFINNS_SVAR_JSON_ID_3)
                    .selected(underlagFinns)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UNDERLAGFINNS_DELSVAR_ID_3)
                        .expression(singleExpression(UNDERLAGFINNS_SVAR_JSON_ID_3))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), UNDERLAGFINNS_DELSVAR_ID_3, UNDERLAGFINNS_SVAR_JSON_ID_3);
    }
}
