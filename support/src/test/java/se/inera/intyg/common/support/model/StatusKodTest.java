/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class StatusKodTest {

    @Test
    public void testFromString() {
        assertEquals(StatusKod.CANCEL, StatusKod.fromString("CANCEL").get());
        assertEquals(StatusKod.DELETE, StatusKod.fromString("DELETE").get());
        assertEquals(StatusKod.RECEIV, StatusKod.fromString("RECEIV").get());
        assertEquals(StatusKod.RESTOR, StatusKod.fromString("RESTOR").get());
        assertEquals(StatusKod.SENTTO, StatusKod.fromString("SENTTO").get());
        assertFalse(StatusKod.fromString("FINNS_EJ").isPresent());
    }

    @Test
    public void testToCertificateState() {
        assertEquals(CertificateState.CANCELLED, StatusKod.CANCEL.toCertificateState().get());
        assertEquals(CertificateState.DELETED, StatusKod.DELETE.toCertificateState().get());
        assertEquals(CertificateState.RECEIVED, StatusKod.RECEIV.toCertificateState().get());
        assertEquals(CertificateState.RESTORED, StatusKod.RESTOR.toCertificateState().get());
        assertEquals(CertificateState.SENT, StatusKod.SENTTO.toCertificateState().get());
    }
}
