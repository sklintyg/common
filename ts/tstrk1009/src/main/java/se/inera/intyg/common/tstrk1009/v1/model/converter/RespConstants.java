/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.model.converter;

public final class RespConstants {

    public static final String CATEGORY_IDENTITET = "identitet"; //KAT_1
    public static final String CATEGORY_ANMALAN = "anmalan"; //KAT_2
    public static final String CATEGORY_MEDICINSKAFORHALLANDEN = "medicinskaforhallanden"; //KAT_3
    public static final String CATEGORY_BEDOMNING = "bedomning"; //KAT_4
    public static final String CATEGORY_INFORMATIONOMBESLUT = "informationombeslut"; //KAT_5

    public static final String KV_ID_KONTROLL_KODSYSTEM = "e7cc8f30-a353-4c42-b17a-a189b6876647";
    public static final String KV_KORKORTSOLAMPLIGHET_KODSYSTEM = "1.2.752.129.5.1.4";
    public static final String KV_KORKORTSBEHORIGHET_KODSYSTEM = "e889fa20-1dee-4f79-8b37-03853e75a9f8";

    public static final String IDENTITET_STYRKT_GENOM_SVAR_ID = "2";
    public static final String IDENTITET_STYRKT_GENOM_DELSVAR_ID = "2.1";
    public static final String IDENTITET_STYRKT_GENOM_JSON_ID = "identitetStyrktGenom";

    public static final String ANMALAN_AVSER_SVAR_ID = "46";
    public static final String ANMALAN_AVSER_DELSVAR_ID = "46.1";
    public static final String ANMALAN_AVSER_JSON_ID = "anmalanAvser";

    public static final String MEDICINSKA_FORHALLANDEN_SVAR_ID = "47";
    public static final String MEDICINSKA_FORHALLANDEN_DELSVAR_ID = "47.1";
    public static final String MEDICINSKA_FORHALLANDEN_JSON_ID = "medicinskaForhallanden";

    public static final String SENASTE_UNDERSOKNINGSDATUM_SVAR_ID = "48";
    public static final String SENASTE_UNDERSOKNINGSDATUM_DELSVAR_ID = "48.1";
    public static final String SENASTE_UNDERSOKNINGSDATUM_JSON_ID = "senasteUndersokningsdatum";

    public static final String INTYGET_AVSER_BEHORIGHET_SVAR_ID = "1";
    public static final String INTYGET_AVSER_BEHORIGHET_DELSVAR_ID = "1.1";
    public static final String INTYGET_AVSER_BEHORIGHET_JSON_ID = "intygetAvserBehorigheter";

    public static final String INFORMATION_OM_TS_BESLUT_ONSKAS_SVAR_ID = "49";
    public static final String INFORMATION_OM_TS_BESLUT_ONSKAS_DELSVAR_ID = "49.1";
    public static final String INFORMATION_OM_TS_BESLUT_ONSKAS_JSON_ID = "informationOmTsBeslutOnskas";

    private RespConstants() {
    }
}
