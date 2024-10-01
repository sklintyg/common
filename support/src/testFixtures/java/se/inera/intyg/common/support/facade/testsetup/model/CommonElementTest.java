/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.facade.testsetup.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public abstract class CommonElementTest {

    protected abstract CertificateDataElement getElement();

    protected abstract String getId();

    protected abstract String getParent();

    protected abstract int getIndex();

    @Test
    void shouldIncludeId() {
        final var question = getElement();
        assertEquals(getId(), question.getId());
    }

    @Test
    void shouldIncludeParentId() {
        final var question = getElement();
        assertEquals(getParent(), question.getParent());
    }

    @Test
    void shouldIncludeIndex() {
        final var expectedIndex = 1;
        final var question = getElement();
        assertEquals(getIndex(), question.getIndex());
    }
}
