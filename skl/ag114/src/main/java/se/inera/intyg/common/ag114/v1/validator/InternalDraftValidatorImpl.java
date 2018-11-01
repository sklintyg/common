/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag114.v1.validator;

import static se.inera.intyg.common.agparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_9;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_1;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_JSON_ID_7_1;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.SJUKSKRIVNINGSPERIOD_SVAR_JSON_ID_7_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

@Component(value = "ag114.InternalDraftValidatorImpl.v1")
public class InternalDraftValidatorImpl implements InternalDraftValidator<Ag114UtlatandeV1> {

    private static final String CATEGORY_SYSSELSATTNING = "sysselsattning";
    private static final String CATEGORY_DIAGNOS = "diagnos";
    private static final String CATEGORY_ARBETSFORMAGA = "arbetsformaga";
    private static final String CATEGORY_BEDOMNING = "bedomning";
    private static final String CATEGORY_OVRIGT = "ovrigt";
    private static final String CATEGORY_KONTAKT = "kontakt";

    private static final int SJUKSKRIVNINGSGRAD_FROM = 0;
    private static final int SJUKSKRIVNINGSGRAD_TOM = 100;

    @Autowired
    private ValidatorUtilSKL validatorUtilSKL;

    @Override
    public ValidateDraftResponse validateDraft(Ag114UtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        validateSysselsattning(utlatande, validationMessages);
        validateDiagnos(utlatande, validationMessages);
        validateNedsattArbetsformaga(utlatande, validationMessages);
        validateArbetsformagaTrotsSjukdom(utlatande, validationMessages);
        validateBedomning(utlatande, validationMessages);
        validateKontakt(utlatande, validationMessages);
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateBedomning(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {

        // Sjukskrivningsgrad
        if (validatorUtilSKL.hasNoContent(utlatande.getSjukskrivningsgrad())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, SJUKSKRIVNINGSGRAD_SVAR_JSON_ID_7_1,
                    ValidationMessageType.EMPTY);
        } else {
            if (!validatorUtilSKL.isIntInRange(utlatande.getSjukskrivningsgrad(), SJUKSKRIVNINGSGRAD_FROM, SJUKSKRIVNINGSGRAD_TOM)) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, SJUKSKRIVNINGSGRAD_SVAR_JSON_ID_7_1,
                        ValidationMessageType.OTHER, "ag114.validation.sjukskrivningsgrad.invalid.percent");
            }
        }
        // Sjukskrivningsperiod
        if (utlatande.getSjukskrivningsperiod() == null || utlatande.getSjukskrivningsperiod().getFrom() == null
                || !utlatande.getSjukskrivningsperiod().getFrom().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, SJUKSKRIVNINGSPERIOD_SVAR_JSON_ID_7_2 + ".from",
                    ValidationMessageType.EMPTY);
        }

        if (utlatande.getSjukskrivningsperiod() == null || utlatande.getSjukskrivningsperiod().getTom() == null
                || !utlatande.getSjukskrivningsperiod().getTom().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, SJUKSKRIVNINGSPERIOD_SVAR_JSON_ID_7_2 + ".tom",
                    ValidationMessageType.EMPTY);
        }

        if (utlatande.getSjukskrivningsperiod() != null && utlatande.getSjukskrivningsperiod().getTom() != null
                && utlatande.getSjukskrivningsperiod().getFrom() != null && !utlatande.getSjukskrivningsperiod().isValid()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, SJUKSKRIVNINGSPERIOD_SVAR_JSON_ID_7_2 + ".from",
                    ValidationMessageType.INCORRECT_COMBINATION, "common.validation.date-period.invalid_order");
        }

    }

    private void validateSysselsattning(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // SysselSattning skall alltid ha fixed värde NUVARANDE_ARBETE
        if (utlatande.getSysselsattning() == null
                || utlatande.getSysselsattning().size() != 1
                || !utlatande.getSysselsattning().get(0).getTyp().equals(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYSSELSATTNING, TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1,
                    ValidationMessageType.EMPTY);
        }

        // NuvarandeArbete är mandatory
        if (validatorUtilSKL.hasNoContent(utlatande.getNuvarandeArbete())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYSSELSATTNING, NUVARANDE_ARBETE_SVAR_JSON_ID_2,
                    ValidationMessageType.EMPTY);
        }

    }

    private void validateDiagnos(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getOnskarFormedlaDiagnos() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS, ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3,
                    ValidationMessageType.EMPTY);
        } else if (utlatande.getOnskarFormedlaDiagnos()) {
            validatorUtilSKL.validateDiagnose(utlatande.getDiagnoser(), validationMessages);
        }
    }

    private void validateNedsattArbetsformaga(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (validatorUtilSKL.hasNoContent(utlatande.getNedsattArbetsformaga())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ARBETSFORMAGA, NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateArbetsformagaTrotsSjukdom(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Ja/Nej obligatoriskt att besvara..
        if (utlatande.getArbetsformagaTrotsSjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ARBETSFORMAGA,
                    ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_1,
                    ValidationMessageType.EMPTY);
        } else if (utlatande.getArbetsformagaTrotsSjukdom()
                && validatorUtilSKL.hasNoContent(utlatande.getArbetsformagaTrotsSjukdomBeskrivning())) {
            // Om ja, så är beskrivningen obligatoriskt
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ARBETSFORMAGA,
                    ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_2,
                    ValidationMessageType.EMPTY);
        }
    }

    // R2
    private void validateKontakt(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Om kryssat ja, är beskrivning mandatory
        if (utlatande.getKontaktMedArbetsgivaren() != null && utlatande.getKontaktMedArbetsgivaren()
                && validatorUtilSKL.hasNoContent(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_KONTAKT, ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_9,
                    ValidationMessageType.EMPTY);
        }
        // ..men får ej förekomma om ej kryssat ja
        if (utlatande.getKontaktMedArbetsgivaren() == null || !utlatande.getKontaktMedArbetsgivaren()
                && !validatorUtilSKL.hasNoContent(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_KONTAKT, ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_9,
                    ValidationMessageType.INCORRECT_COMBINATION);
        }
    }
}
