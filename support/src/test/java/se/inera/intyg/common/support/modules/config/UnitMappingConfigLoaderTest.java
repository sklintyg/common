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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderInfo;
import se.inera.intyg.common.support.modules.converter.mapping.IssuedUnitInfo;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapping;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;

@ExtendWith(MockitoExtension.class)
class UnitMappingConfigLoaderTest {

    @InjectMocks
    private UnitMappingConfigLoader unitMappingConfigLoader;

    @Test
    void shallReturnEmptyListIfPathIsInvalid() {
        ReflectionTestUtils.setField(unitMappingConfigLoader, "unitMappingConfigPath",
            "InvalidPath");
        unitMappingConfigLoader.init();

        assertTrue(unitMappingConfigLoader.getUnitMappings().isEmpty());
    }

    @Test
    void shallReturnEmptyListIfPathIsMissing() {
        ReflectionTestUtils.setField(unitMappingConfigLoader, "unitMappingConfigPath",
            null);
        unitMappingConfigLoader.init();
        assertTrue(unitMappingConfigLoader.getUnitMappings().isEmpty());
    }

    @Test
    void shallMapConfigContent() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        final var filePath = Paths.get(
                classLoader.getResource("care-provider-mapping-config-active-mapping.json").toURI())
            .toString();

        ReflectionTestUtils.setField(unitMappingConfigLoader, "unitMappingConfigPath",
            filePath);
        unitMappingConfigLoader.init();

        var expected = List.of(
            new UnitMapping(
                "Region Stockholm",
                "Avbolagisering av akutsjukhus",
                LocalDateTime.of(2025, 5, 9, 8, 0, 0),
                Map.of(
                    "TSTNMT2321000156-ALFA", new CareProviderInfo("Beta Regionen", "TSTNMT2321000156-BETA"),
                    "TSTNMT2321000152-ALFA2", new CareProviderInfo("Beta Regionen", "TSTNMT2321000156-BETA")
                ),
                null),
            new UnitMapping(
                "Region Gävleborg",
                "Bolagisering av primärvården",
                LocalDateTime.of(2026, 6, 10, 9, 0, 0),
                Map.of(
                    "TSTNMT2321000156-DELTA", new CareProviderInfo("Gamma Regionen", "TSTNMT2321000156-GAMMA")
                ),
                null)
        );

        assertEquals(expected, unitMappingConfigLoader.getUnitMappings());
    }

    @Test
    void shallMapIssuedUnitMappingConfigContent() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        final var filePath = Paths.get(
                classLoader.getResource("care-provider-mapping-config-issued-unit-mapping.json").toURI())
            .toString();

        ReflectionTestUtils.setField(unitMappingConfigLoader, "unitMappingConfigPath",
            filePath);
        unitMappingConfigLoader.init();

        var expected = List.of(
            new UnitMapping(
                "Region Gävleborg",
                "Bolagisering av primärvården",
                LocalDateTime.of(2026, 2, 2, 9, 0, 0),
                null,
                Map.of(
                    "SE2321000016-5G8F", new IssuedUnitInfo(
                        "Region Gävleborg - Primärvård",
                        "SE2321000016-5G8F",
                        "SE2321000016-1G8F",
                        "Region Gävleborg - Enhet 1"
                    ),
                    "SE2321000016-6G8F", new IssuedUnitInfo(
                        "Region Gävleborg - Primärvård",
                        "SE2321000016-5G8F",
                        "SE2321000016-2G8F",
                        "Region Gävleborg - Enhet 2"
                    )
                ))
        );

        assertEquals(expected, unitMappingConfigLoader.getUnitMappings());
    }

}