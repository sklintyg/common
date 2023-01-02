/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class ValueMedicalInvestigationTest<T> extends ValueTest {

    protected abstract CertificateDataElement getElement(T input);

    protected abstract List<InputExpectedValuePair<T, CertificateDataValueMedicalInvestigationList>> inputExpectedValuePairList();

    protected Stream<InputExpectedValuePair<T, CertificateDataValueMedicalInvestigationList>> inputExpectedValuePairStream() {
        return inputExpectedValuePairList().stream();
    }

    @Override
    protected CertificateDataValueType getType() {
        return CertificateDataValueType.MEDICAL_INVESTIGATION_LIST;
    }

    @ParameterizedTest
    @MethodSource("inputExpectedValuePairStream")
    void shouldIncludeMedicalInvestigationList(
        InputExpectedValuePair<T, CertificateDataValueMedicalInvestigationList> inputExpectedValuePair) {

        final var question = getElement(inputExpectedValuePair.getInput());
        final var value = (CertificateDataValueMedicalInvestigationList) question.getValue();
        assertEquals(inputExpectedValuePair.getExpectedValue(), value);
    }
}
