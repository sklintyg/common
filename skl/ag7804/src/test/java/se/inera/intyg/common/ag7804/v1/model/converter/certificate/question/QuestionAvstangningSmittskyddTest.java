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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;

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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionAvstangningSmittskyddTest {

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
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeAvstangningSmittskyddValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                    QuestionAvstangningSmittskydd.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAvstangningSmittskydd());
        }
    }
}
