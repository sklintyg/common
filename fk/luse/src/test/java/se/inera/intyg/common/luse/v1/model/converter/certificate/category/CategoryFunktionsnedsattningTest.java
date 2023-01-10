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

package se.inera.intyg.common.luse.v1.model.converter.certificate.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_DESCRIPTION_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.ExpressionTypeEnum;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCategoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationCategoryMandatoryTest;

@ExtendWith(MockitoExtension.class)
class CategoryFunktionsnedsattningTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return CategoryFunktionsnedsattning.toCertificate(0, texts);
        }

        @Override
        protected String getId() {
            return FUNKTIONSNEDSATTNING_CATEGORY_ID;
        }

        @Override
        protected String getParent() {
            return null;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCategoryTests extends ConfigCategoryTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return CategoryFunktionsnedsattning.toCertificate(0, texts);
        }

        @Override
        protected String getTextId() {
            return FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return FUNKTIONSNEDSATTNING_CATEGORY_DESCRIPTION_ID;
        }
    }

    @Nested
    class IncludeValidationCategoryMandatoryTests extends ValidationCategoryMandatoryTest {

        @Override
        protected ExpressionTypeEnum getExpressionType() {
            return ExpressionTypeEnum.OR;
        }

        @Override
        protected List<CertificateDataValidationMandatory> getListOfQuestions() {
            return List.of(
                CertificateDataValidationMandatory.builder()
                    .questionId(FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8)
                    .expression(singleExpression(FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8))
                    .build(),
                CertificateDataValidationMandatory.builder()
                    .questionId(FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9)
                    .expression(singleExpression(FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9))
                    .build(),
                CertificateDataValidationMandatory.builder()
                    .questionId(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10)
                    .expression(singleExpression(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10))
                    .build(),
                CertificateDataValidationMandatory.builder()
                    .questionId(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11)
                    .expression(singleExpression(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11))
                    .build(),
                CertificateDataValidationMandatory.builder()
                    .questionId(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12)
                    .expression(singleExpression(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12))
                    .build(),
                CertificateDataValidationMandatory.builder()
                    .questionId(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13)
                    .expression(singleExpression(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13))
                    .build(),
                CertificateDataValidationMandatory.builder()
                    .questionId(FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14)
                    .expression(singleExpression(FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14))
                    .build()
            );
        }

        @Override
        protected CertificateDataElement getElement() {
            return CategoryFunktionsnedsattning.toCertificate(0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }
}
