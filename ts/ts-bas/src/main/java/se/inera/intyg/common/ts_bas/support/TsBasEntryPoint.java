/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.support;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;

@Component("TsBasEntryPoint")
public class TsBasEntryPoint implements ModuleEntryPoint {

    public static final String MODULE_ID = "ts-bas";
    public static final String MODULE_NAME = "Transportstyrelsens läkarintyg högre körkortsbehörighet";
    public static final String ISSUER_MODULE_NAME = "Transportstyrelsens läkarintyg";
    public static final String SCHEMATRON_FILE_V6 = "tstrk1007.v6.sch";
    public static final String SCHEMATRON_FILE_V7 = "tstrk1007.v7.sch";
    public static final String KV_UTLATANDETYP_INTYG_CODE = "TSTRK1007";
    // CHECKSTYLE:OFF LineLength
    private static final String DEFAULT_RECIPIENT_ID = "TRANSP";
    public static final String DESCRIPTION_TEXT_KEY = "FRM_1.RBK";
    public static final String DETAILED_DESCRIPTION_TEXT_KEY = "FRM_2.RBK";
    private static final String MODULE_DESCRIPTION = "Läkarintyg - avseende högre körkortsbehörigheter eller taxiförarlegitimation - på begäran av Transportstyrelsen";
    // CHECKSTYLE:ON LineLength

    // Depending on context, an IntygTextRepository may not be available (e.g Intygstjansten)
    @Autowired(required = false)
    private Optional<IntygTextsRepository> repo;

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
        if (repo.isPresent()) {
            final String latestVersion = repo.get().getLatestVersion(getModuleId());
            final IntygTexts texts = repo.get().getTexts(getModuleId(), latestVersion);
            if (texts != null) {
                return texts.getTexter().get(DESCRIPTION_TEXT_KEY);
            }
        }
        return null;
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "";
            case WEBCERT:
                return "/web/webjars/ts-bas/webcert/css/ts-bas.css";
            default:
                return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "/web/webjars/ts-bas/minaintyg/js/module";
            case WEBCERT:
                return "/web/webjars/ts-bas/webcert/module";
            default:
                return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return "/web/webjars/ts-bas/minaintyg/js/module-deps.json";
            case WEBCERT:
                return "/web/webjars/ts-bas/webcert/module-deps.json";
            default:
                return null;
        }
    }

    @Override
    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

    @Override
    public String getExternalId() {
        return KV_UTLATANDETYP_INTYG_CODE;
    }

    @Override
    public String getIssuerTypeId() {
        //Same as externalId for ts
        return KV_UTLATANDETYP_INTYG_CODE;
    }

}
