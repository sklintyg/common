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
package se.inera.intyg.common.support.modules.support.api;

import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.schemas.contract.Personnummer;

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

    /**
     * Logs the event of a certificate retrieved by a part. It's placed here as modules do not have access to an applications monitoringlog
     * service.
     */
    void logCertificateRetrieved(String certificateId, String certificateType, String careUnit, String partId);

}
