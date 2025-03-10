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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp;
import se.inera.intyg.common.luae_na.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

@ExtendWith(MockitoExtension.class)
class QuestionUnderlagBaseratPaTest {

    @Mock
    private CertificateTextProvider texts;

    @Mock
    private WebcertModuleService moduleService;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;

            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, expectedIndex,
                texts);

            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            assertEquals(GRUNDFORMU_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeConfigCertificateDataConfigCheckboxMultipleDate() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            assertEquals(CertificateDataConfigType.UE_CHECKBOX_MULTIPLE_DATE, question.getConfig().getType());
        }

        @Test
        void shouldIncludeConfigText() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");

            verify(texts, atLeastOnce()).get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID);
        }

        @Test
        void shouldIncludeConfigList() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            final var config = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();

            assertFalse(config.getList().isEmpty());
        }

        @Test
        void shouldIncludeConfigListOfCheckboxMultipleDateWithCorrectIds() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            final var config = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();

            assertAll(
                () -> assertEquals(config.getList().get(0).getId(), GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1),
                () -> assertEquals(config.getList().get(1).getId(), GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1),
                () -> assertEquals(config.getList().get(2).getId(), GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1),
                () -> assertEquals(config.getList().get(3).getId(), GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
            );
        }

        @Test
        void shouldIncludeConfigListOfCheckboxMultipleDateWithCorrectLabels() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            verify(texts, atLeastOnce()).get(GRUNDFORMU_UNDERSOKNING_LABEL);
            verify(texts, atLeastOnce()).get(GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL);
            verify(texts, atLeastOnce()).get(GRUNDFORMU_JOURNALUPPGIFTER_LABEL);
            verify(texts, atLeastOnce()).get(GRUNDFORMU_ANNAT_LABEL);
        }

        @Test
        void shouldIncludeConfigListOfCheckboxMultipleDateWithMaxDate() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            final var config = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();

            assertAll(
                () -> assertEquals(LocalDate.now(), config.getList().get(0).getMaxDate()),
                () -> assertEquals(LocalDate.now(), config.getList().get(1).getMaxDate()),
                () -> assertEquals(LocalDate.now(), config.getList().get(2).getMaxDate()),
                () -> assertEquals(LocalDate.now(), config.getList().get(3).getMaxDate())
            );
        }

        @Test
        void shouldIncludeDateListValueType() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            assertEquals(CertificateDataValueType.DATE_LIST, question.getValue().getType());
        }

        @Test
        void shouldIncludeDateListValueList() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );

            final var certificateDataValueListDate = (CertificateDataValueDateList) question.getValue();

            assertNotNull(certificateDataValueListDate.getList());
        }

        @Test
        void shouldIncludeDateListValueListIds() {
            final var question = QuestionUnderlagBaseratPa.toCertificate(new InternalDate(LocalDate.now()),
                new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now()), 0, texts
            );

            final var certificateDataValueListDate = (CertificateDataValueDateList) question.getValue();

            assertAll(
                () -> assertEquals(certificateDataValueListDate.getList().get(0).getId(),
                    GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1),
                () -> assertEquals(certificateDataValueListDate.getList().get(1).getId(),
                    GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1),
                () -> assertEquals(certificateDataValueListDate.getList().get(2).getId(),
                    GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1),
                () -> assertEquals(certificateDataValueListDate.getList().get(3).getId(), GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
            );
        }

        @Test
        void shouldIncludeValidationMandatoryExpression() {
            final var expectedExpression =
                GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1 + " || "
                    + GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1 + " || "
                    + GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1 + " || "
                    + GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
            final var question = QuestionUnderlagBaseratPa.toCertificate(null, null, null, null, 0, texts
            );
            final var mandatoryValidation = (CertificateDataValidationMandatory) question.getValidation()[0];

            assertEquals(expectedExpression, mandatoryValidation.getExpression());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        Stream<InternalDate> dateValues() {
            return Stream.of(new InternalDate(LocalDate.now().plusMonths(10)), new InternalDate(LocalDate.now()), null);
        }

        @ParameterizedTest
        @MethodSource("dateValues")
        void shouldIncludeGrundForMUUndersokningValue(InternalDate expectedValue) {
            final var index = 1;

            final var utlatande =
                LuaenaUtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(new GrundData())
                    .setUndersokningAvPatienten(expectedValue)
                    .setJournaluppgifter(expectedValue)
                    .setAnhorigsBeskrivningAvPatienten(expectedValue)
                    .setAnnatGrundForMU(expectedValue)
                    .build();

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionUnderlagBaseratPa.toCertificate(utlatande.getUndersokningAvPatienten(), utlatande.getJournaluppgifter(),
                    utlatande.getAnhorigsBeskrivningAvPatienten(), utlatande.getAnnatGrundForMU(), index, texts
                ))
                .addElement(QuestionUnderlag.toCertificate(List.of(
                    Underlag.create(
                        UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    ),
                    Underlag.create(
                        UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    ),
                    Underlag.create(
                        UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    )), index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, utlatande, moduleService);

            assertAll(
                () -> assertEquals(expectedValue, updatedCertificate.getUndersokningAvPatienten()),
                () -> assertEquals(expectedValue, updatedCertificate.getJournaluppgifter()),
                () -> assertEquals(expectedValue, updatedCertificate.getAnhorigsBeskrivningAvPatienten()),
                () -> assertEquals(expectedValue, updatedCertificate.getAnnatGrundForMU())
            );
        }
    }
}
