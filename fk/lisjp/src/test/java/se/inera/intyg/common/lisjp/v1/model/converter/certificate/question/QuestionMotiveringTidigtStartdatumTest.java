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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import se.inera.intyg.common.lisjp.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionMotiveringTidigtStartdatumTest {

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
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        @Mock
        WebcertModuleService moduleService;
        private LisjpUtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeMotiveringTidigtStartdatumValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionMotiveringTidigtStartdatum.toCertificate(expectedValue, index))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            if (expectedValue == null || expectedValue.isEmpty()) {
                assertNull(updatedCertificate.getMotiveringTillTidigtStartdatumForSjukskrivning());
            } else {
                assertEquals(expectedValue, updatedCertificate.getMotiveringTillTidigtStartdatumForSjukskrivning());
            }
        }
    }
}
