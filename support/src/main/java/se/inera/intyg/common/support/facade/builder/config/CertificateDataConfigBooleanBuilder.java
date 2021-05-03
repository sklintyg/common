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

package se.inera.intyg.common.support.facade.builder.config;

import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;

public class CertificateDataConfigBooleanBuilder extends AbstractCertificateDataConfigBuilder<CertificateDataConfigBooleanBuilder> {

    private String id;
    private String selectedText;
    private String unselectedText;

    public static CertificateDataConfigBooleanBuilder create() {
        return new CertificateDataConfigBooleanBuilder();
    }

    private CertificateDataConfigBooleanBuilder() {
        super(CertificateDataConfigTypes.UE_RADIO_BOOLEAN);
    }

    public CertificateDataConfigBooleanBuilder id(String id) {
        this.id = id;
        return this;
    }

    public CertificateDataConfigBooleanBuilder selectedText(String selectedText) {
        this.selectedText = selectedText;
        return this;
    }
    
    public CertificateDataConfigBooleanBuilder unselectedText(String unselectedText) {
        this.unselectedText = unselectedText;
        return this;
    }

    @Override
    public CertificateDataConfigBoolean build() {
        final var certificateDataConfigBoolean = new CertificateDataConfigBoolean();
        certificateDataConfigBoolean.setType(type);
        certificateDataConfigBoolean.setText(text);
        certificateDataConfigBoolean.setDescription(description);
        certificateDataConfigBoolean.setId(id);
        certificateDataConfigBoolean.setSelectedText(selectedText);
        certificateDataConfigBoolean.setUnselectedText(unselectedText);
        return certificateDataConfigBoolean;
    }
}
