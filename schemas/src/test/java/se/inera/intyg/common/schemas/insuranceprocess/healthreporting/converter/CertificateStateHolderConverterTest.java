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
package se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;

public class CertificateStateHolderConverterTest {

    @Test
    public void testToCertificateStatusTypeNull() {
        List<CertificateStatusType> res = CertificateStateHolderConverter.toCertificateStatusType(null);

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void testToCertificateStatusTypeEmptyList() {
        List<CertificateStatusType> res = CertificateStateHolderConverter.toCertificateStatusType(new ArrayList<>());

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void testToCertificateStatusType() {
        final String target1 = "target1";
        final LocalDateTime timestamp1 = LocalDateTime.now();
        final String target2 = "target2";
        final LocalDateTime timestamp2 = LocalDateTime.now().minusDays(2);
        final String target3 = "target3";
        final LocalDateTime timestamp3 = LocalDateTime.now().minusDays(1);
        List<CertificateStateHolder> source = new ArrayList<>();
        source.add(new CertificateStateHolder(target1, CertificateState.CANCELLED, timestamp1));
        source.add(new CertificateStateHolder(target2, CertificateState.RECEIVED, timestamp2));
        source.add(new CertificateStateHolder(target3, CertificateState.SENT, timestamp3));
        List<CertificateStatusType> res = CertificateStateHolderConverter.toCertificateStatusType(source);

        assertNotNull(res);
        assertEquals(3, res.size());
        assertEquals(target1, res.get(0).getTarget());
        assertEquals(StatusType.CANCELLED, res.get(0).getType());
        assertEquals(timestamp1, res.get(0).getTimestamp());
        assertEquals(target2, res.get(1).getTarget());
        assertEquals(StatusType.RECEIVED, res.get(1).getType());
        assertEquals(timestamp2, res.get(1).getTimestamp());
        assertEquals(target3, res.get(2).getTarget());
        assertEquals(StatusType.SENT, res.get(2).getType());
        assertEquals(timestamp3, res.get(2).getTimestamp());
    }

    @Test
    public void testToCertificateStatusTypeFiltersInvalidStates() {
        final String target = "target";
        final LocalDateTime timestamp = LocalDateTime.now().minusDays(2);
        List<CertificateStateHolder> source = new ArrayList<>();
        source.add(new CertificateStateHolder("target1", CertificateState.RESTORED, LocalDateTime.now()));
        source.add(new CertificateStateHolder(target, CertificateState.RECEIVED, timestamp));
        source.add(new CertificateStateHolder("target3", CertificateState.DELETED, LocalDateTime.now().minusDays(1)));
        List<CertificateStatusType> res = CertificateStateHolderConverter.toCertificateStatusType(source);

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(target, res.get(0).getTarget());
        assertEquals(StatusType.RECEIVED, res.get(0).getType());
        assertEquals(timestamp, res.get(0).getTimestamp());
    }
}
