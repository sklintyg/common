/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
 * Created by marced on 2018-12-13.
 */
public class GetCopyFromCandidate {
    private String intygType;
    private String intygTypeVersion;
    private String intygId;

    public GetCopyFromCandidate(String intygType, String intygTypeVersion, String intygId) {
        this.intygType = intygType;
        this.intygTypeVersion = intygTypeVersion;
        this.intygId = intygId;
    }

    public String getIntygType() {
        return intygType;
    }

    public String getIntygTypeVersion() {
        return intygTypeVersion;
    }

    public String getIntygId() {
        return intygId;
    }
}
