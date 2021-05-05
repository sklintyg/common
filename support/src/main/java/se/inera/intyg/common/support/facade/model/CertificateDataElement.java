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

import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.ValidationError;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;

public class CertificateDataElement {

    private String id;
    private String parent;
    private int index;
    private CertificateDataConfig config;
    private CertificateDataValue value;
    private CertificateDataValidation[] validation;
    private ValidationError[] validationError;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public CertificateDataConfig getConfig() {
        return config;
    }

    public void setConfig(CertificateDataConfig config) {
        this.config = config;
    }

    public CertificateDataValue getValue() {
        return value;
    }

    public void setValue(CertificateDataValue value) {
        this.value = value;
    }

    public CertificateDataValidation[] getValidation() {
        return validation;
    }

    public void setValidation(CertificateDataValidation[] validation) {
        this.validation = validation;
    }

    public ValidationError[] getValidationError() {
        return validationError;
    }

    public void setValidationError(ValidationError[] validationError) {
        this.validationError = validationError;
    }
}
