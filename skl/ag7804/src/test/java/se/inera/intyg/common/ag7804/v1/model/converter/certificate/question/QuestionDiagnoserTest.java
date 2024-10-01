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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_DIAGNOS;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;

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
import se.inera.intyg.common.ag7804.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.ag7804.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationEnable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionDiagnoserTest {

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
            final var expectedIndex = 10;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

            assertAll("Validating question",
                () -> assertEquals(DIAGNOS_SVAR_ID_6, question.getId()),
                () -> assertEquals(expectedIndex, question.getIndex()),
                () -> assertEquals(CATEGORY_DIAGNOS, question.getParent(), "Invalid parent"),
                () -> assertNotNull(question.getValue(), "Missing value"),
                () -> assertNotNull(question.getValidation(), "Missing validation"),
                () -> assertNotNull(question.getConfig(), "Missing config")
            );
        }

        @Test
        void shouldIncludeQuestionConfig() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

            assertEquals(CertificateDataConfigType.UE_DIAGNOSES, question.getConfig().getType());

            final var certificateDataConfigDiagnoses = (CertificateDataConfigDiagnoses) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertTrue(certificateDataConfigDiagnoses.getText().trim().length() > 0, "Missing text"),
                () -> assertTrue(certificateDataConfigDiagnoses.getDescription().trim().length() > 0, "Missing description"),
                () -> assertEquals("ICD_10_SE", certificateDataConfigDiagnoses.getTerminology().get(0).getId()),
                () -> assertEquals("ICD-10-SE", certificateDataConfigDiagnoses.getTerminology().get(0).getLabel()),
                () -> assertEquals("KSH_97_P", certificateDataConfigDiagnoses.getTerminology().get(1).getId()),
                () -> assertEquals("KSH97-P (Primärvård)", certificateDataConfigDiagnoses.getTerminology().get(1).getLabel()),
                () -> assertEquals("diagnoser[0].row", certificateDataConfigDiagnoses.getList().get(0).getId()),
                () -> assertEquals("diagnoser[1].diagnoskod", certificateDataConfigDiagnoses.getList().get(1).getId()),
                () -> assertEquals("diagnoser[2].diagnoskod", certificateDataConfigDiagnoses.getList().get(2).getId())
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
                () -> assertEquals("diagnoser[0].row", certificateDataConfigDiagnoses.getList().get(0).getId()),
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
                () -> assertEquals("diagnoser[1].diagnoskod", certificateDataConfigDiagnoses.getList().get(0).getId()),
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
                () -> assertEquals("diagnoser[2].diagnoskod", certificateDataConfigDiagnoses.getList().get(0).getId()),
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
                () -> assertEquals("diagnoser[0].row", certificateDataConfigDiagnoses.getList().get(0).getId()),
                () -> assertEquals("diagnoser[1].diagnoskod", certificateDataConfigDiagnoses.getList().get(1).getId()),
                () -> assertEquals("diagnoser[2].diagnoskod", certificateDataConfigDiagnoses.getList().get(2).getId()),
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
                () -> assertEquals("'$diagnoser[0].row'", certificateDataValidationMandatory.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

            final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[2];
            assertAll("Validation question validation",
                () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100, certificateDataValidationHide.getQuestionId()),
                () -> assertEquals("exists(NO)", certificateDataValidationHide.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationEnable() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(DIAGNOS_SVAR_ID_6);

            final var certificateDataValidationEnable = (CertificateDataValidationEnable) question.getValidation()[3];
            assertAll("Validation question validation",
                () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100, certificateDataValidationEnable.getQuestionId()),
                () -> assertEquals("exists(YES) || exists(NO)", certificateDataValidationEnable.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationText() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(RespConstants.DIAGNOS_SVAR_ID_6);

            final var certificateDataValidation = (CertificateDataValidationText) question.getValidation()[1];
            assertAll("Validation question validation",
                () -> assertEquals(81, certificateDataValidation.getLimit())
            );
        }
    }

    @Mock
    WebcertModuleService moduleService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        private static final String DIAGNOSIS_DESCRIPTION = "Beskrivning med egen text";
        private static final String DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION = "Beskrivning utan egen text";
        private Ag7804UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            moduleService = mock(WebcertModuleService.class);
            when(moduleService.getDescriptionFromDiagnosKod(anyString(), anyString())).thenReturn(DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<List<Diagnos>> diagnosisListValues() {
            return Stream.of(Arrays.asList(
                Diagnos.create("F500", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION)
            ), Arrays.asList(
                Diagnos.create("", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION),
                Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION),
                Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION)
            ), Collections.emptyList());
        }

        @ParameterizedTest
        @MethodSource("diagnosisListValues")
        void shouldIncludeDiagnosValue(List<Diagnos> expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDiagnoser.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getDiagnoser());
        }

        @Test
        void shouldIncludeDiagnosValueNull() {
            final var index = 1;
            final List<Diagnos> expectedValue = Collections.emptyList();

            final var certificate = CertificateBuilder.create().addElement(QuestionDiagnoser.toCertificate(null, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);
            assertEquals(expectedValue, updatedCertificate.getDiagnoser());
        }

        @org.junit.Test
        void shouldExcludeDiagnosKodNull() {
            final var index = 1;
            var diagnoser = Arrays.asList(
                Diagnos.create(null, "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION),
                Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION),
                Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DESCRIPTION_WITHOUT_ADDITION));

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDiagnoser.toCertificate(diagnoser, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);
            assertEquals(updatedCertificate.getDiagnoser().size(), 2);
        }
    }
}
