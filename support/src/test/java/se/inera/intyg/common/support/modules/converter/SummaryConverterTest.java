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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

class SummaryConverterTest {

    private static final String LABEL = "summaryLabel";
    private static final String VALUE = "summaryValue";
    private ModuleApi moduleApi;
    private Intyg intyg;
    private SummaryConverter summaryConverter;


    @BeforeEach
    void setUp() {
        summaryConverter = new SummaryConverter();
        moduleApi = mock(ModuleApi.class);
        intyg = mock(Intyg.class);
    }

    @Test
    void shallConvertToSummary() throws ModuleException {
        when(moduleApi.getAdditionalInfo(intyg)).thenReturn(VALUE);
        when(moduleApi.getAdditionalInfoLabel()).thenReturn(LABEL);

        final var expectedSummary = CertificateSummary.builder()
            .label(LABEL)
            .value(VALUE)
            .build();

        final var actualSummary = summaryConverter.convert(moduleApi, intyg);
        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    void shallReturnEmptySummaryIfSummaryLabelIsNull() throws ModuleException {
        when(moduleApi.getAdditionalInfo(intyg)).thenReturn(VALUE);

        final var expectedSummary = CertificateSummary.builder().build();

        final var actualSummary = summaryConverter.convert(moduleApi, intyg);
        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    void shallReturnEmptySummaryIfSummaryValueIsNull() {
        when(moduleApi.getAdditionalInfoLabel()).thenReturn(LABEL);

        final var expectedSummary = CertificateSummary.builder().build();

        final var actualSummary = summaryConverter.convert(moduleApi, intyg);
        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    void shallReturnEmptySummaryIfSummaryLabelIsEmpty() throws ModuleException {
        when(moduleApi.getAdditionalInfo(intyg)).thenReturn(VALUE);
        when(moduleApi.getAdditionalInfoLabel()).thenReturn("");

        final var expectedSummary = CertificateSummary.builder().build();

        final var actualSummary = summaryConverter.convert(moduleApi, intyg);
        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    void shallReturnEmptySummaryIfSummaryValueIsEmpty() throws ModuleException {
        final var expectedSummary = CertificateSummary.builder().build();

        when(moduleApi.getAdditionalInfo(intyg)).thenReturn("");
        when(moduleApi.getAdditionalInfoLabel()).thenReturn(LABEL);

        final var actualSummary = summaryConverter.convert(moduleApi, intyg);
        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    void shallReturnEmptySummaryIfModuleExceptionIsThrown() {
        final var expectedSummary = CertificateSummary.builder().build();

        when(summaryConverter.convert(moduleApi, intyg)).thenThrow(new ModuleException("Could not convert summary"));

        final var actualSummary = summaryConverter.convert(moduleApi, intyg);
        assertEquals(expectedSummary, actualSummary);
    }
}
