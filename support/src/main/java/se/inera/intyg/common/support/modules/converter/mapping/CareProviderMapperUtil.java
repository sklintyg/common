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

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * Utility class for mapping care provider IDs and names based on a configuration file.
 * The configuration file is loaded at startup and contains mappings for care providers as well as a timestamp for when this mapping should go into effect.
 * This is enabling avbolagisering in the case of old certificate that were created directly in intygstjänster without interacting with webcert.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CareProviderMapperUtil {

  private final CareProviderMappingConfigLoader careProviderMappingConfigLoader;

  public MappedCareProvider getMappedCareprovider(String originalCareProviderId,
      String originalCareProviderName) {
    String mappedId = originalCareProviderId;
    String mappedName = originalCareProviderName;

    for (CareProviderMapping mappingConfig : careProviderMappingConfigLoader.getCareProviderMappings()) {
      if (LocalDateTime.now().isAfter(mappingConfig.datetime())
          && mappingConfig.originalCareProviderIds().contains(originalCareProviderId)) {
        mappedId = mappingConfig.careProviderId();
        mappedName = mappingConfig.careProviderName();
        break;
      }
    }
    return new MappedCareProvider(mappedId, mappedName);
  }
}
