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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

public final class InternalToTransport {

    private InternalToTransport() {
    }

    public static RegisterCertificateType convert(TsDiabetesUtlatandeV4 source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        final var transport = new RegisterCertificateType();
        transport.setIntyg(UtlatandeToIntyg.convert(source));
        return transport;
    }
}
