/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.support.feature;

public enum ModuleFeature {

    HANTERA_FRAGOR("hanteraFragor"),
    HANTERA_INTYGSUTKAST("hanteraIntygsutkast"),
    KOPIERA_INTYG("kopieraIntyg"),
    MAKULERA_INTYG("makuleraIntyg"),
    MAKULERA_INTYG_KRAVER_ANLEDNING("makuleraIntygKraverAnledning"),
    SKAPA_NYFRAGA("skapaNyFraga"),
    SKICKA_INTYG("skickaIntyg"),
    SIGNERA_SKICKA_DIREKT("signeraSkickaDirekt"),
    UTSKRIFT("utskrift"),
    ARBETSGIVARUTSKRIFT("arbetsgivarUtskrift"),
    SRS("srs"),
    WARN_ON_PREVIOUS("varnaOmTidigareIntyg"),
    HANTERA_INTYGSUTKAST_AVLIDEN("hanteraIntygsutkastAvliden");

    private final String name;

    ModuleFeature(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
