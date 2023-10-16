package se.inera.intyg.common.lisjp.v1.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
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

        when(moduleApi.getAdditionalInfo(intyg)).thenReturn("");
        when(moduleApi.getAdditionalInfoLabel()).thenReturn(LABEL);

        final var expectedSummary = CertificateSummary.builder().build();

        final var actualSummary = summaryConverter.convert(moduleApi, intyg);
        assertEquals(expectedSummary, actualSummary);
    }
}