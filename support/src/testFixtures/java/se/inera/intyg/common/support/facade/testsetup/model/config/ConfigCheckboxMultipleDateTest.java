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
package se.inera.intyg.common.support.facade.testsetup.model.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;

public abstract class ConfigCheckboxMultipleDateTest extends ConfigTest {

    protected abstract List<CheckboxMultipleDate> getCheckboxMultipleDates();
    protected abstract List<LocalDate> getMaxDates();
    protected abstract List<LocalDate> getMinDates();

    @Override
    protected CertificateDataConfigTypes getType() {
        return CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_DATE;
    }

    @Test
    void shouldIncludeCorrectIds() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
        final var expectedIds = getCheckboxMultipleDates().stream()
            .map(CheckboxMultipleDate::getId).collect(Collectors.toList());
        final var actualIds = config.getList().stream()
            .map(CheckboxMultipleDate::getId).collect(Collectors.toList());

        assertEquals(expectedIds, actualIds);
    }

    @Test
    void shouldIncludeCorrectLabels() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
        final var expectedLabelIds = getCheckboxMultipleDates();
        final var actualLabels = config.getList();

        for (int i = 0; i < expectedLabelIds.size(); i++) {
            assertTrue(actualLabels.get(i).getLabel().trim().length() > 0, "Missing label at index " + i);
            verify(getTextProviderMock(), atLeastOnce()).get(expectedLabelIds.get(i).getLabel());
        }
    }

    @Test
    void shouldIncludeMaxDate() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
        final var expectedMaxDates = getMaxDates();

        for (int i = 0; i < expectedMaxDates.size(); i++) {
            assertEquals(expectedMaxDates.get(i), config.getList().get(i).getMaxDate());
        }
    }

    @Test
    void shouldIncludeMinDate() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxMultipleDate) question.getConfig();
        final var expectedMinDates = getMinDates();

        for (int i = 0; i < expectedMinDates.size(); i++) {
            assertEquals(expectedMinDates.get(i), config.getList().get(i).getMinDate());
        }
    }
}
