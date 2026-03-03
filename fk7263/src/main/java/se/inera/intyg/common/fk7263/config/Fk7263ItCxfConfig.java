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

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.rivtabp20.v1.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateResponderInterface;
import se.inera.intyg.common.fk7263.integration.GetCertificateResponderImpl;
import se.inera.intyg.common.fk7263.integration.GetMedicalCertificateResponderImpl;
import se.inera.intyg.common.fk7263.integration.RegisterMedicalCertificateResponderImpl;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.util.integration.interceptor.SoapFaultToSoapResponseTransformerInterceptor;

/**
 * Java-based replacement for {@code fk7263/src/main/resources/it-module-cxf-servlet.xml} (Step C.7).
 *
 * <p>The original XML (loaded only by intygstjänst via {@code classpath*:it-module-cxf-servlet.xml}) declared:
 * <ul>
 *   <li>A CXF bus with logging feature</li>
 *   <li>Three JAX-WS endpoints:
 *     <ul>
 *       <li>{@code /get-certificate/v1.0} → {@link GetCertificateResponderImpl}</li>
 *       <li>{@code /get-medical-certificate/v1.0} → {@link GetMedicalCertificateResponderImpl}</li>
 *       <li>{@code /register-certificate/v3.0} → {@link RegisterMedicalCertificateResponderImpl}</li>
 *     </ul>
 *     Each endpoint had a {@code SoapFaultToSoapResponseTransformerInterceptor} outFaultInterceptor.
 *   </li>
 *   <li>One JAX-WS client: {@code registerMedicalCertificateClient} at
 *       {@code ${registermedicalcertificatev3.endpoint.url}}</li>
 * </ul>
 *
 * <p>This configuration is activated only under the Spring profile {@code intygstjanst}, ensuring
 * that Webcert and Mina intyg do not inadvertently publish these intygstjänst-only SOAP endpoints.
 *
 * <p>The implementor beans are declared here (rather than relying on component-scan) so that they are
 * only instantiated in the intygstjänst context.
 *
 * <p>The XML file is kept for backwards compatibility and will be deleted in Step C.12.
 */
@Configuration
@Profile("intygstjanst")
public class Fk7263ItCxfConfig {

    @Autowired
    private Bus bus;

    /**
     * Implementor for the {@code GetCertificate} endpoint.
     * Declared as a bean (not via {@code @Component}) so it is only created in the intygstjänst context.
     */
    @Bean
    public GetCertificateResponderInterface getCertificateResponder(ModuleContainerApi moduleContainer) {
        return new GetCertificateResponderImpl(moduleContainer);
    }

    /**
     * Implementor for the {@code GetMedicalCertificate} endpoint.
     */
    @Bean
    public GetMedicalCertificateResponderInterface getMedicalCertificateResponder(ModuleContainerApi moduleContainer) {
        return new GetMedicalCertificateResponderImpl(moduleContainer);
    }

    /**
     * Implementor for the {@code RegisterMedicalCertificate} endpoint.
     */
    @Bean
    public RegisterMedicalCertificateResponderInterface registerMedicalCertificateResponder(ModuleContainerApi moduleContainer) {
        return new RegisterMedicalCertificateResponderImpl(moduleContainer);
    }

    /**
     * Publishes the {@code GetCertificate v1} endpoint at {@code /get-certificate/v1.0}.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <jaxws:endpoint address="/get-certificate/v1.0"
     *                 implementor="se.inera.intyg.common.fk7263.integration.GetCertificateResponderImpl">
     *   <jaxws:outFaultInterceptors>
     *     <bean class="...SoapFaultToSoapResponseTransformerInterceptor">
     *       <constructor-arg value="transform/get-certificate-transform.xslt"/>
     *     </bean>
     *   </jaxws:outFaultInterceptors>
     * </jaxws:endpoint>
     * }</pre>
     */
    @Bean
    public Endpoint getCertificateEndpoint(GetCertificateResponderInterface implementor) {
        final EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        endpoint.getOutFaultInterceptors().add(
            new SoapFaultToSoapResponseTransformerInterceptor("transform/get-certificate-transform.xslt"));
        endpoint.publish("/get-certificate/v1.0");
        return endpoint;
    }

    /**
     * Publishes the {@code GetMedicalCertificate v1} endpoint at {@code /get-medical-certificate/v1.0}.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <jaxws:endpoint address="/get-medical-certificate/v1.0"
     *                 implementor="se.inera.intyg.common.fk7263.integration.GetMedicalCertificateResponderImpl">
     *   <jaxws:outFaultInterceptors>
     *     <bean class="...SoapFaultToSoapResponseTransformerInterceptor">
     *       <constructor-arg value="transform/clinicalprocess-healthcond-certificate/get-medical-certificate-transform.xslt"/>
     *     </bean>
     *   </jaxws:outFaultInterceptors>
     * </jaxws:endpoint>
     * }</pre>
     */
    @Bean
    public Endpoint getMedicalCertificateEndpoint(GetMedicalCertificateResponderInterface implementor) {
        final EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        endpoint.getOutFaultInterceptors().add(
            new SoapFaultToSoapResponseTransformerInterceptor(
                "transform/clinicalprocess-healthcond-certificate/get-medical-certificate-transform.xslt"));
        endpoint.publish("/get-medical-certificate/v1.0");
        return endpoint;
    }

    /**
     * Publishes the {@code RegisterMedicalCertificate v3} endpoint at {@code /register-certificate/v3.0}.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <jaxws:endpoint address="/register-certificate/v3.0"
     *                 implementor="se.inera.intyg.common.fk7263.integration.RegisterMedicalCertificateResponderImpl">
     *   <jaxws:outFaultInterceptors>
     *     <bean class="...SoapFaultToSoapResponseTransformerInterceptor">
     *       <constructor-arg value="transform/register-medical-certificate-transform.xslt"/>
     *     </bean>
     *   </jaxws:outFaultInterceptors>
     * </jaxws:endpoint>
     * }</pre>
     */
    @Bean
    public Endpoint registerMedicalCertificateEndpoint(
        @Qualifier("registerMedicalCertificateResponder") RegisterMedicalCertificateResponderInterface implementor) {
        final EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        endpoint.getOutFaultInterceptors().add(
            new SoapFaultToSoapResponseTransformerInterceptor("transform/register-medical-certificate-transform.xslt"));
        endpoint.publish("/register-certificate/v3.0");
        return endpoint;
    }

    // -------------------------------------------------------------------------
    // JAX-WS client
    // -------------------------------------------------------------------------

    /**
     * Creates the {@code registerMedicalCertificateClient} JAX-WS proxy client.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <jaxws:client id="registerMedicalCertificateClient"
     *               serviceClass="...RegisterMedicalCertificateResponderInterface"
     *               address="${registermedicalcertificatev3.endpoint.url}"/>
     * }</pre>
     */
    @Bean("registerMedicalCertificateClient")
    public RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient(
        @Value("${registermedicalcertificatev3.endpoint.url}") String address) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(RegisterMedicalCertificateResponderInterface.class);
        factory.setAddress(address);
        return (RegisterMedicalCertificateResponderInterface) factory.create();
    }
}





