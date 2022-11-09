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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAKS_UPPGIFTER_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_QUESTION_TEXT_ID;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionGrunderDodsorsaksuppgifterTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ToCertificate {

        private List<Dodsorsaksgrund> dodsorsaksgrund;

        @BeforeEach
        void setUp() {
            dodsorsaksgrund = Collections.emptyList();
        }

        @Test
        void shouldIncludeId() {
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            assertEquals(GRUNDER_DELSVAR_ID, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            assertEquals(DODSORSAKS_UPPGIFTER_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(GRUNDER_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeCheckboxMultipleCodeConfigType() {
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeCheckboxMultipleCodeConfigValueUndersokningFore() {
            final var expectedCode = CheckboxMultipleCode.builder()
                .id(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN.name())
                .label(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN.getBeskrivning())
                .build();
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigCheckboxMultipleCode.getList().get(0));
        }

        @Test
        void shouldIncludeCheckboxMultipleCodeConfigValueYttreUndersokningEfter() {
            final var expectedCode = CheckboxMultipleCode.builder()
                .id(Dodsorsaksgrund.UNDERSOKNING_EFTER_DODEN.name())
                .label(Dodsorsaksgrund.UNDERSOKNING_EFTER_DODEN.getBeskrivning())
                .build();
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigCheckboxMultipleCode.getList().get(1));
        }

        @Test
        void shouldIncludeCheckboxMultipleCodeConfigValueKliniskObduktion() {
            final var expectedCode = CheckboxMultipleCode.builder()
                .id(Dodsorsaksgrund.KLINISK_OBDUKTION.name())
                .label(Dodsorsaksgrund.KLINISK_OBDUKTION.getBeskrivning())
                .build();
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigCheckboxMultipleCode.getList().get(2));
        }

        @Test
        void shouldIncludeCheckboxMultipleCodeConfigValueRattsmedicinskObduktion() {
            final var expectedCode = CheckboxMultipleCode.builder()
                .id(Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION.name())
                .label(Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION.getBeskrivning())
                .build();
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigCheckboxMultipleCode.getList().get(3));
        }

        @Test
        void shouldIncludeCheckboxMultipleCodeConfigValueRattsmedicinskLikbesiktning() {
            final var expectedCode = CheckboxMultipleCode.builder()
                .id(Dodsorsaksgrund.RATTSMEDICINSK_BESIKTNING.name())
                .label(Dodsorsaksgrund.RATTSMEDICINSK_BESIKTNING.getBeskrivning())
                .build();
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
            assertEquals(expectedCode, certificateDataConfigCheckboxMultipleCode.getList().get(4));
        }

        @Test
        void shouldIncludeCodeValueType() {
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            assertEquals(CertificateDataValueType.CODE_LIST, question.getValue().getType());
        }

        @Test
        void shouldIncludeCodeValueEmptyList() {
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var value = (CertificateDataValueCodeList) question.getValue();
            assertTrue(value.getList().isEmpty());
        }

        @Test
        void shouldIncludeCodeValueFilledList() {
            dodsorsaksgrund = List.of(Dodsorsaksgrund.KLINISK_OBDUKTION, Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION);
            final var expectedValueList = List.of(
                CertificateDataValueCode.builder()
                    .id(Dodsorsaksgrund.KLINISK_OBDUKTION.name())
                    .code(Dodsorsaksgrund.KLINISK_OBDUKTION.name())
                    .build(),
                CertificateDataValueCode.builder()
                    .id(Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION.name())
                    .code(Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION.name())
                    .build()
            );
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var value = (CertificateDataValueCodeList) question.getValue();
            assertEquals(expectedValueList, value.getList());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationMandatoryQuestionId() {
            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(GRUNDER_DELSVAR_ID, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var expectedExpression = "$" + Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN.name()
                + " || $" + Dodsorsaksgrund.UNDERSOKNING_EFTER_DODEN.name()
                + " || $" + Dodsorsaksgrund.KLINISK_OBDUKTION.name()
                + " || $" + Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION.name()
                + " || $" + Dodsorsaksgrund.RATTSMEDICINSK_BESIKTNING.name();

            final var question = QuestionGrunderDodsorsaksuppgifter.toCertificate(dodsorsaksgrund, 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertEquals(expectedExpression, certificateDataValidationMandatory.getExpression());
        }
    }
}