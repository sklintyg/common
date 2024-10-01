/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.support.facade.dto;

import java.time.LocalDateTime;
import se.inera.intyg.common.support.facade.model.CertificateStatus;

public class CertificateEventDTO {

    private String certificateId;
    private LocalDateTime timestamp;
    private CertificateEventTypeDTO type;
    private String relatedCertificateId;
    private CertificateStatus relatedCertificateStatus;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public CertificateEventTypeDTO getType() {
        return type;
    }

    public void setType(CertificateEventTypeDTO type) {
        this.type = type;
    }

    public String getRelatedCertificateId() {
        return relatedCertificateId;
    }

    public void setRelatedCertificateId(String relatedCertificateId) {
        this.relatedCertificateId = relatedCertificateId;
    }

    public CertificateStatus getRelatedCertificateStatus() {
        return relatedCertificateStatus;
    }

    public void setRelatedCertificateStatus(CertificateStatus relatedCertificateStatus) {
        this.relatedCertificateStatus = relatedCertificateStatus;
    }
}
