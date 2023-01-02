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
package se.inera.intyg.common.support.facade.model.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import se.inera.intyg.common.support.facade.model.CertificateStatus;
import se.inera.intyg.common.support.facade.model.Patient;
import se.inera.intyg.common.support.facade.model.Staff;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata.CertificateMetadataBuilder;

@JsonDeserialize(builder = CertificateMetadataBuilder.class)
@Data
@Builder
public class CertificateMetadata {

    private String id;
    private String type;
    private String typeVersion;
    private String typeName;
    private String name;
    private String description;
    private LocalDateTime created;
    private CertificateStatus status;
    private boolean testCertificate;
    private boolean forwarded;
    private boolean sent;
    private String sentTo;
    private CertificateRelations relations;
    private Unit unit;
    private Unit careUnit;
    private Unit careProvider;
    private Patient patient;
    private Staff issuedBy;
    private long version;
    private boolean latestMajorVersion;
    private LocalDateTime readyForSign;
    private String responsibleHospName;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateMetadataBuilder {

    }
}
