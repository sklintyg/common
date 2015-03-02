package se.inera.certificate.validate;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.common.util.StringUtil;

public class SimpleHsaIdValidatorTest {

    private SimpleHsaIdValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new SimpleHsaIdValidator();
    }

    @Test
    public void testHsaIdParser() {
        /** This should work */
        assertListSize(0, validator.validateExtension("SE0000000000-1337"));
        assertListSize(0, validator.validateExtension("SE5565594230-1337"));
        assertListSize(0, validator.validateExtension("IFV1239877878-1049"));
        assertListSize(0, validator.validateExtension("TST5565594230-106J"));
        assertListSize(0, validator.validateExtension("SE164815162344-1B01"));
        assertListSize(0, validator.validateExtension("SE0000000000-012345678901234567"));
        assertListSize(0, validator.validateExtension("SE160000000000-1337"));
        assertListSize(0, validator.validateExtension("SE0000000000- '()+,-./:=?"));
        assertListSize(0, validator.validateExtension("SE5565594230-YJ54"));
        
        /** Expect errors */
        assertListSize(1, validator.validateExtension("TESTA5565594230-106J"));
        assertListSize(1, validator.validateExtension("T5565594230-106J"));
        assertListSize(1, validator.validateExtension("DK000000000037"));
        assertListSize(1, validator.validateExtension("SE160000000000- '()+,-./:=?&"));
        assertListSize(1, validator.validateExtension("SE000000000037"));
        assertListSize(1, validator.validateExtension("SE0000000000001337"));
        assertListSize(1, validator.validateExtension("SE000000000000-1337"));
        assertListSize(1, validator.validateExtension("SE0000000000-0123456789012345678"));
    }

    private void assertListSize(int size, List<String> collection) {
        String validationMessage = StringUtil.join(",", collection);
        Assert.assertEquals(validationMessage, size, collection.size());
    }

}
