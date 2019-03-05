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

import java.time.LocalDateTime;
import javax.xml.bind.JAXBElement;
import org.junit.Test;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v1.UtlatandeId;
import se.riv.clinicalprocess.healthcond.certificate.v1.Utlatande;


import static org.junit.Assert.assertEquals;

public class XmlMarshallerHelperTest {

    @Test
    public void marshalTest() {

        JAXBElement<RegisterCertificateType> e1 = new ObjectFactory().createRegisterCertificate(bean());
        String xml1 = XmlMarshallerHelper.marshal(e1);

        JAXBElement<RegisterCertificateType> e2 = XmlMarshallerHelper.unmarshal(xml1);
        String xml2 = XmlMarshallerHelper.marshal(e2);

        assertEquals(xml1, xml2);
    }

    RegisterCertificateType bean() {
        RegisterCertificateType t = new RegisterCertificateType();
        t.setUtlatande(new Utlatande());
        t.getUtlatande().setSigneringsdatum(LocalDateTime.now());
        t.getUtlatande().setUtlatandeId(new UtlatandeId());
        t.getUtlatande().getUtlatandeId().setRoot("root");
        t.getUtlatande().getUtlatandeId().setExtension("extension");
        return t;
    }
}
