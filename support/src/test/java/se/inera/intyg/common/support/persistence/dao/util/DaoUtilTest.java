package se.inera.intyg.common.support.persistence.dao.util;

import static org.junit.Assert.*;

import org.junit.Test;

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.peristence.dao.util.DaoUtil;

public class DaoUtilTest {
    @Test
    public void testPnrWithAndWithoutDashYieldsSameResult() {
        Personnummer pnr1 = new Personnummer("191212121212");
        Personnummer pnr2 = new Personnummer("19121212-1212");

        assertEquals("19121212-1212", DaoUtil.formatPnrForPersistence(pnr1));
        assertEquals("19121212-1212", DaoUtil.formatPnrForPersistence(pnr2));
    }
}
