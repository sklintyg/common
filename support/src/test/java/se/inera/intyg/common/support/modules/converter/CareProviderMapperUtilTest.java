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
package se.inera.intyg.common.support.modules.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.config.CareProviderMapping;
import java.util.stream.Stream;
import se.inera.intyg.common.support.modules.config.CareProviderMappingConfigLoader;

@ExtendWith(MockitoExtension.class)
class CareProviderMapperUtilTest {

  private static final List<CareProviderMapping> CARE_PROVIDER_MAPPINGS = List.of(
      new CareProviderMapping(
          "Beta Regionen",
          "TSTNMT2321000156-BETA",
          LocalDateTime.now(),
          Set.of("TSTNMT2321000156-ALFA", "TSTNMT2321000152-ALFA2")
      ),
      new CareProviderMapping(
          "Gamma Regionen",
          "TSTNMT2321000156-GAMMA",
          LocalDateTime.now().plusHours(1),
          Set.of("TSTNMT2321000156-DELTA")
      )
  );

  @Mock
  private CareProviderMappingConfigLoader careProviderMappingConfigLoader;

  @InjectMocks
  private CareProviderMapperUtil careProviderMapperUtil;


  @ParameterizedTest
  @MethodSource("provideTestCases")
  void shouldMapCareProviderCorrectly(String originalId, String originalName, String expectedId,
      String expectedName) {
    when(careProviderMappingConfigLoader.getCareProviderMappings()).thenReturn(
        CARE_PROVIDER_MAPPINGS);

    var originalCareProvider = new Vardgivare();
    originalCareProvider.setVardgivarid(originalId);
    originalCareProvider.setVardgivarnamn(originalName);

    var mappedCareProvider = careProviderMapperUtil.getMappedCareprovider(originalCareProvider);

    assertEquals(expectedId, mappedCareProvider.id());
    assertEquals(expectedName, mappedCareProvider.name());
  }

  private static Stream<Arguments> provideTestCases() {
    return Stream.of(
        Arguments.of("TSTNMT2321000156-ALFA", "Original Name", "TSTNMT2321000156-BETA",
            "Beta Regionen"),
        Arguments.of("TSTNMT2321000156-DELTA", "Original Name", "TSTNMT2321000156-DELTA",
            "Original Name"),
        Arguments.of("UNKNOWN-ID", "Original Name", "UNKNOWN-ID", "Original Name")
    );
  }
}
