/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.schemas.insuranceprocess.healthreporting.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

/**
 * @author andreaskaltenbach
 */
public class CertificateMetaTypeBuilder {

    private CertificateMetaType metaType;

    public CertificateMetaTypeBuilder() {
        metaType = new CertificateMetaType();
    }

    public CertificateMetaType build() {
        return metaType;
    }

    public CertificateMetaTypeBuilder certificateId(String certificateId) {
        metaType.setCertificateId(certificateId);
        return this;
    }

    public CertificateMetaTypeBuilder certificateType(String certificateType) {
        metaType.setCertificateType(certificateType);
        return this;
    }

    public CertificateMetaTypeBuilder validity(LocalDate fromDate, LocalDate toDate) {
        metaType.setValidFrom(fromDate);
        metaType.setValidTo(toDate);
        return this;
    }

    public CertificateMetaTypeBuilder issuerName(String issuerName) {
        metaType.setIssuerName(issuerName);
        return this;
    }

    public CertificateMetaTypeBuilder facilityName(String facilityName) {
        metaType.setFacilityName(facilityName);
        return this;
    }

    public CertificateMetaTypeBuilder signDate(LocalDate signDate) {
        metaType.setSignDate(signDate);
        return this;
    }

    public CertificateMetaTypeBuilder available(String available) {
        metaType.setAvailable(available);
        return this;
    }

    public CertificateMetaTypeBuilder status(StatusType status, String target, LocalDateTime timestamp) {
        CertificateStatusType certificateStatusType = new CertificateStatusType();
        certificateStatusType.setTarget(target);
        certificateStatusType.setTimestamp(timestamp);
        certificateStatusType.setType(status);
        metaType.getStatus().add(certificateStatusType);
        return this;
    }
}
