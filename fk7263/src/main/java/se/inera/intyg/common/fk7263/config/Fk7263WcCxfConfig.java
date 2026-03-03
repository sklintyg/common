/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateResponderInterface;

/**
 * Java-based replacement for {@code fk7263/src/main/resources/wc-module-cxf-servlet.xml} (Step C.11).
 *
 * <p>The original XML (loaded only by Webcert via {@code classpath*:wc-module-cxf-servlet.xml}) declared:
 * <ul>
 *   <li>A CXF bus with logging feature</li>
 *   <li>Two JAX-WS clients:
 *     <ul>
 *       <li>{@code registerMedicalCertificateClient} —
 *           {@link RegisterMedicalCertificateResponderInterface} at
 *           {@code ${intygstjanst.registermedicalcertificate.endpoint.url}}</li>
 *       <li>{@code getMedicalCertificateResponder} —
 *           {@link GetMedicalCertificateResponderInterface} at
 *           {@code ${intygstjanst.getmedicalcertificate.endpoint.url}}</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>This configuration is activated only under the Spring profile {@code webcert}, ensuring
 * that intygstjänst and Mina intyg do not inadvertently create these Webcert-only JAX-WS proxy clients.
 * The {@code registerMedicalCertificateClient} bean name must match the qualifier used in Webcert's
 * {@code IntygServiceImpl} (or equivalent) which injects it by name.
 *
 * <p>The XML file is kept for backwards compatibility and will be deleted in Step C.12.
 */
@Configuration
@Profile("webcert")
public class Fk7263WcCxfConfig {

    /**
     * Creates the {@code registerMedicalCertificateClient} JAX-WS proxy client pointing at the
     * intygstjänst {@code RegisterMedicalCertificate v3} (RIV TA BP 2.0) endpoint.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <jaxws:client id="registerMedicalCertificateClient"
     *               serviceClass="...RegisterMedicalCertificateResponderInterface"
     *               address="${intygstjanst.registermedicalcertificate.endpoint.url}"/>
     * }</pre>
     */
    @Bean("registerMedicalCertificateClient")
    public RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient(
        @Value("${intygstjanst.registermedicalcertificate.endpoint.url}") String address) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(RegisterMedicalCertificateResponderInterface.class);
        factory.setAddress(address);
        return (RegisterMedicalCertificateResponderInterface) factory.create();
    }

    /**
     * Creates the {@code getMedicalCertificateResponder} JAX-WS proxy client pointing at the
     * intygstjänst {@code GetMedicalCertificate v1} endpoint.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <jaxws:client id="getMedicalCertificateResponder"
     *               serviceClass="...GetMedicalCertificateResponderInterface"
     *               address="${intygstjanst.getmedicalcertificate.endpoint.url}"/>
     * }</pre>
     */
    @Bean("getMedicalCertificateResponder")
    public GetMedicalCertificateResponderInterface getMedicalCertificateResponder(
        @Value("${intygstjanst.getmedicalcertificate.endpoint.url}") String address) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(GetMedicalCertificateResponderInterface.class);
        factory.setAddress(address);
        return (GetMedicalCertificateResponderInterface) factory.create();
    }
}