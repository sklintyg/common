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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_TEXT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;

@ExtendWith(MockitoExtension.class)
class QuestionMedicinskBehandlingPagaendeBehandlingHeaderTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionMedicinskBehandlingPagaendeBehandlingHeader.toCertificate(
                0, texts);
            assertEquals(PAGAENDEBEHANDLING_SVAR_ID_19, question.getId());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionMedicinskBehandlingPagaendeBehandlingHeader.toCertificate(
                expectedIndex, texts);
            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeParentId() {
            final var question = QuestionMedicinskBehandlingPagaendeBehandlingHeader.toCertificate(
                0, texts);
            assertEquals(MEDICINSKABEHANDLINGAR_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeText() {
            final var question = QuestionMedicinskBehandlingPagaendeBehandlingHeader.toCertificate(
                0, texts);
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(PAGAENDEBEHANDLING_SVAR_TEXT);
        }

        @Test
        void shouldIncludeHeaderConfigType() {
            final var question = QuestionMedicinskBehandlingPagaendeBehandlingHeader.toCertificate(
                0, texts);
            assertEquals(CertificateDataConfigType.UE_HEADER, question.getConfig().getType());
        }
    }
}