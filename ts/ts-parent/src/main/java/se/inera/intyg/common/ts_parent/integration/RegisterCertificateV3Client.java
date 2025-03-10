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
package se.inera.intyg.common.ts_parent.integration;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.Dispatch;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderService;

public class RegisterCertificateV3Client extends SendTSClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateV3Client.class);

    private static final String REGISTER_V3_NAMESPACE =
        "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3:RegisterCertificate";

    private static final QName REGISTER_V3_PORT_NAME =
        new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificate:3:rivtabp21",
            "RegisterCertificateResponderPort");

    public RegisterCertificateV3Client(String url) {
        super(url);
    }

    @Override
    protected void setupService() {
        setupService(new RegisterCertificateResponderService(), REGISTER_V3_PORT_NAME);
    }

    @Override
    protected Dispatch<SOAPMessage> createDispatchMessage() {
        return createDispatchMessage(REGISTER_V3_NAMESPACE, REGISTER_V3_PORT_NAME);
    }

}
