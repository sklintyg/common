/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00213.v1.model.converter;

/**
 * Shared constants between ALL versions of AF200123 intyg.
 */
public final class RespConstants {

    public static final String FUNKTIONSNEDSATTNING_CATEGORY_ID = "funktionsnedsattning";
    public static final String FUNKTIONSNEDSATTNING_SVAR_ID_1 = "1";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_ID_11 = "1.1";
    public static final String FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11 = "harFunktionsnedsattning";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_ID_12 = "1.2";
    public static final String FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12 = "funktionsnedsattning";


    public static final String AKTIVITETSBEGRANSNING_CATEGORY_ID = "aktivitetsbegransning";
    public static final String AKTIVITETSBEGRANSNING_SVAR_ID_2 = "2";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID_21 = "2.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21 = "harAktivitetsbegransning";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID_22 = "2.2";
    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22 = "aktivitetsbegransning";

    public static final String UTREDNING_BEHANDLING_CATEGORY_ID = "utredningBehandling";
    public static final String UTREDNING_BEHANDLING_SVAR_ID_3 = "3";
    public static final String UTREDNING_BEHANDLING_DELSVAR_ID_31 = "3.1";
    public static final String UTREDNING_BEHANDLING_SVAR_JSON_ID_31 = "harUtredningBehandling";
    public static final String UTREDNING_BEHANDLING_DELSVAR_ID_32 = "3.2";
    public static final String UTREDNING_BEHANDLING_SVAR_JSON_ID_32 = "utredningBehandling";

    public static final String ARBETETS_PAVERKAN_CATEGORY_ID = "arbetsPaverkan";
    public static final String ARBETETS_PAVERKAN_SVAR_ID_4 = "4";
    public static final String ARBETETS_PAVERKAN_DELSVAR_ID_41 = "4.1";
    public static final String ARBETETS_PAVERKAN_SVAR_JSON_ID_41 = "harArbetetsPaverkan";
    public static final String ARBETETS_PAVERKAN_DELSVAR_ID_42 = "4.2";
    public static final String ARBETETS_PAVERKAN_SVAR_JSON_ID_42 = "arbetetsPaverkan";


    public static final String OVRIGT_CATEGORY_ID = "ovrigt";
    public static final String OVRIGT_SVAR_ID_5 = "5";
    public static final String OVRIGT_DELSVAR_ID_5 = "5.1";
    public static final String OVRIGT_SVAR_JSON_ID_5 = "ovrigt";


    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String TEXTVERSION_JSON_ID = "textVersion";
    public static final String ID_JSON_ID = "id";
    public static final String SIGNATURE = "signature";

    private RespConstants() {
    }

}
