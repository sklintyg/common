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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROGRESSIV_OGONSJUKDOM_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROGRESSIV_OGONSJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SEENDE_NEDSATT_BELYSNING_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SEENDE_NEDSATT_BELYSNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFALTSDEFEKTER_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFALTSDEFEKTER_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFUNKTIONER_CATEGORY_ID;

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
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;

@ExtendWith(MockitoExtension.class)
class QuestionLakarintygAvOgonspecialistMessageTest {

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
            return QuestionLakarintygAvOgonspecialistMessage.toCertificate(null, 0, textProvider);

        }

        @Override
        protected String getId() {
            return LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return SYNFUNKTIONER_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigMessageTest extends ConfigMessageTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionLakarintygAvOgonspecialistMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return null;
        }

        @Override
        protected String getMessageId() {
            return LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_TEXT_ID;
        }

        @Override
        protected MessageLevel getMessageLevel() {
            return MessageLevel.INFO;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    class IncludeValidationShowTests extends ValidationShowTest {

        @Override
        protected String getQuestionId() {
            return SYNFALTSDEFEKTER_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + SYNFALTSDEFEKTER_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionLakarintygAvOgonspecialistMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationShowTests1 extends ValidationShowTest {

        @Override
        protected String getQuestionId() {
            return SEENDE_NEDSATT_BELYSNING_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + SEENDE_NEDSATT_BELYSNING_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionLakarintygAvOgonspecialistMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }
    }

    @Nested
    class IncludeValidationShowTests2 extends ValidationShowTest {

        @Override
        protected String getQuestionId() {
            return PROGRESSIV_OGONSJUKDOM_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + PROGRESSIV_OGONSJUKDOM_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionLakarintygAvOgonspecialistMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 2;
        }
    }

    @Nested
    class IncludeVisibilityTests {

        @Test
        void shouldBeVisibleIfNattblindhetIsTrue() {
            final var syn = Syn.builder().setNattblindhet(true).build();
            final var element = QuestionLakarintygAvOgonspecialistMessage.toCertificate(syn, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldBeVisibleIfProgressivOgonsjukdomIsTrue() {
            final var syn = Syn.builder().setProgressivOgonsjukdom(true).build();
            final var element = QuestionLakarintygAvOgonspecialistMessage.toCertificate(syn, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldBeVisibleIfSynfaltsdefekterIsTrue() {
            final var syn = Syn.builder().setSynfaltsdefekter(true).build();
            final var element = QuestionLakarintygAvOgonspecialistMessage.toCertificate(syn, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldNotBeVisibileIfValuesAreFalse() {
            final var syn = Syn.builder()
                .setNattblindhet(false)
                .setProgressivOgonsjukdom(false)
                .setSynfaltsdefekter(false)
                .build();

            final var element = QuestionLakarintygAvOgonspecialistMessage.toCertificate(null, 0, textProvider);
            assertFalse(element.getVisible());
        }

        @Test
        void shouldNotBeVisibileIfSynIsNull() {
            final var element = QuestionLakarintygAvOgonspecialistMessage.toCertificate(null, 0, textProvider);
            assertFalse(element.getVisible());
        }
    }
}