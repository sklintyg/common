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

package se.inera.intyg.common.support.facade.testsetup.model.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

public abstract class ValueDiagnosListTest<T> extends ValueTest {

    protected abstract CertificateDataElement getElement(T expectedValue);

    protected abstract List<InputExpectedValuePair<T, CertificateDataValueDiagnosisList>> inputExpectedValuePairList();

    protected Stream<InputExpectedValuePair<T, CertificateDataValueDiagnosisList>> inputExpectedValuePairStream() {
        return inputExpectedValuePairList().stream();
    }

    @Override
    protected CertificateDataElement getElement() {
        return getElement(null);
    }

    @Override
    protected CertificateDataValueType getType() {
        return CertificateDataValueType.DIAGNOSIS_LIST;
    }

    @ParameterizedTest
    @MethodSource("inputExpectedValuePairStream")
    void shouldIncludeDiagnosisValueList(InputExpectedValuePair<T, CertificateDataValueCodeList> inputExpectedValuePair) {
        final var question = getElement(inputExpectedValuePair.getInput());
        final var value = (CertificateDataValueDiagnosisList) question.getValue();
        assertEquals(inputExpectedValuePair.getExpectedValue(), value);
    }
}
