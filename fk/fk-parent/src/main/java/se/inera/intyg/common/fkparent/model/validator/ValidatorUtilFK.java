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
package se.inera.intyg.common.fkparent.model.validator;

// CHECKSTYLE:OFF LineLength

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_JSON_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import com.google.common.base.Strings;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;

public class ValidatorUtilFK {

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorUtilFK.class);

    private static final int MIN_SIZE_PSYKISK_DIAGNOS = 4;
    private static final int MIN_SIZE_DIAGNOS = 3;
    private static final int MAX_SIZE_DIAGNOS = 5;

    private static final String CATEGORY_GRUNDFORMU = "grundformu";
    private static final String CATEGORY_DIAGNOS = "diagnos";

    public enum GrundForMu {
        UNDERSOKNING,
        JOURNALUPPGIFTER,
        ANHORIGSBESKRIVNING,
        ANNAT,
        TELEFONKONTAKT;

        public String getFieldName() {
            switch (this) {
                case UNDERSOKNING:
                    return GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
                case ANHORIGSBESKRIVNING:
                    return GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
                case JOURNALUPPGIFTER:
                    return GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
                case ANNAT:
                    return GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
                case TELEFONKONTAKT:
                    return GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
            }
            return "annat";
        }
    }

    public void validateDiagnose(List<Diagnos> diagnoser, List<ValidationMessage> validationMessages) {

        if (diagnoser == null || diagnoser.isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS,
                DIAGNOS_SVAR_JSON_ID_6, ValidationMessageType.EMPTY, "common.validation.diagnos.missing", DIAGNOS_SVAR_ID_6);
            return;
        }

        if (diagnoser.size() > 1 && !validateFirstDiagnosIsPresent(diagnoser)) {
            // Om första diagnosen saknas, så ska det visas fel för hela första raden. Då ska inga andra fel visas.
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS, DIAGNOS_SVAR_JSON_ID_6 + "[0].row",
                ValidationMessageType.INCORRECT_COMBINATION, "common.validation.c-13a", DIAGNOS_SVAR_ID_6);
            return;
        }

        // Alla diagnoser måste härröra från samma kodverk, använd huvuddiagnosens kodverk som bas.
        String kodverk = Strings.nullToEmpty(diagnoser.get(0).getDiagnosKodSystem()).trim();

        for (int i = 0; i < diagnoser.size(); i++) {
            Diagnos diagnos = diagnoser.get(i);

            /*
             * R8 För delfråga 6.2 ska diagnoskod anges med så många positioner som möjligt, men minst tre positioner
             * (t.ex. F32).
             * R9 För delfråga 6.2 ska diagnoskod anges med minst fyra positioner då en psykisk diagnos anges.
             * Med psykisk diagnos avses alla diagnoser som börjar med Z73 eller med F (dvs. som tillhör F-kapitlet i
             * ICD-10).
             */
            if (Strings.nullToEmpty(diagnos.getDiagnosKod()).trim().isEmpty()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS,
                    DIAGNOS_SVAR_JSON_ID_6 + "[" + i + "].diagnoskod",
                    ValidationMessageType.EMPTY, "common.validation.diagnos.codemissing", DIAGNOS_SVAR_ID_6);
            } else {
                String trimDiagnoskod = diagnos.getDiagnosKod().trim().toUpperCase();
                if ((trimDiagnoskod.startsWith("Z73") || trimDiagnoskod.startsWith("F"))
                    && trimDiagnoskod.length() < MIN_SIZE_PSYKISK_DIAGNOS) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS,
                        DIAGNOS_SVAR_JSON_ID_6 + "[" + i + "].diagnoskod",
                        ValidationMessageType.INVALID_FORMAT, "common.validation.diagnos.psykisk.length-4", DIAGNOS_SVAR_ID_6);
                } else if (trimDiagnoskod.length() < MIN_SIZE_DIAGNOS) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS,
                        DIAGNOS_SVAR_JSON_ID_6 + "[" + i + "].diagnoskod",
                        ValidationMessageType.INVALID_FORMAT, "common.validation.diagnos.length-3", DIAGNOS_SVAR_ID_6);
                } else if (trimDiagnoskod.length() > MAX_SIZE_DIAGNOS) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS,
                        DIAGNOS_SVAR_JSON_ID_6 + "[" + i + "].diagnoskod",
                        ValidationMessageType.INVALID_FORMAT, "common.validation.diagnos.length-5", DIAGNOS_SVAR_ID_6);
                } else {
                    validateDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(),
                        "common.validation.diagnos.invalid", validationMessages,
                        DIAGNOS_SVAR_JSON_ID_6 + "[" + i + "].diagnoskod");
                }
            }
            if (Strings.nullToEmpty(diagnos.getDiagnosBeskrivning()).trim().isEmpty()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS,
                    DIAGNOS_SVAR_JSON_ID_6 + "[" + i + "].diagnoskod",
                    ValidationMessageType.EMPTY, "common.validation.diagnos.description.missing", DIAGNOS_SVAR_ID_6);
            }
            if (!Strings.nullToEmpty(diagnos.getDiagnosKodSystem()).trim().isEmpty()
                && !kodverk.equals(diagnos.getDiagnosKodSystem().trim())) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS,
                    DIAGNOS_SVAR_JSON_ID_6 + "[" + i + "].diagnoskod", ValidationMessageType.INCORRECT_COMBINATION,
                    "common.validation.diagnos.invalid_combination", DIAGNOS_SVAR_ID_6);
            }
        }
    }

    private Boolean validateFirstDiagnosIsPresent(List<Diagnos> diagnoser) {
        Diagnos diagnos = diagnoser.get(0);
        return !Strings.nullToEmpty(diagnos.getDiagnosKod()).trim().isEmpty()
            || !Strings.nullToEmpty(diagnos.getDiagnosBeskrivning()).trim().isEmpty();
    }

    private void validateDiagnosKod(String diagnosKod, String kodsystem, String msgKey,
        List<ValidationMessage> validationMessages, String fieldId) {
        // if moduleService is not available, skip this validation
        if (moduleService == null) {
            LOG.warn("Forced to skip validation of diagnosKod since an implementation of ModuleService is not available");
            return;
        }

        if (!moduleService.validateDiagnosisCodeFormat(diagnosKod)
            || !moduleService.validateDiagnosisCode(diagnosKod, kodsystem)) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIAGNOS, fieldId,
                ValidationMessageType.INVALID_FORMAT, msgKey, DIAGNOS_SVAR_ID_6);
        }

    }

    public static void validateGrundForMuDate(InternalDate date, List<ValidationMessage> validationMessages, GrundForMu type) {
        ValidatorUtil.validateDateAndCheckIfFuture(date, validationMessages, CATEGORY_GRUNDFORMU, type.getFieldName(),
            "common.validation.c-06", GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);
    }
}
