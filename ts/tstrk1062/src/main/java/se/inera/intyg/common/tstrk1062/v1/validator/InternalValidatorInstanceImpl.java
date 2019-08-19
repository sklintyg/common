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

import static se.inera.intyg.common.support.validate.ValidatorUtil.addValidationError;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;
import se.inera.intyg.common.tstrk1062.v1.model.internal.*;

@Component("tstrk1062.v1.InternalDraftValidator")
public class InternalValidatorInstanceImpl implements InternalDraftValidator<TsTrk1062UtlatandeV1> {

    public InternalValidatorInstanceImpl() {

    }

    @Override
    public ValidateDraftResponse validateDraft(TsTrk1062UtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 - Intyget avser
        validateIntygAvser(utlatande.getIntygAvser(), validationMessages);

        // Kategori 2 - Identitet
        validateIdKontroll(utlatande.getIdKontroll(), validationMessages);

        // Kategori 3 - Allmänt
        validateDiagnosRegistrering(utlatande.getDiagnosRegistrering(),
            utlatande.getDiagnosKodad(),
            utlatande.getDiagnosFritext(), validationMessages);

        // Kategori 4 - Lakemedelsbehandling
        validateLakemedelsbehandling(utlatande.getLakemedelsbehandling(), validationMessages);

        // Kategori 5 - Symptom, funktionshinder och prognos
        validateSymptom(utlatande.getBedomningAvSymptom(), validationMessages);
        validatePrognos(utlatande.getPrognosTillstand(), validationMessages);

        // Kategori 6 - Ovrigt - Inga regler

        // Kategori 7 - Bedomning
        validateBedomning(utlatande.getBedomning(), validationMessages);

        // Enhetsvalidering
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return new ValidateDraftResponse(ValidatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateIntygAvser(final IntygAvser intygAvser, List<ValidationMessage> validationMessages) {
        if (isNull(intygAvser)) {
            addValidationError(validationMessages,
                INTYG_AVSER_CATEGORY, INTYG_AVSER_SVAR_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        if (intygAvser.getBehorigheter() != null && intygAvser.getBehorigheter().isEmpty()) {
            addValidationError(validationMessages,
                INTYG_AVSER_CATEGORY,
                INTYG_AVSER_SVAR_JSON_ID + PUNKT + INTYG_AVSER_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateIdKontroll(IdKontroll idKontroll, List<ValidationMessage> validationMessages) {
        if (isNull(idKontroll)) {
            addValidationError(validationMessages,
                ID_KONTROLL_CATEGORY,
                ID_KONTROLL_SVAR_JSON_ID + PUNKT + ID_KONTROLL_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnosRegistrering(DiagnosRegistrering diagnosRegistrering,
        ImmutableList<DiagnosKodad> diagnosKodad,
        DiagnosFritext diagnosFritext,
        List<ValidationMessage> validationMessages) {
        if (isNull(diagnosRegistrering) || isNull(diagnosRegistrering.getTyp())) {
            addValidationError(validationMessages,
                ALLMANT_KATEGORI,
                ALLMANT_INMATNING_SVAR_JSON_ID + PUNKT + ALLMANT_INMATNING_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        if (diagnosRegistrering.getTyp() == DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD) {
            if (isNull(diagnosKodad) || diagnosKodad.isEmpty()) {
                addValidationError(validationMessages,
                    ALLMANT_KATEGORI,
                    ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID,
                    ValidationMessageType.EMPTY,
                    "tstrk1062.validation.diagnos.missing");
                return;
            }

            validateDiagnosKodad(diagnosKodad, validationMessages);

        } else if (diagnosRegistrering.getTyp() == DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT) {
            validateDiagnosFritext(diagnosFritext, validationMessages);
        }
    }

    private void validateDiagnosFritext(DiagnosFritext diagnosFritext, List<ValidationMessage> validationMessages) {
        if (isNull(diagnosFritext)) {
            addValidationError(validationMessages,
                ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        if (isNull(diagnosFritext.getDiagnosFritext()) || diagnosFritext.getDiagnosFritext().isEmpty()) {
            addValidationError(validationMessages,
                ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
        }

        if (isNull(diagnosFritext.getDiagnosArtal()) || diagnosFritext.getDiagnosArtal().isEmpty()) {
            addValidationError(validationMessages,
                ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY,
                "common.validation.b-03a");
        } else if (isNotYear(diagnosFritext.getDiagnosArtal())) {
            addValidationError(validationMessages,
                ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID,
                ValidationMessageType.INVALID_FORMAT,
                "common.validation.ue-year-picker.invalid_format");
        } else if (isFutureYear(diagnosFritext.getDiagnosArtal())) {
            addValidationError(validationMessages,
                ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID + PUNKT + ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID,
                ValidationMessageType.OTHER,
                "tstrk1062.validation.diagnos.artal");
        }
    }

    private void validateDiagnosKodad(ImmutableList<DiagnosKodad> diagnosKodad, List<ValidationMessage> validationMessages) {
        int diagnosNr = 0;
        boolean ignoreDiagnoseOne = false;

        if (!diagnosKodad.isEmpty() && !validateFirstDiagnoseIsPresent(diagnosKodad) && diagnosKodad.size() > 1) {
            ValidatorUtil.addValidationError(validationMessages,
                ALLMANT_KATEGORI,
                ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + SB + diagnosNr + EB + PUNKT
                    + ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID,
                ValidationMessageType.INCORRECT_COMBINATION, "common.validation.c-05");
            // No additional validation messages should be added to diagnose one.
            ignoreDiagnoseOne = true;
        }

        for (DiagnosKodad diagnos : diagnosKodad) {
            if (diagnosNr == 0 && ignoreDiagnoseOne) {
                diagnosNr++;
                continue;
            }

            if (isNull(diagnos.getDiagnosKod()) || diagnos.getDiagnosKod().isEmpty()) {
                addValidationError(validationMessages,
                    ALLMANT_KATEGORI,
                    ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + SB + diagnosNr + EB + PUNKT
                        + ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID,
                    ValidationMessageType.EMPTY,
                    "common.validation.b-03a");
            }
            if (isNull(diagnos.getDiagnosKodSystem()) || diagnos.getDiagnosKodSystem().isEmpty()) {
                addValidationError(validationMessages,
                    ALLMANT_KATEGORI,
                    ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + SB + diagnosNr + EB + PUNKT
                        + ALLMANT_DIAGNOSKOD_KODAD_KOD_KODSYSTEM_JSON_ID,
                    ValidationMessageType.EMPTY,
                    "common.validation.diagnos.kodsystem");
            }
            if (isNull(diagnos.getDiagnosArtal()) || diagnos.getDiagnosArtal().isEmpty()) {
                addValidationError(validationMessages,
                    ALLMANT_KATEGORI,
                    ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + SB + diagnosNr + EB + PUNKT
                        + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID,
                    ValidationMessageType.EMPTY,
                    "common.validation.b-03a");
            } else if (isNotYear(diagnos.getDiagnosArtal())) {
                addValidationError(validationMessages,
                    ALLMANT_KATEGORI,
                    ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + SB + diagnosNr + EB + PUNKT
                        + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID,
                    ValidationMessageType.INVALID_FORMAT,
                    "common.validation.ue-year-picker.invalid_format");
            } else if (isFutureYear(diagnos.getDiagnosArtal())) {
                addValidationError(validationMessages,
                    ALLMANT_KATEGORI,
                    ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID + SB + diagnosNr + EB + PUNKT
                        + ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID,
                    ValidationMessageType.OTHER,
                    "tstrk1062.validation.diagnos.artal");
            }

            diagnosNr++;
        }
    }

    private Boolean validateFirstDiagnoseIsPresent(List<DiagnosKodad> diagnosKodad) {
        DiagnosKodad diagnos = diagnosKodad.get(0);
        return !Strings.nullToEmpty(diagnos.getDiagnosKod()).trim().isEmpty()
            || !Strings.nullToEmpty(diagnos.getDiagnosBeskrivning()).trim().isEmpty()
            || !Strings.nullToEmpty(diagnos.getDiagnosArtal()).trim().isEmpty();
    }

    private void validateLakemedelsbehandling(Lakemedelsbehandling lakemedelsbehandling, List<ValidationMessage> validationMessages) {
        if (isNull(lakemedelsbehandling) || isNull(lakemedelsbehandling.getHarHaft())) {
            addValidationError(validationMessages,
                LAKEMEDELSBEHANDLING_KATEGORI,
                LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        if (lakemedelsbehandling.getHarHaft()) {
            if (isNull(lakemedelsbehandling.getPagar())) {
                addValidationError(validationMessages,
                    LAKEMEDELSBEHANDLING_KATEGORI,
                    LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_JSON_ID,
                    ValidationMessageType.EMPTY);
                return;
            }

            if (lakemedelsbehandling.getPagar()) {
                if (isNull(lakemedelsbehandling.getAktuell()) || lakemedelsbehandling.getAktuell().isEmpty()) {
                    addValidationError(validationMessages,
                        LAKEMEDELSBEHANDLING_KATEGORI,
                        LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_JSON_ID,
                        ValidationMessageType.EMPTY);
                }

                if (isNull(lakemedelsbehandling.getPagatt())) {
                    addValidationError(validationMessages,
                        LAKEMEDELSBEHANDLING_KATEGORI,
                        LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_JSON_ID,
                        ValidationMessageType.EMPTY);
                }

                if (isNull(lakemedelsbehandling.getEffekt())) {
                    addValidationError(validationMessages,
                        LAKEMEDELSBEHANDLING_KATEGORI,
                        LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_JSON_ID,
                        ValidationMessageType.EMPTY);
                }

                if (isNull(lakemedelsbehandling.getFoljsamhet())) {
                    addValidationError(validationMessages,
                        LAKEMEDELSBEHANDLING_KATEGORI,
                        LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_JSON_ID,
                        ValidationMessageType.EMPTY);
                }
            } else {
                if (isNull(lakemedelsbehandling.getAvslutadTidpunkt()) || lakemedelsbehandling.getAvslutadTidpunkt().isEmpty()) {
                    addValidationError(validationMessages,
                        LAKEMEDELSBEHANDLING_KATEGORI,
                        LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_JSON_ID,
                        ValidationMessageType.EMPTY);
                }
                if (isNull(lakemedelsbehandling.getAvslutadOrsak()) || lakemedelsbehandling.getAvslutadOrsak().isEmpty()) {
                    addValidationError(validationMessages,
                        LAKEMEDELSBEHANDLING_KATEGORI,
                        LAKEMEDELSBEHANDLING_JSON_ID + PUNKT + LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_JSON_ID,
                        ValidationMessageType.EMPTY);
                }
            }
        }
    }

    private void validateSymptom(String bedomningAvSymptom, List<ValidationMessage> validationMessages) {
        if (isNull(bedomningAvSymptom) || bedomningAvSymptom.isEmpty()) {
            addValidationError(validationMessages,
                SYMPTOM_KATEGORI,
                SYMPTOM_BEDOMNING_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validatePrognos(PrognosTillstand prognosTillstand, List<ValidationMessage> validationMessages) {
        if (isNull(prognosTillstand) || isNull(prognosTillstand.getTyp())) {
            addValidationError(validationMessages,
                SYMPTOM_KATEGORI,
                SYMPTOM_PROGNOS_SVAR_JSON_ID + PUNKT + SYMPTOM_PROGNOS_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateBedomning(Bedomning bedomning, List<ValidationMessage> validationMessages) {
        if (isNull(bedomning) || isNull(bedomning.getUppfyllerBehorighetskrav()) || bedomning.getUppfyllerBehorighetskrav().isEmpty()) {
            addValidationError(validationMessages,
                BEDOMNING_KATEGORI,
                BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID,
                ValidationMessageType.EMPTY);
        } else {
            final Set<Bedomning.BehorighetsTyp> behorighetsTypSet = bedomning.getUppfyllerBehorighetskrav();
            for (Bedomning.BehorighetsTyp behorighetsTyp : behorighetsTypSet) {
                if (behorighetsTyp == Bedomning.BehorighetsTyp.VAR11) {
                    if (behorighetsTypSet.size() > 1) {
                        addValidationError(validationMessages,
                            BEDOMNING_KATEGORI,
                            BEDOMNING_UPPFYLLER_SVAR_JSON_ID + PUNKT + BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION);
                    }
                }
            }
        }
    }

    private boolean isNull(Object objectToValidate) {
        return objectToValidate == null;
    }

    private boolean isNotYear(String artal) {
        try {
            Integer.parseInt(artal);
            return false;
        } catch (NumberFormatException nfe) {
            return true;
        }
    }

    private boolean isFutureYear(String artal) {
        return Integer.parseInt(artal) > LocalDate.now().getYear();
    }
}
