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
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DESCRIPTION;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_TEXT;

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
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionGrundForMUMotiveringTest {

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
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);

            assertEquals(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;

            final var question = QuestionGrundForMUMotivering.toCertificate(expectedIndex, texts, null);

            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);

            assertEquals(GRUNDFORMU_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeConfigCertificateDataConfigTextArea() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);

            assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());
        }

        @Test
        void shouldIncludeConfigId() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);
            final var config = (CertificateDataConfigTextArea) question.getConfig();

            assertEquals(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1, config.getId());
        }

        @Test
        void shouldIncludeConfigText() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);

            verify(texts, atLeastOnce()).get(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_TEXT);
        }

        @Test
        void shouldIncludeConfigDescription() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);

            verify(texts, atLeastOnce()).get(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DESCRIPTION);
        }

        @Test
        void shouldIncludeValueTypeText() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);

            assertEquals(CertificateDataValueType.TEXT, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueId() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);
            final var value = (CertificateDataTextValue) question.getValue();

            assertEquals(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1, value.getId());
        }

        @Test
        void shouldIncludeValueText() {
            final var expectedText = "Annan text";
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, expectedText);
            final var value = (CertificateDataTextValue) question.getValue();

            assertEquals(expectedText, value.getText());
        }

        @Test
        void shouldIncludeValidationShow() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);
            final var showValidation = (CertificateDataValidationShow) question.getValidation()[1];

            assertEquals(CertificateDataValidationType.SHOW_VALIDATION, showValidation.getType());
        }

        @Test
        void shouldIncludeValidationLimit() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);
            final var showValidation = (CertificateDataValidationText) question.getValidation()[0];

            assertEquals(CertificateDataValidationType.TEXT_VALIDATION, showValidation.getType());
        }

        @Test
        void shouldIncludeValidationHide() {
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);
            final var showValidation = (CertificateDataValidationHide) question.getValidation()[2];

            assertEquals(CertificateDataValidationType.HIDE_VALIDATION, showValidation.getType());
        }

        @Test
        void shouldIncludeValidationShowExpression() {
            final var expectedExpression =
                GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1 + " || " +
                    GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1 + " || "
                    + GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);
            final var showValidation = (CertificateDataValidationShow) question.getValidation()[1];

            assertEquals(expectedExpression, showValidation.getExpression());
        }

        @Test
        void shouldIncludeValidationTextLimit() {
            final var expectedLimit = 150;
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);
            final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[0];

            assertEquals(expectedLimit, certificateDataValidationText.getLimit());
        }

        @Test
        void shouldIncludeValidationHideExpression() {
            final var expectedExpression = "$" + GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
            final var question = QuestionGrundForMUMotivering.toCertificate(0, texts, null);
            final var hideExpression = (CertificateDataValidationHide) question.getValidation()[2];

            assertEquals(expectedExpression, hideExpression.getExpression());
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
                .addElement(QuestionGrundForMUMotivering.toCertificate(0, texts, expectedValue))
                .build();

            final var actualValue = QuestionGrundForMUMotivering.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}