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

package se.inera.intyg.common.ag7804.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_SYSSELSATTNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag7804.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.ag7804.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionSysselsattningTest {

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
    class ToCertificate {

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
                () -> assertEquals(CATEGORY_SYSSELSATTNING, question.getParent()),
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
                () -> assertEquals(
                    "exists(NUVARANDE_ARBETE) || exists(ARBETSSOKANDE) || exists(FORALDRALEDIG) || exists(STUDIER)",
                    certificateDataValidationMandatory.getExpression()
                )
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

    @Mock
    WebcertModuleService moduleService;


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<List<Sysselsattning>> codeListValues() {
            return Stream.of(Arrays.asList(
                Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE),
                Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                Sysselsattning.create(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN),
                Sysselsattning.create(SysselsattningsTyp.STUDIER)
            ), Collections.emptyList());
        }

        @ParameterizedTest
        @MethodSource("codeListValues")
        void shouldIncludeSysselsattningValue(List<Sysselsattning> expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionSysselsattning.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getSysselsattning());
        }

        @Test
        void shouldIncludeAtgardValueNull() {
            final var index = 1;
            final List<Sysselsattning> expectedValue = Collections.emptyList();

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionSysselsattning.toCertificate(null, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getSysselsattning());
        }
    }
}
