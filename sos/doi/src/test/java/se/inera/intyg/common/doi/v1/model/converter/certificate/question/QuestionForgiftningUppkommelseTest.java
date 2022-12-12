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

package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

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
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;

@ExtendWith(MockitoExtension.class)
class QuestionForgiftningUppkommelseTest {

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
            final var question = QuestionForgiftningUppkommelse.toCertificate("", 0, texts);
            assertEquals(FORGIFTNING_UPPKOMMELSE_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionForgiftningUppkommelse.toCertificate("", expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionForgiftningUppkommelse.toCertificate("", 0, texts);
            assertEquals(FORGIFTNING_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionForgiftningUppkommelse.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FORGIFTNING_UPPKOMMELSE_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeDescription() {
            final var question = QuestionForgiftningUppkommelse.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FORGIFTNING_UPPKOMMELSE_DESCRIPTION_TEXT_ID);
        }

        @Test
        void shouldIncludeTextConfigType() {
            final var question = QuestionForgiftningUppkommelse.toCertificate("", 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());
        }

        @Test
        void shouldIncludeTextConfigValueId() {
            final var question = QuestionForgiftningUppkommelse.toCertificate("", 0, texts);
            final var certificateDataConfigTextField = (CertificateDataConfigTextArea) question.getConfig();
            assertEquals(FORGIFTNING_UPPKOMMELSE_JSON_ID, certificateDataConfigTextField.getId());
        }

        @Test
        void shouldIncludeTextValueType() {
            final var question = QuestionForgiftningUppkommelse.toCertificate("", 0, texts);
            assertEquals(CertificateDataValueType.TEXT, question.getValue().getType());
        }

        @Test
        void shouldIncludeTextValueId() {
            final var question = QuestionForgiftningUppkommelse.toCertificate("Text value", 0, texts);
            final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
            assertEquals(FORGIFTNING_UPPKOMMELSE_JSON_ID, certificateDataTextValue.getId());
        }

        @Test
        void shouldIncludeTextValue() {
            final var expectedTextValue = "Text value";
            final var question = QuestionForgiftningUppkommelse.toCertificate(expectedTextValue, 0, texts);
            final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
            assertEquals(expectedTextValue, certificateDataTextValue.getText());
        }

        @Test
        void shouldIncludeTextValueEmpty() {
            final var question = QuestionForgiftningUppkommelse.toCertificate(null, 0, texts);
            final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
            assertNull(certificateDataTextValue.getText());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionForgiftningUppkommelse.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionForgiftningUppkommelse.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(FORGIFTNING_UPPKOMMELSE_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var question = QuestionForgiftningUppkommelse.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals("$" + FORGIFTNING_UPPKOMMELSE_JSON_ID, certificateDataValidationMandatory.getExpression());
        }

        @Nested
        class IncludeValidationShowTest extends ValidationShowTest {

            @Override
            protected String getQuestionId() {
                return FORGIFTNING_OM_DELSVAR_ID;
            }

            @Override
            protected String getExpression() {
                return singleExpression(FORGIFTNING_OM_JSON_ID);
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionForgiftningUppkommelse.toCertificate(null, 0, texts);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }
        }

        @Test
        void shouldIncludeValidationMaxCharacterType() {
            final var question = QuestionForgiftningUppkommelse.toCertificate("", 0, texts);
            assertEquals(CertificateDataValidationType.TEXT_VALIDATION, question.getValidation()[2].getType());
        }

        @Test
        void shouldIncludeValidationTextId() {
            final var question = QuestionForgiftningUppkommelse.toCertificate("", 0, texts);
            final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[2];
            assertEquals(FORGIFTNING_UPPKOMMELSE_JSON_ID, certificateDataValidationText.getId());
        }

        @Test
        void shouldIncludeValidationTextLimit() {
            final var question = QuestionForgiftningUppkommelse.toCertificate("", 0, texts);
            final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[2];
            assertEquals(150, certificateDataValidationText.getLimit());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeTextValue(String expectedValue) {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionForgiftningUppkommelse.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionForgiftningUppkommelse.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}
