/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.facade.model;

import java.time.LocalDateTime;

public class CertificateMetadata {
    private String id;
    private String type;
    private String typeVersion;
    private String name;
    private String description;
    private LocalDateTime created;
    private CertificateStatus status;
    private boolean testCertificate;
    private boolean forwarded;
    private CertificateRelations relations;
    private Unit unit;
    private Unit careProvider;
    private Patient patient;
    private Staff issuedBy;
    private long version;
    private CertificateReceiver[] approvedReceivers;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeVersion() {
        return typeVersion;
    }

    public void setTypeVersion(String typeVersion) {
        this.typeVersion = typeVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public CertificateStatus getStatus() {
        return status;
    }

    public void setStatus(CertificateStatus status) {
        this.status = status;
    }

    public boolean isTestCertificate() {
        return testCertificate;
    }

    public void setTestCertificate(boolean testCertificate) {
        this.testCertificate = testCertificate;
    }

    public boolean isForwarded() {
        return forwarded;
    }

    public void setForwarded(boolean forwarded) {
        this.forwarded = forwarded;
    }

    public CertificateRelations getRelations() {
        return relations;
    }

    public void setRelations(CertificateRelations relations) {
        this.relations = relations;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getCareProvider() {
        return careProvider;
    }

    public void setCareProvider(Unit careProvider) {
        this.careProvider = careProvider;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Staff getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(Staff issuedBy) {
        this.issuedBy = issuedBy;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public CertificateReceiver[] getApprovedReceivers() {
        return approvedReceivers;
    }

    public void setApprovedReceivers(CertificateReceiver[] approvedReceivers) {
        this.approvedReceivers = approvedReceivers;
    }
}
