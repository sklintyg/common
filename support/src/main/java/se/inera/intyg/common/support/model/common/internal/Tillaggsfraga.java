/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

public class Tillaggsfraga {

    private String id;
    private String svar;

    public static Tillaggsfraga create(String id, String svar) {
        final var tillaggsfraga = new Tillaggsfraga();
        tillaggsfraga.id = id;
        tillaggsfraga.svar = svar;
        return tillaggsfraga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSvar() {
        return svar;
    }

    public void setSvar(String svar) {
        this.svar = svar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tillaggsfraga that = (Tillaggsfraga) o;
        return Objects.equals(id, that.id) && Objects.equals(svar, that.svar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, svar);
    }

    @Override
    public String toString() {
        return "Tillaggsfraga{"
            + "id='" + id + '\''
            + ", svar='" + svar + '\''
            + '}';
    }
}
