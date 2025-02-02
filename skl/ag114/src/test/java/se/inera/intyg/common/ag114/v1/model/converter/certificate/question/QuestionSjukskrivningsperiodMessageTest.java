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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_BEDOMNING_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_TEXT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;

import java.time.LocalDate;
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
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;

@ExtendWith(MockitoExtension.class)
class QuestionSjukskrivningsperiodMessageTest {

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
            return QuestionSjukskrivningsperiodMessage.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_ID;
        }

        @Override
        protected String getParent() {
            return CATEGORY_BEDOMNING_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigMessageTests extends ConfigMessageTest {

        @Override
        protected String getMessageId() {
            return SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_TEXT_ID;
        }

        @Override
        protected MessageLevel getMessageLevel() {
            return MessageLevel.INFO;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSjukskrivningsperiodMessage.toCertificate(null, 0, texts);
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
    class IncludeValidationShowTests extends ValidationShowTest {

        private static final long DATE_RANGE_LIMIT = 14;

        @Override
        protected String getQuestionId() {
            return SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID + ".to" + " - " + SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID + ".from" + " >= "
                + DATE_RANGE_LIMIT;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSjukskrivningsperiodMessage.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeVisibleTests {

        @Test
        void shouldBeVisible() {
            final var internalLocalDateInterval = new InternalLocalDateInterval();
            internalLocalDateInterval.setFrom(new InternalDate(LocalDate.now()));
            internalLocalDateInterval.setTom(new InternalDate(LocalDate.now().plusDays(14)));
            final var certificateDataElement = QuestionSjukskrivningsperiodMessage.toCertificate(internalLocalDateInterval, 0, texts);
            assertTrue(certificateDataElement.getVisible());
        }

        @Test
        void shouldNotBeVisible() {
            final var internalLocalDateInterval = new InternalLocalDateInterval();
            internalLocalDateInterval.setFrom(new InternalDate(LocalDate.now()));
            internalLocalDateInterval.setTom(new InternalDate(LocalDate.now().plusDays(13)));
            final var certificateDataElement = QuestionSjukskrivningsperiodMessage.toCertificate(internalLocalDateInterval, 0, texts);
            assertFalse(certificateDataElement.getVisible());
        }
    }
}
