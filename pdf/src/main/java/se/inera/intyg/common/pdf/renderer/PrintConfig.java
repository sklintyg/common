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
package se.inera.intyg.common.pdf.renderer;

/**
 * A PrintConfig is a parameter object that is passed to the generic UVRenderer.
 *
 * Contains all intyg- and intygstyp specific data needed for complete rendering.
 */
public class PrintConfig {

    private String intygJsonModel;
    private String upJsModel;

    private String personnummer;
    private byte[] utfardarLogotyp;
    private String intygsNamn;
    private String intygsKod;
    private String infoText;

    private String intygsId;
    private String leftMarginTypText;

    private String summaryHeader;
    private String summaryText;

    private boolean isUtkast;
    private boolean isLockedUtkast;
    private boolean isMakulerad;

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

    public String getInfoText() {
        return infoText;
    }

    public String getIntygsId() {
        return intygsId;
    }

    public String getLeftMarginTypText() {
        return leftMarginTypText;
    }

    public String getSummaryHeader() {
        return summaryHeader;
    }

    public String getSummaryText() {
        return summaryText;
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


    public static final class PrintConfigBuilder {
        private String intygJsonModel;
        private String upJsModel;
        private String personnummer;
        private byte[] utfardarLogotyp;
        private String intygsNamn;
        private String intygsKod;
        private String infoText;
        private String intygsId;
        private String leftMarginTypText;
        private String summaryHeader;
        private String summaryText;
        private boolean isUtkast;
        private boolean isLockedUtkast;
        private boolean isMakulerad;

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

        public PrintConfigBuilder withSummaryHeader(String summaryHeader) {
            this.summaryHeader = summaryHeader;
            return this;
        }

        public PrintConfigBuilder withSummaryText(String summaryText) {
            this.summaryText = summaryText;
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

        public PrintConfig build() {
            PrintConfig printConfig = new PrintConfig();
            printConfig.intygsKod = this.intygsKod;
            printConfig.intygsId = this.intygsId;
            printConfig.isUtkast = this.isUtkast;
            printConfig.infoText = this.infoText;
            printConfig.personnummer = this.personnummer;
            printConfig.leftMarginTypText = this.leftMarginTypText;
            printConfig.upJsModel = this.upJsModel;
            printConfig.intygJsonModel = this.intygJsonModel;
            printConfig.intygsNamn = this.intygsNamn;
            printConfig.utfardarLogotyp = this.utfardarLogotyp;
            printConfig.summaryText = this.summaryText;
            printConfig.summaryHeader = this.summaryHeader;
            printConfig.isLockedUtkast = this.isLockedUtkast;
            printConfig.isMakulerad = this.isMakulerad;
            return printConfig;
        }
    }
}
