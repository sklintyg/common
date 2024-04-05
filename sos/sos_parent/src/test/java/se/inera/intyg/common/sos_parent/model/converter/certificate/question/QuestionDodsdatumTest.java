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
package se.inera.intyg.common.sos_parent.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;

import java.time.LocalDate;
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
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionDodsdatumTest {

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
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            assertEquals(DODSDATUM_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionDodsdatum.toCertificate(null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            assertEquals(DODSDATUM_SAKERT_DELSVAR_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(DODSDATUM_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeDateConfigType() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            assertEquals(CertificateDataConfigType.UE_DATE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeMaxDate() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            final var config = (CertificateDataConfigDate) question.getConfig();
            assertEquals(LocalDate.now(), config.getMaxDate());
        }

        @Test
        void shouldIncludeDateConfigValueId() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataConfigDate = (CertificateDataConfigDate) question.getConfig();
            assertEquals(DODSDATUM_JSON_ID, certificateDataConfigDate.getId());
        }

        @Test
        void shouldIncludeDateValueType() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValueType.DATE, question.getValue().getType());
        }

        @Test
        void shouldIncludeDateValueId() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataValueDate = (CertificateDataValueDate) question.getValue();
            assertEquals(DODSDATUM_JSON_ID, certificateDataValueDate.getId());
        }

        @Test
        void shouldIncludeDateValue() {
            final var expectedDateValue = LocalDate.now();
            final var question = QuestionDodsdatum.toCertificate(expectedDateValue, 0, texts);
            final var certificateDataValueDate = (CertificateDataValueDate) question.getValue();
            assertEquals(expectedDateValue, certificateDataValueDate.getDate());
        }

        @Test
        void shouldIncludeDateValueEmpty() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataValueDate = (CertificateDataValueDate) question.getValue();
            assertNull(certificateDataValueDate.getDate());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(DODSDATUM_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals("$" + DODSDATUM_JSON_ID, certificateDataValidationMandatory.getExpression());
        }

        @Nested
        class IncludeValidationShowTest extends ValidationShowTest {

            @Override
            protected String getQuestionId() {
                return DODSDATUM_SAKERT_DELSVAR_ID;
            }

            @Override
            protected String getExpression() {
                return String.format("exists('%s') && '%s'", DODSDATUM_SAKERT_JSON_ID, DODSDATUM_SAKERT_JSON_ID);
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDodsdatum.toCertificate(null, 0, texts);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }
        }

        @Test
        void shouldIncludeVisbility() {
            final var question = QuestionDodsdatum.toCertificate(null, 0, texts);
            assertEquals(false, question.getVisible());
        }

        @Test
        void shouldIncludeTrueVisbilityIfDateIsSet() {
            final var question = QuestionDodsdatum.toCertificate(LocalDate.now(), 0, texts);
            assertEquals(true, question.getVisible());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        @Test
        void shouldIncludeValue() {
            final var expectedValue = LocalDate.now();

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDodsdatum.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionDodsdatum.toInternal(certificate);

            assertEquals(new InternalDate(expectedValue), actualValue);
        }

        @Test
        void shouldIncludeValueNull() {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDodsdatum.toCertificate(null, 0, texts))
                .build();

            final var actualValue = QuestionDodsdatum.toInternal(certificate);

            assertEquals(null, actualValue);
        }
    }
}