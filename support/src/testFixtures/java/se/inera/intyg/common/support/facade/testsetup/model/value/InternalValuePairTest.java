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
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public abstract class InternalValuePairTest<T, S> {

    protected abstract CertificateDataElement getElement(T input);

    protected abstract T toInternalValue(Certificate certificate);

    protected abstract List<InputExpectedValuePair<T, S>> inputExpectedValuePairList();

    protected Stream<InputExpectedValuePair<T, S>> inputExpectedValuePairStream() {
        return inputExpectedValuePairList().stream();
    }

    @ParameterizedTest
    @MethodSource("inputExpectedValuePairStream")
    void shouldIncludeTextValue(InputExpectedValuePair<T, S> inputExpectedValuePair) {
        final var certificate = CertificateBuilder.create()
            .addElement(getElement(inputExpectedValuePair.getInput()))
            .build();

        final var actualValue = toInternalValue(certificate);

        assertEquals(inputExpectedValuePair.getExpectedValue(), actualValue);
    }
}
