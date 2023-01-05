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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHANDLING_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULINBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULIN_ELLER_TABLETT_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULIN_ELLER_TABLETT_MESSAGE_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TABLETTBEHANDLING_DELSVAR_JSON_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigMessageTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;

@ExtendWith(MockitoExtension.class)
class QuestionTablettEllerInsulinMessageTest {

    @Mock
    CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        when(textProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementsTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionTablettEllerInsulinMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getId() {
            return INSULIN_ELLER_TABLETT_MESSAGE_ID;
        }

        @Override
        protected String getParent() {
            return HAR_DIABETES_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigMessageTests extends ConfigMessageTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionTablettEllerInsulinMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return null;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected String getMessageId() {
            return INSULIN_ELLER_TABLETT_MESSAGE_TEXT_ID;
        }

        @Override
        protected MessageLevel getMessageLevel() {
            return MessageLevel.INFO;
        }
    }

    @Nested
    class IncludeValidationShowTests extends ValidationShowTest {

        @Override
        protected String getQuestionId() {
            return BEHANDLING_DIABETES_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return TABLETTBEHANDLING_DELSVAR_JSON_ID + " || " + INSULINBEHANDLING_DELSVAR_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionTablettEllerInsulinMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeVisibilityTests {

        @Test
        void shouldBeVisibleIfTabletterIsTrue() {
            final var diabetes = Diabetes.builder().setTabletter(true).build();
            final var element = QuestionTablettEllerInsulinMessage.toCertificate(diabetes, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldBeVisibleIfInsulinIsTrue() {
            final var diabetes = Diabetes.builder().setInsulin(true).build();
            final var element = QuestionTablettEllerInsulinMessage.toCertificate(diabetes, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldNotBeVisibleIfTabletterIsFalse() {
            final var diabetes = Diabetes.builder().setTabletter(false).build();
            final var element = QuestionTablettEllerInsulinMessage.toCertificate(diabetes, 0, textProvider);
            assertFalse(element.getVisible());
        }

        @Test
        void shouldNotBeVisibleIfInsulinIsFalse() {
            final var diabetes = Diabetes.builder().setInsulin(false).build();
            final var element = QuestionTablettEllerInsulinMessage.toCertificate(diabetes, 0, textProvider);
            assertFalse(element.getVisible());
        }

        @Test
        void shouldNotBeVisibleIfDiabetesIsNull() {
            final var element = QuestionTablettEllerInsulinMessage.toCertificate(null, 0, textProvider);
            assertFalse(element.getVisible());
        }
    }
}