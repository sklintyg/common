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
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElementStyleEnum;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDropdown;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationEnable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionPrognosTimePeriodTest {

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
    class QuestionPrognosTimeperiod {

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
            final var expectedIndex = 25;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            assertAll("Validating question",
                () -> assertEquals(PROGNOS_BESKRIVNING_DELSVAR_ID_39, question.getId()),
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

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertNull(certificateDataConfigDropdown.getText(), "Should not have text"),
                () -> assertNull(certificateDataConfigDropdown.getDescription(), "Should not have description"),
                () -> assertNull(certificateDataConfigDropdown.getHeader(), "Should not have a header"),
                () -> assertNull(certificateDataConfigDropdown.getIcon(), "Should not have an iconr"),
                () -> assertNull(certificateDataConfigDropdown.getLabel(), "Should not have a label")
            );
        }

        @Test
        void shouldIncludeQuestionConfigDefaultChoice() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals("",
                    certificateDataConfigDropdown.getList().get(0).getId()),
                () -> assertTrue(certificateDataConfigDropdown.getList().get(0).getLabel().trim().length() > 0,
                    "Missing label"),
                () -> assertEquals("Välj tidsperiod",
                    certificateDataConfigDropdown.getList().get(0).getLabel())
            );
        }

        @Test
        void shouldIncludeQuestionConfig30Days() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_30.getId(),
                    certificateDataConfigDropdown.getList().get(1).getId()),
                () -> assertTrue(certificateDataConfigDropdown.getList().get(1).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfig60Days() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_60.getId(),
                    certificateDataConfigDropdown.getList().get(2).getId()),
                () -> assertTrue(certificateDataConfigDropdown.getList().get(2).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfig90Days() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_90.getId(),
                    certificateDataConfigDropdown.getList().get(3).getId()),
                () -> assertTrue(certificateDataConfigDropdown.getList().get(3).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfig180Days() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_180.getId(),
                    certificateDataConfigDropdown.getList().get(4).getId()),
                () -> assertTrue(certificateDataConfigDropdown.getList().get(4).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionConfig365Days() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            assertEquals(CertificateDataConfigTypes.UE_DROPDOWN, question.getConfig().getType());

            final var certificateDataConfigDropdown = (CertificateDataConfigDropdown) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertEquals(PrognosDagarTillArbeteTyp.DAGAR_365.getId(),
                    certificateDataConfigDropdown.getList().get(5).getId()),
                () -> assertTrue(certificateDataConfigDropdown.getList().get(5).getLabel().trim().length() > 0,
                    "Missing label")
            );
        }

        @Test
        void shouldIncludeQuestionValue30Days() {
            final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValue60Days() {
            final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_60);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValue90Days() {
            final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_90);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValue180Days() {
            final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_180);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValue365Days() {
            final var expectedPrognos = Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_365);
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setPrognos(expectedPrognos)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataValueCode = (CertificateDataValueCode) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getId()),
                () -> assertEquals(expectedPrognos.getDagarTillArbete().getId(), certificateDataValueCode.getCode())
            );
        }

        @Test
        void shouldIncludeQuestionValidationMandatory() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[1];
            assertAll("Validation question validation",
                () -> assertEquals(PROGNOS_BESKRIVNING_DELSVAR_ID_39, certificateDataValidationMandatory.getQuestionId()),
                () -> assertEquals("$TRETTIO_DGR || $SEXTIO_DGR || $NITTIO_DGR || $HUNDRAATTIO_DAGAR || $TREHUNDRASEXTIOFEM_DAGAR",
                    certificateDataValidationMandatory.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationEnable() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataValidationEnable = (CertificateDataValidationEnable) question.getValidation()[2];
            assertAll("Validation question validation",
                () -> assertEquals(PROGNOS_SVAR_ID_39, certificateDataValidationEnable.getQuestionId()),
                () -> assertEquals("$" + PrognosTyp.ATER_X_ANTAL_DGR.getId(),
                    certificateDataValidationEnable.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionConfigHiddenStyle() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39);

            final var certificateDataConfigStyle = question.getStyle();
            assertAll("Validation question config style",
                () -> assertEquals(certificateDataConfigStyle, CertificateDataElementStyleEnum.HIDDEN)
            );
        }
    }
}
