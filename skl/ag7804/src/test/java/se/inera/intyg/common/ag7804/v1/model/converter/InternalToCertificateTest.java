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

package se.inera.intyg.common.ag7804.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;

import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;

@DisplayName("Should convert Ag7804UtlatandeV1 to Certificate")
class InternalToCertificateTest {

    private GrundData grundData;
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class MetaData {

        private Ag7804UtlatandeV1 internalCertificate;

        @Nested
        class CommonMetadata {

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion 1")
                    .build();

            }

            @Test
            void shouldIncludeCertificateId() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(internalCertificate.getId(), certificate.getMetadata().getId());
            }

            @Test
            void shouldIncludeCertificateType() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(internalCertificate.getTyp(), certificate.getMetadata().getType());
            }

            @Test
            void shouldIncludeCertificateTypeVersion() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(internalCertificate.getTextVersion(), certificate.getMetadata().getTypeVersion());
            }

            @Test
            void shouldIncludeCertificateName() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals("Läkarintyg om arbetsförmåga - arbetsgivaren", certificate.getMetadata().getName());
            }

            @Test
            void shouldIncludeCertificateDescription() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertTrue(certificate.getMetadata().getDescription().trim().length() > 0, "Should contain description");
            }
        }

        @Nested
        class ValidateUnit {

            private Vardenhet unit;

            @BeforeEach
            void createInternalCertificateToConvert() {
                unit = new Vardenhet();

                grundData.getSkapadAv().setVardenhet(unit);

                internalCertificate = Ag7804UtlatandeV1.builder()
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
        class CategorySmittbararpenning {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 0;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(AVSTANGNING_SMITTSKYDD_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_CATEGORY_ID, category.getId()),
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

                final var category = certificate.getData().get(AVSTANGNING_SMITTSKYDD_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }
        }

        @Nested
        class QuestionAvstangningSmittbararpenning {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setAvstangningSmittskydd(true)
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 1;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AVSTANGNING_SMITTSKYDD_SVAR_ID_27);

                assertAll("Validating question",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNull(question.getValidation(), "Shouldn't have validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AVSTANGNING_SMITTSKYDD_SVAR_ID_27);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigCheckboxBoolean = (CertificateDataConfigCheckboxBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigCheckboxBoolean.getLabel().trim().length() > 0, "Missing label"),
                    () -> assertNull(certificateDataConfigCheckboxBoolean.getText(), "Shouldnt have text"),
                    () -> assertNull(certificateDataConfigCheckboxBoolean.getDescription(), "Shouldnt have description"),
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataConfigCheckboxBoolean.getId()),
                    () -> assertTrue(certificateDataConfigCheckboxBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue(certificateDataConfigCheckboxBoolean.getUnselectedText().trim().length() > 0,
                        "Missing unselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AVSTANGNING_SMITTSKYDD_SVAR_ID_27);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getAvstangningSmittskydd(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setAvstangningSmittskydd(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AVSTANGNING_SMITTSKYDD_SVAR_ID_27);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getAvstangningSmittskydd(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AVSTANGNING_SMITTSKYDD_SVAR_ID_27);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }
        }

        @Nested
        class CategoryGrundForMU {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 2;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(GRUNDFORMU_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(GRUNDFORMU_CATEGORY_ID, category.getId()),
                    () -> assertEquals(expectedIndex, category.getIndex()),
                    () -> assertNull(category.getParent(), "Should not contain a parent"),
                    () -> assertNull(category.getValue(), "Should not contain a value"),
                    () -> assertNotNull(category.getValidation(), "Should include validation"),
                    () -> assertNotNull(category.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeCategoryConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(GRUNDFORMU_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }

            @Test
            void shouldIncludeCategoryValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMU_CATEGORY_ID);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionIntygetBaseratPa {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 3;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                assertAll("Validating question",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(GRUNDFORMU_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfigUndersokningPatient() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleDate = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(certificateDataConfigCheckboxMultipleDate.getList().get(0).getId(),
                        GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleDate.getList().get(0).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigTelefonkontakt() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleDate = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(certificateDataConfigCheckboxMultipleDate.getList().get(1).getId(),
                        GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleDate.getList().get(1).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigJournaluppgifter() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleDate = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(certificateDataConfigCheckboxMultipleDate.getList().get(2).getId(),
                        GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleDate.getList().get(2).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigAnnat() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleDate = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(certificateDataConfigCheckboxMultipleDate.getList().get(3).getId(),
                        GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleDate.getList().get(3).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionValueUndersokningPatienten() {
                final var expectedDate = new InternalDate(LocalDate.now());
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setUndersokningAvPatienten(expectedDate)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
                        certificateDataValueDateList.getList().get(0).getId()),
                    () -> assertEquals(expectedDate.asLocalDate(), certificateDataValueDateList.getList().get(0).getDate())
                );
            }

            @Test
            void shouldIncludeQuestionValueTelefonkontakt() {
                final var expectedDate = new InternalDate(LocalDate.now());
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setTelefonkontaktMedPatienten(expectedDate)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1,
                        certificateDataValueDateList.getList().get(0).getId()),
                    () -> assertEquals(expectedDate.asLocalDate(), certificateDataValueDateList.getList().get(0).getDate())
                );
            }

            @Test
            void shouldIncludeQuestionValueJournaluppgifter() {
                final var expectedDate = new InternalDate(LocalDate.now());
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setJournaluppgifter(expectedDate)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1,
                        certificateDataValueDateList.getList().get(0).getId()),
                    () -> assertEquals(expectedDate.asLocalDate(), certificateDataValueDateList.getList().get(0).getDate())
                );
            }

            @Test
            void shouldIncludeQuestionValueAnnat() {
                final var expectedDate = new InternalDate(LocalDate.now());
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setAnnatGrundForMU(expectedDate)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1,
                        certificateDataValueDateList.getList().get(0).getId()),
                    () -> assertEquals(expectedDate.asLocalDate(), certificateDataValueDateList.getList().get(0).getDate())
                );
            }

            @Test
            void shouldIncludeQuestionValueAllOfThem() {
                final var expectedDateKontakt = new InternalDate(LocalDate.now());
                final var expectedDateTelefon = new InternalDate(LocalDate.now().minusDays(1));
                final var expectedDateJournal = new InternalDate(LocalDate.now().minusDays(2));
                final var expectedDateAnnat = new InternalDate(LocalDate.now().minusDays(3));
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setUndersokningAvPatienten(expectedDateKontakt)
                    .setTelefonkontaktMedPatienten(expectedDateTelefon)
                    .setJournaluppgifter(expectedDateJournal)
                    .setAnnatGrundForMU(expectedDateAnnat)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
                        certificateDataValueDateList.getList().get(0).getId()),
                    () -> assertEquals(expectedDateKontakt.asLocalDate(), certificateDataValueDateList.getList().get(0).getDate()),
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1,
                        certificateDataValueDateList.getList().get(1).getId()),
                    () -> assertEquals(expectedDateTelefon.asLocalDate(), certificateDataValueDateList.getList().get(1).getDate()),
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1,
                        certificateDataValueDateList.getList().get(2).getId()),
                    () -> assertEquals(expectedDateJournal.asLocalDate(), certificateDataValueDateList.getList().get(2).getDate()),
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1,
                        certificateDataValueDateList.getList().get(3).getId()),
                    () -> assertEquals(expectedDateAnnat.asLocalDate(), certificateDataValueDateList.getList().get(3).getDate())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1
                            + " || $" + GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1
                            + " || $" + GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1
                            + " || $" + GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1,
                        certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMaxDateUndersokning() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
                        certificateDataValidationMaxDate.getId()),
                    () -> assertEquals(0, certificateDataValidationMaxDate.getNumberOfDays())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMaxDateTelefon() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[2];
                assertAll("Validation question validation",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1,
                        certificateDataValidationMaxDate.getId()),
                    () -> assertEquals(0, certificateDataValidationMaxDate.getNumberOfDays())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMaxDateJournal() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[3];
                assertAll("Validation question validation",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1,
                        certificateDataValidationMaxDate.getId()),
                    () -> assertEquals(0, certificateDataValidationMaxDate.getNumberOfDays())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMaxDateAnnat() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[4];
                assertAll("Validation question validation",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1,
                        certificateDataValidationMaxDate.getId()),
                    () -> assertEquals(0, certificateDataValidationMaxDate.getNumberOfDays())
                );
            }

            @Test
            void shouldIncludeCategoryValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[5];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionAnnatGrundFor {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 4;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);

                assertAll("Validating question",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(certificateDataConfigTextArea.getDescription(), "Shouldnt have description"),
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setAnnatGrundForMUBeskrivning(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, certificateDataTextValue.getId()),
                    () -> assertEquals(expectedText, certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueTextEmpty() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1,
                        certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, certificateDataValidationShow.getExpression())
                );
            }

            @Test
            void shouldIncludeCategoryValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[2];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class CategorySysselsattning {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 5;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(SYSSELSATTNING_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(SYSSELSATTNING_CATEGORY_ID, category.getId()),
                    () -> assertEquals(expectedIndex, category.getIndex()),
                    () -> assertNull(category.getParent(), "Should not contain a parent"),
                    () -> assertNull(category.getValue(), "Should not contain a value"),
                    () -> assertNotNull(category.getValidation(), "Should include validation"),
                    () -> assertNotNull(category.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeCategoryConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(SYSSELSATTNING_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }

            @Test
            void shouldIncludeCategoryValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(SYSSELSATTNING_CATEGORY_ID);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27,
                        certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionSysselsattning {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 6;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                assertAll("Validating question",
                    () -> assertEquals(TYP_AV_SYSSELSATTNING_SVAR_ID_28, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(SYSSELSATTNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getDescription().trim().length() > 0, "Missing description")
                );
            }

            @Test
            void shouldIncludeQuestionConfigNuvarandeArbete() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(SysselsattningsTyp.NUVARANDE_ARBETE.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(0).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(0).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigArbetssokande() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(SysselsattningsTyp.ARBETSSOKANDE.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(1).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(1).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigForaldraledig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(2).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(2).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigStudier() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(SysselsattningsTyp.STUDIER.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(3).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(3).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionValueNuvarandeArbete() {
                final var expectedSysselsattning = Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE);
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSysselsattning(Arrays.asList(expectedSysselsattning))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedSysselsattning.getTyp().getId(),
                        certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedSysselsattning.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueArbetssokande() {
                final var expectedSysselsattning = Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE);
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSysselsattning(Arrays.asList(expectedSysselsattning))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedSysselsattning.getTyp().getId(),
                        certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedSysselsattning.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueForaldraledig() {
                final var expectedSysselsattning = Sysselsattning.create(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN);
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSysselsattning(Arrays.asList(expectedSysselsattning))
                    .build();

                final var certificate = InternalToCertificate
                    .convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedSysselsattning.getTyp().getId(),
                        certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedSysselsattning.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueStudier() {
                final var expectedSysselsattning = Sysselsattning.create(SysselsattningsTyp.STUDIER);
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSysselsattning(Arrays.asList(expectedSysselsattning))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedSysselsattning.getTyp().getId(),
                        certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedSysselsattning.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueAllOfThem() {
                final var expectedSysselsattning = Arrays.asList(
                    Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE),
                    Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                    Sysselsattning.create(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN),
                    Sysselsattning.create(SysselsattningsTyp.STUDIER)
                );
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSysselsattning(expectedSysselsattning)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedSysselsattning.get(0).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedSysselsattning.get(0).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(0).getCode()),
                    () -> assertEquals(expectedSysselsattning.get(1).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(1).getId()),
                    () -> assertEquals(expectedSysselsattning.get(1).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(1).getCode()),
                    () -> assertEquals(expectedSysselsattning.get(2).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(2).getId()),
                    () -> assertEquals(expectedSysselsattning.get(2).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(2).getCode()),
                    () -> assertEquals(expectedSysselsattning.get(3).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(3).getId()),
                    () -> assertEquals(expectedSysselsattning.get(3).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(3).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(TYP_AV_SYSSELSATTNING_SVAR_ID_28, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$NUVARANDE_ARBETE || $ARBETSSOKANDE || $FORALDRALEDIG || $STUDIER",
                        certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27,
                        certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionSysselsattningYrkeQuestion {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 7;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                assertAll("Validating question",
                    () -> assertEquals(NUVARANDE_ARBETE_SVAR_ID_29, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(se.inera.intyg.common.fkparent.model.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID,
                        question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(certificateDataConfigTextArea.getDescription(), "Shouldnt have description"),
                    () -> assertEquals(NUVARANDE_ARBETE_SVAR_JSON_ID_29, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setNuvarandeArbete(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(NUVARANDE_ARBETE_SVAR_JSON_ID_29, certificateDataValueText.getId()),
                    () -> assertEquals(expectedText, certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueTextEmpty() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(NUVARANDE_ARBETE_SVAR_JSON_ID_29, certificateDataValueText.getId()),
                    () -> assertNull(certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(NUVARANDE_ARBETE_SVAR_ID_29, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + NUVARANDE_ARBETE_SVAR_JSON_ID_29,
                        certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28,
                        certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals(
                        "$" + se.inera.intyg.common.lisjp.model.internal.Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE.getId(),
                        certificateDataValidationShow.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[2];
                assertAll("Validation question validation",
                    () -> assertEquals(se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27,
                        certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals(
                        "$" + se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27,
                        certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class CategoryDiagnoses {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeCategoryElement() {
                final var expectedIndex = 8;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(DIAGNOS_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(DIAGNOS_CATEGORY_ID, category.getId()),
                    () -> assertEquals(expectedIndex, category.getIndex()),
                    () -> assertNull(category.getParent(), "Should not contain a parent"),
                    () -> assertNull(category.getValue(), "Should not contain a value"),
                    () -> assertNull(category.getValidation(), "Should not include validation"),
                    () -> assertNotNull(category.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeCategoryConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(DIAGNOS_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text")
                );
            }
        }

        @Nested
        class QuestionShouldIncludeDiagnoses {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 9;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

                assertAll("Validating question",
                    () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(DIAGNOS_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNull(question.getValidation(), "Should not include validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigRadioBoolean = (CertificateDataConfigRadioBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertNull(certificateDataConfigRadioBoolean.getText(), "Should not include text"),
                    () -> assertNull(certificateDataConfigRadioBoolean.getDescription(), "Should not description"),
                    () -> assertNull(certificateDataConfigRadioBoolean.getHeader(), "Should not include header"),
                    () -> assertNull(certificateDataConfigRadioBoolean.getLabel(), "Should not include label"),
                    () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100, certificateDataConfigRadioBoolean.getId()),
                    () -> assertTrue(certificateDataConfigRadioBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue(certificateDataConfigRadioBoolean.getUnselectedText().trim().length() > 0, "Missing unseselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setOnskarFormedlaDiagnos(true)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getOnskarFormedlaDiagnos(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setOnskarFormedlaDiagnos(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getOnskarFormedlaDiagnos(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }
        }

        @Nested
        class QuestionDiagnos {

            private Ag7804UtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 10;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

                assertAll("Validating question",
                    () -> assertEquals(DIAGNOS_SVAR_ID_6, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(DIAGNOS_CATEGORY_ID, question.getParent(), "Invalid parent"),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

                assertEquals(CertificateDataConfigTypes.UE_DIAGNOSES, question.getConfig().getType());

                final var certificateDataConfigDiagnoses = (CertificateDataConfigDiagnoses) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigDiagnoses.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigDiagnoses.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals("ICD_10_SE", certificateDataConfigDiagnoses.getTerminology().get(0).getId()),
                    () -> assertEquals("ICD-10-SE", certificateDataConfigDiagnoses.getTerminology().get(0).getLabel()),
                    () -> assertEquals("KSH_97_P", certificateDataConfigDiagnoses.getTerminology().get(1).getId()),
                    () -> assertEquals("KSH97-P (Primärvård)", certificateDataConfigDiagnoses.getTerminology().get(1).getLabel()),
                    () -> assertEquals("1", certificateDataConfigDiagnoses.getList().get(0).getId()),
                    () -> assertEquals("2", certificateDataConfigDiagnoses.getList().get(1).getId()),
                    () -> assertEquals("3", certificateDataConfigDiagnoses.getList().get(2).getId())
                );
            }

            @Test
            void shouldIncludeValueFirstDiagnosis() {
                final var expectedDiagnos = Diagnos.create("A01", "ICD10", "Diagnos beskrivning", "Diagnos display name");
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setDiagnoser(Arrays.asList(expectedDiagnos))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

                assertEquals(CertificateDataValueType.DIAGNOSIS_LIST, question.getValue().getType());

                final var certificateDataConfigDiagnoses = (CertificateDataValueDiagnosisList) question.getValue();
                assertAll(
                    () -> assertEquals("1", certificateDataConfigDiagnoses.getList().get(0).getId()),
                    () -> assertEquals(expectedDiagnos.getDiagnosKodSystem(),
                        certificateDataConfigDiagnoses.getList().get(0).getTerminology()),
                    () -> assertEquals(expectedDiagnos.getDiagnosKod(), certificateDataConfigDiagnoses.getList().get(0).getCode()),
                    () -> assertEquals(expectedDiagnos.getDiagnosBeskrivning(),
                        certificateDataConfigDiagnoses.getList().get(0).getDescription())
                );
            }

            @Test
            void shouldIncludeValueSecondDiagnosis() {
                final var emptyDiagnos = Diagnos.create(null, "ICD10", null, null);
                final var expectedDiagnos = Diagnos.create("A01", "ICD10", "Diagnos beskrivning", "Diagnos display name");
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setDiagnoser(Arrays.asList(emptyDiagnos, expectedDiagnos))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

                assertEquals(CertificateDataValueType.DIAGNOSIS_LIST, question.getValue().getType());

                final var certificateDataConfigDiagnoses = (CertificateDataValueDiagnosisList) question.getValue();
                assertAll(
                    () -> assertEquals("2", certificateDataConfigDiagnoses.getList().get(0).getId()),
                    () -> assertEquals(expectedDiagnos.getDiagnosKodSystem(),
                        certificateDataConfigDiagnoses.getList().get(0).getTerminology()),
                    () -> assertEquals(expectedDiagnos.getDiagnosKod(), certificateDataConfigDiagnoses.getList().get(0).getCode()),
                    () -> assertEquals(expectedDiagnos.getDiagnosBeskrivning(),
                        certificateDataConfigDiagnoses.getList().get(0).getDescription())
                );
            }

            @Test
            void shouldIncludeValueThirdDiagnosis() {
                final var emptyDiagnos = Diagnos.create(null, "ICD10", null, null);
                final var expectedDiagnos = Diagnos.create("A01", "ICD10", "Diagnos beskrivning", "Diagnos display name");
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setDiagnoser(Arrays.asList(emptyDiagnos, emptyDiagnos, expectedDiagnos))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

                assertEquals(CertificateDataValueType.DIAGNOSIS_LIST, question.getValue().getType());

                final var certificateDataConfigDiagnoses = (CertificateDataValueDiagnosisList) question.getValue();
                assertAll(
                    () -> assertEquals("3", certificateDataConfigDiagnoses.getList().get(0).getId()),
                    () -> assertEquals(expectedDiagnos.getDiagnosKodSystem(),
                        certificateDataConfigDiagnoses.getList().get(0).getTerminology()),
                    () -> assertEquals(expectedDiagnos.getDiagnosKod(), certificateDataConfigDiagnoses.getList().get(0).getCode()),
                    () -> assertEquals(expectedDiagnos.getDiagnosBeskrivning(),
                        certificateDataConfigDiagnoses.getList().get(0).getDescription())
                );
            }

            @Test
            void shouldIncludeValueAllDiagnosis() {
                final var expectedDiagnosFirst = Diagnos.create("A01", "ICD10", "Diagnos beskrivning", "Diagnos display name 1");
                final var expectedDiagnosSecond = Diagnos.create("A02", "ICD10", "Diagnos beskrivning", "Diagnos display name 2");
                final var expectedDiagnosThird = Diagnos.create("A03", "ICD10", "Diagnos beskrivning", "Diagnos display name 3");
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setDiagnoser(Arrays.asList(expectedDiagnosFirst, expectedDiagnosSecond, expectedDiagnosThird))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

                assertEquals(CertificateDataValueType.DIAGNOSIS_LIST, question.getValue().getType());

                final var certificateDataConfigDiagnoses = (CertificateDataValueDiagnosisList) question.getValue();
                assertAll(
                    () -> assertEquals("1", certificateDataConfigDiagnoses.getList().get(0).getId()),
                    () -> assertEquals("2", certificateDataConfigDiagnoses.getList().get(1).getId()),
                    () -> assertEquals("3", certificateDataConfigDiagnoses.getList().get(2).getId()),
                    () -> assertEquals(expectedDiagnosFirst.getDiagnosKod(), certificateDataConfigDiagnoses.getList().get(0).getCode()),
                    () -> assertEquals(expectedDiagnosSecond.getDiagnosKod(), certificateDataConfigDiagnoses.getList().get(1).getCode()),
                    () -> assertEquals(expectedDiagnosThird.getDiagnosKod(), certificateDataConfigDiagnoses.getList().get(2).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(DIAGNOS_SVAR_ID_6, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$1", certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(DIAGNOS_SVAR_ID_6, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$onskarFormedlaDiagnos", certificateDataValidationHide.getExpression())
                );
            }
        }
    }
}