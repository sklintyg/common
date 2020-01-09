/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
import java.util.Objects;

public class Vardenhet {

    private String enhetsid;

    private String enhetsnamn;

    private String postadress;

    private String postnummer;

    private String postort;

    private String telefonnummer;

    private String epost;

    private Vardgivare vardgivare;

    private String arbetsplatsKod;

    public String getEnhetsid() {
        return enhetsid;
    }

    public void setEnhetsid(String enhetsid) {
        this.enhetsid = enhetsid;
    }

    public String getEnhetsnamn() {
        return enhetsnamn;
    }

    public void setEnhetsnamn(String enhetsnamn) {
        this.enhetsnamn = enhetsnamn;
    }

    public String getPostadress() {
        return postadress;
    }

    public void setPostadress(String postadress) {
        this.postadress = postadress;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(String postnummer) {
        this.postnummer = postnummer;
    }

    public String getPostort() {
        return postort;
    }

    public void setPostort(String postort) {
        this.postort = postort;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public Vardgivare getVardgivare() {
        return vardgivare;
    }

    public void setVardgivare(Vardgivare vardgivare) {
        this.vardgivare = vardgivare;
    }

    public String getArbetsplatsKod() {
        return arbetsplatsKod;
    }

    public void setArbetsplatsKod(String arbetsplatsKod) {
        this.arbetsplatsKod = arbetsplatsKod;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Vardenhet)) {
            return false;
        }
        final Vardenhet that = (Vardenhet) object;
        return Objects.equals(this.enhetsid, that.enhetsid)
            && Objects.equals(this.enhetsnamn, that.enhetsnamn)
            && Objects.equals(this.postadress, that.postadress)
            && Objects.equals(this.postnummer, that.postnummer)
            && Objects.equals(this.postort, that.postort)
            && Objects.equals(this.telefonnummer, that.telefonnummer)
            && Objects.equals(this.epost, that.epost)
            && Objects.equals(this.vardgivare, that.vardgivare)
            && Objects.equals(this.arbetsplatsKod, that.arbetsplatsKod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enhetsid, this.enhetsnamn, this.postadress, this.postnummer, this.postort,
            this.telefonnummer, this.epost, this.vardgivare, this.arbetsplatsKod);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("enhetsid", enhetsid)
            .add("enhetsnamn", enhetsnamn)
            .add("postadress", postadress)
            .add("postnummer", postnummer)
            .add("postort", postort)
            .add("telefonnummer", telefonnummer)
            .add("epost", epost)
            .add("vardgivare", vardgivare)
            .add("arbetsplatsKod", arbetsplatsKod)
            .toString();
    }
}
