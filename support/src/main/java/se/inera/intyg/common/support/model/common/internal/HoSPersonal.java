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
package se.inera.intyg.common.support.model.common.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoSPersonal {

    private String personId;

    private String fullstandigtNamn;

    private String forskrivarKod;

    private List<String> befattningar;

    private List<String> specialiteter;

    private Vardenhet vardenhet;

    public HoSPersonal() {

    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final HoSPersonal that = (HoSPersonal) object;
        return Objects.equals(this.personId, that.personId)
                && Objects.equals(this.fullstandigtNamn, that.fullstandigtNamn)
                && Objects.equals(this.forskrivarKod, that.forskrivarKod)
                && Objects.deepEquals(this.befattningar, that.befattningar)
                && Objects.deepEquals(this.specialiteter, that.specialiteter)
                && Objects.equals(this.vardenhet, that.vardenhet);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.personId, this.fullstandigtNamn, this.forskrivarKod, this.befattningar, this.specialiteter);
    }

    public List<String> getSpecialiteter() {
        if (specialiteter == null) {
            specialiteter = new ArrayList<>();
        }
        return specialiteter;
    }

    public List<String> getBefattningar() {
        if (befattningar == null) {
            befattningar = new ArrayList<>();
        }
        return befattningar;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFullstandigtNamn() {
        return fullstandigtNamn;
    }

    public void setFullstandigtNamn(String fullstandigtNamn) {
        this.fullstandigtNamn = fullstandigtNamn;
    }

    public String getForskrivarKod() {
        return forskrivarKod;
    }

    public void setForskrivarKod(String forskrivarKod) {
        this.forskrivarKod = forskrivarKod;
    }

    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }

}
