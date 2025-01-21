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

import java.time.LocalDateTime;

/**
 * @author andreaskaltenbach
 */
public class Status {

    private CertificateState type;

    private String target;

    private LocalDateTime timestamp;

    public Status(CertificateState type, String target, LocalDateTime timestamp) {
        this.type = type;
        this.target = target;
        this.timestamp = timestamp;
    }

    public Status() {
        // Needed for deserialization
    }

    public CertificateState getType() {
        return type;
    }

    public void setType(CertificateState type) {
        this.type = type;
    }

    public final String getTarget() {
        return target;
    }

    public final void setTarget(String target) {
        this.target = target;
    }

    public final LocalDateTime getTimestamp() {
        return timestamp;
    }

    public final void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
