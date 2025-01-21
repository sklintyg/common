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
package se.inera.intyg.common.fkparent.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK.GrundForMu;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorUtilFKTest {

    private static final String VALID_CODE_1 = "A00";
    private static final String ICD_CODE_SYSTEM = "ICD";
    private static final String VALID_CODE_2 = "A01";
    private static final String KSH_CODE_SYSTEM = "KSH";
    private static final String INVALID_CODE = "sdfds";
    private static final String INVALID_CODE_DOT = "U07.1";

    @Mock
    private WebcertModuleService moduleService;

    @InjectMocks
    private ValidatorUtilFK validatorUtil = mock(ValidatorUtilFK.class, Mockito.CALLS_REAL_METHODS);

    @Before
    public void setup() {
        //Valid with ICD
        when(moduleService.validateDiagnosisCode(VALID_CODE_1, ICD_CODE_SYSTEM)).thenReturn(true);
        when(moduleService.validateDiagnosisCodeFormat(VALID_CODE_1)).thenReturn(true);
        when(moduleService.validateDiagnosisCode(VALID_CODE_2, ICD_CODE_SYSTEM)).thenReturn(true);
        when(moduleService.validateDiagnosisCodeFormat(VALID_CODE_2)).thenReturn(true);

        //Valid with KSH
        when(moduleService.validateDiagnosisCode(VALID_CODE_2, KSH_CODE_SYSTEM)).thenReturn(true);

        when(moduleService.validateDiagnosisCode(eq(INVALID_CODE), anyString())).thenReturn(false);
        when(moduleService.validateDiagnosisCodeFormat(INVALID_CODE)).thenReturn(true);

        when(moduleService.validateDiagnosisCodeFormat(INVALID_CODE_DOT)).thenReturn(false);
    }

    @Test
    public void testDiagnosesAreValid() {
        List<Diagnos> source = Arrays
            .asList(buildDiagnos(VALID_CODE_1, ICD_CODE_SYSTEM, "besk"), buildDiagnos(VALID_CODE_2, ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertTrue(validationMessages.isEmpty());
        verify(moduleService).validateDiagnosisCode(VALID_CODE_1, ICD_CODE_SYSTEM);
        verify(moduleService).validateDiagnosisCode(VALID_CODE_2, ICD_CODE_SYSTEM);
    }

    @Test
    public void testDiagnoesMustHaveSameCodeSystem() {
        List<Diagnos> source = Arrays
            .asList(buildDiagnos(VALID_CODE_1, ICD_CODE_SYSTEM, "besk"), buildDiagnos(VALID_CODE_2, KSH_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        // The codes are valid in themselves, but the combination is not.
        assertTrue(validationMessages.size() == 1);
        verify(moduleService).validateDiagnosisCode(VALID_CODE_1, ICD_CODE_SYSTEM);
        verify(moduleService).validateDiagnosisCode(VALID_CODE_2, KSH_CODE_SYSTEM);
    }

    @Test
    public void testDiagnosesAreValidFalse() {
        List<Diagnos> source = Arrays
            .asList(buildDiagnos(INVALID_CODE, ICD_CODE_SYSTEM, "besk"), buildDiagnos(VALID_CODE_2, ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser[0].diagnoskod", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.invalid", validationMessages.get(0).getMessage());
        verify(moduleService).validateDiagnosisCode(INVALID_CODE, ICD_CODE_SYSTEM);
        verify(moduleService).validateDiagnosisCode(VALID_CODE_2, ICD_CODE_SYSTEM);
    }

    @Test
    public void testDiagnosesOneInvalid() {
        List<Diagnos> source = Arrays
            .asList(buildDiagnos(VALID_CODE_1, ICD_CODE_SYSTEM, "besk"),
                buildDiagnos(INVALID_CODE_DOT, ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser[1].diagnoskod", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.invalid", validationMessages.get(0).getMessage());
        verify(moduleService).validateDiagnosisCodeFormat(VALID_CODE_1);
        verify(moduleService).validateDiagnosisCode(VALID_CODE_1, ICD_CODE_SYSTEM);
        verify(moduleService).validateDiagnosisCodeFormat(INVALID_CODE_DOT);
    }

    @Test
    public void testDiagnosesAreValidNull() {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(null, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.missing", validationMessages.get(0).getMessage());
        verifyNoInteractions(moduleService);
    }

    @Test
    public void testDiagnosesAreValidEmptyList() {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(new ArrayList<>(), validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.missing", validationMessages.get(0).getMessage());
        verifyNoInteractions(moduleService);
    }

    @Test
    public void testDiagnosesAreValidDiagnoskodMissing() {
        List<Diagnos> source = Arrays.asList(buildDiagnos(null, ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser[0].diagnoskod", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.codemissing", validationMessages.get(0).getMessage());
        verifyNoInteractions(moduleService);
    }

    @Test
    public void testDiagnosesAreValidDiagnosBeskrivingMissing() {
        List<Diagnos> source = Arrays.asList(buildDiagnos(VALID_CODE_1, ICD_CODE_SYSTEM, null));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser[0].diagnoskod", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.description.missing", validationMessages.get(0).getMessage());
        verify(moduleService).validateDiagnosisCode(VALID_CODE_1, ICD_CODE_SYSTEM);
    }

    @Test
    public void testDiagnosesAreValidZ73TooShort() {
        List<Diagnos> source = Arrays.asList(buildDiagnos("Z73", ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser[0].diagnoskod", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.psykisk.length-4", validationMessages.get(0).getMessage());
        verifyNoInteractions(moduleService);
    }

    @Test
    public void testDiagnosesAreValidFTooShort() {
        List<Diagnos> source = Arrays.asList(buildDiagnos("f3", ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser[0].diagnoskod", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.psykisk.length-4", validationMessages.get(0).getMessage());
        verifyNoInteractions(moduleService);
    }

    @Test
    public void testDiagnosesAreValidTooShort() {
        List<Diagnos> source = Arrays.asList(buildDiagnos("A0", ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser[0].diagnoskod", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.length-3", validationMessages.get(0).getMessage());
        verifyNoInteractions(moduleService);
    }

    @Test
    public void testDiagnosesAreValidTooLong() {
        List<Diagnos> source = Arrays.asList(buildDiagnos("A02345", ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertEquals(1, validationMessages.size());
        assertEquals("diagnos", validationMessages.get(0).getCategory());
        assertEquals("diagnoser[0].diagnoskod", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, validationMessages.get(0).getType());
        assertEquals("common.validation.diagnos.length-5", validationMessages.get(0).getMessage());
        verifyNoInteractions(moduleService);
    }

    @Test
    public void testDiagnosesAreValidModuleServiceMissing() {
        ReflectionTestUtils.setField(validatorUtil, "moduleService", null);
        List<Diagnos> source = Arrays
            .asList(buildDiagnos(VALID_CODE_1, ICD_CODE_SYSTEM, "besk"), buildDiagnos(VALID_CODE_2, ICD_CODE_SYSTEM, "besk"));
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorUtil.validateDiagnose(source, validationMessages);

        assertTrue(validationMessages.isEmpty());
        verifyNoInteractions(moduleService);
    }

    @Test
    public void testValidateGrundForMuDate() {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        ValidatorUtilFK.validateGrundForMuDate(new InternalDate(LocalDate.now()), validationMessages, GrundForMu.TELEFONKONTAKT);

        assertTrue(validationMessages.isEmpty());
    }

    @Test
    public void testValidateGrundForMuDateInvalid() {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        ValidatorUtilFK.validateGrundForMuDate(new InternalDate("invalidDate"), validationMessages, GrundForMu.JOURNALUPPGIFTER);

        assertEquals(1, validationMessages.size());
        assertEquals("grundformu", validationMessages.get(0).getCategory());
        assertEquals("journaluppgifter", validationMessages.get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, validationMessages.get(0).getType());
    }

    private Diagnos buildDiagnos(String code, String codeSystem, String description) {
        return Diagnos.create(code, codeSystem, description, null);
    }
}
