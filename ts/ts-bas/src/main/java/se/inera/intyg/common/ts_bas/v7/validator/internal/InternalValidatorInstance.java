/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v7.validator.internal;

import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.ADHD_ADD_DAMP_ASPERGERS_TOURETTES_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BALANSRUBBNINGAR_YRSEL_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHANDLING_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.DUBBELSEENDE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HJART_ELLER_KARLSJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAKEMEDEL_ORDINERAD_DOS_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.MEDVETANDESTORNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.NEDSATT_NJURFUNKTION_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.NYSTAGMUS_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.OTILLRACKLIG_RORELSEFORMAGA_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PLATS_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROGRESSIV_OGONSJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PSYKISK_SJUKDOM_STORNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.RISKFAKTORER_STROKE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SEENDE_NEDSATT_BELYSNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFALTSDEFEKTER_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_NEUROLOGISK_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_PA_HJARNSKADA_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TIDPUNKT_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UPPFATTA_SAMTALSTAMMA_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARDEN_FOR_SYNSKARPA_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARDINSATSER_MISSBRUK_BEROENDE_DELSVAR_ID;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_bas.v7.model.internal.Bedomning;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_bas.v7.model.internal.Funktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.internal.HjartKarl;
import se.inera.intyg.common.ts_bas.v7.model.internal.HorselBalans;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvser;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_bas.v7.model.internal.Kognitivt;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medicinering;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medvetandestorning;
import se.inera.intyg.common.ts_bas.v7.model.internal.NarkotikaLakemedel;
import se.inera.intyg.common.ts_bas.v7.model.internal.Neurologi;
import se.inera.intyg.common.ts_bas.v7.model.internal.Njurar;
import se.inera.intyg.common.ts_bas.v7.model.internal.Psykiskt;
import se.inera.intyg.common.ts_bas.v7.model.internal.Sjukhusvard;
import se.inera.intyg.common.ts_bas.v7.model.internal.SomnVakenhet;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.common.ts_bas.v7.model.internal.Utvecklingsstorning;
import se.inera.intyg.common.ts_bas.v7.model.internal.Vardkontakt;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;

public class InternalValidatorInstance {

    private static final String CATEGORY_INTYG_AVSER = "intygAvser";
    private static final String CATEGORY_IDENTITET = "identitet";
    private static final String CATEGORY_SYN = "syn";
    private static final String CATEGORY_HORSEL_BALANS = "horselBalans";
    private static final String CATEGORY_FUNKTIONSNEDSATTNING = "funktionsnedsattning";
    private static final String CATEGORY_HJART_KARL = "hjartKarl";
    private static final String CATEGORY_DIABETES = "diabetes";
    private static final String CATEGORY_NEUROLOGI = "neurologi";
    private static final String CATEGORY_MEDVETANDESTORNING = "medvetandestorning";
    private static final String CATEGORY_NJURAR = "njurar";
    private static final String CATEGORY_KOGNITIVT = "kognitivt";
    private static final String CATEGORY_SOMN_VAKENHET = "somnVakenhet";
    private static final String CATEGORY_NARKOTIKA_LAKEMEDEL = "narkotikaLakemedel";
    private static final String CATEGORY_PSYKISKT = "psykiskt";
    private static final String CATEGORY_UTVECKLINGSSTORNING = "utvecklingsstorning";
    private static final String CATEGORY_SJUKHUSVARD = "sjukhusvard";
    private static final String CATEGORY_MEDICINERING = "medicinering";
    private static final String CATEGORY_BEDOMNING = "bedomning";

    private final List<ValidationMessage> validationMessages;

    private ValidationContext context;

    public InternalValidatorInstance() {
        validationMessages = new ArrayList<>();
    }

    /**
     * Validates an internal draft of an {@link TsBasUtlatandeV7} (this means the object being validated is not
     * necessarily
     * complete).
     *
     * @param utlatande an internal {@link TsBasUtlatandeV7}
     * @return a ValidateDraftResponseHolder with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(TsBasUtlatandeV7 utlatande) {

        if (utlatande == null) {
            ValidatorUtil.addValidationError(validationMessages, "utlatande", "utlatande", ValidationMessageType.EMPTY,
                "ts-bas.validation.utlatande.missing");

        } else {

            context = new ValidationContext(utlatande);

            // OBS! Utökas formuläret i framtiden, lägg in validering i rätt ordning nedan.
            validateIntygAvser(utlatande.getIntygAvser());
            validateIdentitetStyrkt(utlatande.getVardkontakt());
            validateSyn(utlatande); // 1.
            validateHorselBalans(utlatande.getHorselBalans()); // 2.
            validateFunktionsnedsattning(utlatande.getFunktionsnedsattning()); // 3.
            validateHjartKarl(utlatande.getHjartKarl()); // 4.
            validateDiabetes(utlatande.getDiabetes()); // 5.
            validateNeurologi(utlatande.getNeurologi()); // 6.
            validateMedvetandestorning(utlatande.getMedvetandestorning()); // 7.
            validateNjurar(utlatande.getNjurar()); // 8.
            validateKognitivt(utlatande.getKognitivt()); // 9.
            validateSomnVakenhet(utlatande.getSomnVakenhet()); // 10.
            validateNarkotikaLakemedel(utlatande.getNarkotikaLakemedel()); // 11.
            validatePsykiskt(utlatande.getPsykiskt()); // 12.
            validateUtvecklingsstorning(utlatande.getUtvecklingsstorning()); // 13.
            validateSjukhusvard(utlatande.getSjukhusvard()); // 14.
            validateMedicinering(utlatande.getMedicinering()); // 15.
            validateBedomning(utlatande.getBedomning());
            ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);
        }

        return new ValidateDraftResponse(ValidatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateIdentitetStyrkt(Vardkontakt vardkontakt) {
        if (vardkontakt == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_IDENTITET, "vardkontakt",
                ValidationMessageType.EMPTY, IDENTITET_STYRKT_GENOM_SVAR_ID);
            return;
        }
        if (vardkontakt.getIdkontroll() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_IDENTITET, "vardkontakt.idkontroll",
                ValidationMessageType.EMPTY, IDENTITET_STYRKT_GENOM_SVAR_ID);
        }
    }

    private void validateUtvecklingsstorning(Utvecklingsstorning utvecklingsstorning) {
        if (utvecklingsstorning == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_UTVECKLINGSSTORNING, "",
                ValidationMessageType.EMPTY, "ts-bas.validation.utvecklingsstorning.missing",
                PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID);
            return;
        }
        if (utvecklingsstorning.getHarSyndrom() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_UTVECKLINGSSTORNING,
                "utvecklingsstorning.harSyndrom", ValidationMessageType.EMPTY, ADHD_ADD_DAMP_ASPERGERS_TOURETTES_DELSVAR_ID);
        }
        if (utvecklingsstorning.getPsykiskUtvecklingsstorning() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_UTVECKLINGSSTORNING,
                "utvecklingsstorning.psykiskUtvecklingsstorning", ValidationMessageType.EMPTY, PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID);
        }

    }

    private void validatePsykiskt(Psykiskt psykiskt) {
        if (psykiskt == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_PSYKISKT, "psykiskt", ValidationMessageType.EMPTY,
                "ts-bas.validation.psykiskt.missing", PSYKISK_SJUKDOM_STORNING_DELSVAR_ID);
            return;
        }

        if (psykiskt.getPsykiskSjukdom() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_PSYKISKT, "psykiskt.psykiskSjukdom",
                ValidationMessageType.EMPTY, PSYKISK_SJUKDOM_STORNING_DELSVAR_ID);
        }
    }

    private void validateSomnVakenhet(SomnVakenhet somnVakenhet) {
        if (somnVakenhet == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SOMN_VAKENHET,
                "somnVakenhet", ValidationMessageType.EMPTY,
                "ts-bas.validation.somnvakenhet.missing", TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID);
            return;
        }
        if (somnVakenhet.getTeckenSomnstorningar() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SOMN_VAKENHET, "somnVakenhet.teckenSomnstorningar",
                ValidationMessageType.EMPTY, TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID);
        }
    }

    private void validateNjurar(Njurar njurar) {
        if (njurar == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NJURAR, "njurar", ValidationMessageType.EMPTY,
                "ts-bas.validation.njurar.missing", NEDSATT_NJURFUNKTION_SVAR_ID);
            return;
        }
        if (njurar.getNedsattNjurfunktion() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NJURAR, "njurar.nedsattNjurfunktion",
                ValidationMessageType.EMPTY, NEDSATT_NJURFUNKTION_SVAR_ID);
        }
    }

    private void validateNeurologi(Neurologi neurologi) {
        if (neurologi == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NEUROLOGI, "neurologi", ValidationMessageType.EMPTY,
                "ts-bas.validation.neurologi.missing", TECKEN_NEUROLOGISK_SJUKDOM_SVAR_ID);
            return;
        }
        if (neurologi.getNeurologiskSjukdom() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NEUROLOGI, "neurologi.neurologiskSjukdom",
                ValidationMessageType.EMPTY,
                "ts-bas.validation.neurologi.neurologisksjukdom.missing", TECKEN_NEUROLOGISK_SJUKDOM_SVAR_ID);
        }
    }

    private void validateSjukhusvard(Sjukhusvard sjukhusvard) {

        if (sjukhusvard == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SJUKHUSVARD,
                "sjukhusvard", ValidationMessageType.EMPTY, "ts-bas.validation.sjukhusvard.missing",
                FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID);
            return;
        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SJUKHUSVARD, "sjukhusvard.sjukhusEllerLakarkontakt",
                ValidationMessageType.EMPTY, FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID);
            return;

        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt()) {
            ValidatorUtil.assertDescriptionNotEmptyWithQuestionId(validationMessages, sjukhusvard.getTidpunkt(), CATEGORY_SJUKHUSVARD,
                "sjukhusvard.tidpunkt", TIDPUNKT_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID);
            ValidatorUtil.assertDescriptionNotEmptyWithQuestionId(validationMessages, sjukhusvard.getVardinrattning(), CATEGORY_SJUKHUSVARD,
                "sjukhusvard.vardinrattning", PLATS_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID);
            ValidatorUtil.assertDescriptionNotEmptyWithQuestionId(validationMessages, sjukhusvard.getAnledning(), CATEGORY_SJUKHUSVARD,
                "sjukhusvard.anledning", ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID);
        }
    }

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_BEDOMNING, "bedomning", ValidationMessageType.EMPTY,
                "ts-bas.validation.bedomning.missing", LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID);
            return;
        }

        if (bedomning.getKorkortstyp() == null || bedomning.getKorkortstyp().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_BEDOMNING, "bedomning.korkortstyp",
                ValidationMessageType.EMPTY, LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID);
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {

        if (diabetes == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIABETES, "diabetes", ValidationMessageType.EMPTY,
                "ts-bas.validation.diabetes.missing", HAR_DIABETES_SVAR_ID);
            return;
        }

        if (diabetes.getHarDiabetes() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIABETES, "diabetes.harDiabetes",
                ValidationMessageType.EMPTY, HAR_DIABETES_SVAR_ID);
            return;
        }
        if (diabetes.getHarDiabetes()) {

            if (diabetes.getDiabetesTyp() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIABETES, "diabetes.diabetesTyp",
                    ValidationMessageType.EMPTY, TYP_AV_DIABETES_SVAR_ID);

            } else if (diabetes.getDiabetesTyp().equals(DiabetesKod.DIABETES_TYP_2.name())) {
                if (isNullOrFalse(diabetes.getInsulin()) && isNullOrFalse(diabetes.getKost()) && isNullOrFalse(diabetes.getTabletter())) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_DIABETES, "diabetes.behandlingsTyp",
                        ValidationMessageType.EMPTY, BEHANDLING_DIABETES_SVAR_ID);
                }
            }
        }
    }

    private boolean isNullOrFalse(Boolean insulin) {
        return insulin == null || !insulin;
    }

    private void validateFunktionsnedsattning(final Funktionsnedsattning funktionsnedsattning) {

        if (funktionsnedsattning == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING, "funktionsnedsattning",
                ValidationMessageType.EMPTY,
                "ts-bas.validation.funktionsnedsattning.missing", SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID);
            return;
        }

        if (funktionsnedsattning.getFunktionsnedsattning() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING,
                "funktionsnedsattning.funktionsnedsattning", ValidationMessageType.EMPTY, SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID);

        } else if (funktionsnedsattning.getFunktionsnedsattning()) {
            ValidatorUtil.assertDescriptionNotEmptyWithQuestionId(validationMessages, funktionsnedsattning.getBeskrivning(),
                CATEGORY_FUNKTIONSNEDSATTNING, "funktionsnedsattning.beskrivning", TYP_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID);
        }

        if (context.isPersontransportContext()) {
            if (funktionsnedsattning.getOtillrackligRorelseformaga() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING,
                    "funktionsnedsattning.otillrackligRorelseformaga",
                    ValidationMessageType.EMPTY, OTILLRACKLIG_RORELSEFORMAGA_SVAR_ID);
            }
        }
    }

    private void validateHjartKarl(final HjartKarl hjartKarl) {

        if (hjartKarl == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_HJART_KARL, "hjartKarl",
                ValidationMessageType.EMPTY,
                "ts-bas.validation.hjartKarl.missing", HJART_ELLER_KARLSJUKDOM_SVAR_ID);
            return;
        }

        if (hjartKarl.getHjartKarlSjukdom() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_HJART_KARL, "hjartKarl.hjartKarlSjukdom",
                ValidationMessageType.EMPTY, HJART_ELLER_KARLSJUKDOM_SVAR_ID);
        }

        if (hjartKarl.getHjarnskadaEfterTrauma() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_HJART_KARL, "hjartKarl.hjarnskadaEfterTrauma",
                ValidationMessageType.EMPTY, TECKEN_PA_HJARNSKADA_SVAR_ID);
        }

        if (hjartKarl.getRiskfaktorerStroke() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_HJART_KARL, "hjartKarl.riskfaktorerStroke",
                ValidationMessageType.EMPTY, RISKFAKTORER_STROKE_SVAR_ID);

        } else if (hjartKarl.getRiskfaktorerStroke()) {
            ValidatorUtil.assertDescriptionNotEmptyWithQuestionId(validationMessages, hjartKarl.getBeskrivningRiskfaktorer(),
                CATEGORY_HJART_KARL, "hjartKarl.beskrivningRiskfaktorer", TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID);
        }
    }

    private void validateHorselBalans(final HorselBalans horselBalans) {

        if (horselBalans == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_HORSEL_BALANS, "horselBalans",
                ValidationMessageType.EMPTY,
                "ts-bas.validation.horselBalans.missing", BALANSRUBBNINGAR_YRSEL_SVAR_ID);
            return;
        }

        if (horselBalans.getBalansrubbningar() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_HORSEL_BALANS, "horselBalans.balansrubbningar",
                ValidationMessageType.EMPTY, BALANSRUBBNINGAR_YRSEL_SVAR_ID);
        }

        if (context.isPersontransportContext()) {
            if (horselBalans.getSvartUppfattaSamtal4Meter() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_HORSEL_BALANS,
                    "horselBalans.svartUppfattaSamtal4Meter",
                    ValidationMessageType.EMPTY, UPPFATTA_SAMTALSTAMMA_SVAR_ID);
            }
        }
    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_INTYG_AVSER, "intygAvser",
                ValidationMessageType.EMPTY, INTYG_AVSER_SVAR_ID_1);
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_INTYG_AVSER, "intygAvser.korkortstyp",
                ValidationMessageType.EMPTY, INTYG_AVSER_SVAR_ID_1);
        }
    }

    private void validateKognitivt(final Kognitivt kognitivt) {

        if (kognitivt == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_KOGNITIVT, "kognitivt", ValidationMessageType.EMPTY,
                "ts-bas.validation.kognitivt.missing", TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_SVAR_ID);
            return;
        }

        if (kognitivt.getSviktandeKognitivFunktion() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_KOGNITIVT, "kognitivt.sviktandeKognitivFunktion",
                ValidationMessageType.EMPTY, TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_SVAR_ID);
        }
    }

    private void validateMedicinering(final Medicinering medicinering) {

        if (medicinering == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_MEDICINERING,
                "medicinering", ValidationMessageType.EMPTY,
                "ts-bas.validation.medicinering.missing", FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID);
            return;
        }

        if (medicinering.getStadigvarandeMedicinering() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_MEDICINERING,
                "medicinering.stadigvarandeMedicinering",
                ValidationMessageType.EMPTY, FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID);
        } else if (medicinering.getStadigvarandeMedicinering()) {
            ValidatorUtil.assertDescriptionNotEmptyWithQuestionId(validationMessages, medicinering.getBeskrivning(),
                CATEGORY_MEDICINERING, "medicinering.beskrivning", FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID);
        }
    }

    private void validateNarkotikaLakemedel(final NarkotikaLakemedel narkotikaLakemedel) {

        if (narkotikaLakemedel == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL,
                "narkotikaLakemedel",
                ValidationMessageType.EMPTY, "ts-bas.validation.narkotikaLakemedel.missing", TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID);
            return;
        }

        if (narkotikaLakemedel.getTeckenMissbruk() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL,
                "narkotikaLakemedel.teckenMissbruk",
                ValidationMessageType.EMPTY, TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID);
        }

        if (narkotikaLakemedel.getForemalForVardinsats() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL,
                "narkotikaLakemedel.foremalforvardinsats",
                ValidationMessageType.EMPTY, VARDINSATSER_MISSBRUK_BEROENDE_DELSVAR_ID);
        }

        if ((narkotikaLakemedel.getTeckenMissbruk() != null && narkotikaLakemedel.getTeckenMissbruk())
            || (narkotikaLakemedel.getForemalForVardinsats() != null && narkotikaLakemedel.getForemalForVardinsats())) {
            if (narkotikaLakemedel.getProvtagningBehovs() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL,
                    "narkotikaLakemedel.provtagningBehovs",
                    ValidationMessageType.EMPTY, PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID);
            }
        }

        if (narkotikaLakemedel.getLakarordineratLakemedelsbruk() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL,
                "narkotikaLakemedel.lakarordineratLakemedelsbruk",
                ValidationMessageType.EMPTY, REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_DELSVAR_ID);

        } else if (narkotikaLakemedel.getLakarordineratLakemedelsbruk()) {
            ValidatorUtil.assertDescriptionNotEmptyWithQuestionId(validationMessages, narkotikaLakemedel.getLakemedelOchDos(),
                CATEGORY_NARKOTIKA_LAKEMEDEL, "narkotikaLakemedel.lakemedelOchDos", LAKEMEDEL_ORDINERAD_DOS_DELSVAR_ID);
        }
    }

    private void validateMedvetandestorning(final Medvetandestorning medvetandestorning) {

        if (medvetandestorning == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_MEDVETANDESTORNING, "medvetandestorning",
                ValidationMessageType.EMPTY, "ts-bas.validation.medvetandestorning.missing", MEDVETANDESTORNING_SVAR_ID);
            return;
        }

        if (medvetandestorning.getMedvetandestorning() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_MEDVETANDESTORNING,
                "medvetandestorning.medvetandestorning",
                ValidationMessageType.EMPTY, MEDVETANDESTORNING_SVAR_ID);
        }
    }

    private void validateSyn(final TsBasUtlatandeV7 utlatande) {
        if (utlatande.getSyn() == null) {
            return;
        }

        final Syn syn = utlatande.getSyn();
        if (syn == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn", ValidationMessageType.EMPTY,
                "ts-bas.validation.syn.missing", SYNFALTSDEFEKTER_SVAR_ID);
            return;
        }

        if (syn.getSynfaltsdefekter() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.synfaltsdefekter",
                ValidationMessageType.EMPTY, SYNFALTSDEFEKTER_SVAR_ID);
        }

        if (syn.getNattblindhet() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.nattblindhet",
                ValidationMessageType.EMPTY, SEENDE_NEDSATT_BELYSNING_SVAR_ID);
        }

        if (syn.getProgressivOgonsjukdom() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.progressivOgonsjukdom",
                ValidationMessageType.EMPTY, PROGRESSIV_OGONSJUKDOM_SVAR_ID);
        }

        if (syn.getDiplopi() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.diplopi", ValidationMessageType.EMPTY,
                DUBBELSEENDE_SVAR_ID);
        }

        if (syn.getNystagmus() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.nystagmus", ValidationMessageType.EMPTY,
                NYSTAGMUS_SVAR_ID);
        }

        if (syn.getSynskarpaSkickasSeparat() == null || !syn.getSynskarpaSkickasSeparat()) { //R37
            if (syn.getHogerOga() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.hogerOga",
                    ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.hogeroga.missing", VARDEN_FOR_SYNSKARPA_ID);
            } else {
                if (syn.getHogerOga().getUtanKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.hogerOga.utanKorrektion",
                        ValidationMessageType.EMPTY, VARDEN_FOR_SYNSKARPA_ID);

                } else if (syn.getHogerOga().getUtanKorrektion() < 0.0 || syn.getHogerOga().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.hogerOga.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds", VARDEN_FOR_SYNSKARPA_ID);
                }

                if (syn.getHogerOga().getMedKorrektion() != null) {
                    if (syn.getHogerOga().getMedKorrektion() < 0.0 || syn.getHogerOga().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.hogerOga.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds", VARDEN_FOR_SYNSKARPA_ID);
                    }
                }
            }

            if (syn.getVansterOga() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.vansterOga",
                    ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.vansteroga.missing", VARDEN_FOR_SYNSKARPA_ID);
            } else {
                if (syn.getVansterOga().getUtanKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.vansterOga.utanKorrektion",
                        ValidationMessageType.EMPTY, VARDEN_FOR_SYNSKARPA_ID);

                } else if (syn.getVansterOga().getUtanKorrektion() < 0.0 || syn.getVansterOga().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.vansterOga.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds", VARDEN_FOR_SYNSKARPA_ID);
                }

                if (syn.getVansterOga().getMedKorrektion() != null) {
                    if (syn.getVansterOga().getMedKorrektion() < 0.0 || syn.getVansterOga().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.vansterOga.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds", VARDEN_FOR_SYNSKARPA_ID);
                    }
                }
            }

            if (syn.getBinokulart() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.binokulart",
                    ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.binokulart.missing", VARDEN_FOR_SYNSKARPA_ID);
            } else {
                if (syn.getBinokulart().getUtanKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.binokulart.utanKorrektion",
                        ValidationMessageType.EMPTY, VARDEN_FOR_SYNSKARPA_ID);

                } else if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.binokulart.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds", VARDEN_FOR_SYNSKARPA_ID);
                }

                if (syn.getBinokulart().getMedKorrektion() != null) {
                    if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds", VARDEN_FOR_SYNSKARPA_ID);
                    }
                }
            }

            validateSynKorrektionsRegler(utlatande, syn);
        } else { //R37
            if (syn.getHogerOga() != null) {
                if (syn.getHogerOga().getUtanKorrektion() != null
                    || syn.getHogerOga().getMedKorrektion() != null
                    || (syn.getHogerOga().getKontaktlins() != null && syn.getHogerOga().getKontaktlins())) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.hogerOga",
                        ValidationMessageType.INCORRECT_COMBINATION, "ts-bas.validation.syn.R37", VARDEN_FOR_SYNSKARPA_ID);
                }
            }
            if (syn.getVansterOga() != null) {
                if (syn.getVansterOga().getUtanKorrektion() != null
                    || syn.getVansterOga().getMedKorrektion() != null
                    || (syn.getVansterOga().getKontaktlins() != null && syn.getVansterOga().getKontaktlins())) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.vansterOga",
                        ValidationMessageType.INCORRECT_COMBINATION, "ts-bas.validation.syn.R37", VARDEN_FOR_SYNSKARPA_ID);
                }
            }
            if (syn.getBinokulart() != null
                && (syn.getBinokulart().getUtanKorrektion() != null || syn.getBinokulart().getMedKorrektion() != null)) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.binokulart",
                    ValidationMessageType.INCORRECT_COMBINATION, "ts-bas.validation.syn.R37", VARDEN_FOR_SYNSKARPA_ID);
            }
        }
    }

    private void validateSynKorrektionsRegler(TsBasUtlatandeV7 utlatande, Syn syn) {
        // CHECKSTYLE:OFF MagicNumber
        if (syn.getBinokulart() != null && syn.getHogerOga() != null && syn.getVansterOga() != null
            && utlatande.getIntygAvser() != null && utlatande.getIntygAvser().getKorkortstyp() != null) {
            // R33
            if (utlatande.getIntygAvser().getKorkortstyp().contains(IntygAvserKategori.IAV10)
                && syn.getBinokulart().getUtanKorrektion() != null
                && syn.getBinokulart().getUtanKorrektion() < 0.5) {
                if (syn.getHogerOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.hogerOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r33", VARDEN_FOR_SYNSKARPA_ID);
                }
                if (syn.getVansterOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.vansterOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r33", VARDEN_FOR_SYNSKARPA_ID);
                }
                if (syn.getBinokulart().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r33", VARDEN_FOR_SYNSKARPA_ID);
                }
                // R34
            } else if (CollectionUtils.containsAny(IntygAvserKategori.getNormalCategories(), utlatande.getIntygAvser().getKorkortstyp())
                && (syn.getHogerOga().getUtanKorrektion() != null && syn.getVansterOga().getUtanKorrektion() != null)
                && (syn.getHogerOga().getUtanKorrektion() < 0.8 && syn.getVansterOga().getUtanKorrektion() < 0.8)
                && (syn.getHogerOga().getUtanKorrektion() >= 0.1 && syn.getVansterOga().getUtanKorrektion() >= 0.1)) {
                if (syn.getHogerOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.hogerOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r34", VARDEN_FOR_SYNSKARPA_ID);
                }
                if (syn.getVansterOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.vansterOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r34", VARDEN_FOR_SYNSKARPA_ID);
                }
                if (syn.getBinokulart().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r34", VARDEN_FOR_SYNSKARPA_ID);
                }
                // R35
            } else if (CollectionUtils.containsAny(IntygAvserKategori.getNormalCategories(), utlatande.getIntygAvser().getKorkortstyp())
                && !(syn.getHogerOga().getUtanKorrektion() == null && syn.getVansterOga().getUtanKorrektion() == null)
                && ((syn.getHogerOga().getUtanKorrektion() == null
                && (syn.getVansterOga().getUtanKorrektion() != null && syn.getVansterOga().getUtanKorrektion() < 0.1))
                || (syn.getVansterOga().getUtanKorrektion() == null
                && (syn.getHogerOga().getUtanKorrektion() != null && syn.getHogerOga().getUtanKorrektion() < 0.1))
                || (syn.getHogerOga().getUtanKorrektion() != null && syn.getHogerOga().getUtanKorrektion() < 0.1)
                || (syn.getVansterOga().getUtanKorrektion() != null && syn.getVansterOga().getUtanKorrektion() < 0.1))
            ) {
                if (syn.getHogerOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.hogerOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r35", VARDEN_FOR_SYNSKARPA_ID);
                }
                if (syn.getVansterOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.vansterOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r35", VARDEN_FOR_SYNSKARPA_ID);
                }
                if (syn.getBinokulart().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r35", VARDEN_FOR_SYNSKARPA_ID);
                }
            }
        }
        // CHECKSTYLE:ON MagicNumber*/
    }
}
