/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.modules.support.api.builder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

public class CertificateHolderBuilder {

    private CertificateHolder certificate;

    public CertificateHolderBuilder(String certificateId) {
        this(certificateId, "");
    }

    public CertificateHolderBuilder(String certificateId, String document) {
        this.certificate = new CertificateHolder();
        this.certificate.setId(certificateId);
        this.certificate.setDocument(document);
    }

    public CertificateHolderBuilder certificateType(String certificateType) {
        certificate.setType(certificateType);
        return this;
    }

    public CertificateHolderBuilder civicRegistrationNumber(Personnummer civicRegistrationNumber) {
        certificate.setCivicRegistrationNumber(civicRegistrationNumber);
        return this;
    }

    public CertificateHolderBuilder validity(String fromDate, String toDate) {
        certificate.setValidFromDate(fromDate);
        certificate.setValidToDate(toDate);
        return this;
    }

    public CertificateHolderBuilder careUnitId(String careUnitId) {
        certificate.setCareUnitId(careUnitId);
        return this;
    }

    public CertificateHolderBuilder careUnitName(String careUnitName) {
        certificate.setCareUnitName(careUnitName);
        return this;
    }

    public CertificateHolderBuilder signingDoctorName(String signingDoctorName) {
        certificate.setSigningDoctorName(signingDoctorName);
        return this;
    }

    public CertificateHolderBuilder signedDate(LocalDateTime signedDate) {
        certificate.setSignedDate(signedDate);
        return this;
    }

    public CertificateHolderBuilder deleted(boolean deleted) {
        certificate.setDeleted(deleted);
        return this;
    }

    public CertificateHolderBuilder state(CertificateState state, String target) {
        return state(state, target, null);
    }

    public CertificateHolderBuilder state(CertificateState state, String target, LocalDateTime timestamp) {
        List<CertificateStateHolder> states = this.certificate.getCertificateStates();
        if (states == null) {
            states = new ArrayList<CertificateStateHolder>();
            this.certificate.setCertificateStates(states);
        }
        states.add(new CertificateStateHolder(target, state, timestamp));
        return this;
    }

    public CertificateHolder build() {
        return certificate;
    }
}
