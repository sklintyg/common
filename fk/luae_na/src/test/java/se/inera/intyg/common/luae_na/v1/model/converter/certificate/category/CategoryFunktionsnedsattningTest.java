/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationCategoryMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.validation.ExpressionTypeEnum;

@ExtendWith(MockitoExtension.class)
class CategoryFunktionsnedsattningTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Test
    void shouldIncludeId() {
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        assertEquals(FUNKTIONSNEDSATTNING_CATEGORY_ID, category.getId());
    }

    @Test
    void shouldIncludeIndex() {
        final var expectedIndex = 3;
        final var category = CategoryFunktionsnedsattning.toCertificate(expectedIndex, texts);
        assertEquals(expectedIndex, category.getIndex());
    }

    @Test
    void shouldIncludeCategoryText() {
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text");
        verify(texts, atLeastOnce()).get(FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID);
    }

    @Test
    void shouldIncludeCategoryDescription() {
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        assertTrue(category.getConfig().getDescription().trim().length() > 0, "Missing text");
        verify(texts, atLeastOnce()).get(FUNKTIONSNEDSATTNING_CATEGORY_DESCRIPTION_ID);
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryType() {
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        assertEquals(CertificateDataValidationType.CATEGORY_MANDATORY_VALIDATION, category.getValidation()[0].getType());
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryExpressionType() {
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        final var validationCategoryMandatory = (CertificateDataValidationCategoryMandatory) category.getValidation()[0];
        assertEquals(ExpressionTypeEnum.OR, validationCategoryMandatory.getExpressionType());
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryQuestionIntellektuell() {
        final var expectedMandatoryQuestion = CertificateDataValidationMandatory.builder()
            .questionId(FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8)
            .expression("$" + FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8)
            .build();
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        final var validationCategoryMandatory = (CertificateDataValidationCategoryMandatory) category.getValidation()[0];
        assertEquals(expectedMandatoryQuestion, validationCategoryMandatory.getQuestions().get(0));
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryQuestionKommunikation() {
        final var expectedMandatoryQuestion = CertificateDataValidationMandatory.builder()
            .questionId(FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9)
            .expression("$" + FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9)
            .build();
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        final var validationCategoryMandatory = (CertificateDataValidationCategoryMandatory) category.getValidation()[0];
        assertEquals(expectedMandatoryQuestion, validationCategoryMandatory.getQuestions().get(1));
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryQuestionKoncentration() {
        final var expectedMandatoryQuestion = CertificateDataValidationMandatory.builder()
            .questionId(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10)
            .expression("$" + FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10)
            .build();
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        final var validationCategoryMandatory = (CertificateDataValidationCategoryMandatory) category.getValidation()[0];
        assertEquals(expectedMandatoryQuestion, validationCategoryMandatory.getQuestions().get(2));
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryQuestionPsykisk() {
        final var expectedMandatoryQuestion = CertificateDataValidationMandatory.builder()
            .questionId(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11)
            .expression("$" + FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11)
            .build();
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        final var validationCategoryMandatory = (CertificateDataValidationCategoryMandatory) category.getValidation()[0];
        assertEquals(expectedMandatoryQuestion, validationCategoryMandatory.getQuestions().get(3));
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryQuestionSynHorselTal() {
        final var expectedMandatoryQuestion = CertificateDataValidationMandatory.builder()
            .questionId(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12)
            .expression("$" + FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12)
            .build();
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        final var validationCategoryMandatory = (CertificateDataValidationCategoryMandatory) category.getValidation()[0];
        assertEquals(expectedMandatoryQuestion, validationCategoryMandatory.getQuestions().get(4));
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryQuestionBalansKoordination() {
        final var expectedMandatoryQuestion = CertificateDataValidationMandatory.builder()
            .questionId(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13)
            .expression("$" + FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13)
            .build();
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        final var validationCategoryMandatory = (CertificateDataValidationCategoryMandatory) category.getValidation()[0];
        assertEquals(expectedMandatoryQuestion, validationCategoryMandatory.getQuestions().get(5));
    }

    @Test
    void shouldIncludeValidationCategoryMandatoryQuestionAnnan() {
        final var expectedMandatoryQuestion = CertificateDataValidationMandatory.builder()
            .questionId(FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14)
            .expression("$" + FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14)
            .build();
        final var category = CategoryFunktionsnedsattning.toCertificate(0, texts);
        final var validationCategoryMandatory = (CertificateDataValidationCategoryMandatory) category.getValidation()[0];
        assertEquals(expectedMandatoryQuestion, validationCategoryMandatory.getQuestions().get(6));
    }
}