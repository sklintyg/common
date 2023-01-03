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
package se.inera.intyg.common.support.integration.module.exception;

import se.inera.intyg.schemas.contract.Personnummer;

import java.util.Optional;

/**
 * Exception thrown whenever a certificate with unknown certificate ID is tried to access, or the civic registration
 * number doesn't match the one in the certificate.
 *
 * @author andreaskaltenbach
 */
public class InvalidCertificateException extends Exception {

    private static final long serialVersionUID = 9207157337550587128L;

    public InvalidCertificateException(String certificateId, Personnummer personnummer) {
        super(isValidPersonnummer(personnummer)
            ? String.format("Certificate '%s' does not exist for user '%s'", certificateId, personnummer.getPersonnummerHash())
            : String.format("Unknown certificate ID: %s", certificateId));
    }

    private static boolean isValidPersonnummer(Personnummer personnummer) {
        return Optional.ofNullable(personnummer).isPresent();
    }
}
