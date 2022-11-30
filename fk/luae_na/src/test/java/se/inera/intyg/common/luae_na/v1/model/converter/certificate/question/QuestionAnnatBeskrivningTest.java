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

package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;

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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionAnnatBeskrivningTest {

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
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);

            assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;

            final var question = QuestionAnnatBeskrivning.toCertificate(null, expectedIndex, texts);

            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);

            assertEquals(GRUNDFORMU_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeConfigCertificateDataConfigTextfield() {
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);

            assertEquals(CertificateDataConfigTypes.UE_TEXTFIELD, question.getConfig().getType());
        }

        @Test
        void shouldIncludeConfigId() {
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);
            final var config = (CertificateDataConfigTextField) question.getConfig();

            assertEquals(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, config.getId());
        }

        @Test
        void shouldIncludeConfigText() {
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);

            verify(texts, atLeastOnce()).get(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT);
        }

        @Test
        void shouldIncludeValueTypeText() {
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);

            assertEquals(CertificateDataValueType.TEXT, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueId() {
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);
            final var value = (CertificateDataTextValue) question.getValue();

            assertEquals(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, value.getId());
        }

        @Test
        void shouldIncludeValueText() {
            final var expectedText = "Annan text";
            final var question = QuestionAnnatBeskrivning.toCertificate(expectedText, 0, texts);
            final var value = (CertificateDataTextValue) question.getValue();

            assertEquals(expectedText, value.getText());
        }

        @Test
        void shouldIncludeValidationShow() {
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);
            final var showValidation = (CertificateDataValidationShow) question.getValidation()[0];

            assertEquals(CertificateDataValidationType.SHOW_VALIDATION, showValidation.getType());
        }

        @Test
        void shouldIncludeValidationShowExpression() {
            final var expectedExpression = "$" + GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);
            final var showValidation = (CertificateDataValidationShow) question.getValidation()[0];

            assertEquals(expectedExpression, showValidation.getExpression());
        }

        @Test
        void shouldIncludeValidationMandatory() {
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);
            final var mandatoryValidation = (CertificateDataValidationMandatory) question.getValidation()[1];

            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, mandatoryValidation.getType());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var expectedExpression = "$" + GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
            final var question = QuestionAnnatBeskrivning.toCertificate(null, 0, texts);
            final var showValidation = (CertificateDataValidationMandatory) question.getValidation()[1];

            assertEquals(expectedExpression, showValidation.getExpression());
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
                .addElement(QuestionAnnatBeskrivning.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionAnnatBeskrivning.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}