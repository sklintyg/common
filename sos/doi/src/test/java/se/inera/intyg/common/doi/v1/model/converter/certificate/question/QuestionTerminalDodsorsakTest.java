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
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeathSpecification.KRONISK;
import static se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeathSpecification.PLOTSLIG;
import static se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeathSpecification.UPPGIFT_SAKNAS;

import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTerminalCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueTerminalCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionTerminalDodsorsakTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {
        @Test
        void shouldIncludeId() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertEquals(DODSORSAK_DELSVAR_ID, question.getId());
        }
        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }
        @Test
        void shouldIncludeParentId() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertEquals(DODSORSAK_SVAR_ID, question.getParent());
        }
        @Test
        void shouldIncludeText() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(TERMINAL_DODSORSAK_QUESTION_TEXT_ID);
        }
        @Test
        void shouldIncludeDescription() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertTrue(question.getConfig().getDescription().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(TERMINAL_DODSORSAK_DESCRIPTION_TEXT_ID);
        }
        @Test
        void shouldIncludeTerminalCauseOfDeathConfigType() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertEquals(CertificateDataConfigTypes.UE_CAUSE_OF_DEATH, question.getConfig().getType());
        }
        @Test
        void shouldIncludeTerminalCauseOfDeathList() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            final var config = (CertificateDataConfigTerminalCauseOfDeath) question.getConfig();
            assertNotNull(config.getTerminalCauseOfDeath());
        }
        @Test
        void shouldIncludeCorrectConfigValuesInTerminalCauseOfDeathList() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            final var config = (CertificateDataConfigTerminalCauseOfDeath) question.getConfig();
            final var causeOfDeathElement = config.getTerminalCauseOfDeath();
            assertAll(
                () -> assertEquals(causeOfDeathElement.getId(), "A"),
                () -> assertEquals(causeOfDeathElement.getLabel(), "A"),
                () -> assertEquals(causeOfDeathElement.getSpecifications(), Map.of(
                    KRONISK, KRONISK.getDescription(),
                    PLOTSLIG, PLOTSLIG.getDescription(),
                    UPPGIFT_SAKNAS, UPPGIFT_SAKNAS.getDescription())),
                () -> assertEquals(causeOfDeathElement.getText(), "Beskrivning")
            );
        }
        @Test
        void shouldIncludeValueTypeTerminalCauseOfDeath() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertEquals(CertificateDataValueType.TERMINAL_CAUSE_OF_DEATH, question.getValue().getType());
        }
        @Test
        void shouldIncludeCorrectValues() {
            final var expectedDate = LocalDate.now();
            final var expectedDescription = "description";
            final var expectedSpecification = Specifikation.KRONISK;
            final var question = QuestionTerminalDodsorsak.toCertificate(expectedDescription, expectedDate, expectedSpecification, 0, texts);
            final var values = (CertificateDataValueTerminalCauseOfDeath) question.getValue();
            assertAll(
                () -> assertEquals(expectedDate, values.getDate()),
                () -> assertEquals(expectedDescription, values.getDescription()),
                () -> assertEquals(KRONISK, values.getSpecification())
            );
        }
        @Test
        void shouldIncludeValidationMandatoryType() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeValidationTextType() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertEquals(CertificateDataValidationType.TEXT_VALIDATION, question.getValidation()[1].getType());
        }

        @Test
        void shouldIncludeValidationTextId() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            final var certificateDataValidationMaxDate = (CertificateDataValidationText) question.getValidation()[1];
            assertEquals(TERMINAL_DODSORSAK_QUESTION_TEXT_ID, certificateDataValidationMaxDate.getId());
        }
        @Test
        void shouldIncludeValidationTextLimit() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            final var certificateDataValidationMaxDate = (CertificateDataValidationText) question.getValidation()[1];
            assertEquals(120, certificateDataValidationMaxDate.getLimit());
        }
        @Test
        void shouldIncludeValidationMaxDateType() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            assertEquals(CertificateDataValidationType.MAX_DATE_VALIDATION, question.getValidation()[2].getType());
        }

        @Test
        void shouldIncludeValidationMaxDateId() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[2];
            assertEquals(DODSORSAK_DATUM_JSON_ID, certificateDataValidationMaxDate.getId());
        }

        @Test
        void shouldIncludeValidationMaxDateNumberOfDays() {
            final var question = QuestionTerminalDodsorsak.toCertificate(null, null, null, 0, texts);
            final var certificateDataValidationMaxDate = (CertificateDataValidationMaxDate) question.getValidation()[2];
            assertEquals(0, certificateDataValidationMaxDate.getNumberOfDays());
        }
    }
}
