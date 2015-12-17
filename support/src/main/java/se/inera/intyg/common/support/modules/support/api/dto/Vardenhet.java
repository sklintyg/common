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

public class Vardenhet {

    private final String hsaId;

    private final String namn;

    private final String postadress;

    private final String postnummer;

    private final String postort;

    private final String telefonnummer;

    private final String epost;

    private final Vardgivare vardgivare;

    private final String arbetsplatskod;

    // CHECKSTYLE:OFF ParameterNumber
    public Vardenhet(String hsaId, String namn, String postadress, String postnummer, String postort,
            String telefonnummer, String epost, String arbetsplatskod, Vardgivare vardgivare) {
        hasText(hsaId, "'hsaId' must not be empty");
        hasText(namn, "'namn' must not be empty");
        notNull(vardgivare, "'vardgivare' must not be null");
        this.hsaId = hsaId;
        this.namn = namn;
        this.postadress = postadress;
        this.postnummer = postnummer;
        this.postort = postort;
        this.telefonnummer = telefonnummer;
        this.epost = epost;
        this.vardgivare = vardgivare;
        this.arbetsplatskod = arbetsplatskod;
    }
    // CHECKSTYLE:OFF ParameterNumber

    public String getHsaId() {
        return hsaId;
    }

    public String getNamn() {
        return namn;
    }

    public Vardgivare getVardgivare() {
        return vardgivare;
    }

    public String getPostadress() {
        return postadress;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public String getPostort() {
        return postort;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public String getEpost() {
        return epost;
    }

    public String getArbetsplatskod() {
        return arbetsplatskod;
    }

}
