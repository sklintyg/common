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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULIN_ELLER_TABLETT_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULIN_ELLER_TABLETT_MESSAGE_TEXT_ID;

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
import se.inera.intyg.common.ts_bas.v6.model.internal.Diabetes;

@ExtendWith(MockitoExtension.class)
class QuestionTablettEllerInsulinMessageTest {

    @Mock
    CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        when(textProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

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
    class IncludeConfigViewText extends ConfigMessageTest {


        @Override
        protected String getMessageId() {
            return INSULIN_ELLER_TABLETT_MESSAGE_TEXT_ID;
        }

        @Override
        protected MessageLevel getMessageLevel() {
            return MessageLevel.INFO;
        }

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
    }

    @Nested
    class IncludeValueMessageTests {

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
        void shouldNotBeVisibleIfTabletterOrInsulinIsFalse() {
            final var diabetes = Diabetes.builder()
                .setTabletter(false)
                .setInsulin(false)
                .build();
            final var element = QuestionTablettEllerInsulinMessage.toCertificate(diabetes, 0, textProvider);
            assertFalse(element.getVisible());
        }

        @Test
        void shouldNotBeVisibleIfTabletterOrInsulinIsNull() {
            final var diabetes = Diabetes.builder().build();
            final var element = QuestionTablettEllerInsulinMessage.toCertificate(diabetes, 0, textProvider);
            assertFalse(element.getVisible());
        }
    }
}
