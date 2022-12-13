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

package se.inera.intyg.common.support.facade.testsetup.model.config;

import static com.helger.commons.mock.CommonsAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.DiagnosesTerminology;

public abstract class ConfigDiagnosTest extends ConfigTest{

    protected abstract HashMap<String,String> getExpectedIdAndLabel();
    protected abstract List<String> getExpectedListIds();

    @Override
    protected CertificateDataConfigTypes getType() {
        return CertificateDataConfigTypes.UE_DIAGNOSES;
    }

    @Test
    void terminology() {
        var certificateDataConfigDiagnoses = (CertificateDataConfigDiagnoses) getElement().getConfig();
        var terminology = certificateDataConfigDiagnoses.getTerminology();
        assertAll(
            () -> {
                for (DiagnosesTerminology diagnosesTerminology: terminology) {
                    assertTrue(getExpectedIdAndLabel().containsKey(diagnosesTerminology.getId()));
                    assertEquals(diagnosesTerminology.getLabel(), getExpectedIdAndLabel().get(diagnosesTerminology.getId()));
                }
            }
        );
    }

    @Test
    void listId() {
        var certificateDataConfigDiagnoses = (CertificateDataConfigDiagnoses) getElement().getConfig();
        var list = certificateDataConfigDiagnoses.getList();
        assertAll(
            () -> {
                assertEquals(list.size(), getExpectedListIds().size());
                for (int i = 0; i < list.size(); i++) {
                    assertEquals(list.get(i).getId(), getExpectedListIds().get(i));
                }
            }
        );
    }
}
