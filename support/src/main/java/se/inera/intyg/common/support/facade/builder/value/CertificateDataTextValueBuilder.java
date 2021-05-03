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

package se.inera.intyg.common.support.facade.builder.value;

import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

public final class CertificateDataTextValueBuilder extends AbstractCertificateDataValueBuilder<CertificateDataTextValueBuilder> {

    private String id;
    private String text;

    public static CertificateDataTextValueBuilder create() {
        return new CertificateDataTextValueBuilder();
    }

    private CertificateDataTextValueBuilder() {
        super(CertificateDataValueType.TEXT);
    }

    public CertificateDataTextValueBuilder id(String id) {
        this.id = id;
        return this;
    }

    public CertificateDataTextValueBuilder text(String text) {
        this.text = text;
        return this;
    }

    @Override
    public CertificateDataTextValue build() {
        final var certificateDataTextValue = new CertificateDataTextValue();
        certificateDataTextValue.setId(id);
        certificateDataTextValue.setText(text);
        certificateDataTextValue.setType(type);
        return certificateDataTextValue;
    }
}
