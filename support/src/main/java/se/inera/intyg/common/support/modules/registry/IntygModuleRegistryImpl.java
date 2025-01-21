/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.registry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;

public class IntygModuleRegistryImpl implements IntygModuleRegistry, ApplicationContextAware {

    public static final String MODULE_API_BEAN_PREFIX = "moduleapi.";
    private static final String VERSIONED_MODULE_API_BEANID_TEMPLATE = MODULE_API_BEAN_PREFIX + "%s.v%s";
    private static final String JSON_UTLATANDE_VERSION_JSON_PROPERTY_NAME = "textVersion";

    private static final Logger LOG = LoggerFactory.getLogger(IntygModuleRegistryImpl.class);

    @Autowired
    private ApplicationContext applicationContext;
    /*
     * The Autowired annotation will automagically pickup all registered beans with type ModuleEntryPoint and
     * insert into the moduleEntryPoints list.
     */
    @Autowired
    private List<ModuleEntryPoint> moduleEntryPoints;

    private Map<String, ModuleEntryPoint> moduleApiMap = new HashMap<>();

    private Map<String, IntygModule> intygModuleMap = new HashMap<>();

    private Map<String, String> externalIdToModuleId = new HashMap<>();

    private ApplicationOrigin origin;

    @PostConstruct
    public void initModulesList() {

        for (ModuleEntryPoint entryPoint : moduleEntryPoints) {
            moduleApiMap.put(entryPoint.getModuleId(), entryPoint);
            externalIdToModuleId.put(entryPoint.getExternalId(), entryPoint.getModuleId());
            IntygModule module = new IntygModule(
                entryPoint.getModuleId(),
                entryPoint.getModuleName(),
                entryPoint.getModuleDescription(),
                entryPoint.getDetailedModuleDescription(),
                entryPoint.getIssuerTypeId(),
                entryPoint.getModuleCssPath(origin),
                entryPoint.getModuleScriptPath(origin),
                entryPoint.getModuleDependencyDefinitionPath(origin),
                entryPoint.getDefaultRecipient()
            );

            intygModuleMap.put(module.getId(), module);
        }

        LOG.info("Module registry loaded with {} modules", moduleApiMap.size());
    }

    @Override
    public List<IntygModule> listAllModules() {
        List<IntygModule> moduleList = new ArrayList<>(intygModuleMap.values());
        Collections.sort(moduleList);
        return moduleList;
    }

    @Override
    public ModuleApi getModuleApi(String intygType, String intygTypeVersion) throws ModuleNotFoundException {

        if (Strings.isNullOrEmpty(intygType) || Strings.isNullOrEmpty(intygTypeVersion)) {
            throw new ModuleNotFoundException(
                "intygType and intygTypeVersion is required, got '" + intygType + "' and '" + intygTypeVersion + "'");
        }
        // Make sure this is a known intygType before return a wrapper for it..
        ModuleEntryPoint api = moduleApiMap.get(intygType);
        if (api == null) {
            throw new ModuleNotFoundException("Could not find module " + intygType);
        }

        return getVersionedModuleApiBean(intygType, intygTypeVersion);

    }

    @Override
    public ModuleEntryPoint getModuleEntryPoint(String id) throws ModuleNotFoundException {
        ModuleEntryPoint entryPoint = moduleApiMap.get(id);
        if (entryPoint == null) {
            throw new ModuleNotFoundException("Could not find module " + id);
        }
        return entryPoint;
    }

    @Override
    public IntygModule getIntygModule(String id) throws ModuleNotFoundException {
        if (!intygModuleMap.containsKey(id)) {
            throw new ModuleNotFoundException("Could not find module " + id);
        }
        return intygModuleMap.get(id);
    }

    @Override
    public List<ModuleEntryPoint> getModuleEntryPoints() {
        return moduleEntryPoints;
    }

    @Override
    public boolean moduleExists(String moduleId) {
        return moduleApiMap.containsKey(moduleId);
    }

    public ApplicationOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(ApplicationOrigin origin) {
        this.origin = origin;
    }

    @Override
    public String getModuleIdFromExternalId(String externalId) {
        return externalIdToModuleId.get(externalId);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public String resolveVersionFromUtlatandeJson(String intygType, String internalModel) throws ModuleNotFoundException {
        try {
            // First, try extracting textVersion from Utlatande Json
            final JsonNode utlatandeModelNode = new ObjectMapper().readTree(internalModel);
            final JsonNode textVersionNode = utlatandeModelNode.get(JSON_UTLATANDE_VERSION_JSON_PROPERTY_NAME);
            String version = textVersionNode != null ? textVersionNode.asText() : null;
            if (Strings.isNullOrEmpty(version)) {
                // Not found in json: look for default fallback version using the types entrypoint
                version = getModuleEntryPoint(intygType).getDefaultFallbackIntygTypeVersion();
            }

            if (Strings.isNullOrEmpty(version)) {
                throw new ModuleNotFoundException(
                    "Could not extract version for " + intygType + " type utlatande json model string");
            }

            return version;
        } catch (IOException e) {
            throw new ModuleNotFoundException(
                "Could not extract version for " + intygType + " utlatande json model string", e);
        }
    }

    private ModuleApi getVersionedModuleApiBean(String intygType, String intygTypeVersion) throws ModuleNotFoundException {

        try {
            // Majorversion defines model version, we dont really care about the minor (text) version in this context.
            if (Strings.isNullOrEmpty(intygTypeVersion)) {
                throw new ModuleNotFoundException(
                    "Can not resolve ModuleApiBean without intygTypeVersion - got '" + intygTypeVersion + "'");
            }

            final String majorVersion = getMajorVersion(intygTypeVersion);

            // Construct bean name using formal name convention "moduleapi.<type>.<majorversion>"
            final String beanName = String.format(VERSIONED_MODULE_API_BEANID_TEMPLATE, intygType, majorVersion);

            final Object bean = applicationContext
                .getBean(beanName);
            if (bean instanceof ModuleApi) {
                LOG.debug(String.format("Resolved bean named '%s' as instance of %s", beanName, bean.getClass().getName()));
                return (ModuleApi) bean;
            } else {
                throw new ModuleNotFoundException("Resolved bean (" + beanName + ") was not an instance of ModuleApi bean?");
            }
        } catch (BeansException e) {
            throw new ModuleNotFoundException("Exception while trying to look up ModuleApi bean with for intygType '" + intygType
                + "', intygTypeVersion '" + intygTypeVersion + "'", e);
        }
    }

    private String getMajorVersion(String version) {
        return version.split("\\.", 0)[0];
    }

}
