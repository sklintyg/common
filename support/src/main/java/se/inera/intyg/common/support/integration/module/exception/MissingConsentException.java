/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

/**
 * @author andreaskaltenbach
 */
public class MissingConsentException extends RuntimeException {

    private static final long serialVersionUID = -2935854410295967047L;

    public MissingConsentException(Personnummer civicRegistrationNumber) {
        super(String.format("Consent required from user %s", Personnummer.getPnrHashSafe(civicRegistrationNumber)));
    }
}
