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

package se.inera.intyg.common.fk7263.model.converter.certificate.question;

import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_FOM_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_FOM_TEXT;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_100_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_25_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_50_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_75_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_NEDSATT_TEXT;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_TOM_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_BEDOMNING_TOM_TEXT;

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewTable;
import se.inera.intyg.common.support.facade.model.config.ViewColumn;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewRow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewTable;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;

public class QuestionArbetsformogaBedomning {

    private static final String NOT_PROVIDED = "Ej Angivet";

    public static CertificateDataElement toCertificate(InternalLocalDateInterval nedsattMed25, InternalLocalDateInterval nedsattMed50,
        InternalLocalDateInterval nedsattMed75, InternalLocalDateInterval nedsattMed100, int index,
        CertificateMessagesProvider messagesProvider) {
        return CertificateDataElement.builder()
            .id(ARBETSFORMAGA_BEDOMNING_SVAR_ID)
            .parent(ARBETSFORMAGA_BEDOMNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewTable.builder()
                    .columns(
                        List.of(
                            ViewColumn.builder()
                                .id(ARBETSFORMAGA_BEDOMNING_NEDSATT_ID)
                                .text(ARBETSFORMAGA_BEDOMNING_NEDSATT_TEXT)
                                .build(),
                            ViewColumn.builder()
                                .id(ARBETSFORMAGA_BEDOMNING_FOM_ID)
                                .text(ARBETSFORMAGA_BEDOMNING_FOM_TEXT)
                                .build(),
                            ViewColumn.builder()
                                .id(ARBETSFORMAGA_BEDOMNING_TOM_ID)
                                .text(ARBETSFORMAGA_BEDOMNING_TOM_TEXT)
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueViewTable.builder()
                    .rows(
                        getRows(nedsattMed25, nedsattMed50, nedsattMed75, nedsattMed100, messagesProvider)
                    )
                    .build()
            )
            .build();
    }

    private static List<CertificateDataValueViewRow> getRows(InternalLocalDateInterval nedsattMed25, InternalLocalDateInterval nedsattMed50,
        InternalLocalDateInterval nedsattMed75, InternalLocalDateInterval nedsattMed100, CertificateMessagesProvider messagesProvider) {
        final var rows = new ArrayList<CertificateDataValueViewRow>();
        if (nedsattMed25 != null) {
            rows.add(
                CertificateDataValueViewRow.builder()
                    .columns(getColumnNedsatt(nedsattMed25, ARBETSFORMAGA_BEDOMNING_NEDSATT_25_TEXT_ID, messagesProvider))
                    .build());
        }

        if (nedsattMed50 != null) {
            rows.add(
                CertificateDataValueViewRow.builder()
                    .columns(getColumnNedsatt(nedsattMed50, ARBETSFORMAGA_BEDOMNING_NEDSATT_50_TEXT_ID, messagesProvider))
                    .build());
        }
        if (nedsattMed75 != null) {
            rows.add(
                CertificateDataValueViewRow.builder()
                    .columns(getColumnNedsatt(nedsattMed75, ARBETSFORMAGA_BEDOMNING_NEDSATT_75_TEXT_ID, messagesProvider))
                    .build());
        }
        if (nedsattMed100 != null) {
            rows.add(
                CertificateDataValueViewRow.builder()
                    .columns(getColumnNedsatt(nedsattMed100, ARBETSFORMAGA_BEDOMNING_NEDSATT_100_TEXT_ID, messagesProvider))
                    .build());
        }

        return rows;
    }

    private static List<CertificateDataValueText> getColumnNedsatt(InternalLocalDateInterval nedsattMed, String textId,
        CertificateMessagesProvider messagesProvider) {
        return List.of(
            CertificateDataValueText.builder()
                .id(ARBETSFORMAGA_BEDOMNING_NEDSATT_ID)
                .text(messagesProvider.get(textId))
                .build(),
            CertificateDataValueText.builder()
                .id(ARBETSFORMAGA_BEDOMNING_FOM_ID)
                .text(nedsattMed.fromAsLocalDate().toString())
                .build(),
            CertificateDataValueText.builder()
                .id(ARBETSFORMAGA_BEDOMNING_TOM_ID)
                .text(nedsattMed.tomAsLocalDate().toString())
                .build());
    }
}
