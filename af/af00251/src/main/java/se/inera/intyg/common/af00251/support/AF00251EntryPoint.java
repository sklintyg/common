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
package se.inera.intyg.common.af00251.support;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.af_parent.support.AfAbstractModuleEntryPoint;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

@Component("AF00251EntryPoint")
public class AF00251EntryPoint extends AfAbstractModuleEntryPoint {

    public static final String ISSUER_TYPE_ID = "AF00251";
    public static final String MODULE_ID = "af00251";
    public static final String MODULE_NAME = "Läkarintyg för deltagare i arbetsmarknadspolitiska program";
    public static final String MODULE_DESCRIPTION = "Läkarintyg för deltagare i arbetsmarknadspolitiska program"
        + " med aktivitetsstöd, utvecklingsersättning eller etableringsersättning";

    @Override
    public String getModuleId() {
        return MODULE_ID;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String getExternalId() {
        return KvIntygstyp.AF00251.getCodeValue();
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
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "";
            case WEBCERT:
                return "";
            default:
                return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "/web/webjars/af00251/minaintyg/js/module";
            case WEBCERT:
                return "/web/webjars/af00251/webcert/module";
            default:
                return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "/web/webjars/af00251/minaintyg/js/module-deps.json";
            case WEBCERT:
                return "/web/webjars/af00251/webcert/module-deps.json";
            default:
                return null;
        }
    }
}
