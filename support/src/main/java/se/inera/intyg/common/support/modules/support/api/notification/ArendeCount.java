/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.support.api.notification;

public class ArendeCount {

    private int totalt;

    private int ejBesvarade;

    private int besvarade;

    private int hanterade;

    public ArendeCount(int totalt, int ejBesvarade, int besvarade, int hanterade) {
        this.totalt = totalt;
        this.ejBesvarade = ejBesvarade;
        this.besvarade = besvarade;
        this.hanterade = hanterade;
    }

    public ArendeCount() {
        // Needed for deserialization
    }

    public static ArendeCount getEmpty() {
        return new ArendeCount(0, 0, 0, 0);
    }

    @Override
    public String toString() {
        return "Arenden [totalt=" + totalt + ", ejBesvarade=" + ejBesvarade + ", besvarade=" + besvarade + ", hanterade=" + hanterade + "]";
    }

    public int getTotalt() {
        return totalt;
    }

    public int getEjBesvarade() {
        return ejBesvarade;
    }

    public int getBesvarade() {
        return besvarade;
    }

    public int getHanterade() {
        return hanterade;
    }
}
