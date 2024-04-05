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
package se.inera.intyg.common.support.facade.testsetup.model.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;

public abstract class ConfigCheckboxMultipleCodeTest extends ConfigTest {

    protected abstract List<CheckboxMultipleCode> getExpectedListOfCodes();

    protected abstract Layout getLayout();

    @Override
    protected CertificateDataConfigType getType() {
        return CertificateDataConfigType.UE_CHECKBOX_MULTIPLE_CODE;
    }

    @Test
    void shouldIncludeExpectedListOfCodes() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
        assertIterableEquals(config.getList(), getExpectedListOfCodes());
    }

    @Test
    void shouldIncludeLayout() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxMultipleCode) question.getConfig();
        assertEquals(getLayout(), config.getLayout());
    }
}
