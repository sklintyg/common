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

package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateListValue;

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionUnderlagBaseratPa {

    private static final short NUMBER_OF_DAYS_IN_FUTURE = 0;

    public static CertificateDataElement toCertificate(InternalDate undersokningPatient, InternalDate journaluppgifter,
        InternalDate beskrivningPatient, InternalDate annat, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMU_CATEGORY_ID)
            .config(CertificateDataConfigCheckboxMultipleDate.builder()
                .text(textProvider.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT))
                .list(
                    List.of(
                        CheckboxMultipleDate.builder()
                            .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                            .label(textProvider.get(GRUNDFORMU_UNDERSOKNING_LABEL))
                            .build(),
                        CheckboxMultipleDate.builder()
                            .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                            .label(textProvider.get(GRUNDFORMU_JOURNALUPPGIFTER_LABEL))
                            .build(),
                        CheckboxMultipleDate.builder()
                            .id(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1)
                            .label(textProvider.get(GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL))
                            .build(),
                        CheckboxMultipleDate.builder()
                            .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                            .label(textProvider.get(GRUNDFORMU_ANNAT_LABEL))
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
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                        .numberOfDays(NUMBER_OF_DAYS_IN_FUTURE)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                        .numberOfDays(NUMBER_OF_DAYS_IN_FUTURE)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1)
                        .numberOfDays(NUMBER_OF_DAYS_IN_FUTURE)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                        .numberOfDays(NUMBER_OF_DAYS_IN_FUTURE)
                        .build(),
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

    public static InternalDate toInternal(Certificate certificate, String itemId) {
        final var localDate = dateListValue(certificate.getData(), GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, itemId);
        if (localDate == null) {
            return null;
        }
        return new InternalDate(localDate);
    }
}
