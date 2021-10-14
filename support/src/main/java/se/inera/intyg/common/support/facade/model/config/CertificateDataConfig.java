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
package se.inera.intyg.common.support.facade.model.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = CertificateDataConfigBoolean.class, name = "UE_RADIO_BOOLEAN"),
    @Type(value = CertificateDataConfigCategory.class, name = "CATEGORY"),
    @Type(value = CertificateDataConfigTextArea.class, name = "UE_TEXTAREA"),
    @Type(value = CertificateDataConfigCheckboxBoolean.class, name = "UE_CHECKBOX_BOOLEAN"),
    @Type(value = CertificateDataConfigRadioBoolean.class, name = "UE_RADIO_BOOLEAN"),
    @Type(value = CertificateDataConfigRadioMultipleCode.class, name = "UE_RADIO_MULTIPLE_CODE"),
    @Type(value = CertificateDataConfigCheckboxMultipleDate.class, name = "UE_CHECKBOX_MULTIPLE_DATE"),
    @Type(value = CertificateDataConfigCheckboxMultipleCode.class, name = "UE_CHECKBOX_MULTIPLE_CODE"),
    @Type(value = CertificateDataConfigSickLeavePeriod.class, name = "UE_SICK_LEAVE_PERIOD"),
    @Type(value = CertificateDataConfigDiagnoses.class, name = "UE_DIAGNOSES"),
    @Type(value = CertificateDataConfigDropdown.class, name = "UE_DROPDOWN"),
    @Type(value = CertificateDataConfigIcf.class, name = "UE_ICF")
})
public interface CertificateDataConfig {

    CertificateDataConfigTypes getType();

    String getHeader();

    String getLabel();

    String getIcon();

    String getText();

    String getDescription();
}
