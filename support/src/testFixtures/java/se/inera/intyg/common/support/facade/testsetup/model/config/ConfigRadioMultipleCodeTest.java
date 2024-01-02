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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;

public abstract class ConfigRadioMultipleCodeTest extends ConfigTest {

    @Override
    protected CertificateDataConfigTypes getType() {
        return CertificateDataConfigTypes.UE_RADIO_MULTIPLE_CODE;
    }

    protected abstract List<RadioMultipleCode> getExpectedRadioMultipleCodes();

    protected abstract Layout getExpectedLayout();

    @Test
    void shouldIncludeExpectedRadioMultipleCode() {
        final var config = (CertificateDataConfigRadioMultipleCode) getElement().getConfig();
        assertIterableEquals(getExpectedRadioMultipleCodes(), config.getList());
    }

    @Test
    void shouldIncludeLayout() {
        var config = (CertificateDataConfigRadioMultipleCode) getElement().getConfig();
        var actualValue = config.getLayout();

        assertEquals(getExpectedLayout(), actualValue);
    }
}
