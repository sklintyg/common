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

import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;

public final class CertificateDataElementBuilder {

    private String id;
    private String parent;
    private int index;
    private CertificateDataConfig certificateDataConfig;
    private CertificateDataValue certificateDataValue;
    private CertificateDataValidation[] certificateDataValidations;

    public static CertificateDataElementBuilder create() {
        return new CertificateDataElementBuilder();
    }

    private CertificateDataElementBuilder() {

    }

    public CertificateDataElementBuilder id(String id) {
        this.id = id;
        return this;
    }

    public CertificateDataElementBuilder parent(String parent) {
        this.parent = parent;
        return this;
    }

    public CertificateDataElement build() {
        final var certificateDataElement = new CertificateDataElement();
        certificateDataElement.setId(id);
        certificateDataElement.setParent(parent);
        certificateDataElement.setIndex(index);
        certificateDataElement.setConfig(certificateDataConfig);
        certificateDataElement.setValue(certificateDataValue);
        certificateDataElement.setValidation(certificateDataValidations);
        return certificateDataElement;
    }

    public CertificateDataElementBuilder index(int index) {
        this.index = index;
        return this;
    }

    public CertificateDataElementBuilder config(CertificateDataConfig certificateDataConfig) {
        this.certificateDataConfig = certificateDataConfig;
        return this;
    }

    public CertificateDataElementBuilder value(CertificateDataValue certificateDataValue) {
        this.certificateDataValue = certificateDataValue;
        return this;
    }

    public CertificateDataElementBuilder validation(CertificateDataValidation[] certificateDataValidations) {
        this.certificateDataValidations = certificateDataValidations;
        return this;
    }
}
