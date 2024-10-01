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
package se.inera.intyg.common.tstrk1009.v1.validator;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.ANMALAN_AVSER_JSON_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.CATEGORY_ANMALAN;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.CATEGORY_BEDOMNING;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.CATEGORY_IDENTITET;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.CATEGORY_MEDICINSKAFORHALLANDEN;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_JSON_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INTYGET_AVSER_BEHORIGHET_JSON_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.MEDICINSKA_FORHALLANDEN_JSON_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.SENASTE_UNDERSOKNINGSDATUM_JSON_ID;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;
import se.inera.intyg.common.tstrk1009.v1.model.internal.KorkortBehorighetGrupp;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;

@Component("tstrk1009.v1.InternalDraftValidatorImpl")
public class InternalDraftValidatorImpl implements InternalDraftValidator<Tstrk1009UtlatandeV1> {

    protected static final String VALIDATION_COMMON_EXCEEDS_MAXDATE = "common.validation.date.today.or.earlier";
    public static final int FOUR = 4;

    @Override
    public ValidateDraftResponse validateDraft(Tstrk1009UtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        validateIdentitet(utlatande, validationMessages);
        validateOlamplighet(utlatande, validationMessages);
        validateGrundForOlamplighet(utlatande, validationMessages);
        validateBehorighet(utlatande, validationMessages);
        validateOmExtraInfoOnskas(utlatande, validationMessages);

        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private static void validateIdentitet(Tstrk1009UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Identitet styrkt genom
        if (utlatande.getIdentitetStyrktGenom() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_IDENTITET, IDENTITET_STYRKT_GENOM_JSON_ID + ".typ",
                ValidationMessageType.EMPTY);
        }
    }

    private static void validateOlamplighet(Tstrk1009UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Anmälan avser olämplighet eller sannolik olämplighet
        if (utlatande.getAnmalanAvser() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ANMALAN, ANMALAN_AVSER_JSON_ID + ".typ",
                ValidationMessageType.EMPTY);
        }
    }

    private static void validateGrundForOlamplighet(Tstrk1009UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Medicinska förhållanden som utgör grund för olämplighet
        if (Strings.isNullOrEmpty(utlatande.getMedicinskaForhallanden())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKAFORHALLANDEN, MEDICINSKA_FORHALLANDEN_JSON_ID,
                ValidationMessageType.EMPTY);
        }

        if (ValidatorUtil.validateDate(utlatande.getSenasteUndersokningsdatum(), validationMessages,
            CATEGORY_MEDICINSKAFORHALLANDEN, SENASTE_UNDERSOKNINGSDATUM_JSON_ID, null)) {

            if (eligibleForRule1(utlatande)) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKAFORHALLANDEN, SENASTE_UNDERSOKNINGSDATUM_JSON_ID,
                    ValidationMessageType.OTHER, VALIDATION_COMMON_EXCEEDS_MAXDATE);
            }
        }
    }

    private static void validateBehorighet(Tstrk1009UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Intyget avser behörighet
        if (utlatande.getIntygetAvserBehorigheter() == null || isEmpty(utlatande.getIntygetAvserBehorigheter().getTyper())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, INTYGET_AVSER_BEHORIGHET_JSON_ID + ".typer",
                ValidationMessageType.EMPTY);
        } else if (utlatande.getIntygetAvserBehorigheter().getTyper().size() > FOUR) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, INTYGET_AVSER_BEHORIGHET_JSON_ID + ".typer",
                ValidationMessageType.OTHER);
        } else if (eligibleForRule2To10(utlatande)) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, INTYGET_AVSER_BEHORIGHET_JSON_ID + ".typer",
                ValidationMessageType.OTHER);
        }
    }

    private static void validateOmExtraInfoOnskas(Tstrk1009UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Ej obligatorisk fraga -> ingen validering.
    }

    private static boolean eligibleForRule1(Tstrk1009UtlatandeV1 utlatande) {
        return utlatande.getSenasteUndersokningsdatum() != null
            && utlatande.getSenasteUndersokningsdatum().isValidDate()
            && utlatande.getSenasteUndersokningsdatum().asLocalDate().isAfter(LocalDate.now());
    }

    private static boolean eligibleForRule2To10(Tstrk1009UtlatandeV1 utlatande) {
        if (utlatande.getIntygetAvserBehorigheter() == null || utlatande.getIntygetAvserBehorigheter().getTyper() == null) {
            return false;
        }
        Set<KorkortBehorighetGrupp> behorighetsGrupper = utlatande.getIntygetAvserBehorigheter().getTyper();

        // R2
        if (behorighetsGrupper.contains(KorkortBehorighetGrupp.ALLA) && behorighetsGrupper.size() != 1) {
            return true;
        }

        // R8
        if (behorighetsGrupper.contains(KorkortBehorighetGrupp.KANINTETASTALLNING) && behorighetsGrupper.size() != 1) {
            return true;
        }

        return false;
    }
}
