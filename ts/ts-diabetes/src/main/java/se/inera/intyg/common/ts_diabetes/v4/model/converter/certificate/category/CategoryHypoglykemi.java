/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.category;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;

public class CategoryHypoglykemi {

    public static CertificateDataElement toCertificate(int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(HYPOGLYKEMI_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(HYPOGLYKEMI_CATEGORY_TEXT_ID))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID)
                        .expression(singleExpression(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(INTYG_AVSER_SVAR_ID)
                        .expression(multipleOrExpressionWithExists(IntygAvserKategori.VAR1.name(), IntygAvserKategori.VAR2.name(),
                            IntygAvserKategori.VAR3.name(), IntygAvserKategori.VAR4.name(), IntygAvserKategori.VAR5.name(),
                            IntygAvserKategori.VAR6.name(), IntygAvserKategori.VAR7.name(), IntygAvserKategori.VAR8.name(),
                            IntygAvserKategori.VAR9.name())
                        )
                        .build()
                }
            )
            .build();
    }
}
