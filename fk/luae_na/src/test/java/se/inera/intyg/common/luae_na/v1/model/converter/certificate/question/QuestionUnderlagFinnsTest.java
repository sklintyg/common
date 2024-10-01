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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_SELECTED_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_UNSELECTED_TEXT;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionUnderlagFinnsTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);

            assertEquals(UNDERLAGFINNS_SVAR_ID_3, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;

            final var question = QuestionUnderlagFinns.toCertificate(null, expectedIndex, texts);

            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);

            assertEquals(GRUNDFORMU_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeConfigCertificateDataConfigDate() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);

            assertEquals(CertificateDataConfigType.UE_RADIO_BOOLEAN, question.getConfig().getType());
        }

        @Test
        void shouldIncludeConfigId() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);
            final var config = (CertificateDataConfigRadioBoolean) question.getConfig();

            assertEquals(UNDERLAGFINNS_SVAR_JSON_ID_3, config.getId());
        }

        @Test
        void shouldIncludeConfigText() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);

            verify(texts, atLeastOnce()).get(UNDERLAGFINNS_SVAR_TEXT);
        }

        @Test
        void shouldIncludeConfigSelectedText() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);
            final var config = (CertificateDataConfigRadioBoolean) question.getConfig();

            assertEquals(UNDERLAGFINNS_SELECTED_TEXT, config.getSelectedText());
        }

        @Test
        void shouldIncludeConfigUnselectedText() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);
            final var config = (CertificateDataConfigRadioBoolean) question.getConfig();

            assertEquals(UNDERLAGFINNS_UNSELECTED_TEXT, config.getUnselectedText());
        }

        @Test
        void shouldIncludeValueTypeBoolean() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);

            assertEquals(CertificateDataValueType.BOOLEAN, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueId() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);
            final var value = (CertificateDataValueBoolean) question.getValue();

            assertEquals(UNDERLAGFINNS_SVAR_JSON_ID_3, value.getId());
        }

        @Test
        void shouldIncludeValueBoolean() {
            final var expectedValue = true;
            final var question = QuestionUnderlagFinns.toCertificate(expectedValue, 0, texts);
            final var value = (CertificateDataValueBoolean) question.getValue();

            assertEquals(expectedValue, value.getSelected());
        }

        @Test
        void shouldIncludeValidationMandatory() {
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);
            final var certificateDataValidationText = question.getValidation()[0];

            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, certificateDataValidationText.getType());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var expectedExpression = "exists(" + UNDERLAGFINNS_SVAR_JSON_ID_3 + ")";
            final var question = QuestionUnderlagFinns.toCertificate(null, 0, texts);
            final var certificateDataValidationText = (CertificateDataValidationMandatory) question.getValidation()[0];

            assertEquals(expectedExpression, certificateDataValidationText.getExpression());
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
                .addElement(QuestionUnderlagFinns.toCertificate(expectedValue, index, texts))
                .build();

            final var actualValue = QuestionUnderlagFinns.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}