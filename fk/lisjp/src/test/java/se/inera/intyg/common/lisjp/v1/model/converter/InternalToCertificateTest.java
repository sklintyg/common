/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

@DisplayName("Should convert LisjpUtlatandeV1 to Certificate")
class InternalToCertificateTest {

    private GrundData grundData;
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class CategoryKontakt {

        private LisjpUtlatandeV1 internalCertificate;

        @BeforeEach
        void createInternalCertificateToConvert() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

        }

        @Test
        void shouldIncludeCategoryElement() {
            final var expectedIndex = 31;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var category = certificate.getData().get(KONTAKT_CATEGORY_ID);

            assertAll("Validating category",
                () -> assertEquals(KONTAKT_CATEGORY_ID, category.getId()),
                () -> assertEquals(expectedIndex, category.getIndex()),
                () -> assertNull(category.getParent(), "Should not contain a parent"),
                () -> assertNull(category.getValue(), "Should not contain a value"),
                () -> assertNotNull(category.getValidation(), "Missing validation"),
                () -> assertNotNull(category.getConfig(), "Should include config")
            );
        }

        @Test
        void shouldIncludeCategoryConfig() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var category = certificate.getData().get(KONTAKT_CATEGORY_ID);

            assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

            assertAll("Validating category configuration",
                () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text"),
                () -> assertNull(category.getConfig().getDescription(), "Should not contain description")
            );
        }

        @Test
        void shouldIncludeValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var category = certificate.getData().get(KONTAKT_CATEGORY_ID);

            final var certificateDataValidationHide = (CertificateDataValidationHide) category.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
            );
        }
    }

    @Nested
    class QuestionKontakt {

        private LisjpUtlatandeV1 internalCertificate;

        @BeforeEach
        void createInternalCertificateToConvert() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        @Test
        void shouldIncludeQuestionElement() {
            final var expectedIndex = 32;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_26);

            assertAll("Validating question",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_ID_26, question.getId()),
                () -> assertEquals(expectedIndex, question.getIndex()),
                () -> assertEquals(KONTAKT_CATEGORY_ID, question.getParent()),
                () -> assertNotNull(question.getValue(), "Missing value"),
                () -> assertNotNull(question.getValidation(), "Missing validation"),
                () -> assertNotNull(question.getConfig(), "Missing config")
            );
        }

        @Test
        void shouldIncludeQuestionConfig() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_26);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_BOOLEAN, question.getConfig().getType());

            final var certificateDataConfigCheckboxBoolean = (CertificateDataConfigCheckboxBoolean) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertTrue(certificateDataConfigCheckboxBoolean.getText().trim().length() > 0, "Missing text"),
                () -> assertTrue(certificateDataConfigCheckboxBoolean.getLabel().trim().length() > 0, "Missing label"),
                () -> assertTrue(certificateDataConfigCheckboxBoolean.getDescription().trim().length() > 0, "Missing description"),
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_JSON_ID_26, certificateDataConfigCheckboxBoolean.getId()),
                () -> assertTrue(certificateDataConfigCheckboxBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                () -> assertTrue(certificateDataConfigCheckboxBoolean.getUnselectedText().trim().length() > 0,
                    "Missing unselected text")
            );
        }

        @Test
        void shouldIncludeQuestionValueTrue() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setKontaktMedFk(true)
                .build();
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_26);

            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_JSON_ID_26, certificateDataValueBoolean.getId()),
                () -> assertEquals(internalCertificate.getKontaktMedFk(), certificateDataValueBoolean.getSelected())
            );
        }

        @Test
        void shouldIncludeQuestionValueFalse() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setAvstangningSmittskydd(false)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_26);

            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_JSON_ID_26, certificateDataValueBoolean.getId()),
                () -> assertEquals(internalCertificate.getKontaktMedFk(), certificateDataValueBoolean.getSelected())
            );
        }

        @Test
        void shouldIncludeQuestionValueEmpty() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_26);

            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_JSON_ID_26, certificateDataValueBoolean.getId()),
                () -> assertNull(certificateDataValueBoolean.getSelected())
            );
        }

        @Test
        void shouldIncludeValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_26);

            final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
            );
        }
    }

    @Nested
    class QuestionKontaktBeskrivning {

        private LisjpUtlatandeV1 internalCertificate;

        @BeforeEach
        void createInternalCertificateToConvert() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        @Test
        void shouldIncludeQuestionElement() {
            final var expectedIndex = 33;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26);

            assertAll("Validating question",
                () -> assertEquals(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26, question.getId()),
                () -> assertEquals(expectedIndex, question.getIndex()),
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_ID_26, question.getParent()),
                () -> assertNotNull(question.getValue(), "Missing value"),
                () -> assertNotNull(question.getValidation(), "Missing validation"),
                () -> assertNotNull(question.getConfig(), "Missing config")
            );
        }

        @Test
        void shouldIncludeQuestionConfig() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26);

            assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

            final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                () -> assertNull(certificateDataConfigTextArea.getDescription(), "Should not include description"),
                () -> assertNull(certificateDataConfigTextArea.getHeader(), "Should not include header"),
                () -> assertNull(certificateDataConfigTextArea.getLabel(), "Should not include label"),
                () -> assertNull(certificateDataConfigTextArea.getIcon(), "Should not include icon"),
                () -> assertEquals(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26, certificateDataConfigTextArea.getId())
            );
        }

        @Test
        void shouldIncludeQuestionValueText() {
            final var expectedText = "Text value for question";
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setAnledningTillKontakt(expectedText)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26);

            final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26, certificateDataValueText.getId()),
                () -> assertEquals(expectedText, certificateDataValueText.getText())
            );
        }

        @Test
        void shouldIncludeQuestionValueTextEmpty() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26);

            final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26, certificateDataValueText.getId()),
                () -> assertNull(certificateDataValueText.getText())
            );
        }

        @Test
        void shouldIncludeQuestionValidationShow() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26);

            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_ID_26, certificateDataValidationShow.getQuestionId()),
                () -> assertEquals("$" + KONTAKT_ONSKAS_SVAR_JSON_ID_26, certificateDataValidationShow.getExpression())
            );
        }
    }
}

