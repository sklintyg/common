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
package se.inera.intyg.common.luae_na.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.common.fkparent.config.FkParentModuleConfig;

/**
 * Java-based replacement for {@code module-config.xml} + {@code luae_na-beans.xml} in the luae_na module.
 *
 * <p>The original XML chain performed a component-scan of {@code se.inera.intyg.common.luae_na}.
 * This class replicates that behaviour. The XML files are kept for backwards compatibility
 * and will be removed in step C.12.
 *
 * <p>{@link FkParentModuleConfig} is imported explicitly so that shared beans such as
 * {@code ValidatorUtilFK} are registered regardless of the host application's component-scan
 * configuration.
 */
@Configuration
@ComponentScan(basePackages = "se.inera.intyg.common.luae_na")
@Import(FkParentModuleConfig.class)
public class LuaenaModuleConfig {

}