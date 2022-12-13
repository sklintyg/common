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

package se.inera.intyg.common.sos_parent.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAD_DOD_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleAndExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.not;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.withCitation;

import java.time.LocalDate;
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
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionAntraffadDodTest {

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
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            assertEquals(ANTRAFFAT_DOD_DATUM_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionAntraffadDod.toCertificate(null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            assertEquals(DODSDATUM_SAKERT_DELSVAR_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(ANTRAFFAD_DOD_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeDateConfigType() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_DATE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeDateConfigValueId() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            final var certificateDataConfigDate = (CertificateDataConfigDate) question.getConfig();
            assertEquals(ANTRAFFAT_DOD_DATUM_JSON_ID, certificateDataConfigDate.getId());
        }

        @Test
        void shouldIncludeDateValueType() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValueType.DATE, question.getValue().getType());
        }

        @Test
        void shouldIncludeDateValueId() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            final var certificateDataValueDate = (CertificateDataValueDate) question.getValue();
            assertEquals(ANTRAFFAT_DOD_DATUM_JSON_ID, certificateDataValueDate.getId());
        }

        @Test
        void shouldIncludeDateValue() {
            final var expectedDateValue = LocalDate.now();
            final var question = QuestionAntraffadDod.toCertificate(expectedDateValue, 0, texts);
            final var certificateDataValueDate = (CertificateDataValueDate) question.getValue();
            assertEquals(expectedDateValue, certificateDataValueDate.getDate());
        }

        @Test
        void shouldIncludeDateValueEmpty() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            final var certificateDataValueDate = (CertificateDataValueDate) question.getValue();
            assertNull(certificateDataValueDate.getDate());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(ANTRAFFAT_DOD_DATUM_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals("$" + ANTRAFFAT_DOD_DATUM_JSON_ID, certificateDataValidationMandatory.getExpression());
        }

        @Nested
        class IncludeValidationShowTest extends ValidationShowTest {

            @Override
            protected String getQuestionId() {
                return DODSDATUM_SAKERT_DELSVAR_ID;
            }

            @Override
            protected String getExpression() {
                return multipleAndExpression(
                    exists(withCitation(DODSDATUM_SAKERT_JSON_ID)),
                    not(withCitation(DODSDATUM_SAKERT_JSON_ID))
                );
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionAntraffadDod.toCertificate(null, 0, texts);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }
        }

        @Test
        void shouldIncludeValidationMaxDateType() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.MAX_DATE_VALIDATION, question.getValidation()[2].getType());
        }

        @Test
        void shouldIncludeValidationMaxDateId() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[2];
            assertEquals(ANTRAFFAT_DOD_DATUM_JSON_ID, certificateDataValidationMaxDate.getId());
        }

        @Test
        void shouldIncludeValidationMaxDateNumberOfDays() {
            final var question = QuestionAntraffadDod.toCertificate(null, 0, texts);
            final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[2];
            assertEquals(0, certificateDataValidationMaxDate.getNumberOfDays());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        @Test
        void shouldIncludeValue() {
            final var expectedValue = LocalDate.now();

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionAntraffadDod.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionAntraffadDod.toInternal(certificate);

            assertEquals(new InternalDate(expectedValue), actualValue);
        }

        @Test
        void shouldIncludeValueNull() {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionAntraffadDod.toCertificate(null, 0, texts))
                .build();

            final var actualValue = QuestionAntraffadDod.toInternal(certificate);

            assertEquals(null, actualValue);
        }
    }
}