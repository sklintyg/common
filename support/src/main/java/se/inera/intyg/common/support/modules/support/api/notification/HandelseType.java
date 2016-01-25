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

public enum HandelseType {

    /**
     * HAN1: Intygsutkast skapat.
     */
    INTYGSUTKAST_SKAPAT,

    /**
     * HAN2: Intyg signerat.
     */
    INTYGSUTKAST_SIGNERAT,

    /**
     * HAN3: Intyg skickat till FK.
     */
    INTYG_SKICKAT_FK,

    /**
     * HAN4: Intygsutkast raderat.
     */
    INTYGSUTKAST_RADERAT,

    /**
     * HAN5: Intyg makulerat.
     */
    INTYG_MAKULERAT,

    /**
     * HAN6: Ny fråga från FK.
     */
    FRAGA_FRAN_FK,

    /**
     * HAN7: Nytt svar från FK.
     */
    SVAR_FRAN_FK,

    /**
     * HAN8: Ny fråga till FK.
     */
    FRAGA_TILL_FK,

    /**
     * HAN9: Hanterad fråga från FK.
     */
    FRAGA_FRAN_FK_HANTERAD,

    /**
     * HAN10: Hanterat svar från FK.
     */
    SVAR_FRAN_FK_HANTERAD,

    /**
     * HAN11: Intygsutkast ändrat.
     */
    INTYGSUTKAST_ANDRAT;

    public String value() {
        return name();
    }

    public static HandelseType fromValue(String v) {
        return valueOf(v);
    }

}
