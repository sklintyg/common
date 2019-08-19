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
package se.inera.intyg.common.support.modules.converter;

import java.time.LocalDateTime;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateType;

public final class InternalToRevoke {

    private InternalToRevoke() {
    }

    public static RevokeCertificateType convert(Utlatande source, HoSPersonal skickatAv, String meddelande) throws ConverterException {
        RevokeCertificateType request = new RevokeCertificateType();
        request.setIntygsId(InternalConverterUtil.getIntygsId(source));
        request.setMeddelande(meddelande);
        request.setPatientPersonId(InternalConverterUtil.getPersonId(source.getGrundData().getPatient().getPersonId()));
        request.setSkickatAv(InternalConverterUtil.getSkapadAv(skickatAv));
        request.setSkickatTidpunkt(LocalDateTime.now());
        return request;
    }
}
