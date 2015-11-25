package se.inera.intyg.common.support.modules.support.api;

import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

/**
 * The module API defines methods that allows an module to invoke methods on the Module container.
 */
public interface ModuleContainerApi {

    /**
     * Signal the reception of a certificate to the module container.
     */
    void certificateReceived(CertificateHolder certificate) throws CertificateAlreadyExistsException, InvalidCertificateException;

    /**
     * Get a certificate from the module container.
     */
    CertificateHolder getCertificate(String certificateId, Personnummer personId, boolean checkConsent) throws InvalidCertificateException;

}
