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
package se.inera.intyg.common.support.modules.support.api;

/**
 * Defines a source intyg type for which another intygtype can
 * extract information from.
 *
 * Created by marced on 2018-12-13.
 */
public class GetCopyFromCriteria {

    private final String intygType;
    private final String intygTypeMajorVersion;

    public GetCopyFromCriteria(String intygType, String intygTypeMajorVersion) {
        this.intygType = intygType;
        this.intygTypeMajorVersion = intygTypeMajorVersion;
    }

    public String getIntygType() {
        return intygType;
    }

    public String getIntygTypeMajorVersion() {
        return intygTypeMajorVersion;
    }
}
