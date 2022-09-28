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
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_OSAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigUncertainDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataUncertainDateValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionOsakertDodsdatumTest {

    @Mock
    private CertificateTextProvider texts;

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            assertEquals(DODSDATUM_OSAKERT_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionOsakertDodsdatum.toCertificate(null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            assertEquals(DODSDATUM_SAKERT_DELSVAR_ID, question.getParent());
        }

        @Test
        void shouldIncludeUncertainDateConfigType() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_UNCERTAIN_DATE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeUncertainDateConfigValueId() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataConfigUncertainDate = (CertificateDataConfigUncertainDate) question.getConfig();
            assertEquals(DODSDATUM_JSON_ID, certificateDataConfigUncertainDate.getId());
        }

        @Test
        void shouldIncludeUncertainDateConfigAllowedYears() {
            final var expectedAllowedYears = List.of("2022", "2021");
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataConfigUncertainDate = (CertificateDataConfigUncertainDate) question.getConfig();
            assertEquals(expectedAllowedYears, certificateDataConfigUncertainDate.getAllowedYears());
        }

        @Test
        void shouldIncludeUncertainDateConfigUnknownYear() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataConfigUncertainDate = (CertificateDataConfigUncertainDate) question.getConfig();
            assertEquals(true, certificateDataConfigUncertainDate.isUnknownYear());
        }

        @Test
        void shouldIncludeUncertainDateConfigUnknownMonth() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataConfigUncertainDate = (CertificateDataConfigUncertainDate) question.getConfig();
            assertEquals(true, certificateDataConfigUncertainDate.isUnknownMonth());
        }

        @Test
        void shouldIncludeUncertainDateValueType() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValueType.UNCERTAIN_DATE, question.getValue().getType());
        }

        @Test
        void shouldIncludeUncertainDateValueId() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataUncertainDateValue = (CertificateDataUncertainDateValue) question.getValue();
            assertEquals(DODSDATUM_JSON_ID, certificateDataUncertainDateValue.getId());
        }

        @Test
        void shouldIncludeUncertainDateValue() {
            final var expectedUncertainDateValue = "2021-11-00";
            final var question = QuestionOsakertDodsdatum.toCertificate(expectedUncertainDateValue, 0, texts);
            final var certificateDataUncertainDateValue = (CertificateDataUncertainDateValue) question.getValue();
            assertEquals(expectedUncertainDateValue, certificateDataUncertainDateValue.getValue());
        }

        @Test
        void shouldIncludeUncertainDateValueEmpty() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataUncertainDateValue = (CertificateDataUncertainDateValue) question.getValue();
            assertNull(certificateDataUncertainDateValue.getValue());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(DODSDATUM_OSAKERT_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals("$" + DODSDATUM_JSON_ID, certificateDataValidationMandatory.getExpression());
        }

        @Test
        void shouldIncludeValidationShowType() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            assertEquals(CertificateDataValidationType.SHOW_VALIDATION, question.getValidation()[1].getType());
        }

        @Test
        void shouldIncludeValidationShowQuestionId() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
            assertEquals(DODSDATUM_SAKERT_DELSVAR_ID, certificateDataValidationShow.getQuestionId());
        }

        @Test
        void shouldIncludeValidationShowExpression() {
            final var question = QuestionOsakertDodsdatum.toCertificate(null, 0, texts);
            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
            assertEquals("$" + DODSDATUM_SAKERT_JSON_ID + " == false", certificateDataValidationShow.getExpression());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        @Test
        void shouldIncludeValueUncertainDay() {
            final var expectedValue = "2022-11-00";

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionOsakertDodsdatum.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionOsakertDodsdatum.toInternal(certificate);

            assertEquals(new InternalDate(expectedValue), actualValue);
        }

        @Test
        void shouldIncludeValueUncertainMonth() {
            final var expectedValue = "2022-00-00";

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionOsakertDodsdatum.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionOsakertDodsdatum.toInternal(certificate);

            assertEquals(new InternalDate(expectedValue), actualValue);
        }

        @Test
        void shouldIncludeValueUncertainYear() {
            final var expectedValue = "0000-00-00";

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionOsakertDodsdatum.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionOsakertDodsdatum.toInternal(certificate);

            assertEquals(new InternalDate(expectedValue), actualValue);
        }

        @Test
        void shouldIncludeValueNull() {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionOsakertDodsdatum.toCertificate(null, 0, texts))
                .build();

            final var actualValue = QuestionOsakertDodsdatum.toInternal(certificate);

            assertEquals(null, actualValue);
        }
    }
}