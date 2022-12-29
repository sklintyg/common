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

package se.inera.intyg.common.support.facade.testsetup.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

public abstract class CommonMetaDataTest {

    protected abstract CertificateMetadata getMetaData();

    protected abstract Utlatande getInternalCertificate();

    protected abstract String getName();

    protected abstract String getDescription();

    protected abstract CertificateTextProvider getTextProvider();

    @Test
    void shouldIncludeId() {
        final var metadata = getMetaData();
        assertEquals(getInternalCertificate().getId(), metadata.getId());
    }

    @Test
    void shouldIncludeType() {
        final var metadata = getMetaData();
        assertEquals(getInternalCertificate().getTyp(), metadata.getType());
    }

    @Test
    void shouldIncludeTypeVersion() {
        final var metadata = getMetaData();
        assertEquals(getInternalCertificate().getTextVersion(), metadata.getTypeVersion());
    }

    @Test
    void shouldIncludeName() {
        final var metadata = getMetaData();
        assertEquals(getName(), metadata.getName());
    }

    @Test
    void shouldIncludeDescription() {
        final var metadata = getMetaData();
        if (getTextProvider() != null) {
            verify(getTextProvider(), atLeastOnce()).get(getDescription());
        }
    }

    @Test
    void shouldIncludeUnit() {
        final var metadata = getMetaData();
        assertNotNull(metadata.getUnit(), "Missing unit!");
    }

    @Test
    void shouldIncludeIssuedBy() {
        final var metadata = getMetaData();
        assertNotNull(metadata.getIssuedBy(), "Missing issued by!");
    }

    @Test
    void shouldIncludePatient() {
        final var metadata = getMetaData();
        assertNotNull(metadata.getPatient(), "Missing patient!");
    }
}

