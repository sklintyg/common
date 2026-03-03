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
package se.inera.intyg.common.fk7263.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Java-based replacement for {@code module-config.xml} + {@code fk7263-beans.xml} in the fk7263 module.
 *
 * <p>The original XML chain declared individual beans for classes that lacked {@code @Component}.
 * Step C.3 adds {@code @Component} to all previously unannotated classes
 * ({@code WebcertModelFactoryImpl}, {@code Fk7263ModelCompareUtil}, {@code InternalDraftValidator},
 * {@code Fk7263EntryPoint}) so that a simple component-scan of the module package is sufficient.
 * The XML files are kept for backwards compatibility and will be removed in step C.12.
 */
@Configuration
@ComponentScan(basePackages = "se.inera.intyg.common.fk7263")
public class Fk7263ModuleConfig {

}