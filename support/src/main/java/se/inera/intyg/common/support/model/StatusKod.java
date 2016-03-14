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

package se.inera.intyg.common.support.model;

import java.util.Optional;
import java.util.stream.Stream;

public enum StatusKod {

    RECEIV("RECEIVED"),
    SENTTO("SENT"),
    CANCEL("CANCELLED"),
    DELETE("DELETED"),
    RESTOR("RESTORED");

    private String displayName;

    StatusKod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Optional<StatusKod> fromString(String code) {
        return Stream.of(StatusKod.values()).filter(s -> s.name().equals(code)).findFirst();
    }

    public Optional<CertificateState> toCertificateState() {
        switch (this) {
        case RECEIV:
            return Optional.of(CertificateState.RECEIVED);
        case SENTTO:
            return Optional.of(CertificateState.SENT);
        case CANCEL:
            return Optional.of(CertificateState.CANCELLED);
        case DELETE:
            return Optional.of(CertificateState.DELETED);
        case RESTOR:
            return Optional.of(CertificateState.RESTORED);
        default:
            return Optional.empty();
        }
    }
}
