/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;

public final class CertificateBuilder {

    private Map<String, CertificateDataElement> data = new HashMap<>();
    private CertificateMetadata metadata;

    public static CertificateBuilder create() {
        return new CertificateBuilder();
    }

    private CertificateBuilder() {

    }

    public CertificateBuilder addElement(CertificateDataElement element) {
        this.data.put(element.getId(), element);
        return this;
    }

    public CertificateBuilder addElements(List<CertificateDataElement> elements) {
        for (CertificateDataElement element : elements) {
            this.data.put(element.getId(), element);
        }
        return this;
    }

    public CertificateBuilder metadata(CertificateMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Certificate build() {
        final var certificate = new Certificate();
        certificate.setMetadata(metadata);
        certificate.setData(data);
        return certificate;
    }

}
