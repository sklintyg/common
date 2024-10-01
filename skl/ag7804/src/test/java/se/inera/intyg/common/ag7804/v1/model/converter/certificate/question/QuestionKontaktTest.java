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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_103;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.KONTAKT_CATEGORY_ID;

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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionKontaktTest {

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
            final var expectedIndex = 31;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_103);

            assertAll("Validating question",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_ID_103, question.getId()),
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

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_103);

            assertEquals(CertificateDataConfigType.UE_CHECKBOX_BOOLEAN, question.getConfig().getType());

            final var certificateDataConfigCheckboxBoolean = (CertificateDataConfigCheckboxBoolean) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertFalse(certificateDataConfigCheckboxBoolean.getText().trim().isEmpty(), "Missing text"),
                () -> assertFalse(certificateDataConfigCheckboxBoolean.getLabel().trim().isEmpty(), "Missing label"),
                () -> assertNull(certificateDataConfigCheckboxBoolean.getDescription(), "Should not have description"),
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_JSON_ID_103, certificateDataConfigCheckboxBoolean.getId()),
                () -> assertFalse(certificateDataConfigCheckboxBoolean.getSelectedText().trim().isEmpty(), "Missing selected text"),
                () -> assertFalse(certificateDataConfigCheckboxBoolean.getUnselectedText().trim().isEmpty(), "Missing unselected text")
            );
        }

        @Test
        void shouldIncludeQuestionValueTrue() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setKontaktMedAg(true)
                .build();
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_103);

            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_JSON_ID_103, certificateDataValueBoolean.getId()),
                () -> assertEquals(internalCertificate.getKontaktMedAg(), certificateDataValueBoolean.getSelected())
            );
        }

        @Test
        void shouldIncludeQuestionValueFalseAsEmpty() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setKontaktMedAg(false)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_103);

            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_JSON_ID_103, certificateDataValueBoolean.getId()),
                () -> assertNull(certificateDataValueBoolean.getSelected())
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

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_103);

            final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(KONTAKT_ONSKAS_SVAR_JSON_ID_103, certificateDataValueBoolean.getId()),
                () -> assertNull(certificateDataValueBoolean.getSelected())
            );
        }

        @Test
        void shouldIncludeValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_103);

            final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
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
            return Stream.of(true, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeKontaktValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(
                    QuestionKontakt.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getKontaktMedAg());
        }

        @Test
        void shouldIncludeKontaktValueFalseAsNull() {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(
                    QuestionKontakt.toCertificate(false, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertNull(updatedCertificate.getKontaktMedAg());
        }
    }
}
