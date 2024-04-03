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
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_BEDOMNING;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.ag7804.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.ag7804.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxDateRangeList;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionBehovAvSjukskrivningTest {

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
            final var expectedIndex = 18;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertAll("Validating question",
                () -> assertEquals(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, question.getId()),
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

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertTrue(certificateDataConfigSickLeavePeriod.getText().trim().length() > 0, "Missing text"),
                () -> assertTrue(certificateDataConfigSickLeavePeriod.getDescription().trim().length() > 0, "Missing description")
            );
        }

        @Test
        void shouldIncludeQuestionConfigPreviousSickLeavePeriod() {
            final var expectedPreviousSickLeavePeriod = "På det ursprungliga intyget var slutdatumet för den sista "
                + "sjukskrivningsperioden 2020-01-01 och sjukskrivningsgraden var 75%.";

            internalCertificate.getGrundData().setRelation(new Relation());
            internalCertificate.getGrundData().getRelation().setRelationKod(RelationKod.FRLANG);
            internalCertificate.getGrundData().getRelation().setSistaSjukskrivningsgrad("75%");
            internalCertificate.getGrundData().getRelation()
                .setSistaGiltighetsDatum(LocalDate.parse("2020-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertEquals(expectedPreviousSickLeavePeriod, certificateDataConfigSickLeavePeriod.getPreviousDateRangeText());
        }

        @Test
        void shouldNotIncludeQuestionConfigPreviousSickLeavePeriodIfNotRenewRelation() {
            final String expectedPreviousSickLeavePeriod = null;

            internalCertificate.getGrundData().setRelation(new Relation());
            internalCertificate.getGrundData().getRelation().setRelationKod(RelationKod.ERSATT);
            internalCertificate.getGrundData().getRelation().setSistaSjukskrivningsgrad("75%");
            internalCertificate.getGrundData().getRelation()
                .setSistaGiltighetsDatum(LocalDate.parse("2020-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertEquals(expectedPreviousSickLeavePeriod, certificateDataConfigSickLeavePeriod.getPreviousDateRangeText());
        }

        @Test
        void shouldNotIncludeQuestionConfigPreviousSickLeavePeriodIfNoPeriodOfValidityDate() {
            final String expectedPreviousSickLeavePeriod = null;

            internalCertificate.getGrundData().setRelation(new Relation());
            internalCertificate.getGrundData().getRelation().setRelationKod(RelationKod.FRLANG);
            internalCertificate.getGrundData().getRelation().setSistaSjukskrivningsgrad("75%");
            internalCertificate.getGrundData().getRelation().setSistaGiltighetsDatum(null);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(
                se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertEquals(expectedPreviousSickLeavePeriod, certificateDataConfigSickLeavePeriod.getPreviousDateRangeText());
        }

        @Test
        void shouldNotIncludeQuestionConfigPreviousSickLeavePeriodIfNoRelation() {
            final String expectedPreviousSickLeavePeriod = null;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertEquals(expectedPreviousSickLeavePeriod, certificateDataConfigSickLeavePeriod.getPreviousDateRangeText());
        }

        @Test
        void shouldIncludeQuestionConfigOneFourth() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(certificateDataConfigSickLeavePeriod.getList().get(0).getId(), SjukskrivningsGrad.NEDSATT_1_4.getId()
                ),
                () -> assertTrue(certificateDataConfigSickLeavePeriod.getList().get(0).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfigHalf() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(certificateDataConfigSickLeavePeriod.getList().get(1).getId(),
                    SjukskrivningsGrad.NEDSATT_HALFTEN.getId()),
                () -> assertTrue(certificateDataConfigSickLeavePeriod.getList().get(1).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfigThreeFourth() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(certificateDataConfigSickLeavePeriod.getList().get(2).getId(),
                    SjukskrivningsGrad.NEDSATT_3_4.getId()),
                () -> assertTrue(certificateDataConfigSickLeavePeriod.getList().get(2).getLabel().trim().length() > 0, "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfigWhole() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            assertEquals(CertificateDataConfigTypes.UE_CHECKBOX_DATE_RANGE_LIST, question.getConfig().getType());

            final var certificateDataConfigSickLeavePeriod = (CertificateDataConfigCheckboxDateRangeList) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(certificateDataConfigSickLeavePeriod.getList().get(3).getId(),
                    SjukskrivningsGrad.HELT_NEDSATT.getId()),
                () -> assertTrue(certificateDataConfigSickLeavePeriod.getList().get(3).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionValueOneFourth() {
            final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
            final var expectedGrad = SjukskrivningsGrad.NEDSATT_1_4;
            final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Arrays.asList(expectedDateRange))
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedGrad.getId(),
                    certificateDataValueDateRangeList.getList().get(0).getId()),
                () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getFrom())
            );
        }

        @Test
        void shouldIncludeQuestionValueHalf() {
            final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
            final var expectedGrad = SjukskrivningsGrad.NEDSATT_HALFTEN;
            final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Arrays.asList(expectedDateRange))
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedGrad.getId(),
                    certificateDataValueDateRangeList.getList().get(0).getId()),
                () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getFrom())
            );
        }

        @Test
        void shouldIncludeQuestionValueThreeFourth() {
            final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
            final var expectedGrad = SjukskrivningsGrad.NEDSATT_3_4;
            final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Arrays.asList(expectedDateRange))
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedGrad.getId(),
                    certificateDataValueDateRangeList.getList().get(0).getId()),
                () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getFrom())
            );
        }

        @Test
        void shouldIncludeQuestionValueWhole() {
            final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
            final var expectedGrad = SjukskrivningsGrad.HELT_NEDSATT;
            final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Arrays.asList(expectedDateRange))
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedGrad.getId(),
                    certificateDataValueDateRangeList.getList().get(0).getId()),
                () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getFrom())
            );
        }

        @Test
        void shouldExcludeSickleavePeriodWithPeriodNull() {
            final var expectedGrad = Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT;
            final var expectedDateRange = Sjukskrivning.create(expectedGrad, null);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Arrays.asList(expectedDateRange))
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertEquals(0, certificateDataValueDateRangeList.getList().size());
        }

        @Test
        void shouldExcludeSickleavePeriodWithMissingStart() {
            final var expectedPeriod = new InternalLocalDateInterval();
            expectedPeriod.setFrom(new InternalDate("2021-01-01"));
            final var expectedGrad = SjukskrivningsGrad.HELT_NEDSATT;
            final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Arrays.asList(expectedDateRange))
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate,
                texts);

            final var question = certificate.getData().get(RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertEquals(0, certificateDataValueDateRangeList.getList().size());
        }

        @Test
        void shouldExcludeSickleavePeriodWithMissingEnd() {
            final var expectedPeriod = new InternalLocalDateInterval();
            expectedPeriod.setTom(new InternalDate("2021-01-01"));
            final var expectedGrad = SjukskrivningsGrad.HELT_NEDSATT;
            final var expectedDateRange = Sjukskrivning.create(expectedGrad, expectedPeriod);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Arrays.asList(expectedDateRange))
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate,
                texts);

            final var question = certificate.getData().get(RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertEquals(0, certificateDataValueDateRangeList.getList().size());
        }

        @Test
        void shouldIncludeQuestionValueAllOfThem() {
            final var expectedPeriod = new InternalLocalDateInterval("2021-01-01", "2021-02-02");
            final var expectedGradOneFourth = SjukskrivningsGrad.NEDSATT_1_4;
            final var expectedDateRangeOneFourth = Sjukskrivning.create(expectedGradOneFourth, expectedPeriod);
            final var expectedGradThreeFourth = SjukskrivningsGrad.NEDSATT_3_4;
            final var expectedDateRangeThreeFourth = Sjukskrivning.create(expectedGradThreeFourth, expectedPeriod);
            final var expectedGradHalf = SjukskrivningsGrad.NEDSATT_HALFTEN;
            final var expectedDateRangeHalf = Sjukskrivning.create(expectedGradHalf, expectedPeriod);
            final var expectedGradWhole = SjukskrivningsGrad.HELT_NEDSATT;
            final var expectedDateRangeWhole = Sjukskrivning.create(expectedGradWhole, expectedPeriod);
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Arrays
                    .asList(expectedDateRangeOneFourth, expectedDateRangeThreeFourth, expectedDateRangeHalf, expectedDateRangeWhole))
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedGradOneFourth.getId(),
                    certificateDataValueDateRangeList.getList().get(0).getId()),
                () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(0).getTo()),
                () -> assertEquals(expectedPeriod.getFrom().asLocalDate(),
                    certificateDataValueDateRangeList.getList().get(0).getFrom()),
                () -> assertEquals(expectedGradThreeFourth.getId(),
                    certificateDataValueDateRangeList.getList().get(1).getId()),
                () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(1).getTo()),
                () -> assertEquals(expectedPeriod.getFrom().asLocalDate(),
                    certificateDataValueDateRangeList.getList().get(1).getFrom()),
                () -> assertEquals(expectedGradHalf.getId(),
                    certificateDataValueDateRangeList.getList().get(2).getId()),
                () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(2).getTo()),
                () -> assertEquals(expectedPeriod.getFrom().asLocalDate(),
                    certificateDataValueDateRangeList.getList().get(2).getFrom()),
                () -> assertEquals(expectedGradWhole.getId(),
                    certificateDataValueDateRangeList.getList().get(3).getId()),
                () -> assertEquals(expectedPeriod.getTom().asLocalDate(), certificateDataValueDateRangeList.getList().get(3).getTo()),
                () -> assertEquals(expectedPeriod.getFrom().asLocalDate(), certificateDataValueDateRangeList.getList().get(3).getFrom())
            );
        }

        @Test
        void shouldIncludeQuestionValueNone() {
            internalCertificate = Ag7804UtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setSjukskrivningar(Collections.emptyList())
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValueDateRangeList = (CertificateDataValueDateRangeList) question.getValue();
            assertEquals(0, certificateDataValueDateRangeList.getList().size());
        }

        @Test
        void shouldIncludeQuestionValidationMandatory() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, certificateDataValidationMandatory.getQuestionId()),
                () -> assertEquals("$" + SjukskrivningsGrad.NEDSATT_1_4.getId()
                        + " || $" + SjukskrivningsGrad.NEDSATT_HALFTEN.getId()
                        + " || $" + SjukskrivningsGrad.NEDSATT_3_4.getId()
                        + " || $" + SjukskrivningsGrad.HELT_NEDSATT.getId(),
                    certificateDataValidationMandatory.getExpression())
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

        Stream<List<Sjukskrivning>> sickLeaveValues() {
            return Stream.of(
                Arrays.asList(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now())
                        )
                    ),
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now())
                        )
                    )
                ), Collections.emptyList()
            );
        }

        @ParameterizedTest
        @MethodSource("sickLeaveValues")
        void shouldIncludeBehovAvSjukskrivningValue(List<Sjukskrivning> expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                    QuestionBehovAvSjukskrivning.toCertificate(expectedValue, index, texts, internalCertificate.getGrundData()
                        .getRelation()))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getSjukskrivningar());
        }

        @Test
        void shouldIncludeBehovAvSjukskrivningValueNull() {
            final var index = 1;

            final var certificate = CertificateBuilder.create().addElement(
                    QuestionBehovAvSjukskrivning.toCertificate(null, index, texts, internalCertificate.getGrundData()
                        .getRelation()))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(Collections.emptyList(), updatedCertificate.getSjukskrivningar());
        }
    }
}
