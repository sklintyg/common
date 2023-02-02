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

package se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.doubleValue;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BINOKULART_LABEL;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HOGER_OGA_LABEL;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.MED_KORREKTION_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.MED_KORREKTION_TEXT;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNSKARPA_TYP_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_VARDEN_SYNSKARPA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.UTAN_KORREKTION_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.UTAN_KORREKTION_TEXT;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.VANSTER_OGA_LABEL;

import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewTable;
import se.inera.intyg.common.support.facade.model.config.ViewColumn;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewRow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewTable;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synfunktion;

public class QuestionSynSynskarpa {

    private static final String NOT_SPECIFIED = "-";

    public static CertificateDataElement toCertificate(Synfunktion synfunktion, int index, CertificateTextProvider textProvider) {
        final var hogerOga = synfunktion != null && synfunktion.getHoger() != null ? synfunktion.getHoger() : null;
        final var vansterOga = synfunktion != null && synfunktion.getVanster() != null ? synfunktion.getVanster() : null;
        final var binokulart = synfunktion != null && synfunktion.getBinokulart() != null ? synfunktion.getBinokulart() : null;
        return CertificateDataElement.builder()
            .id(SYN_VARDEN_SYNSKARPA_SVAR_ID)
            .parent(SYN_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewTable.builder()
                    .columns(
                        List.of(
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
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueViewTable.builder()
                    .rows(
                        List.of(
                            CertificateDataValueViewRow.builder()
                                .columns(
                                    List.of(
                                        CertificateDataTextValue.builder()
                                            .id(SYNSKARPA_TYP_ID)
                                            .text(textProvider.get(HOGER_OGA_LABEL))
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(UTAN_KORREKTION_ID)
                                            .text(hogerOga != null ? doubleValue(hogerOga.getUtanKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(MED_KORREKTION_ID)
                                            .text(hogerOga != null ? doubleValue(hogerOga.getMedKorrektion()) : NOT_SPECIFIED)
                                            .build()
                                    )
                                ).build(),
                            CertificateDataValueViewRow.builder()
                                .columns(
                                    List.of(
                                        CertificateDataTextValue.builder()
                                            .id(SYNSKARPA_TYP_ID)
                                            .text(textProvider.get(VANSTER_OGA_LABEL))
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(UTAN_KORREKTION_ID)
                                            .text(vansterOga != null ? doubleValue(vansterOga.getUtanKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(MED_KORREKTION_ID)
                                            .text(vansterOga != null ? doubleValue(vansterOga.getMedKorrektion()) : NOT_SPECIFIED)
                                            .build()
                                    )
                                ).build(),
                            CertificateDataValueViewRow.builder()
                                .columns(
                                    List.of(
                                        CertificateDataTextValue.builder()
                                            .id(SYNSKARPA_TYP_ID)
                                            .text(textProvider.get(BINOKULART_LABEL))
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(UTAN_KORREKTION_ID)
                                            .text(binokulart != null ? doubleValue(binokulart.getUtanKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(MED_KORREKTION_ID)
                                            .text(binokulart != null ? doubleValue(binokulart.getMedKorrektion()) : NOT_SPECIFIED)
                                            .build()
                                    )
                                ).build()
                        )
                    )
                    .build()
            )
            .build();
    }
}
