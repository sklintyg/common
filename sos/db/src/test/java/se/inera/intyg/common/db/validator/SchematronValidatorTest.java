package se.inera.intyg.common.db.validator;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;

import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertTrue;

public class SchematronValidatorTest {
    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator("db.sch");

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("db.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().isEmpty());
    }

    @Test
    public void failR1() {

    }
}
