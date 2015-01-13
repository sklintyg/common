package se.inera.certificate.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

public class InternalLocalDateIntervalTest {

    @Test
    public void testInternaDatesAreValid() {
        InternalDate fromString = new InternalDate("2011-01-01");
        LocalDate fromDate = new LocalDate("2011-01-01");
        InternalDate tomString = new InternalDate("2011-01-02");
        LocalDate tomDate = new LocalDate("2011-01-02");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertEquals("Constructed from date did not match expected", interval.fromAsLocalDate(), fromDate);
        assertEquals("Constructed tom date did not match expected", interval.tomAsLocalDate(), tomDate);
    }

    @Test
    public void testInternaDatesAreInvalid() {
        InternalDate fromDate = new InternalDate("2011-01-01");
        InternalDate tomDate = new InternalDate("2011-01");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromDate, tomDate);
        interval.fromAsLocalDate();
        assertEquals(interval.tomAsLocalDate(), null);
    }

    @Test 
    public void testInternaDatesAreWeirdlyInvalid() {
        InternalDate fromDate = new InternalDate("");
        InternalDate tomDate = new InternalDate();

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromDate, tomDate);
        interval.fromAsLocalDate();
        assertEquals(interval.tomAsLocalDate(), null);
    }

    @Test
    public void testInternalDateIntervalIsValid() {
        InternalDate fromString = new InternalDate("2011-01-01");
        InternalDate tomString = new InternalDate("2011-01-02");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertTrue(interval.isValid());

    }

    @Test
    public void testSingleDayIntervalIsValid() {
        InternalDate fromString = new InternalDate("2011-01-01");
        InternalDate tomString = fromString;

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertTrue(interval.isValid());

    }

    @Test
    public void testInternalDateIntervalIsInvalid() {
        InternalDate fromString = new InternalDate("2011-02-01");
        InternalDate tomString = new InternalDate("2011-01-02");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertFalse(interval.isValid());

    }

    @Test
    public void testEquals() {
        InternalDate fromString = new InternalDate("2011-02-01");
        InternalDate tomString = new InternalDate("2011-03-02");

        InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString, tomString);
        InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString, tomString);
        assertTrue(interval1.equals(interval2));
    }

    @Test
    public void testNotEquals() {
        InternalDate fromString1 = new InternalDate("2011-02-01");
        InternalDate tomString1 = new InternalDate("2011-03-02");

        InternalDate fromString2 = new InternalDate("2012-02-01");
        InternalDate tomString2 = new InternalDate("2012-03-02");
        
        InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString1, tomString1);
        InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString2, tomString2);
        assertFalse(interval1.equals(interval2));
    }
}
