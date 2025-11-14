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
package se.inera.intyg.common.sos_parent.validator;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_OSAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_JSON_ID;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadEnum;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.validate.ValidatorUtil;

public final class SosInternalDraftValidator {

    private static final int BARN_SOM_AVLIDIT_INOM_28_DAGAR = 28;

    private SosInternalDraftValidator() {
    }

    public static void validateIdentitetStyrkt(SosUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getIdentitetStyrkt()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "personuppgifter", IDENTITET_STYRKT_JSON_ID,
                ValidationMessageType.EMPTY, IDENTITET_STYRKT_DELSVAR_ID);
        }
    }

    public static void validateDodsdatum(SosUtlatande utlatande, List<ValidationMessage> validationMessages, String prefix) {
        if (utlatande.getDodsdatumSakert() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_SAKERT_JSON_ID,
                ValidationMessageType.EMPTY, DODSDATUM_SAKERT_DELSVAR_ID);
            return;
        }
        final var patientBirthDateFromPersonId = ValidatorUtil.getBirthDateFromPersonnummer(
            utlatande.getGrundData().getPatient().getPersonId());
        // R1 & R2
        if (utlatande.getDodsdatum() == null) {
            if (utlatande.getDodsdatumSakert()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.EMPTY, DODSDATUM_DELSVAR_ID);
            } else {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.EMPTY, "common.validation.ue-vaguedate.empty.year", DODSDATUM_OSAKERT_DELSVAR_ID);

                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.EMPTY, "common.validation.ue-vaguedate.empty.month", DODSDATUM_OSAKERT_DELSVAR_ID);
            }
        } else if (utlatande.getDodsdatumSakert()) {

            if (ValidatorUtil.validateDate(utlatande.getDodsdatum(), validationMessages, "dodsdatumOchdodsPlats",
                DODSDATUM_JSON_ID, null, DODSDATUM_DELSVAR_ID)) {
                if (!utlatande.getDodsdatum().isBeforeNumDays(-1)) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date.today.or.earlier", DODSDATUM_DELSVAR_ID);
                } else if (utlatande.getDodsdatum().isValidDate() && utlatande.getDodsdatum()
                    .isBefore(patientBirthDateFromPersonId)) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date.beforePatientBirthDate", DODSDATUM_DELSVAR_ID);
                }
            }

        } else {
            if (!utlatande.getDodsdatum().isYearCorrectFormat()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID + ".year",
                    ValidationMessageType.EMPTY, "common.validation.date.year.not_selected", DODSDATUM_OSAKERT_DELSVAR_ID);
            } else if (!utlatande.getDodsdatum().isMonthCorrectFormat()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID + ".month",
                    ValidationMessageType.EMPTY, "common.validation.date.month.not_selected", DODSDATUM_OSAKERT_DELSVAR_ID);
            } else if (!utlatande.getDodsdatum().isCorrectFormat()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.INVALID_FORMAT, DODSDATUM_OSAKERT_DELSVAR_ID);
            } else if (utlatande.getDodsdatum().vagueDateInFuture()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.OTHER, "common.validation.date.today.or.earlier", DODSDATUM_OSAKERT_DELSVAR_ID);
            } else if (utlatande.getDodsdatum().isValidDate() && utlatande.getDodsdatum()
                .isBefore(patientBirthDateFromPersonId)) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSDATUM_JSON_ID,
                    ValidationMessageType.OTHER, "common.validation.date.beforePatientBirthDate", DODSDATUM_OSAKERT_DELSVAR_ID);
            }
        }
        final var patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId());
        // R3
        if (!utlatande.getDodsdatumSakert()) {

            if (ValidatorUtil.validateDate(utlatande.getAntraffatDodDatum(), validationMessages, "dodsdatumOchdodsPlats",
                ANTRAFFAT_DOD_DATUM_JSON_ID, null, ANTRAFFAT_DOD_DATUM_DELSVAR_ID)) {
                if (!utlatande.getAntraffatDodDatum().isBeforeNumDays(-1)) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date.today.or.earlier", ANTRAFFAT_DOD_DATUM_DELSVAR_ID);
                } else if (!utlatande.getAntraffatDodDatum().isReasonable()) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date_out_of_range_no_future", ANTRAFFAT_DOD_DATUM_DELSVAR_ID);
                } else if (utlatande.getDodsdatum() != null && utlatande.getDodsdatum()
                    .vagueDateAfterDate(utlatande.getAntraffatDodDatum().asLocalDate())) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                        ValidationMessageType.INCORRECT_COMBINATION, prefix + ".validation.datum.innanDodsdatum",
                        ANTRAFFAT_DOD_DATUM_DELSVAR_ID);
                } else if (utlatande.getAntraffatDodDatum().isBefore(patientBirthDate)) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                        ValidationMessageType.OTHER, "common.validation.date.beforePatientBirthDate", ANTRAFFAT_DOD_DATUM_DELSVAR_ID);
                }
            }

        } else {
            if (utlatande.getAntraffatDodDatum() != null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", ANTRAFFAT_DOD_DATUM_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION, prefix + ".validation.antraffatDod.dodsdatumSakert",
                    ANTRAFFAT_DOD_DATUM_DELSVAR_ID);
            }
        }
    }

    public static void validateDodsplats(SosUtlatande utlatande, List<ValidationMessage> validationMessages,
        TypeAheadProvider typeAheadProvider) {
        if (Strings.nullToEmpty(utlatande.getDodsplatsKommun()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSPLATS_KOMMUN_JSON_ID,
                ValidationMessageType.EMPTY, DODSPLATS_KOMMUN_DELSVAR_ID);
        } else if (typeAheadProvider != null) {
            final var value = utlatande.getDodsplatsKommun();
            final var validMunicipalityValues = typeAheadProvider.getValues(TypeAheadEnum.MUNICIPALITIES);

            if (!validMunicipalityValues.contains(value)) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSPLATS_KOMMUN_JSON_ID,
                    ValidationMessageType.INVALID_FORMAT, "common.validation.ue-typeahead.invalid", DODSPLATS_KOMMUN_DELSVAR_ID);
            }
        }

        // R4
        if (utlatande.getDodsplatsBoende() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "dodsdatumOchdodsPlats", DODSPLATS_BOENDE_JSON_ID,
                ValidationMessageType.EMPTY, DODSPLATS_BOENDE_DELSVAR_ID);
        }
    }

    public static void validateBarn(SosUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getBarn() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "barnSomAvlidit", BARN_JSON_ID, ValidationMessageType.EMPTY,
                BARN_DELSVAR_ID);
        } else if (utlatande.getDodsdatumSakert() != null && utlatande.getDodsdatumSakert()
            && utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate()) {
            // R20
            LocalDate patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(
                utlatande.getGrundData().getPatient().getPersonId());

            if (utlatande.getBarn() && utlatande.getDodsdatum().asLocalDate().isAfter(
                patientBirthDate.plus(BARN_SOM_AVLIDIT_INOM_28_DAGAR, ChronoUnit.DAYS))) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "barnSomAvlidit", BARN_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION, BARN_DELSVAR_ID);
            } else if (!utlatande.getBarn() && utlatande.getDodsdatum().asLocalDate().isBefore(
                patientBirthDate.plus(BARN_SOM_AVLIDIT_INOM_28_DAGAR + 1, ChronoUnit.DAYS))) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "barnSomAvlidit", BARN_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION, BARN_DELSVAR_ID);
            }
        }
    }

}
