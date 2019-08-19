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

import javax.xml.bind.JAXBElement;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public final class XmlMarshallerHelper {

    private static XmlMarshaller marshaller = new XmlMarshaller();

    private XmlMarshallerHelper() {
    }

    public static Jaxb2Marshaller marshaller() {
        return marshaller.jaxb2Marshaller;
    }

    public static <T> String marshal(final JAXBElement<T> element) {
        return marshaller.marshal(element);
    }

    public static <T> JAXBElement<T> unmarshal(final String xmlString) {
        return marshaller.unmarshal(xmlString);
    }
}
