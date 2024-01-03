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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCategoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;

@ExtendWith(MockitoExtension.class)
class CategoryHypoglykemiTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        doReturn("Text!").when(textProvider).get(anyString());
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return CategoryHypoglykemi.toCertificate(getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return HYPOGLYKEMI_CATEGORY_ID;
        }

        @Override
        protected String getParent() {
            return null;
        }

        @Override
        protected int getIndex() {
            return 3;
        }
    }

    @Nested
    class IncludeConfigCategoryTest extends ConfigCategoryTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return CategoryHypoglykemi.toCertificate(0, textProvider);
        }

        @Override
        protected String getTextId() {
            return HYPOGLYKEMI_CATEGORY_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    class IncludeValidationShowTestRiskForHypoglykemi extends ValidationShowTest {

        @Override
        protected CertificateDataElement getElement() {
            return CategoryHypoglykemi.toCertificate(0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getQuestionId() {
            return ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
        }
    }

    @Nested
    class IncludeValidationShowTestIntygAvser extends ValidationShowTest {

        @Override
        protected CertificateDataElement getElement() {
            return CategoryHypoglykemi.toCertificate(0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected String getQuestionId() {
            return INTYG_AVSER_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + IntygAvserKategori.VAR1.name() + ") || exists(" + IntygAvserKategori.VAR2.name() + ") || exists("
                + IntygAvserKategori.VAR3.name()
                + ") || exists(" + IntygAvserKategori.VAR4.name() + ") || exists(" + IntygAvserKategori.VAR5.name()
                + ") || exists(" + IntygAvserKategori.VAR6.name() + ") || exists(" + IntygAvserKategori.VAR7.name()
                + ") || exists(" + IntygAvserKategori.VAR8.name() + ") || exists(" + IntygAvserKategori.VAR9.name() + ")";
        }
    }
}
