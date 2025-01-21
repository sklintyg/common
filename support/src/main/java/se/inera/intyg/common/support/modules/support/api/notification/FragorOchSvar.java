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
package se.inera.intyg.common.support.modules.support.api.notification;

public class FragorOchSvar {

    private int antalFragor;

    private int antalSvar;

    private int antalHanteradeFragor;

    private int antalHanteradeSvar;

    public FragorOchSvar(int antalFragor, int antalSvar, int antalHanteradeFragor, int antalHanteradeSvar) {
        super();
        this.antalFragor = antalFragor;
        this.antalSvar = antalSvar;
        this.antalHanteradeFragor = antalHanteradeFragor;
        this.antalHanteradeSvar = antalHanteradeSvar;
    }

    public FragorOchSvar() {
        // Needed for deserialization
    }

    public static FragorOchSvar getEmpty() {
        return new FragorOchSvar(0, 0, 0, 0);
    }

    @Override
    public String toString() {
        return "antalFragor=" + antalFragor + ", antalSvar=" + antalSvar + ", antalHanteradeFragor=" + antalHanteradeFragor
            + ", antalHanteradeSvar=" + antalHanteradeSvar;
    }

    public int getAntalFragor() {
        return antalFragor;
    }

    public int getAntalSvar() {
        return antalSvar;
    }

    public int getAntalHanteradeFragor() {
        return antalHanteradeFragor;
    }

    public int getAntalHanteradeSvar() {
        return antalHanteradeSvar;
    }

    public void setAntalFragor(int antalFragor) {
        this.antalFragor = antalFragor;
    }

    public void setAntalSvar(int antalSvar) {
        this.antalSvar = antalSvar;
    }

    public void setAntalHanteradeFragor(int antalHanteradeFragor) {
        this.antalHanteradeFragor = antalHanteradeFragor;
    }

    public void setAntalHanteradeSvar(int antalHanteradeSvar) {
        this.antalHanteradeSvar = antalHanteradeSvar;
    }

}
