/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.fkparent.model.converter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InternalToTransportUtilTest {

    private static final String DIAGNOSIS_CODE_S666 = "S666";
    private static final String DIAGNOSIS_CODE_Z731 = "Z731";
    private static final String DIAGNOSIS_CODE_A039 = "A039";
    private static final String DIAGNOSIS_CODE_A00DASH = "A00-";
    private static final String DIAGNOSIS_CODE_INVALID = "INVALID";
    private static final String DIAGNOSIS_CODE_INVALID_DOT = "U07.1";

    @Mock
    private WebcertModuleService webcertModuleService;

    @Before
    public void setup() {
        when(webcertModuleService.validateDiagnosisCode(eq(DIAGNOSIS_CODE_S666), anyString())).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(DIAGNOSIS_CODE_S666)).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCode(eq(DIAGNOSIS_CODE_Z731), anyString())).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(DIAGNOSIS_CODE_Z731)).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCode(eq(DIAGNOSIS_CODE_A039), anyString())).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(DIAGNOSIS_CODE_A039)).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCode(eq(DIAGNOSIS_CODE_A00DASH), anyString())).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(DIAGNOSIS_CODE_A00DASH)).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCode(eq(DIAGNOSIS_CODE_INVALID), anyString())).thenReturn(false);
        when(webcertModuleService.validateDiagnosisCodeFormat(DIAGNOSIS_CODE_INVALID)).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(DIAGNOSIS_CODE_INVALID_DOT)).thenReturn(false);
    }

    @Test
    public void handleDiagnosSvarTest() throws Exception {
        final String diagnosKod1 = DIAGNOSIS_CODE_S666;
        final String diagnosKodSystem1 = "ICD_10_SE";
        final String diagnosBeskrivning1 = "Skada på multipla böjmuskler och deras senor på handleds- och handnivå";
        final String diagnosDisplayName1 = "displayName1";
        final String diagnosKod2 = DIAGNOSIS_CODE_Z731;
        final String diagnosKodSystem2 = "ICD_10_SE";
        final String diagnosBeskrivning2 = "Accentuering av personlighetsdrag";
        final String diagnosDisplayName2 = "displayName2";
        final String diagnosKod3 = "A039";
        final String diagnosKodSystem3 = "ICD_10_SE";
        final String diagnosBeskrivning3 = "Shigellos, ospecificerad";
        final String diagnosDisplayName3 = "displayName3";

        List<Svar> res = new ArrayList<>();
        List<Diagnos> internal = new ArrayList<>();
        internal.add(Diagnos.create(diagnosKod1, diagnosKodSystem1, diagnosBeskrivning1, diagnosDisplayName1));
        internal.add(Diagnos.create(diagnosKod2, diagnosKodSystem2, diagnosBeskrivning2, diagnosDisplayName2));
        internal.add(Diagnos.create(diagnosKod3, diagnosKodSystem3, diagnosBeskrivning3, diagnosDisplayName3));

        InternalToTransportUtil.handleDiagnosSvar(res, internal, webcertModuleService);

        assertEquals(1, res.size());
        assertEquals("6", res.get(0).getId());
        assertNull(res.get(0).getInstans());
        assertEquals(6, res.get(0).getDelsvar().size());
        assertEquals("6.2", res.get(0).getDelsvar().get(0).getId());
        CVType diagnos1 = TransportConverterUtil.getCVSvarContent(res.get(0).getDelsvar().get(0));
        assertEquals(diagnosKod1, diagnos1.getCode());
        assertEquals("1.2.752.116.1.1.1.1.3", diagnos1.getCodeSystem());
        assertEquals(diagnosDisplayName1, diagnos1.getDisplayName());
        assertEquals("6.1", res.get(0).getDelsvar().get(1).getId());
        assertEquals(diagnosBeskrivning1, TransportConverterUtil.getStringContent(res.get(0).getDelsvar().get(1)));
        assertEquals("6.4", res.get(0).getDelsvar().get(2).getId());
        CVType diagnos2 = TransportConverterUtil.getCVSvarContent(res.get(0).getDelsvar().get(2));
        assertEquals(diagnosKod2, diagnos2.getCode());
        assertEquals("1.2.752.116.1.1.1.1.3", diagnos2.getCodeSystem());
        assertEquals(diagnosDisplayName2, diagnos2.getDisplayName());
        assertEquals("6.3", res.get(0).getDelsvar().get(3).getId());
        assertEquals(diagnosBeskrivning2, TransportConverterUtil.getStringContent(res.get(0).getDelsvar().get(3)));
        assertEquals("6.6", res.get(0).getDelsvar().get(4).getId());
        CVType diagnos3 = TransportConverterUtil.getCVSvarContent(res.get(0).getDelsvar().get(4));
        assertEquals(diagnosKod3, diagnos3.getCode());
        assertEquals("1.2.752.116.1.1.1.1.3", diagnos3.getCodeSystem());
        assertEquals(diagnosDisplayName3, diagnos3.getDisplayName());
        assertEquals("6.5", res.get(0).getDelsvar().get(5).getId());
        assertEquals(diagnosBeskrivning3, TransportConverterUtil.getStringContent(res.get(0).getDelsvar().get(5)));
    }

    @Test
    public void handleDiagnosSvarTwoDiagnoses() throws Exception {
        final String diagnosKod1 = DIAGNOSIS_CODE_S666;
        final String diagnosKodSystem1 = "ICD_10_SE";
        final String diagnosBeskrivning1 = "Skada på multipla böjmuskler och deras senor på handleds- och handnivå";
        final String diagnosDisplayName1 = "displayName1";
        final String diagnosKod2 = DIAGNOSIS_CODE_Z731;
        final String diagnosKodSystem2 = "ICD_10_SE";
        final String diagnosBeskrivning2 = "Accentuering av personlighetsdrag";
        final String diagnosDisplayName2 = "displayName2";

        List<Svar> res = new ArrayList<>();
        List<Diagnos> internal = new ArrayList<>();
        internal.add(Diagnos.create(diagnosKod1, diagnosKodSystem1, diagnosBeskrivning1, diagnosDisplayName1));
        internal.add(Diagnos.create(diagnosKod2, diagnosKodSystem2, diagnosBeskrivning2, diagnosDisplayName2));

        InternalToTransportUtil.handleDiagnosSvar(res, internal, webcertModuleService);

        assertEquals(1, res.size());
        assertEquals("6", res.get(0).getId());
        assertNull(res.get(0).getInstans());
        assertEquals(4, res.get(0).getDelsvar().size());
        assertEquals("6.2", res.get(0).getDelsvar().get(0).getId());
        CVType diagnos1 = TransportConverterUtil.getCVSvarContent(res.get(0).getDelsvar().get(0));
        assertEquals(diagnosKod1, diagnos1.getCode());
        assertEquals("1.2.752.116.1.1.1.1.3", diagnos1.getCodeSystem());
        assertEquals(diagnosDisplayName1, diagnos1.getDisplayName());
        assertEquals("6.1", res.get(0).getDelsvar().get(1).getId());
        assertEquals(diagnosBeskrivning1, TransportConverterUtil.getStringContent(res.get(0).getDelsvar().get(1)));
        assertEquals("6.4", res.get(0).getDelsvar().get(2).getId());
        CVType diagnos2 = TransportConverterUtil.getCVSvarContent(res.get(0).getDelsvar().get(2));
        assertEquals(diagnosKod2, diagnos2.getCode());
        assertEquals("1.2.752.116.1.1.1.1.3", diagnos2.getCodeSystem());
        assertEquals(diagnosDisplayName2, diagnos2.getDisplayName());
        assertEquals("6.3", res.get(0).getDelsvar().get(3).getId());
        assertEquals(diagnosBeskrivning2, TransportConverterUtil.getStringContent(res.get(0).getDelsvar().get(3)));
    }

    @Test
    public void handleDiagnosSvarOneDiagnosis() throws Exception {
        final String diagnosKod1 = DIAGNOSIS_CODE_S666;
        final String diagnosKodSystem1 = "ICD_10_SE";
        final String diagnosBeskrivning1 = "Skada på multipla böjmuskler och deras senor på handleds- och handnivå";
        final String diagnosDisplayName1 = "displayName1";

        List<Svar> res = new ArrayList<>();
        List<Diagnos> internal = new ArrayList<>();
        internal.add(Diagnos.create(diagnosKod1, diagnosKodSystem1, diagnosBeskrivning1, diagnosDisplayName1));

        InternalToTransportUtil.handleDiagnosSvar(res, internal, webcertModuleService);

        assertEquals(1, res.size());
        assertEquals("6", res.get(0).getId());
        assertNull(res.get(0).getInstans());
        assertEquals(2, res.get(0).getDelsvar().size());
        assertEquals("6.2", res.get(0).getDelsvar().get(0).getId());
        CVType diagnos1 = TransportConverterUtil.getCVSvarContent(res.get(0).getDelsvar().get(0));
        assertEquals(diagnosKod1, diagnos1.getCode());
        assertEquals("1.2.752.116.1.1.1.1.3", diagnos1.getCodeSystem());
        assertEquals(diagnosDisplayName1, diagnos1.getDisplayName());
        assertEquals("6.1", res.get(0).getDelsvar().get(1).getId());
        assertEquals(diagnosBeskrivning1, TransportConverterUtil.getStringContent(res.get(0).getDelsvar().get(1)));
    }

    @Test
    public void handleDiagnosSvarKSH97P() throws Exception {
        final String diagnosKod1 = DIAGNOSIS_CODE_A00DASH;
        final String diagnosKodSystem1 = "KSH_97_P";
        final String diagnosBeskrivning1 = "Kolera";
        final String diagnosDisplayName1 = "displayName1";

        List<Svar> res = new ArrayList<>();
        List<Diagnos> internal = new ArrayList<>();
        internal.add(Diagnos.create(diagnosKod1, diagnosKodSystem1, diagnosBeskrivning1, diagnosDisplayName1));

        InternalToTransportUtil.handleDiagnosSvar(res, internal, webcertModuleService);

        assertEquals(1, res.size());
        assertEquals("6", res.get(0).getId());
        assertNull(res.get(0).getInstans());
        assertEquals(2, res.get(0).getDelsvar().size());
        assertEquals("6.2", res.get(0).getDelsvar().get(0).getId());
        CVType diagnos1 = TransportConverterUtil.getCVSvarContent(res.get(0).getDelsvar().get(0));
        assertEquals(diagnosKod1, diagnos1.getCode());
        assertEquals("1.2.752.116.1.3.1.4.1", diagnos1.getCodeSystem());
        assertEquals(diagnosDisplayName1, diagnos1.getDisplayName());
        assertEquals("6.1", res.get(0).getDelsvar().get(1).getId());
        assertEquals(diagnosBeskrivning1, TransportConverterUtil.getStringContent(res.get(0).getDelsvar().get(1)));
    }

    @Test
    public void handleDiagnosSvarEmpty() {
        List<Svar> res = new ArrayList<>();
        List<Diagnos> internal = new ArrayList<>();

        InternalToTransportUtil.handleDiagnosSvar(res, internal, webcertModuleService);

        assertTrue(res.isEmpty());
    }

    @Test
    public void handleDiagnosSvarInvalidDiagnose() {
        final String diagnosKod1 = DIAGNOSIS_CODE_INVALID;
        final String diagnosKodSystem1 = "KSH_97_P";
        final String diagnosBeskrivning1 = "Kolera";
        final String diagnosDisplayName1 = "displayName1";

        List<Svar> res = new ArrayList<>();
        List<Diagnos> internal = new ArrayList<>();
        internal.add(Diagnos.create(diagnosKod1, diagnosKodSystem1, diagnosBeskrivning1, diagnosDisplayName1));

        InternalToTransportUtil.handleDiagnosSvar(res, internal, webcertModuleService);

        assertEquals(0, res.size());
    }

    @Test
    public void handleDiagnosSvarTwoDiagnosesOneInvalid() throws Exception {
        final String diagnosKod1 = DIAGNOSIS_CODE_S666;
        final String diagnosKodSystem1 = "ICD_10_SE";
        final String diagnosBeskrivning1 = "Skada på multipla böjmuskler och deras senor på handleds- och handnivå";
        final String diagnosDisplayName1 = "displayName1";
        final String diagnosKod2 = DIAGNOSIS_CODE_INVALID_DOT;
        final String diagnosKodSystem2 = "ICD_10_SE";
        final String diagnosBeskrivning2 = "Accentuering av personlighetsdrag";
        final String diagnosDisplayName2 = "displayName2";

        List<Svar> res = new ArrayList<>();
        List<Diagnos> internal = new ArrayList<>();
        internal.add(Diagnos.create(diagnosKod1, diagnosKodSystem1, diagnosBeskrivning1, diagnosDisplayName1));
        internal.add(Diagnos.create(diagnosKod2, diagnosKodSystem2, diagnosBeskrivning2, diagnosDisplayName2));

        InternalToTransportUtil.handleDiagnosSvar(res, internal, webcertModuleService);

        assertEquals(1, res.size());
        assertEquals("6", res.get(0).getId());
        assertNull(res.get(0).getInstans());
        assertEquals(2, res.get(0).getDelsvar().size());
        assertEquals("6.2", res.get(0).getDelsvar().get(0).getId());
        CVType diagnos1 = TransportConverterUtil.getCVSvarContent(res.get(0).getDelsvar().get(0));
        assertEquals(diagnosKod1, diagnos1.getCode());
        assertEquals("1.2.752.116.1.1.1.1.3", diagnos1.getCodeSystem());
        assertEquals(diagnosDisplayName1, diagnos1.getDisplayName());
        assertEquals("6.1", res.get(0).getDelsvar().get(1).getId());
        assertEquals(diagnosBeskrivning1, TransportConverterUtil.getStringContent(res.get(0).getDelsvar().get(1)));
    }

}
