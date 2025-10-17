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
package se.inera.intyg.common.support.model.common.internal;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HoSPersonal {

    private String personId;

    private String fullstandigtNamn;

    private String forskrivarKod;

    private String titel;

    private String medarbetarUppdrag;

    private List<String> befattningar;

    private List<PaTitle> befattningsKoder;

    private List<String> specialiteter;

    private Vardenhet vardenhet;

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

    public List<String> getBefattningarAsCode() {
        if (!getBefattningsKoder().isEmpty()) {
            return befattningsKoder.stream()
                .map(PaTitle::getKod)
                .collect(Collectors.toCollection(ArrayList::new));
        }
        return befattningar;
    }

    public List<PaTitle> getBefattningsKoder() {
        if (befattningsKoder == null) {
            befattningsKoder = new ArrayList<>();
        }
        return befattningsKoder;
    }

    public void clearAllBefattningsKoder() {
        if (befattningsKoder != null) {
            befattningsKoder.clear();
        }
        if (befattningar != null) {
            befattningar.clear();
        }
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

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getMedarbetarUppdrag() {
        return medarbetarUppdrag;
    }

    public void setMedarbetarUppdrag(String medarbetarUppdrag) {
        this.medarbetarUppdrag = medarbetarUppdrag;
    }

    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof HoSPersonal)) {
            return false;
        }
        final HoSPersonal that = (HoSPersonal) object;
        return Objects.equals(this.personId, that.personId)
            && Objects.equals(this.fullstandigtNamn, that.fullstandigtNamn)
            && Objects.equals(this.forskrivarKod, that.forskrivarKod)
            && Objects.equals(this.titel, that.titel)
            && Objects.equals(this.medarbetarUppdrag, that.medarbetarUppdrag)
            && Objects.deepEquals(this.befattningar, that.befattningar)
            && Objects.deepEquals(this.specialiteter, that.specialiteter)
            && Objects.equals(this.vardenhet, that.vardenhet);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.personId, this.fullstandigtNamn, this.forskrivarKod,
            this.titel, this.medarbetarUppdrag, this.befattningar, this.specialiteter);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("personId", personId)
            .add("fullstandigtNamn", fullstandigtNamn)
            .add("forskrivarKod", forskrivarKod)
            .add("titel", titel)
            .add("medarbetarUppdrag", medarbetarUppdrag)
            .add("befattningar", befattningar)
            .add("specialiteter", specialiteter)
            .add("vardenhet", vardenhet)
            .toString();
    }
}
