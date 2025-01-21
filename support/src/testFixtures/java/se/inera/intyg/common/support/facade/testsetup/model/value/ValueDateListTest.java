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
package se.inera.intyg.common.support.facade.testsetup.model.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class ValueDateListTest<T> extends ValueTest {

    protected abstract CertificateDataElement getElement(T expectedValue);

    protected abstract List<InputExpectedValuePair<T, CertificateDataValueDateList>> inputExpectedValuePairList();

    protected Stream<InputExpectedValuePair<T, CertificateDataValueDateList>> inputExpectedValuePairStream() {
        return inputExpectedValuePairList().stream();
    }

    @Override
    protected CertificateDataValueType getType() {
        return CertificateDataValueType.DATE_LIST;
    }

    @ParameterizedTest
    @MethodSource("inputExpectedValuePairStream")
    void shouldIncludeCorrectIds(InputExpectedValuePair<T, CertificateDataValueDateList> inputExpectedValuePair) {
        final var actualIds = getActualList(inputExpectedValuePair).stream()
            .map(CertificateDataValueDate::getId)
            .collect(Collectors.toList());
        final var expectedIds = getExpectedList(inputExpectedValuePair).stream()
            .map(CertificateDataValueDate::getId)
            .collect(Collectors.toList());

        assertEquals(expectedIds, actualIds);
    }

    @ParameterizedTest
    @MethodSource("inputExpectedValuePairStream")
    void shouldIncludeCorrectDates(InputExpectedValuePair<T, CertificateDataValueDateList> inputExpectedValuePair) {
        final var actualDates = getActualList(inputExpectedValuePair).stream()
            .map(CertificateDataValueDate::getDate)
            .collect(Collectors.toList());
        final var expectedDates = getExpectedList(inputExpectedValuePair).stream()
            .map(CertificateDataValueDate::getDate)
            .collect(Collectors.toList());

        assertEquals(expectedDates, actualDates);
    }

    private List<CertificateDataValueDate> getActualList(InputExpectedValuePair<T, CertificateDataValueDateList> inputExpectedValuePair) {
        final var value = (CertificateDataValueDateList) getElement(inputExpectedValuePair.getInput()).getValue();
        return value.getList();
    }

    private List<CertificateDataValueDate> getExpectedList(InputExpectedValuePair<T, CertificateDataValueDateList> inputExpectedValuePair) {
        return inputExpectedValuePair.getExpectedValue().getList();
    }
}
