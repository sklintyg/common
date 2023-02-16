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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_COLLECTION;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_INFO;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.icfCodeValue;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.icfTextValue;

import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigIcf;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataIcfValue;

public class QuestionAktivitetsbegransningar {

    public static CertificateDataElement toCertificate(String value, List<String> activityLimitationCategories,
        int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_SVAR_ID_17)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigIcf.builder()
                    .header(texts.get(AKTIVITETSBEGRANSNING_SVAR_TEXT))
                    .text(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_TEXT))
                    .description(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING))
                    .modalLabel(AKTIVITETSBEGRANSNING_ICF_INFO)
                    .collectionsLabel(AKTIVITETSBEGRANSNING_ICF_COLLECTION)
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .placeholder(AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER)
                    .build()
            )
            .value(
                CertificateDataIcfValue.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .text(value)
                    .icfCodes(activityLimitationCategories)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(AKTIVITETSBEGRANSNING_SVAR_ID_17)
                        .expression(
                            singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static String toInternalTextValue(Certificate certificate) {
        return icfTextValue(certificate.getData(), AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17);
    }

    public static List<String> toInternalCodeValue(Certificate certificate) {
        return icfCodeValue(certificate.getData(), AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17);
    }
}
