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
package se.inera.intyg.common.sos_doi.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.sos_doi.rest.DodsorsaksintygModuleApi;
import se.inera.intyg.common.sos_parent.support.SosAbstractModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeaturesFactory;

public class DodsorsaksintygModuleEntryPoint extends SosAbstractModuleEntryPoint {

    public static final String SCHEMATRON_FILE = "doi.sch";
    public static final String MODULE_ID = "DOI";

    private static final String MODULE_NAME = "Dödsorsaksintyg";
    private static final String MODULE_DESCRIPTION = "Dödsorsaksintyg";

    @Autowired
    private DodsorsaksintygModuleApi moduleApi;

    @Override
    public String getModuleId() {
        return MODULE_ID;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String getModuleDescription() {
        return MODULE_DESCRIPTION;
    }

    @Override
    public String getDetailedModuleDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getExternalId() {
        return MODULE_ID;
    }

    @Override
    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures(MODULE_ID, "doi-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        return "/web/webjars/sos_doi/webcert/css/sos_doi.css";
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        return "/web/webjars/sos_doi/webcert/module";
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        return "/web/webjars/sos_doi/webcert/module-deps.json";
    }
}
