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
package se.inera.intyg.common.ts_bas.v7.transformation;

import se.inera.intyg.common.ts_parent.transformation.test.BooleanXPathExpression;
import se.inera.intyg.common.ts_parent.transformation.test.DateXPathExpression;
import se.inera.intyg.common.ts_parent.transformation.test.StringXPathExpression;

public final class XPathExpressionsV3 {

    private XPathExpressionsV3() {
    }

    private static final String TIDSTAMPEL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final StringXPathExpression TYP_AV_INTYG_XPATH = new StringXPathExpression("intyg/ns3:typ/ns2:code");

    public static final StringXPathExpression TS_VERSION_XPATH = new StringXPathExpression("intyg/ns3:version");

    public static final StringXPathExpression PATIENT_FORNAMN_XPATH = new StringXPathExpression("intyg/ns3:patient/ns3:fornamn");

    public static final StringXPathExpression PATIENT_EFTERNAMN_XPATH = new StringXPathExpression("intyg/ns3:patient/ns3:efternamn");

    public static final StringXPathExpression PATIENT_POSTADRESS_XPATH = new StringXPathExpression("intyg/ns3:patient/ns3:postadress");

    public static final StringXPathExpression PATIENT_POSTNUMMER_XPATH = new StringXPathExpression("intyg/ns3:patient/ns3:postnummer");

    public static final StringXPathExpression PATIENT_POSTORT_XPATH = new StringXPathExpression("intyg/ns3:patient/ns3:postort");

    public static final StringXPathExpression PATIENT_PERSONNUMMER_XPATH = new StringXPathExpression(
        "intyg/ns3:patient/ns3:person-id/ns2:extension");

    public static final DateXPathExpression SIGNERINGTIDPUNKT_XPATH = new DateXPathExpression(
        "intyg/ns3:signeringstidpunkt", TIDSTAMPEL_FORMAT);

    public static final StringXPathExpression ENHET_VARDINRATTNINGENS_NAMN_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:enhet/ns3:enhetsnamn");

    public static final StringXPathExpression ENHET_ID_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:enhet/ns3:enhets-id/ns2:extension");

    public static final StringXPathExpression ENHET_POSTADRESS_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:enhet/ns3:postadress");

    public static final StringXPathExpression ENHET_POSTORT_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:enhet/ns3:postort");

    public static final StringXPathExpression ENHET_POSTNUMMER_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:enhet/ns3:postnummer");

    public static final StringXPathExpression ENHET_TELEFONNUMMER_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:enhet/ns3:telefonnummer");

    public static final StringXPathExpression VARDGIVARE_ID_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:enhet/ns3:vardgivare/ns3:vardgivare-id/ns2:extension");

    public static final StringXPathExpression VARDGIVARE_NAMN_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:enhet/ns3:vardgivare/ns3:vardgivarnamn");

    public static final StringXPathExpression SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:fullstandigtNamn");

    public static final StringXPathExpression SKAPAD_AV_HSAID_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:personal-id/ns2:extension");

    public static final StringXPathExpression SKAPAD_AV_SPECIALISTKOMPETENS_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:specialistkompetens/ns2:displayName");

    public static final StringXPathExpression SKAPAD_AV_BEFATTNING_XPATH = new StringXPathExpression(
        "intyg/ns3:skapadAv/ns3:befattning/ns2:code");

    public static final StringXPathExpression SYNFUNKTION_DEFEKT_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='3']/ns3:delsvar[@id='3.1']");

    public static final StringXPathExpression SYNFUNKTION_NATTBLINDHET_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='4']/ns3:delsvar[@id='4.1']");

    public static final StringXPathExpression SYNFUNKTION_PROGRESSIV_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='5']/ns3:delsvar[@id='5.1']");

    public static final StringXPathExpression SYNFUNKTION_DIPLOPI_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='6']/ns3:delsvar[@id='6.1']");

    public static final StringXPathExpression SYNFUNKTION_NYSTAGMUS_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='7']/ns3:delsvar[@id='7.1']");

    public static final StringXPathExpression SYNFUNKTION_UTAN_KORREKTION_HOGER_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='8']/ns3:delsvar[@id='8.1']");

    public static final StringXPathExpression SYNFUNKTION_UTAN_KORREKTION_VANSTER_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='8']/ns3:delsvar[@id='8.2']");

    public static final StringXPathExpression SYNFUNKTION_UTAN_KORREKTION_BINOKULART_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='8']/ns3:delsvar[@id='8.3']");

    public static final StringXPathExpression SYNFUNKTION_MED_KORREKTION_HOGER_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='8']/ns3:delsvar[@id='8.4']");

    public static final StringXPathExpression SYNFUNKTION_MED_KORREKTION_VANSTER_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='8']/ns3:delsvar[@id='8.5']");

    public static final StringXPathExpression SYNFUNKTION_MED_KORREKTION_BINOKULART_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='8']/ns3:delsvar[@id='8.6']");

    public static final StringXPathExpression SYNFUNKTION_KONTAKTLINS_HOGER_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='8']/ns3:delsvar[@id='8.7']");

    public static final StringXPathExpression SYNFUNKTION_KONTAKTLINS_VANSTER_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='8']/ns3:delsvar[@id='8.8']");

    public static final StringXPathExpression HORSEL_BALANS_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='10']/ns3:delsvar[@id='10.1']");

    public static final StringXPathExpression RORELSEORGAN_SJUKDOM_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='12']/ns3:delsvar[@id='12.1']");

    public static final StringXPathExpression RORELSEORGAN_BESKRICNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='12']/ns3:delsvar[@id='12.2']");

    public static final StringXPathExpression HJARTKARL_RISK_FUNKTION_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='14']/ns3:delsvar[@id='14.1']");

    public static final StringXPathExpression HJARTKARL_SKADA_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='15']/ns3:delsvar[@id='15.1']");

    public static final StringXPathExpression HJARTKARL_RISK_STROKE_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='16']/ns3:delsvar[@id='16.1']");

    public static final StringXPathExpression HJARTKARL_RISK_STROKE_BESKRIVNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='16']/ns3:delsvar[@id='16.2']");

    public static final StringXPathExpression DIABETES_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='17']/ns3:delsvar[@id='17.1']");

    public static final StringXPathExpression DIABETES_TYPE_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='18']/ns3:delsvar[@id='18.1']/ns2:cv/ns2:code");

    public static final StringXPathExpression DIABETES_KOST_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='19']/ns3:delsvar[@id='19.1']");

    public static final StringXPathExpression DIABETES_TABLETTER_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='19']/ns3:delsvar[@id='19.2']");

    public static final StringXPathExpression DIABETES_INSULIN_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='19']/ns3:delsvar[@id='19.3']");

    public static final StringXPathExpression NEUROLOGISKA_SJUKDOMAR_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='20']/ns3:delsvar[@id='20.1']");

    public static final StringXPathExpression MEDVETANDESTORNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='21']/ns3:delsvar[@id='21.1']");

    public static final StringXPathExpression MEDVETANDESTORNING_BESKRIVNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='21']/ns3:delsvar[@id='21.2']");

    public static final StringXPathExpression NJURSJUKDOM_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='22']/ns3:delsvar[@id='22.1']");

    public static final StringXPathExpression DEMENS_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='23']/ns3:delsvar[@id='23.1']");

    public static final StringXPathExpression SOMN_OCH_VAKENHETSSTORNINGAR_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='24']/ns3:delsvar[@id='24.1']");

    public static final StringXPathExpression ALKOHOL_TECKEN_MISSBRUK_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='25']/ns3:delsvar[@id='25.1']");

    public static final StringXPathExpression ALKOHOL_HAR_VARDINSATS_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='25']/ns3:delsvar[@id='25.2']");

    public static final StringXPathExpression ALKOHOL_HAR_VARDINSATS_PROVTAGNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='25']/ns3:delsvar[@id='25.3']");

    public static final StringXPathExpression ALKOHOL_ORDINERAT_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='26']/ns3:delsvar[@id='26.1']");

    public static final StringXPathExpression ALKOHOL_ORDINERAT_LAKEMEDEL_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='26']/ns3:delsvar[@id='26.2']");

    public static final StringXPathExpression PSYKISKA_SJUKDOMAR_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='27']/ns3:delsvar[@id='27.1']");

    public static final StringXPathExpression UTVECKLINGSSTORNING_PSYKISK_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='28']/ns3:delsvar[@id='28.1']");

    public static final StringXPathExpression UTVECKLINGSSTORNING_ANDRA_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='29']/ns3:delsvar[@id='29.1']");

    public static final StringXPathExpression SJUKHUSVARD_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='30']/ns3:delsvar[@id='30.1']");

    public static final StringXPathExpression SJUKHUSVARD_DATUM_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='30']/ns3:delsvar[@id='30.2']");

    public static final StringXPathExpression SJUKHUSVARD_INRATTNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='30']/ns3:delsvar[@id='30.3']");

    public static final StringXPathExpression SJUKHUSVARD_ANLEDNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='30']/ns3:delsvar[@id='30.4']");

    public static final StringXPathExpression OVRIG_MEDICIN_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='31']/ns3:delsvar[@id='31.1']");

    public static final StringXPathExpression OVRIG_MEDICIN_BESKRIVNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='31']/ns3:delsvar[@id='31.2']");

    public static final StringXPathExpression OVRIG_BESKRIVNING_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='32']/ns3:delsvar[@id='32.1']");

    public static final StringXPathExpression BEDOMNING_SPECIALIST_XPATH = new StringXPathExpression(
        "intyg/ns3:svar[@id='34']/ns3:delsvar[@id='34.1']");

    public static final String INTYG_AVSER_TEMPLATE = "intyg/ns3:svar[@id='1']//ns2:code = '%s'";

    public static final String BEDOMNING_BEHORIGHET_TEMPLATE = "intyg/ns3:svar[@id='33']//ns2:code = '%s'";

    public static final String ID_KONTROLL_TEMPLATE = "intyg/ns3:svar[@id='2']//ns2:code = '%s'";

    /**
     * Creates a {@link BooleanXPathExpression} from a string template and arguments.
     *
     * @param template The string template to use.
     * @param args The additional arguments to inject in the template.
     * @return A boolean xPath expression.
     */
    public static BooleanXPathExpression booleanXPath(String template, Object... args) {
        return new BooleanXPathExpression(String.format(template, args));
    }

    /**
     * Creates a {@link StringXPathExpression} from a string template and arguments.
     *
     * @param template The string template to use.
     * @param args The additional arguments to inject in the template.
     * @return A string xPath expression.
     */
    public static StringXPathExpression stringXPath(String template, Object... args) {
        return new StringXPathExpression(String.format(template, args));
    }

    public static DateXPathExpression dateXPath(String template, String dateFormat, Object... args) {
        return new DateXPathExpression(String.format(template, args), dateFormat);
    }
}
