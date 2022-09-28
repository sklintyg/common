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

package se.inera.intyg.common.db.v1.model.converter.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_DELSVAR_ID;

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
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionUndersokningYttreTest {

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
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            assertEquals(UNDERSOKNING_YTTRE_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionUndersokningYttre.toCertificate(null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            assertEquals(UNDERSOKNING_YTTRE_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigType() {
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueJa() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(Undersokning.JA.name())
                .label(Undersokning.JA.getBeskrivning())
                .build();
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(0));
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueSkaGoras() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(Undersokning.UNDERSOKNING_SKA_GORAS.name())
                .label(Undersokning.UNDERSOKNING_SKA_GORAS.getBeskrivning())
                .build();
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(1));
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueKortForeDoden() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN.name())
                .label(Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN.getBeskrivning())
                .build();
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(2));
        }

        @Test
        void shouldIncludeCodeValueType() {
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValueType.CODE, question.getValue().getType());
        }

        @Test
        void shouldIncludeCodeValueId() {
            final var question = QuestionUndersokningYttre.toCertificate(Undersokning.JA, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertEquals(Undersokning.JA.name(), certificateDataValueCode.getId());
        }

        @Test
        void shouldIncludeCodeValue() {
            final var question = QuestionUndersokningYttre.toCertificate(Undersokning.JA, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertEquals(Undersokning.JA.name(), certificateDataValueCode.getCode());
        }

        @Test
        void shouldIncludeCodeValueEmpty() {
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertNull(certificateDataValueCode.getCode());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(UNDERSOKNING_YTTRE_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var expectedExpression = "$" + Undersokning.JA.name()
                + " || $" + Undersokning.UNDERSOKNING_SKA_GORAS.name()
                + " || $" + Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN.name();

            final var question = QuestionUndersokningYttre.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(expectedExpression, certificateDataValidationMandatory.getExpression());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        Stream<Undersokning> undersokningYttreValues() {
            return Stream.of(
                Undersokning.JA,
                Undersokning.UNDERSOKNING_SKA_GORAS,
                Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN,
                null
            );
        }

        @ParameterizedTest
        @MethodSource("undersokningYttreValues")
        void shouldIncludeTextValue(Undersokning expectedValue) {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionUndersokningYttre.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionUndersokningYttre.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}