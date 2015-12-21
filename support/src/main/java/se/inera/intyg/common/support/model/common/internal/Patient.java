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

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

import java.util.Objects;

public class Patient {

    private Personnummer personId;

    private String fullstandigtNamn;

    private String fornamn;

    private String mellannamn;

    private String efternamn;

    private String postadress;

    private String postnummer;

    private String postort;

    public Patient() {

    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Patient that = (Patient) object;
        return Objects.equals(this.personId, that.personId) &&
                Objects.equals(this.fullstandigtNamn, that.fullstandigtNamn) &&
                Objects.equals(this.fornamn, that.fornamn) &&
                Objects.equals(this.mellannamn, that.mellannamn) &&
                Objects.equals(this.efternamn, that.efternamn) &&
                Objects.equals(this.postadress, that.postadress) &&
                Objects.equals(this.postnummer, that.postnummer) &&
                Objects.equals(this.postort, that.postort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.personId, this.fullstandigtNamn, this.fornamn, this.mellannamn, this.efternamn,
                this.postadress, this.postnummer, this.postort);
    }

    public boolean isSamordningsNummer() {
        if (personId == null) {
            throw new IllegalStateException("No person id has been set");
        }
        return personId.isSamordningsNummer();
    }

    public Personnummer getPersonId() {
        return personId;
    }

    public void setPersonId(Personnummer personId) {
        this.personId = personId;
    }

    public String getFullstandigtNamn() {
        return fullstandigtNamn;
    }

    public void setFullstandigtNamn(String fullstandigtNamn) {
        this.fullstandigtNamn = fullstandigtNamn;
    }

    public String getFornamn() {
        return fornamn;
    }

    public void setFornamn(String fornamn) {
        this.fornamn = fornamn;
    }

    public String getMellannamn() {
        return mellannamn;
    }

    public void setMellannamn(String mellannamn) {
        this.mellannamn = mellannamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public void setEfternamn(String efternamn) {
        this.efternamn = efternamn;
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

}
