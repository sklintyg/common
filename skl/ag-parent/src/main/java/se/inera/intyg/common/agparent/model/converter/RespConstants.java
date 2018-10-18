/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.agparent.model.converter;

public final class RespConstants {

    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String TEXTVERSION_JSON_ID = "textVersion";
    public static final String ID_JSON_ID = "id";
    public static final String SIGNATURE = "signature";

    public static final String TYP_AV_SYSSELSATTNING_CODE_SYSTEM = "KV_FKMU_0002";

    // Flytta ner till intyget sen!

    public static final String TYP_AV_SYSSELSATTNING_SVAR_ID_1 = "1";
    public static final String TYP_AV_SYSSELSATTNING_DELSVAR_ID_1 = "1.1";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1 = "sysselsattning";

    public static final String NUVARANDE_ARBETE_SVAR_ID_2 = "2";
    public static final String NUVARANDE_ARBETE_DELSVAR_ID_2 = "2.1";
    public static final String NUVARANDE_ARBETE_SVAR_JSON_ID_2 = "nuvarandeArbete";

    private RespConstants() {
    }

    public static String getJsonPropertyFromFrageId(String frageId) {
        switch (frageId) {
        default:
            throw new IllegalArgumentException("The supplied value of frageId " + frageId + " is not supported.");
        }
    }
}
