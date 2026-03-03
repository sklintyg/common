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
package se.inera.intyg.common.fkparent.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Java-based replacement for the unconditional {@code <beans>} block in
 * {@code fk-parent/src/main/resources/module-config.xml} (Step C.4).
 *
 * <p>The original XML file declared two logical sections:
 * <ol>
 *   <li>An unconditional {@code <bean>} for {@code ValidatorUtilFK} — now covered by the
 *       component-scan below after {@code @Component} was added to that class.</li>
 *   <li>A profile-conditional stub block ({@code dev,it-fk-stub}) with a
 *       {@code RegisterCertificateResponderStub} bean and a CXF JAX-WS endpoint — now handled
 *       by {@code FkParentStubConfig} (in the {@code integration.stub} sub-package) which is
 *       discovered by the component-scan below and is only activated when the relevant profile
 *       is active (via its own {@code @Profile} annotation).</li>
 * </ol>
 *
 * <p>Host applications can pick this configuration up by including
 * {@code se.inera.intyg.common.fkparent} (or a parent package) in their own
 * {@code @ComponentScan}, or by explicitly {@code @Import(FkParentModuleConfig.class)}.
 *
 * <p>The {@code module-config.xml} file is kept for backwards compatibility and will be
 * removed in Step C.12.
 */
@Configuration
@ComponentScan(basePackages = "se.inera.intyg.common.fkparent")
public class FkParentModuleConfig {

}