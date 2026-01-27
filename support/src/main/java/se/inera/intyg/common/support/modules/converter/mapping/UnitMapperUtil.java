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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.model.common.internal.Utlatande;


/**
 * Utility class for mapping IDs and names based on a configuration file. The
 * configuration file is loaded at startup and contains mappings for care providers, issuing unit as well as a
 * timestamp for when this mapping should go into effect. This is enabling avbolagisering/bolagisering in the
 * case of old certificate that were created directly in intygstjänster without interacting with
 * webcert.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UnitMapperUtil {

    private final UnitMappingConfigLoader unitMappingConfigLoader;

    public void decorateWithMappedCareProvider(Utlatande utlatande) {
        decorateWithMappedCareProvider(utlatande, null);
    }

    public void decorateWithMappedCareProvider(Utlatande utlatande, LocalDateTime created) {
        if (utlatande == null
            || utlatande.getGrundData() == null
            || utlatande.getGrundData().getSkapadAv() == null
            || utlatande.getGrundData().getSkapadAv().getVardenhet() == null
            || utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare() == null
            || utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid() == null) {
            return;
        }

        final var vardenhet = utlatande.getGrundData().getSkapadAv().getVardenhet();
        final var vardgivare = vardenhet.getVardgivare();
        final var certificateIssuedDate =
            utlatande.getGrundData().getSigneringsdatum() != null ? utlatande.getGrundData().getSigneringsdatum() : created;
        final var mappedUnit = getMappedUnit(
            vardgivare.getVardgivarid(),
            vardgivare.getVardgivarnamn(),
            vardenhet.getEnhetsid(),
            vardenhet.getEnhetsnamn(),
            certificateIssuedDate
        );

        vardgivare.setVardgivarid(mappedUnit.careProviderId());
        vardgivare.setVardgivarnamn(mappedUnit.careProviderName());

        vardenhet.setEnhetsid(mappedUnit.issuedUnitId());
        vardenhet.setEnhetsnamn(mappedUnit.issuedUnitName());

    }

    /**
     * Gets the mapped unit information based on either issued unit mapping or care provider mapping.
     *
     * @param originalCareProviderId the original care provider ID
     * @param originalCareProviderName the original care provider name
     * @param originalIssuedUnitId the original issued unit ID (can be null)
     * @param originalIssuedUnitName the original issued unit name (can be null)
     * @return MappedUnit containing the mapped values
     */
    public MappedUnit getMappedUnit(final String originalCareProviderId,
        final String originalCareProviderName,
        final String originalIssuedUnitId,
        final String originalIssuedUnitName, LocalDateTime certificateIssuedDate) {

        final var issuedUnitMapping = findIssuedUnitMapping(originalIssuedUnitId, certificateIssuedDate);
        if (issuedUnitMapping.isPresent()) {
            final var issuedUnitInfo = issuedUnitMapping.get();
            return MappedUnit.create(
                issuedUnitInfo.careProviderId(),
                issuedUnitInfo.careProviderName(),
                issuedUnitInfo.issuedUnitId(),
                issuedUnitInfo.issuedUnitName()
            );
        }

        final var careProviderMapping = findCareProviderMapping(originalCareProviderId);
        if (careProviderMapping.isPresent()) {
            final var careProviderInfo = careProviderMapping.get();
            return MappedUnit.create(
                careProviderInfo.careProviderId(),
                careProviderInfo.careProviderName(),
                originalIssuedUnitId,
                originalIssuedUnitName
            );
        }

        return MappedUnit.create(
            originalCareProviderId,
            originalCareProviderName,
            originalIssuedUnitId,
            originalIssuedUnitName
        );
    }

    private Optional<IssuedUnitInfo> findIssuedUnitMapping(final String issuedUnitId, LocalDateTime certificateIssuedDate) {
        final var unitMappingKey = new UnitMappingKey(issuedUnitId);
        return unitMappingConfigLoader.getUnitMappings().stream()
            .filter(mappingConfig -> LocalDateTime.now().isAfter(mappingConfig.datetime())
                && mappingConfig.issuedUnitMapping() != null
                && mappingConfig.issuedUnitMapping().containsKey(unitMappingKey)
                && (mappingConfig.issuedDateTime() == null || mappingConfig.issuedDateTime().isBefore(certificateIssuedDate)))
            .findFirst()
            .map(mappingConfig -> mappingConfig.issuedUnitMapping().get(unitMappingKey));
    }

    private Optional<CareProviderInfo> findCareProviderMapping(final String careProviderId) {
        final var unitMappingKey = new UnitMappingKey(careProviderId);
        return unitMappingConfigLoader.getUnitMappings().stream()
            .filter(mappingConfig -> LocalDateTime.now().isAfter(mappingConfig.datetime())
                && mappingConfig.careProviderMapping() != null
                && mappingConfig.careProviderMapping().containsKey(unitMappingKey))
            .findFirst()
            .map(mappingConfig -> mappingConfig.careProviderMapping().get(unitMappingKey));
    }
}