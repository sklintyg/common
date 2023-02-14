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
package se.inera.intyg.common.fkparent.model.converter.certificate;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateListValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.model.InternalDate;

public abstract class AbstractQuestionUtlatandeBaseratPa {

    protected static CertificateDataElement toCertificate(InternalDate undersokningPatient, InternalDate journaluppgifter,
        InternalDate beskrivningPatient, InternalDate annat, int index, CertificateTextProvider textProvider, String questionId,
        String parentId, String textId) {
        return CertificateDataElement.builder()
            .id(questionId)
            .parent(parentId)
            .index(index)
            .config(CertificateDataConfigCheckboxMultipleDate.builder()
                .text(textProvider.get(textId))
                .list(
                    List.of(
                        CheckboxMultipleDate.builder()
                            .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                            .maxDate(LocalDate.now())
                            .label(textProvider.get(GRUNDFORMU_UNDERSOKNING_LABEL_ID))
                            .build(),
                        CheckboxMultipleDate.builder()
                            .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                            .maxDate(LocalDate.now())
                            .label(textProvider.get(GRUNDFORMU_JOURNALUPPGIFTER_LABEL_ID))
                            .build(),
                        CheckboxMultipleDate.builder()
                            .id(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1)
                            .maxDate(LocalDate.now())
                            .label(textProvider.get(GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL_ID))
                            .build(),
                        CheckboxMultipleDate.builder()
                            .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                            .maxDate(LocalDate.now())
                            .label(textProvider.get(GRUNDFORMU_ANNAT_LABEL_ID))
                            .build()
                    )
                )
                .build()
            )
            .value(
                CertificateDataValueDateList.builder()
                    .list(getValues(undersokningPatient, journaluppgifter, beskrivningPatient, annat))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(multipleOrExpression(
                            GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
                            GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1,
                            GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1,
                            GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1))
                        .build()
                }
            )
            .build();
    }

    private static boolean validDate(InternalDate date) {
        return date != null && date.isValidDate();
    }

    private static List<CertificateDataValueDate> getValues(InternalDate undersokningPatient, InternalDate journaluppgifter,
        InternalDate beskrivningPatient, InternalDate annat) {
        final List<CertificateDataValueDate> values = new ArrayList<>();

        if (validDate(undersokningPatient)) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                    .date(undersokningPatient.asLocalDate())
                    .build()
            );
        }

        if (validDate(journaluppgifter)) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                    .date(journaluppgifter.asLocalDate())
                    .build()
            );
        }

        if (validDate(beskrivningPatient)) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1)
                    .date(beskrivningPatient.asLocalDate())
                    .build()
            );
        }

        if (validDate(annat)) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                    .date(annat.asLocalDate())
                    .build()
            );
        }
        return values;
    }

    public static InternalDate toInternal(Certificate certificate, String questionId, String itemId) {
        final var localDate = dateListValue(certificate.getData(), questionId, itemId);
        if (localDate == null) {
            return null;
        }
        return new InternalDate(localDate);
    }
}
