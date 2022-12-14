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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

public abstract class ValueCodeListTest extends ValueTest {

    protected abstract CertificateDataElement getElementWithValues();

    protected abstract CertificateDataElement getElement(List<Object> expectedValue);

    protected abstract List<Object> getList();

    @Override
    protected CertificateDataValueType getType() {
        return CertificateDataValueType.CODE_LIST;
    }

    protected Stream<List<Object>> codeListValues() {
        return Stream.of(getList(), Collections.emptyList());
    }

    @ParameterizedTest
    @MethodSource("codeListValues")
    void shouldIncludeCodeValueList(List<Object> expectedValues) {
        final var question = getElement(expectedValues);
        final var value = (CertificateDataValueCodeList) question.getValue();
        assertEquals(expectedValues.size(), value.getList().size());
    }

//    @Test
//    void shouldIncludeCodeValueListWithValues() {
//        final var expectedValueList = getList();
//        final var question = getElementWithValues();
//        final var value = (CertificateDataValueCodeList) question.getValue();
//        assertEquals(expectedValueList.size(), value.getList().size());
//    }
}
