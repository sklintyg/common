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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;

@ExtendWith(MockitoExtension.class)
class InternalConverterUtilUnitMappingTest {

    @Mock
    private UnitMapperUtil unitMapperUtil;

    @InjectMocks
    private InternalConverterUtil internalConverterUtil;


    @Test
    void shouldUseCareProviderMapperUtil() {
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("TSTNMT2321000156-BETA");
        vardgivare.setVardgivarnamn("Beta Regionen");
        vardenhet.setVardgivare(vardgivare);
        skapadAv.setVardenhet(vardenhet);
        vardenhet.setEnhetsid("UNIT_ID");
        vardenhet.setEnhetsnamn("UNIT_NAME");

        var careProvider = new Vardgivare();
        careProvider.setVardgivarid("TSTNMT2321000156-BETA");
        careProvider.setVardgivarnamn("Beta Regionen");
        final var certificateIssuedDate = LocalDateTime.now().minusDays(1);

        internalConverterUtil.initialize();
        when(unitMapperUtil.getMappedUnit(
            careProvider.getVardgivarid(),
            careProvider.getVardgivarnamn(),
            "UNIT_ID",
            "UNIT_NAME",
            certificateIssuedDate))
            .thenReturn(
                new MappedUnit("TSTNMT2321000156-BETA", "Beta Regionen", "UPDATED_UNIT_ID", "UPDATED_UNIT_NAME"));

        var result = InternalConverterUtil.getSkapadAv(skapadAv, certificateIssuedDate);
        assertAll(() -> {
            assertEquals("TSTNMT2321000156-BETA",
                result.getEnhet().getVardgivare().getVardgivareId().getExtension());
            assertEquals("Beta Regionen", result.getEnhet().getVardgivare().getVardgivarnamn());
            assertEquals("UPDATED_UNIT_ID", result.getEnhet().getEnhetsId().getExtension());
            assertEquals("UPDATED_UNIT_NAME", result.getEnhet().getEnhetsnamn());
        });
    }

}