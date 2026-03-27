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
package se.inera.intyg.common.fkparent.integration.stub;

import jakarta.xml.ws.Endpoint;
import java.util.List;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;

@Configuration
@Profile({"dev", "it-fk-stub"})
public class FkParentStubConfig {

  @Autowired private Bus bus;

  @Bean("register-fk-stub")
  public RegisterCertificateResponderInterface registerFkStub() {
    return new RegisterCertificateResponderStub();
  }

  @Bean
  public Endpoint registerFkStubEndpoint(
      @Qualifier("register-fk-stub") RegisterCertificateResponderInterface implementor) {
    final var endpoint = new EndpointImpl(bus, implementor);
    endpoint.setSchemaLocations(
        List.of(
            "classpath:/core_components/clinicalprocess_healthcond_certificate_3.3.xsd",
            "classpath:/core_components/clinicalprocess_healthcond_certificate_types_3.2.xsd",
            "classpath:/core_components/clinicalprocess_healthcond_certificate_3.2_ext.xsd",
            "classpath:/core_components/clinicalprocess_healthcond_certificate_3.4_ext.xsd",
            "classpath:/core_components/xmldsig-core-schema_0.1.xsd",
            "classpath:/core_components/xmldsig-filter2.xsd",
            "classpath:/interactions/RegisterCertificateInteraction/RegisterCertificateResponder_3.1.xsd"));
    endpoint.publish(
        "/stubs/clinicalprocess/healthcond/certificate/RegisterCertificate/3/rivtabp21");
    return endpoint;
  }
}
