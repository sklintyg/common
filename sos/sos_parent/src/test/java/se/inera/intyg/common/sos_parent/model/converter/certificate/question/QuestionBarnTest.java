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

package se.inera.intyg.common.sos_parent.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_QUESTION_SELECTED_QUESTION;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_QUESTION_UNSELECTED_QUESTION;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationAutoFill;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class QuestionBarnTest {

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
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            assertEquals(BARN_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionBarn.toCertificate(personId, true, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            assertEquals(BARN_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionBarn.toCertificate(personId, null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(BARN_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeSelectedText() {
            final var question = QuestionBarn.toCertificate(personId, null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(BARN_QUESTION_SELECTED_QUESTION);
        }

        @Test
        void shouldIncludeUnselectedText() {
            final var question = QuestionBarn.toCertificate(personId, null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(BARN_QUESTION_UNSELECTED_QUESTION);
        }

        @Test
        void shouldIncludeRadioBooleanConfigType() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_RADIO_BOOLEAN, question.getConfig().getType());
        }

        @Test
        void shouldIncludeRadioBooleanConfigValueId() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataConfigRadioBoolean = (CertificateDataConfigRadioBoolean) question.getConfig();
            assertEquals(BARN_JSON_ID, certificateDataConfigRadioBoolean.getId());
        }

        @Test
        void shouldIncludeBooleanValueType() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            assertEquals(CertificateDataValueType.BOOLEAN, question.getValue().getType());
        }

        @Test
        void shouldIncludeBooleanValueId() {
            final var question = QuestionBarn.toCertificate(personId, null, 0, texts);
            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertEquals(BARN_JSON_ID, certificateDataValueBoolean.getId());
        }

        @Test
        void shouldIncludeBooleanValueTrue() {
            final var expectedBooleanValue = Boolean.TRUE;
            final var question = QuestionBarn.toCertificate(personId, expectedBooleanValue, 0, texts);
            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertEquals(expectedBooleanValue, certificateDataValueBoolean.getSelected());
        }

        @Test
        void shouldIncludeBooleanValueFalse() {
            final var expectedBooleanValue = Boolean.FALSE;
            final var question = QuestionBarn.toCertificate(personId, expectedBooleanValue, 0, texts);
            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertEquals(expectedBooleanValue, certificateDataValueBoolean.getSelected());
        }

        @Test
        void shouldIncludeBooleanValueEmpty() {
            final var question = QuestionBarn.toCertificate(personId, null, 0, texts);
            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertNull(certificateDataValueBoolean.getSelected());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(BARN_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals("$" + BARN_JSON_ID, certificateDataValidationMandatory.getExpression());
        }

        @Test
        void shouldIncludeValidationAutoFillType() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            assertEquals(CertificateDataValidationType.AUTO_FILL_VALIDATION, question.getValidation()[1].getType());
        }

        @Test
        void shouldIncludeValidationAutoFillQuestionId() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[1];
            assertEquals(DODSDATUM_DELSVAR_ID, certificateDataValidationAutoFill.getQuestionId());
        }

        @Test
        void shouldIncludeValidationAutoFillExpressionWithin28DaysOfBirth() {
            final var withinTwentyEightDaysAfter19121212 = -20811;
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[1];
            assertEquals("$" + DODSDATUM_JSON_ID + ".toEpochDay <= " + withinTwentyEightDaysAfter19121212,
                certificateDataValidationAutoFill.getExpression());
        }

        @Test
        void shouldIncludeValidationAutoFillValueBooleanType() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[1];
            assertEquals(CertificateDataValueType.BOOLEAN, certificateDataValidationAutoFill.getFillValue().getType());
        }

        @Test
        void shouldIncludeValidationAutoFillValueBooleanId() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[1];
            final var fillValue = (CertificateDataValueBoolean) certificateDataValidationAutoFill.getFillValue();
            assertEquals(BARN_JSON_ID, fillValue.getId());
        }

        @Test
        void shouldIncludeValidationAutoFillValueBooleanTrue() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[1];
            final var fillValue = (CertificateDataValueBoolean) certificateDataValidationAutoFill.getFillValue();
            assertEquals(true, fillValue.getSelected());
        }

        @Test
        void shouldIncludeValidationAutoFillTypeAfter28DaysOfBirth() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            assertEquals(CertificateDataValidationType.AUTO_FILL_VALIDATION, question.getValidation()[2].getType());
        }

        @Test
        void shouldIncludeValidationAutoFillQuestionIdAfter28DaysOfBirth() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[2];
            assertEquals(DODSDATUM_DELSVAR_ID, certificateDataValidationAutoFill.getQuestionId());
        }

        @Test
        void shouldIncludeValidationAutoFillExpressionAfter28DaysOfBirth() {
            final var withinTwentyEightDaysAfter19121212 = -20811;
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[2];
            assertEquals("$" + DODSDATUM_JSON_ID + ".toEpochDay > " + withinTwentyEightDaysAfter19121212,
                certificateDataValidationAutoFill.getExpression());
        }

        @Test
        void shouldIncludeValidationAutoFillValueBooleanTypeAfter28DaysOfBirth() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[2];
            assertEquals(CertificateDataValueType.BOOLEAN, certificateDataValidationAutoFill.getFillValue().getType());
        }

        @Test
        void shouldIncludeValidationAutoFillValueBooleanIdAfter28DaysOfBirth() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[2];
            final var fillValue = (CertificateDataValueBoolean) certificateDataValidationAutoFill.getFillValue();
            assertEquals(BARN_JSON_ID, fillValue.getId());
        }

        @Test
        void shouldIncludeValidationAutoFillValueBooleanFalseAfter28DaysOfBirth() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationAutoFill = (CertificateDataValidationAutoFill) question.getValidation()[2];
            final var fillValue = (CertificateDataValueBoolean) certificateDataValidationAutoFill.getFillValue();
            assertEquals(false, fillValue.getSelected());
        }

        @Test
        void shouldIncludeValidationDisableType() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            assertEquals(CertificateDataValidationType.DISABLE_VALIDATION, question.getValidation()[3].getType());
        }

        @Test
        void shouldIncludeValidationDisableQuestionId() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationDisable = (CertificateDataValidationDisable) question.getValidation()[3];
            assertEquals(DODSDATUM_DELSVAR_ID, certificateDataValidationDisable.getQuestionId());
        }

        @Test
        void shouldIncludeValidationDisableExpression() {
            final var question = QuestionBarn.toCertificate(personId, true, 0, texts);
            final var certificateDataValidationDisable = (CertificateDataValidationDisable) question.getValidation()[3];
            assertEquals("$" + DODSDATUM_JSON_ID, certificateDataValidationDisable.getExpression());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionBarn.toCertificate(personId, expectedValue, index, texts))
                .build();

            final var actualValue = QuestionBarn.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}