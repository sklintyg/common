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
package se.inera.intyg.common.db.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Java-based replacement for {@code module-config.xml} + {@code db-beans.xml} in the db module.
 *
 * <p>The original XML chain performed a component-scan of {@code se.inera.intyg.common.db}.
 * This class replicates that behaviour. The XML files are kept for backwards compatibility
 * and will be removed in step C.12.
 */
@Configuration
@ComponentScan(basePackages = "se.inera.intyg.common.db")
public class DbModuleConfig {

}