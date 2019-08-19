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
package se.inera.intyg.common.ts_diabetes.v3.model.validator;

final class ValidationMessages {

    static final String RULE1_MESSAGE = "Om \"Intyget avser behörighet (delsvar)\" har något av värdena IAV1 - IAV8, måste även \"Lämplig att inneha behörighet med hänsyn till aktuella körningar och arbetsformer (Delsvar)\" besvaras.";
    static final String RULE2_MESSAGE = "Datumet måste uttryckas som YYYY.";
    static final String RULE3_MESSAGE = "Om \"Typ av diabetes (Svar)\" är \"ANNAT\" så måste \"Beskrivning annan diabetes (Svar)\" anges.";
    static final String RULE4_MESSAGE = "Minst en av \"Kostbehandling (delsvar)\",\n" +
        "        \"Tablettbehandling (delsvar)\",\n" +
        "        \"Insulinbehandling (delsvar)\" eller\n" +
        "        \"Annan behandling (Delsvar)\" måste besvaras.";
    static final String RULE5_MESSAGE = "Om \"Insulinbehandling (delsvar)\" är besvarat måste även \"Insulinbehandling sedan åt (Delsvar)\" besvaras.";

    static final String RULE6_MESSAGE = "Om \"Insulinbehandling (delsvar)\" är besvarat måste även\n" +
        "        \"Kunskap om lämpliga åtgärder vid hypoglykemi (Delsvar)\",\n" +
        "        \"Förekomst av hypoglykemier med tecken på nedsatt hjärnfunktion som kan innebära en trafiksäkerhetsrisk (Delsvar)\",\n" +
        "        \"Saknar förmåga att känna varningstecken (Delsvar)\",\n" +
        "        \"Diabetessjukdomen är under kontroll avseende hypoglykemi (Delsvar)\",\n" +
        "        \"Förstår risker förknippade med hypoglykemi (Delsvar)\",\n" +
        "        \"Är väl förtrogen med symptomen på hypoglykemi (Delsvar)\",\n" +
        "        \"Egenkontroller av blodsocker (Delsvar)\",\n" +
        "        \"Allvarlig återkommande hypoglykemi i vaket tillstånd under de senaste tre månaderna (Svar)\",\n" +
        "        \"Förekomst av allvarlig återkommande hypoglykemi under det senaste året (Delsvar)\",\n" +
        "        \"Förekomst av allvarlig hypoglykemi i trafiken under det senaste året (Delsvar)\" besvaras.";
    static final String RULE7_MESSAGE = "Datumet måste uttryckas som YYYY.";
    static final String RULE8_MESSAGE = "Om \"Allvarlig återkommande hypoglykemi i vaket tillstånd under de senaste tre månaderna (Svar)\" är besvarat, måste även \"Senaste tidpunkt för allvarlig hypoglykemi (Delsvar)\" besvaras";
    static final String RULE9_MESSAGE = "Om \"Förekomst av allvarlig återkommande hypoglykemi under det senaste året (Delsvar)\" är besvarat, måste även \"Senaste tidpunkt för allvarlig hypoglykemi i vaket tillstånd (Delsvar)\" besvaras";
    static final String RULE10_MESSAGE = "Om \"Förekomst av allvarlig hypoglykemi i trafiken under det senaste året (Delsvar)\" är besvarat, måste även \"Senaste tidpunkt för allvarlig hypoglykemi i trafiken (Delsvar)\" besvaras";
    static final String RULE12_MESSAGE =
        "Om \"Proliferativ retinopati, genomgången laserbehandling av retinopati, signifikant makulaödem, annan ögonsjukdom eller ögonbottenfoto som ger misstanke om sådan ögonsjukdom (Delsvar)\" har värdet false, eller\n"
            +
            "        om \"Ögonbottenfoto saknas (Delsvar)\" har värdet false,\n" +
            "        måste även \"Synskärpa (Svar)\" besvaras.";
    static final String RULE13_MESSAGE =
        "Om frågan \"Intyget avser behörighet (delsvar)\" besvarats med något av valen \"IAV11 - IAV17\"\n" +
            "        OCH frågan \"Binokulärt utan korrektion (Delsvar)\" har besvarats med ett värde som är mindre än 0,5 är\n" +
            "        \"Höger öga med korrektion (Delsvar)\", \"Vänster öga med korrektion (Delsvar)  \" och \"Binokulärt med korrektion (Delsvar) \" obligatoriska att besvara.";
    static final String RULE14_MESSAGE = "Om frågan \"Intyget avser behörighet (delsvar)\" besvarats med något av valen IAV1 - IAV9\n" +
        "        OCH frågan \"Höger öga utan korrektion (Delsvar)\" har besvarats med ett värde som är mindre än 0,8\n" +
        "        OCH frågan \"Vänster öga utan korrektion (Delsvar)\" (DFR 8.2) har besvarats med ett värde som är mindre än 0,8\n" +
        "        är \"Höger öga med korrektion (Delsvar)\", \"Vänster öga med korrektion (Delsvar)  \" och \"Binokulärt med korrektion (Delsvar) \" obligatoriska att besvara.";
    static final String RULE15_MESSAGE = "Om frågan \"Intyget avser behörighet (delsvar)\" besvarats med något av valen IAV1 - IAV9\n" +
        "        OCH frågan \"Höger öga utan korrektion (Delsvar)\" har besvarats med ett värde som är mindre än 0,1\n" +
        "        ELLER frågan \"Vänster öga utan korrektion (Delsvar)\" (DFR 8.2) har besvarats med ett värde som är mindre än 0,1 är\n" +
        "        \"Höger öga med korrektion (Delsvar)\",\n" +
        "        \"Vänster öga med korrektion (Delsvar)  \" och\n" +
        "        \"Binokulärt med korrektion (Delsvar) \" obligatoriska att besvara.";
    static final String RULE16_MESSAGE = "Om frågan \"Tablettbehandling (delsvar)\" besvaras ska frågan\n" +
        "        \"Tablettbehandling ger risk för hypoglykemi (Delsvar)\" visas och vara obligatorisk att besvara.";
    static final String RULE17_MESSAGE = "Om \"Tablettbehandling ger risk för hypoglykemi (Delsvar)\" besvaras måste även\n" +
        "        \"Kunskap om lämpliga åtgärder vid hypoglykemi (Delsvar)\",\n" +
        "        \"Förekomst av hypoglykemier med tecken på nedsatt hjärnfunktion som kan innebära en trafiksäkerhetsrisk (Delsvar)\",\n" +
        "        \"Saknar förmåga att känna varningstecken (Delsvar)\",\n" +
        "        \"Diabetessjukdomen är under kontroll avseende hypoglykemi (Delsvar)\",\n" +
        "        \"Förstår risker förknippade med hypoglykemi (Delsvar)\",\n" +
        "        \"Är väl förtrogen med symptomen på hypoglykemi (Delsvar)\",\n" +
        "        \"Egenkontroller av blodsocker (Delsvar)\",\n" +
        "        \"Allvarlig återkommande hypoglykemi i vaket tillstånd under de senaste tre månaderna (Svar)\",\n" +
        "        \"Förekomst av allvarlig återkommande hypoglykemi under det senaste året (Delsvar)\",\n" +
        "        \"Förekomst av allvarlig hypoglykemi i trafiken under det senaste året (Delsvar)\" besvaras.";
    static final String RULE18_MESSAGE = "Om \"Annan behandling (Delsvar)\" är besvarat måste även även \"Annan behandling beskriven (Delsvar)\" besvaras.";
}
