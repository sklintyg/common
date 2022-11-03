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
package se.inera.intyg.common.support.facade.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.common.support.facade.model.CertificateDataElement.CertificateDataElementBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.ValidationError;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;


@JsonDeserialize(builder = CertificateDataElementBuilder.class)
@Value
@Builder
public class CertificateDataElement {

    private String id;
    private String parent;
    private int index;
    private CertificateDataConfig config;
    private CertificateDataValue value;
    private CertificateDataValidation[] validation;
    private ValidationError[] validationError;
    private CertificateDataElementStyleEnum style;
    private Boolean visible;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateDataElementBuilder {

    }
}
