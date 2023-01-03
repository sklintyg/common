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
package se.inera.intyg.common.ag7804.support;

import org.springframework.stereotype.Component;

import se.inera.intyg.common.agparent.support.AgAbstractModuleEntryPoint;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

@Component("ag7804EntryPoint")
public class Ag7804EntryPoint extends AgAbstractModuleEntryPoint {

    public static final String ISSUER_TYPE_ID = "AG7804";
    public static final String MODULE_ID = "ag7804";
    public static final String MODULE_NAME = "Läkarintyg om arbetsförmåga – arbetsgivaren";
    public static final String MODULE_DESCRIPTION = "Läkarintyg om arbetsförmåga – arbetsgivaren";

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
    public String getIssuerTypeId() {
        return ISSUER_TYPE_ID;
    }

    @Override
    public String getExternalId() {
        return KvIntygstyp.AG7804.getCodeValue();
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "/web/webjars/ag7804/minaintyg/css/ag7804.css";
            case WEBCERT:
                return "/web/webjars/ag7804/webcert/css/ag7804.css";
            default:
                return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "/web/webjars/ag7804/minaintyg/js/module";
            case WEBCERT:
                return "/web/webjars/ag7804/webcert/module";
            default:
                return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "/web/webjars/ag7804/minaintyg/js/module-deps.json";
            case WEBCERT:
                return "/web/webjars/ag7804/webcert/module-deps.json";
            default:
                return null;
        }
    }
}
