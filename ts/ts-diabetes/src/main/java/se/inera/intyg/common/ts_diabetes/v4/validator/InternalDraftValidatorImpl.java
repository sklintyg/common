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
package se.inera.intyg.common.ts_diabetes.v4.validator;

import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static se.inera.intyg.common.support.validate.ValidatorUtil.addValidationError;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_RISK_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_FOR_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYGETAVSER_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;

@Component("ts-diabetes.v4.InternalDraftValidator")
public class InternalDraftValidatorImpl implements InternalDraftValidator<TsDiabetesUtlatandeV4> {

    private static final int MAX_ANNAN_BEHANDLING_CHARS = 53;
    private static final int MAX_FIFTY_THREE_CHARS = 53;
    private static final int MAX_SEVENTY_ONE_CHARS = 71;
    private static final int MAX_HUNDRED_EIGHTY_NINE_CHARS = 189;

    protected static final String CATEGORY_INTYGET_AVSER_BEHORIGHET = "intygAvser";
    protected static final String CATEGORY_ALLMANT = "allmant";
    protected static final String CATEGORY_IDENTITET = "identitetStyrktGenom";
    protected static final String CATEGORY_HYPOGLYKEMI = "hypoglykemi";
    protected static final String CATEGORY_OVRIGT = "ovrigt";
    protected static final String CATEGORY_BEDOMNING = "bedomning";
    protected static final String BEHANDLING_ROOT_FIELD_PATH = ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + ".";

    private static final String D_02 = "common.validation.d-02";
    private static final String B_02B = "common.validation.b-02b";
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

    private static final Set<KorkortsbehorighetKod> RULE_28_LICENSE_SET = ImmutableSet.of(
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

    // R5
    private static boolean eligibleForRule5(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getInsulin());
    }

    // R8
    private static boolean eligibleForRule8(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null && isTrue(utlatande.getHypoglykemi().getAterkommandeSenasteAret());
    }

    // R9
    private static boolean eligibleForRule9(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getHypoglykemi() != null && isTrue(utlatande.getHypoglykemi().getAterkommandeVaketSenasteTolv());
    }

    // R16
    private static boolean eligibleForRule16(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande != null && utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && (isTrue(utlatande.getAllmant().getBehandling().getTabletter()) || isTrue(
            utlatande.getAllmant().getBehandling().getAnnanBehandling()));
    }

    // R18
    private static boolean eligibleForRule18(TsDiabetesUtlatandeV4 utlatande) {
        return utlatande.getAllmant() != null && utlatande.getAllmant().getBehandling() != null
            && isTrue(utlatande.getAllmant().getBehandling().getAnnanBehandling());
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
        //final ImmutableSet<KorkortsbehorighetKod> intygAvser = ImmutableSet.copyOf(utlatande.getIntygAvser().getKategorier());
        //return !Collections.disjoint(intygAvser, RULE_28_LICENSE_SET);

        final ImmutableSet<IntygAvserKategori> intygAvser = ImmutableSet.copyOf(utlatande.getIntygAvser().getKategorier());
        return !Collections.disjoint(intygAvser, RULE_1_14_15_LICENSE_SET);
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

    @Override
    public ValidateDraftResponse validateDraft(TsDiabetesUtlatandeV4 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

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
        } else {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_JSON_ID, ValidationMessageType.EMPTY);
        }

        // Kategori 4 - Hypoglykemi
        if (eligibleForRule28(utlatande) || eligibleForRule30(utlatande)) {
            if (utlatande.getHypoglykemi() == null) {
                addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI, HYPOGLYKEMI_JSON_ID, ValidationMessageType.EMPTY);
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
            addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_JSON_ID, ValidationMessageType.EMPTY);
        }

        // Kategori 7 - Bedömning
        if (utlatande.getBedomning() != null) {
            validateUppfyllerBehorighetskrav(utlatande, validationMessages);
            validateOvrigaKommentarer(utlatande, validationMessages);
        } else {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_JSON_ID, ValidationMessageType.EMPTY);
        }

        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateIntygetAvser(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        Set<IntygAvserKategori> intygAvser = utlatande.getIntygAvser() == null ? null : utlatande.getIntygAvser().getKategorier();
        if (intygAvser == null || intygAvser.isEmpty()) {
            addValidationError(validationMessages, CATEGORY_INTYGET_AVSER_BEHORIGHET, INTYGETAVSER_SVAR_JSON_ID + ".kategorier",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateIdentitetStyrkt(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getIdentitetStyrktGenom() == null) {
            addValidationError(validationMessages, CATEGORY_IDENTITET, IDENTITET_STYRKT_GENOM_JSON_ID + ".typ",
                ValidationMessageType.EMPTY);
        }
    }

    private void validatePatientenFoljsAv(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getAllmant().getPatientenFoljsAv() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, ALLMANT_JSON_ID + "." + ALLMANT_PATIENTEN_FOLJS_AV_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnosAr(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        String diabetesSedanArFieldPath = ALLMANT_JSON_ID + "." + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
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

    private void validateTypAvDiabetes(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
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
            && Strings.nullToEmpty(allmant.getBeskrivningAnnanTypAvDiabetes()).trim().length() > MAX_FIFTY_THREE_CHARS) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, annanTypAvDiabetesBeskrivningFieldPath, ValidationMessageType.OTHER);
        }
    }

    private void validateMedicineringForDiabetes(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        String medicineringForDiabetes = ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_FOR_DIABETES_JSON_ID;
        Allmant allmant = utlatande.getAllmant();
        if (allmant.getMedicineringForDiabetes() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT, medicineringForDiabetes, ValidationMessageType.EMPTY);
        }
    }

    private void validateMedicineringMedforRiskForHypoglykemi(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        if (eligibleForRule32(utlatande) && utlatande.getAllmant().getMedicineringMedforRiskForHypoglykemi() == null) {
            addValidationError(validationMessages, CATEGORY_ALLMANT,
                ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateBehandling(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
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


    private void validateKontrollSjukdomstillstand(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = utlatande.getHypoglykemi();
        if (hypoglykemi.getKontrollSjukdomstillstand() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID, ValidationMessageType.EMPTY);
            return;
        }

        if (eligibleForRule27(utlatande) && Strings.nullToEmpty(hypoglykemi.getKontrollSjukdomstillstandVarfor()).trim().isEmpty()) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI, HYPOGLYKEMI_JSON_ID + "."
                + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID, ValidationMessageType.EMPTY);
        } else if (eligibleForRule27(utlatande)
            && Strings.nullToEmpty(hypoglykemi.getKontrollSjukdomstillstandVarfor()).trim().length() > MAX_FIFTY_THREE_CHARS) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI, HYPOGLYKEMI_JSON_ID + "."
                + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID, ValidationMessageType.OTHER);
        }
    }

    private void validateForstarRiskerMedHypoglykemi(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemi().getForstarRiskerMedHypoglykemi() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID, ValidationMessageType.EMPTY);
        }
    }

    private void validateFormagaKannaVarningsTecken(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemi().getFormagaKannaVarningstecken() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID, ValidationMessageType.EMPTY);
        }
    }

    private void validateVidtaAdekvataAtgarder(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemi().getVidtaAdekvataAtgarder() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID, ValidationMessageType.EMPTY);
        }
    }

    private void validateAterkommandeSenasteAret(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = utlatande.getHypoglykemi();
        if (hypoglykemi.getAterkommandeSenasteAret() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID, ValidationMessageType.EMPTY);
            return;
        }

        if (eligibleForRule8(utlatande) || eligibleForRule20(utlatande)) {
            final var patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId());
            validateDateWithinInterval(hypoglykemi.getAterkommandeSenasteAretTidpunkt(), patientBirthDate, LocalDate.now(),
                validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, D_11, D_08);
        }

        if (eligibleForRule8(utlatande) && hypoglykemi.getAterkommandeSenasteAretKontrolleras() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID, ValidationMessageType.EMPTY);
        }

        if (eligibleForRule8(utlatande) && hypoglykemi.getAterkommandeSenasteAretTrafik() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID, ValidationMessageType.EMPTY);
        }
    }

    private void validateAterkommandeVaketSenasteTolv(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = utlatande.getHypoglykemi();
        if (hypoglykemi.getAterkommandeVaketSenasteTolv() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID, ValidationMessageType.EMPTY);
            return;
        }

        if (eligibleForRule9(utlatande) && hypoglykemi.getAterkommandeVaketSenasteTre() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID, ValidationMessageType.EMPTY);
            return;
        }

        if (eligibleForRule33(utlatande) || eligibleForRule21(utlatande)) {
            final var patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId());
            validateDateWithinInterval(hypoglykemi.getAterkommandeVaketSenasteTreTidpunkt(), patientBirthDate, LocalDate.now(),
                validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID, D_11, D_08);
        }
    }

    private void validateAllvarligSenasteTolvManaderna(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var hypoglykemi = utlatande.getHypoglykemi();
        if (hypoglykemi.getAllvarligSenasteTolvManaderna() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID, ValidationMessageType.EMPTY);
            return;
        }

        if (eligibleForRule34(utlatande) || eligibleForRule22(utlatande)) {
            final var patientBirthDate = ValidatorUtil.getBirthDateFromPersonnummer(utlatande.getGrundData().getPatient().getPersonId());
            validateDateWithinInterval(hypoglykemi.getAllvarligSenasteTolvManadernaTidpunkt(), patientBirthDate, LocalDate.now(),
                validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID, D_11, D_08);
        }
    }

    private void validateRegelbundnaBlodsockerkontroller(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHypoglykemi().getRegelbundnaBlodsockerkontroller() == null) {
            addValidationError(validationMessages, CATEGORY_HYPOGLYKEMI,
                HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID, ValidationMessageType.EMPTY);
        }
    }

    private void validateKomplikationerAvSjukdomen(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var ovrigt = utlatande.getOvrigt();
        if (ovrigt.getKomplikationerAvSjukdomen() == null) {
            addValidationError(validationMessages, CATEGORY_OVRIGT,
                OVRIGT_JSON_ID + "." + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID, ValidationMessageType.EMPTY);
            return;
        }

        if (eligibleForRule29(utlatande) && Strings.nullToEmpty(ovrigt.getKomplikationerAvSjukdomenAnges()).trim().isEmpty()) {
            addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_JSON_ID + "."
                + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID, ValidationMessageType.EMPTY);
        } else if (eligibleForRule29(utlatande)
            && Strings.nullToEmpty(ovrigt.getKomplikationerAvSjukdomenAnges()).trim().length() > MAX_HUNDRED_EIGHTY_NINE_CHARS) {
            addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_JSON_ID + "."
                + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID, ValidationMessageType.OTHER);
        }
    }

    private void validateBorBedomasAvSpecialist(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var shouldBeExaminedBySpecialist = utlatande.getOvrigt().getBorUndersokasAvSpecialist();
        if (shouldBeExaminedBySpecialist != null && shouldBeExaminedBySpecialist.length() > MAX_SEVENTY_ONE_CHARS) {
            addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_JSON_ID + "."
                + OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_JSON_ID, ValidationMessageType.OTHER);
        }
    }

    private void validateUppfyllerBehorighetskrav(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
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

    private void validateOvrigaKommentarer(TsDiabetesUtlatandeV4 utlatande, List<ValidationMessage> validationMessages) {
        final var commentsAndInfo = utlatande.getBedomning().getOvrigaKommentarer();
        if (commentsAndInfo != null && commentsAndInfo.length() > MAX_HUNDRED_EIGHTY_NINE_CHARS) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEDOMNING_JSON_ID + "."
                + BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID, ValidationMessageType.OTHER);
        }
    }

    // CHECKSTYLE:OFF ParameterNumber
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
    } // CHECKSTYLE:ON ParameterNumber

}
