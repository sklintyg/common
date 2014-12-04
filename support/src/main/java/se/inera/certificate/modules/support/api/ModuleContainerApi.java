package se.inera.certificate.modules.support.api;

import se.inera.certificate.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.certificate.integration.module.exception.CertificateRevokedException;
import se.inera.certificate.integration.module.exception.InvalidCertificateException;

/**
 * The module API defines methods that allows an module to invoke methods on the Module container.
 */
public interface ModuleContainerApi {
    
    /**
     * Signal the reception of a certificate to the module container.
     */
    public void certificateReceived(CertificateHolder certificate, boolean wireTapped) throws CertificateAlreadyExistsException, InvalidCertificateException;

    /**
     * Get a certificate from the module container.
     */
    public CertificateHolder getCertificate(String certificateId, String personId, boolean checkConsent) throws InvalidCertificateException;

}
