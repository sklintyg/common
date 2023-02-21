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

package se.inera.intyg.common.ag7804.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_DIAGNOS;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NO_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.YES_ID;

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
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHighlight;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionDiagnosOnskasFormedlasTest {

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
            final var expectedIndex = 9;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

            assertAll("Validating question",
                () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100, question.getId()),
                () -> assertEquals(expectedIndex, question.getIndex()),
                () -> assertEquals(CATEGORY_DIAGNOS, question.getParent()),
                () -> assertNotNull(question.getValue(), "Missing value"),
                () -> assertNotNull(question.getValidation(), "Missing validation"),
                () -> assertNotNull(question.getConfig(), "Missing config")
            );
        }

        @Test
        void shouldIncludeQuestionConfig() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

            assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE, question.getConfig().getType());

            final var certificateDataConfigRadioCode = (CertificateDataConfigRadioMultipleCode) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertTrue(certificateDataConfigRadioCode.getText().trim().length() > 0, "Missing text"),
                () -> assertNull(certificateDataConfigRadioCode.getDescription(), "Should not include description"),
                () -> assertNull(certificateDataConfigRadioCode.getHeader(), "Should not include header"),
                () -> assertNull(certificateDataConfigRadioCode.getLabel(), "Should not include label"),
                () -> assertTrue(certificateDataConfigRadioCode.getList().size() == 2, "Wrong number of codes")
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

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(YES_ID, certificateDataValueCode.getId()),
                () -> assertEquals(YES_ID, certificateDataValueCode.getCode())
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

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(NO_ID, certificateDataValueCode.getId()),
                () -> assertEquals(NO_ID, certificateDataValueCode.getCode())
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

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertNull(certificateDataValueCode.getId()),
                () -> assertNull(certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValidationMandatory() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100, certificateDataValidationMandatory.getQuestionId()),
                () -> assertEquals("exists(YES) || exists(NO)", certificateDataValidationMandatory.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationHighlight() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);

            final var certificateDataValidationHighlight = (CertificateDataValidationHighlight) question.getValidation()[1];
            assertAll("Validation question validation",
                () -> assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100, certificateDataValidationHighlight.getQuestionId()),
                () -> assertEquals(
                    "exists(YES) || exists(NO) || !exists(YES) || !exists(NO)",
                    certificateDataValidationHighlight.getExpression()
                )
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

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludePatientWantsDiagnosesIncludedValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(
                    QuestionDiagnosOnskasFormedlas.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getOnskarFormedlaDiagnos());
        }

        @org.junit.Test
        void shouldIncludePatientWantsDiagnosesIncludedNullValue() {
            final var index = 1;
            final var expectedValue = false;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDiagnosOnskasFormedlas.toCertificate(null, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getOnskarFormedlaDiagnos());
        }
    }
}
