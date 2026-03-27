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

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.intyg.common.fk7263.integration.stub.RegisterMedicalCertificateResponderStub;
import se.inera.intyg.common.support.stub.MedicalCertificatesStore;

@Configuration
@Profile({"dev", "it-fk-stub"})
public class Fk7263StubConfig {

  @Autowired private Bus bus;

  @Bean
  public RegisterMedicalCertificateResponderInterface registerMedicalCertificateStub() {
    return new RegisterMedicalCertificateResponderStub(new MedicalCertificatesStore());
  }

  @Bean
  public Endpoint registerMedicalCertificateStubEndpoint(
      @Qualifier("registerMedicalCertificateStub") RegisterMedicalCertificateResponderInterface implementor) {
    final var endpoint = new EndpointImpl(bus, implementor);
    endpoint.publish("/stubs/RegisterMedicalCertificate/3/rivtabp20");
    return endpoint;
  }
}
