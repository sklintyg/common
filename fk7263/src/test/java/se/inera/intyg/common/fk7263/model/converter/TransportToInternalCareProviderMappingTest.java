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

package se.inera.intyg.common.fk7263.model.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedCareProvider;

@ExtendWith(MockitoExtension.class)
class TransportToInternalCareProviderMappingTest {

  private static final String RESOURCE = "TransportToInternalConverterTest/legacy-maximalt-fk7263-transport.xml";

  @Mock
  private CareProviderMapperUtil careProviderMapperUtil;

  @InjectMocks
  private TransportToInternal transportToInternal;


  @Test
  void shouldUseCareProviderMapperUtil() throws ConverterException, JAXBException, IOException {
    JAXBElement<LakarutlatandeType> utlatandeElement = JAXBContext.newInstance(
            LakarutlatandeType.class).createUnmarshaller()
        .unmarshal(new StreamSource(new ClassPathResource(RESOURCE).getInputStream()),
            LakarutlatandeType.class);

    transportToInternal.initialize();
    when(careProviderMapperUtil.getMappedCareprovider("VardgivarId",
        "Landstinget Norrland")).thenReturn(
        new MappedCareProvider("TSTNMT2321000156-BETA", "Beta Regionen"));

    var result = TransportToInternal.convert(utlatandeElement.getValue());
    assertAll(() -> {
      assertEquals("TSTNMT2321000156-BETA",
          result.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
      assertEquals("Beta Regionen",
          result.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarnamn());
    });
  }

}