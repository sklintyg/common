/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_GRUNDFORMU;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import java.time.LocalDate;
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
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionIntygetBaseratPaTest {

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
            final var expectedIndex = 3;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

            assertAll("Validating question",
                () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, question.getId()),
                () -> assertEquals(expectedIndex, question.getIndex()),
                () -> assertEquals(CATEGORY_GRUNDFORMU, question.getParent()),
                () -> assertNotNull(question.getValue(), "Missing value"),
                () -> assertNotNull(question.getValidation(), "Missing validation"),
                () -> assertNotNull(question.getConfig(), "Missing config")
            );
        }

        @Test
        void shouldIncludeQuestionConfigUndersokningPatient() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

            assertEquals(CertificateDataConfigType.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

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

            assertEquals(CertificateDataConfigType.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

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

            assertEquals(CertificateDataConfigType.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

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

            assertEquals(CertificateDataConfigType.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());

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
        void shouldExcludeQuestionValueUndersokningPatientenWhenDateIsNotValid() {
            final var expectedDate = new InternalDate("2022-");
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setUndersokningAvPatienten(expectedDate)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate,
                texts);

            final var question = certificate.getData().get(RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

            final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
            assertEquals(0, certificateDataValueDateList.getList().size());
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
        void shouldExcludeQuestionValueTelefonkontaktWhenDateIsNotValid() {
            final var expectedDate = new InternalDate("2022-");
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setTelefonkontaktMedPatienten(expectedDate)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate,
                texts);

            final var question = certificate.getData().get(RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

            final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
            assertEquals(0, certificateDataValueDateList.getList().size());
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
        void shouldExcludeQuestionValueJournaluppgifterWhenDateIsNotValid() {
            final var expectedDate = new InternalDate("2022-");
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setJournaluppgifter(expectedDate)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate,
                texts);

            final var question = certificate.getData().get(RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

            final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
            assertEquals(0, certificateDataValueDateList.getList().size());
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
        void shouldIncludeQuestionValueAnnatWhenDateIsNotValid() {
            final var expectedDate = new InternalDate("2022-");
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setAnnatGrundForMU(expectedDate)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate,
                texts);

            final var question = certificate.getData().get(RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

            final var certificateDataValueDateList = (CertificateDataValueDateList) question.getValue();
            assertEquals(0, certificateDataValueDateList.getList().size());
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
                () -> assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1
                        + " || " + GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1
                        + " || " + GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1
                        + " || " + GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1,
                    certificateDataValidationMandatory.getExpression())
            );
        }

        @Test
        void shouldIncludeCategoryValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);

            final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
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

        Stream<InternalDate> dateValues() {
            return Stream.of(new InternalDate(LocalDate.now().plusMonths(10)), new InternalDate(LocalDate.now()), null);
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUUndersokningValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setUndersokningAvPatienten(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(
                    se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionIntygetBaseratPa.toCertificate(utlatande,
                        index, texts)).build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getUndersokningAvPatienten());
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUTelefonkontaktValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setTelefonkontaktMedPatienten(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(
                    se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionIntygetBaseratPa.toCertificate(utlatande,
                        index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getTelefonkontaktMedPatienten());
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUJournalUppgifterValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setJournaluppgifter(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(
                    se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionIntygetBaseratPa.toCertificate(utlatande,
                        index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getJournaluppgifter());
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUAnnatValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                Ag7804UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setAnnatGrundForMU(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(
                    se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionIntygetBaseratPa.toCertificate(utlatande,
                        index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAnnatGrundForMU());
        }
    }

}
