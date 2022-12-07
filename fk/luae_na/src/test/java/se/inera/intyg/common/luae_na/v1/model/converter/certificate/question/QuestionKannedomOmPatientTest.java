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
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.KANNEDOM_SVAR_JSON_ID_2;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.KANNEDOM_SVAR_TEXT;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionKannedomOmPatientTest {

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
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);

            assertEquals(KANNEDOM_SVAR_ID_2, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;

            final var question = QuestionKannedomOmPatient.toCertificate(null, expectedIndex, texts);

            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);

            assertEquals(GRUNDFORMU_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeConfigCertificateDataConfigDate() {
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);

            assertEquals(CertificateDataConfigTypes.UE_DATE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeConfigId() {
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);
            final var config = (CertificateDataConfigDate) question.getConfig();

            assertEquals(KANNEDOM_SVAR_JSON_ID_2, config.getId());
        }

        @Test
        void shouldIncludeConfigText() {
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);

            verify(texts, atLeastOnce()).get(KANNEDOM_SVAR_TEXT);
        }

        @Test
        void shouldIncludeValueTypeDate() {
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);

            assertEquals(CertificateDataValueType.DATE, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueId() {
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);
            final var value = (CertificateDataValueDate) question.getValue();

            assertEquals(KANNEDOM_SVAR_JSON_ID_2, value.getId());
        }

        @Test
        void shouldIncludeValueDate() {
            final var expectedText = new InternalDate(LocalDate.now());
            final var question = QuestionKannedomOmPatient.toCertificate(expectedText, 0, texts);
            final var value = (CertificateDataValueDate) question.getValue();

            assertEquals(expectedText.asLocalDate(), value.getDate());
        }

        @Test
        void shouldIncludeValidationMandatory() {
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);
            final var certificateDataValidationText = question.getValidation()[0];

            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, certificateDataValidationText.getType());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var expectedExpression = "$" + KANNEDOM_SVAR_JSON_ID_2;
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);
            final var certificateDataValidationText = (CertificateDataValidationMandatory) question.getValidation()[0];

            assertEquals(expectedExpression, certificateDataValidationText.getExpression());
        }

        @Test
        void shouldIncludeValidationMaxDate() {
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);
            final var certificateDataValidationText = question.getValidation()[1];

            assertEquals(CertificateDataValidationType.MAX_DATE_VALIDATION, certificateDataValidationText.getType());
        }

        @Test
        void shouldIncludeValidationMaxDateLimit() {
            final var expectedLimit = (short) 0;
            final var question = QuestionKannedomOmPatient.toCertificate(null, 0, texts);
            final var certificateDataValidationText = (CertificateDataValidationMaxDate) question.getValidation()[1];

            assertEquals(expectedLimit, certificateDataValidationText.getNumberOfDays());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        @Test
        void shouldIncludeValue() {
            final var expectedValue = LocalDate.now();

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionKannedomOmPatient.toCertificate(new InternalDate(expectedValue), 0, texts))
                .build();

            final var actualValue = QuestionKannedomOmPatient.toInternal(certificate);

            assertEquals(new InternalDate(expectedValue), actualValue);
        }

        @Test
        void shouldIncludeValueNull() {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionKannedomOmPatient.toCertificate(null, 0, texts))
                .build();

            final var actualValue = QuestionKannedomOmPatient.toInternal(certificate);

            assertEquals(null, actualValue);
        }
    }
}