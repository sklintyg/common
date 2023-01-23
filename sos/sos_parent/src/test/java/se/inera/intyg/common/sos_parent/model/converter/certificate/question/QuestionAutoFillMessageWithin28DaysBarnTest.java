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
package se.inera.intyg.common.sos_parent.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_AUTOFILL_WITHIN_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_AUTO_FILL_WITHIN_MESSAGE_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes.UE_MESSAGE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class QuestionAutoFillMessageWithin28DaysBarnTest {

    @Mock
    private CertificateTextProvider texts;

    private Personnummer personId = Personnummer.createPersonnummer("19121212-1212").get();

    @BeforeEach
    void setup() {
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            assertEquals(BARN_AUTOFILL_WITHIN_MESSAGE_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            assertEquals(BARN_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeConfigType() {
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            assertEquals(UE_MESSAGE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeConfigMessage() {
            final var certificateDataElement = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            final var certificateDataElementConfig = (CertificateDataConfigMessage) certificateDataElement.getConfig();
            assertTrue(certificateDataElementConfig.getMessage().trim().length() > 0, "Missing message");
            verify(texts, atLeastOnce()).get(BARN_AUTO_FILL_WITHIN_MESSAGE_ID);
        }

        @Test
        void shouldIncludeConfigLevelInfo() {
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            final var config = (CertificateDataConfigMessage) question.getConfig();
            assertEquals(MessageLevel.OBSERVE, config.getLevel());
        }

        @Test
        void shouldIncludeValidationShowType() {
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            assertEquals(CertificateDataValidationType.SHOW_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationShowQuestionId() {
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
            assertEquals(DODSDATUM_DELSVAR_ID, certificateDataValidationShow.getQuestionId());
        }

        @Test
        void shouldIncludeValidationShowExpression() {
            final var withinTwentyEightDaysAfter19121212 = -20811;
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
            assertEquals("$" + DODSDATUM_JSON_ID + ".toEpochDay <= " + withinTwentyEightDaysAfter19121212,
                certificateDataValidationShow.getExpression());
        }

        @Test
        void shouldIncludeVisibilityFalse() {
            final var question = QuestionAutoFillMessageWithin28DaysBarn.toCertificate(personId, 0, texts);
            assertEquals(false, question.getVisible());
        }
    }
}