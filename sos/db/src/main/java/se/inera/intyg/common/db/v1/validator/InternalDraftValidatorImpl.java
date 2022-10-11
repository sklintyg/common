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
package se.inera.intyg.common.db.v1.validator;

import static se.inera.intyg.common.db.support.DbModuleEntryPoint.MODULE_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_AVLAGSNAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_AVLAGSNAT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_JSON_ID;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateBarn;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsdatum;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsplats;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateIdentitetStyrkt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

@Component("db.v1.InternalDraftValidatorImpl")
public class InternalDraftValidatorImpl implements InternalDraftValidator<DbUtlatandeV1> {

    public static final int FOUR_WEEKS = 4;

    @Override
    public ValidateDraftResponse validateDraft(DbUtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);

        validateIdentitetStyrkt(utlatande, validationMessages);
        validateDodsdatum(utlatande, validationMessages, MODULE_ID);
        validateDodsplats(utlatande, validationMessages);
        validateBarn(utlatande, validationMessages);
        validateExplosivtImplantat(utlatande, validationMessages);
        validateUndersokning(utlatande, validationMessages);
        validatePolisanmalan(utlatande, validationMessages);
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateExplosivtImplantat(DbUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // R5
        if (utlatande.getExplosivImplantat() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "explosivImplantat", EXPLOSIV_IMPLANTAT_JSON_ID,
                ValidationMessageType.EMPTY, EXPLOSIV_IMPLANTAT_DELSVAR_ID);
        } else if (utlatande.getExplosivImplantat() && utlatande.getExplosivAvlagsnat() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "explosivImplantat", EXPLOSIV_AVLAGSNAT_JSON_ID,
                ValidationMessageType.EMPTY, EXPLOSIV_AVLAGSNAT_DELSVAR_ID);
        } else if (!utlatande.getExplosivImplantat() && utlatande.getExplosivAvlagsnat() != null) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, "explosivImplantat", EXPLOSIV_AVLAGSNAT_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION,
                    MODULE_ID + ".validation.explosivAvlagsnat.explosivImplantatFalse", EXPLOSIV_AVLAGSNAT_DELSVAR_ID);
        }
    }

    private void validateUndersokning(DbUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // R6 & R7
        if (utlatande.getUndersokningYttre() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "yttreUndersokning", UNDERSOKNING_YTTRE_JSON_ID,
                ValidationMessageType.EMPTY, UNDERSOKNING_YTTRE_DELSVAR_ID);
        } else if (utlatande.getUndersokningYttre() == Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN) {

            if (ValidatorUtil.validateDate(utlatande.getUndersokningDatum(), validationMessages, "yttreUndersokning",
                UNDERSOKNING_DATUM_JSON_ID, null, UNDERSOKNING_DATUM_DELSVAR_ID)) {

                if (!utlatande.getUndersokningDatum().isReasonable() || utlatande.getUndersokningDatum().asLocalDate()
                    .isAfter(LocalDate.now())) {
                    ValidatorUtil
                        .addValidationErrorWithQuestionId(validationMessages, "yttreUndersokning", UNDERSOKNING_DATUM_JSON_ID,
                            ValidationMessageType.INVALID_FORMAT, "common.validation.date.today.or.earlier", UNDERSOKNING_DATUM_DELSVAR_ID);
                } else if (utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate()
                    && utlatande.getUndersokningDatum().asLocalDate().isAfter(utlatande.getDodsdatum().asLocalDate())) {
                    ValidatorUtil
                        .addValidationErrorWithQuestionId(validationMessages, "yttreUndersokning", UNDERSOKNING_DATUM_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION,
                            "db.validation.undersokningDatum.after.dodsdatum", UNDERSOKNING_DATUM_DELSVAR_ID);
                } else if (utlatande.getDodsdatumSakert() != null && utlatande.getDodsdatumSakert()
                    && utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate()
                    && utlatande.getUndersokningDatum().asLocalDate()
                    .isBefore(utlatande.getDodsdatum().asLocalDate().minusWeeks(FOUR_WEEKS))) {
                    ValidatorUtil
                        .addValidationErrorWithQuestionId(validationMessages, "yttreUndersokning", UNDERSOKNING_DATUM_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION,
                            "common.validation.date.e-06", UNDERSOKNING_DATUM_DELSVAR_ID);
                } else if ((utlatande.getDodsdatumSakert() != null && !utlatande.getDodsdatumSakert())
                    && (utlatande.getAntraffatDodDatum() != null && utlatande.getAntraffatDodDatum().isValidDate())
                    && utlatande.getUndersokningDatum().asLocalDate().isAfter(utlatande.getAntraffatDodDatum().asLocalDate())) {
                    ValidatorUtil
                        .addValidationErrorWithQuestionId(validationMessages, "yttreUndersokning", UNDERSOKNING_DATUM_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION,
                            "db.validation.undersokningDatum.after.antraffatDodDatum", UNDERSOKNING_DATUM_DELSVAR_ID);
                } else if (utlatande.getUndersokningDatum().isBeforeBeginningOfLastYear()) {
                    ValidatorUtil
                        .addValidationErrorWithQuestionId(validationMessages, "yttreUndersokning", UNDERSOKNING_DATUM_JSON_ID,
                            ValidationMessageType.OTHER,
                            "db.validation.undersokningDatum.before.beginningOflastYear", UNDERSOKNING_DATUM_DELSVAR_ID);
                }
            }
        }
    }

    private void validatePolisanmalan(DbUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getPolisanmalan() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "polisanmalan", POLISANMALAN_JSON_ID,
                ValidationMessageType.EMPTY, POLISANMALAN_DELSVAR_ID);
        } else if (utlatande.getUndersokningYttre() == Undersokning.UNDERSOKNING_SKA_GORAS && !utlatande.getPolisanmalan()) {
            // R19
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, "polisanmalan", POLISANMALAN_JSON_ID,
                ValidationMessageType.INCORRECT_COMBINATION, POLISANMALAN_DELSVAR_ID);
        }
    }
}
