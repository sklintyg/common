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
package se.inera.intyg.common.support.model;

public enum StatusKod {

    RECEIV("RECEIVED"),
    SENTTO("SENT"),
    CANCEL("CANCELLED"),
    DELETE("DELETED"),
    RESTOR("RESTORED");

    private final String displayName;

    StatusKod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public CertificateState toCertificateState() {
        switch (this) {
            case RECEIV:
                return CertificateState.RECEIVED;
            case SENTTO:
                return CertificateState.SENT;
            case CANCEL:
                return CertificateState.CANCELLED;
            case DELETE:
                return CertificateState.DELETED;
            case RESTOR:
                return CertificateState.RESTORED;
        }
        return null;
    }
}
