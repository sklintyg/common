/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.af00213.v1.model.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;

import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;

@DisplayName("Should convert AF00213Utlatande to Certificate")
class InternalToCertificateTest {

    private GrundData grundData;
    @Mock
    private SortedMap<String, String> texts;

    @BeforeEach
    void setup() {
        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);

        texts = Mockito.mock(TreeMap.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class MetaData {

        private Af00213UtlatandeV1 internalCertificate;

        @Nested
        class CommonMetadata {

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCertificateId() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertAll("Validating metadata",
                    () -> assertEquals(internalCertificate.getId(), certificate.getMetadata().getId()),
                    () -> assertEquals(internalCertificate.getTyp(), certificate.getMetadata().getType()),
                    () -> assertEquals(internalCertificate.getTextVersion(), certificate.getMetadata().getTypeVersion()),
                    () -> assertEquals("Arbetsförmedlingens medicinska utlåtande", certificate.getMetadata().getName()),
                    () -> assertTrue(certificate.getMetadata().getDescription().trim().length() > 0, "Should contain description")
                );
            }
        }

        @Nested
        class ValidateUnit {

            private Vardenhet unit;

            @BeforeEach
            void createAf00213ToConvert() {
                unit = new Vardenhet();

                grundData.getSkapadAv().setVardenhet(unit);

                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shallIncludeUnitId() {
                final var expectedUnitId = "UnitID";
                unit.setEnhetsid(expectedUnitId);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitId, certificate.getMetadata().getUnit().getUnitId());
            }

            @Test
            void shallIncludeUnitName() {
                final var expectedUnitName = "UnitName";
                unit.setEnhetsnamn(expectedUnitName);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitName, certificate.getMetadata().getUnit().getUnitName());
            }

            @Test
            void shallIncludeUnitAddress() {
                final var expectedUnitAddress = "UnitAddress";
                unit.setPostadress(expectedUnitAddress);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitAddress, certificate.getMetadata().getUnit().getAddress());
            }

            @Test
            void shallIncludeUnitZipCode() {
                final var expectedUnitZipCode = "UnitZipCode";
                unit.setPostnummer(expectedUnitZipCode);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitZipCode, certificate.getMetadata().getUnit().getZipCode());
            }

            @Test
            void shallIncludeUnitCity() {
                final var expectedUnitCity = "UnitCity";
                unit.setPostort(expectedUnitCity);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitCity, certificate.getMetadata().getUnit().getCity());
            }

            @Test
            void shallIncludeUnitEmail() {
                final var expectedUnitEmail = "UnitEmail";
                unit.setEpost(expectedUnitEmail);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitEmail, certificate.getMetadata().getUnit().getEmail());
            }

            @Test
            void shallIncludeUnitPhoneNumber() {
                final var expectedUnitPhoneNumber = "UnitPhoneNumber";
                unit.setTelefonnummer(expectedUnitPhoneNumber);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitPhoneNumber, certificate.getMetadata().getUnit().getPhoneNumber());
            }
        }
    }

    @Nested
    @DisplayName("Should convert categories and questions")
    class DataElements {

        @Nested
        class CategoryFunktionsnedsattning {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 0;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID);

                assertAll("Validating category Funktionsnedsättning",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_CATEGORY_ID, category.getId()),
                    () -> assertEquals(expectedIndex, category.getIndex()),
                    () -> assertNull(category.getParent(), "Should not contain a parent"),
                    () -> assertNull(category.getValue(), "Should not contain a value"),
                    () -> assertNull(category.getValidation(), "Should not contain any validation"),
                    () -> assertNotNull(category.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeCategoryConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }
        }

        @Nested
        class QuestionHarFunktionsnedsattning {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarFunktionsnedsattning(true)
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 1;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);

                assertAll("Validating question HarFunktionsnedsättning",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue()),
                    () -> assertNotNull(question.getValidation()),
                    () -> assertNotNull(question.getConfig())
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigBoolean = (CertificateDataConfigBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigBoolean.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigBoolean.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataConfigBoolean.getId()),
                    () -> assertTrue(certificateDataConfigBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue(certificateDataConfigBoolean.getUnselectedText().trim().length() > 0, "Missing unselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarFunktionsnedsattning(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarFunktionsnedsattning(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarFunktionsnedsattning(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);

                assertEquals(1, question.getValidation().length);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValidationMandatory.getExpression())
                );
            }
        }

        @Nested
        class QuestionFunktionsnedsattning {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setFunktionsnedsattning("Text som beskriver funktionsnedsättningen.")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 2;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_12);

                assertAll("Validating question Funktionsnedsättning",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_12, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_12);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigBoolean = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigBoolean.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigBoolean.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12, certificateDataConfigBoolean.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_12);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12, certificateDataTextValue.getId()),
                    () -> assertEquals(internalCertificate.getFunktionsnedsattning(), certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_12);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_12);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_12, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_12);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValidationShow.getExpression())
                );
            }
        }

        @Nested
        class CategoryAktivitetsbegransning {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 3;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(AKTIVITETSBEGRANSNING_CATEGORY_ID);

                assertAll("Validating category Aktivitetsbegränsning",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_CATEGORY_ID, category.getId()),
                    () -> assertEquals(expectedIndex, category.getIndex()),
                    () -> assertNull(category.getParent(), "Should not contain a parent"),
                    () -> assertNull(category.getValue(), "Should not contain a value"),
                    () -> assertNotNull(category.getValidation(), "Should include Validation"),
                    () -> assertNotNull(category.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeCategoryConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(AKTIVITETSBEGRANSNING_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }

            @Test
            void shouldIncludeCategoryValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_CATEGORY_ID);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValidationShow.getExpression())
                );
            }
        }

        @Nested
        class QuestionHarAktivitetsbegransning {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarAktivitetsbegransning(true)
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 4;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                assertAll("Validating question HarAktivitetsbegränsning",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_DELSVAR_ID_21, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigBoolean = (CertificateDataConfigBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigBoolean.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigBoolean.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataConfigBoolean.getId()),
                    () -> assertTrue(certificateDataConfigBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue(certificateDataConfigBoolean.getUnselectedText().trim().length() > 0, "Missing unselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarAktivitetsbegransning(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarAktivitetsbegransning(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarAktivitetsbegransning(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_DELSVAR_ID_21, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValidationShow.getExpression())
                );
            }
        }

        @Nested
        class QuestionAktivitetsbegransning {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setAktivitetsbegransning("Text som beskriver Aktivitetsbegränsningen.")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 5;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);

                assertAll("Validating question Funktionsnedsättning",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_DELSVAR_ID_22, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_DELSVAR_ID_21, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigTextArea.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22, certificateDataTextValue.getId()),
                    () -> assertEquals(internalCertificate.getAktivitetsbegransning(), certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_DELSVAR_ID_22, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);

                final var certificateDataValidationShowOne = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, certificateDataValidationShowOne.getQuestionId()),
                    () -> assertEquals("$" + FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValidationShowOne.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShowTwo() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);

                final var certificateDataValidationShowTwo = (CertificateDataValidationShow) question.getValidation()[2];
                assertAll("Validation question validation",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_DELSVAR_ID_21, certificateDataValidationShowTwo.getQuestionId()),
                    () -> assertEquals("$" + AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValidationShowTwo.getExpression())
                );
            }
        }

        @Nested
        class CategoryUtredningOchBehandling {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 6;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(UTREDNING_BEHANDLING_CATEGORY_ID);

                assertAll("Validating category Utredning och behandling",
                    () -> assertEquals(UTREDNING_BEHANDLING_CATEGORY_ID, category.getId()),
                    () -> assertEquals(expectedIndex, category.getIndex()),
                    () -> assertNull(category.getParent(), "Should not contain a parent"),
                    () -> assertNull(category.getValue(), "Should not contain a value"),
                    () -> assertNull(category.getValidation(), "Should not contain any validation"),
                    () -> assertNotNull(category.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeCategoryConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(UTREDNING_BEHANDLING_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }
        }

        @Nested
        class QuestionHarUtredningOchBehandling {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarUtredningBehandling(true)
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 7;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_31);

                assertAll("Validating question HarUtredning/Behandling",
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_31, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(UTREDNING_BEHANDLING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_31);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigBoolean = (CertificateDataConfigBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigBoolean.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigBoolean.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_31, certificateDataConfigBoolean.getId()),
                    () -> assertTrue(certificateDataConfigBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue(certificateDataConfigBoolean.getUnselectedText().trim().length() > 0, "Missing unselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_31);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_31, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarUtredningBehandling(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarUtredningBehandling(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_31);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_31, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarUtredningBehandling(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_31);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_31, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_31);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_31, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + UTREDNING_BEHANDLING_SVAR_JSON_ID_31, certificateDataValidationMandatory.getExpression())
                );
            }
        }

        @Nested
        class QuestionUtredningOchBehandling {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setUtredningBehandling("Text som beskriver Utredning/Behandling.")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 8;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                assertAll("Validating question Funktionsnedsättning",
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_32, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_31, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigTextArea.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_32, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_32, certificateDataTextValue.getId()),
                    () -> assertEquals(internalCertificate.getUtredningBehandling(), certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_32, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_32, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + UTREDNING_BEHANDLING_SVAR_JSON_ID_32, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_31, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + UTREDNING_BEHANDLING_SVAR_JSON_ID_31, certificateDataValidationShow.getExpression())
                );
            }
        }

        @Nested
        class CategoryArbetspaverkan {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 9;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(ARBETETS_PAVERKAN_CATEGORY_ID);

                assertAll("Validating category Arbetspåverkan",
                    () -> assertEquals(ARBETETS_PAVERKAN_CATEGORY_ID, category.getId()),
                    () -> assertEquals(expectedIndex, category.getIndex()),
                    () -> assertNull(category.getParent(), "Should not contain a parent"),
                    () -> assertNull(category.getValue(), "Should not contain a value"),
                    () -> assertNull(category.getValidation(), "Should not contain any validation"),
                    () -> assertNotNull(category.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeCategoryConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(ARBETETS_PAVERKAN_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }
        }

        @Nested
        class QuestionHarArbetspaverkan {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarArbetetsPaverkan(true)
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 10;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_41);

                assertAll("Validating question HarUtredning/Behandling",
                    () -> assertEquals(ARBETETS_PAVERKAN_DELSVAR_ID_41, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(ARBETETS_PAVERKAN_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_41);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigBoolean = (CertificateDataConfigBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigBoolean.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigBoolean.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(ARBETETS_PAVERKAN_SVAR_JSON_ID_41, certificateDataConfigBoolean.getId()),
                    () -> assertTrue( certificateDataConfigBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue( certificateDataConfigBoolean.getUnselectedText().trim().length() > 0, "Missing unselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_41);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETETS_PAVERKAN_SVAR_JSON_ID_41, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarArbetetsPaverkan(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarArbetetsPaverkan(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_41);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETETS_PAVERKAN_SVAR_JSON_ID_41, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarArbetetsPaverkan(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_41);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETETS_PAVERKAN_SVAR_JSON_ID_41, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_41);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(ARBETETS_PAVERKAN_DELSVAR_ID_41, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + ARBETETS_PAVERKAN_SVAR_JSON_ID_41, certificateDataValidationMandatory.getExpression())
                );
            }
        }

        @Nested
        class QuestionArbetspaverkan {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetetsPaverkan("Text som beskriver Arbetspåverkan.")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 11;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_42);

                assertAll("Validating question Funktionsnedsättning",
                    () -> assertEquals(ARBETETS_PAVERKAN_DELSVAR_ID_42, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(ARBETETS_PAVERKAN_DELSVAR_ID_41, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_42);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigTextArea.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(ARBETETS_PAVERKAN_SVAR_JSON_ID_42, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_42);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETETS_PAVERKAN_SVAR_JSON_ID_42, certificateDataTextValue.getId()),
                    () -> assertEquals(internalCertificate.getArbetetsPaverkan(), certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_42);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETETS_PAVERKAN_SVAR_JSON_ID_42, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_42);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(ARBETETS_PAVERKAN_DELSVAR_ID_42, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + ARBETETS_PAVERKAN_SVAR_JSON_ID_42, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_42);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(ARBETETS_PAVERKAN_DELSVAR_ID_41, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + ARBETETS_PAVERKAN_SVAR_JSON_ID_41, certificateDataValidationShow.getExpression())
                );
            }
        }

        @Nested
        class CategoryOvrigt {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 12;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(OVRIGT_CATEGORY_ID);

                assertAll("Validating category Övrigt",
                    () -> assertEquals(OVRIGT_CATEGORY_ID, category.getId()),
                    () -> assertEquals(expectedIndex, category.getIndex()),
                    () -> assertNull(category.getParent(), "Should not contain a parent"),
                    () -> assertNull(category.getValue(), "Should not contain a value"),
                    () -> assertNull(category.getValidation(), "Should not contain any validation"),
                    () -> assertNotNull(category.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeCategoryConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(OVRIGT_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }
        }

        @Nested
        class QuestionOvrigt {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setOvrigt("Text som beskriver Övrigt")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 13;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(OVRIGT_DELSVAR_ID_5);

                assertAll("Validating question Övrigt",
                    () -> assertEquals(OVRIGT_DELSVAR_ID_5, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(OVRIGT_SVAR_JSON_ID_5, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNull(question.getValidation(), "Shouldn't include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(OVRIGT_DELSVAR_ID_5);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigTextArea.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(OVRIGT_SVAR_JSON_ID_5, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(OVRIGT_DELSVAR_ID_5);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(OVRIGT_CATEGORY_ID, certificateDataTextValue.getId()),
                    () -> assertEquals(internalCertificate.getOvrigt(), certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(OVRIGT_DELSVAR_ID_5);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(OVRIGT_SVAR_JSON_ID_5, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }
        }
    }
}
