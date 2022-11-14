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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_AV_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_ID;

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
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionFoljdAvTest {
    @Mock
    private CertificateTextProvider texts;

    private List<Dodsorsak> causeOfDeathEmpty;
    private List<CertificateDataConfigCode> allSpecifications;
    private Dodsorsak causeB;
    private Dodsorsak causeC;
    private Dodsorsak causeD;

    private static final String LABEL_B = "-B";
    private static final String LABEL_C = "-C";
    private static final String LABEL_D = "-D";
    

    private static final String KRONISK = "Kronisk";
    private static final String PLOTSLIG = "Akut";
    private static final String UPPGIFT_SAKNAS = "Uppgift Saknas";

    @BeforeEach
    void setup() {
        causeOfDeathEmpty = List.of(Dodsorsak.create(null, null, null));
        allSpecifications = List.of(
            CertificateDataConfigCode.builder()
                .id(Specifikation.PLOTSLIG.name() + LABEL_B)
                .label(PLOTSLIG)
                .code(Specifikation.PLOTSLIG.name())
                .build(),
            CertificateDataConfigCode.builder()
                .id(Specifikation.KRONISK.name() + LABEL_B)
                .label(KRONISK)
                .code(Specifikation.KRONISK.name())
                .build(),
            CertificateDataConfigCode.builder()
                .id(Specifikation.UPPGIFT_SAKNAS.name() + LABEL_B)
                .label(UPPGIFT_SAKNAS)
                .code(Specifikation.UPPGIFT_SAKNAS.name())
                .build()
        );
        causeB = Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.KRONISK);
        causeC = Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS);
        causeD = Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.PLOTSLIG);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            assertEquals(FOLJD_OM_DELSVAR_ID + LABEL_B, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, expectedIndex, texts, LABEL_B);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            assertEquals(DODSORSAK_DELSVAR_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FOLJD_AV_QUESTION_TEXT_ID);
        }

        @Test
        void shouldIncludeLabelB() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(config.getLabel(), LABEL_B);
        }

        @Test
        void shouldIncludeLabelC() {
            final var question = QuestionFoljdAv.toCertificate(List.of(causeB, causeC), 0, texts, LABEL_C);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(config.getLabel(), LABEL_C);
        }

        @Test
        void shouldIncludeLabelD() {
            final var question = QuestionFoljdAv.toCertificate(List.of(causeB, causeC, causeD), 0, texts, LABEL_D);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(config.getLabel(), LABEL_D);
        }

        @Test
        void shouldIncludeTerminalCauseOfDeathConfigType() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            assertEquals(CertificateDataConfigTypes.UE_CAUSE_OF_DEATH, question.getConfig().getType());
        }

        @Test
        void shouldIncludeTerminalCauseOfDeathList() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertNotNull(config.getCauseOfDeath());
        }

        @Test
        void shouldIncludeCorrectConfigIdWithLabel() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(FOLJD_JSON_ID + LABEL_B, config.getCauseOfDeath().getId());
        }

        @Test
        void shouldIncludeCorrectConfigDescriptionIdWithLabel() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(FOLJD_OM_DELSVAR_ID + LABEL_B, config.getCauseOfDeath().getDescriptionId());
        }

        @Test
        void shouldIncludeCorrectConfigDebutId() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(FOLJD_DATUM_DELSVAR_ID + LABEL_B, config.getCauseOfDeath().getDebutId());
        }

        @Test
        void shouldIncludeCorrectConfigSpecificationsWithLabels() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var config = (CertificateDataConfigCauseOfDeath) question.getConfig();
            assertEquals(allSpecifications, config.getCauseOfDeath().getSpecifications());
        }

        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeath() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            assertEquals(CertificateDataValueType.TERMINAL_CAUSE_OF_DEATH, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueIdWithLabel() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var valueId = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(FOLJD_JSON_ID + LABEL_B, valueId.getId());
        }

        @Test
        void shouldIncludeCorrectValueDebut() {
            final var expectedDebut = LocalDate.now();
            final var causeOfDeath = List.of(Dodsorsak.create(null, new InternalDate(expectedDebut), null));
            final var question = QuestionFoljdAv.toCertificate(causeOfDeath, 0, texts, LABEL_B);
            final var values = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(expectedDebut, values.getDebut().getDate());
        }

        @Test
        void shouldIncludeCorrectValueSpecification() {
            final var expectedSpecification = CertificateDataValueCode.builder()
                .id(Specifikation.KRONISK.name() + LABEL_B)
                .code(Specifikation.KRONISK.name())
                .build();
            final var causeOfDeath = List.of(Dodsorsak.create(null, null, Specifikation.KRONISK));
            final var question = QuestionFoljdAv.toCertificate(causeOfDeath, 0, texts, LABEL_B);
            final var values = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(expectedSpecification, values.getSpecification());
        }

        @Test
        void shouldIncludeCorrectValueDescription() {
            final var expectedDescription = "expectedDescription";
            final var causeOfDeath = List.of(Dodsorsak.create(expectedDescription, null, null));
            final var question = QuestionFoljdAv.toCertificate(causeOfDeath, 0, texts, LABEL_B);
            final var values = (CertificateDataValueCauseOfDeath) question.getValue();
            assertEquals(expectedDescription, values.getDescription().getText());
        }

        @Test
        void shouldIncludeValidationTextType() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            assertEquals(CertificateDataValidationType.TEXT_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationTextId() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var certificateDataValidationMaxDate = (CertificateDataValidationText) question.getValidation()[0];
            assertEquals(FOLJD_OM_DELSVAR_ID + LABEL_B, certificateDataValidationMaxDate.getId());
        }

        @Test
        void shouldIncludeValidationTextLimit() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var certificateDataValidationMaxDate = (CertificateDataValidationText) question.getValidation()[0];
            assertEquals(80, certificateDataValidationMaxDate.getLimit());
        }

        @Test
        void shouldIncludeValidationMaxDateType() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            assertEquals(CertificateDataValidationType.MAX_DATE_VALIDATION, question.getValidation()[1].getType());
        }

        @Test
        void shouldIncludeValidationMaxDateId() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[1];
            assertEquals(FOLJD_DATUM_DELSVAR_ID + LABEL_B, certificateDataValidationMaxDate.getId());
        }

        @Test
        void shouldIncludeValidationMaxDateNumberOfDays() {
            final var question = QuestionFoljdAv.toCertificate(causeOfDeathEmpty, 0, texts, LABEL_B);
            final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[1];
            assertEquals(0, certificateDataValidationMaxDate.getNumberOfDays());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        final Dodsorsak causeB = Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.KRONISK);
        final Dodsorsak causeC = Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.UPPGIFT_SAKNAS);
        final Dodsorsak causeD = Dodsorsak.create("beskrivning", new InternalDate(LocalDate.now()), Specifikation.PLOTSLIG);
        final Dodsorsak causeOfDeathEmpty = Dodsorsak.create("",null, null);

        Stream<List<Dodsorsak>> dodsOrsaker1() {
            return Stream.of(
                List.of(causeB),
                List.of(causeB, causeC),
                List.of(causeB, causeC, causeD)
            );
        }

        @ParameterizedTest
        @MethodSource("dodsOrsaker1")
        void shouldIncludeTextValue(List<Dodsorsak> expectedValue) {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionFoljdAv.toCertificate(expectedValue, 0, texts, LABEL_B))
                .addElement(QuestionFoljdAv.toCertificate(expectedValue, 1, texts, LABEL_C))
                .addElement(QuestionFoljdAv.toCertificate(expectedValue, 2, texts, LABEL_D))
                .build();

            final var actualValue = QuestionFoljdAv.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }

        @Test
        void shouldNotIncludeEmptyDodsorsak() {
            final var expectedValue = List.of(causeB, causeD);
            final var inputValue = List.of(causeB, causeOfDeathEmpty, causeD);
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionFoljdAv.toCertificate(inputValue, 0, texts, LABEL_B))
                .addElement(QuestionFoljdAv.toCertificate(inputValue, 1, texts, LABEL_C))
                .addElement(QuestionFoljdAv.toCertificate(inputValue, 2, texts, LABEL_D))
                .build();

            final var actualValue = QuestionFoljdAv.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }

        @Test
        void shouldReturnEmptyList() {
            final var expectedValue = List.of();
            final var inputValue = List.of(causeOfDeathEmpty);
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionFoljdAv.toCertificate(inputValue, 0, texts, LABEL_B))
                .addElement(QuestionFoljdAv.toCertificate(inputValue, 1, texts, LABEL_C))
                .addElement(QuestionFoljdAv.toCertificate(inputValue, 2, texts, LABEL_D))
                .build();

            final var actualValue = QuestionFoljdAv.toInternal(certificate);

            assertEquals(expectedValue, actualValue);
        }
    }
}