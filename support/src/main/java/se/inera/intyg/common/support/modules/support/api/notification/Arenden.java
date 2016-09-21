/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

public class Arenden {

    private int totalt;

    private int ejBesvarade;

    private int besvarade;

    private int hanterade;

    public Arenden(int totalt, int ejBesvarade, int besvarade, int hanterade) {
        super();
        this.totalt = totalt;
        this.ejBesvarade = ejBesvarade;
        this.besvarade = besvarade;
        this.hanterade = hanterade;
    }

    public Arenden() {
    }

    public static Arenden getEmpty() {
        return new Arenden(0, 0, 0, 0);
    }

    @Override
    public String toString() {
        return "Arenden [totalt=" + totalt + ", ejBesvarade=" + ejBesvarade + ", besvarade=" + besvarade + ", hanterade=" + hanterade + "]";
    }

    public int getTotalt() {
        return totalt;
    }

    public void setTotalt(int totalt) {
        this.totalt = totalt;
    }

    public int getEjBesvarade() {
        return ejBesvarade;
    }

    public void setEjBesvarade(int ejBesvarade) {
        this.ejBesvarade = ejBesvarade;
    }

    public int getBesvarade() {
        return besvarade;
    }

    public void setBesvarade(int besvarade) {
        this.besvarade = besvarade;
    }

    public int getHanterade() {
        return hanterade;
    }

    public void setHanterade(int hanterade) {
        this.hanterade = hanterade;
    }
}
