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

package se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.MED_KORREKTION_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.MED_KORREKTION_TEXT;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNSKARPA_TYP_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_VARDEN_SYNSKARPA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.UTAN_KORREKTION_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.UTAN_KORREKTION_TEXT;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.ViewColumn;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewTable;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigViewTableTest;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synfunktion;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synskarpevarden;

@ExtendWith(MockitoExtension.class)
class QuestionSynSynskarpaTest {

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
            return QuestionSynSynskarpa.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getId() {
            return SYN_VARDEN_SYNSKARPA_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return SYN_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigViewTableTests extends ConfigViewTableTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSynSynskarpa.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return null;
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

        @Override
        protected List<ViewColumn> getColumns() {
            return List.of(
                ViewColumn.builder()
                    .id(SYNSKARPA_TYP_ID)
                    .build(),
                ViewColumn.builder()
                    .id(UTAN_KORREKTION_ID)
                    .text(UTAN_KORREKTION_TEXT)
                    .build(),
                ViewColumn.builder()
                    .id(MED_KORREKTION_ID)
                    .text(MED_KORREKTION_TEXT)
                    .build()
            );
        }
    }

    @Nested
    class IncludeValueViewTableTests {

        @Nested
        class RightEye {

            @Test
            void shouldIncludeWithoutCorrection() {
                final var expectedValue = "2.0";
                final var element = QuestionSynSynskarpa.toCertificate(Synfunktion.builder().setHoger(
                    Synskarpevarden.builder().setUtanKorrektion(2.0).build()
                ).build(), 0, textProvider);

                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(0).getColumns().get(1).getText();
                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeWithCorrection() {
                final var expectedValue = "2.0";
                final var element = QuestionSynSynskarpa.toCertificate(Synfunktion.builder().setHoger(
                    Synskarpevarden.builder().setMedKorrektion(2.0).build()
                ).build(), 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(0).getColumns().get(2).getText();
                assertEquals(expectedValue, actualValue);
            }
        }

        @Nested
        class LeftEye {

            @Test
            void shouldIncludeWithoutCorrection() {
                final var expectedValue = "2.0";
                final var element = QuestionSynSynskarpa.toCertificate(Synfunktion.builder().setVanster(
                    Synskarpevarden.builder().setUtanKorrektion(2.0).build()
                ).build(), 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(1).getColumns().get(1).getText();
                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeWithCorrection() {
                final var expectedValue = "2.0";
                final var element = QuestionSynSynskarpa.toCertificate(Synfunktion.builder().setVanster(
                    Synskarpevarden.builder().setMedKorrektion(2.0).build()
                ).build(), 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(1).getColumns().get(2).getText();
                assertEquals(expectedValue, actualValue);
            }
        }

        @Nested
        class Binoculuar {

            @Test
            void shouldIncludeWithoutCorrection() {
                final var expectedValue = "2.0";
                final var element = QuestionSynSynskarpa.toCertificate(Synfunktion.builder().setBinokulart(
                    Synskarpevarden.builder().setUtanKorrektion(2.0).build()
                ).build(), 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(2).getColumns().get(1).getText();
                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeWithCorrection() {
                final var expectedValue = "2.0";
                final var element = QuestionSynSynskarpa.toCertificate(Synfunktion.builder().setBinokulart(
                    Synskarpevarden.builder().setMedKorrektion(2.0).build()
                ).build(), 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(2).getColumns().get(2).getText();
                assertEquals(expectedValue, actualValue);
            }
        }

        @Nested
        class CommonSynskarpa {

            @Test
            void shouldHandleEmptyValues() {
                final var expectedValue = "-";
                final var element = QuestionSynSynskarpa.toCertificate(null, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                assertAll(
                    () -> assertEquals(expectedValue, value.getRows().get(0).getColumns().get(1).getText()),
                    () -> assertEquals(expectedValue, value.getRows().get(0).getColumns().get(2).getText()),
                    () -> assertEquals(expectedValue, value.getRows().get(1).getColumns().get(1).getText()),
                    () -> assertEquals(expectedValue, value.getRows().get(1).getColumns().get(2).getText()),
                    () -> assertEquals(expectedValue, value.getRows().get(2).getColumns().get(1).getText()),
                    () -> assertEquals(expectedValue, value.getRows().get(2).getColumns().get(2).getText())
                );
            }
        }
    }
}
