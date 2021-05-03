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

package se.inera.intyg.common.support.facade.builder;

import se.inera.intyg.common.support.facade.builder.config.AbstractCertificateDataConfigBuilder;
import se.inera.intyg.common.support.facade.model.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.Unit;

public class CertificateMetadataBuilder {

    private String id;
    private String type;
    private String typeVersion;
    private String name;
    private String description;
    private Unit unit;

    public static CertificateMetadataBuilder create() {
        return new CertificateMetadataBuilder();
    }

    private CertificateMetadataBuilder() {

    }

    public CertificateMetadataBuilder id(String id) {
        this.id = id;
        return this;
    }

    public CertificateMetadata build() {
        final var certificateMetadata = new CertificateMetadata();
        certificateMetadata.setId(id);
        certificateMetadata.setType(type);
        certificateMetadata.setTypeVersion(typeVersion);
        certificateMetadata.setName(name);
        certificateMetadata.setDescription(description);
        certificateMetadata.setUnit(unit);
        return certificateMetadata;
    }

    public CertificateMetadataBuilder type(String type) {
        this.type = type;
        return this;
    }

    public CertificateMetadataBuilder typeVersion(String typeVersion) {
        this.typeVersion = typeVersion;
        return this;
    }

    public CertificateMetadataBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CertificateMetadataBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CertificateMetadataBuilder unit(Unit unit) {
        this.unit = unit;
        return this;
    }
}
