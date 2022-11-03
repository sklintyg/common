/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.db.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_PRINT_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_PRINT_MESSAGE_ID;
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

@ExtendWith(MockitoExtension.class)
class QuestionPrintMessagePolisanmalanTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            assertEquals(POLISANMALAN_PRINT_MESSAGE_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            assertEquals(POLISANMALAN_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeConfigType() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            assertEquals(UE_MESSAGE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeConfigMessage() {
            final var certificateDataElement = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            final var certificateDataElementConfig = (CertificateDataConfigMessage) certificateDataElement.getConfig();
            assertTrue(certificateDataElementConfig.getMessage().trim().length() > 0, "Missing message");
            verify(texts, atLeastOnce()).get(POLISANMALAN_PRINT_MESSAGE_ID);
        }

        @Test
        void shouldIncludeConfigLevelInfo() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            final var config = (CertificateDataConfigMessage) question.getConfig();
            assertEquals(MessageLevel.INFO, config.getLevel());
        }

        @Test
        void shouldIncludeValidationShowType() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.SHOW_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationShowQuestionId() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
            assertEquals(POLISANMALAN_DELSVAR_ID, certificateDataValidationShow.getQuestionId());
        }

        @Test
        void shouldIncludeValidationShowExpression() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
            assertEquals("$" + POLISANMALAN_JSON_ID, certificateDataValidationShow.getExpression());
        }

        @Test
        void shouldIncludeVisibilityFalse() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(false, 0, texts);
            assertEquals(false, question.getVisible());
        }

        @Test
        void shouldIncludeVisibilityTrue() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(true, 0, texts);
            assertEquals(true, question.getVisible());
        }

        @Test
        void shouldIncludeVisibilitNull() {
            final var question = QuestionPrintMessagePolisanmalan.toCertificate(null, 0, texts);
            assertNull(question.getVisible());
        }
    }
}