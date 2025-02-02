/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataUncertainDateValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueInteger;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueYear;

public final class DataValueUtil {

    private DataValueUtil() {
    }

    public static CertificateDataUncertainDateValue getDataUncertainDateValue(String id, String value) {
        return CertificateDataUncertainDateValue.builder()
            .id(id)
            .value(value)
            .build();
    }

    public static CertificateDataValueBoolean getDataValueBoolean(String id, boolean selected) {
        return CertificateDataValueBoolean.builder()
            .id(id)
            .selected(selected)
            .build();
    }

    public static CertificateDataValueCode getDataValueCode(String id, String code) {
        return CertificateDataValueCode.builder()
            .id(id)
            .code(code)
            .build();
    }

    public static CertificateDataValueDate getDataValueDate(String id, LocalDate date) {
        return CertificateDataValueDate.builder()
            .id(id)
            .date(date)
            .build();
    }

    public static CertificateDataValueText getDataValueText(String id, String text) {
        return CertificateDataValueText.builder()
            .id(id)
            .text(text)
            .build();
    }

    public static CertificateDataValueCodeList getDataValueCodeList(String id, String code) {
        return CertificateDataValueCodeList.builder()
            .list(List.of(
                    getDataValueCode(id, code)
                )
            )
            .build();
    }

    public static CertificateDataValueCodeList getDataValueCodeListMaximumValues(List<String> id, List<String> code) {
        final var certificateDataValueCodes = new ArrayList<CertificateDataValueCode>();
        for (int i = 0; i < id.size(); i++) {
            certificateDataValueCodes.add(
                getDataValueCode(id.get(i), code.get(i))
            );
        }
        return CertificateDataValueCodeList.builder()
            .list(
                certificateDataValueCodes
            )
            .build();
    }

    public static CertificateDataValueDateList getDataValueDateListMinimal(String id, LocalDate date) {
        return CertificateDataValueDateList.builder()
            .list(List.of(
                    getDataValueDate(id, date)
                )
            )
            .build();
    }

    public static CertificateDataValueDateList getDataValueDateListMaximal(List<String> listOfIds, LocalDate date, int numberOfDates) {
        final var certificateDataValueDateList = new ArrayList<CertificateDataValueDate>();
        for (int i = 0; i < numberOfDates; i++) {
            certificateDataValueDateList.add(getDataValueDate(listOfIds.get(i), date));
        }
        return CertificateDataValueDateList.builder()
            .list(
                certificateDataValueDateList
            )
            .build();
    }

    public static CertificateDataValueYear getDataValueYear(String id) {
        return CertificateDataValueYear.builder()
            .id(id)
            .year(LocalDate.now().getYear())
            .build();
    }

    public static CertificateDataValueInteger getDataValueInteger(String id, String unitOfMeasurement, Integer value) {
        return CertificateDataValueInteger.builder()
            .id(id)
            .unitOfMeasurement(unitOfMeasurement)
            .value(value)
            .build();
    }

    public static CertificateDataValueDateRange getDataValueDateRange(String id, LocalDate from, LocalDate to) {
        return CertificateDataValueDateRange.builder()
            .id(id)
            .from(from)
            .to(to)
            .build();
    }
}
