package se.inera.intyg.common.lisjp.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LisjpEntryPointTest {

    @Test
    void shouldReturnCertificateServiceTypeId() {
        final var expected = "fk7804";
        final var actual = new LisjpEntryPoint().certificateServiceTypeId();
        assertEquals(expected, actual);
    }
}