/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
 *
 * @author npet
 *
 */
public interface WebcertModuleService {

    /**
     * Validates a diagnosis code using the DiagnosService.
     *
     * @param codeFragment
     *            The code to validate
     * @param codeSystemStr
     *            A string representing the code system the diagnosis belongs to
     * @return true if the code matches a diagnosis, false otherwise.
     */
    boolean validateDiagnosisCode(String codeFragment, String codeSystemStr);

    /**
     * Validates a diagnosis code using the DiagnosService.
     *
     * @param codeFragment
     *              The code to validate
     * @param codeSystem
     *            Enum representing the code system the diagnosis belongs to
     * @return true if the code matches a diagnosis, false otherwise.
     */
    boolean validateDiagnosisCode(String codeFragment, Diagnoskodverk codeSystem);
}
