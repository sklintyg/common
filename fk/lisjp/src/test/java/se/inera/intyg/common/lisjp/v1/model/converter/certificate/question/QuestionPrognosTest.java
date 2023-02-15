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
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionPrognosTest {

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
            final var expectedIndex = 24;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            assertAll("Validating question",
                () -> assertEquals(PROGNOS_SVAR_ID_39, question.getId()),
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

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE_OPTIONAL_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigMultipleCodeOptionalDropdown = (CertificateDataConfigRadioMultipleCodeOptionalDropdown) question
                .getConfig();
            assertAll("Validating question configuration",
                () -> assertTrue(certificateDataConfigMultipleCodeOptionalDropdown.getText().trim().length() > 0, "Missing text"),
                () -> assertTrue(certificateDataConfigMultipleCodeOptionalDropdown.getDescription().trim().length() > 0,
                    "Missing description"),
                () -> assertNull(certificateDataConfigMultipleCodeOptionalDropdown.getHeader(), "Should not have a header"),
                () -> assertNull(certificateDataConfigMultipleCodeOptionalDropdown.getIcon(), "Should not have an iconr"),
                () -> assertNull(certificateDataConfigMultipleCodeOptionalDropdown.getLabel(), "Should not have a label")
            );
        }

        @Test
        void shouldIncludeQuestionConfigStorSannolikhet() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE_OPTIONAL_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigMultipleCodeOptionalDropdown = (CertificateDataConfigRadioMultipleCodeOptionalDropdown) question
                .getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosTyp.MED_STOR_SANNOLIKHET.getId(),
                    certificateDataConfigMultipleCodeOptionalDropdown.getList().get(0).getId()),
                () -> assertTrue(certificateDataConfigMultipleCodeOptionalDropdown.getList().get(0).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfigAntalDagar() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE_OPTIONAL_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigMultipleCodeOptionalDropdown = (CertificateDataConfigRadioMultipleCodeOptionalDropdown) question
                .getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosTyp.ATER_X_ANTAL_DGR.getId(),
                    certificateDataConfigMultipleCodeOptionalDropdown.getList().get(1).getId()),
                () -> assertEquals(RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39,
                    certificateDataConfigMultipleCodeOptionalDropdown.getList().get(1).getDropdownQuestionId(),
                    "missing dropdown question id"),
                () -> assertTrue(certificateDataConfigMultipleCodeOptionalDropdown.getList().get(1).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfigSannoliktInte() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE_OPTIONAL_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigMultipleCodeOptionalDropdown = (CertificateDataConfigRadioMultipleCodeOptionalDropdown) question
                .getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId(),
                    certificateDataConfigMultipleCodeOptionalDropdown.getList().get(2).getId()),
                () -> assertTrue(certificateDataConfigMultipleCodeOptionalDropdown.getList().get(2).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfigPrognosOklar() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE_OPTIONAL_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigMultipleCodeOptionalDropdown = (CertificateDataConfigRadioMultipleCodeOptionalDropdown) question
                .getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosTyp.PROGNOS_OKLAR.getId(),
                    certificateDataConfigMultipleCodeOptionalDropdown.getList().get(3).getId()),
                () -> assertTrue(certificateDataConfigMultipleCodeOptionalDropdown.getList().get(3).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionValueStorSannolikhet() {
            final var expectedPrognos = Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, null);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValueAntalDagar() {
            final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValueSannoliktInte() {
            final var expectedPrognos = Prognos.create(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING, null);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValuePrognosOklar() {
            final var expectedPrognos = Prognos.create(PrognosTyp.PROGNOS_OKLAR, null);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getTyp().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValidationMandatory() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(PROGNOS_SVAR_ID_39, certificateDataValidationMandatory.getQuestionId()),
                () -> assertEquals("$STOR_SANNOLIKHET || $ATER_X_ANTAL_DGR || $SANNOLIKT_INTE || $PROGNOS_OKLAR",
                    certificateDataValidationMandatory.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_SVAR_ID_39);

            final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
            assertAll("Validation question validation",
                () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
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

        Stream<Prognos> prognosValues() {
            return Stream.of(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, null),
                Prognos.create(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING, null),
                Prognos.create(PrognosTyp.PROGNOS_OKLAR, null),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_60),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_90),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_180),
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_365), null);
        }

        @ParameterizedTest
        @MethodSource("prognosValues")
        void shouldIncludePrognosValue(Prognos expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionPrognos.toCertificate(expectedValue, index, texts))
                .addElement(QuestionPrognosTimePeriod.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getPrognos());
        }
    }
}
