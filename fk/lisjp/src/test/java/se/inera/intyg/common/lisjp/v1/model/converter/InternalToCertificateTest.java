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

package se.inera.intyg.common.lisjp.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_COLLECTION;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_INFO;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_JSON_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ATGARDER_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ICF_COLLECTION;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ICF_INFO;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ICF_PLACEHOLDER;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDropdown;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigIcf;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigSickLeavePeriod;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationEnable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataIcfValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;

@DisplayName("Should convert LisjpUtlatandeV1 to Certificate")
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

        private LisjpUtlatandeV1 internalCertificate;

        @Nested
        class CommonMetadata {

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
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

                assertEquals("Läkarintyg för sjukpenning", certificate.getMetadata().getName());
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

                internalCertificate = LisjpUtlatandeV1.builder()
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

            private LisjpUtlatandeV1 internalCertificate;

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleDate = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigCheckboxMultipleDate.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleDate.getDescription().trim().length() > 0, "Missing description")
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
        class QuestionMotiveringEjUndersokning {

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
                final var expectedIndex = 5;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1);

                assertAll("Validating question",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, question.getId()),
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

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigTextArea.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals("lightbulb_outline", certificateDataConfigTextArea.getIcon()),
                    () -> assertEquals(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setMotiveringTillInteBaseratPaUndersokning(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1, certificateDataTextValue.getId()),
                    () -> assertEquals(expectedText, certificateDataTextValue.getText())
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

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals(
                        "!$undersokningAvPatienten && ($telefonkontaktMedPatienten || $journaluppgifter || $annatGrundForMU)",
                        certificateDataValidationShow.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationText() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1);

                final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1, certificateDataValidationText.getId()),
                    () -> assertEquals(150, certificateDataValidationText.getLimit())
                );
            }

            @Test
            void shouldIncludeCategoryValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[2];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class CategorySysselsattning {

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
                final var expectedIndex = 6;

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
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionSysselsattning {

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
                final var expectedIndex = 7;

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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
            void shouldIncludeQuestionValueStudier() {
                final var expectedSysselsattning = Sysselsattning.create(SysselsattningsTyp.STUDIER);
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionSysselsattningYrkeQuestion {

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
                final var expectedIndex = 8;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                assertAll("Validating question",
                    () -> assertEquals(NUVARANDE_ARBETE_SVAR_ID_29, question.getId()),
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                    () -> assertEquals(TYP_AV_SYSSELSATTNING_SVAR_ID_28, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + SysselsattningsTyp.NUVARANDE_ARBETE.getId(), certificateDataValidationShow.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[2];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class CategoryDiagnos {

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
                final var expectedIndex = 9;

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
        class QuestionDiagnos {

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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
                internalCertificate = LisjpUtlatandeV1.builder()
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
        }

        @Nested
        class CategoryFunktionsnedsattning {

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
                final var expectedIndex = 11;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_CATEGORY_ID, category.getId()),
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

                final var category = certificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(category.getConfig().getDescription(), "Should not contain a description")
                );
            }

            @Test
            void shouldIncludeValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionFunktionsnedsattning {

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
                final var expectedIndex = 12;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID_35);

                assertAll("Validating question",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_ID_35, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID_35);

                assertEquals(CertificateDataConfigTypes.UE_ICF, question.getConfig().getType());

                final var certificateDataConfigIcf = (CertificateDataConfigIcf) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigIcf.getHeader().trim().length() > 0, "Missing header"),
                    () -> assertTrue(certificateDataConfigIcf.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigIcf.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35, certificateDataConfigIcf.getId()),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_ICF_INFO, certificateDataConfigIcf.getModalLabel()),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_ICF_COLLECTION, certificateDataConfigIcf.getCollectionsLabel()),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_ICF_PLACEHOLDER, certificateDataConfigIcf.getPlaceholder())
                );

            }

            @Test
            void shouldIncludeQuestionValueIcf() {
                final var expectedText = "Text value for question";
                final var expectedIcfValues = Arrays.asList("Test", "Test 2", "Test 3");
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setFunktionsnedsattning(expectedText)
                    .setFunktionsKategorier(expectedIcfValues)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID_35);

                final var certificateDataValuIcf = (CertificateDataIcfValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35, certificateDataValuIcf.getId()),
                    () -> assertEquals(expectedText, certificateDataValuIcf.getText()),
                    () -> assertEquals(expectedIcfValues, certificateDataValuIcf.getIcfCodes())
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

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID_35);

                final var certificateDataValueText = (CertificateDataIcfValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35, certificateDataValueText.getId()),
                    () -> assertNull(certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueIcfEmpty() {
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID_35);

                final var certificateDataValueText = (CertificateDataIcfValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35, certificateDataValueText.getId()),
                    () -> assertTrue(certificateDataValueText.getIcfCodes().isEmpty())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID_35);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_SVAR_ID_35, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID_35);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionAktivitetsbegransning {

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
                final var expectedIndex = 13;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

                assertAll("Validating question",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_ID_17, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(FUNKTIONSNEDSATTNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

                assertEquals(CertificateDataConfigTypes.UE_ICF, question.getConfig().getType());

                final var certificateDataConfigIcf = (CertificateDataConfigIcf) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigIcf.getHeader().trim().length() > 0, "Missing header"),
                    () -> assertTrue(certificateDataConfigIcf.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigIcf.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataConfigIcf.getId()),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_ICF_INFO, certificateDataConfigIcf.getModalLabel()),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_ICF_COLLECTION, certificateDataConfigIcf.getCollectionsLabel()),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER, certificateDataConfigIcf.getPlaceholder())
                );
            }

            @Test
            void shouldIncludeQuestionValueIcf() {
                final var expectedText = "Text value for question";
                final var expectedIcfValues = Arrays.asList("Test", "Test 2", "Test 3");
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setAktivitetsbegransning(expectedText)
                    .setAktivitetsKategorier(expectedIcfValues)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

                final var certificateDataValueIcf = (CertificateDataIcfValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataValueIcf.getId()),
                    () -> assertEquals(expectedText, certificateDataValueIcf.getText()),
                    () -> assertEquals(expectedIcfValues, certificateDataValueIcf.getIcfCodes())
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

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

                final var certificateDataValueText = (CertificateDataIcfValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataValueText.getId()),
                    () -> assertNull(certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueIcfEmpty() {
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

                final var certificateDataValueIcf = (CertificateDataIcfValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataValueIcf.getId()),
                    () -> assertTrue(certificateDataValueIcf.getIcfCodes().isEmpty())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_ID_17, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class CategoryMedicinskaBehandlingar {

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
                final var expectedIndex = 14;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(MEDICINSKABEHANDLINGAR_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(MEDICINSKABEHANDLINGAR_CATEGORY_ID, category.getId()),
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

                final var category = certificate.getData().get(MEDICINSKABEHANDLINGAR_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(category.getConfig().getDescription(), "Should not contain a description")
                );
            }

            @Test
            void shouldIncludeCategoryValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(MEDICINSKABEHANDLINGAR_CATEGORY_ID);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionPagaendeBehandling {

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
                final var expectedIndex = 15;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PAGAENDEBEHANDLING_SVAR_ID_19);

                assertAll("Validating question",
                    () -> assertEquals(PAGAENDEBEHANDLING_SVAR_ID_19, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(MEDICINSKABEHANDLINGAR_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PAGAENDEBEHANDLING_SVAR_ID_19);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getHeader().trim().length() > 0, "Missing header"),
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(certificateDataConfigTextArea.getDescription(), "Should not include description"),
                    () -> assertEquals(PAGAENDEBEHANDLING_SVAR_JSON_ID_19, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPagaendeBehandling(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PAGAENDEBEHANDLING_SVAR_ID_19);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(PAGAENDEBEHANDLING_SVAR_JSON_ID_19, certificateDataValueText.getId()),
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

                final var question = certificate.getData().get(PAGAENDEBEHANDLING_SVAR_ID_19);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(PAGAENDEBEHANDLING_SVAR_JSON_ID_19, certificateDataValueText.getId()),
                    () -> assertNull(certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PAGAENDEBEHANDLING_SVAR_ID_19);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionPlaneradBehandling {

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
                final var expectedIndex = 16;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PLANERADBEHANDLING_SVAR_ID_20);

                assertAll("Validating question",
                    () -> assertEquals(PLANERADBEHANDLING_SVAR_ID_20, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(MEDICINSKABEHANDLINGAR_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PLANERADBEHANDLING_SVAR_ID_20);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getHeader().trim().length() > 0, "Missing header"),
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(certificateDataConfigTextArea.getDescription(), "Should not include description"),
                    () -> assertEquals(PLANERADBEHANDLING_SVAR_JSON_ID_20, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPlaneradBehandling(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PLANERADBEHANDLING_SVAR_ID_20);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(PLANERADBEHANDLING_SVAR_JSON_ID_20, certificateDataValueText.getId()),
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

                final var question = certificate.getData().get(PLANERADBEHANDLING_SVAR_ID_20);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(PLANERADBEHANDLING_SVAR_JSON_ID_20, certificateDataValueText.getId()),
                    () -> assertNull(certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PLANERADBEHANDLING_SVAR_ID_20);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class CategoryBedomning {

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
                final var expectedIndex = 17;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(BEDOMNING_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, category.getId()),
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

                final var category = certificate.getData().get(BEDOMNING_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing selected text"),
                    () -> assertNull(category.getConfig().getDescription(), "Should not contain a description")
                );
            }

            @Test
            void shouldNotIncludeAnyValidation() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);
                final var question = certificate.getData().get(BEDOMNING_CATEGORY_ID);
                assertTrue(question.getValidation().length == 0, "Should not contain any validation");
            }
        }

        @Nested
        class QuestionBehovAvSjukskrivning {

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
                final var expectedIndex = 18;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                assertAll("Validating question",
                    () -> assertEquals(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                assertEquals(CertificateDataConfigTypes.UE_SICK_LEAVE_PERIOD, question.getConfig().getType());

                final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigSickLeavePeriod) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigSickLeavePeriod.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigSickLeavePeriod.getDescription().trim().length() > 0, "Missing description")
                );
            }

            @Test
            void shouldIncludeQuestionConfigOneFourth() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                assertEquals(CertificateDataConfigTypes.UE_SICK_LEAVE_PERIOD, question.getConfig().getType());

                final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigSickLeavePeriod) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(certificateDataConfigSickLeavePeriod.getList().get(0).getId(), SjukskrivningsGrad.NEDSATT_1_4.getId()
                    ),
                    () -> assertTrue(certificateDataConfigSickLeavePeriod.getList().get(0).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigHalf() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                assertEquals(CertificateDataConfigTypes.UE_SICK_LEAVE_PERIOD, question.getConfig().getType());

                final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigSickLeavePeriod) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(certificateDataConfigSickLeavePeriod.getList().get(1).getId(),
                        SjukskrivningsGrad.NEDSATT_HALFTEN.getId()),
                    () -> assertTrue(certificateDataConfigSickLeavePeriod.getList().get(1).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigThreeFourth() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                assertEquals(CertificateDataConfigTypes.UE_SICK_LEAVE_PERIOD, question.getConfig().getType());

                final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigSickLeavePeriod) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(certificateDataConfigSickLeavePeriod.getList().get(2).getId(),
                        SjukskrivningsGrad.NEDSATT_3_4.getId()),
                    () -> assertTrue(certificateDataConfigSickLeavePeriod.getList().get(2).getLabel().trim().length() > 0, "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigWhole() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                assertEquals(CertificateDataConfigTypes.UE_SICK_LEAVE_PERIOD, question.getConfig().getType());

                final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigSickLeavePeriod) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(certificateDataConfigSickLeavePeriod.getList().get(3).getId(),
                        SjukskrivningsGrad.HELT_NEDSATT.getId()),
                    () -> assertTrue(certificateDataConfigSickLeavePeriod.getList().get(3).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionValueOneFourth() {
                final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
                final var expectedGrad = SjukskrivningsGrad.NEDSATT_1_4;
                final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSjukskrivningar(Arrays.asList(expectedDateRange))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedGrad.getId(),
                        certificateDataValueDateRangeList.getList().get(0).getId()),
                    () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                    () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getFrom())
                );
            }

            @Test
            void shouldIncludeQuestionValueHalf() {
                final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
                final var expectedGrad = SjukskrivningsGrad.NEDSATT_HALFTEN;
                final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSjukskrivningar(Arrays.asList(expectedDateRange))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedGrad.getId(),
                        certificateDataValueDateRangeList.getList().get(0).getId()),
                    () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                    () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getFrom())
                );
            }

            @Test
            void shouldIncludeQuestionValueThreeFourth() {
                final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
                final var expectedGrad = SjukskrivningsGrad.NEDSATT_3_4;
                final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSjukskrivningar(Arrays.asList(expectedDateRange))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedGrad.getId(),
                        certificateDataValueDateRangeList.getList().get(0).getId()),
                    () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                    () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getFrom())
                );
            }

            @Test
            void shouldIncludeQuestionValueWhole() {
                final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
                final var expectedGrad = SjukskrivningsGrad.HELT_NEDSATT;
                final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSjukskrivningar(Arrays.asList(expectedDateRange))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedGrad.getId(),
                        certificateDataValueDateRangeList.getList().get(0).getId()),
                    () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                    () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getFrom())
                );
            }

            @Test
            void shouldIncludeQuestionValueAllOfThem() {
                final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
                final var expectedGradOneFourth = SjukskrivningsGrad.NEDSATT_1_4;
                final var expectedDateRangeOneFourth = Sjukskrivning.create(expectedGradOneFourth, expectedPeriod);
                final var expectedGradThreeFourth = SjukskrivningsGrad.NEDSATT_3_4;
                final var expectedDateRangeThreeFourth = Sjukskrivning.create(expectedGradThreeFourth, expectedPeriod);
                final var expectedGradHalf = SjukskrivningsGrad.NEDSATT_HALFTEN;
                final var expectedDateRangeHalf = Sjukskrivning.create(expectedGradHalf, expectedPeriod);
                final var expectedGradWhole = SjukskrivningsGrad.HELT_NEDSATT;
                final var expectedDateRangeWhole = Sjukskrivning.create(expectedGradWhole, expectedPeriod);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSjukskrivningar(Arrays
                        .asList(expectedDateRangeOneFourth, expectedDateRangeThreeFourth, expectedDateRangeHalf, expectedDateRangeWhole))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedGradOneFourth.getId(),
                        certificateDataValueDateRangeList.getList().get(0).getId()),
                    () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                    () -> assertEquals(expectedPeriod.getFrom().asLocalDate(),
                        certificateDataValueDateRangeList.getList().get(0).getFrom()),
                    () -> assertEquals(expectedGradThreeFourth.getId(),
                        certificateDataValueDateRangeList.getList().get(1).getId()),
                    () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(1).getTo()),
                    () -> assertEquals(expectedPeriod.getFrom().asLocalDate(),
                        certificateDataValueDateRangeList.getList().get(1).getFrom()),
                    () -> assertEquals(expectedGradHalf.getId(),
                        certificateDataValueDateRangeList.getList().get(2).getId()),
                    () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(2).getTo()),
                    () -> assertEquals(expectedPeriod.getFrom().asLocalDate(),
                        certificateDataValueDateRangeList.getList().get(2).getFrom()),
                    () -> assertEquals(expectedGradWhole.getId(),
                        certificateDataValueDateRangeList.getList().get(3).getId()),
                    () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(3).getTo()),
                    () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(3).getFrom())
                );
            }

            @Test
            void shouldIncludeQuestionValueNone() {
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setSjukskrivningar(Collections.emptyList())
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
                assertTrue(certificateDataValueDateRangeList.getList().size() == 0);
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + SjukskrivningsGrad.NEDSATT_1_4.getId()
                            + " || $" + SjukskrivningsGrad.NEDSATT_HALFTEN.getId()
                            + " || $" + SjukskrivningsGrad.NEDSATT_3_4.getId()
                            + " || $" + SjukskrivningsGrad.HELT_NEDSATT.getId(),
                        certificateDataValidationMandatory.getExpression())
                );
            }
        }

        @Nested
        class QuestionMotiveringTidigtStartdatum {

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
                final var expectedIndex = 19;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32);

                assertAll("Validating question",
                    () -> assertEquals(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigTextArea.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertNull(certificateDataConfigTextArea.getHeader(), "Should not include a header"),
                    () -> assertNull(certificateDataConfigTextArea.getLabel(), "Should not include a label"),
                    () -> assertEquals("lightbulb_outline", certificateDataConfigTextArea.getIcon(), "Wrong icon"),
                    () -> assertEquals(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setMotiveringTillTidigtStartdatumForSjukskrivning(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID, certificateDataValueText.getId()),
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

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID, certificateDataValueText.getId()),
                    () -> assertNull(certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals(
                        "($EN_FJARDEDEL.from <= -7) || ($HALFTEN.from <= -7) || ($TRE_FJARDEDEL.from <= -7) || ($HELT_NEDSATT.from <= -7)",
                        certificateDataValidationShow.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationText() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32);

                final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID, certificateDataValidationText.getId()),
                    () -> assertEquals(150, certificateDataValidationText.getLimit())
                );
            }
        }

        @Nested
        class QuestionForsakringsmedicinsktBeslutsstod {

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
                final var expectedIndex = 20;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37);

                assertAll("Validating question",
                    () -> assertEquals(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertNull(certificateDataConfigTextArea.getHeader(), "Should not include header"),
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigTextArea.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setForsakringsmedicinsktBeslutsstod(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37, certificateDataValueText.getId()),
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

                final var question = certificate.getData().get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37, certificateDataValueText.getId()),
                    () -> assertNull(certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionArbetstidsforlaggning {

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
                final var expectedIndex = 21;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

                assertAll("Validating question",
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_SVAR_ID_33, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigRadioBoolean = (CertificateDataConfigRadioBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigRadioBoolean.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigRadioBoolean.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertNull(certificateDataConfigRadioBoolean.getHeader(), "Should not include header"),
                    () -> assertNull(certificateDataConfigRadioBoolean.getLabel(), "Should not include label"),
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33, certificateDataConfigRadioBoolean.getId()),
                    () -> assertTrue(certificateDataConfigRadioBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue(certificateDataConfigRadioBoolean.getUnselectedText().trim().length() > 0, "Missing unseselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetstidsforlaggning(true)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getArbetstidsforlaggning(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetstidsforlaggning(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getArbetstidsforlaggning(), certificateDataValueBoolean.getSelected())
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

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals(
                        "$EN_FJARDEDEL || $HALFTEN || $TRE_FJARDEDEL",
                        certificateDataValidationShow.getExpression())
                );
            }

            @Test
            void shouldIncludeCategoryValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionMotiveringArbetstidsforlaggning {

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
                final var expectedIndex = 22;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33);

                assertAll("Validating question",
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(certificateDataConfigTextArea.getDescription(), "Should not include description"),
                    () -> assertNull(certificateDataConfigTextArea.getHeader(), "Should not include header"),
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetstidsforlaggningMotivering(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33, certificateDataTextValue.getId()),
                    () -> assertEquals(expectedText, certificateDataTextValue.getText())
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

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33);

                final var certificateDataTextValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(ARBETSTIDSFORLAGGNING_SVAR_ID_33, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33, certificateDataValidationShow.getExpression())
                );
            }

            @Test
            void shouldIncludeValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionArbetsresor {

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
                final var expectedIndex = 23;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSRESOR_SVAR_ID_34);

                assertAll("Validating question",
                    () -> assertEquals(ARBETSRESOR_SVAR_ID_34, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setArbetsresor(true)
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSRESOR_SVAR_ID_34);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigCheckboxBoolean = (CertificateDataConfigCheckboxBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigCheckboxBoolean.getLabel().trim().length() > 0, "Missing label"),
                    () -> assertNull(certificateDataConfigCheckboxBoolean.getText(), "Shouldn't have text"),
                    () -> assertNull(certificateDataConfigCheckboxBoolean.getDescription(), "Shouldn't have description"),
                    () -> assertEquals(ARBETSRESOR_SVAR_JSON_ID_34, certificateDataConfigCheckboxBoolean.getId()),
                    () -> assertTrue(certificateDataConfigCheckboxBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue(certificateDataConfigCheckboxBoolean.getUnselectedText().trim().length() > 0,
                        "Missing unselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSRESOR_SVAR_ID_34);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSRESOR_SVAR_JSON_ID_34, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getArbetsresor(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetsresor(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSRESOR_SVAR_ID_34);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSRESOR_SVAR_JSON_ID_34, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getArbetsresor(), certificateDataValueBoolean.getSelected())
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

                final var question = certificate.getData().get(ARBETSRESOR_SVAR_ID_34);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSRESOR_SVAR_JSON_ID_34, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSRESOR_SVAR_ID_34);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionPrognos {

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
                final var expectedIndex = 24;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                assertAll("Validating question",
                    () -> assertEquals(PROGNOS_SVAR_ID_39, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigMultipleCode.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigMultipleCode.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertNull(certificateDataConfigMultipleCode.getHeader(), "Should not have a header"),
                    () -> assertNull(certificateDataConfigMultipleCode.getIcon(), "Should not have an iconr"),
                    () -> assertNull(certificateDataConfigMultipleCode.getLabel(), "Should not have a label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigStorSannolikhet() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosTyp.MED_STOR_SANNOLIKHET.getId(),
                        certificateDataConfigMultipleCode.getList().get(0).getId()),
                    () -> assertTrue(certificateDataConfigMultipleCode.getList().get(0).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigAntalDagar() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosTyp.ATER_X_ANTAL_DGR.getId(),
                        certificateDataConfigMultipleCode.getList().get(1).getId()),
                    () -> assertTrue(certificateDataConfigMultipleCode.getList().get(1).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigSannoliktInte() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId(),
                        certificateDataConfigMultipleCode.getList().get(2).getId()),
                    () -> assertTrue(certificateDataConfigMultipleCode.getList().get(2).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigPrognosOklar() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigMultipleCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosTyp.PROGNOS_OKLAR.getId(),
                        certificateDataConfigMultipleCode.getList().get(3).getId()),
                    () -> assertTrue(certificateDataConfigMultipleCode.getList().get(3).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionValueStorSannolikhet() {
                final var expectedPrognos = Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, null);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueAntalDagar() {
                final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueSannoliktInte() {
                final var expectedPrognos = Prognos.create(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING, null);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValuePrognosOklar() {
                final var expectedPrognos = Prognos.create(PrognosTyp.PROGNOS_OKLAR, null);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(PROGNOS_SVAR_ID_39, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$STOR_SANNOLIKHET || $ATER_X_ANTAL_DGR || $SANNOLIKT_INTE || $PROGNOS_OKLAR",
                        certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionPrognosTimeperiod {

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
                final var expectedIndex = 25;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                assertAll("Validating question",
                    () -> assertEquals(PROGNOS_BESKRIVNING_DELSVAR_ID_39, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(BEDOMNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

                final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertNull(certificateDataConfigDropdown.getText(), "Should not have text"),
                    () -> assertNull(certificateDataConfigDropdown.getDescription(), "Should not have description"),
                    () -> assertNull(certificateDataConfigDropdown.getHeader(), "Should not have a header"),
                    () -> assertNull(certificateDataConfigDropdown.getIcon(), "Should not have an iconr"),
                    () -> assertNull(certificateDataConfigDropdown.getLabel(), "Should not have a label")
                );
            }

            @Test
            void shouldIncludeQuestionConfigDefaultChoice() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

                final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals("VALJ_TIDSPERIOD",
                        certificateDataConfigDropdown.getList().get(0).getId()),
                    () -> assertTrue(certificateDataConfigDropdown.getList().get(0).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals("Välj tidsperiod",
                        certificateDataConfigDropdown.getList().get(0).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfig30Days() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

                final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_30.getId(),
                        certificateDataConfigDropdown.getList().get(1).getId()),
                    () -> assertTrue(certificateDataConfigDropdown.getList().get(1).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfig60Days() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

                final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_60.getId(),
                        certificateDataConfigDropdown.getList().get(2).getId()),
                    () -> assertTrue(certificateDataConfigDropdown.getList().get(2).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfig90Days() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

                final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_90.getId(),
                        certificateDataConfigDropdown.getList().get(3).getId()),
                    () -> assertTrue(certificateDataConfigDropdown.getList().get(3).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfig180Days() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

                final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_180.getId(),
                        certificateDataConfigDropdown.getList().get(4).getId()),
                    () -> assertTrue(certificateDataConfigDropdown.getList().get(4).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionConfig365Days() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

                final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_365.getId(),
                        certificateDataConfigDropdown.getList().get(5).getId()),
                    () -> assertTrue(certificateDataConfigDropdown.getList().get(5).getLabel().trim().length() > 0,
                        "Missing label")
                );
            }

            @Test
            void shouldIncludeQuestionValue30Days() {
                final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValue60Days() {
                final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_60);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValue90Days() {
                final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_90);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValue180Days() {
                final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_180);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValue365Days() {
                final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_365);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setPrognos(expectedPrognos)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                    () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(PROGNOS_BESKRIVNING_DELSVAR_ID_39, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$TRETTIO_DGR || $SEXTIO_DGR || $NITTIO_DGR || $HUNDRAATTIO_DAGAR || $TREHUNDRASEXTIOFEM_DAGAR",
                        certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationEnable() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

                final var certificateDataValidationEnable = (CertificateDataValidationEnable) question.getValidation()[2];
                assertAll("Validation question validation",
                    () -> assertEquals(PROGNOS_SVAR_ID_39, certificateDataValidationEnable.getQuestionId()),
                    () -> assertEquals("$" + PrognosTyp.ATER_X_ANTAL_DGR.getId(),
                        certificateDataValidationEnable.getExpression())
                );
            }
        }

        @Nested
        class CategoryAtgarder {

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
                final var expectedIndex = 26;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(ATGARDER_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(ATGARDER_CATEGORY_ID, category.getId()),
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

                final var category = certificate.getData().get(ATGARDER_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(category.getConfig().getDescription(), "Should not contain description")
                );
            }

            @Test
            void shouldIncludeCategoryValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ATGARDER_CATEGORY_ID);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionAtgarder {

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
                final var expectedIndex = 27;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertAll("Validating question",
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(ATGARDER_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(certificateDataConfigCheckboxMultipleCode.getDescription(), "Should not contain a description")
                );
            }

            @Test
            void shouldIncludeQuestionConfigEjAktuellt() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(0).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(0).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(0).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigArbetstraning() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.ARBETSTRANING;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(1).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(1).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(1).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigArbetsanpassning() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(2).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(2).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(2).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigSokaArbete() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(3).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(3).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(3).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigBesokaArbete() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(4).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(4).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(4).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigErgonomisk() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(5).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(5).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(5).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigHjalpmedel() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.HJALPMEDEL;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(6).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(6).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(6).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigKonflikthantering() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(7).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(7).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(7).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigKontaktFHV() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(8).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(8).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(8).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigOmfordelning() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(9).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(9).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(9).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionConfigOvrigt() {
                final var expectedChoice = ArbetslivsinriktadeAtgarderVal.OVRIGT;
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE, question.getConfig().getType());

                final var certificateDataConfigCheckboxMultipleCode = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertEquals(expectedChoice.getId(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(10).getId()),
                    () -> assertTrue(certificateDataConfigCheckboxMultipleCode.getList().get(10).getLabel().trim().length() > 0,
                        "Missing label"),
                    () -> assertEquals(expectedChoice.getLabel(),
                        certificateDataConfigCheckboxMultipleCode.getList().get(10).getLabel())
                );
            }

            @Test
            void shouldIncludeQuestionValueArbetssokande() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueArbetstraning() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }


            @Test
            void shouldIncludeQuestionValueArbetsanpassning() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueNyttArbete() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueBesokaArbetsplatsen() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueHjalpmedel() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueErgonomisk() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueKonflikthantering() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueOmfordelning() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder
                    .create(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueKontaktFHV() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }

            @Test
            void shouldIncludeQuestionValueOvrigt() {
                final var expectedAtgard = ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT);
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(Arrays.asList(expectedAtgard))
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgard.getTyp().getId(), certificateDataValueCodeList.getList().get(0).getCode())
                );
            }


            @Test
            void shouldIncludeQuestionValueSeveralOfThem() {
                final var expectedAtgarder = Arrays.asList(
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT)
                );
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarder(expectedAtgarder)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValueCodeList = (CertificateDataValueCodeList) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(expectedAtgarder.get(0).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(0).getId()),
                    () -> assertEquals(expectedAtgarder.get(1).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(1).getId()),
                    () -> assertEquals(expectedAtgarder.get(2).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(2).getId()),
                    () -> assertEquals(expectedAtgarder.get(3).getTyp().getId(),
                        certificateDataValueCodeList.getList().get(3).getId())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$EJ_AKTUELLT || $ARBETSTRANING || $ARBETSANPASSNING || $SOKA_NYTT_ARBETE || $BESOK_ARBETSPLATS "
                            + "|| $ERGONOMISK || $HJALPMEDEL || $KONFLIKTHANTERING || $KONTAKT_FHV || $OMFORDELNING || $OVRIGA_ATGARDER",
                        certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationDisable() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValidationDisable = (CertificateDataValidationDisable) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, certificateDataValidationDisable.getQuestionId()),
                    () -> assertTrue(certificateDataValidationDisable.getId().size() == 1),
                    () -> assertEquals("EJ_AKTUELLT", certificateDataValidationDisable.getId().get(0)),
                    () -> assertEquals("$ARBETSTRANING || $ARBETSANPASSNING || $SOKA_NYTT_ARBETE || $BESOK_ARBETSPLATS "
                            + "|| $ERGONOMISK || $HJALPMEDEL || $KONFLIKTHANTERING || $KONTAKT_FHV || $OMFORDELNING || $OVRIGA_ATGARDER",
                        certificateDataValidationDisable.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationDisableAll() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValidationDisable = (CertificateDataValidationDisable) question.getValidation()[2];
                assertAll("Validation question validation",
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, certificateDataValidationDisable.getQuestionId()),
                    () -> assertTrue(certificateDataValidationDisable.getId().size() == 10),
                    //() -> assertEquals("$EJ_AKTUELLT", certificateDataValidationDisable.getId().get(0)),
                    () -> assertEquals("$EJ_AKTUELLT",
                        certificateDataValidationDisable.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[3];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class QuestionAtgarderBeskrivning {

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
                final var expectedIndex = 28;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44);

                assertAll("Validating question",
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(ATGARDER_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(certificateDataConfigTextArea.getDescription(), "Should not include description"),
                    () -> assertNull(certificateDataConfigTextArea.getHeader(), "Should not include header"),
                    () -> assertNull(certificateDataConfigTextArea.getLabel(), "Should not include label"),
                    () -> assertNull(certificateDataConfigTextArea.getIcon(), "Should not include icon"),
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setArbetslivsinriktadeAtgarderBeskrivning(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44, certificateDataValueText.getId()),
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

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44);

                final var certificateDataValueText = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44, certificateDataValueText.getId()),
                    () -> assertNull(certificateDataValueText.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals(
                        "$ARBETSTRANING || $ARBETSANPASSNING || $SOKA_NYTT_ARBETE || $BESOK_ARBETSPLATS || $ERGONOMISK || $HJALPMEDEL ||"
                            + " $KONFLIKTHANTERING || $KONTAKT_FHV || $OMFORDELNING || $OVRIGA_ATGARDER",
                        certificateDataValidationShow.getExpression())
                );
            }

            @Test
            void shouldIncludeValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }

        @Nested
        class CategoryOvrigt {

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
                final var expectedIndex = 29;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var category = certificate.getData().get(OVRIGT_CATEGORY_ID);

                assertAll("Validating category",
                    () -> assertEquals(OVRIGT_CATEGORY_ID, category.getId()),
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

                final var category = certificate.getData().get(OVRIGT_CATEGORY_ID);

                assertEquals(CertificateDataConfigTypes.CATEGORY, category.getConfig().getType());

                assertAll("Validating category configuration",
                    () -> assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text"),
                    () -> assertNull(category.getConfig().getDescription(), "Should not contain description")
                );
            }
        }

        @Nested
        class QuestionOvrigt {

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
                final var expectedIndex = 30;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(OVRIGT_SVAR_ID_25);

                assertAll("Validating question",
                    () -> assertEquals(OVRIGT_SVAR_ID_25, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(OVRIGT_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Missing value"),
                    () -> assertNotNull(question.getValidation(), "Missing validation"),
                    () -> assertNotNull(question.getConfig(), "Missing config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(OVRIGT_SVAR_ID_25);

                assertEquals(CertificateDataConfigTypes.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertNull(certificateDataConfigTextArea.getText(), "Should not include text"),
                    () -> assertNull(certificateDataConfigTextArea.getDescription(), "Should not include description"),
                    () -> assertNull(certificateDataConfigTextArea.getHeader(), "Should not include a header"),
                    () -> assertNull(certificateDataConfigTextArea.getLabel(), "Should not include a label"),
                    () -> assertNull(certificateDataConfigTextArea.getIcon(), "Should not include icon"),
                    () -> assertEquals(OVRIGT_SVAR_JSON_ID_25, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValueText() {
                final var expectedText = "Text value for question";
                internalCertificate = LisjpUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setOvrigt(expectedText)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(OVRIGT_SVAR_ID_25);

                final var certificateDataValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(OVRIGT_SVAR_JSON_ID_25, certificateDataValue.getId()),
                    () -> assertEquals(expectedText, certificateDataValue.getText())
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

                final var question = certificate.getData().get(OVRIGT_SVAR_ID_25);

                final var certificateDataValue = (CertificateDataTextValue) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(OVRIGT_SVAR_JSON_ID_25, certificateDataValue.getId()),
                    () -> assertNull(certificateDataValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationText() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(OVRIGT_SVAR_ID_25);

                final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(OVRIGT_SVAR_JSON_ID_25, certificateDataValidationText.getId()),
                    () -> assertEquals(3296, certificateDataValidationText.getLimit())
                );
            }
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
                    () -> assertEquals(KONTAKT_CATEGORY_ID, question.getParent()),
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

            @Test
            void shouldIncludeValidationHide() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26);

                final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                    () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
                );
            }
        }
    }
}