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
package se.inera.intyg.common.ag114.v1.validator;

import static se.inera.intyg.common.ag114.model.converter.RespConstants.TYP_AV_DIAGNOS_SVAR_JSON_ID_4;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;

// CHECKSTYLE:OFF LineLength

// CHECKSTYLE:ON LineLength

/**
 * Derived from the ValidatorUtilFK.
 */
public class ValidatorUtilSKL {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorUtilSKL.class);
    private static final int MIN_SIZE_PSYKISK_DIAGNOS = 4;
    private static final int MIN_SIZE_DIAGNOS = 3;
    private static final int MAX_SIZE_DIAGNOS = 5;
    private static final String CATEGORY_DIAGNOS = "diagnos";

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    public void validateDiagnose(List<Diagnos> diagnoser, List<ValidationMessage> validationMessages) {

        if (diagnoser == null || diagnoser.isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS,
                TYP_AV_DIAGNOS_SVAR_JSON_ID_4,
                ValidationMessageType.EMPTY,
                "common.validation.diagnos.missing");
            return;
        }

        if (diagnoser.size() > 1 && !validateFirstDiagnosIsPresent(diagnoser)) {
            // Om första diagnosen saknas, så ska det visas fel för hela första raden. Då ska inga andra fel visas.
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS,
                TYP_AV_DIAGNOS_SVAR_JSON_ID_4 + "[0].row",
                ValidationMessageType.INCORRECT_COMBINATION, "common.validation.c-13a");
            return;
        }

        // Alla diagnoser måste härröra från samma kodverk, använd huvuddiagnosens kodverk som bas.
        String kodverk = Strings.nullToEmpty(diagnoser.get(0).getDiagnosKodSystem()).trim();

        for (int i = 0; i < diagnoser.size(); i++) {
            Diagnos diagnos = diagnoser.get(i);

            /*
             * R8 För delfråga 4.2 ska diagnoskod anges med så många positioner som möjligt, men minst tre positioner
             * (t.ex. F32).
             * R9 För delfråga 4.2 ska diagnoskod anges med minst fyra positioner då en psykisk diagnos anges.
             * Med psykisk diagnos avses alla diagnoser som börjar med Z73 eller med F (dvs. som tillhör F-kapitlet i
             * ICD-10).
             */
            if (Strings.nullToEmpty(diagnos.getDiagnosKod()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS,
                    TYP_AV_DIAGNOS_SVAR_JSON_ID_4 + "[" + i + "].diagnoskod",
                    ValidationMessageType.EMPTY, "common.validation.diagnos.codemissing");
            } else {
                String trimDiagnoskod = diagnos.getDiagnosKod().trim().toUpperCase();
                if ((trimDiagnoskod.startsWith("Z73") || trimDiagnoskod.startsWith("F"))
                    && trimDiagnoskod.length() < MIN_SIZE_PSYKISK_DIAGNOS) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS,
                        TYP_AV_DIAGNOS_SVAR_JSON_ID_4 + "[" + i + "].diagnoskod",
                        ValidationMessageType.INVALID_FORMAT,
                        "common.validation.diagnos.psykisk.length-4");
                } else if (trimDiagnoskod.length() < MIN_SIZE_DIAGNOS) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS,
                        TYP_AV_DIAGNOS_SVAR_JSON_ID_4 + "[" + i + "].diagnoskod",
                        ValidationMessageType.INVALID_FORMAT,
                        "common.validation.diagnos.length-3");
                } else if (trimDiagnoskod.length() > MAX_SIZE_DIAGNOS) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS,
                        TYP_AV_DIAGNOS_SVAR_JSON_ID_4 + "[" + i + "].diagnoskod",
                        ValidationMessageType.INVALID_FORMAT,
                        "common.validation.diagnos.length-5");
                } else {
                    validateDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(),
                        "common.validation.diagnos.invalid", validationMessages);
                }
            }
            if (Strings.nullToEmpty(diagnos.getDiagnosBeskrivning()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS,
                    TYP_AV_DIAGNOS_SVAR_JSON_ID_4 + "[" + i + "].diagnosbeskrivning",
                    ValidationMessageType.EMPTY,
                    "common.validation.diagnos.description.missing");
            }
            if (!Strings.nullToEmpty(diagnos.getDiagnosKodSystem()).trim().isEmpty()
                && !kodverk.equals(diagnos.getDiagnosKodSystem().trim())) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS, TYP_AV_DIAGNOS_SVAR_JSON_ID_4 + ".diagnoskodsystem",
                    ValidationMessageType.INCORRECT_COMBINATION,
                    "common.validation.diagnos.invalid_combination");
            }
        }
    }

    private void validateDiagnosKod(String diagnosKod, String kodsystem, String msgKey,
        List<ValidationMessage> validationMessages) {
        // if moduleService is not available, skip this validation
        if (moduleService == null) {
            LOG.warn("Forced to skip validation of diagnosKod since an implementation of ModuleService is not available");
            return;
        }

        if (!moduleService.validateDiagnosisCode(diagnosKod, kodsystem)) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS, TYP_AV_DIAGNOS_SVAR_JSON_ID_4,
                ValidationMessageType.INVALID_FORMAT, msgKey);
        }

    }

    private Boolean validateFirstDiagnosIsPresent(List<Diagnos> diagnoser) {
        Diagnos diagnos = diagnoser.get(0);
        return !Strings.nullToEmpty(diagnos.getDiagnosKod()).trim().isEmpty()
            || !Strings.nullToEmpty(diagnos.getDiagnosBeskrivning()).trim().isEmpty();
    }

    public boolean isIntInRange(String intString, int min, int max) {
        final Integer parsedInteger = Ints.tryParse(intString);

        if (parsedInteger == null) {
            return false;
        }

        if (intString.length() > 1 && intString.charAt(0) == '0') {
            return false;
        }

        return parsedInteger >= min && parsedInteger <= max;
    }

    public boolean hasNoContent(String stringValue) {
        return (stringValue == null || stringValue.trim().isEmpty());
    }
}
