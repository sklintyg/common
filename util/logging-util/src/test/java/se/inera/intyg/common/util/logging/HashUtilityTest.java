package se.inera.intyg.common.util.logging;

import org.junit.Assert;
import org.junit.Test;

public class HashUtilityTest {

    @Test
    public void shouldReturnCorrectHashValue() {
        String payload = "1234567890";
        String hashedPayload = HashUtility.hash(payload);
        Assert.assertEquals("c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646", hashedPayload);
    }

    @Test
    public void shouldReturnEmptyHashConstantWhenPayloadIsNull() {
        String payload = null;
        String hashedPayload = HashUtility.hash(payload);
        Assert.assertEquals(HashUtility.EMPTY, hashedPayload);
    }

    @Test
    public void shouldReturnEmptyHashConstantWhenPayloadIsEmpty() {
        String payload = "";
        String hashedPayload = HashUtility.hash(payload);
        Assert.assertEquals(HashUtility.EMPTY, hashedPayload);
    }
}
