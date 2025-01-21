/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_QUESTION_DESCRIPTION_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_QUESTION_TEXT_ID;

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
import se.inera.intyg.common.doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;

@ExtendWith(MockitoExtension.class)
class QuestionForgiftningOrsakTest {

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
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            assertEquals(FORGIFTNING_ORSAK_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionForgiftningOrsak.toCertificate(null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            assertEquals(FORGIFTNING_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FORGIFTNING_ORSAK_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeDescription() {
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getDescription().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FORGIFTNING_ORSAK_QUESTION_DESCRIPTION_ID);
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigType() {
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            assertEquals(CertificateDataConfigType.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueOlycksfall() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(ForgiftningOrsak.OLYCKSFALL.name())
                .label(ForgiftningOrsak.OLYCKSFALL.getBeskrivning())
                .build();
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(0));
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueSjalvmord() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(ForgiftningOrsak.SJALVMORD.name())
                .label(ForgiftningOrsak.SJALVMORD.getBeskrivning())
                .build();
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(1));
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueAvsiktligtVallad() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(ForgiftningOrsak.AVSIKTLIGT_VALLAD.name())
                .label(ForgiftningOrsak.AVSIKTLIGT_VALLAD.getBeskrivning())
                .build();
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(2));
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueOklart() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(ForgiftningOrsak.OKLART.name())
                .label(ForgiftningOrsak.OKLART.getBeskrivning())
                .build();
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(3));
        }

        @Test
        void shouldIncludeCodeValueType() {
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValueType.CODE, question.getValue().getType());
        }

        @Test
        void shouldIncludeCodeValueId() {
            final var question = QuestionForgiftningOrsak.toCertificate(ForgiftningOrsak.OLYCKSFALL, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertEquals(ForgiftningOrsak.OLYCKSFALL.name(), certificateDataValueCode.getId());
        }

        @Test
        void shouldIncludeCodeValue() {
            final var question = QuestionForgiftningOrsak.toCertificate(ForgiftningOrsak.OLYCKSFALL, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertEquals(ForgiftningOrsak.OLYCKSFALL.name(), certificateDataValueCode.getCode());
        }

        @Test
        void shouldIncludeCodeValueEmpty() {
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertNull(certificateDataValueCode.getCode());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(FORGIFTNING_ORSAK_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var expectedExpression = "exists(" + ForgiftningOrsak.OLYCKSFALL.name()
                + ") || exists(" + ForgiftningOrsak.SJALVMORD.name()
                + ") || exists(" + ForgiftningOrsak.AVSIKTLIGT_VALLAD.name()
                + ") || exists(" + ForgiftningOrsak.OKLART.name() + ")";

            final var question = QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(expectedExpression, certificateDataValidationMandatory.getExpression());
        }

        @Nested
        class IncludeValidationShowTest extends ValidationShowTest {

            @Override
            protected String getQuestionId() {
                return FORGIFTNING_OM_DELSVAR_ID;
            }

            @Override
            protected String getExpression() {
                return "$" + FORGIFTNING_OM_JSON_ID;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionForgiftningOrsak.toCertificate(null, 0, texts);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        Stream<ForgiftningOrsak> forgiftningsOrsakValues() {
            return Stream.of(
                ForgiftningOrsak.OLYCKSFALL,
                ForgiftningOrsak.SJALVMORD,
                ForgiftningOrsak.AVSIKTLIGT_VALLAD,
                ForgiftningOrsak.OKLART,
                null
            );
        }

        @ParameterizedTest
        @MethodSource("forgiftningsOrsakValues")
        void shouldIncludeTextValue(ForgiftningOrsak expectedValue) {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionForgiftningOrsak.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionForgiftningOrsak.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}
