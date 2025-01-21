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
import static se.inera.intyg.common.support.facade.model.value.CertificateDataValueType.BOOLEAN;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

public abstract class ValueBooleanTest extends ValueTest {

    protected abstract String getJsonId();

    protected abstract Boolean getBoolean();

    protected CertificateDataValueType getType() {
        return BOOLEAN;
    }

    @Test
    void shouldIncludeValueId() {
        final var question = getElement();
        final var value = (CertificateDataValueBoolean) question.getValue();
        assertEquals(getJsonId(), value.getId());
    }

    @Test
    void shouldIncludeValueBoolean() {
        final var question = getElement();
        final var value = (CertificateDataValueBoolean) question.getValue();
        assertEquals(getBoolean(), value.getSelected());
    }
}
