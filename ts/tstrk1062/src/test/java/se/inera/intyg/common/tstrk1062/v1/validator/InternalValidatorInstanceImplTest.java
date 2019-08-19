/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1062.v1.validator;

import static org.junit.Assert.*;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.tstrk1062.v1.model.internal.*;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorInstanceImplTest {

    @InjectMocks
    InternalValidatorInstanceImpl validator;

    private TsTrk1062UtlatandeV1.Builder builderTemplate;

    @Before
    public void setUp() {
        builderTemplate = TsTrk1062UtlatandeV1.builder()
                .setId("intygsId")
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvser.BehorighetsTyp.IAV11)))
                .setIdKontroll(IdKontroll.create(IdKontrollKod.KORKORT))
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("Diagnoser", "2017"))
                .setLakemedelsbehandling(Lakemedelsbehandling.create(false, false, "", false, false, false, null, ""))
                .setBedomningAvSymptom("Bedömning av symptom")
                .setPrognosTillstand(PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.JA))
                .setOvrigaKommentarer(null)
                .setBedomning(Bedomning.builder().setUppfyllerBehorighetskrav(EnumSet.of(Bedomning.BehorighetsTyp.VAR11)).build())
                .setTextVersion("");

    }

    @Test
    public void validateDraft() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate.build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);

        assertFalse("Shouldn't have error messages", validateDraftResponse.hasErrorMessages());
    }

    @Test
    public void validateNullIntygAvser() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setIntygAvser(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, INTYG_AVSER_CATEGORY, INTYG_AVSER_SVAR_JSON_ID);
    }

    @Test
    public void validateMissingIntygAvser() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setIntygAvser(IntygAvser.create(EnumSet.noneOf(IntygAvser.BehorighetsTyp.class)))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, INTYG_AVSER_CATEGORY,
                INTYG_AVSER_SVAR_JSON_ID + PUNKT + INTYG_AVSER_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullIdKontroll() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setIdKontroll(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ID_KONTROLL_CATEGORY,
                ID_KONTROLL_SVAR_JSON_ID + PUNKT + ID_KONTROLL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosRegistrering() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ALLMANT_KATEGORI,
                ALLMANT_INMATNING_SVAR_JSON_ID + PUNKT + ALLMANT_INMATNING_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosRegistreringTyp() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ALLMANT_KATEGORI,
                ALLMANT_INMATNING_SVAR_JSON_ID + PUNKT + ALLMANT_INMATNING_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosFritext() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosFritextValues() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create(null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have two error messages", 2, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyDiagnosFritextValues() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("", ""))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have two error messages", 2, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateInvalidDiagnosFritextYear() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("Diagnoser", "Årtal"))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ValidationMessageType.INVALID_FORMAT, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateFutureDiagnosFritextYear() {
        final String futureYear = Integer.toString(LocalDate.now().plusYears(1).getYear());

        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
                .setDiagnosFritext(DiagnosFritext.create("Diagnoser", futureYear))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ValidationMessageType.OTHER, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnosKodad() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID);
    }

    @Test
    public void validateEmptyDiagnosKodad() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(new ArrayList<>())
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID);
    }

    @Test
    public void validateNullDiagnos() {
        final DiagnosKodad diagnosKodad = DiagnosKodad.create(null,
                null, null, null, null);

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<>(1);
        diagnosKodadList.add(diagnosKodad);

        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have three error messages", 3, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_KODSYSTEM_JSON_ID);
    }

    @Test
    public void validateEmptyDiagnos() {
        final DiagnosKodad diagnosKodad = DiagnosKodad.create("", "",
                "", "", "");

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<>(1);
        diagnosKodadList.add(diagnosKodad);

        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have three error messages", 3, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_KODSYSTEM_JSON_ID);
    }

    @Test
    public void validateFirstDiagnosMissing() {
        final DiagnosKodad diagnosKodadOne = DiagnosKodad.create("", "",
                "", "", "");

        final DiagnosKodad diagnosKodadTwo = DiagnosKodad.create("A01", "ICD10",
                "Diagnosbeskrivning", "A01 - Diagnosbeskrivning", "2019");

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<>(2);
        diagnosKodadList.add(diagnosKodadOne);
        diagnosKodadList.add(diagnosKodadTwo);

        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertOneValidationMessages(validationMessages, ValidationMessageType.INCORRECT_COMBINATION, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID);
    }

    @Test
    public void validateFirstDiagnosMissingAndSecondInvalid() {
        final DiagnosKodad diagnosKodadOne = DiagnosKodad.create("", "",
                "", "", "");

        final DiagnosKodad diagnosKodadTwo = DiagnosKodad.create("A01", "ICD10",
                "Diagnosbeskrivning", "A01 - Diagnosbeskrivning", "Årtal");

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<>(2);
        diagnosKodadList.add(diagnosKodadOne);
        diagnosKodadList.add(diagnosKodadTwo);

        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertEquals("Should have two error messages", 2, validationMessages.size());

        final Map<String, ValidationMessage> validationsMap = buildMapFromMessages(validationMessages);

        assertValidationMessage(validationsMap, ValidationMessageType.INCORRECT_COMBINATION, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID);

        assertValidationMessage(validationsMap, ValidationMessageType.INVALID_FORMAT, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[1]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateInvalidDiagnosArtal() {
        final DiagnosKodad diagnosKodad = DiagnosKodad.create("A01", "ICD10",
                "Diagnosbeskrivning", "A01 - Diagnosbeskrivning", "Årtal");

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<>(1);
        diagnosKodadList.add(diagnosKodad);

        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ValidationMessageType.INVALID_FORMAT, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateFutureDiagnosArtal() {
        final String futureYear = Integer.toString(LocalDate.now().plusYears(1).getYear());

        final DiagnosKodad diagnosKodad = DiagnosKodad.create("A01", "ICD10",
                "Diagnosbeskrivning", "A01 - Diagnosbeskrivning", futureYear);

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<>(1);
        diagnosKodadList.add(diagnosKodad);

        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
                .setDiagnosKodad(diagnosKodadList)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, ValidationMessageType.OTHER, ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + "[0]" + PUNKT + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandling() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingHarHaft() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(null, null, null, null,
                        null, null, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingPagar() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, null, null, null,
                        null, null, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingAktuell() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, null, false,
                        false, false, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyLakemedelsbehandlingAktuell() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "", false,
                        false, false, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingPagatt() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "Aktuell", null,
                        false, false, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingEffekt() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "Aktuell", false,
                        null, false, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingFoljsamhet() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "Aktuell", false,
                        false, null, null, null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingAvslutadTidpunkt() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null,
                        null, null, null, "Orsak"))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyLakemedelsbehandlingAvslutadTidpunkt() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null,
                        null, null, "", "Orsak"))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullLakemedelsbehandlingAvslutadOrsak() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null,
                        null, null, "Förra året", null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyLakemedelsbehandlingAvslutadOrsak() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null,
                        null, null, "Förra året", ""))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullBedomningAvSymptom() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomningAvSymptom(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, SYMPTOM_KATEGORI, SYMPTOM_BEDOMNING_DELSVAR_JSON_ID);
    }

    @Test
    public void validateEmptyBedomningAvSymptom() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomningAvSymptom(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, SYMPTOM_KATEGORI, SYMPTOM_BEDOMNING_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullPrognos() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setPrognosTillstand(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, SYMPTOM_KATEGORI,
                SYMPTOM_PROGNOS_SVAR_JSON_ID + PUNKT + SYMPTOM_PROGNOS_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullPrognosType() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setPrognosTillstand(PrognosTillstand.create(null))
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessages = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessages, SYMPTOM_KATEGORI,
                SYMPTOM_PROGNOS_SVAR_JSON_ID + PUNKT + SYMPTOM_PROGNOS_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullBedomning() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomning(null)
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID);
    }

    @Test
    public void validateNullBedomningBehorighetsKrav() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomning(Bedomning.builder().build())
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID);
    }

    @Test
    public void validateMissingBedomning() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomning(Bedomning.builder()
                        .setUppfyllerBehorighetskrav(EnumSet.noneOf(Bedomning.BehorighetsTyp.class))
                        .build())
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID);
    }

    @Test
    public void validateInvalidBedomning() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
                .setBedomning(Bedomning.builder()
                        .setUppfyllerBehorighetskrav(EnumSet.of(Bedomning.BehorighetsTyp.VAR6,
                                Bedomning.BehorighetsTyp.VAR11))
                        .build())
                .build();

        final ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatande);
        final List<ValidationMessage> validationMessage = validateDraftResponse.getValidationErrors();

        assertOneValidationMessages(validationMessage, ValidationMessageType.INCORRECT_COMBINATION, BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID);
    }

    private Map<String, ValidationMessage> buildMapFromMessages(List<ValidationMessage> validationMessages) {
        final Map<String, ValidationMessage> validationsMap = new HashMap<>(validationMessages.size());
        for (ValidationMessage validationMessage : validationMessages) {
            validationsMap.put(validationMessage.getField(), validationMessage);
        }
        return validationsMap;
    }

    private void assertValidationMessage(Map<String, ValidationMessage> validationMessageMap, String category, String field) {
        assertValidationMessage(validationMessageMap, ValidationMessageType.EMPTY, category, field);
    }

    private void assertValidationMessage(Map<String, ValidationMessage> validationMessageMap, ValidationMessageType validationMessageType,
            String category, String field) {
        final ValidationMessage validationMessage = validationMessageMap.get(field);
        assertNotNull("Missing message for " + field, validationMessage);
        assertValidationMessage(validationMessage, validationMessageType, category, field);
    }

    private void assertOneValidationMessages(List<ValidationMessage> validationMessage, String category, String field) {
        assertOneValidationMessages(validationMessage, ValidationMessageType.EMPTY, category, field);
    }

    private void assertOneValidationMessages(List<ValidationMessage> validationMessage, ValidationMessageType validationMessageType,
            String category, String field) {
        assertEquals("Should have error messages", 1, validationMessage.size());
        assertValidationMessage(validationMessage.get(0), validationMessageType, category, field);
    }

    private void assertValidationMessage(ValidationMessage validationMessage, ValidationMessageType validationMessageType, String category,
            String field) {
        assertEquals("Should have Empty message type", validationMessageType, validationMessage.getType());
        assertEquals("Should have category: " + category, category, validationMessage.getCategory());
        assertEquals("Should have field: " + field, field, validationMessage.getField());
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("vardgivareNamn");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("0112312313");

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId("HSAID_123");
        skapadAv.setFullstandigtNamn("Torsten Ericsson");

        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        patient.setPostadress("postadress");
        patient.setPostnummer("11111");
        patient.setPostort("postort");

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }
}
