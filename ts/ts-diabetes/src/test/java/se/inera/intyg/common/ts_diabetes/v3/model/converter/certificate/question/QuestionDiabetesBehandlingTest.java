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

package se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_CATEGORY_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigViewTextTest;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Behandling;

@ExtendWith(MockitoExtension.class)
class QuestionDiabetesBehandlingTest {

    @Mock
    CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        when(textProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesBehandling.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getId() {
            return ALLMANT_BEHANDLING_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return ALLMANT_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigViewTextTests extends ConfigViewTextTest {


        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesBehandling.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return ALLMANT_BEHANDLING_TEXT_ID;
        }


        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected CertificateMessagesProvider getMessageProviderMock() {
            return null;
        }

        @Override
        protected String getMessageId() {
            return null;
        }
    }

    @Nested
    class IncludeValueViewTextTests {

        @Test
        void shallIncludeKost() {
            final var element = QuestionDiabetesBehandling.toCertificate(Allmant.builder().setBehandling(
                Behandling.builder().setEndastKost(true).build()
            ).build(), 0, textProvider);
            final var value = (CertificateDataValueViewText) element.getValue();
            final var expectedValue = "Endast kost";
            assertEquals(expectedValue, value.getText());
        }

        @Test
        void shallIncludeTabletter() {
            final var element = QuestionDiabetesBehandling.toCertificate(Allmant.builder().setBehandling(
                Behandling.builder().setTabletter(true).build()
            ).build(), 0, textProvider);
            final var value = (CertificateDataValueViewText) element.getValue();
            final var expectedValue = "Tabletter";
            assertEquals(expectedValue, value.getText());
        }

        @Test
        void shallIncludeInsulin() {
            final var element = QuestionDiabetesBehandling.toCertificate(Allmant.builder().setBehandling(
                Behandling.builder().setInsulin(true).build()
            ).build(), 0, textProvider);
            final var value = (CertificateDataValueViewText) element.getValue();
            final var expectedValue = "Insulin";
            assertEquals(expectedValue, value.getText());
        }

        @Test
        void shallIncludeAnnan() {
            final var element = QuestionDiabetesBehandling.toCertificate(Allmant.builder().setBehandling(
                Behandling.builder().setAnnanBehandling(true).build()
            ).build(), 0, textProvider);
            final var value = (CertificateDataValueViewText) element.getValue();
            final var expectedValue = "Annan behandling";
            assertEquals(expectedValue, value.getText());
        }

        @Test
        void shallIncludeKostTabletterInsulinAndAnnan() {
            final var element = QuestionDiabetesBehandling.toCertificate(Allmant.builder().setBehandling(
                    Behandling.builder()
                        .setInsulin(true)
                        .setEndastKost(true)
                        .setTabletter(true)
                        .setAnnanBehandling(true)
                        .build()
                )
                .build(), 0, textProvider);
            final var value = (CertificateDataValueViewText) element.getValue();
            final var expectedValue = "Endast kost, Tabletter, Insulin, Annan behandling";
            assertEquals(expectedValue, value.getText());
        }
    }
}
