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
package se.inera.intyg.common.support.modules.support.api.notification;

import java.util.Optional;
import java.util.stream.Stream;

public enum SchemaVersion {
    VERSION_1("V1"), VERSION_3("V3");

    private final String version;

    SchemaVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public static Optional<SchemaVersion> fromString(String version) {
        return Stream.of(SchemaVersion.values()).filter(s -> version.equals(s.getVersion())).findFirst();
    }
}
