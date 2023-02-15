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
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ATGARDER_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisableSubElement;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
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

            final var certificateDataValidationDisableSubElement =
                (CertificateDataValidationDisableSubElement) question.getValidation()[1];
            assertAll("Validation question validation",
                () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, certificateDataValidationDisableSubElement.getQuestionId()),
                () -> assertTrue(certificateDataValidationDisableSubElement.getId().size() == 1),
                () -> assertEquals("EJ_AKTUELLT", certificateDataValidationDisableSubElement.getId().get(0)),
                () -> assertEquals("$ARBETSTRANING || $ARBETSANPASSNING || $SOKA_NYTT_ARBETE || $BESOK_ARBETSPLATS "
                        + "|| $ERGONOMISK || $HJALPMEDEL || $KONFLIKTHANTERING || $KONTAKT_FHV || $OMFORDELNING || $OVRIGA_ATGARDER",
                    certificateDataValidationDisableSubElement.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationDisableAll() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);

            final var certificateDataValidationDisableSubElement =
                (CertificateDataValidationDisableSubElement) question.getValidation()[2];
            assertAll("Validation question validation",
                () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, certificateDataValidationDisableSubElement.getQuestionId()),
                () -> assertTrue(certificateDataValidationDisableSubElement.getId().size() == 10),
                () -> assertEquals("$EJ_AKTUELLT",
                    certificateDataValidationDisableSubElement.getExpression())
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
                () -> assertEquals(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, question.getParent()),
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
                () -> assertEquals(2700, certificateDataValidationText.getLimit())
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

