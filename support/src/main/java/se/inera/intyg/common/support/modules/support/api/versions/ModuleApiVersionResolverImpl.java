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
package se.inera.intyg.common.support.modules.support.api.versions;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;

/**
 * Created by marced on 2018-09-18.
 */

@Component
public class ModuleApiVersionResolverImpl implements ModuleApiVersionResolver, ApplicationContextAware {

    public static final String MODULE_API_BEAN_PREFIX = "moduleapi.";
    private static final Logger LOG = LoggerFactory.getLogger(ModuleApiVersionResolverImpl.class);
    private static final String VERSIONED_MODULE_API_BEANID_TEMPLATE = MODULE_API_BEAN_PREFIX + "%s.v%s";
    private static final String JSON_UTLATANDE_VERSION_JSON_PROPERTY_NAME = "textVersion";

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String resolveVersionFromUtlatandeJson(String internalModel) throws ModuleException {
        try {
            final JsonNode jsonNode = new ObjectMapper().readTree(internalModel);
            final JsonNode textVersionNode = jsonNode.get(JSON_UTLATANDE_VERSION_JSON_PROPERTY_NAME);
            final String version = textVersionNode != null ? textVersionNode.asText() : null;
            if (Strings.isNullOrEmpty(version)) {
                throw new ModuleException("Could not extract '" + JSON_UTLATANDE_VERSION_JSON_PROPERTY_NAME + "' from utlatande json model string");
            }

            return getMajorVersion(version);
        } catch (IOException e) {
            throw new ModuleException("Could not extract '" + JSON_UTLATANDE_VERSION_JSON_PROPERTY_NAME  + "' from utlatande json model string");
        }
    }

    private String getMajorVersion(String version) {
        return version.split("\\.", 0)[0];
    }

    @Override
    public ModuleApi getVersionedModuleApi(String intygType, String intygTypeVersion) throws ModuleException {

        // Majorversion defines model version, we dont really care about the minor (text) version in this context.
        final String majorVersion = getMajorVersion(intygTypeVersion);

        // Construct bean name using convention "moduleapi.<type>.<majorversion>"
        final String beanName = String.format(VERSIONED_MODULE_API_BEANID_TEMPLATE, intygType, majorVersion);

        try {
            final Object bean = applicationContext
                    .getBean(beanName);
            if (bean instanceof ModuleApi) {
                LOG.debug(String.format("Resolved bean named '%s' as instance of %s", beanName, bean.getClass().getName()));
                return (ModuleApi) bean;
            } else {
                throw new ModuleException(
                        String.format("Resolved bean '%s' was not an instance of ModuleApi bean? (for intygstyp %s, version %s)", beanName,
                                intygType, majorVersion));
            }
        } catch (BeansException e) {
            throw new ModuleException(String.format("Exception while trying to find moduleapi bean '%s' (for intygstyp %s, version %s)",
                    beanName, intygType, majorVersion), e);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
