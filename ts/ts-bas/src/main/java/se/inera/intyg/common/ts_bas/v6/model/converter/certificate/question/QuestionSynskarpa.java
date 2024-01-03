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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.booleanValue;
import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.doubleValue;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.BINOKULART_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HOGER_OGA_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.KONTAKTLINSER_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.KONTAKTLINSER_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MED_KORREKTION_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MED_KORREKTION_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNFUNKTIONER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNKARPA_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNSKARPA_TYP_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UTAN_KORREKTION_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UTAN_KORREKTION_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.VANSTER_OGA_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.VARDEN_FOR_SYNSKARPA_ID;

import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewTable;
import se.inera.intyg.common.support.facade.model.config.ViewColumn;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewRow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewTable;
import se.inera.intyg.common.ts_bas.v6.model.internal.Syn;

public class QuestionSynskarpa {

    private static final String NOT_SPECIFIED = "Ej Angivet";

    public static CertificateDataElement toCertificate(Syn syn, int index, CertificateTextProvider textProvider) {
        final var binokulart = syn != null && syn.getBinokulart() != null ? syn.getBinokulart() : null;
        final var hogerOga = syn != null && syn.getHogerOga() != null ? syn.getHogerOga() : null;
        final var vansterOga = syn != null && syn.getVansterOga() != null ? syn.getVansterOga() : null;
        return CertificateDataElement.builder()
            .id(VARDEN_FOR_SYNSKARPA_ID)
            .parent(SYNFUNKTIONER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewTable.builder()
                    .text(textProvider.get(SYNKARPA_TEXT_ID))
                    .columns(
                        List.of(
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
                                            .text(textProvider.get(HOGER_OGA_LABEL_ID))
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(UTAN_KORREKTION_ID)
                                            .text(hogerOga != null ? doubleValue(hogerOga.getUtanKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(MED_KORREKTION_ID)
                                            .text(hogerOga != null ? doubleValue(hogerOga.getMedKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(KONTAKTLINSER_ID)
                                            .text(hogerOga != null ? booleanValue(hogerOga.getKontaktlins()) : NOT_SPECIFIED)
                                            .build()
                                    ))
                                .build(),
                            CertificateDataValueViewRow.builder()
                                .columns(
                                    List.of(
                                        CertificateDataTextValue.builder()
                                            .id(SYNSKARPA_TYP_ID)
                                            .text(textProvider.get(VANSTER_OGA_LABEL_ID))
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(UTAN_KORREKTION_ID)
                                            .text(vansterOga != null ? doubleValue(vansterOga.getUtanKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(MED_KORREKTION_ID)
                                            .text(vansterOga != null ? doubleValue(vansterOga.getMedKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(KONTAKTLINSER_ID)
                                            .text(vansterOga != null ? booleanValue(vansterOga.getKontaktlins()) : NOT_SPECIFIED)
                                            .build()
                                    ))
                                .build(),
                            CertificateDataValueViewRow.builder()
                                .columns(
                                    List.of(
                                        CertificateDataTextValue.builder()
                                            .id(SYNSKARPA_TYP_ID)
                                            .text(textProvider.get(BINOKULART_LABEL_ID))
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(UTAN_KORREKTION_ID)
                                            .text(binokulart != null ? doubleValue(binokulart.getUtanKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(MED_KORREKTION_ID)
                                            .text(binokulart != null ? doubleValue(binokulart.getMedKorrektion()) : NOT_SPECIFIED)
                                            .build(),
                                        CertificateDataTextValue.builder()
                                            .id(KONTAKTLINSER_ID)
                                            .text("-")
                                            .build()
                                    ))
                                .build()
                        )
                    )
                    .build()
            )
            .build();
    }
}
