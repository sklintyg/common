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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_TEXT_ID;

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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionFunktionsnedsattningKoncentrationTest {

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
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertEquals(FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertEquals(FUNKTIONSNEDSATTNING_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeConfigCertificateDataConfigTextfield() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());
        }

        @Test
        void shouldIncludeConfigId() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            final var config = (CertificateDataConfigTextArea) question.getConfig();
            assertEquals(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10, config.getId());
        }

        @Test
        void shouldIncludeConfigText() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FUNKTIONSNEDSATTNING_KONCENTRATION_TEXT_ID);
        }

        @Test
        void shouldIncludeConfigDescription() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getDescription().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FUNKTIONSNEDSATTNING_KONCENTRATION_DESCRIPTION_ID);
        }

        @Test
        void shouldIncludeConfigAccordionOpenText() {
            final var expectedOpenText = "Visa fritextfältet";
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertEquals(expectedOpenText, question.getConfig().getAccordion().getOpenText());
        }

        @Test
        void shouldIncludeConfigAccordionClosedText() {
            final var expectedOpenText = "Dölj fritextfältet";
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertEquals(expectedOpenText, question.getConfig().getAccordion().getCloseText());
        }

        @Test
        void shouldIncludeConfigAccordionHeader() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getAccordion().getHeader().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_TEXT_ID);
        }

        @Test
        void shouldIncludeValueTypeText() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValueType.TEXT, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueId() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            final var value = (CertificateDataTextValue) question.getValue();
            assertEquals(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10, value.getId());
        }

        @Test
        void shouldIncludeValueText() {
            final var expectedText = "Annan text";
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(expectedText, 0, texts);
            final var value = (CertificateDataTextValue) question.getValue();
            assertEquals(expectedText, value.getText());
        }

        @Test
        void shouldIncludeValidationText() {
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.TEXT_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationTextLimit() {
            final var expectedLimit = 4000;
            final var question = QuestionFunktionsnedsattningKoncentration.toCertificate(null, 0, texts);
            final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[0];
            assertEquals(expectedLimit, certificateDataValidationText.getLimit());
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
                .addElement(QuestionFunktionsnedsattningKoncentration.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionFunktionsnedsattningKoncentration.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}