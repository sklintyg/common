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

import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static se.inera.intyg.common.support.validate.ValidatorUtil.addValidationError;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_RISK_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_BOR_UNDERSOKAS_JSON_ID;
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
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.INTYGETAVSER_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.OVRIGT_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;
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

@Component("ts-diabetes.v3.InternalDraftValidator")
public class InternalDraftValidatorImpl implements InternalDraftValidator<TsDiabetesUtlatandeV3> {

    private static final double RULE_13_CUTOFF = 0.5;
    private static final double RULE_14_CUTOFF = 0.8;
    private static final double RULE_15_CUTOFF = 0.1;
    private static final double MAX_SYNSKARPA_VARDE = 2.0;
    private static final int MAX_OVRIGT_CHARS = 189;
    private static final int MAX_ANNAN_BEHANDLING_CHARS = 53;
    private static final int MAX_ANNAN_DIABETES_CHARS = 53;
    private static final int MAX_UNDERSOKAS_SPECIALIST_CHARS = 71;

    protected static final String CATEGORY_INTYGET_AVSER_BEHORIGHET = "intygAvser";
    protected static final String CATEGORY_ALLMANT = "allmant";
    protected static final String CATEGORY_IDENTITET = "identitetStyrktGenom";
    protected static final String CATEGORY_HYPOGLYKEMIER = "hypoglykemier";
    protected static final String CATEGORY_SYNFUNKTION = "synfunktion";
    protected static final String CATEGORY_OVRIGT = "ovrigt";
    protected static final String CATEGORY_BEDOMNING = "bedomning";
    protected static final String BEHANDLING_ROOT_FIELD_PATH = ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + ".";

    private static final String D_02 = "common.validation.d-02";
    private static final String B_02B = "common.validation.b-02b";
    private static final String D_03 = "common.validation.d-03";
    private static final String D_08 = "common.validation.d-08";
    private static final String D_11 = "common.validation.d-11";

    private static final Set<IntygAvserKategori> RULE_1_14_15_LICENSE_SET = ImmutableSet.of(
        IntygAvserKategori.IAV1,
        IntygAvserKategori.IAV2,
        IntygAvserKategori.IAV3,
        IntygAvserKategori.IAV4,
        IntygAvserKategori.IAV5,
        IntygAvserKategori.IAV6,
        IntygAvserKategori.IAV7,
        IntygAvserKategori.IAV8,
        IntygAvserKategori.IAV9);

    // R1
    private static boolean eligibleForRule1(TsDiabetesUtlatandeV3 utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        final ImmutableSet<IntygAvserKategori> intygAvser = ImmutableSet.copyOf(utlatande.getIntygAvser().getKategorier());
        return !Collections.disjoint(intygAvser, RULE_1_14_15_LICENSE_SET);
    }

    // R3
    private static boolean eligibleForRule3(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null
            && utlatande.getAllmant().getTypAvDiabetes() == KvTypAvDiabetes.ANNAN;
    }

    // R5
    private static boolean eligibleForRule5(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getInsulin());
    }

    // R6
    private static boolean eligibleForRule6(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getInsulin());
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
            && isFalse(utlatande.getSynfunktion().getMisstankeOgonsjukdom());
    }

    // R13
    private static boolean eligibleForRule13(TsDiabetesUtlatandeV3 utlatande) {
        if (utlatande.getIntygAvser() == null) {
            return false;
        }

        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        if (intygAvser == null || intygAvser.isEmpty()) {
            return false;
        }

        Set<IntygAvserKategori> answerRequiringAdditionalData = ImmutableSet.of(
            IntygAvserKategori.IAV11,
            IntygAvserKategori.IAV12,
            IntygAvserKategori.IAV13,
            IntygAvserKategori.IAV14,
            IntygAvserKategori.IAV15,
            IntygAvserKategori.IAV16,
            IntygAvserKategori.IAV17);
        boolean conditionKorkortstyp = !Collections.disjoint(intygAvser, answerRequiringAdditionalData);
        boolean conditionBinokulartDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getBinokulart() != null
            && utlatande.getSynfunktion().getBinokulart().getUtanKorrektion() != null
            && utlatande.getSynfunktion().getBinokulart().getUtanKorrektion() < RULE_13_CUTOFF;
        return conditionKorkortstyp && conditionBinokulartDaligSyn;
    }

    // R14
    private static boolean eligibleForRule14(TsDiabetesUtlatandeV3 utlatande) {
        if (utlatande.getIntygAvser() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        if (intygAvser == null || intygAvser.isEmpty()) {
            return false;
        }

        boolean conditionKorkortstyp = !Collections.disjoint(intygAvser, RULE_1_14_15_LICENSE_SET);

        boolean conditionVansterOgaDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getVanster() != null
            && utlatande.getSynfunktion().getVanster().getUtanKorrektion() != null
            && utlatande.getSynfunktion().getVanster().getUtanKorrektion() < RULE_14_CUTOFF;

        boolean conditionHogerOgaDaligSyn = utlatande.getSynfunktion() != null && utlatande.getSynfunktion().getHoger() != null
            && utlatande.getSynfunktion().getHoger().getUtanKorrektion() != null
            && utlatande.getSynfunktion().getHoger().getUtanKorrektion() < RULE_14_CUTOFF;

        return conditionKorkortstyp && conditionVansterOgaDaligSyn && conditionHogerOgaDaligSyn;
    }

    // R15
    private static boolean eligibleForRule15(TsDiabetesUtlatandeV3 utlatande) {
        if (utlatande.getIntygAvser() == null) {
            return false;
        }
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser().getKategorier();
        if (intygAvser == null || intygAvser.isEmpty()) {
            return false;
        }

        boolean conditionKorkortstyp = !Collections.disjoint(intygAvser, RULE_1_14_15_LICENSE_SET);
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
        return utlatande != null && utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && (isTrue(utlatande.getAllmant().getBehandling().getTabletter()) || isTrue(
            utlatande.getAllmant().getBehandling().getAnnanBehandling()));
    }

    // R17
    private static boolean eligibleForRule17(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getRiskHypoglykemi());
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
                return optionalUtlatande // 8.2
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getHoger)
                    .map(Synskarpevarden::getUtanKorrektion)
                    .isPresent();
            case VANSTER_UTAN_KORREKTION:
                return optionalUtlatande // 8.3
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getVanster)
                    .map(Synskarpevarden::getUtanKorrektion)
                    .isPresent();
            case BINOKULART_UTAN_KORREKTION:
                return optionalUtlatande // 8.4
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getBinokulart)
                    .map(Synskarpevarden::getUtanKorrektion)
                    .isPresent();
            case HOGER_MED_KORREKTION:
                return optionalUtlatande // 8.5
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getHoger)
                    .map(Synskarpevarden::getMedKorrektion)
                    .isPresent();
            case VANSTER_MED_KORREKTION:
                return optionalUtlatande // 8.6
                    .map(TsDiabetesUtlatandeV3::getSynfunktion)
                    .map(Synfunktion::getVanster)
                    .map(Synskarpevarden::getMedKorrektion)
                    .isPresent();
            case BINOKULART_MED_KORREKTION:
                return optionalUtlatande // 8.7
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
            && utlatande.getHypoglykemier().getAterkommandeSenasteTidpunkt() != null;
    }

    // R21
    private static boolean eligibleForRule21(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null
            && utlatande.getHypoglykemier().getSenasteTidpunktVaken() != null;
    }

    // R22
    private static boolean eligibleForRule22(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null
            && utlatande.getHypoglykemier().getForekomstTrafikTidpunkt() != null;
    }

    private static boolean eligibleForRule25(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.getHypoglykemier() != null
            && utlatande.getHypoglykemier().getNedsattHjarnfunktion() != null
            && isTrue(utlatande.getHypoglykemier().getNedsattHjarnfunktion());
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
        // R6
        if (eligibleForRule6(utlatande) || eligibleForRule17(utlatande)) {
            if (utlatande.getHypoglykemier() != null) {
                validateEgenkontrollBlodsocker(utlatande, validationMessages);
                validateNedsattHjarnFunktion(utlatande, validationMessages);
            } else {
                addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, HYPOGLYKEMIER_JSON_ID, ValidationMessageType.EMPTY);
            }
        }
        if (utlatande.getHypoglykemier() != null) {
            validateSjukdomenUnderKontroll(utlatande, validationMessages);
            validateFormagaVarningsTecken(utlatande, validationMessages);
            validateAterkommandeSenasteAret(utlatande, validationMessages);
            validateAterkommandeSenasteKvartalet(utlatande, validationMessages);
            validateForekomstTrafikSenasteAret(utlatande, validationMessages);
        }

        // Kategori 5 - Synfunktion
        if (utlatande.getSynfunktion() != null) {
            validateMisstankeOgonSjukdom(utlatande, validationMessages);
            validateSynskarpaSkickasSeparat(utlatande, validationMessages);
            validateVanster(utlatande, validationMessages);
            validateHoger(utlatande, validationMessages);
            validateBinokulart(utlatande, validationMessages);
        } else {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION, SYNFUNKTION_JSON_ID, ValidationMessageType.EMPTY);
        }

        // Kategori 6 – Övrigt
        validateOvrigt(utlatande, validationMessages);

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
            addValidationError(validationMessages, CATEGORY_ALLMANT, diabetesSedanArFieldPath, ValidationMessageType.EMPTY, B_02B);
            return;
        }

        Year parsedYear;
        try {
            parsedYear = Year.parse(cleanedDiabetesDiagnosAr);
        } catch (DateTimeParseException e) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, diabetesSedanArFieldPath,
                ValidationMessageType.INVALID_FORMAT, B_02B);
            return;
        }

        // R2
        if (ValidatorUtil.isYearBeforeBirth(parsedYear, utlatande.getGrundData().getPatient().getPersonId())) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, diabetesSedanArFieldPath,
                ValidationMessageType.OTHER, D_02);
        }
        if (parsedYear.isAfter(Year.now())) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, diabetesSedanArFieldPath,
                ValidationMessageType.OTHER, D_02);
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
        } else if (eligibleForRule3(utlatande)
            && Strings.nullToEmpty(allmant.getBeskrivningAnnanTypAvDiabetes()).trim().length() > MAX_ANNAN_DIABETES_CHARS) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, annanTypAvDiabetesBeskrivningFieldPath, ValidationMessageType.OTHER);
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

        // R4
        if (isNotTrue(behandling.getEndastKost())
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
                    ValidationMessageType.EMPTY, B_02B);
            } else {
                Year parsedYear;
                try {
                    parsedYear = Year.parse(cleanedInsulinSedanArString);
                } catch (DateTimeParseException e) {
                    addValidationError(validationMessages, CATEGORY_ALLMANT, insulinSedanArFieldPath,
                        ValidationMessageType.INVALID_FORMAT, B_02B);
                    return;
                }

                // R7: Årtal för 'insulinbehandling sedan' måste vara efter patienten är född, och senast innevarande år
                if (ValidatorUtil.isYearBeforeBirth(parsedYear,
                    utlatande.getGrundData().getPatient().getPersonId())) {
                    addValidationError(validationMessages, CATEGORY_ALLMANT, insulinSedanArFieldPath,
                        ValidationMessageType.OTHER, D_02);
                }
                if (parsedYear.isAfter(Year.now())) {
                    addValidationError(validationMessages, CATEGORY_ALLMANT, insulinSedanArFieldPath,
                        ValidationMessageType.OTHER, D_02);
                }
            }

        }

        // R16: Om tablettbehandling, svara på om tablettbehandling ger risk för hypoglykemi.
        if (eligibleForRule16(utlatande) && utlatande.getAllmant().getBehandling().getRiskHypoglykemi() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT,
                BEHANDLING_ROOT_FIELD_PATH + ALLMANT_BEHANDLING_RISK_HYPOGLYKEMI_JSON_ID,
                ValidationMessageType.EMPTY);
        }

        // R18: Om annan behandling, beskriv annan behandling.
        if (eligibleForRule18(utlatande) && Strings.nullToEmpty(behandling.getAnnanBehandlingBeskrivning()).trim().isEmpty()) {
            addValidationError(validationMessages, CATEGORY_ALLMANT,
                BEHANDLING_ROOT_FIELD_PATH + ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID,
                ValidationMessageType.EMPTY);
        } else if (eligibleForRule18(utlatande)
            && Strings.nullToEmpty(behandling.getAnnanBehandlingBeskrivning()).trim().length() > MAX_ANNAN_BEHANDLING_CHARS) {
            addValidationError(validationMessages, CATEGORY_ALLMANT,
                BEHANDLING_ROOT_FIELD_PATH + ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID, ValidationMessageType.OTHER);
        }
    }

    private void validateSjukdomenUnderKontroll(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (eligibleForRule25(utlatande) && utlatande.getHypoglykemier().getSjukdomenUnderKontroll() == null) {
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

    private void validateFormagaVarningsTecken(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (eligibleForRule25(utlatande) && utlatande.getHypoglykemier().getFormagaVarningstecken() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_JSON_ID,
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
        if (eligibleForRule25(utlatande) && hypoglykemier.getAterkommandeSenasteAret() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        // R8 & R20
        if (eligibleForRule8(utlatande) || eligibleForRule20(utlatande)) {
            validateDateWithinInterval(hypoglykemier.getAterkommandeSenasteTidpunkt(),
                ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId()),
                LocalDate.now(),
                validationMessages,
                CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID,
                D_11,
                D_08);
        }
    }


    private void validateAterkommandeSenasteKvartalet(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();
        if (eligibleForRule25(utlatande) && hypoglykemier.getAterkommandeSenasteKvartalet() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        // R9 & R21
        if (eligibleForRule9(utlatande) || eligibleForRule21(utlatande)) {
            validateDateWithinInterval(hypoglykemier.getSenasteTidpunktVaken(),
                ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId()),
                LocalDate.now(),
                validationMessages,
                CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID,
                D_11,
                D_08);
        }
    }

    private void validateForekomstTrafikSenasteAret(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();
        if (eligibleForRule25(utlatande) && hypoglykemier.getForekomstTrafik() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }

        // R10 & R22
        if (eligibleForRule10(utlatande) || eligibleForRule22(utlatande)) {
            validateDateWithinInterval(hypoglykemier.getForekomstTrafikTidpunkt(),
                ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId()),
                LocalDate.now(),
                validationMessages,
                CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID,
                D_11,
                D_08);
        }
    }

    private void validateMisstankeOgonSjukdom(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSynfunktion().getMisstankeOgonsjukdom() == null) {
            addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateSynskarpaSkickasSeparat(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {

        // R26
        if (isTrue(utlatande.getSynfunktion().getSkickasSeparat())) {
            Synskarpevarden vanster = utlatande.getSynfunktion().getVanster();
            Synskarpevarden hoger = utlatande.getSynfunktion().getHoger();
            Synskarpevarden binokulart = utlatande.getSynfunktion().getBinokulart();

            boolean vansterHasValues = vanster != null && (vanster.getMedKorrektion() != null || vanster.getUtanKorrektion() != null);
            boolean hogerHasValues = hoger != null && (hoger.getMedKorrektion() != null || hoger.getUtanKorrektion() != null);
            boolean binokulartHasValues =
                binokulart != null && (binokulart.getMedKorrektion() != null || binokulart.getUtanKorrektion() != null);

            if (vansterHasValues || hogerHasValues || binokulartHasValues) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION);
            }

        }
    }

    private void validateVanster(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden vanster = utlatande.getSynfunktion().getVanster();
        validateSynskarpaVarden(vanster, "VANSTER", SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID, utlatande, validationMessages);
    }

    private void validateHoger(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden hoger = utlatande.getSynfunktion().getHoger();
        validateSynskarpaVarden(hoger, "HOGER", SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID, utlatande, validationMessages);
    }

    private void validateBinokulart(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        Synskarpevarden binokulart = utlatande.getSynfunktion().getBinokulart();
        validateSynskarpaVarden(binokulart, "BINOKULART", SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID, utlatande, validationMessages);
    }

    private void validateSynskarpaVarden(Synskarpevarden varden, String synvardeTyp, String field, TsDiabetesUtlatandeV3 utlatande,
        List<ValidationMessage> validationMessages) {

        if (isFalse(utlatande.getSynfunktion().getSkickasSeparat())) {
            if (varden == null) {
                // R12
                if (eligibleForRule12(utlatande)) {
                    addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                        SYNFUNKTION_JSON_ID + "." + field,
                        ValidationMessageType.EMPTY);
                }
                return;
            }

            if (eligibleForRule12(utlatande) && varden.getUtanKorrektion() == null) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_JSON_ID + "." + field + "."
                        + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                    ValidationMessageType.EMPTY);
            }

            // R19
            if (eligibleForRule19(utlatande, SynfunktionTyp.valueOf(synvardeTyp + "_UTAN_KORREKTION")) && invalidSynskarpeVarde(
                varden.getUtanKorrektion())) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_JSON_ID + "." + field + "."
                        + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                    ValidationMessageType.INVALID_FORMAT, D_03);
            }

            if ((eligibleForRule13(utlatande) || eligibleForRule14(utlatande) || eligibleForRule15(utlatande))
                && varden.getMedKorrektion() == null) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_JSON_ID + "." + field + "."
                        + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                    ValidationMessageType.EMPTY);

            }

            // R19
            if (eligibleForRule19(utlatande, SynfunktionTyp.valueOf(synvardeTyp + "_MED_KORREKTION")) && invalidSynskarpeVarde(
                varden.getMedKorrektion())) {
                addValidationError(validationMessages, CATEGORY_SYNFUNKTION,
                    (SYNFUNKTION_JSON_ID + "." + field + "."
                        + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                    ValidationMessageType.INVALID_FORMAT, D_03);
            }
        }
    }

    private void validateUppfyllerBehorighetskrav(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        // Minst 1 behörighetskrav behöver vara markerat.
        if (utlatande.getBedomning() == null || utlatande.getBedomning().getUppfyllerBehorighetskrav() == null || utlatande.getBedomning()
            .getUppfyllerBehorighetskrav().isEmpty()) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                BEDOMNING_JSON_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID,
                ValidationMessageType.EMPTY);
            return;
        }
        Set<BedomningKorkortstyp> behorighet = utlatande.getBedomning().getUppfyllerBehorighetskrav();

        if (behorighet.size() > 1 && behorighet.contains(BedomningKorkortstyp.VAR11)) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                BEDOMNING_JSON_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID,
                ValidationMessageType.INCORRECT_COMBINATION);
        }
    }

    private void validateLampligtInnehav(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (eligibleForRule1(utlatande) && utlatande.getBedomning().getLampligtInnehav() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_JSON_ID + "." + BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateBorUndersokasBeskrivning(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getBedomning().getBorUndersokasBeskrivning())) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_BOR_UNDERSOKAS_JSON_ID, ValidationMessageType.BLANK,
                "ts-diabetes.validation.blanksteg.otillatet");
            return;
        }
        if (utlatande.getBedomning().getBorUndersokasBeskrivning() != null
            && utlatande.getBedomning().getBorUndersokasBeskrivning().length() > MAX_UNDERSOKAS_SPECIALIST_CHARS) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_BOR_UNDERSOKAS_JSON_ID, ValidationMessageType.OTHER);
        }
    }

    private void validateOvrigt(TsDiabetesUtlatandeV3 utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_DELSVAR_JSON_ID, ValidationMessageType.BLANK,
                "ts-diabetes.validation.blanksteg.otillatet");
            return;
        }
        if (utlatande.getOvrigt() != null && utlatande.getOvrigt().length() > MAX_OVRIGT_CHARS) {
            addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_DELSVAR_JSON_ID, ValidationMessageType.OTHER);
        }
    }

    private static boolean invalidSynskarpeVarde(final Double synskarpeVarde) {
        return synskarpeVarde == null || synskarpeVarde > MAX_SYNSKARPA_VARDE;
    }

    private boolean validateDateWithinInterval(InternalDate dateToValidate, LocalDate notBeforeDate, LocalDate notAfterDate,
        List<ValidationMessage> validationMessages, String category,
        String field, String notBeforeMessage, String notAfterMessage) {

        if (dateToValidate == null) {
            addValidationError(validationMessages, category, field, ValidationMessageType.EMPTY);
            return false;
        }

        if (!dateToValidate.isValidDate()) {
            if (dateToValidate.isCorrectFormat()) {
                addValidationError(validationMessages, category, field, ValidationMessageType.INVALID_FORMAT,
                    "common.validation.date_invalid");
            } else {
                addValidationError(validationMessages, category, field, ValidationMessageType.INVALID_FORMAT);
            }
            return false;
        }

        LocalDate dateToValidateLocalDate = dateToValidate.asLocalDate();
        if (dateToValidateLocalDate.isBefore(notBeforeDate)) {
            addValidationError(validationMessages, category, field, ValidationMessageType.OTHER, notBeforeMessage);
            return false;
        }
        if (dateToValidateLocalDate.isAfter(notAfterDate)) {
            addValidationError(validationMessages, category, field, ValidationMessageType.OTHER, notAfterMessage);
            return false;
        }

        return true;
    }

    enum SynfunktionTyp {
        HOGER_UTAN_KORREKTION,
        VANSTER_UTAN_KORREKTION,
        BINOKULART_UTAN_KORREKTION,
        HOGER_MED_KORREKTION,
        VANSTER_MED_KORREKTION,
        BINOKULART_MED_KORREKTION
    }
}
