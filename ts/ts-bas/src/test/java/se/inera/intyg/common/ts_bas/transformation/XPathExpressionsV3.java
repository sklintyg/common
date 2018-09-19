/*
 * Copyright (C) 2018 Inera AB (httns3://www.inera.se)
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
 * along with this program.  If not, see <httns3://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.ts_bas.transformation;

import se.inera.intyg.common.ts_parent.transformation.test.BooleanXPathExpression;
import se.inera.intyg.common.ts_parent.transformation.test.DateXPathExpression;
import se.inera.intyg.common.ts_parent.transformation.test.StringXPathExpression;

/**
 * Defines xPath expressions and templates used to create all expressions needed by Transportstyrelsen.
 */
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

    public static final StringXPathExpression PATIENT_PERSONNUMMER_XPATH = new StringXPathExpression("intyg/ns3:patient/ns3:person-id/ns2:extension");

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
            "intyg/ns3:skapadAv/ns3:specialistkompetens/ns2:code");

    public static final StringXPathExpression SKAPAD_AV_BEFATTNING_XPATH = new StringXPathExpression(
            "intyg/ns3:skapadAv/ns3:befattning/ns2:code");

    public static final StringXPathExpression OVRIG_BESKRIVNING_XPATH = new StringXPathExpression(
            "intyg/ns3:kommentar");

    public static final String INTYG_AVSER_TEMPLATE = "intyg/ns3:svar[@id='1']//ns2:code = '%s'";

    public static final String ID_KONTROLL_TEMPLATE = "intyg/ns3:svar[@id='2']//ns2:code = '%s'";

    /**
     * Creates a {@link BooleanXPathExpression} from a string template and arguments.
     *
     * @param template
     *            The string template to use.
     * @param args
     *            The additional arguments to inject in the template.
     *
     * @return A boolean xPath expression.
     */
    public static BooleanXPathExpression booleanXPath(String template, Object... args) {
        return new BooleanXPathExpression(String.format(template, args));
    }

    /**
     * Creates a {@link StringXPathExpression} from a string template and arguments.
     *
     * @param template
     *            The string template to use.
     * @param args
     *            The additional arguments to inject in the template.
     *
     * @return A string xPath expression.
     */
    public static StringXPathExpression stringXPath(String template, Object... args) {
        return new StringXPathExpression(String.format(template, args));
    }

    public static DateXPathExpression dateXPath(String template, String dateFormat, Object... args) {
        return new DateXPathExpression(String.format(template, args), dateFormat);
    }
}
