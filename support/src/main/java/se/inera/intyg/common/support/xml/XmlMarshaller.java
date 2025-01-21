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
package se.inera.intyg.common.support.xml;

import com.helger.xml.transform.StringStreamResult;
import jakarta.xml.bind.JAXBElement;
import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

//import com.helger.commons.xml.transform.StringStreamResult;

/**
 * Setup marshalling for applicable XML-beans (scan by package names).
 */
final class XmlMarshaller {

    Jaxb2Marshaller jaxb2Marshaller;

    XmlMarshaller() {
        jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan(
            "se.riv.clinicalprocess",
            "se.riv.ehr",
            "se.riv.infrastructure",
            "se.riv.strategicresourcemanagement",
            "se.riv.intygsbestallning",
            "se.riv.population",
            "se.riv.informationsecurity",
            "se.riv.insuranceprocess",
            "se.inera.ifv",
            "se.inera.intygstjanster.ts",
            "org.w3");
        try {
            jaxb2Marshaller.afterPropertiesSet();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    <T> String marshal(final JAXBElement<T> element) {
        final StringStreamResult result = new StringStreamResult();
        jaxb2Marshaller.marshal(element, result);
        return result.getAsString();
    }

    <T> JAXBElement<T> unmarshal(final String xmlString) {
        return (JAXBElement<T>) jaxb2Marshaller.unmarshal(new StreamSource(new StringReader(xmlString)));
    }
}
