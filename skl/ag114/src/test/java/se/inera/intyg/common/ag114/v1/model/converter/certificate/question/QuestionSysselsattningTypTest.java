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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_SYSSELSATTNING_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_LABEL_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_TEXT_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationDisableTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueBooleanTest;

class QuestionSysselsattningTypTest {

    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }

        @Override
        protected String getId() {
            return TYP_AV_SYSSELSATTNING_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return CATEGORY_SYSSELSATTNING_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCheckboxBooleanTests extends ConfigCheckboxBooleanTest {

        @Override
        protected String getJsonId() {
            return TYP_AV_SYSSELSATTNING_SVAR_JSON_ID;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }

        @Override
        protected String getLabelId() {
            return TYP_AV_SYSSELSATTNING_SVAR_LABEL_ID;
        }

        @Override
        protected String getTextId() {
            return TYP_AV_SYSSELSATTNING_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected String getSelectedTextId() {
            return ANSWER_YES;
        }

        @Override
        protected String getUnselectedTextId() {
            return ANSWER_NOT_SELECTED;
        }
    }

    @Nested
    class IncludeValueBooleanTests extends ValueBooleanTest {

        @Override
        protected String getJsonId() {
            return TYP_AV_SYSSELSATTNING_SVAR_JSON_ID;
        }

        @Override
        protected Boolean getBoolean() {
            return true;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }
    }

    @Nested
    class IncludeValidationDisableTests extends ValidationDisableTest {

        @Override
        protected String getQuestionId() {
            return TYP_AV_SYSSELSATTNING_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + TYP_AV_SYSSELSATTNING_SVAR_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeInternalSysselsattningValueTests {

        @Test
        void shallReturnSysselsattningNuvarandeArbete() {
            final var result = QuestionSysselsattningTyp.toInternal();
            assertEquals(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE), result.get(0));
        }
    }
}
