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
package se.inera.intyg.common.support.model.converter.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;

public class CertificateStateHolderUtilTest {

    @Test
    public void isArchivedTest() {
        List<CertificateStateHolder> param = new ArrayList<>();
        param.add(new CertificateStateHolder("MI", CertificateState.DELETED, LocalDateTime.now()));
        assertTrue(CertificateStateHolderUtil.isArchived(param));
    }

    @Test
    public void isArchivedThenRestoredTest() {
        List<CertificateStateHolder> param = new ArrayList<>();
        param.add(new CertificateStateHolder("MI", CertificateState.DELETED, LocalDateTime.now().minusSeconds(10)));
        param.add(new CertificateStateHolder("MI", CertificateState.RESTORED, LocalDateTime.now()));
        assertFalse(CertificateStateHolderUtil.isArchived(param));
    }

    @Test
    public void isArchivedThenRestoredAndArchivedAgainTest() {
        List<CertificateStateHolder> param = new ArrayList<>();
        param.add(new CertificateStateHolder("MI", CertificateState.DELETED, LocalDateTime.now().minusSeconds(10)));
        param.add(new CertificateStateHolder("MI", CertificateState.RESTORED, LocalDateTime.now().minusSeconds(5)));
        param.add(new CertificateStateHolder("MI", CertificateState.DELETED, LocalDateTime.now()));
        assertTrue(CertificateStateHolderUtil.isArchived(param));
    }

    @Test
    public void isArchivedMultipleStatusesTest() {
        List<CertificateStateHolder> param = new ArrayList<>();
        param.add(new CertificateStateHolder("FK", CertificateState.SENT, LocalDateTime.now().minusSeconds(10)));
        param.add(new CertificateStateHolder("MI", CertificateState.DELETED, LocalDateTime.now().minusSeconds(5)));
        param.add(new CertificateStateHolder("FK", CertificateState.RECEIVED, LocalDateTime.now()));
        assertTrue(CertificateStateHolderUtil.isArchived(param));
    }

    @Test
    public void isArchivedNoArchivedStatusesTest() {
        List<CertificateStateHolder> param = new ArrayList<>();
        param.add(new CertificateStateHolder("FK", CertificateState.SENT, LocalDateTime.now()));
        param.add(new CertificateStateHolder("FK", CertificateState.RECEIVED, LocalDateTime.now()));
        param.add(new CertificateStateHolder("HV", CertificateState.CANCELLED, LocalDateTime.now()));
        assertFalse(CertificateStateHolderUtil.isArchived(param));
    }

    @Test
    public void isArchivedNullListTest() {
        assertFalse(CertificateStateHolderUtil.isArchived(null));
    }

    @Test
    public void isArchivedEmptyListTest() {
        assertFalse(CertificateStateHolderUtil.isArchived(new ArrayList<>()));
    }
}
