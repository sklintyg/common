/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;


import java.io.IOException;
import java.util.Collections;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.google.common.io.CharSource;
import com.helger.commons.xml.transform.StringStreamResult;

/**
 * Setup marshalling for applicable XML-beans (scan by package names).
 */
final class XmlMarshaller {
    Jaxb2Marshaller jaxb2Marshaller;

    XmlMarshaller()  {
        jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("se.riv", "se.inera.ifv", "se.inera.intygstjanster.ts");
        jaxb2Marshaller.setMarshallerProperties(Collections.singletonMap(JAXB_FORMATTED_OUTPUT, true));
    }

     <T> String marshal(final JAXBElement<T> element) {
        final StringStreamResult result = new StringStreamResult();
        jaxb2Marshaller.marshal(element, result);
        return result.getAsString();
    }

    <T> JAXBElement<T> unmarshal(final String xmlString) {
        try {
            return (JAXBElement<T>) jaxb2Marshaller.unmarshal(new StreamSource(
                    CharSource.wrap(xmlString).openStream()
            ));
        } catch (IOException e) {
            throw new DataBindingException(e);
        }
    }
}
