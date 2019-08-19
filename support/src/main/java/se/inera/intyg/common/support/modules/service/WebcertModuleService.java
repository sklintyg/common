/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.service;

import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;

/**
 * Interface for exposing Webcert services that can be used by modules.
 */
public interface WebcertModuleService {

    /**
     * Validates a diagnosis code using the DiagnosService.
     *
     * @param codeFragment  the code to validate
     * @param codeSystemStr the string representing the code system the diagnosis belongs to
     * @return true if the code matches a diagnosis, false otherwise.
     */
    boolean validateDiagnosisCode(String codeFragment, String codeSystemStr);

    /**
     * Validates a diagnosis code using the DiagnosService.
     *
     * @param codeFragment the code to validate
     * @param codeSystem   the enum representing the code system the diagnosis belongs to
     * @return true if the code matches a diagnosis, false otherwise.
     */
    boolean validateDiagnosisCode(String codeFragment, Diagnoskodverk codeSystem);

    /**
     * Gets the description belonging to a diagnosis code.
     * <p>
     * The given code may represent a group of multiple diagnosis codes, and thus generate a list of matches instead of
     * only one. This means that the mapping is no longer 1:1. In that case, as well as in the case where there is no
     * match, description will be set to "" since we do not wish to interpret what is being delivered to us - we only
     * wish to forward the information.
     *
     * @param code          the code of the diagnosis
     * @param codeSystemStr the codeSystem used for the diagnosis code
     * @return the description if there is one and only one, otherwise empty String
     */
    String getDescriptionFromDiagnosKod(String code, String codeSystemStr);
}
