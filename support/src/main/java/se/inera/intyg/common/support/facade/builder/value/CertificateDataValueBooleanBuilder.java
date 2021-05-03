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

import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

public class CertificateDataValueBooleanBuilder extends AbstractCertificateDataValueBuilder<CertificateDataTextValueBuilder> {

    private String id;
    private Boolean selected;

    public static CertificateDataValueBooleanBuilder create() {
        return new CertificateDataValueBooleanBuilder();
    }

    private CertificateDataValueBooleanBuilder() {
        super(CertificateDataValueType.BOOLEAN);
    }

    public CertificateDataValueBooleanBuilder id(String id) {
        this.id = id;
        return this;
    }

    public CertificateDataValueBooleanBuilder selected(Boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public CertificateDataValueBoolean build() {
        final var certificateDataValueBoolean = new CertificateDataValueBoolean();
        certificateDataValueBoolean.setType(type);
        certificateDataValueBoolean.setId(id);
        certificateDataValueBoolean.setSelected(selected);
        return certificateDataValueBoolean;
    }
}
