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
import org.junit.jupiter.api.Nested;
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
import se.inera.intyg.common.support.modules.converter.mapping.IssuedUnitInfo;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapping;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingKey;

@ExtendWith(MockitoExtension.class)
class UnitMapperUtilTest {

    private static final List<UnitMapping> CARE_PROVIDER_MAPPINGS = List.of(
        new UnitMapping(
            "Region Stockholm",
            "Avbolagisering av akutsjukhus",
            LocalDateTime.now(),
            null,
            Map.of(
                new UnitMappingKey("TSTNMT2321000156-ALFA"), new CareProviderInfo("Beta Regionen", "TSTNMT2321000156-BETA"),
                new UnitMappingKey("TSTNMT2321000152-ALFA2"), new CareProviderInfo("Beta Regionen", "TSTNMT2321000156-BETA")
            ),
            null
        ),
        new UnitMapping(
            "Region Gävleborg",
            "Bolagisering av primärvården",
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2),
            Map.of(
                new UnitMappingKey("TSTNMT2321000156-DELTA"), new CareProviderInfo("Gamma Regionen", "TSTNMT2321000156-GAMMA")
            ),
            null
        )
    );

    private static final List<UnitMapping> ISSUED_UNIT_MAPPINGS = List.of(
        new UnitMapping(
            "Region Gävleborg",
            "Bolagisering av primärvården",
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().minusDays(5),
            null,
            Map.of(
                new UnitMappingKey("SE2321000016-5G8F"),
                new IssuedUnitInfo(
                    "Region Gävleborg - Primärvård",
                    "TSTNMT2321000156-ALFA",
                    "SE2321000016-1G8F",
                    "Region Gävleborg - Enhet 1"
                )
            )
        )
    );

    @Mock
    private UnitMappingConfigLoader unitMappingConfigLoader;

    @InjectMocks
    private UnitMapperUtil unitMapperUtil;

    @Nested
    class DecorateUnitTests {

        @Test
        void shouldNotTryToDecorateIfUtlatandeNull() {
            unitMapperUtil.decorateWithMappedCareProvider(null);
            verifyNoInteractions(unitMappingConfigLoader);
        }

        @Test
        void shouldNotTryToDecorateIfGrundDataNull() {
            final var utlatande = mock(Utlatande.class);
            when(utlatande.getGrundData()).thenReturn(null);
            unitMapperUtil.decorateWithMappedCareProvider(utlatande);
            verifyNoInteractions(unitMappingConfigLoader);
        }

        @Test
        void shouldNotTryToDecorateIfSkapadAvNull() {
            final var utlatande = mock(Utlatande.class);
            final var grundData = mock(GrundData.class);
            when(utlatande.getGrundData()).thenReturn(grundData);
            when(utlatande.getGrundData().getSkapadAv()).thenReturn(null);
            unitMapperUtil.decorateWithMappedCareProvider(utlatande);
            verifyNoInteractions(unitMappingConfigLoader);
        }

        @Test
        void shouldNotTryToDecorateIfVardenhetNull() {
            final var utlatande = mock(Utlatande.class);
            final var grundData = mock(GrundData.class);
            final var skapadAv = mock(HoSPersonal.class);
            when(utlatande.getGrundData()).thenReturn(grundData);
            when(utlatande.getGrundData().getSkapadAv()).thenReturn(skapadAv);
            when(utlatande.getGrundData().getSkapadAv().getVardenhet()).thenReturn(null);
            unitMapperUtil.decorateWithMappedCareProvider(utlatande);
            verifyNoInteractions(unitMappingConfigLoader);
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
            unitMapperUtil.decorateWithMappedCareProvider(utlatande);
            verifyNoInteractions(unitMappingConfigLoader);
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
            unitMapperUtil.decorateWithMappedCareProvider(utlatande);
            verifyNoInteractions(unitMappingConfigLoader);
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
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(CARE_PROVIDER_MAPPINGS);

            unitMapperUtil.decorateWithMappedCareProvider(utlatande);

            assertAll(
                () -> assertEquals(expectedId, vardgivare.getVardgivarid()),
                () -> assertEquals(expectedName, vardgivare.getVardgivarnamn())
            );
        }

        @Test
        void shouldDecorateWithMappedIssuedUnit() {
            final var expectedCareProviderId = "TSTNMT2321000156-ALFA";
            final var expectedCareProviderName = "Region Gävleborg - Primärvård";
            final var expectedUnitId = "SE2321000016-1G8F";
            final var expectedUnitName = "Region Gävleborg - Enhet 1";

            final var utlatande = mock(Utlatande.class);
            final var grundData = mock(GrundData.class);
            final var skapadAv = mock(HoSPersonal.class);
            final var vardenhet = new Vardenhet();
            vardenhet.setEnhetsid("SE2321000016-5G8F");
            vardenhet.setEnhetsnamn("Original Enhet");
            final var vardgivare = new Vardgivare();
            vardgivare.setVardgivarid("Original Vardgivarid");
            vardgivare.setVardgivarnamn("Original Name");
            vardenhet.setVardgivare(vardgivare);

            when(utlatande.getGrundData()).thenReturn(grundData);
            when(grundData.getSkapadAv()).thenReturn(skapadAv);
            when(grundData.getSigneringsdatum()).thenReturn(LocalDateTime.now().minusDays(5));
            when(skapadAv.getVardenhet()).thenReturn(vardenhet);
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(ISSUED_UNIT_MAPPINGS);

            unitMapperUtil.decorateWithMappedCareProvider(utlatande);

            assertAll(
                () -> assertEquals(expectedCareProviderId, vardgivare.getVardgivarid()),
                () -> assertEquals(expectedCareProviderName, vardgivare.getVardgivarnamn()),
                () -> assertEquals(expectedUnitId, vardenhet.getEnhetsid()),
                () -> assertEquals(expectedUnitName, vardenhet.getEnhetsnamn())
            );
        }

        @Test
        void shouldNSetOriginalIssuedUnitWhenOnlyCareProviderMappingFound() {
            final var expectedCareProviderId = "TSTNMT2321000156-BETA";
            final var expectedCareProviderName = "Beta Regionen";
            final var originalUnitId = "Original-Unit-Id";
            final var originalUnitName = "Original Enhet";

            final var utlatande = mock(Utlatande.class);
            final var grundData = mock(GrundData.class);
            final var skapadAv = mock(HoSPersonal.class);
            final var vardenhet = new Vardenhet();
            vardenhet.setEnhetsid(originalUnitId);
            vardenhet.setEnhetsnamn(originalUnitName);
            final var vardgivare = new Vardgivare();
            vardgivare.setVardgivarid("TSTNMT2321000156-ALFA");
            vardgivare.setVardgivarnamn("Original Name");
            vardenhet.setVardgivare(vardgivare);

            when(utlatande.getGrundData()).thenReturn(grundData);
            when(grundData.getSkapadAv()).thenReturn(skapadAv);
            when(skapadAv.getVardenhet()).thenReturn(vardenhet);
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(CARE_PROVIDER_MAPPINGS);

            unitMapperUtil.decorateWithMappedCareProvider(utlatande);

            assertAll(
                () -> assertEquals(expectedCareProviderId, vardgivare.getVardgivarid()),
                () -> assertEquals(expectedCareProviderName, vardgivare.getVardgivarnamn()),
                () -> assertEquals(originalUnitId, vardenhet.getEnhetsid()),
                () -> assertEquals(originalUnitName, vardenhet.getEnhetsnamn())
            );
        }
    }

    @Nested
    class GetMappedUnitTests {

        @ParameterizedTest
        @MethodSource("provideTestCases")
        void shouldMapCareProviderCorrectly(String originalId, String originalName, String expectedId,
            String expectedName) {
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(
                CARE_PROVIDER_MAPPINGS);

            var originalCareProvider = new Vardgivare();
            originalCareProvider.setVardgivarid(originalId);
            originalCareProvider.setVardgivarnamn(originalName);

            var mappedCareProvider = unitMapperUtil.getMappedUnit(
                originalCareProvider.getVardgivarid(),
                originalCareProvider.getVardgivarnamn(),
                null,
                null,
                null
            );

            assertAll(
                () -> assertEquals(expectedId, mappedCareProvider.careProviderId()),
                () -> assertEquals(expectedName, mappedCareProvider.careProviderName())
            );
        }

        private static Stream<Arguments> provideTestCases() {
            return Stream.of(
                Arguments.of("TSTNMT2321000156-ALFA", "Original Name", "TSTNMT2321000156-BETA",
                    "Beta Regionen"),
                Arguments.of("TSTNMT2321000156-DELTA", "Original Name", "TSTNMT2321000156-DELTA",
                    "Original Name"),
                Arguments.of("tstnmt2321000156-alfa", "Original Name", "TSTNMT2321000156-BETA",
                    "Beta Regionen"),
                Arguments.of("UNKNOWN-ID", "Original Name", "UNKNOWN-ID", "Original Name"),
                Arguments.of("TSTNMT2321000156-GAMMA", null, "TSTNMT2321000156-GAMMA", null),
                Arguments.of("", "", "", "")
            );
        }

        @Test
        void shouldReturnIssuedUnitMappingWhenPresent() {
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(ISSUED_UNIT_MAPPINGS);

            final var mappedUnit = unitMapperUtil.getMappedUnit(
                "SE2321000016-5G8F",
                "Original Vardgivare",
                "SE2321000016-5G8F",
                "Original Enhet",
                LocalDateTime.now().minusDays(1)
            );

            assertAll(
                () -> assertEquals("TSTNMT2321000156-ALFA", mappedUnit.careProviderId()),
                () -> assertEquals("Region Gävleborg - Primärvård", mappedUnit.careProviderName()),
                () -> assertEquals("SE2321000016-1G8F", mappedUnit.issuedUnitId()),
                () -> assertEquals("Region Gävleborg - Enhet 1", mappedUnit.issuedUnitName())
            );
        }

        @Test
        void shouldNotReturnIssuedUnitMappingIfCertificateIssuedDateHasNotPassed() {
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(ISSUED_UNIT_MAPPINGS);

            final var mappedUnit = unitMapperUtil.getMappedUnit(
                "SE2321000016-5G8F",
                "Original Vardgivare",
                "SE2321000016-5G8F",
                "Original Enhet",
                LocalDateTime.now().minusDays(6)
            );

            assertAll(
                () -> assertEquals("SE2321000016-5G8F", mappedUnit.careProviderId()),
                () -> assertEquals("Original Vardgivare", mappedUnit.careProviderName()),
                () -> assertEquals("SE2321000016-5G8F", mappedUnit.issuedUnitId()),
                () -> assertEquals("Original Enhet", mappedUnit.issuedUnitName())
            );
        }

        @Test
        void shouldReturnIssuedUnitMappingWhenPresentAndOtherCasingUsed() {
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(ISSUED_UNIT_MAPPINGS);

            final var mappedUnit = unitMapperUtil.getMappedUnit(
                "SE2321000016-5G8F",
                "Original Vardgivare",
                "se2321000016-5g8f",
                "Original Enhet",
                LocalDateTime.now().minusDays(1)
            );

            assertAll(
                () -> assertEquals("TSTNMT2321000156-ALFA", mappedUnit.careProviderId()),
                () -> assertEquals("Region Gävleborg - Primärvård", mappedUnit.careProviderName()),
                () -> assertEquals("SE2321000016-1G8F", mappedUnit.issuedUnitId()),
                () -> assertEquals("Region Gävleborg - Enhet 1", mappedUnit.issuedUnitName())
            );
        }

        @Test
        void shouldFallBackToCareProviderMappingWhenNoIssuedUnitMapping() {
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(CARE_PROVIDER_MAPPINGS);

            final var mappedUnit = unitMapperUtil.getMappedUnit(
                "TSTNMT2321000156-ALFA",
                "Original Vardgivare",
                "Original-Unit-Id",
                "Original Enhet",
                LocalDateTime.now().minusDays(1)
            );

            assertAll(
                () -> assertEquals("TSTNMT2321000156-BETA", mappedUnit.careProviderId()),
                () -> assertEquals("Beta Regionen", mappedUnit.careProviderName()),
                () -> assertEquals("Original-Unit-Id", mappedUnit.issuedUnitId()),
                () -> assertEquals("Original Enhet", mappedUnit.issuedUnitName())
            );
        }

        @Test
        void shouldReturnOriginalValuesWhenNoMappingFound() {
            when(unitMappingConfigLoader.getUnitMappings()).thenReturn(CARE_PROVIDER_MAPPINGS);

            final var mappedUnit = unitMapperUtil.getMappedUnit(
                "UNKNOWN-ID",
                "Original Vardgivare",
                "Original-Unit-Id",
                "Original Enhet",
                LocalDateTime.now().minusDays(1)
            );

            assertAll(
                () -> assertEquals("UNKNOWN-ID", mappedUnit.careProviderId()),
                () -> assertEquals("Original Vardgivare", mappedUnit.careProviderName()),
                () -> assertEquals("Original-Unit-Id", mappedUnit.issuedUnitId()),
                () -> assertEquals("Original Enhet", mappedUnit.issuedUnitName())
            );
        }
    }
}