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

import com.google.common.base.MoreObjects;
import java.util.Objects;

public class Vardgivare {

    private String vardgivarid;

    private String vardgivarnamn;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof  Vardgivare)) {
            return false;
        }
        final Vardgivare that = (Vardgivare) object;
        return Objects.equals(this.vardgivarid, that.vardgivarid)
                && Objects.equals(this.vardgivarnamn, that.vardgivarnamn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.vardgivarid, this.vardgivarnamn);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("vardgivarid", vardgivarid)
                .add("vardgivarnamn", vardgivarnamn)
                .toString();
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

    public void setVardgivarnamn(String vardgivarnamn) {
        this.vardgivarnamn = vardgivarnamn;
    }



}
