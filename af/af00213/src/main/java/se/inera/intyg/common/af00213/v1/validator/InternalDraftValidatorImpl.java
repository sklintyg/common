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
package se.inera.intyg.common.af00213.v1.validator;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

@Component("af00213.v1.InternalDraftValidatorImpl")
public class InternalDraftValidatorImpl implements InternalDraftValidator<Af00213UtlatandeV1> {

    @Override
    public ValidateDraftResponse validateDraft(Af00213UtlatandeV1 utlatande) {

        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 - Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);

        // Kategori 2 - Aktivitetsbegränsning
        validateAktivitetsbegransning(utlatande, validationMessages);

        // Kategori 3 - Behandling / Utredning
        validateUtredningBehandling(utlatande, validationMessages);

        // Kategori 4 - arbetetsPaverkan
        validateArbetetsPaverkan(utlatande, validationMessages);

        // Kategori 5 – Övrigt
        validateBlanksForOptionalFields(utlatande, validationMessages);

        // vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateFunktionsnedsattning(Af00213UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Yes or no must be specified.
        if (utlatande.getHarFunktionsnedsattning() == null) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, FUNKTIONSNEDSATTNING_CATEGORY_ID,
                    FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11,
                    ValidationMessageType.EMPTY, FUNKTIONSNEDSATTNING_DELSVAR_ID_11);
        }
        if (isSetToTrue(utlatande.getHarFunktionsnedsattning())
            && Strings.nullToEmpty(utlatande.getFunktionsnedsattning()).trim().isEmpty()) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, FUNKTIONSNEDSATTNING_CATEGORY_ID,
                    FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12,
                    ValidationMessageType.EMPTY, FUNKTIONSNEDSATTNING_DELSVAR_ID_12);
        }
    }

    private void validateAktivitetsbegransning(Af00213UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (isSetToTrue(utlatande.getHarFunktionsnedsattning()) && isSetToTrue(utlatande.getHarAktivitetsbegransning())
            && Strings.nullToEmpty(utlatande.getAktivitetsbegransning()).trim().isEmpty()) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, AKTIVITETSBEGRANSNING_CATEGORY_ID,
                    AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22,
                    ValidationMessageType.EMPTY, AKTIVITETSBEGRANSNING_DELSVAR_ID_22);
        }

        if (isSetToTrue(utlatande.getHarFunktionsnedsattning()) && utlatande.getHarAktivitetsbegransning() == null) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, AKTIVITETSBEGRANSNING_CATEGORY_ID,
                    AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21,
                    ValidationMessageType.EMPTY, AKTIVITETSBEGRANSNING_DELSVAR_ID_21);
        }
    }

    private void validateUtredningBehandling(Af00213UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Yes or no must be specified.
        if (utlatande.getHarUtredningBehandling() == null) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, UTREDNING_BEHANDLING_CATEGORY_ID,
                    UTREDNING_BEHANDLING_SVAR_JSON_ID_31,
                    ValidationMessageType.EMPTY, UTREDNING_BEHANDLING_DELSVAR_ID_31);
        }

        if (isSetToTrue(utlatande.getHarUtredningBehandling())
            && (utlatande.getUtredningBehandling() == null || utlatande.getUtredningBehandling().isEmpty())) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, UTREDNING_BEHANDLING_CATEGORY_ID,
                    UTREDNING_BEHANDLING_SVAR_JSON_ID_32,
                    ValidationMessageType.EMPTY, UTREDNING_BEHANDLING_DELSVAR_ID_32);
        }
    }

    private void validateBlanksForOptionalFields(Af00213UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {

        if (ValidatorUtil.isBlankButNotNull(utlatande.getUtredningBehandling())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages,
                UTREDNING_BEHANDLING_CATEGORY_ID, UTREDNING_BEHANDLING_SVAR_JSON_ID_32, ValidationMessageType.EMPTY,
                "af00213.validation.blanksteg.otillatet", UTREDNING_BEHANDLING_DELSVAR_ID_32);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, OVRIGT_CATEGORY_ID, OVRIGT_SVAR_JSON_ID_5,
                    ValidationMessageType.EMPTY,
                    "af00213.validation.blanksteg.otillatet", OVRIGT_DELSVAR_ID_5);
        }
    }

    private void validateArbetetsPaverkan(Af00213UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Yes or no must be specified.
        if (utlatande.getHarArbetetsPaverkan() == null) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, ARBETETS_PAVERKAN_CATEGORY_ID, ARBETETS_PAVERKAN_SVAR_JSON_ID_41,
                    ValidationMessageType.EMPTY, ARBETETS_PAVERKAN_DELSVAR_ID_41);
        }

        if (isSetToTrue(utlatande.getHarArbetetsPaverkan()) && Strings.nullToEmpty(utlatande.getArbetetsPaverkan()).trim().isEmpty()) {
            ValidatorUtil
                .addValidationErrorWithQuestionId(validationMessages, ARBETETS_PAVERKAN_CATEGORY_ID, ARBETETS_PAVERKAN_SVAR_JSON_ID_42,
                    ValidationMessageType.EMPTY, ARBETETS_PAVERKAN_DELSVAR_ID_42);
        }
    }

    private boolean isSetToTrue(Boolean bool) {
        return bool != null && bool;
    }
}
