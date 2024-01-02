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
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.KOMPLETTERANDE_PATIENTUPPGIFTER_CATEGORY_ID;

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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionIdentitetenStyrktTest {

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
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            assertEquals(IDENTITET_STYRKT_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionIdentitetenStyrkt.toCertificate("", expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            assertEquals(KOMPLETTERANDE_PATIENTUPPGIFTER_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionIdentitetenStyrkt.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(IDENTITET_STYRKT_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeTextConfigType() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_TEXTFIELD, question.getConfig().getType());
        }

        @Test
        void shouldIncludeTextConfigValueId() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            final var certificateDataConfigTextField = (CertificateDataConfigTextField) question.getConfig();
            assertEquals(IDENTITET_STYRKT_JSON_ID, certificateDataConfigTextField.getId());
        }

        @Test
        void shouldIncludeTextValueType() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            assertEquals(CertificateDataValueType.TEXT, question.getValue().getType());
        }

        @Test
        void shouldIncludeTextValueId() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("Text value", 0, texts);
            final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
            assertEquals(IDENTITET_STYRKT_JSON_ID, certificateDataTextValue.getId());
        }

        @Test
        void shouldIncludeTextValue() {
            final var expectedTextValue = "Text value";
            final var question = QuestionIdentitetenStyrkt.toCertificate(expectedTextValue, 0, texts);
            final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
            assertEquals(expectedTextValue, certificateDataTextValue.getText());
        }

        @Test
        void shouldIncludeTextValueEmpty() {
            final var question = QuestionIdentitetenStyrkt.toCertificate(null, 0, texts);
            final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
            assertNull(certificateDataTextValue.getText());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(IDENTITET_STYRKT_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals("$" + IDENTITET_STYRKT_JSON_ID, certificateDataValidationMandatory.getExpression());
        }

        @Test
        void shouldIncludeValidationMaxCharacterType() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            assertEquals(CertificateDataValidationType.TEXT_VALIDATION, question.getValidation()[1].getType());
        }

        @Test
        void shouldIncludeValidationTextId() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[1];
            assertEquals(IDENTITET_STYRKT_JSON_ID, certificateDataValidationText.getId());
        }

        @Test
        void shouldIncludeValidationTextLimit() {
            final var question = QuestionIdentitetenStyrkt.toCertificate("", 0, texts);
            final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[1];
            assertEquals(27, certificateDataValidationText.getLimit());
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
                .addElement(QuestionIdentitetenStyrkt.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionIdentitetenStyrkt.toInternal(certificate);

            if (expectedValue == null || expectedValue.isEmpty()) {
                assertNull(actualValue);
            } else {
                assertEquals(expectedValue, actualValue);
            }
        }
    }
}