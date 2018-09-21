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
package se.inera.intyg.common.ts_diabetes_2.support;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.ts_diabetes_2.rest.TsDiabetes2ModuleApi;

public class TsDiabetes2EntryPoint implements ModuleEntryPoint {

    public static final String DETAILED_DESCRIPTION_TEXT_KEY = "FRM_1.RBK";
    public static final String ISSUER_TYPE_ID = "TSTRK1031-002";
    public static final String MODULE_ID = "ts-diabetes-2";
    public static final String MODULE_NAME = "Läkarintyg Diabetes avseende lämpligheten att inneha körkort m.m.";
    public static final String DEFAULT_RECIPIENT_ID = "TS";
    public static final String MODULE_DESCRIPTION = "Beskrivning av ts-diabetes-2";
    public static final String SCHEMATRON_FILE = "tstrk1031.sch";

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
    public String getIssuerTypeId() {
        return ISSUER_TYPE_ID;
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes-2/minaintyg/css/ts-diabetes-2.css";
        case WEBCERT:
            return "/web/webjars/ts-diabetes-2/webcert/css/ts-diabetes-2.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes-2/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/ts-diabetes-2/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes-2/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/ts-diabetes-2/webcert/module-deps.json";
        default:
            return null;
        }
    }

    @Override
    public String getDetailedModuleDescription() {
        if (repo.isPresent()) {
            final String latestVersion = repo.get().getLatestVersion(getModuleId());
            final IntygTexts texts = repo.get().getTexts(getModuleId(), latestVersion);
            if (texts != null) {
                return texts.getTexter().get(DETAILED_DESCRIPTION_TEXT_KEY);
            }
        }
        return null;
    }

    @Override
    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

    @Override
    public String getExternalId() {
        return ISSUER_TYPE_ID;
    }
}
