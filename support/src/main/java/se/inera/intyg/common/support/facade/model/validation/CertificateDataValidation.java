/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.facade.model.validation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = CertificateDataValidationMandatory.class, name = "MANDATORY_VALIDATION"),
    @Type(value = CertificateDataValidationCategoryMandatory.class, name = "CATEGORY_MANDATORY_VALIDATION"),
    @Type(value = CertificateDataValidationShow.class, name = "SHOW_VALIDATION"),
    @Type(value = CertificateDataValidationHide.class, name = "HIDE_VALIDATION"),
    @Type(value = CertificateDataValidationText.class, name = "TEXT_VALIDATION"),
    @Type(value = CertificateDataValidationEnable.class, name = "ENABLE_VALIDATION"),
    @Type(value = CertificateDataValidationDisable.class, name = "DISABLE_VALIDATION"),
    @Type(value = CertificateDataValidationAutoFill.class, name = "AUTO_FILL_VALIDATION"),
    @Type(value = CertificateDataValidationHighlight.class, name = "HIGHLIGHT_VALIDATION"),
    @Type(value = CertificateDataValidationDisableSubElement.class, name = "DISABLE_SUB_ELEMENT_VALIDATION")

})
public interface CertificateDataValidation {

    CertificateDataValidationType getType();
}
