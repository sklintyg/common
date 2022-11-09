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
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_QUESTION_SELECTED_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_QUESTION_UNKNOWN_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_QUESTION_UNSELECTED_TEXT_ID;

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
import se.inera.intyg.common.doi.model.internal.OmOperation;
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
class QuestionOperationTest {

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
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            assertEquals(OPERATION_OM_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionOperation.toCertificate(null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            assertEquals(OPERATION_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(OPERATION_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigType() {
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueJa() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(OmOperation.JA.name())
                .label("Test string")
                .build();
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            verify(texts, atLeastOnce()).get(OPERATION_QUESTION_SELECTED_TEXT_ID);
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(0));
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueNej() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(OmOperation.NEJ.name())
                .label("Test string")
                .build();
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            verify(texts, atLeastOnce()).get(OPERATION_QUESTION_UNSELECTED_TEXT_ID);
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(1));
        }

        @Test
        void shouldIncludeRadioMultipleCodeConfigValueUppgiftSaknas() {
            final var expectedCode = RadioMultipleCode.builder()
                .id(OmOperation.UPPGIFT_SAKNAS.name())
                .label("Test string")
                .build();
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            final var certificateDataConfigRadioMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            verify(texts, atLeastOnce()).get(OPERATION_QUESTION_UNKNOWN_TEXT_ID);
            assertEquals(expectedCode, certificateDataConfigRadioMultipleCode.getList().get(2));
        }

        @Test
        void shouldIncludeCodeValueType() {
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValueType.CODE, question.getValue().getType());
        }

        @Test
        void shouldIncludeCodeValueJa() {
            final var expected = CertificateDataValueCode.builder()
                .id(OmOperation.JA.name())
                .code(OmOperation.JA.name())
                .build();
            final var question = QuestionOperation.toCertificate(OmOperation.JA, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertEquals(expected, certificateDataValueCode);
        }

        @Test
        void shouldIncludeCodeValueNej() {
            final var expected = CertificateDataValueCode.builder()
                .id(OmOperation.NEJ.name())
                .code(OmOperation.NEJ.name())
                .build();
            final var question = QuestionOperation.toCertificate(OmOperation.NEJ, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertEquals(expected, certificateDataValueCode);
        }

        @Test
        void shouldIncludeCodeValueUppgiftSaknas() {
            final var expected = CertificateDataValueCode.builder()
                .id(OmOperation.UPPGIFT_SAKNAS.name())
                .code(OmOperation.UPPGIFT_SAKNAS.name())
                .build();
            final var question = QuestionOperation.toCertificate(OmOperation.UPPGIFT_SAKNAS, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertEquals(expected, certificateDataValueCode);
        }

        @Test
        void shouldIncludeCodeValueEmpty() {
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertNull(certificateDataValueCode.getCode());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionOperation.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(OPERATION_OM_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var expectedExpression = "$" + OmOperation.JA.name()
                + " || $" + OmOperation.NEJ.name()
                + " || $" + OmOperation.UPPGIFT_SAKNAS.name();

            final var question = QuestionOperation.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(expectedExpression, certificateDataValidationMandatory.getExpression());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        Stream<OmOperation> undersokningYttreValues() {
            return Stream.of(
                OmOperation.JA,
                OmOperation.NEJ,
                OmOperation.UPPGIFT_SAKNAS,
                null
            );
        }

        @ParameterizedTest
        @MethodSource("undersokningYttreValues")
        void shouldIncludeTextValue(OmOperation expectedValue) {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionOperation.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionOperation.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}
