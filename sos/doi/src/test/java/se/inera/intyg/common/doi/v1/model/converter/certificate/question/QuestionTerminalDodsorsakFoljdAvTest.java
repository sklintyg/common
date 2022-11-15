/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_AV_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_B_DATUM_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_B_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_B_LABEL;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_C_DATUM_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_C_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_C_LABEL;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_D_DATUM_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_D_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_D_LABEL;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_KRONISK;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_PLOTSLIG;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_JSON_ID;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionTerminalDodsorsakFoljdAvTest {

    @Mock
    private CertificateTextProvider texts;

    private Dodsorsak causeOfDeathEmpty;
    private List<CertificateDataConfigCode> allSpecifications;


    @BeforeEach
    void setup() {
        causeOfDeathEmpty = Dodsorsak.create(null, null, null);
        allSpecifications = List.of(
            CertificateDataConfigCode.builder()
                .id(Specifikation.PLOTSLIG.name())
                .label(FOLJD_OM_DELSVAR_PLOTSLIG)
                .code(Specifikation.PLOTSLIG.name())
                .build(),
            CertificateDataConfigCode.builder()
                .id(Specifikation.KRONISK.name())
                .label(FOLJD_OM_DELSVAR_KRONISK)
                .code(Specifikation.KRONISK.name())
                .build(),
            CertificateDataConfigCode.builder()
                .id(Specifikation.UPPGIFT_SAKNAS.name())
                .label(FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS)
                .code(Specifikation.UPPGIFT_SAKNAS.name())
                .build()
        );
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToCertificate {

        Stream<String> delsvarIdStream() {
           return List.of(FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_C_ID, FOLJD_OM_DELSVAR_D_ID).stream();
        }

        @ParameterizedTest
        @MethodSource("delsvarIdStream")
        void shouldIncludeQuestionId(String delsvarId) {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, delsvarId, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            CertificateDataConfigCauseOfDeath causeOfDeath = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(delsvarId, question.getId());
            assertEquals(delsvarId, causeOfDeath.getCauseOfDeath().getDescriptionId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, expectedIndex, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            assertEquals(expectedIndex, question.getIndex());
        }

       @Test
       void shouldIncludeParentId() {
           final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
               causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
           assertEquals(DODSORSAK_SVAR_ID, question.getParent());
       }

        @Test
        void shouldIncludeText() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FOLJD_AV_QUESTION_TEXT_ID);
        }

        Stream<String> dodsOrsakerLabelStream() {
           return List.of(FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_C_LABEL, FOLJD_OM_DELSVAR_D_LABEL).stream();
        }

        @ParameterizedTest
        @MethodSource("dodsOrsakerLabelStream")
        void shouldIncludeLabel(String label) {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, label, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(config.getLabel(), label);
        }

        @Test
        void shouldIncludeTerminalCauseOfDeathConfigType() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            assertEquals(CertificateDataConfigTypes.UE_CAUSE_OF_DEATH, question.getConfig().getType());
        }

        @Test
        void shouldIncludeTerminalCauseOfDeathList() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertNotNull(config.getCauseOfDeath());
        }

        @Test
        void shouldIncludeCorrectConfigId() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(TERMINAL_DODSORSAK_JSON_ID, config.getCauseOfDeath().getId());
        }

        @Test
        void shouldIncludeCorrectConfigDescriptionId() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(FOLJD_OM_DELSVAR_B_ID, config.getCauseOfDeath().getDescriptionId());
        }

        Stream<String> debutIdStream() {
            return List.of(FOLJD_OM_DELSVAR_B_DATUM_ID, FOLJD_OM_DELSVAR_C_DATUM_ID, FOLJD_OM_DELSVAR_D_DATUM_ID).stream();
        }

        @ParameterizedTest
        @MethodSource("debutIdStream")
        void shouldIncludeCorrectConfigDebutId(String debutId) {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, debutId);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            CertificateDataValueCauseOfDeath valueCauseOfDeath = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(debutId, config.getCauseOfDeath().getDebutId());
            assertEquals(debutId, valueCauseOfDeath.getDebut().getId());
        }


        @Test
        void shouldIncludeCorrectConfigSpecifications() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(allSpecifications, config.getCauseOfDeath().getSpecifications());
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeath() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            assertEquals(CertificateDataValueType.CAUSE_OF_DEATH, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueId() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeathEmpty, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var valueId = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(TERMINAL_DODSORSAK_JSON_ID, valueId.getId());
        }

        @Test
        void shouldIncludeCorrectValueDebut() {
            final var expectedDebut = LocalDate.now();
            final var causeOfDeath = Dodsorsak.create(null, new InternalDate(expectedDebut), null);
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeath, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var values = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(expectedDebut, values.getDebut().getDate());
        }

        @Test
        void shouldIncludeCorrectValueSpecification() {
            final var expectedSpecification = CertificateDataValueCode.builder()
                .id(Specifikation.KRONISK.name())
                .code(Specifikation.KRONISK.name())
                .build();
            final var causeOfDeath = Dodsorsak.create(null, null, Specifikation.KRONISK);
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeath, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var values = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(expectedSpecification, values.getSpecification());
        }

        @Test
        void shouldIncludeCorrectValueDescription() {
            final var expectedDescription = "expectedDescription";
            final var causeOfDeath = Dodsorsak.create(expectedDescription, null, null);
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(
                causeOfDeath, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            final var values = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(expectedDescription, values.getDescription().getText());
        }

        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionTerminalDodsorsakFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, DODSORSAK_DELSVAR_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        Stream<Dodsorsak> dodsOrsaker() {
            return Stream.of(
                Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.PLOTSLIG),
                Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.KRONISK),
                Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS),
                Dodsorsak.create(null, null, null)
            );
        }

        @ParameterizedTest
        @MethodSource("dodsOrsaker")
        void shouldIncludeTextValue(Dodsorsak expectedValue) {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionTerminalDodsorsakFoljdAv.toCertificate(
                    expectedValue, 0, texts, FOLJD_OM_DELSVAR_B_ID, FOLJD_OM_DELSVAR_B_LABEL, FOLJD_OM_DELSVAR_B_DATUM_ID))
                .build();

            final var actualValue = QuestionTerminalDodsorsakFoljdAv.toInternal(certificate, List.of(FOLJD_OM_DELSVAR_B_ID));

            assertEquals(expectedValue, actualValue.get(0));
        }

    }
}