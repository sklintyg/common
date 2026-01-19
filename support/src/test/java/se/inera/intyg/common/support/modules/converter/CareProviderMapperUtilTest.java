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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderInfo;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMapping;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMappingConfigLoader;

@ExtendWith(MockitoExtension.class)
class CareProviderMapperUtilTest {

    private static final List<CareProviderMapping> CARE_PROVIDER_MAPPINGS = List.of(
        new CareProviderMapping(
            "Region Stockholm",
            "Avbolagisering av akutsjukhus",
            LocalDateTime.now(),
            Map.of(
                "TSTNMT2321000156-ALFA", new CareProviderInfo("Beta Regionen", "TSTNMT2321000156-BETA"),
                "TSTNMT2321000152-ALFA2", new CareProviderInfo("Beta Regionen", "TSTNMT2321000156-BETA")
            ),
            null
        ),
        new CareProviderMapping(
            "Region Gävleborg",
            "Bolagisering av primärvården",
            LocalDateTime.now().plusHours(1),
            Map.of(
                "TSTNMT2321000156-DELTA", new CareProviderInfo("Gamma Regionen", "TSTNMT2321000156-GAMMA")
            ),
            null
        )
    );

    @Mock
    private CareProviderMappingConfigLoader careProviderMappingConfigLoader;

    @InjectMocks
    private CareProviderMapperUtil careProviderMapperUtil;

    @Test
    void shouldNotTryToDecorateIfUtlatandeNull() {
        careProviderMapperUtil.decorateWithMappedCareProvider(null);
        verifyNoInteractions(careProviderMappingConfigLoader);
    }

    @Test
    void shouldNotTryToDecorateIfGrundDataNull() {
        final var utlatande = mock(Utlatande.class);
        when(utlatande.getGrundData()).thenReturn(null);
        careProviderMapperUtil.decorateWithMappedCareProvider(utlatande);
        verifyNoInteractions(careProviderMappingConfigLoader);
    }

    @Test
    void shouldNotTryToDecorateIfSkapadAvNull() {
        final var utlatande = mock(Utlatande.class);
        final var grundData = mock(GrundData.class);
        when(utlatande.getGrundData()).thenReturn(grundData);
        when(utlatande.getGrundData().getSkapadAv()).thenReturn(null);
        careProviderMapperUtil.decorateWithMappedCareProvider(utlatande);
        verifyNoInteractions(careProviderMappingConfigLoader);
    }

    @Test
    void shouldNotTryToDecorateIfVardenhetNull() {
        final var utlatande = mock(Utlatande.class);
        final var grundData = mock(GrundData.class);
        final var skapadAv = mock(HoSPersonal.class);
        when(utlatande.getGrundData()).thenReturn(grundData);
        when(utlatande.getGrundData().getSkapadAv()).thenReturn(skapadAv);
        when(utlatande.getGrundData().getSkapadAv().getVardenhet()).thenReturn(null);
        careProviderMapperUtil.decorateWithMappedCareProvider(utlatande);
        verifyNoInteractions(careProviderMappingConfigLoader);
    }

    @Test
    void shouldNotTryToDecorateIfVardgivareNull() {
        final var utlatande = mock(Utlatande.class);
        final var grundData = mock(GrundData.class);
        final var skapadAv = mock(HoSPersonal.class);
        final var vardenhet = mock(Vardenhet.class);
        when(utlatande.getGrundData()).thenReturn(grundData);
        when(utlatande.getGrundData().getSkapadAv()).thenReturn(skapadAv);
        when(utlatande.getGrundData().getSkapadAv().getVardenhet()).thenReturn(vardenhet);
        when(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare()).thenReturn(null);
        careProviderMapperUtil.decorateWithMappedCareProvider(utlatande);
        verifyNoInteractions(careProviderMappingConfigLoader);
    }

    @Test
    void shouldNotTryToDecorateIfVardgivaridNull() {
        final var utlatande = mock(Utlatande.class);
        final var grundData = mock(GrundData.class);
        final var skapadAv = mock(HoSPersonal.class);
        final var vardenhet = mock(Vardenhet.class);
        final var vardgivare = mock(Vardgivare.class);
        when(utlatande.getGrundData()).thenReturn(grundData);
        when(utlatande.getGrundData().getSkapadAv()).thenReturn(skapadAv);
        when(utlatande.getGrundData().getSkapadAv().getVardenhet()).thenReturn(vardenhet);
        when(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare()).thenReturn(vardgivare);
        when(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid()).thenReturn(null);
        careProviderMapperUtil.decorateWithMappedCareProvider(utlatande);
        verifyNoInteractions(careProviderMappingConfigLoader);
    }

    @Test
    void shouldDecorateWithMappedCareProvider() {
        final var expectedId = "TSTNMT2321000156-BETA";
        final var expectedName = "Beta Regionen";

        final var utlatande = mock(Utlatande.class);
        final var grundData = mock(GrundData.class);
        final var skapadAv = mock(HoSPersonal.class);
        final var vardenhet = mock(Vardenhet.class);
        final var vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("TSTNMT2321000156-ALFA");
        vardgivare.setVardgivarnamn("Original Name");
        when(utlatande.getGrundData()).thenReturn(grundData);
        when(utlatande.getGrundData().getSkapadAv()).thenReturn(skapadAv);
        when(utlatande.getGrundData().getSkapadAv().getVardenhet()).thenReturn(vardenhet);
        when(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare()).thenReturn(vardgivare);
        when(careProviderMappingConfigLoader.getCareProviderMappings()).thenReturn(CARE_PROVIDER_MAPPINGS);

        careProviderMapperUtil.decorateWithMappedCareProvider(utlatande);

        assertAll(
            () -> assertEquals(expectedId, vardgivare.getVardgivarid()),
            () -> assertEquals(expectedName, vardgivare.getVardgivarnamn())
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void shouldMapCareProviderCorrectly(String originalId, String originalName, String expectedId,
        String expectedName) {
        when(careProviderMappingConfigLoader.getCareProviderMappings()).thenReturn(
            CARE_PROVIDER_MAPPINGS);

        var originalCareProvider = new Vardgivare();
        originalCareProvider.setVardgivarid(originalId);
        originalCareProvider.setVardgivarnamn(originalName);

        var mappedCareProvider = careProviderMapperUtil.getMappedCareprovider(
            originalCareProvider.getVardgivarid(),
            originalCareProvider.getVardgivarnamn());

        assertAll(
            () -> assertEquals(expectedId, mappedCareProvider.id()),
            () -> assertEquals(expectedName, mappedCareProvider.name())
        );
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of("TSTNMT2321000156-ALFA", "Original Name", "TSTNMT2321000156-BETA",
                "Beta Regionen"),
            Arguments.of("TSTNMT2321000156-DELTA", "Original Name", "TSTNMT2321000156-DELTA",
                "Original Name"),
            Arguments.of("UNKNOWN-ID", "Original Name", "UNKNOWN-ID", "Original Name"),
            Arguments.of("TSTNMT2321000156-GAMMA", null, "TSTNMT2321000156-GAMMA", null),
            Arguments.of("", "", "", "")
        );
    }
}