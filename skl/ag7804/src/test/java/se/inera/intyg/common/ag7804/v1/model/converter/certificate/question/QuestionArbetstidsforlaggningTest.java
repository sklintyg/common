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
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_BEDOMNING;

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
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.QuestionArbetstidsforlaggning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionArbetstidsforlaggningTest {

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
            final var expectedIndex = 20;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

            assertAll("Validating question",
                () -> assertEquals(ARBETSTIDSFORLAGGNING_SVAR_ID_33, question.getId()),
                () -> assertEquals(expectedIndex, question.getIndex()),
                () -> assertEquals(CATEGORY_BEDOMNING, question.getParent()),
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
            internalCertificate = Ag7804UtlatandeV1.builder()
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
            internalCertificate = Ag7804UtlatandeV1.builder()
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
            internalCertificate = Ag7804UtlatandeV1.builder()
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
                    "exists(EN_FJARDEDEL) || exists(HALFTEN) || exists(TRE_FJARDEDEL)",
                    certificateDataValidationShow.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationMandatory() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

            final var certificateDataValidation = (CertificateDataValidationMandatory) question.getValidation()[1];
            assertAll("Validation question validation",
                () -> assertEquals(ARBETSTIDSFORLAGGNING_SVAR_ID_33, certificateDataValidation.getQuestionId()),
                () -> assertEquals("exists(" + ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33 + ")",
                    certificateDataValidation.getExpression())
            );
        }

        @Test
        void shouldIncludeCategoryValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33);

            final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[2];
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

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeArbetstidsforlaggningValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                    QuestionArbetstidsforlaggning.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getArbetstidsforlaggning());
        }
    }
}
