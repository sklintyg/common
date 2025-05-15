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
package se.inera.intyg.common.support.modules.config;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMapping;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMappingConfigLoader;

@ExtendWith(MockitoExtension.class)
class CareProviderMappingConfigLoaderTest {

  @InjectMocks
  private CareProviderMappingConfigLoader careProviderMappingConfigLoader;

  @Test
  void shallReturnEmptyListIfPathIsInvalid() {
    ReflectionTestUtils.setField(careProviderMappingConfigLoader, "careProviderMappingConfigPath",
        "InvalidPath");
    careProviderMappingConfigLoader.init();

    assertTrue(careProviderMappingConfigLoader.getCareProviderMappings().isEmpty());
  }

  @Test
  void shallReturnEmptyListIfPathIsMissing() {
    ReflectionTestUtils.setField(careProviderMappingConfigLoader, "careProviderMappingConfigPath",
        null);
    careProviderMappingConfigLoader.init();
    assertTrue(careProviderMappingConfigLoader.getCareProviderMappings().isEmpty());
  }

  @Test
  void shallMapConfigContent() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    String filePath = Paths.get(
            classLoader.getResource("care-provider-mapping-config-active-mapping.json").toURI())
        .toString();

    ReflectionTestUtils.setField(careProviderMappingConfigLoader, "careProviderMappingConfigPath",
        filePath);
    careProviderMappingConfigLoader.init();

    var expected = List.of(
        new CareProviderMapping(
            "Beta Regionen",
            "TSTNMT2321000156-BETA",
            LocalDateTime.of(2025, 5, 9, 8, 0, 0),
            Set.of("TSTNMT2321000156-ALFA", "TSTNMT2321000152-ALFA2")),
        new CareProviderMapping(
            "Gamma Regionen",
            "TSTNMT2321000156-GAMMA",
            LocalDateTime.of(2026, 6, 10, 9, 0, 0),
            Set.of("TSTNMT2321000156-DELTA"))
    );

    assertEquals(expected, careProviderMappingConfigLoader.getCareProviderMappings());
  }

}