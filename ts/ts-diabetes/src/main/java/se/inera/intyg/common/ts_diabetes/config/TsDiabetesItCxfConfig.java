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
package se.inera.intyg.common.ts_diabetes.config;

import jakarta.xml.ws.Endpoint;
import java.util.List;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.ts_diabetes.v2.integration.GetTSDiabetesResponderImpl;
import se.inera.intyg.common.ts_diabetes.v2.integration.RegisterTSDiabetesResponderImpl;
import se.inera.intyg.common.ts_parent.integration.RegisterCertificateV1Client;
import se.inera.intyg.common.util.integration.interceptor.SoapFaultToSoapResponseTransformerInterceptor;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponderInterface;

/**
 * Java-based replacement for {@code ts-diabetes/src/main/resources/it-module-cxf-servlet.xml} (Step C.9).
 *
 * <p>The original XML (loaded only by intygstjänst via {@code classpath*:it-module-cxf-servlet.xml}) declared:
 * <ul>
 *   <li>A CXF bus with logging feature</li>
 *   <li>Two JAX-WS endpoints:
 *     <ul>
 *       <li>{@code /get-ts-diabetes/v1.0} → {@link GetTSDiabetesResponderImpl} with a
 *           {@code SoapFaultToSoapResponseTransformerInterceptor} outFaultInterceptor</li>
 *       <li>{@code /register-ts-diabetes/v1.0} → {@link RegisterTSDiabetesResponderImpl} with a
 *           {@code SoapFaultToSoapResponseTransformerInterceptor} outFaultInterceptor,
 *           schema-validation enabled, and three schemaLocations</li>
 *     </ul>
 *   </li>
 *   <li>One {@link XslTransformer} bean: {@code tsDiabetesXslTransformer}</li>
 *   <li>One {@link RegisterCertificateV1Client} bean: {@code sendTsDiabetesClient}</li>
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
public class TsDiabetesItCxfConfig {

    @Autowired
    private Bus bus;

    /**
     * Implementor for the {@code GetTSDiabetes} endpoint.
     * Declared as a bean (not via {@code @Component}) so it is only created in the intygstjänst context.
     */
    @Bean
    public GetTSDiabetesResponderInterface getTSDiabetesResponder(ModuleContainerApi moduleContainer) {
        return new GetTSDiabetesResponderImpl(moduleContainer);
    }

    /**
     * Implementor for the {@code RegisterTSDiabetes} endpoint.
     * Declared as a bean (not via {@code @Component}) so it is only created in the intygstjänst context.
     */
    @Bean
    public RegisterTSDiabetesResponderInterface registerTSDiabetesResponder(ModuleContainerApi moduleContainer) {
        return new RegisterTSDiabetesResponderImpl(moduleContainer);
    }

    /**
     * Publishes the {@code GetTSDiabetes v1} endpoint at {@code /get-ts-diabetes/v1.0}.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <jaxws:endpoint address="/get-ts-diabetes/v1.0"
     *                 implementor="se.inera.intyg.common.ts_diabetes.v2.integration.GetTSDiabetesResponderImpl">
     *   <jaxws:outFaultInterceptors>
     *     <bean class="...SoapFaultToSoapResponseTransformerInterceptor">
     *       <constructor-arg value="transform/se-intygstjanster-ts-services/get-ts-diabetes-transform.xslt"/>
     *     </bean>
     *   </jaxws:outFaultInterceptors>
     * </jaxws:endpoint>
     * }</pre>
     */
    @Bean
    public Endpoint getTSDiabetesEndpoint(GetTSDiabetesResponderInterface implementor) {
        final EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        endpoint.getOutFaultInterceptors().add(
            new SoapFaultToSoapResponseTransformerInterceptor(
                "transform/se-intygstjanster-ts-services/get-ts-diabetes-transform.xslt"));
        endpoint.publish("/get-ts-diabetes/v1.0");
        return endpoint;
    }

    /**
     * Publishes the {@code RegisterTSDiabetes v1} endpoint at {@code /register-ts-diabetes/v1.0}.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <jaxws:endpoint address="/register-ts-diabetes/v1.0"
     *                 implementor="se.inera.intyg.common.ts_diabetes.v2.integration.RegisterTSDiabetesResponderImpl">
     *   <jaxws:outFaultInterceptors>
     *     <bean class="...SoapFaultToSoapResponseTransformerInterceptor">
     *       <constructor-arg value="transform/se-intygstjanster-ts-services/register-ts-diabetes-transform.xslt"/>
     *     </bean>
     *   </jaxws:outFaultInterceptors>
     *   <jaxws:properties>
     *     <entry key="schema-validation-enabled" value="true"/>
     *   </jaxws:properties>
     *   <jaxws:schemaLocations>
     *     <jaxws:schemaLocation>classpath:/core_components/se_intygstjanster_services_1.0.xsd</jaxws:schemaLocation>
     *     <jaxws:schemaLocation>classpath:/core_components/se_intygstjanster_services_types_1.0.xsd</jaxws:schemaLocation>
     *     <jaxws:schemaLocation>classpath:/interactions/RegisterTSDiabetesInteraction/RegisterTSDiabetesResponder_1.0.xsd</jaxws:schemaLocation>
     *   </jaxws:schemaLocations>
     * </jaxws:endpoint>
     * }</pre>
     */
    @Bean
    public Endpoint registerTSDiabetesEndpoint(RegisterTSDiabetesResponderInterface implementor) {
        final EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        endpoint.getOutFaultInterceptors().add(
            new SoapFaultToSoapResponseTransformerInterceptor(
                "transform/se-intygstjanster-ts-services/register-ts-diabetes-transform.xslt"));
        endpoint.getProperties().put("schema-validation-enabled", "true");
        endpoint.setSchemaLocations(List.of(
            "classpath:/core_components/se_intygstjanster_services_1.0.xsd",
            "classpath:/core_components/se_intygstjanster_services_types_1.0.xsd",
            "classpath:/interactions/RegisterTSDiabetesInteraction/RegisterTSDiabetesResponder_1.0.xsd"
        ));
        endpoint.publish("/register-ts-diabetes/v1.0");
        return endpoint;
    }

    /**
     * Creates the {@code tsDiabetesXslTransformer} bean used for XSLT transformation of ts-diabetes XML.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <bean id="tsDiabetesXslTransformer"
     *       class="se.inera.intyg.common.support.modules.transformer.XslTransformer">
     *   <constructor-arg value="xsl/transform-ts-diabetes.xsl"/>
     * </bean>
     * }</pre>
     */
    @Bean("tsDiabetesXslTransformer")
    public XslTransformer tsDiabetesXslTransformer() {
        return new XslTransformer("xsl/transform-ts-diabetes.xsl");
    }

    /**
     * Creates the {@code sendTsDiabetesClient} — a {@link RegisterCertificateV1Client} pointing at the
     * configured endpoint URL.
     *
     * <p>Equivalent XML:
     * <pre>{@code
     * <bean id="sendTsDiabetesClient"
     *       class="se.inera.intyg.common.ts_parent.integration.RegisterCertificateV1Client">
     *   <constructor-arg value="${registercertificatev1.endpoint.url}"/>
     * </bean>
     * }</pre>
     */
    @Bean("sendTsDiabetesClient")
    public RegisterCertificateV1Client sendTsDiabetesClient(
        @Value("${registercertificatev1.endpoint.url}") String url) {
        return new RegisterCertificateV1Client(url);
    }
}