/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.sos_parent.validator;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_JSON_ID;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.google.common.base.Strings;

import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;

public final class SosInternalDraftValidator {

    private static final int BARN_SOM_AVLIDIT_INOM_28_DAGAR = 28;

    private SosInternalDraftValidator() {
    }

    public static void validateIdentitetStyrkt(SosUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getIdentitetStyrkt()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "personuppgifter", IDENTITET_STYRKT_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    public static void validateDodsdatum(SosUtlatande utlatande, List<ValidationMessage> validationMessages, String prefix) {
        if (utlatande.getDodsdatumSakert() == null) {
            ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_SAKERT_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        // R1 & R2
        if (utlatande.getDodsdatum() == null) {
            if (utlatande.getDodsdatumSakert()) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.EMPTY);
            } else {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.EMPTY, "common.validation.ue-vaguedate.empty.year");

                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.EMPTY, "common.validation.ue-vaguedate.empty.month");
            }
        } else if (utlatande.getDodsdatumSakert()) {

            if (ValidatorUtil.validateDate(utlatande.getDodsdatum(), validationMessages, "dodsdatumOchdodsPlats",
                DODSDATUM_JSON_ID, null)) {
                if (!utlatande.getDodsdatum().isBeforeNumDays(-1)) {
                    ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date.today.or.earlier");
                } else if (utlatande.getDodsdatum().isValidDate() && utlatande.getDodsdatum().isBeforeBeginningOfLastYear()) {
                    ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date.beforeLastYear");
                }
            }

        } else {
            if (!utlatande.getDodsdatum().isYearCorrectFormat()) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID + ".year",
                    ValidationMessageType.EMPTY, "common.validation.date.year.not_selected");
            } else if (!utlatande.getDodsdatum().isMonthCorrectFormat()) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID + ".month",
                    ValidationMessageType.EMPTY, "common.validation.date.month.not_selected");
            } else if (!utlatande.getDodsdatum().isCorrectFormat()) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.INVALID_FORMAT);
            } else if (utlatande.getDodsdatum().vagueDateInFuture()) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.OTHER, "common.validation.date.today.or.earlier");
            } else if (utlatande.getDodsdatum().isValidDate() && utlatande.getDodsdatum().isBeforeBeginningOfLastYear()) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.OTHER, "common.validation.date.beforeLastYear");
            }
        }

        // R3
        if (!utlatande.getDodsdatumSakert()) {

            if (ValidatorUtil.validateDate(utlatande.getAntraffatDodDatum(), validationMessages, "dodsdatumOchdodsPlats",
                ANTRAFFAT_DOD_DATUM_JSON_ID, null)) {
                if (!utlatande.getAntraffatDodDatum().isBeforeNumDays(-1)) {
                    ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date.today.or.earlier");
                } else if (!utlatande.getAntraffatDodDatum().isReasonable()) {
                    ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date_out_of_range_no_future");
                } else if (utlatande.getDodsdatum() != null && utlatande.getDodsdatum()
                    .vagueDateAfterDate(utlatande.getAntraffatDodDatum().asLocalDate())) {
                    ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                        ValidationMessageType.INCORRECT_COMBINATION, prefix + ".validation.datum.innanDodsdatum");
                } else if (utlatande.getAntraffatDodDatum().isBeforeBeginningOfLastYear()) {
                    ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date.beforeLastYear");
                }
            }

        } else {
            if (utlatande.getAntraffatDodDatum() != null) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION, prefix + ".validation.antraffatDod.dodsdatumSakert");
            }
        }
    }

    public static void validateDodsplats(SosUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getDodsplatsKommun()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSPLATS_KOMMUN_JSON_ID,
                ValidationMessageType.EMPTY);
        }

        // R4
        if (utlatande.getDodsplatsBoende() == null) {
            ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats", DODSPLATS_BOENDE_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    public static void validateBarn(SosUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getBarn() == null) {
            ValidatorUtil.addValidationError(validationMessages, "barnSomAvlidit", BARN_JSON_ID, ValidationMessageType.EMPTY);
        } else if (utlatande.getDodsdatumSakert() != null && utlatande.getDodsdatumSakert()
            && utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate()) {
            // R20
            LocalDate patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(
                utlatande.getGrundData().getPatient().getPersonId());

            if (utlatande.getBarn() && utlatande.getDodsdatum().asLocalDate().isAfter(
                patientBirthDate.plus(BARN_SOM_AVLIDIT_INOM_28_DAGAR, ChronoUnit.DAYS))) {
                ValidatorUtil.addValidationError(validationMessages, "barnSomAvlidit", BARN_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION);
            } else if (!utlatande.getBarn() && utlatande.getDodsdatum().asLocalDate().isBefore(
                patientBirthDate.plus(BARN_SOM_AVLIDIT_INOM_28_DAGAR + 1, ChronoUnit.DAYS))) {
                ValidatorUtil.addValidationError(validationMessages, "barnSomAvlidit", BARN_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION);
            }
        }
    }

}
