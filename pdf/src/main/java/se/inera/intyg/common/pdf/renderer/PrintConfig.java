/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.pdf.renderer;

import java.util.Map;
import se.inera.intyg.common.pdf.model.Summary;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

/**
 * A PrintConfig is a parameter object that is passed to the generic UVRenderer.
 *
 * Contains all intyg- and intygstyp specific data needed for complete rendering.
 */
public class PrintConfig {

    public static final String UTSK001_HEADER = "Skicka intyg till mottagare";
    public static final String UTSK001_BODY = "Du som fått ett intyg utfärdat kan snabbt och säkert hantera detta "
        + "intyg i e-tjänsten Mina intyg. Där kan du till exempel skicka intyget till mottagaren."
        + "\n\n"
        + "Du når Mina intyg via 1177 Vårdguidens webbplats 1177.se eller via minaintyg.se. "
        + "Det enda du behöver för att logga in är e-legitimation.";

    private String intygJsonModel;
    private String upJsModel;

    private String personnummer;
    private byte[] utfardarLogotyp;
    private String intygsNamn;
    private String intygsKod;
    private String intygsVersion;
    private String infoText;

    private String intygsId;
    private String leftMarginTypText;

    private Summary summary;

    private boolean isUtkast;
    private boolean isLockedUtkast;
    private boolean isMakulerad;
    private boolean showSignBox;
    private boolean showSignatureLine;
    private String footerAppName;

    //Defines a override (String) value to be rendered instead of actual modelProp.
    // This is used when creating a employer print where certain values are not to be included
    private Map<String, String> modelPropReplacements;

    private ApplicationOrigin applicationOrigin;

    public String getIntygJsonModel() {
        return intygJsonModel;
    }

    public String getUpJsModel() {
        return upJsModel;
    }

    public String getPersonnummer() {
        return personnummer;
    }

    public byte[] getUtfardarLogotyp() {
        return utfardarLogotyp;
    }

    public String getIntygsNamn() {
        return intygsNamn;
    }

    public String getIntygsKod() {
        return intygsKod;
    }

    public String getIntygsVersion() {
        return intygsVersion;
    }

    public String getInfoText() {
        return infoText;
    }

    public String getIntygsId() {
        return intygsId;
    }

    public String getLeftMarginTypText() {
        return leftMarginTypText;
    }

    public boolean hasSummaryPage() {
        return summary != null && !summary.isEmpty();
    }

    public Summary getSummary() {
        return summary;
    }

    public boolean isUtkast() {
        return isUtkast;
    }

    public boolean isLockedUtkast() {
        return isLockedUtkast;
    }

    public boolean isMakulerad() {
        return isMakulerad;
    }

    public boolean showSignBox() {
        return showSignBox;
    }

    public boolean showSignatureLine() {
        return showSignatureLine;
    }

    public Map<String, String> getModelPropReplacements() {
        return modelPropReplacements;
    }

    public ApplicationOrigin getApplicationOrigin() {
        return applicationOrigin;
    }

    public String getFooterAppName() {
        return footerAppName;
    }

    public static final class PrintConfigBuilder {

        private String intygJsonModel;
        private String upJsModel;
        private String personnummer;
        private byte[] utfardarLogotyp;
        private String intygsNamn;
        private String intygsKod;
        private String intygsVersion;
        private String infoText;
        private String intygsId;
        private String leftMarginTypText;
        private Summary summary;
        private boolean isUtkast;
        private boolean isLockedUtkast;
        private boolean isMakulerad;
        private boolean showSignBox;
        private boolean showSignatureLine;
        private ApplicationOrigin applicationOrigin;
        private Map<String, String> modelPropReplacements;
        private String footerAppName;

        private PrintConfigBuilder() {
        }

        public static PrintConfigBuilder aPrintConfig() {
            return new PrintConfigBuilder();
        }

        public PrintConfigBuilder withIntygJsonModel(String intygJsonModel) {
            this.intygJsonModel = intygJsonModel;
            return this;
        }

        public PrintConfigBuilder withUpJsModel(String upJsModel) {
            this.upJsModel = upJsModel;
            return this;
        }

        public PrintConfigBuilder withPersonnummer(String personnummer) {
            this.personnummer = personnummer;
            return this;
        }

        public PrintConfigBuilder withUtfardarLogotyp(byte[] utfardarLogotyp) {
            this.utfardarLogotyp = utfardarLogotyp;
            return this;
        }

        public PrintConfigBuilder withIntygsNamn(String intygsNamn) {
            this.intygsNamn = intygsNamn;
            return this;
        }

        public PrintConfigBuilder withIntygsKod(String intygsKod) {
            this.intygsKod = intygsKod;
            return this;
        }

        public PrintConfigBuilder withIntygsVersion(String intygsVersion) {
            this.intygsVersion = intygsVersion;
            return this;
        }

        public PrintConfigBuilder withInfoText(String infoText) {
            this.infoText = infoText;
            return this;
        }

        public PrintConfigBuilder withIntygsId(String intygsId) {
            this.intygsId = intygsId;
            return this;
        }

        public PrintConfigBuilder withLeftMarginTypText(String leftMarginTypText) {
            this.leftMarginTypText = leftMarginTypText;
            return this;
        }

        public PrintConfigBuilder withSummary(Summary summary) {
            this.summary = summary;
            return this;
        }

        public PrintConfigBuilder withIsUtkast(boolean isUtkast) {
            this.isUtkast = isUtkast;
            return this;
        }

        public PrintConfigBuilder withIsLockedUtkast(boolean isLockedUtkast) {
            this.isLockedUtkast = isLockedUtkast;
            return this;
        }

        public PrintConfigBuilder withIsMakulerad(boolean isMakulerad) {
            this.isMakulerad = isMakulerad;
            return this;
        }

        public PrintConfigBuilder withApplicationOrigin(ApplicationOrigin applicationOrigin) {
            this.applicationOrigin = applicationOrigin;
            return this;
        }

        public PrintConfigBuilder withSignBox(boolean showSignBox) {
            this.showSignBox = showSignBox;
            return this;
        }

        public PrintConfigBuilder withSignatureLine(boolean showSignatureLine) {
            this.showSignatureLine = showSignatureLine;
            return this;
        }

        public PrintConfigBuilder withModelPropReplacements(Map<String, String> modelPropReplacements) {
            this.modelPropReplacements = modelPropReplacements;
            return this;
        }

        public PrintConfigBuilder withFooterAppName(String footerAppName) {
            this.footerAppName = footerAppName;
            return this;
        }

        public PrintConfig build() {
            PrintConfig printConfig = new PrintConfig();
            printConfig.isUtkast = this.isUtkast;
            printConfig.utfardarLogotyp = this.utfardarLogotyp;
            printConfig.personnummer = this.personnummer;
            printConfig.leftMarginTypText = this.leftMarginTypText;
            printConfig.intygJsonModel = this.intygJsonModel;
            printConfig.isMakulerad = this.isMakulerad;
            printConfig.upJsModel = this.upJsModel;
            printConfig.infoText = this.infoText;
            printConfig.intygsKod = this.intygsKod;
            printConfig.intygsVersion = this.intygsVersion;
            printConfig.summary = this.summary;
            printConfig.applicationOrigin = this.applicationOrigin;
            printConfig.intygsNamn = this.intygsNamn;
            printConfig.intygsId = this.intygsId;
            printConfig.isLockedUtkast = this.isLockedUtkast;
            printConfig.showSignBox = this.showSignBox;
            printConfig.showSignatureLine = this.showSignatureLine;
            printConfig.modelPropReplacements = this.modelPropReplacements;
            printConfig.footerAppName = this.footerAppName;
            return printConfig;
        }
    }
}
