/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v4.validator;

import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static se.inera.intyg.common.support.validate.ValidatorUtil.addValidationError;
import static se.inera.intyg.common.support.validate.ValidatorUtil.addValidationErrorWithQuestionId;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_FOR_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;

@Component("ts-diabetes.v4.InternalDraftValidator")
public class InternalDraftValidatorImpl implements InternalDraftValidator<TsDiabetesUtlatandeV4> {

    private static final int MAX_FIFTY_THREE_CHARS = 53;
    private static final int MAX_SEVENTY_ONE_CHARS = 71;
    private static final int MAX_HUNDRED_EIGHTY_NINE_CHARS = 189;

    protected static final String BEHANDLING_ROOT_FIELD_PATH = ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + ".";

    private static final String D_02 = "common.validation.d-02";
    private static final String B_02B = "common.validation.b-02b";
    private static final String D_08 = "common.validation.d-08";
    private static final String D_11 = "common.validation.d-11";
    private static final String D_12 = "common.validation.d-12";

    private static final Set<KorkortsbehorighetKod> HIGHER_LICENCE_TYPES = ImmutableSet.of(
        KorkortsbehorighetKod.C1,
        KorkortsbehorighetKod.C1E,
        KorkortsbehorighetKod.C,
        KorkortsbehorighetKod.CE,
        KorkortsbehorighetKod.D1,
        KorkortsbehorighetKod.D1E,
        KorkortsbehorighetKod.D,
        KorkortsbehorighetKod.DE,
        KorkortsbehorighetKod.TAXI
    );

    // R3
    private static boolean eligibleForRule3(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getAllmant() != null
            && utlatande.getAllmant().getTypAvDiabetes() == KvTypAvDiabetes.ANNAN;
    }

    // R8
    private static boolean eligibleForRule8(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null && isTrue(utlatande.getHypoglykemi().getAterkommandeSenasteAret());
    }

    // R9
    private static boolean eligibleForRule9(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null && isTrue(utlatande.getHypoglykemi().getAterkommandeVaketSenasteTolv());
    }

    // R18
    private static boolean eligibleForRule18(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getAnnan());
    }

    // R19
    private static boolean eligibleForRule19(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getAllmant() != null
            && utlatande.getAllmant().getMedicineringMedforRiskForHypoglykemiTidpunkt() != null;
    }

    // R20
    private static boolean eligibleForRule20(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null
            && utlatande.getHypoglykemi().getAterkommandeSenasteAretTidpunkt() != null;
    }

    // R21
    private static boolean eligibleForRule21(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null
            && utlatande.getHypoglykemi().getAterkommandeVaketSenasteTreTidpunkt() != null;
    }

    // R22
    private static boolean eligibleForRule22(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null
            && utlatande.getHypoglykemi().getAllvarligSenasteTolvManadernaTidpunkt() != null;
    }

    private static boolean eligibleForRule27(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null
            && utlatande.getHypoglykemi().getKontrollSjukdomstillstand() != null
            && isFalse(utlatande.getHypoglykemi().getKontrollSjukdomstillstand());
    }

    private static boolean eligibleForRule28(TsDiabetesUtlatandeV4 utlatande) {
        if (utlatande.getIntygAvser() == null || utlatande.getIntygAvser().getKategorier() == null) {
            return false;
        }
        final var selectedKategorier = getLicenceTypes(utlatande.getIntygAvser().getKategorier());
        return !Collections.disjoint(selectedKategorier, HIGHER_LICENCE_TYPES);
    }

    private static boolean eligibleForRule29(TsDiabetesUtlatandeV4 utlatande) {
        final var ovrigt = utlatande.getOvrigt();
        return ovrigt != null && ovrigt.getKomplikationerAvSjukdomen() != null && isTrue(ovrigt.getKomplikationerAvSjukdomen());
    }

    private static boolean eligibleForRule30(TsDiabetesUtlatandeV4 utlatande) {
        final var allmant = utlatande.getAllmant();
        return allmant != null && allmant.getMedicineringMedforRiskForHypoglykemi() != null
            && isTrue(allmant.getMedicineringMedforRiskForHypoglykemi());
    }

    private static boolean eligibleForRule32(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getAllmant() != null && isTrue(utlatande.getAllmant().getMedicineringForDiabetes());
    }

    private static boolean eligibleForRule33(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null
            && utlatande.getHypoglykemi().getAterkommandeVaketSenasteTre() != null
            && isTrue(utlatande.getHypoglykemi().getAterkommandeVaketSenasteTre());
    }

    private static boolean eligibleForRule34(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null
            && utlatande.getHypoglykemi().getAllvarligSenasteTolvManaderna() != null
            && isTrue(utlatande.getHypoglykemi().getAllvarligSenasteTolvManaderna());
    }

    private static boolean eligibleForRule35(TsDiabetesUtlatandeV4 utlatande) {
        if (utlatande.getBedomning() == null
            || utlatande.getBedomning().getUppfyllerBehorighetskrav() == null
            || utlatande.getBedomning().getUppfyllerBehorighetskrav().isEmpty()
            || utlatande.getIntygAvser() == null
            || utlatande.getIntygAvser().getKategorier() == null
            || utlatande.getIntygAvser().getKategorier().isEmpty()) {
            return false;
        }

        final var selectedBehorigheter = getLicenceTypes(utlatande.getBedomning().getUppfyllerBehorighetskrav());
        return !Collections.disjoint(selectedBehorigheter, HIGHER_LICENCE_TYPES);
    }

    @Override
    public ValidateDraftResponse validateDraft(TsDiabetesUtlatandeV4 utlatande) {
        final var validationMessages = new ArrayList<ValidationMessage>();

        // Kategori 1 - Intyget avser
        validateIntygetAvser(utlatande, validationMessages);

        // Kategori 2 - Identitet
        validateIdentitetStyrkt(utlatande, validationMessages);

        // Kategori 3 - Allmänt
        if (utlatande.getAllmant() != null) {
            validatePatientenFoljsAv(utlatande, validationMessages);
            validateDiagnosAr(utlatande, validationMessages);
            validateTypAvDiabetes(utlatande, validationMessages);
            validateMedicineringForDiabetes(utlatande, validationMessages);
            validateMedicineringMedforRiskForHypoglykemi(utlatande, validationMessages);
            validateBehandling(utlatande, validationMessages);
            validateMedicineringMedforRiskForHypoglykemiTidpunkt(utlatande, validationMessages);
        } else {
            addValidationError(validationMessages, ALLMANT_CATEGORY_ID, ALLMANT_JSON_ID, ValidationMessageType.EMPTY);
        }

        // Kategori 4 - Hypoglykemi
        if (eligibleForRule28(utlatande) || eligibleForRule30(utlatande)) {
            if (utlatande.getHypoglykemi() == null) {
                addValidationError(validationMessages, HYPOGLYKEMI_CATEGORY_ID, HYPOGLYKEMI_CATEGORY_ID, ValidationMessageType.EMPTY);
            } else {
                if (eligibleForRule30(utlatande)) {
                    validateKontrollSjukdomstillstand(utlatande, validationMessages);
                    validateForstarRiskerMedHypoglykemi(utlatande, validationMessages);
                    validateFormagaKannaVarningsTecken(utlatande, validationMessages);
                    validateVidtaAdekvataAtgarder(utlatande, validationMessages);
                    validateAterkommandeSenasteAret(utlatande, validationMessages);
                    validateAterkommandeVaketSenasteTolv(utlatande, validationMessages);
                }
                if (eligibleForRule28(utlatande)) {
                    validateAllvarligSenasteTolvManaderna(utlatande, validationMessages);
                    validateRegelbundnaBlodsockerkontroller(utlatande, validationMessages);
                }
            }
        }

        // Kategori 6 – Övrigt
        if (utlatande.getOvrigt() != null) {
            validateKomplikationerAvSjukdomen(utlatande, validationMessages);
            validateBorBedomasAvSpecialist(utlatande, validationMessages);
        } else {
            addValidationError(validationMessages, OVRIGT_CATEGORY_ID, OVRIGT_CATEGORY_ID, ValidationMessageType.EMPTY);
        }

        // Kategori 7 - Bedömning
        if (utlatande.getBedomning() != null) {
            validateUppfyllerBehorighetskrav(utlatande, validationMessages);
            validateOvrigaKommentarer(utlatande, validationMessages);
        } else {
            addValidationError(validationMessages, BEDOMNING_CATEGORY_ID, BEDOMNING_CATEGORY_ID, ValidationMessageType.EMPTY);
        }

        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateIntygetAvser(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var intygAvser = utlatande.getIntygAvser() == null ? null : utlatande.getIntygAvser().getKategorier();
        if (intygAvser == null || intygAvser.isEmpty()) {
            addValidationErrorWithQuestionId(validationMessages, INTYG_AVSER_CATEGORY_ID,
                INTYG_AVSER_SVAR_JSON_ID + ".kategorier", ValidationMessageType.EMPTY, INTYG_AVSER_SVAR_ID);
        }
    }

    private void validateIdentitetStyrkt(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getIdentitetStyrktGenom() == null) {
            addValidationErrorWithQuestionId(validationMessages, IDENTITET_CATEGORY_ID, IDENTITET_STYRKT_GENOM_JSON_ID + ".typ",
                ValidationMessageType.EMPTY, IDENTITET_STYRKT_GENOM_SVAR_ID);
        }
    }

    private void validatePatientenFoljsAv(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var allmant = Objects.requireNonNull(utlatande.getAllmant());
        if (allmant.getPatientenFoljsAv() == null) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID,
                ALLMANT_JSON_ID + "." + ALLMANT_PATIENTEN_FOLJS_AV_JSON_ID, ValidationMessageType.EMPTY,
                ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID);
        }
    }

    private void validateDiagnosAr(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var diabetesSedanArFieldPath = ALLMANT_JSON_ID + "." + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
        final var allmant = Objects.requireNonNull(utlatande.getAllmant());
        final var cleanedDiabetesDiagnosAr = Strings.nullToEmpty(allmant.getDiabetesDiagnosAr()).trim();
        if (cleanedDiabetesDiagnosAr.isEmpty()) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID, diabetesSedanArFieldPath, ValidationMessageType.EMPTY,
                B_02B, ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID);
            return;
        }

        Year parsedYear;
        try {
            parsedYear = Year.parse(cleanedDiabetesDiagnosAr);
        } catch (DateTimeParseException e) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID, diabetesSedanArFieldPath,
                ValidationMessageType.INVALID_FORMAT, B_02B, ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID);
            return;
        }

        // R2
        if (ValidatorUtil.isYearBeforeBirth(parsedYear, utlatande.getGrundData().getPatient().getPersonId())) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID, diabetesSedanArFieldPath,
                ValidationMessageType.OTHER, D_02, ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID);
        }
        if (parsedYear.isAfter(Year.now())) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID, diabetesSedanArFieldPath,
                ValidationMessageType.OTHER, D_02, ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID);
        }
    }

    private void validateTypAvDiabetes(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var typAvDiabetesFieldPath = ALLMANT_JSON_ID + "." + ALLMANT_TYP_AV_DIABETES_JSON_ID;
        final var annanTypAvDiabetesBeskrivningFieldPath = ALLMANT_JSON_ID + "." + ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
        final var allmant = Objects.requireNonNull(utlatande.getAllmant());
        if (allmant.getTypAvDiabetes() == null) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID, typAvDiabetesFieldPath, ValidationMessageType.EMPTY,
                ALLMANT_TYP_AV_DIABETES_SVAR_ID);
        }

        if (eligibleForRule3(utlatande) && Strings.nullToEmpty(allmant.getBeskrivningAnnanTypAvDiabetes()).trim().isEmpty()) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID, annanTypAvDiabetesBeskrivningFieldPath,
                ValidationMessageType.EMPTY, ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID);
        } else if (eligibleForRule3(utlatande)
            && Strings.nullToEmpty(allmant.getBeskrivningAnnanTypAvDiabetes()).trim().length() > MAX_FIFTY_THREE_CHARS) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID, annanTypAvDiabetesBeskrivningFieldPath,
                ValidationMessageType.OTHER, ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID);
        }
    }

    private void validateMedicineringForDiabetes(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var medicineringForDiabetes = ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_FOR_DIABETES_JSON_ID;
        final var allmant = Objects.requireNonNull(utlatande.getAllmant());
        if (allmant.getMedicineringForDiabetes() == null) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID, medicineringForDiabetes, ValidationMessageType.EMPTY,
                ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID);
        }
    }

    private void validateMedicineringMedforRiskForHypoglykemi(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var allmant = Objects.requireNonNull(utlatande.getAllmant());
        if (eligibleForRule32(utlatande) && allmant.getMedicineringMedforRiskForHypoglykemi() == null) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID,
                ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID,
                ValidationMessageType.EMPTY, ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID);
        }
    }

    private void validateBehandling(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var allmant = Objects.requireNonNull(utlatande.getAllmant());
        if (eligibleForRule30(utlatande)) {
            final var behandling = allmant.getBehandling();
            if (behandling == null) {
                addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID,
                    ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID, ValidationMessageType.EMPTY,
                    "common.validation.ue-checkgroup.empty", ALLMANT_BEHANDLING_SVAR_ID);
                return;
            }

            // R4
            if (isNotTrue(behandling.getInsulin())
                && isNotTrue(behandling.getTabletter())
                && isNotTrue(behandling.getAnnan())) {
                addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID,
                    ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID,
                    ValidationMessageType.EMPTY, "common.validation.ue-checkgroup.empty", ALLMANT_BEHANDLING_SVAR_ID);
            }

            // R18: Om annan , ange vilken.
            if (eligibleForRule18(utlatande) && Strings.nullToEmpty(behandling.getAnnanAngeVilken()).trim().isEmpty()) {
                addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID,
                    BEHANDLING_ROOT_FIELD_PATH + ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID,
                    ValidationMessageType.EMPTY, ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID);
            } else if (eligibleForRule18(utlatande)
                && Strings.nullToEmpty(behandling.getAnnanAngeVilken()).trim().length() > MAX_FIFTY_THREE_CHARS) {
                addValidationError(validationMessages, ALLMANT_CATEGORY_ID,
                    BEHANDLING_ROOT_FIELD_PATH + ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID, ValidationMessageType.OTHER,
                    ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID);
            }
        }
    }

    private void validateMedicineringMedforRiskForHypoglykemiTidpunkt(TsDiabetesUtlatandeV4 utlatande,
        List<ValidationMessage> validationMessages) {
        final var allmant = Objects.requireNonNull(utlatande.getAllmant());
        if (eligibleForRule30(utlatande) && allmant.getMedicineringMedforRiskForHypoglykemiTidpunkt() == null) {
            addValidationErrorWithQuestionId(validationMessages, ALLMANT_CATEGORY_ID,
                ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID,
                ValidationMessageType.EMPTY, ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_SVAR_ID);
        }

        if (eligibleForRule19(utlatande)) {
            final var patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId());
            validateDateWithinIntervalWithQuestionId(allmant.getMedicineringMedforRiskForHypoglykemiTidpunkt(),
                patientBirthDate, LocalDate.now(), validationMessages, ALLMANT_CATEGORY_ID,
                ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID,
                ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_SVAR_ID);
        }
    }

    private void validateKontrollSjukdomstillstand(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = Objects.requireNonNull(utlatande.getHypoglykemi());
        if (hypoglykemi.getKontrollSjukdomstillstand() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID);
            return;
        }

        if (eligibleForRule27(utlatande) && Strings.nullToEmpty(hypoglykemi.getKontrollSjukdomstillstandVarfor()).trim().isEmpty()) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID, HYPOGLYKEMI_CATEGORY_ID + "."
                    + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID);
        } else if (eligibleForRule27(utlatande)
            && Strings.nullToEmpty(hypoglykemi.getKontrollSjukdomstillstandVarfor()).trim().length() > MAX_FIFTY_THREE_CHARS) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID, HYPOGLYKEMI_CATEGORY_ID + "."
                    + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID, ValidationMessageType.OTHER,
                HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID);
        }
    }

    private void validateForstarRiskerMedHypoglykemi(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = Objects.requireNonNull(utlatande.getHypoglykemi());
        if (hypoglykemi.getForstarRiskerMedHypoglykemi() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID);
        }
    }

    private void validateFormagaKannaVarningsTecken(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = Objects.requireNonNull(utlatande.getHypoglykemi());
        if (hypoglykemi.getFormagaKannaVarningstecken() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID);
        }
    }

    private void validateVidtaAdekvataAtgarder(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = Objects.requireNonNull(utlatande.getHypoglykemi());
        if (hypoglykemi.getVidtaAdekvataAtgarder() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID);
        }
    }

    private void validateAterkommandeSenasteAret(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = Objects.requireNonNull(utlatande.getHypoglykemi());
        if (hypoglykemi.getAterkommandeSenasteAret() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID);
            return;
        }

        if (eligibleForRule8(utlatande) || eligibleForRule20(utlatande)) {
            final var patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId());
            validateDateWithinIntervalWithQuestionId(hypoglykemi.getAterkommandeSenasteAretTidpunkt(), patientBirthDate, LocalDate.now(),
                validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID,
                HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID);
        }

        if (eligibleForRule8(utlatande) && hypoglykemi.getAterkommandeSenasteAretKontrolleras() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID);
        }

        if (eligibleForRule8(utlatande) && hypoglykemi.getAterkommandeSenasteAretTrafik() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID);
        }
    }

    private void validateAterkommandeVaketSenasteTolv(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = Objects.requireNonNull(utlatande.getHypoglykemi());
        if (hypoglykemi.getAterkommandeVaketSenasteTolv() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID);
            return;
        }

        if (eligibleForRule9(utlatande) && hypoglykemi.getAterkommandeVaketSenasteTre() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID);
            return;
        }

        if (eligibleForRule33(utlatande) || eligibleForRule21(utlatande)) {
            final var patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId());
            validateDateWithinIntervalWithQuestionId(hypoglykemi.getAterkommandeVaketSenasteTreTidpunkt(), patientBirthDate,
                LocalDate.now(), validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID,
                HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID);
        }
    }

    private void validateAllvarligSenasteTolvManaderna(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = Objects.requireNonNull(utlatande.getHypoglykemi());
        if (hypoglykemi.getAllvarligSenasteTolvManaderna() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID);
            return;
        }

        if (eligibleForRule34(utlatande) || eligibleForRule22(utlatande)) {
            final var patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId());
            validateDateWithinIntervalWithQuestionId(hypoglykemi.getAllvarligSenasteTolvManadernaTidpunkt(), patientBirthDate,
                LocalDate.now(), validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID,
                HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID);
        }
    }

    private void validateRegelbundnaBlodsockerkontroller(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = Objects.requireNonNull(utlatande.getHypoglykemi());
        if (hypoglykemi.getRegelbundnaBlodsockerkontroller() == null) {
            addValidationErrorWithQuestionId(validationMessages, HYPOGLYKEMI_CATEGORY_ID,
                HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID, ValidationMessageType.EMPTY,
                HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID);
        }
    }

    private void validateKomplikationerAvSjukdomen(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var ovrigt = Objects.requireNonNull(utlatande.getOvrigt());
        if (ovrigt.getKomplikationerAvSjukdomen() == null) {
            addValidationErrorWithQuestionId(validationMessages, OVRIGT_CATEGORY_ID,
                OVRIGT_CATEGORY_ID + "." + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID, ValidationMessageType.EMPTY,
                OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID);
            return;
        }

        if (eligibleForRule29(utlatande) && Strings.nullToEmpty(ovrigt.getKomplikationerAvSjukdomenAnges()).trim().isEmpty()) {
            addValidationErrorWithQuestionId(validationMessages, OVRIGT_CATEGORY_ID, OVRIGT_CATEGORY_ID + "."
                    + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID, ValidationMessageType.EMPTY,
                OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID);
        } else if (eligibleForRule29(utlatande)
            && Strings.nullToEmpty(ovrigt.getKomplikationerAvSjukdomenAnges()).trim().length() > MAX_HUNDRED_EIGHTY_NINE_CHARS) {
            addValidationErrorWithQuestionId(validationMessages, OVRIGT_CATEGORY_ID, OVRIGT_CATEGORY_ID + "."
                    + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID, ValidationMessageType.OTHER,
                OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID);
        }
    }

    private void validateBorBedomasAvSpecialist(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var shouldBeExaminedBySpecialist = Objects.requireNonNull(utlatande.getOvrigt()).getBorUndersokasAvSpecialist();
        if (shouldBeExaminedBySpecialist != null && shouldBeExaminedBySpecialist.length() > MAX_SEVENTY_ONE_CHARS) {
            addValidationErrorWithQuestionId(validationMessages, OVRIGT_CATEGORY_ID, OVRIGT_CATEGORY_ID + "."
                    + OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_JSON_ID, ValidationMessageType.OTHER,
                OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_SVAR_ID);
        }
    }

    private void validateUppfyllerBehorighetskrav(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        // Minst 1 behörighetskrav behöver vara markerat.
        if (utlatande.getBedomning() == null || utlatande.getBedomning().getUppfyllerBehorighetskrav() == null || utlatande.getBedomning()
            .getUppfyllerBehorighetskrav().isEmpty()) {
            addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                BEDOMNING_CATEGORY_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID,
                ValidationMessageType.EMPTY, BEDOMNING_SVAR_ID);
            return;
        }

        final var selectedBehorigheter = getLicenceTypes(utlatande.getBedomning().getUppfyllerBehorighetskrav());
        if (selectedBehorigheter.contains(KorkortsbehorighetKod.KANINTETASTALLNING) && selectedBehorigheter.size() > 1) {
            addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                BEDOMNING_CATEGORY_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID, ValidationMessageType.INCORRECT_COMBINATION,
                BEDOMNING_SVAR_ID);
        }

        if (eligibleForRule35(utlatande)) {
            final var intygAvser = Objects.requireNonNull(utlatande.getIntygAvser());
            final var selectedKategorier = getLicenceTypes(intygAvser.getKategorier());
            final var selectedBehorigheterMutable = new HashSet<>(selectedBehorigheter);
            selectedBehorigheterMutable.retainAll(HIGHER_LICENCE_TYPES);
            if (!selectedKategorier.containsAll(selectedBehorigheterMutable)) {
                addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                    BEDOMNING_CATEGORY_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION, D_12, BEDOMNING_SVAR_ID);
            }
        }
    }

    private void validateOvrigaKommentarer(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var commentsAndInfo = Objects.requireNonNull(utlatande.getBedomning()).getOvrigaKommentarer();
        if (commentsAndInfo != null && commentsAndInfo.length() > MAX_HUNDRED_EIGHTY_NINE_CHARS) {
            addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID, BEDOMNING_CATEGORY_ID + "."
                + BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID, ValidationMessageType.OTHER, BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID);
        }
    }

    private static <T extends Enum<?>> Set<KorkortsbehorighetKod> getLicenceTypes(Set<T> enumCodes) {
        if (enumCodes == null) {
            return ImmutableSet.of();
        }
        return enumCodes.stream().map(code -> KorkortsbehorighetKod.fromCode(code.name())).collect(ImmutableSet.toImmutableSet());
    }

    private void validateDateWithinIntervalWithQuestionId(InternalDate dateToValidate, LocalDate notBeforeDate, LocalDate notAfterDate,
        List<ValidationMessage> validationMessages, String category, String field, String questionId) {

        if (dateToValidate == null) {
            addValidationErrorWithQuestionId(validationMessages, category, field, ValidationMessageType.EMPTY, questionId);
            return;
        }

        if (!dateToValidate.isValidDate()) {
            if (dateToValidate.isCorrectFormat()) {
                addValidationErrorWithQuestionId(validationMessages, category, field, ValidationMessageType.INVALID_FORMAT,
                    "common.validation.date_invalid", questionId);
            } else {
                addValidationErrorWithQuestionId(validationMessages, category, field, ValidationMessageType.INVALID_FORMAT, questionId);
            }
            return;
        }

        final var dateToValidateLocalDate = dateToValidate.asLocalDate();
        if (dateToValidateLocalDate.isBefore(notBeforeDate)) {
            addValidationErrorWithQuestionId(validationMessages, category, field, ValidationMessageType.OTHER,
                InternalDraftValidatorImpl.D_11,
                questionId);
            return;
        }
        if (dateToValidateLocalDate.isAfter(notAfterDate)) {
            addValidationErrorWithQuestionId(validationMessages, category, field, ValidationMessageType.OTHER,
                InternalDraftValidatorImpl.D_08, questionId);
        }
    }
}
