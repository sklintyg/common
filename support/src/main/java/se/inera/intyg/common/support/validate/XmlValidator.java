/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.validate;

import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import com.helger.schematron.svrl.SVRLHelper;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;

/**
 * XML validation utility class for certificate-modules.
 *
 * @author erik
 *
 */
public final class XmlValidator {

    private XmlValidator() {
    }

    /**
     * Perform validation of the specified XML string using the supplied {@link RegisterCertificateValidator},
     * allows each module to set up its validator using each modules' schematron-file.
     *
     * @param validator
     *            {@link RegisterCertificateValidator}
     * @param inputXml
     *            String
     * @return {@link ValidateXmlResponse}
     * @throws ModuleException
     */
    public static ValidateXmlResponse validate(RegisterCertificateValidator validator, String inputXml) throws ModuleException {
        try {
            SchematronOutputType valResult = validator.validateSchematron(new StreamSource(new StringReader(inputXml)));
            if (!SVRLHelper.getAllFailedAssertions(valResult).isEmpty()) {
                List<String> errorMsgs = new ArrayList<>();
                SVRLHelper.getAllFailedAssertions(valResult)
                        .forEach(fra -> errorMsgs.add(String.format("TEST: %s, MSG: %s", fra.getTest(), fra.getText())));
                return new ValidateXmlResponse(ValidationStatus.INVALID, errorMsgs);
            } else {
                return new ValidateXmlResponse(ValidationStatus.VALID, new ArrayList<>());
            }
        } catch (Exception e) {
            throw new ModuleException("Failed to validate xml", e);
        }
    }
}
