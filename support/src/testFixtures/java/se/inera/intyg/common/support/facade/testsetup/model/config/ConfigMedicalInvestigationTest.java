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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMedicalInvestigation;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.MedicalInvestigation;

public abstract class ConfigMedicalInvestigationTest extends ConfigTest {

    protected abstract String getText();
    protected abstract String getTypeText();
    protected abstract String getDateText();
    protected abstract String getInformationSourceText();
    protected abstract String getInformationSourceDescription();
    protected abstract List<MedicalInvestigation> getMedicalInvestigations();
    protected abstract List<LocalDate> getMaxDates();
    protected abstract List<LocalDate> getMinDates();

    @Override
    protected CertificateDataConfigTypes getType() {
        return CertificateDataConfigTypes.UE_MEDICAL_INVESTIGATION;
    }

    @Test
    void shouldIncludeTypeText() {
        final var config = getConfig();
        if (getLabelId() != null) {
            assertTrue(config.getTypeText().trim().length() > 0, "Missing type text");
            verify(getTextProviderMock(), atLeastOnce()).get(getTypeText());
        }
    }

    @Test
    void shouldIncludeDateText() {
        final var config = getConfig();
        if (getIconId() != null) {
            assertEquals(getDateText(), config.getDateText());
        }
    }

    @Test
    void shouldIncludeInformationSourceText() {
        final var config = getConfig();
        if (getLabelId() != null) {
            assertTrue(config.getInformationSourceText().trim().length() > 0, "Missing type text");
            verify(getTextProviderMock(), atLeastOnce()).get(getInformationSourceText());
        }
    }

    @Test
    void shouldIncludeInformationSourceDescription() {
        final var config = getConfig();
        if (getLabelId() != null) {
            assertTrue(config.getInformationSourceDescription().trim().length() > 0, "Missing type text");
            verify(getTextProviderMock(), atLeastOnce()).get(getInformationSourceDescription());
        }
    }

    @Test
    void shouldIncludeCorrectInvestigationTypeIds() {
        final var expectedTypeIds = getMedicalInvestigations().stream().map(MedicalInvestigation::getInvestigationTypeId)
            .collect(Collectors.toList());
        final var actualTypeIds = getConfig().getList().stream().map(MedicalInvestigation::getInvestigationTypeId)
            .collect(Collectors.toList());
        assertEquals(expectedTypeIds, actualTypeIds);
    }

    @Test
    void shouldIncludeCorrectDateIds() {
        final var expectedDateIds = getMedicalInvestigations().stream().map(MedicalInvestigation::getDateId).collect(Collectors.toList());
        final var actualDateIds = getConfig().getList().stream().map(MedicalInvestigation::getDateId).collect(Collectors.toList());
        assertEquals(expectedDateIds, actualDateIds);
    }

    @Test
    void shouldIncludeCorrectTypeOptions() {
        final var expectedTypeOptions = getMedicalInvestigations().stream().map(MedicalInvestigation::getTypeOptions)
            .collect(Collectors.toList());
        final var actualTypeOptions = getConfig().getList().stream().map(MedicalInvestigation::getTypeOptions).collect(Collectors.toList());
        assertEquals(expectedTypeOptions, actualTypeOptions);
    }

    @Test
    void shouldIncludeCorrectInformationSourceIds() {
        final var expectedTypeOptions = getMedicalInvestigations().stream().map(MedicalInvestigation::getInformationSourceId)
            .collect(Collectors.toList());
        final var actualTypeOptions = getConfig().getList().stream().map(MedicalInvestigation::getInformationSourceId)
            .collect(Collectors.toList());
        assertEquals(expectedTypeOptions, actualTypeOptions);
    }

    @Test
    void shouldIncludeMaxDate() {
        final var question = getElement();
        final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
        final var expectedMaxDates = getMaxDates();

        for (int i = 0; i < expectedMaxDates.size(); i++) {
            assertEquals(expectedMaxDates.get(i), config.getList().get(i).getMaxDate());
        }
    }

    @Test
    void shouldIncludeMinDate() {
        final var question = getElement();
        final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
        final var expectedMinDates = getMinDates();

        for (int i = 0; i < expectedMinDates.size(); i++) {
            assertEquals(expectedMinDates.get(i), config.getList().get(i).getMinDate());
        }
    }

    private CertificateDataConfigMedicalInvestigation getConfig() {
        final var question = getElement();
        return (CertificateDataConfigMedicalInvestigation) question.getConfig();
    }

}
