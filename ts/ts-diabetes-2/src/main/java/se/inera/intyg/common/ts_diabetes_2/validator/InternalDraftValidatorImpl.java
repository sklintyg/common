/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes_2.validator;

import static se.inera.intyg.common.support.validate.ValidatorUtil.addValidationError;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.BEDOMNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.INTYGETAVSER_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.OVRIGT_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.SYNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.SYNFUNKTION_OGONBOTTENFOTO_SAKNAS_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID;

import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes_2.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes_2.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_diabetes_2.model.internal.TsDiabetes2Utlatande;
import se.inera.intyg.common.ts_diabetes_2.model.kodverk.KvTypAvDiabetes;

public class InternalDraftValidatorImpl implements InternalDraftValidator<TsDiabetes2Utlatande> {

    private static final String CATEGORY_INTYGET_AVSER_BEHORIGHET = "intygAvser";
    private static final String CATEGORY_ALLMANT = "allmant";
    private static final String CATEGORY_IDENTITET = "identitet";
    private static final String CATEGORY_HYPOGLYKEMIER = "hypoglykemier";
    private static final String CATEGORY_SYNFUNKTION = "synfunktion";
    private static final String CATEGORY_OVRIGT = "ovrigt";
    private static final String CATEGORY_BEDOMNING = "bedomning";
    public static final double RULE_13_CUTOFF = 0.5;
    public static final double RULE_14_CUTOFF = 0.8;
    public static final double RULE_15_CUTOFF = 0.1;

    @Override
    public ValidateDraftResponse validateDraft(TsDiabetes2Utlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 - Intyget avser
        validateIntygetAvser(utlatande, validationMessages);

        // Kategori 2 - Identitet
        validateIdentitetStyrkt(utlatande, validationMessages);

        // Kategori 3 - Allmänt
        if (utlatande.getAllmant() != null) {
            validateDiagnosAr(utlatande, validationMessages);
            validateTypAvDiabetes(utlatande, validationMessages);
            validateBehandling(utlatande, validationMessages);
        } else {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_JSON_ID, ValidationMessageType.EMPTY);
        }

        // Kategori 4 - Hypoglykemier
        // R6: Obligatoriska följdfrågor vid insulinbehandling
        if (eligibleForRule6(utlatande) || eligibleForRule17(utlatande)) {
            if (utlatande.getHypoglykemier() != null) {
                validateSjukdomenUnderKontroll(utlatande, validationMessages);
                validateNedsattHjarnFunktion(utlatande, validationMessages);
                validateForstarRisker(utlatande, validationMessages);
                validateFortrogenMedSymptom(utlatande, validationMessages);
                validateSaknarFormagaVarningsTecken(utlatande, validationMessages);
                validateKunskapLampligaAtgarder(utlatande, validationMessages);
                validateEgenkontrollBlodsocker(utlatande, validationMessages);
                validateAterkommandeSenasteAret(utlatande, validationMessages);
                validateAterkommandeSenasteKvartalet(utlatande, validationMessages);
                validateForekomstTrafikSenasteAret(utlatande, validationMessages);
            } else {
                addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_JSON_ID, ValidationMessageType.EMPTY);
            }
        }

        // Kategori 5 - Synfunktion
        if (utlatande.getSynfunktion() != null) {
            validateMisstankeOgonSjukdom(utlatande, validationMessages);
            validateOgonbottenFotoSaknas(utlatande, validationMessages);
            validateVanster(utlatande, validationMessages);
            validateHoger(utlatande, validationMessages);
            validateBinokulart(utlatande, validationMessages);
        } else {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION, SYNFUNKTION_JSON_ID, ValidationMessageType.EMPTY);
        }

        // Kategori 6 – Övrigt
        validateBlanksForOptionalFields(utlatande, validationMessages);

        // Kategori 7 - Bedömning
        if (utlatande.getBedomning() != null) {
            validateUppfyllerBehorighetskrav(utlatande, validationMessages);
            validateLampligtInnehav(utlatande, validationMessages);
            validateBorUndersokasBeskrivning(utlatande, validationMessages);
        } else {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_JSON_ID, ValidationMessageType.EMPTY);
        }

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateIntygetAvser(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser() == null ? null : utlatande.getIntygAvser().getKategorier();
        if (intygAvser == null || intygAvser.isEmpty()) {
            addValidationError(validationMessages, CATEGORY_INTYGET_AVSER_BEHORIGHET, INTYGETAVSER_SVAR_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateIdentitetStyrkt(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getIdentitetStyrktGenom() == null) {
            addValidationError(validationMessages, CATEGORY_IDENTITET, IDENTITET_STYRKT_GENOM_JSON_ID, ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnosAr(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        String cleanedDiabetesDiagnosAr = Strings.nullToEmpty(utlatande.getAllmant().getDiabetesDiagnosAr()).trim();
        if (cleanedDiabetesDiagnosAr.isEmpty()) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11, ValidationMessageType.EMPTY);
            return;
        }

        Year parsedYear;
        try {
            parsedYear = Year.parse(cleanedDiabetesDiagnosAr);
        } catch (DateTimeParseException e) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11,
                    ValidationMessageType.INVALID_FORMAT);
            return;
        }

        if (eligibleForRule2(utlatande, parsedYear)) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11, ValidationMessageType.OTHER);
        }
    }

    private void validateTypAvDiabetes(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Allmant allmant = utlatande.getAllmant();
        if (allmant.getTypAvDiabetes() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_TYP_AV_DIABETES_JSON_ID, ValidationMessageType.EMPTY);
        }

        if (eligibleForRule3(utlatande) && Strings.nullToEmpty(allmant.getBeskrivningAnnanTypAvDiabetes()).trim().isEmpty()) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateBehandling(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getAllmant().getBehandling() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_BEHANDLING_JSON_ID, ValidationMessageType.EMPTY);
            return;
        }
        Behandling behandling = utlatande.getAllmant().getBehandling();

        if (eligibleForRule4(utlatande)
                && (!nullToFalse(behandling.getEndastKost()))
                && (!nullToFalse(behandling.getTabletter()))
                && (!nullToFalse(behandling.getInsulin()))
                && (!nullToFalse(behandling.getAnnanBehandling()))) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_BEHANDLING_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION);
        }

        if (eligibleForRule5(utlatande)) {
            // R5: Om insulinbehandling besvaras så ska även årtal anges.
            String cleanedInsulinSedanArString = Strings.nullToEmpty(behandling.getInsulinSedanAr()).trim();
            if (cleanedInsulinSedanArString.isEmpty()) {
                addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID,
                        ValidationMessageType.EMPTY);
            } else {
                Year parsedYear;
                try {
                    parsedYear = Year.parse(cleanedInsulinSedanArString);
                } catch (DateTimeParseException e) {
                    addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID,
                            ValidationMessageType.INVALID_FORMAT);
                    return;
                }

                // R7: Årtal för 'insulinbehandling sedan' måste vara efter patienten är född
                if (eligibleForRule7(utlatande)
                        && ValidatorUtil.isYearBeforeBirth(cleanedInsulinSedanArString, utlatande.getGrundData().getPatient().getPersonId())
                        || parsedYear.isAfter(Year.now())) {
                    addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID,
                            ValidationMessageType.OTHER);
                }
            }

        }

        // R16: Om tablettbehandling, svara på om tablettbehandling ger risk för hypoglykemi.
        if (eligibleForRule16(utlatande) && utlatande.getAllmant().getBehandling().getTablettRiskHypoglykemi() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_JSON_ID,
                    ValidationMessageType.EMPTY);
        }

        // R18: Om annan behandling, beskriv annan behandling.
        if (eligibleForRule18(utlatande) && Strings.nullToEmpty(behandling.getAnnanBehandlingBeskrivning()).trim().isEmpty()) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateSjukdomenUnderKontroll(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getSjukdomenUnderKontroll() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateNedsattHjarnFunktion(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getNedsattHjarnfunktion() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateForstarRisker(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getForstarRisker() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateFortrogenMedSymptom(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getFortrogenMedSymptom() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateSaknarFormagaVarningsTecken(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getSaknarFormagaVarningstecken() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateKunskapLampligaAtgarder(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getKunskapLampligaAtgarder() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateEgenkontrollBlodsocker(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getEgenkontrollBlodsocker() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateAterkommandeSenasteAret(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();
        if (hypoglykemier.getAterkommandeSenasteAret() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID,
                    ValidationMessageType.EMPTY);
            return;
        }

        // R8
        if (eligibleForRule8(utlatande) && hypoglykemier.getAterkommandeSenasteTidpunkt() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateAterkommandeSenasteKvartalet(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();
        if (hypoglykemier.getAterkommandeSenasteKvartalet() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID,
                    ValidationMessageType.EMPTY);
            return;
        }

        // R9
        if (eligibleForRule9(utlatande) && hypoglykemier.getSenasteTidpunktVaken() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateForekomstTrafikSenasteAret(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();
        if (hypoglykemier.getForekomstTrafik() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID,
                    ValidationMessageType.EMPTY);
            return;
        }

        // R10
        if (eligibleForRule10(utlatande) && hypoglykemier.getForekomstTrafikTidpunkt() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateMisstankeOgonSjukdom(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSynfunktion().getMisstankeOgonsjukdom() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION, SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateOgonbottenFotoSaknas(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSynfunktion().getOgonbottenFotoSaknas() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION, SYNFUNKTION_OGONBOTTENFOTO_SAKNAS_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateVanster(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden vanster = utlatande.getSynfunktion().getVanster();
        if (vanster == null) {
            // R12
            if (eligibleForRule12(utlatande)) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION, SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID,
                        ValidationMessageType.EMPTY);
            }
            return;
        }

        // 8.2
        if (eligibleForRule12(utlatande) && vanster.getUtanKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                    ValidationMessageType.EMPTY);
        }

        // 8.5
        if ((eligibleForRule13(utlatande) || eligibleForRule14(utlatande) || eligibleForRule15(utlatande))
                && vanster.getMedKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateHoger(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden hoger = utlatande.getSynfunktion().getHoger();
        if (hoger == null) {
            // R12
            if (eligibleForRule12(utlatande)) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION, SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID,
                        ValidationMessageType.EMPTY);
            }
            return;
        }

        // 8.1
        if (eligibleForRule12(utlatande) && hoger.getUtanKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                    ValidationMessageType.EMPTY);
        }

        // 8.4
        if ((eligibleForRule13(utlatande) || eligibleForRule14(utlatande) || eligibleForRule15(utlatande))
                && hoger.getMedKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateBinokulart(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden binokulart = utlatande.getSynfunktion().getBinokulart();
        if (binokulart == null) {
            // R12
            if (eligibleForRule12(utlatande)) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION, SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID,
                        ValidationMessageType.EMPTY);
            }
            return;
        }

        // 8.3
        if (eligibleForRule12(utlatande) && binokulart.getUtanKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                    ValidationMessageType.EMPTY);
        }

        // 8.6
        if ((eligibleForRule13(utlatande) || eligibleForRule14(utlatande) || eligibleForRule15(utlatande))
                && binokulart.getMedKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateUppfyllerBehorighetskrav(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        Set<BedomningKorkortstyp> behorighet = utlatande.getBedomning().getUppfyllerBehorighetskrav();
        // Minst 1 behörighetskrav behöver vara markerat.
        if (behorighet == null || behorighet.isEmpty()) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateLampligtInnehav(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (eligibleForRule1(utlatande) && utlatande.getBedomning().getLampligtInnehav() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateBorUndersokasBeskrivning(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Optional, därför ingen validering.
    }

    private void validateBlanksForOptionalFields(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_DELSVAR_JSON_ID, ValidationMessageType.EMPTY,
                    "ts-diabetes-2.validation.blanksteg.otillatet");
        }
    }

    // R1
    private static boolean eligibleForRule1(TsDiabetes2Utlatande utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        Set<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(IntygAvserKategori.C1, IntygAvserKategori.C1E,
                IntygAvserKategori.C, IntygAvserKategori.CE, IntygAvserKategori.D1, IntygAvserKategori.D1E, IntygAvserKategori.D,
                IntygAvserKategori.DE, IntygAvserKategori.TAXI);
        return !Collections.disjoint(intygAvser, answerRequiringAdditionalData);
    }

    // R2
    private static boolean eligibleForRule2(TsDiabetes2Utlatande utlatande, Year diagnosYear) {
        return ValidatorUtil.isYearBeforeBirth(diagnosYear.toString(), utlatande.getGrundData().getPatient().getPersonId())
                || diagnosYear.isAfter(Year.now());
    }

    // R3
    private static boolean eligibleForRule3(TsDiabetes2Utlatande utlatande) {
        return utlatande.getAllmant() != null
                && utlatande.getAllmant().getTypAvDiabetes() == KvTypAvDiabetes.ANNAN;
    }

    // R4: Minst 1 behandling behöver vara markerad.
    private static boolean eligibleForRule4(TsDiabetes2Utlatande utlatande) {
        return true;
    }

    // R5
    private static boolean eligibleForRule5(TsDiabetes2Utlatande utlatande) {
        return utlatande != null && utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
                && nullToFalse(utlatande.getAllmant().getBehandling().getInsulin());
    }

    // R6
    private static boolean eligibleForRule6(TsDiabetes2Utlatande utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
                && nullToFalse(utlatande.getAllmant().getBehandling().getInsulin());
    }

    // R7
    private static boolean eligibleForRule7(TsDiabetes2Utlatande utlatande) {
        return true;
    }

    // R8
    private static boolean eligibleForRule8(TsDiabetes2Utlatande utlatande) {
        return utlatande.getHypoglykemier() != null && nullToFalse(utlatande.getHypoglykemier().getAterkommandeSenasteAret());
    }

    // R9
    private static boolean eligibleForRule9(TsDiabetes2Utlatande utlatande) {
        return utlatande.getHypoglykemier() != null && nullToFalse(utlatande.getHypoglykemier().getAterkommandeSenasteKvartalet());
    }

    // R10
    private static boolean eligibleForRule10(TsDiabetes2Utlatande utlatande) {
        return utlatande.getHypoglykemier() != null && nullToFalse(utlatande.getHypoglykemier().getForekomstTrafik());
    }

    // R12
    private static boolean eligibleForRule12(TsDiabetes2Utlatande utlatande) {
        return utlatande.getSynfunktion() != null
                && Boolean.FALSE.equals(utlatande.getSynfunktion().getMisstankeOgonsjukdom())
                && Boolean.FALSE.equals(utlatande.getSynfunktion().getOgonbottenFotoSaknas());
    }

    // R13
    private static boolean eligibleForRule13(TsDiabetes2Utlatande utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        Set<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(IntygAvserKategori.AM, IntygAvserKategori.A1,
                IntygAvserKategori.A2,
                IntygAvserKategori.A, IntygAvserKategori.B, IntygAvserKategori.BE, IntygAvserKategori.TRAKTOR);
        boolean conditionKorkortstyp = !Collections.disjoint(intygAvser, answerRequiringAdditionalData);
        boolean conditionBinokulartDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getBinokulart() != null
                && utlatande.getSynfunktion().getBinokulart().getUtanKorrektion() != null
                && utlatande.getSynfunktion().getBinokulart().getUtanKorrektion() < RULE_13_CUTOFF;
        return conditionKorkortstyp && conditionBinokulartDaligSyn;
    }

    // R14: Vid vissa körkortslicenser, vid dålig syn utan korrigering, blir en del synvärden obligatoriska.
    private static boolean eligibleForRule14(TsDiabetes2Utlatande utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        Set<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(IntygAvserKategori.C1, IntygAvserKategori.C1E,
                IntygAvserKategori.C, IntygAvserKategori.CE, IntygAvserKategori.D1, IntygAvserKategori.D1E, IntygAvserKategori.D,
                IntygAvserKategori.DE, IntygAvserKategori.TAXI);
        boolean conditionKorkortstyp = !Collections.disjoint(intygAvser, answerRequiringAdditionalData);
        boolean conditionVansterOgaDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getVanster() != null
                && utlatande.getSynfunktion().getVanster().getUtanKorrektion() != null
                && utlatande.getSynfunktion().getVanster().getUtanKorrektion() < RULE_14_CUTOFF;
        boolean conditionHogerOgaDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getHoger() != null
                && utlatande.getSynfunktion().getHoger().getUtanKorrektion() != null
                && utlatande.getSynfunktion().getHoger().getUtanKorrektion() < RULE_14_CUTOFF;

        return conditionKorkortstyp && (conditionVansterOgaDaligSyn || conditionHogerOgaDaligSyn);
    }

    // R15: som R14, fast fler obligatoriska frågor
    private static boolean eligibleForRule15(TsDiabetes2Utlatande utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        Set<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(IntygAvserKategori.C1, IntygAvserKategori.C1E,
                IntygAvserKategori.C,
                IntygAvserKategori.CE, IntygAvserKategori.D1, IntygAvserKategori.D1E, IntygAvserKategori.D, IntygAvserKategori.DE,
                IntygAvserKategori.TAXI);
        boolean conditionKorkortstyp = !Collections.disjoint(intygAvser, answerRequiringAdditionalData);
        boolean conditionVansterOgaDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getVanster() != null
                && utlatande.getSynfunktion().getVanster().getUtanKorrektion() != null
                && utlatande.getSynfunktion().getVanster().getUtanKorrektion() < RULE_15_CUTOFF;
        boolean conditionHogerOgaDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getHoger() != null
                && utlatande.getSynfunktion().getHoger().getUtanKorrektion() != null
                && utlatande.getSynfunktion().getHoger().getUtanKorrektion() < RULE_15_CUTOFF;

        return conditionKorkortstyp && (conditionVansterOgaDaligSyn || conditionHogerOgaDaligSyn);
    }

    // R16
    private static boolean eligibleForRule16(TsDiabetes2Utlatande utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
                && nullToFalse(utlatande.getAllmant().getBehandling().getTabletter());
    }

    // R17
    private static boolean eligibleForRule17(TsDiabetes2Utlatande utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
                && nullToFalse(utlatande.getAllmant().getBehandling().getTablettRiskHypoglykemi());
    }

    // R18
    private static boolean eligibleForRule18(TsDiabetes2Utlatande utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
                && nullToFalse(utlatande.getAllmant().getBehandling().getAnnanBehandling());
    }

    private static boolean nullToFalse(Boolean bool) {
        return bool != null && bool;
    }
}
