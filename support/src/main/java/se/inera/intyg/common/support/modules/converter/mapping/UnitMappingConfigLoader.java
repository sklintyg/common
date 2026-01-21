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
package se.inera.intyg.common.support.modules.converter.mapping;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnitMappingConfigLoader {

    @Value("${unit.mapping.config.path:}")
    private String unitMappingConfigPath;
    @Getter
    private List<UnitMapping> unitMappings;

    @PostConstruct
    public void init() {
        unitMappings = new ArrayList<>();
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try (final var resourceAsStream = new FileInputStream(unitMappingConfigPath)) {
            unitMappings = objectMapper.readValue(
                resourceAsStream,
                new TypeReference<>() {
                });
            log.info("Unit mapping configuration was loaded successfully: {}",
                unitMappings);
        } catch (FileNotFoundException e) {
            log.warn("File not found: {}. Returning empty configuration.",
                unitMappingConfigPath);
        } catch (Exception e) {
            log.error(
                String.format("Failed to load unit mapping configuration. Reason: %s",
                    e.getMessage()), e
            );
        }
    }
}