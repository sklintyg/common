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
package se.inera.intyg.common.db.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIVT_IMPLANTAT_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIVT_IMPLANTAT_QUESTION_SELECTED_TEXT;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIVT_IMPLANTAT_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIVT_IMPLANTAT_QUESTION_UNSELECTED_TEXT;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_JSON_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalBooleanValueTest;

@ExtendWith(MockitoExtension.class)
class QuestionExplosivtImplantatTest {

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
            final var question = QuestionExplosivtImplantat.toCertificate(true, 0, texts);
            assertEquals(EXPLOSIV_IMPLANTAT_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionExplosivtImplantat.toCertificate(true, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionExplosivtImplantat.toCertificate(true, 0, texts);
            assertEquals(EXPLOSIVT_IMPLANTAT_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionExplosivtImplantat.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(EXPLOSIVT_IMPLANTAT_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeSelectedText() {
            final var question = QuestionExplosivtImplantat.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(EXPLOSIVT_IMPLANTAT_QUESTION_SELECTED_TEXT);
        }

        @Test
        void shouldIncludeUnselectedText() {
            final var question = QuestionExplosivtImplantat.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(EXPLOSIVT_IMPLANTAT_QUESTION_UNSELECTED_TEXT);
        }

        @Test
        void shouldIncludeRadioBooleanConfigType() {
            final var question = QuestionExplosivtImplantat.toCertificate(true, 0, texts);
            assertEquals(CertificateDataConfigType.UE_RADIO_BOOLEAN, question.getConfig().getType());
        }

        @Test
        void shouldIncludeRadioBooleanConfigValueId() {
            final var question = QuestionExplosivtImplantat.toCertificate(true, 0, texts);
            final var certificateDataConfigRadioBoolean = (CertificateDataConfigRadioBoolean) question.getConfig();
            assertEquals(EXPLOSIV_IMPLANTAT_JSON_ID, certificateDataConfigRadioBoolean.getId());
        }

        @Test
        void shouldIncludeBooleanValueType() {
            final var question = QuestionExplosivtImplantat.toCertificate(true, 0, texts);
            assertEquals(CertificateDataValueType.BOOLEAN, question.getValue().getType());
        }

        @Test
        void shouldIncludeBooleanValueId() {
            final var question = QuestionExplosivtImplantat.toCertificate(null, 0, texts);
            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertEquals(EXPLOSIV_IMPLANTAT_JSON_ID, certificateDataValueBoolean.getId());
        }

        @Test
        void shouldIncludeBooleanValueTrue() {
            final var expectedBooleanValue = Boolean.TRUE;
            final var question = QuestionExplosivtImplantat.toCertificate(expectedBooleanValue, 0, texts);
            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertEquals(expectedBooleanValue, certificateDataValueBoolean.getSelected());
        }

        @Test
        void shouldIncludeBooleanValueFalse() {
            final var expectedBooleanValue = Boolean.FALSE;
            final var question = QuestionExplosivtImplantat.toCertificate(expectedBooleanValue, 0, texts);
            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertEquals(expectedBooleanValue, certificateDataValueBoolean.getSelected());
        }

        @Test
        void shouldIncludeBooleanValueEmpty() {
            final var question = QuestionExplosivtImplantat.toCertificate(null, 0, texts);
            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertNull(certificateDataValueBoolean.getSelected());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionExplosivtImplantat.toCertificate(true, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionExplosivtImplantat.toCertificate(true, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(EXPLOSIV_IMPLANTAT_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var question = QuestionExplosivtImplantat.toCertificate(true, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals("exists(" + EXPLOSIV_IMPLANTAT_JSON_ID + ")", certificateDataValidationMandatory.getExpression());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class IncludeInternalBooleanValueTest extends InternalBooleanValueTest {

            @Override
            protected CertificateDataElement getElement(Boolean expectedValue) {
                return QuestionExplosivtImplantat.toCertificate(expectedValue, 0, texts);
            }

            @Override
            protected Boolean toInternalBooleanValue(Certificate certificate) {
                return QuestionExplosivtImplantat.toInternal(certificate);
            }
        }
    }
}
