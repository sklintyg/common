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
import se.inera.intyg.common.support.model.common.internal.Utlatande;


/**
 * Utility class for mapping care provider IDs and names based on a configuration file. The
 * configuration file is loaded at startup and contains mappings for care providers as well as a
 * timestamp for when this mapping should go into effect. This is enabling avbolagisering in the
 * case of old certificate that were created directly in intygstjänster without interacting with
 * webcert.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CareProviderMapperUtil {

    private final CareProviderMappingConfigLoader careProviderMappingConfigLoader;
    
    public void decorateWithMappedCareProvider(Utlatande utlatande) {
        if (utlatande == null
            || utlatande.getGrundData() == null
            || utlatande.getGrundData().getSkapadAv() == null
            || utlatande.getGrundData().getSkapadAv().getVardenhet() == null
            || utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare() == null
            || utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid() == null) {
            return;
        }

        final var vardgivare = utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare();
        final var mappedVardgivare = getMappedCareprovider(vardgivare.getVardgivarid(), vardgivare.getVardgivarnamn());
        vardgivare.setVardgivarid(mappedVardgivare.id());
        vardgivare.setVardgivarnamn(mappedVardgivare.name());
    }

    public MappedCareProvider getMappedCareprovider(final String originalCareProviderId,
        final String originalCareProviderName) {

        return careProviderMappingConfigLoader.getCareProviderMappings().stream()
            .filter(mappingConfig -> LocalDateTime.now().isAfter(mappingConfig.datetime())
                && mappingConfig.originalCareProviderIds().contains(originalCareProviderId))
            .findFirst()
            .map(mappingConfig -> new MappedCareProvider(mappingConfig.careProviderId(), mappingConfig.careProviderName()))
            .orElse(new MappedCareProvider(originalCareProviderId, originalCareProviderName));
    }
}
