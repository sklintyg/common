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

package se.inera.intyg.common.support.facade.util;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;

public final class ValueToolkit {

    private ValueToolkit() {

    }

    public static Boolean booleanValue(Map<String, CertificateDataElement> data, String questionId, String valueId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataValueBoolean)) {
            return null;
        }

        final var booleanDataValue = (CertificateDataValueBoolean) dataValue;
        if (!Objects.equals(booleanDataValue.getId(), valueId)) {
            return null;
        }

        return booleanDataValue.getSelected();
    }

    public static String textValue(Map<String, CertificateDataElement> data, String questionId, String valueId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataTextValue)) {
            return null;
        }

        final var textDataValue = (CertificateDataTextValue) dataValue;
        if (!Objects.equals(textDataValue.getId(), valueId)) {
            return null;
        }

        return textDataValue.getText();
    }

    public static LocalDate dateValue(Map<String, CertificateDataElement> data, String questionId, String valueId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataValueDateList)) {
            return null;
        }

        final var dateListDataValue = (CertificateDataValueDateList) dataValue;
        final var dateValue = dateListDataValue.getList()
            .stream()
            .filter(item -> item.getId().equals(valueId))
            .findAny()
            .orElse(null);
        if (dateValue == null) {
            return null;
        }

        return dateValue.getDate();
    }

    public static String codeValue(Map<String, CertificateDataElement> data, String questionId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataValueCode)) {
            return null;
        }

        final var codeDataValue = (CertificateDataValueCode) dataValue;
        return codeDataValue.getCode();
    }

    public static List<CertificateDataValueCode> codeListValue(Map<String, CertificateDataElement> data, String questionId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataValueCodeList)) {
            return Collections.emptyList();
        }

        final var codeListDataValue = (CertificateDataValueCodeList) dataValue;
        return codeListDataValue.getList();
    }

    public static List<CertificateDataValueDiagnosis> diagnosisListValue(Map<String, CertificateDataElement> data, String questionId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataValueDiagnosisList)) {
            return Collections.emptyList();
        }

        final var diagnosisListDataValue = (CertificateDataValueDiagnosisList) dataValue;
        return diagnosisListDataValue.getList();
    }

    public static List<CertificateDataValueDateRange> dateRangeListValue(Map<String, CertificateDataElement> data, String questionId) {
        final var dataValue = getValue(data, questionId);
        if (!(dataValue instanceof CertificateDataValueDateRangeList)) {
            return Collections.emptyList();
        }

        final var dateRangeListDataValue = (CertificateDataValueDateRangeList) dataValue;
        return dateRangeListDataValue.getList();
    }

    private static CertificateDataValue getValue(Map<String, CertificateDataElement> data, String questionId) {
        final var element = data.get(questionId);
        if (element == null) {
            return null;
        }

        return element.getValue();
    }
}
