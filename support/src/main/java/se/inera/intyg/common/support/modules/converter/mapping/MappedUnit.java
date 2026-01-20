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

/**
 * Record containing mapped unit information including care provider and optionally issued unit.
 *
 * @param careProviderId the mapped care provider ID
 * @param careProviderName the mapped care provider name
 * @param issuedUnitId the mapped issued unit ID (null if only care provider mapping)
 * @param issuedUnitName the mapped issued unit name (null if only care provider mapping)
 */
public record MappedUnit(
    String careProviderId,
    String careProviderName,
    String issuedUnitId,
    String issuedUnitName
) {

    /**
     * Creates a MappedUnit with only care provider information.
     */
    public static MappedUnit careProviderOnly(String careProviderId, String careProviderName) {
        return new MappedUnit(careProviderId, careProviderName, null, null);
    }

    /**
     * Returns true if this mapping includes issued unit information.
     */
    public boolean hasIssuedUnitMapping() {
        return issuedUnitId != null;
    }
}