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
package se.inera.intyg.common.support.model.common.internal;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import se.inera.intyg.common.support.validate.SamordningsnummerValidator;
import se.inera.intyg.schemas.contract.Personnummer;

public class Patient {

    private Personnummer personId;

    private String fullstandigtNamn;

    private String fornamn;

    private String mellannamn;

    private String efternamn;

    private String postadress;

    private String postnummer;

    private String postort;

    private boolean sekretessmarkering;

    private boolean avliden;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Patient)) {
            return false;
        }
        final Patient that = (Patient) object;
        return Objects.equals(this.personId, that.personId)
            && Objects.equals(this.fullstandigtNamn, that.fullstandigtNamn)
            && Objects.equals(this.fornamn, that.fornamn)
            && Objects.equals(this.mellannamn, that.mellannamn)
            && Objects.equals(this.efternamn, that.efternamn)
            && Objects.equals(this.postadress, that.postadress)
            && Objects.equals(this.postnummer, that.postnummer)
            && Objects.equals(this.postort, that.postort)
            && Objects.equals(this.sekretessmarkering, that.sekretessmarkering)
            && Objects.equals(this.avliden, that.avliden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.personId, this.fullstandigtNamn, this.fornamn, this.mellannamn, this.efternamn,
            this.postadress, this.postnummer, this.postort, this.sekretessmarkering, this.avliden);
    }

    public boolean isSamordningsNummer() {
        if (personId == null) {
            throw new IllegalStateException("No person id has been set");
        }
        return SamordningsnummerValidator.isSamordningsNummer(Optional.of(personId));
    }

    @JsonIgnore
    public boolean isCompleteAddressProvided() {
        return !Strings.isNullOrEmpty(this.postadress)
            && !Strings.isNullOrEmpty(this.postort)
            && !Strings.isNullOrEmpty(this.postnummer);
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

    public boolean isSekretessmarkering() {
        return sekretessmarkering;
    }

    public void setSekretessmarkering(boolean sekretessmarkering) {
        this.sekretessmarkering = sekretessmarkering;
    }

    public boolean isAvliden() {
        return avliden;
    }

    public void setAvliden(boolean avliden) {
        this.avliden = avliden;
    }

}
