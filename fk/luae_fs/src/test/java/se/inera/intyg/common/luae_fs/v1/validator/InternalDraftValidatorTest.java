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
package se.inera.intyg.common.luae_fs.v1.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.schemas.contract.Personnummer;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Magnus Ekstrand on 2016-04-20.
 */
@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    private static final String PATIENT_PERSON_ID = "19121212-1212";
    private static final String SKAPADAV_PERSON_ID = "19010101-9801";
    private static final String SKAPADAV_PERSON_NAMN = "Torsten Ericsson";
    private static final String VARDGIVARE_ID = "vardgivareId";
    private static final String VARDGIVARE_NAMN = "vardgivareNamn";
    private static final String ENHET_ID = "enhetId";
    private static final String ENHET_NAMN = "enhetNamn";
    private static final String INTYG_ID = "intyg-1";

    @InjectMocks
    InternalDraftValidatorImpl validator;

    @InjectMocks
    ValidatorUtilFK validatorUtil;

    List<ValidationMessage> validationMessages;

    LuaefsUtlatandeV1.Builder builderTemplate;

    @Mock
    WebcertModuleService moduleService;

    @Before
    public void setUp() throws Exception {
        validationMessages = new ArrayList<>();

        builderTemplate = LuaefsUtlatandeV1.builder()
            .setId(INTYG_ID)
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .setTextVersion("");

        when(moduleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);

        // use reflection to set ValidatorUtilFK in InternalDraftValidator
        Field field = InternalDraftValidatorImpl.class.getDeclaredField("validatorUtilFK");
        field.setAccessible(true);
        field.set(validator, validatorUtil);
    }

    // Kategori 1 – Grund för medicinskt underlag

    @Test
    public void validateGrundForMU_Ok() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
            .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(2)))
            .setMotiveringTillInteBaseratPaUndersokning("behövs, ty ingen undersökning")
            .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(validationMessages.isEmpty());
    }

    @Test
    public void validateGrundForMU_IngenTypOchIngenKannedomOmPatient() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate.build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(2 == validationMessages.size());

        assertValidationMessageCategory("grundformu", 0);
        assertValidationMessageField("baseratPa", 0);
        assertValidationMessageCategory("grundformu", 1);
        assertValidationMessageField("kannedomOmPatient", 1);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 1);
    }

    @Test
    public void validateGrundForMU_IngenKannedomOmPatient() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
            .setMotiveringTillInteBaseratPaUndersokning("behövs, ty ingen undersökning")
            .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessageCategory("grundformu", 0);
        assertValidationMessageField("kannedomOmPatient", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    @Test
    public void validateGrundForMU_KannedomOmPatientEfterUndersokning() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setUndersokningAvPatienten(new InternalDate(LocalDate.now().minusDays(2)))
            .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(1)))
            .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.grund-for-mu.kannedom.after", 0);
        assertValidationMessageDynamicKey("KV_FKMU_0001.UNDERSOKNING.RBK", 0);
        assertValidationMessageType(ValidationMessageType.OTHER, 0);
    }

    @Test
    public void validateGrundForMU_KannedomOmPatientEfterAnhorigsBeskrivning() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now().minusDays(2)))
            .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(1)))
            .setMotiveringTillInteBaseratPaUndersokning("behövs, ty ingen undersökning")
            .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.grund-for-mu.kannedom.after", 0);
        assertValidationMessageDynamicKey("KV_FKMU_0001.ANHORIG.RBK", 0);
        assertValidationMessageType(ValidationMessageType.OTHER, 0);
    }

    @Test
    public void validateGrundForMU_OmAnnanGrundBeskrivningOchInteAnnanGrundDatum() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setAnnatGrundForMUBeskrivning("En beskrivning...")
            .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(1)))
            .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(2 == validationMessages.size());

        assertValidationMessageCategory("grundformu", 0);
        assertValidationMessageField("baseratPa", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
        assertValidationMessage("luae_fs.validation.grund-for-mu.annat.beskrivning.invalid_combination", 1);
        assertValidationMessageType(ValidationMessageType.EMPTY, 1);
    }

    @Test
    public void validateGrundForMU_OmAnnanGrundKraverBeskrivning() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setAnnatGrundForMU(new InternalDate(LocalDate.now().minusDays(2)))
            .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(1)))
            .setMotiveringTillInteBaseratPaUndersokning("behövs, ty ingen undersökning")
            .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessageCategory("grundformu", 0);
        assertValidationMessageField("annatGrundForMUBeskrivning", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    @Test
    public void validateGrundForMUKannedomOmPatientFramtidaDatum() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
            .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(2)))
            .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now().plusDays(5)))
            .setMotiveringTillInteBaseratPaUndersokning("behövs, ty ingen undersökning")
            .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertEquals(1, validationMessages.stream().filter(vm -> vm.getType() != ValidationMessageType.WARN).count());
        assertEquals(0, validationMessages.stream().filter(vm -> vm.getType() == ValidationMessageType.WARN).count());
    }

    // Kategori 2 - Andra medicinska utredningar och underlag

    @Test
    public void validateUnderlag_UnderlagFinnsInte() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate.build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessageCategory("grundformu", 0);
        assertValidationMessageField("underlagFinns", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    @Test
    public void validateUnderlag_UnderlagFinnsMenArTomt() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate.setUnderlagFinns(true).build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessageCategory("grundformu", 0);
        assertValidationMessageField("underlag", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    @Test
    public void validateUnderlag_UnderlagFinnsInteMenArIfyllt() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setUnderlagFinns(false)
            .setUnderlag(buildUnderlag("NEUROPSYKIATRISKT"))
            .build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.underlagfinns.incorrect_combination", 0);
        assertValidationMessageType(ValidationMessageType.INVALID_FORMAT, 0);
    }

    @Test
    public void validateUnderlag_GodkandaUnderlag() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setUnderlagFinns(true)
            .setUnderlag(buildUnderlag(
                "NEUROPSYKIATRISKT",
                "HABILITERING",
                "ARBETSTERAPEUT"))
            .build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(validationMessages.isEmpty());
    }

    @Test
    public void validateUnderlag_tooMany() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setUnderlagFinns(true)
            .setUnderlag(buildUnderlag(
                "NEUROPSYKIATRISKT",
                "HABILITERING",
                "ARBETSTERAPEUT",
                "FYSIOTERAPEUT",
                "LOGOPED",
                "PSYKOLOG",
                "SKOLHALSOVARD",
                "SPECIALISTKLINIK",
                "VARD_UTOMLANDS",
                "OVRIGT_UTLATANDE"))
            .build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertEquals(1, validationMessages.size());

        assertValidationMessage("luae_fs.validation.underlag.too_many", 0);
        assertValidationMessageType(ValidationMessageType.OTHER, 0);
    }

    @Test
    public void validateUnderlag_EjGodkantUnderlag() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setUnderlagFinns(true)
            .setUnderlag(buildUnderlag("FORETAGSHALSOVARD"))
            .build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.underlag.incorrect_format", 0);
        assertValidationMessageType(ValidationMessageType.INVALID_FORMAT, 0);
    }

    @Test
    public void validateUnderlag_UnderlagFinnsMenArFelaktigtIfyllt() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setUnderlagFinns(true)
            .setUnderlag(Arrays.asList(Underlag.create(Underlag.UnderlagsTyp.fromId("NEUROPSYKIATRISKT"), null, null)))
            .build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(2 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.underlag.date.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
        assertValidationMessage("luae_fs.validation.underlag.hamtas-fran.missing", 1);
        assertValidationMessageType(ValidationMessageType.EMPTY, 1);
    }

    // Kategori 3 - Diagnos

    @Test
    public void validateDiagnos_IngenDiagnos() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate.build();

        validator.validateDiagnose(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("common.validation.diagnos.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    @Test
    public void validateDiagnos_GodkandDiagnosKod() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setDiagnoser(buildDiagnoser("J22", "Z730", "F642"))
            .build();

        validator.validateDiagnose(utlatande, validationMessages);

        assertTrue(validationMessages.isEmpty());
    }

    @Test
    public void validateDiagnos_EjGodkandaDiagnosKoder() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setDiagnoser(buildDiagnoser("J2", "Z73", "F6"))
            .build();

        validator.validateDiagnose(utlatande, validationMessages);

        assertTrue(3 == validationMessages.size());

        assertValidationMessage("common.validation.diagnos.length-3", 0);
        assertValidationMessageType(ValidationMessageType.INVALID_FORMAT, 0);
        assertValidationMessage("common.validation.diagnos.psykisk.length-4", 1);
        assertValidationMessageType(ValidationMessageType.INVALID_FORMAT, 1);
        assertValidationMessage("common.validation.diagnos.psykisk.length-4", 2);
        assertValidationMessageType(ValidationMessageType.INVALID_FORMAT, 2);
    }

    @Test
    public void validateDiagnos_GodkandDiagnosKodMenIngenBeskrivning() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setDiagnoser(Arrays.asList(Diagnos.create("J22", "ICD-10-SE", null, "Ett namn...")))
            .build();

        validator.validateDiagnose(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("common.validation.diagnos.description.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    // Kategori 4 - Funktionsnedsättning

    @Test
    public void validateFunktionsnedsattning_DebutOchPaverkanFinns() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setFunktionsnedsattningDebut("Debut..")
            .setFunktionsnedsattningPaverkan("Paverkan..")
            .build();

        validator.validateFunktionsnedsattning(utlatande, validationMessages);

        assertTrue(validationMessages.isEmpty());
    }

    @Test
    public void validateFunktionsnedsattning_DebutOchPaverkanEjGiltiga() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setFunktionsnedsattningDebut("")
            .setFunktionsnedsattningPaverkan(null)
            .build();

        validator.validateFunktionsnedsattning(utlatande, validationMessages);

        assertTrue(2 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.funktionsnedsattning.debut.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
        assertValidationMessage("luae_fs.validation.funktionsnedsattning.paverkan.missing", 1);
        assertValidationMessageType(ValidationMessageType.EMPTY, 1);
    }

    // Kategori 5 - Övrigt
    // - Inga tester...

    // Kategori 6 - Kontakt

    @Test
    public void validateKontakt_KontaktOnskas() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setKontaktMedFk(true)
            .setAnledningTillKontakt("En anledning")
            .build();

        validator.validateKontakt(utlatande, validationMessages);

        assertTrue(validationMessages.isEmpty());
    }

    @Test
    public void validateKontakt_KontaktOnskasMenIngenAnledningIfylld() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setKontaktMedFk(true)
            .build();

        validator.validateKontakt(utlatande, validationMessages);

        assertTrue(validationMessages.isEmpty());
    }

    @Test
    public void validateKontakt_KontaktOnskasInteMenAnledningIfylld() throws Exception {
        LuaefsUtlatandeV1 utlatande = builderTemplate
            .setKontaktMedFk(false)
            .setAnledningTillKontakt("En andledning...")
            .build();

        validator.validateKontakt(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.kontakt.invalid_combination", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    // - - - Private scope - - -

    private void assertValidationMessage(String expectedMessage, int index) {
        assertEquals(expectedMessage, validationMessages.get(index).getMessage());
    }

    private void assertValidationMessageDynamicKey(String expectedDynamicKey, int index) {
        assertEquals(expectedDynamicKey, validationMessages.get(index).getDynamicKey());
    }

    private void assertValidationMessageCategory(String expectedCategory, int index) {
        assertEquals(expectedCategory, validationMessages.get(index).getCategory());
    }

    private void assertValidationMessageField(String expectedField, int index) {
        assertEquals(expectedField, validationMessages.get(index).getField());
    }

    private void assertValidationMessageType(ValidationMessageType expectedType, int index) {
        assertTrue(expectedType == validationMessages.get(index).getType());
    }

    private List<Diagnos> buildDiagnoser(String... diagnosKoder) {
        List<Diagnos> diagnoser = new ArrayList<>();

        for (String kod : diagnosKoder) {
            diagnoser.add(Diagnos.create(kod, "ICD-10-SE", "En beskrivning...", "Ett namn..."));
        }

        return diagnoser;
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(VARDGIVARE_ID);
        vardgivare.setVardgivarnamn(VARDGIVARE_NAMN);

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(ENHET_ID);
        vardenhet.setEnhetsnamn(ENHET_NAMN);
        vardenhet.setVardgivare(vardgivare);

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId(SKAPADAV_PERSON_ID);
        skapadAv.setFullstandigtNamn(SKAPADAV_PERSON_NAMN);

        Patient patient = new Patient();
        patient.setPersonId(createPnr(PATIENT_PERSON_ID));

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }

    private List<Underlag> buildUnderlag(String... underlagsTyper) {
        List<Underlag> underlag = new ArrayList<>();

        for (String typ : underlagsTyper) {
            underlag.add(Underlag.create(Underlag.UnderlagsTyp.fromId(typ), new InternalDate(LocalDate.now()), "Hamtas fran..."));
        }

        return underlag;
    }

    private Personnummer createPnr(String civicRegistrationNumber) {
        return Personnummer.createPersonnummer(civicRegistrationNumber).get();
    }

}
