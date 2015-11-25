/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.support.model.common.internal;

import java.util.Objects;

public class Vardgivare {

    private String vardgivarid;

    private String vardgivarnamn;

    public Vardgivare() {

    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Vardgivare that = (Vardgivare) object;
        return Objects.equals(this.vardgivarid, that.vardgivarid) &&
                Objects.equals(this.vardgivarnamn, that.vardgivarnamn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.vardgivarid, this.vardgivarnamn);
    }

    public String getVardgivarid() {
        return vardgivarid;
    }

    public void setVardgivarid(String vardgivarid) {
        this.vardgivarid = vardgivarid;
    }

    public String getVardgivarnamn() {
        return vardgivarnamn;
    }

    public void setVardgivarnamn(String vardgivarNamn) {
        this.vardgivarnamn = vardgivarNamn;
    }

}
