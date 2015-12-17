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

package se.inera.intyg.common.support.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;

public class Vardgivare {

    private final String hsaId;

    private final String namn;

    public Vardgivare(String hsaId, String namn) {
        hasText(hsaId, "'hsaId' must not be empty");
        hasText(namn, "'namn' must not be empty");
        this.hsaId = hsaId;
        this.namn = namn;
    }

    public String getHsaId() {
        return hsaId;
    }

    public String getNamn() {
        return namn;
    }
}
