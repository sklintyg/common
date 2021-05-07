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
package se.inera.intyg.common.support.facade.model.value;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = CertificateDataTextValue.class, name = "TEXT"),
    @Type(value = CertificateDataValueBoolean.class, name = "BOOLEAN"),
    @Type(value = CertificateDataValueDateList.class, name = "DATE_LIST"),
    @Type(value = CertificateDataValueDate.class, name = "DATE"),
    @Type(value = CertificateDataValueDiagnosisList.class, name = "DIAGNOSIS_LIST"),
    @Type(value = CertificateDataValueDiagnosis.class, name = "DIAGNOSIS"),
    @Type(value = CertificateDataValueCodeList.class, name = "CODE_LIST"),
    @Type(value = CertificateDataValueCode.class, name = "CODE")
})
public interface CertificateDataValue {
    CertificateDataValueType getType();
}
