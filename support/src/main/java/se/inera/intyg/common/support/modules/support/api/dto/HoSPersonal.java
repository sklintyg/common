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
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

public class HoSPersonal {

    private final String hsaId;

    private final String namn;

    private final String forskrivarkod;

    private final String befattning;

    private List<String> specialiseringar;

    private final Vardenhet vardenhet;

    public HoSPersonal(String hsaId, String namn, String forskrivarkod, String befattning, List<String> specialiseringar, Vardenhet vardenhet) {
        hasText(hsaId, "'hsaId' must not be empty");
        hasText(namn, "'namn' must not be empty");
        notNull(vardenhet, "'vardenhet' must not be null");
        this.hsaId = hsaId;
        this.namn = namn;
        this.vardenhet = vardenhet;
        this.forskrivarkod = forskrivarkod;
        this.befattning = befattning;
        if (specialiseringar != null) {
            this.specialiseringar = new ArrayList<String>();
            this.specialiseringar.addAll(specialiseringar);
        }
    }

    public String getHsaId() {
        return hsaId;
    }

    public String getNamn() {
        return namn;
    }

    public String getForskrivarkod() {
        return forskrivarkod;
    }

    public String getBefattning() {
        return befattning;
    }

    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public List<String> getSpecialiseringar() {
        return specialiseringar;
    }
}
