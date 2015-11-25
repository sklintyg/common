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
