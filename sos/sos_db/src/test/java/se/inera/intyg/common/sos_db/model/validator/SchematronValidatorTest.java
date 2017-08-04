package se.inera.intyg.common.sos_db.model.validator;

import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;

public class SchematronValidatorTest {
    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator("db.sch");

    // @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("db.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().isEmpty());
    }

}
