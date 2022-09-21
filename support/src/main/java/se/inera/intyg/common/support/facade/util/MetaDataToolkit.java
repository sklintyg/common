/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.facade.util;

import se.inera.intyg.common.support.facade.model.Staff;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;

public final class MetaDataToolkit {

    public static Unit toCertificate(Vardenhet unit) {
        return Unit.builder()
            .unitId(unit.getEnhetsid())
            .unitName(unit.getEnhetsnamn())
            .address(unit.getPostadress())
            .zipCode(unit.getPostnummer())
            .city(unit.getPostort())
            .email(unit.getEpost())
            .phoneNumber(unit.getTelefonnummer())
            .build();
    }

    public static Staff toCertificate(HoSPersonal staff) {
        return Staff.builder()
            .personId(staff.getPersonId())
            .fullName(staff.getFullstandigtNamn())
            .build();
    }
}
