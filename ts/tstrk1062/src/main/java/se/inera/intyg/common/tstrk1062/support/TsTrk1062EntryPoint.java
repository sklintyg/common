/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1062.support;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;

@Component("TsTrk1062EntryPoint")
public class TsTrk1062EntryPoint implements ModuleEntryPoint {

    public static final String MODULE_ID = "tstrk1062";
    public static final String MODULE_NAME = "Transportstyrelsens läkarintyg ADHD";

    private static final String DEFAULT_RECIPIENT_ID = "TRANSP";
    private static final String DETAILED_DESCRIPTION_TEXT_KEY = "FRM_1.RBK";

    // CHECKSTYLE:OFF LineLength
    private static final String MODULE_DESCRIPTION = "Läkarintyg avseende ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning";
    // CHECKSTYLE:ON LineLength

    private static final String WEBCERT_MODULE_CSS_PATH = "/web/webjars/tstrk1062/webcert/css/tstrk1062.css";
    private static final String WEBCERT_MODULE_SCRIPT_PATH = "/web/webjars/tstrk1062/webcert/module";
    private static final String WEBCERT_MODULE_DEPENDENCY_PATH = "/web/webjars/tstrk1062/webcert/module-deps.json";

    private static final String MINA_INTYG_MODULE_CSS_PATH = "";
    private static final String MINA_INTYG_MODULE_SCRIPT_PATH = "/web/webjars/tstrk1062/minaintyg/js/module";
    private static final String MINA_INTYG_MODULE_DEPENDENCY_PATH = "/web/webjars/tstrk1062/minaintyg/js/module-deps.json";

    // Depending on context, an IntygTextRepository may not be available (e.g Intygstjansten)
    private Optional<IntygTextsRepository> repo;

    public TsTrk1062EntryPoint() {
        this.repo = Optional.empty();
    }

    @Autowired(required = false)
    public TsTrk1062EntryPoint(Optional<IntygTextsRepository> repo) {
        this.repo = repo;
    }

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
        String detailedModuleDescription = null;
        if (repo.isPresent()) {
            final IntygTextsRepository intygTextsRepository = repo.get();
            final String latestVersion = intygTextsRepository.getLatestVersion(getModuleId());
            final IntygTexts texts = intygTextsRepository.getTexts(getModuleId(), latestVersion);
            if (texts != null) {
                detailedModuleDescription = texts.getTexter().get(DETAILED_DESCRIPTION_TEXT_KEY);
            }
        }
        return detailedModuleDescription;
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return MINA_INTYG_MODULE_CSS_PATH;
            case WEBCERT:
                return WEBCERT_MODULE_CSS_PATH;
            default:
                return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return MINA_INTYG_MODULE_SCRIPT_PATH;
            case WEBCERT:
                return WEBCERT_MODULE_SCRIPT_PATH;
            default:
                return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
            case MINA_INTYG:
                return MINA_INTYG_MODULE_DEPENDENCY_PATH;
            case WEBCERT:
                return WEBCERT_MODULE_DEPENDENCY_PATH;
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
        return KvIntygstyp.TSTRK1062.getCodeValue();
    }

    @Override
    public String getIssuerTypeId() {
        // Same as externalId for ts
        return this.getExternalId();
    }
}
