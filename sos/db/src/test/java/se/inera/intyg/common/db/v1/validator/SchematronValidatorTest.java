/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.db.v1.validator;

import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.util.stream.Collectors;
import org.junit.Test;
import se.inera.intyg.common.db.v1.rest.DbModuleApiV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator(DbModuleApiV1.SCHEMATRON_FILE);

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("v1/db.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().stream().collect(Collectors.joining(", ")), response.getValidationErrors().isEmpty());
    }

}
