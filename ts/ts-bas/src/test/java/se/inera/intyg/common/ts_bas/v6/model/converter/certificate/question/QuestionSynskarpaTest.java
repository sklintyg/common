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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.KONTAKTLINSER_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.KONTAKTLINSER_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MED_KORREKTION_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MED_KORREKTION_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNFUNKTIONER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNKARPA_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNSKARPA_TYP_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UTAN_KORREKTION_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UTAN_KORREKTION_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.VARDEN_FOR_SYNSKARPA_ID;

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
import se.inera.intyg.common.ts_bas.v6.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v6.model.internal.Synskarpevarden;

@ExtendWith(MockitoExtension.class)
class QuestionSynskarpaTest {

    private final Syn syn = Syn.builder()
        .setHogerOga(
            Synskarpevarden.builder().build()
        )
        .setVansterOga(
            Synskarpevarden.builder().build()

        )
        .setBinokulart(
            Synskarpevarden.builder().build()
        )
        .build();
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
            return QuestionSynskarpa.toCertificate(syn, 0, textProvider);
        }

        @Override
        protected String getId() {
            return VARDEN_FOR_SYNSKARPA_ID;
        }

        @Override
        protected String getParent() {
            return SYNFUNKTIONER_CATEGORY_ID;
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
            return QuestionSynskarpa.toCertificate(syn, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return SYNKARPA_TEXT_ID;
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
                    .text(textProvider.get(UTAN_KORREKTION_TEXT_ID))
                    .build(),
                ViewColumn.builder()
                    .id(MED_KORREKTION_ID)
                    .text(textProvider.get(MED_KORREKTION_TEXT_ID))
                    .build(),
                ViewColumn.builder()
                    .id(KONTAKTLINSER_ID)
                    .text(textProvider.get(KONTAKTLINSER_TEXT_ID))
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
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(2.0)
                            .build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder().build()

                    )
                    .setBinokulart(
                        Synskarpevarden.builder().build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(0).getColumns().get(1).getText();

                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeWithCorrection() {
                final var expectedValue = "2.0";
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder()
                            .setMedKorrektion(2.0)
                            .build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder().build()

                    )
                    .setBinokulart(
                        Synskarpevarden.builder().build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(0).getColumns().get(2).getText();

                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeContactLenses() {
                final var expectedValue = "Ja";
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder()
                            .setKontaktlins(true)
                            .build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder().build()

                    )
                    .setBinokulart(
                        Synskarpevarden.builder().build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(0).getColumns().get(3).getText();

                assertEquals(expectedValue, actualValue);
            }
        }

        @Nested
        class LeftEye {

            @Test
            void shouldIncludeWithoutCorrection() {
                final var expectedValue = "2.0";
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder().build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(2.0)
                            .build()

                    )
                    .setBinokulart(
                        Synskarpevarden.builder().build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(1).getColumns().get(1).getText();

                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeWithCorrection() {
                final var expectedValue = "2.0";
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder().build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder()
                            .setMedKorrektion(2.0)
                            .build()
                    )
                    .setBinokulart(
                        Synskarpevarden.builder().build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(1).getColumns().get(2).getText();

                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeContactLenses() {
                final var expectedValue = "Ja";
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder().build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder()
                            .setKontaktlins(true)
                            .build()
                    )
                    .setBinokulart(
                        Synskarpevarden.builder().build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(1).getColumns().get(3).getText();

                assertEquals(expectedValue, actualValue);
            }
        }

        @Nested
        class Binoculuar {

            @Test
            void shouldIncludeWithoutCorrection() {
                final var expectedValue = "2.0";
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder().build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder().build()
                    )
                    .setBinokulart(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(2.0)
                            .build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(2).getColumns().get(1).getText();

                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeWithCorrection() {
                final var expectedValue = "2.0";
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder().build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder().build()
                    )
                    .setBinokulart(
                        Synskarpevarden.builder()
                            .setMedKorrektion(2.0)
                            .build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(2).getColumns().get(2).getText();

                assertEquals(expectedValue, actualValue);
            }

            @Test
            void shouldIncludeContactLenses() {
                final var expectedValue = "-";
                final var syn = Syn.builder()
                    .setHogerOga(
                        Synskarpevarden.builder().build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder().build()
                    )
                    .setBinokulart(
                        Synskarpevarden.builder()
                            .setKontaktlins(true)
                            .build()
                    )
                    .build();

                final var element = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                final var value = (CertificateDataValueViewTable) element.getValue();
                final var actualValue = value.getRows().get(2).getColumns().get(3).getText();

                assertEquals(expectedValue, actualValue);
            }
        }
    }
}