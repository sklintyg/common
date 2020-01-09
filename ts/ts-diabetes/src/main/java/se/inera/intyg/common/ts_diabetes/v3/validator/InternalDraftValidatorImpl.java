/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v3.validator;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synfunktion;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.inera.intyg.common.ts_diabetes.v3.model.kodverk.KvTypAvDiabetes;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;

import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static se.inera.intyg.common.support.validate.ValidatorUtil.addValidationError;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.INTYGETAVSER_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.OVRIGT_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_OGONBOTTENFOTO_SAKNAS_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.SynfunktionTyp.BINOKULART_MED_KORREKTION;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.SynfunktionTyp.BINOKULART_UTAN_KORREKTION;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.SynfunktionTyp.HOGER_MED_KORREKTION;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.SynfunktionTyp.HOGER_UTAN_KORREKTION;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.SynfunktionTyp.VANSTER_MED_KORREKTION;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.SynfunktionTyp.VANSTER_UTAN_KORREKTION;

@Component("ts-diabetes.v3.InternalDraftValidator")
public class InternalDraftValidatorImpl implements InternalDraftValidator<TsDiabetesUtlatandeV3> {

    public static final double RULE_13_CUTOFF = 0.5;
    public static final double RULE_14_CUTOFF = 0.8;
    public static final double RULE_15_CUTOFF = 0.1;
    public static final long RULE_20_22_TWELVE_MONTHS = 12;
    public static final long RULE_21_THREE_MONTHS = 3;
    protected static final String CATEGORY_INTYGET_AVSER_BEHORIGHET = "intygAvser";
    protected static final String CATEGORY_ALLMANT = "allmant";
    protected static final String CATEGORY_IDENTITET = "identitetStyrktGenom";
    protected static final String CATEGORY_HYPOGLYKEMIER = "hypoglykemier";
    protected static final String CATEGORY_SYNFUNKTION = "synfunktion";
    protected static final String CATEGORY_OVRIGT = "ovrigt";
    protected static final String CATEGORY_BEDOMNING = "bedomning";
    protected static final String BEHANDLING_ROOT_FIELD_PATH = ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + ".";

    // R1
    private static boolean eligibleForRule1(TsDiabetesUtlatandeV3 utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        final ImmutableSet<IntygAvserKategori> intygAvser = ImmutableSet.copyOf(utlatande.getIntygAvser().getKategorier());
        final ImmutableSet<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(
            IntygAvserKategori.C1,
            IntygAvserKategori.C1E,
            IntygAvserKategori.C,
            IntygAvserKategori.CE,
            IntygAvserKategori.D1,
            IntygAvserKategori.D1E,
            IntygAvserKategori.D,
            IntygAvserKategori.DE,
            IntygAvserKategori.TAXI);

        return !Collections.disjoint(intygAvser, answerRequiringAdditionalData);
    }

    // R2
    private static boolean eligibleForRule2(TsDiabetesUtlatandeV3 utlatande, Year diagnosYear) {
        return ValidatorUtil.isYearBeforeBirth(diagnosYear.toString(), utlatande.getGrundData().getPatient().getPersonId())
            || diagnosYear.isAfter(Year.now());
    }

    // R3
    private static boolean eligibleForRule3(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null
            && utlatande.getAllmant().getTypAvDiabetes() == KvTypAvDiabetes.ANNAN;
    }

    // R4
    private static boolean eligibleForRule4(TsDiabetesUtlatandeV3 utlatande) {
        return true;
    }

    // R5
    private static boolean eligibleForRule5(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande != null && utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getInsulin());
    }

    // R6
    private static boolean eligibleForRule6(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getInsulin());
    }

    // R7
    private static boolean eligibleForRule7(TsDiabetesUtlatandeV3 utlatande) {
        return true;
    }

    // R8
    private static boolean eligibleForRule8(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null && isTrue(utlatande.getHypoglykemier().getAterkommandeSenasteAret());
    }

    // R9
    private static boolean eligibleForRule9(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null && isTrue(utlatande.getHypoglykemier().getAterkommandeSenasteKvartalet());
    }

    // R10
    private static boolean eligibleForRule10(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null && isTrue(utlatande.getHypoglykemier().getForekomstTrafik());
    }

    // R12
    private static boolean eligibleForRule12(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getSynfunktion() != null
            && isFalse(utlatande.getSynfunktion().getMisstankeOgonsjukdom())
            && isFalse(utlatande.getSynfunktion().getOgonbottenFotoSaknas());
    }

    // R13
    private static boolean eligibleForRule13(TsDiabetesUtlatandeV3 utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        Set<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(
            IntygAvserKategori.AM,
            IntygAvserKategori.A1,
            IntygAvserKategori.A2,
            IntygAvserKategori.A,
            IntygAvserKategori.B,
            IntygAvserKategori.BE,
            IntygAvserKategori.TRAKTOR);
        boolean conditionKorkortstyp = !Collections.disjoint(intygAvser, answerRequiringAdditionalData);
        boolean conditionBinokulartDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getBinokulart() != null
            && utlatande.getSynfunktion().getBinokulart().getUtanKorrektion() != null
            && utlatande.getSynfunktion().getBinokulart().getUtanKorrektion() < RULE_13_CUTOFF;
        return conditionKorkortstyp && conditionBinokulartDaligSyn;
    }

    // R14: Vid vissa körkortslicenser, vid dålig syn utan korrigering, blir en del synvärden obligatoriska.
    private static boolean eligibleForRule14(TsDiabetesUtlatandeV3 utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        Set<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(
            IntygAvserKategori.C1,
            IntygAvserKategori.C1E,
            IntygAvserKategori.C,
            IntygAvserKategori.CE,
            IntygAvserKategori.D1,
            IntygAvserKategori.D1E,
            IntygAvserKategori.D,
            IntygAvserKategori.DE,
            IntygAvserKategori.TAXI);

        boolean conditionKorkortstyp = !Collections.disjoint(intygAvser, answerRequiringAdditionalData);

        boolean conditionVansterOgaDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getVanster() != null
            && utlatande.getSynfunktion().getVanster().getUtanKorrektion() != null
            && utlatande.getSynfunktion().getVanster().getUtanKorrektion() < RULE_14_CUTOFF;

        boolean conditionHogerOgaDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getHoger() != null
            && utlatande.getSynfunktion().getHoger().getUtanKorrektion() != null
            && utlatande.getSynfunktion().getHoger().getUtanKorrektion() < RULE_14_CUTOFF;

        return conditionKorkortstyp && (conditionVansterOgaDaligSyn && conditionHogerOgaDaligSyn);
    }

    // R15: som R14, fast fler obligatoriska frågor
    private static boolean eligibleForRule15(TsDiabetesUtlatandeV3 utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        Set<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(
            IntygAvserKategori.C1,
            IntygAvserKategori.C1E,
            IntygAvserKategori.C,
            IntygAvserKategori.CE,
            IntygAvserKategori.D1,
            IntygAvserKategori.D1E,
            IntygAvserKategori.D,
            IntygAvserKategori.DE,
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
    private static boolean eligibleForRule16(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getTabletter());
    }

    // R17
    private static boolean eligibleForRule17(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getTablettRiskHypoglykemi());
    }

    // R18
    private static boolean eligibleForRule18(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getAnnanBehandling());
    }

    // R19
    private static boolean eligibleForRule19(TsDiabetesUtlatandeV3 utlatande, SynfunktionTyp synfunctionTyp) {
        final Optional<TsDiabetesUtlatandeV3> optionalUtlatande = Optional.ofNullable(utlatande);

        switch (synfunctionTyp) {
            case HOGER_UTAN_KORREKTION:
                return optionalUtlatande // 8.1
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getHoger)
                    .map(Synskarpevarden::getUtanKorrektion)
                    .isPresent();
            case VANSTER_UTAN_KORREKTION:
                return optionalUtlatande // 8.2
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getVanster)
                    .map(Synskarpevarden::getUtanKorrektion)
                    .isPresent();
            case BINOKULART_UTAN_KORREKTION:
                return optionalUtlatande // 8.3
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getBinokulart)
                    .map(Synskarpevarden::getUtanKorrektion)
                    .isPresent();
            case HOGER_MED_KORREKTION:
                return optionalUtlatande // 8.4
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getHoger)
                    .map(Synskarpevarden::getMedKorrektion)
                    .isPresent();
            case VANSTER_MED_KORREKTION:
                return optionalUtlatande // 8.5
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getVanster)
                    .map(Synskarpevarden::getMedKorrektion)
                    .isPresent();
            case BINOKULART_MED_KORREKTION:
                return optionalUtlatande // 8.6
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getBinokulart)
                    .map(Synskarpevarden::getMedKorrektion)
                    .isPresent();
        }

        throw new IllegalArgumentException("SyntypFunktionTyp not supported");
    }

    // R20
    private static boolean eligibleForRule20(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null
            && isTrue(utlatande.getHypoglykemier().getAterkommandeSenasteAret())
            && utlatande.getHypoglykemier().getAterkommandeSenasteTidpunkt() != null;
    }

    // R21
    private static boolean eligibleForRule21(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null
            && isTrue(utlatande.getHypoglykemier().getAterkommandeSenasteKvartalet())
            && utlatande.getHypoglykemier().getSenasteTidpunktVaken() != null;
    }

    // R22
    private static boolean eligibleForRule22(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null
            && isTrue(utlatande.getHypoglykemier().getForekomstTrafik())
            && utlatande.getHypoglykemier().getForekomstTrafikTidpunkt() != null;
    }

    @Override
    public ValidateDraftResponse validateDraft(TsDiabetesUtlatandeV3 utlatande) {
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

        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateIntygetAvser(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser() == null ? null : utlatande.getIntygAvser().getKategorier();
        if (intygAvser == null || intygAvser.isEmpty()) {
            addValidationError(validationMessages, CATEGORY_INTYGET_AVSER_BEHORIGHET, INTYGETAVSER_SVAR_JSON_ID + ".kategorier",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateIdentitetStyrkt(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getIdentitetStyrktGenom() == null) {
            addValidationError(validationMessages, CATEGORY_IDENTITET, IDENTITET_STYRKT_GENOM_JSON_ID + ".typ",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnosAr(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        String diabetesSedanArFieldPath = ALLMANT_JSON_ID + "." + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11;
        String cleanedDiabetesDiagnosAr = Strings.nullToEmpty(utlatande.getAllmant().getDiabetesDiagnosAr()).trim();
        if (cleanedDiabetesDiagnosAr.isEmpty()) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, diabetesSedanArFieldPath, ValidationMessageType.EMPTY);
            return;
        }

        Year parsedYear;
        try {
            parsedYear = Year.parse(cleanedDiabetesDiagnosAr);
        } catch (DateTimeParseException e) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, diabetesSedanArFieldPath,
                ValidationMessageType.INVALID_FORMAT);
            return;
        }

        // R2
        if (ValidatorUtil.isYearBeforeBirth(parsedYear.toString(), utlatande.getGrundData().getPatient().getPersonId())) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, diabetesSedanArFieldPath,
                ValidationMessageType.OTHER, "common.validation.d-05");
        }
        if (parsedYear.isAfter(Year.now())) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, diabetesSedanArFieldPath,
                ValidationMessageType.OTHER, "common.validation.d-02");
        }

    }

    private void validateTypAvDiabetes(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        String typAvDiabetesFieldPath = ALLMANT_JSON_ID + "." + ALLMANT_TYP_AV_DIABETES_JSON_ID;
        String annanTypAvDiabetesBeskrivningFieldPath = ALLMANT_JSON_ID + "." + ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
        Allmant allmant = utlatande.getAllmant();
        if (allmant.getTypAvDiabetes() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, typAvDiabetesFieldPath, ValidationMessageType.EMPTY);
        }

        if (eligibleForRule3(utlatande) && Strings.nullToEmpty(allmant.getBeskrivningAnnanTypAvDiabetes()).trim().isEmpty()) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, annanTypAvDiabetesBeskrivningFieldPath,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateBehandling(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        String insulinSedanArFieldPath = ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + "."
            + ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID;

        if (utlatande.getAllmant().getBehandling() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID,
                ValidationMessageType.EMPTY, "common.validation.ue-checkgroup.empty");
            return;
        }
        Behandling behandling = utlatande.getAllmant().getBehandling();

        // R4: Minst 1 av nedanstående behandlingar behöver vara markerad.
        if (eligibleForRule4(utlatande)
            && isNotTrue(behandling.getEndastKost())
            && isNotTrue(behandling.getTabletter())
            && isNotTrue(behandling.getInsulin())
            && isNotTrue(behandling.getAnnanBehandling())) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID,
                ValidationMessageType.EMPTY, "common.validation.ue-checkgroup.empty");
        }

        // R5: Om insulinbehandling besvaras så ska även årtal anges.
        if (eligibleForRule5(utlatande)) {
            String cleanedInsulinSedanArString = Strings.nullToEmpty(behandling.getInsulinSedanAr()).trim();
            if (cleanedInsulinSedanArString.isEmpty()) {

                addValidationError(validationMessages, CATEGORY_ALLMANT,
                    insulinSedanArFieldPath,
                    ValidationMessageType.EMPTY, "common.validation.d-01");
            } else {
                Year parsedYear;
                try {
                    parsedYear = Year.parse(cleanedInsulinSedanArString);
                } catch (DateTimeParseException e) {
                    addValidationError(validationMessages, CATEGORY_ALLMANT, insulinSedanArFieldPath, ValidationMessageType.INVALID_FORMAT);
                    return;
                }

                // R7: Årtal för 'insulinbehandling sedan' måste vara efter patienten är född, och senast innevarande år
                if (eligibleForRule7(utlatande)
                    && ValidatorUtil.isYearBeforeBirth(cleanedInsulinSedanArString,
                    utlatande.getGrundData().getPatient().getPersonId())) {
                    addValidationError(validationMessages, CATEGORY_ALLMANT, insulinSedanArFieldPath,
                        ValidationMessageType.OTHER, "common.validation.d-05");
                }
                if (eligibleForRule7(utlatande)
                    && parsedYear.isAfter(Year.now())) {
                    addValidationError(validationMessages, CATEGORY_ALLMANT, insulinSedanArFieldPath,
                        ValidationMessageType.OTHER, "common.validation.d-02");
                }
            }

        }

        // R16: Om tablettbehandling, svara på om tablettbehandling ger risk för hypoglykemi.
        if (eligibleForRule16(utlatande) && utlatande.getAllmant().getBehandling().getTablettRiskHypoglykemi() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT,
                BEHANDLING_ROOT_FIELD_PATH + ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_JSON_ID,
                ValidationMessageType.EMPTY);
        }

        // R18: Om annan behandling, beskriv annan behandling.
        if (eligibleForRule18(utlatande) && Strings.nullToEmpty(behandling.getAnnanBehandlingBeskrivning()).trim().isEmpty()) {
            addValidationError(validationMessages, CATEGORY_ALLMANT,
                BEHANDLING_ROOT_FIELD_PATH + ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateSjukdomenUnderKontroll(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getSjukdomenUnderKontroll() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateNedsattHjarnFunktion(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getNedsattHjarnfunktion() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateForstarRisker(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getForstarRisker() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateFortrogenMedSymptom(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getFortrogenMedSymptom() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateSaknarFormagaVarningsTecken(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getSaknarFormagaVarningstecken() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateKunskapLampligaAtgarder(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getKunskapLampligaAtgarder() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateEgenkontrollBlodsocker(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemier().getEgenkontrollBlodsocker() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateAterkommandeSenasteAret(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();
        if (hypoglykemier.getAterkommandeSenasteAret() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        // R8
        if (eligibleForRule8(utlatande)) {
            if (!ValidatorUtil.validateDate(hypoglykemier.getAterkommandeSenasteTidpunkt(), validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, null)) {
                return;
            }
        }

        // R20 The date should be within last 12 months
        if (eligibleForRule20(utlatande)) {
            InternalDate minDateBack = new InternalDate(LocalDate.now().minusMonths(RULE_20_22_TWELVE_MONTHS));
            InternalDate givenDate = hypoglykemier.getAterkommandeSenasteTidpunkt();
            if (givenDate.beforeMinDateOrInFuture(minDateBack.asLocalDate())) {
                addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                    HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID,
                    ValidationMessageType.OTHER, "common.validation.d-09");
            }
        }
    }

    private void validateAterkommandeSenasteKvartalet(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();
        if (hypoglykemier.getAterkommandeSenasteKvartalet() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        // R9
        if (eligibleForRule9(utlatande)) {
            if (!ValidatorUtil.validateDate(hypoglykemier.getSenasteTidpunktVaken(), validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID, null)) {
                return;
            }
        }

        // R21 The date should be within the last 3 months
        if (eligibleForRule21(utlatande)) {
            InternalDate minDateBack = new InternalDate(LocalDate.now().minusMonths(RULE_21_THREE_MONTHS));
            InternalDate givenDate = utlatande.getHypoglykemier().getSenasteTidpunktVaken();
            if (givenDate.beforeMinDateOrInFuture(minDateBack.asLocalDate())) {
                addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                    HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID,
                    ValidationMessageType.OTHER, "common.validation.d-10");
            }
        }
    }

    private void validateForekomstTrafikSenasteAret(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();
        if (hypoglykemier.getForekomstTrafik() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        // R10
        if (eligibleForRule10(utlatande)) {
            if (!ValidatorUtil.validateDate(hypoglykemier.getForekomstTrafikTidpunkt(), validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID, null)) {
                return;
            }
        }

        // R22 The date should be within the last 12 months
        if (eligibleForRule22(utlatande)) {
            InternalDate minDateBack = new InternalDate(LocalDate.now().minusMonths(RULE_20_22_TWELVE_MONTHS));
            InternalDate givenDate = utlatande.getHypoglykemier().getForekomstTrafikTidpunkt();
            if (givenDate.beforeMinDateOrInFuture(minDateBack.asLocalDate())) {
                addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                    HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID,
                    ValidationMessageType.OTHER, "common.validation.d-09");
            }
        }
    }

    private void validateMisstankeOgonSjukdom(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSynfunktion().getMisstankeOgonsjukdom() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateOgonbottenFotoSaknas(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSynfunktion().getOgonbottenFotoSaknas() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_OGONBOTTENFOTO_SAKNAS_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateVanster(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden vanster = utlatande.getSynfunktion().getVanster();
        if (vanster == null) {
            // R12
            if (eligibleForRule12(utlatande)) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID,
                    ValidationMessageType.EMPTY);
            }
            return;
        }

        // 8.2
        if (eligibleForRule12(utlatande) && vanster.getUtanKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY);
        }

        // R19
        if (eligibleForRule19(utlatande, VANSTER_UTAN_KORREKTION) && !validateMaxSynskarpeVarde(vanster.getUtanKorrektion())) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.ue-synskarpa.invalid_format");
        }

        // 8.5
        if ((eligibleForRule13(utlatande) || eligibleForRule14(utlatande) || eligibleForRule15(utlatande))
            && vanster.getMedKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY);

        }

        // R19
        if (eligibleForRule19(utlatande, VANSTER_MED_KORREKTION) && !validateMaxSynskarpeVarde(vanster.getMedKorrektion())) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.ue-synskarpa.invalid_format");
        }
    }

    private void validateHoger(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden hoger = utlatande.getSynfunktion().getHoger();
        if (hoger == null) {
            // R12
            if (eligibleForRule12(utlatande)) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID,
                    ValidationMessageType.EMPTY);
            }
            return;
        }

        // 8.1
        if (eligibleForRule12(utlatande) && hoger.getUtanKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY);
        }

        // R19
        if (eligibleForRule19(utlatande, HOGER_UTAN_KORREKTION) && !validateMaxSynskarpeVarde(hoger.getUtanKorrektion())) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.ue-synskarpa.invalid_format");
        }

        // 8.4
        if ((eligibleForRule13(utlatande) || eligibleForRule14(utlatande) || eligibleForRule15(utlatande))
            && hoger.getMedKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY);
        }

        // R19
        if (eligibleForRule19(utlatande, HOGER_MED_KORREKTION) && !validateMaxSynskarpeVarde(hoger.getMedKorrektion())) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.ue-synskarpa.invalid_format");
        }
    }

    private void validateBinokulart(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden binokulart = utlatande.getSynfunktion().getBinokulart();
        if (binokulart == null) {
            // R12
            if (eligibleForRule12(utlatande)) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID,
                    ValidationMessageType.EMPTY);
            }
            return;
        }

        // 8.3
        if (eligibleForRule12(utlatande) && binokulart.getUtanKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY);
        }

        // R19
        if (eligibleForRule19(utlatande, BINOKULART_UTAN_KORREKTION) && !validateMaxSynskarpeVarde(binokulart.getUtanKorrektion())) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.ue-synskarpa.invalid_format");
        }

        // 8.6
        if ((eligibleForRule13(utlatande) || eligibleForRule14(utlatande) || eligibleForRule15(utlatande))
            && binokulart.getMedKorrektion() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY);
        }

        // R19
        if (eligibleForRule19(utlatande, BINOKULART_MED_KORREKTION) && !validateMaxSynskarpeVarde(binokulart.getMedKorrektion())) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + "."
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.ue-synskarpa.invalid_format");
        }
    }

    private void validateUppfyllerBehorighetskrav(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Set<BedomningKorkortstyp> behorighet = utlatande.getBedomning().getUppfyllerBehorighetskrav();
        // Minst 1 behörighetskrav behöver vara markerat.
        if (behorighet == null || behorighet.isEmpty()) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                BEDOMNING_JSON_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateLampligtInnehav(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (eligibleForRule1(utlatande) && utlatande.getBedomning().getLampligtInnehav() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_JSON_ID + "." + BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateBorUndersokasBeskrivning(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        // Optional, därför ingen validering.
    }

    private void validateBlanksForOptionalFields(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_DELSVAR_JSON_ID, ValidationMessageType.EMPTY,
                "ts-diabetes.validation.blanksteg.otillatet");
        }
    }

    private static boolean validateMaxSynskarpeVarde(final double synskarpeVarde) {
        final double maxSynskarpeVarde = 2.0;
        return synskarpeVarde <= maxSynskarpeVarde;
    }

    enum SynfunktionTyp {
        HOGER_UTAN_KORREKTION("8.1"),
        VANSTER_UTAN_KORREKTION("8.2"),
        BINOKULART_UTAN_KORREKTION("8.3"),
        HOGER_MED_KORREKTION("8.4"),
        VANSTER_MED_KORREKTION("8.5"),
        BINOKULART_MED_KORREKTION("8.6");

        private final String delfraga;

        SynfunktionTyp(final String delfraga) {
            this.delfraga = delfraga;
        }

        public String getDelfraga() {
            return delfraga;
        }
    }
}
