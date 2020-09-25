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
package se.inera.intyg.common.ts_bas.v7.validator.internal;

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

/**
 * Class for validating drafts of the internal model.
 *
 * @author erik
 */
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
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_IDENTITET, "vardkontakt", ValidationMessageType.EMPTY);
            return;
        }
        if (vardkontakt.getIdkontroll() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_IDENTITET, "vardkontakt.idkontroll",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateUtvecklingsstorning(Utvecklingsstorning utvecklingsstorning) {
        if (utvecklingsstorning == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_UTVECKLINGSSTORNING, "", ValidationMessageType.EMPTY,
                "ts-bas.validation.utvecklingsstorning.missing");
            return;
        }
        if (utvecklingsstorning.getHarSyndrom() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_UTVECKLINGSSTORNING,
                "utvecklingsstorning.harSyndrom", ValidationMessageType.EMPTY);
        }
        if (utvecklingsstorning.getPsykiskUtvecklingsstorning() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_UTVECKLINGSSTORNING,
                "utvecklingsstorning.psykiskUtvecklingsstorning", ValidationMessageType.EMPTY);
        }

    }

    private void validatePsykiskt(Psykiskt psykiskt) {
        if (psykiskt == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_PSYKISKT, "psykiskt", ValidationMessageType.EMPTY,
                "ts-bas.validation.psykiskt.missing");
            return;
        }

        if (psykiskt.getPsykiskSjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_PSYKISKT, "psykiskt.psykiskSjukdom",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateSomnVakenhet(SomnVakenhet somnVakenhet) {
        if (somnVakenhet == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SOMN_VAKENHET, "somnVakenhet", ValidationMessageType.EMPTY,
                "ts-bas.validation.somnvakenhet.missing");
            return;
        }
        if (somnVakenhet.getTeckenSomnstorningar() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SOMN_VAKENHET, "somnVakenhet.teckenSomnstorningar",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateNjurar(Njurar njurar) {
        if (njurar == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NJURAR, "njurar", ValidationMessageType.EMPTY,
                "ts-bas.validation.njurar.missing");
            return;
        }
        if (njurar.getNedsattNjurfunktion() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NJURAR, "njurar.nedsattNjurfunktion",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateNeurologi(Neurologi neurologi) {
        if (neurologi == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NEUROLOGI, "neurologi", ValidationMessageType.EMPTY,
                "ts-bas.validation.neurologi.missing");
            return;
        }
        if (neurologi.getNeurologiskSjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NEUROLOGI, "neurologi.neurologiskSjukdom",
                ValidationMessageType.EMPTY,
                "ts-bas.validation.neurologi.neurologisksjukdom.missing");
        }
    }

    private void validateSjukhusvard(Sjukhusvard sjukhusvard) {

        if (sjukhusvard == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SJUKHUSVARD, "sjukhusvard", ValidationMessageType.EMPTY,
                "ts-bas.validation.sjukhusvard.missing");
            return;
        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SJUKHUSVARD, "sjukhusvard.sjukhusEllerLakarkontakt",
                ValidationMessageType.EMPTY);
            return;

        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, sjukhusvard.getTidpunkt(), CATEGORY_SJUKHUSVARD,
                "sjukhusvard.tidpunkt");
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, sjukhusvard.getVardinrattning(), CATEGORY_SJUKHUSVARD,
                "sjukhusvard.vardinrattning");
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, sjukhusvard.getAnledning(), CATEGORY_SJUKHUSVARD,
                "sjukhusvard.anledning");
        }
    }

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, "bedomning", ValidationMessageType.EMPTY,
                "ts-bas.validation.bedomning.missing");
            return;
        }

        if (bedomning.getKorkortstyp() == null || bedomning.getKorkortstyp().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, "bedomning.korkortstyp", ValidationMessageType.EMPTY);
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {

        if (diabetes == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes", ValidationMessageType.EMPTY,
                "ts-bas.validation.diabetes.missing");
            return;
        }

        if (diabetes.getHarDiabetes() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.harDiabetes", ValidationMessageType.EMPTY);
            return;
        }
        if (diabetes.getHarDiabetes()) {

            if (diabetes.getDiabetesTyp() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.diabetesTyp",
                    ValidationMessageType.EMPTY);

            } else if (diabetes.getDiabetesTyp().equals(DiabetesKod.DIABETES_TYP_2.name())) {
                if (isNullOrFalse(diabetes.getInsulin()) && isNullOrFalse(diabetes.getKost()) && isNullOrFalse(diabetes.getTabletter())) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.behandlingsTyp",
                        ValidationMessageType.EMPTY);
                }
            }
        }
    }

    private boolean isNullOrFalse(Boolean insulin) {
        return insulin == null || !insulin;
    }

    private void validateFunktionsnedsattning(final Funktionsnedsattning funktionsnedsattning) {

        if (funktionsnedsattning == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING, "funktionsnedsattning",
                ValidationMessageType.EMPTY,
                "ts-bas.validation.funktionsnedsattning.missing");
            return;
        }

        if (funktionsnedsattning.getFunktionsnedsattning() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING,
                "funktionsnedsattning.funktionsnedsattning", ValidationMessageType.EMPTY);

        } else if (funktionsnedsattning.getFunktionsnedsattning()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, funktionsnedsattning.getBeskrivning(),
                CATEGORY_FUNKTIONSNEDSATTNING, "funktionsnedsattning.beskrivning");
        }

        if (context.isPersontransportContext()) {
            if (funktionsnedsattning.getOtillrackligRorelseformaga() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING,
                    "funktionsnedsattning.otillrackligRorelseformaga",
                    ValidationMessageType.EMPTY);
            }
        }
    }

    private void validateHjartKarl(final HjartKarl hjartKarl) {

        if (hjartKarl == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HJART_KARL, "hjartKarl", ValidationMessageType.EMPTY,
                "ts-bas.validation.hjartKarl.missing");
            return;
        }

        if (hjartKarl.getHjartKarlSjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HJART_KARL, "hjartKarl.hjartKarlSjukdom",
                ValidationMessageType.EMPTY);
        }

        if (hjartKarl.getHjarnskadaEfterTrauma() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HJART_KARL, "hjartKarl.hjarnskadaEfterTrauma",
                ValidationMessageType.EMPTY);
        }

        if (hjartKarl.getRiskfaktorerStroke() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HJART_KARL, "hjartKarl.riskfaktorerStroke",
                ValidationMessageType.EMPTY);

        } else if (hjartKarl.getRiskfaktorerStroke()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, hjartKarl.getBeskrivningRiskfaktorer(),
                CATEGORY_HJART_KARL, "hjartKarl.beskrivningRiskfaktorer");
        }
    }

    private void validateHorselBalans(final HorselBalans horselBalans) {

        if (horselBalans == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HORSEL_BALANS, "horselBalans", ValidationMessageType.EMPTY,
                "ts-bas.validation.horselBalans.missing");
            return;
        }

        if (horselBalans.getBalansrubbningar() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HORSEL_BALANS, "horselBalans.balansrubbningar",
                ValidationMessageType.EMPTY);
        }

        if (context.isPersontransportContext()) {
            if (horselBalans.getSvartUppfattaSamtal4Meter() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_HORSEL_BALANS, "horselBalans.svartUppfattaSamtal4Meter",
                    ValidationMessageType.EMPTY);
            }
        }
    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_INTYG_AVSER, "intygAvser", ValidationMessageType.EMPTY);
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_INTYG_AVSER, "intygAvser.korkortstyp",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateKognitivt(final Kognitivt kognitivt) {

        if (kognitivt == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_KOGNITIVT, "kognitivt", ValidationMessageType.EMPTY,
                "ts-bas.validation.kognitivt.missing");
            return;
        }

        if (kognitivt.getSviktandeKognitivFunktion() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_KOGNITIVT, "kognitivt.sviktandeKognitivFunktion",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateMedicinering(final Medicinering medicinering) {

        if (medicinering == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINERING, "medicinering", ValidationMessageType.EMPTY,
                "ts-bas.validation.medicinering.missing");
            return;
        }

        if (medicinering.getStadigvarandeMedicinering() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINERING, "medicinering.stadigvarandeMedicinering",
                ValidationMessageType.EMPTY);
        } else if (medicinering.getStadigvarandeMedicinering()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, medicinering.getBeskrivning(),
                CATEGORY_MEDICINERING, "medicinering.beskrivning");
        }
    }

    private void validateNarkotikaLakemedel(final NarkotikaLakemedel narkotikaLakemedel) {

        if (narkotikaLakemedel == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL, "narkotikaLakemedel",
                ValidationMessageType.EMPTY, "ts-bas.validation.narkotikaLakemedel.missing");
            return;
        }

        if (narkotikaLakemedel.getTeckenMissbruk() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL, "narkotikaLakemedel.teckenMissbruk",
                ValidationMessageType.EMPTY);
        }

        if (narkotikaLakemedel.getForemalForVardinsats() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL, "narkotikaLakemedel.foremalforvardinsats",
                ValidationMessageType.EMPTY);
        }

        if ((narkotikaLakemedel.getTeckenMissbruk() != null && narkotikaLakemedel.getTeckenMissbruk())
            || (narkotikaLakemedel.getForemalForVardinsats() != null && narkotikaLakemedel.getForemalForVardinsats())) {
            if (narkotikaLakemedel.getProvtagningBehovs() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL, "narkotikaLakemedel.provtagningBehovs",
                    ValidationMessageType.EMPTY);
            }
        }

        if (narkotikaLakemedel.getLakarordineratLakemedelsbruk() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NARKOTIKA_LAKEMEDEL,
                "narkotikaLakemedel.lakarordineratLakemedelsbruk", ValidationMessageType.EMPTY);

        } else if (narkotikaLakemedel.getLakarordineratLakemedelsbruk()) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, narkotikaLakemedel.getLakemedelOchDos(),
                CATEGORY_NARKOTIKA_LAKEMEDEL, "narkotikaLakemedel.lakemedelOchDos");
        }
    }

    private void validateMedvetandestorning(final Medvetandestorning medvetandestorning) {

        if (medvetandestorning == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDVETANDESTORNING, "medvetandestorning",
                ValidationMessageType.EMPTY, "ts-bas.validation.medvetandestorning.missing");
            return;
        }

        if (medvetandestorning.getMedvetandestorning() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDVETANDESTORNING, "medvetandestorning.medvetandestorning",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateSyn(final TsBasUtlatandeV7 utlatande) {
        if (utlatande.getSyn() == null) {
            return;
        }

        final Syn syn = utlatande.getSyn();
        if (syn == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn", ValidationMessageType.EMPTY,
                "ts-bas.validation.syn.missing");
            return;
        }

        if (syn.getSynfaltsdefekter() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.synfaltsdefekter", ValidationMessageType.EMPTY);
        }

        if (syn.getNattblindhet() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.nattblindhet", ValidationMessageType.EMPTY);
        }

        if (syn.getProgressivOgonsjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.progressivOgonsjukdom", ValidationMessageType.EMPTY);
        }

        if (syn.getDiplopi() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.diplopi", ValidationMessageType.EMPTY);
        }

        if (syn.getNystagmus() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.nystagmus", ValidationMessageType.EMPTY);
        }

        if (syn.getSynskarpaSkickasSeparat() == null || !syn.getSynskarpaSkickasSeparat()) { //R37
            if (syn.getHogerOga() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hogerOga", ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.hogeroga.missing");
            } else {
                if (syn.getHogerOga().getUtanKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hogerOga.utanKorrektion",
                        ValidationMessageType.EMPTY);

                } else if (syn.getHogerOga().getUtanKorrektion() < 0.0 || syn.getHogerOga().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hogerOga.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds");
                }

                if (syn.getHogerOga().getMedKorrektion() != null) {
                    if (syn.getHogerOga().getMedKorrektion() < 0.0 || syn.getHogerOga().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hogerOga.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds");
                    }
                }
            }

            if (syn.getVansterOga() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vansterOga", ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.vansteroga.missing");
            } else {
                if (syn.getVansterOga().getUtanKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vansterOga.utanKorrektion",
                        ValidationMessageType.EMPTY);

                } else if (syn.getVansterOga().getUtanKorrektion() < 0.0 || syn.getVansterOga().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vansterOga.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds");
                }

                if (syn.getVansterOga().getMedKorrektion() != null) {
                    if (syn.getVansterOga().getMedKorrektion() < 0.0 || syn.getVansterOga().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vansterOga.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds");
                    }
                }
            }

            if (syn.getBinokulart() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart", ValidationMessageType.EMPTY,
                    "ts-bas.validation.syn.binokulart.missing");
            } else {
                if (syn.getBinokulart().getUtanKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.utanKorrektion",
                        ValidationMessageType.EMPTY);

                } else if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.out-of-bounds");
                }

                if (syn.getBinokulart().getMedKorrektion() != null) {
                    if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.out-of-bounds");
                    }
                }
            }

            validateSynKorrektionsRegler(utlatande, syn);
        } else { //R37
            if (syn.getHogerOga() != null) {
                if (syn.getHogerOga().getUtanKorrektion() != null
                    || syn.getHogerOga().getMedKorrektion() != null
                    || (syn.getHogerOga().getKontaktlins() != null && syn.getHogerOga().getKontaktlins())) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hogerOga",
                        ValidationMessageType.INCORRECT_COMBINATION, "ts-bas.validation.syn.R37");
                }
            }
            if (syn.getVansterOga() != null) {
                if (syn.getVansterOga().getUtanKorrektion() != null
                    || syn.getVansterOga().getMedKorrektion() != null
                    || (syn.getVansterOga().getKontaktlins() != null && syn.getVansterOga().getKontaktlins())) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vansterOga",
                        ValidationMessageType.INCORRECT_COMBINATION, "ts-bas.validation.syn.R37");
                }
            }
            if (syn.getBinokulart() != null
                && (syn.getBinokulart().getUtanKorrektion() != null || syn.getBinokulart().getMedKorrektion() != null)) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart",
                    ValidationMessageType.INCORRECT_COMBINATION, "ts-bas.validation.syn.R37");
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
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hogerOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r33");
                }
                if (syn.getVansterOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vansterOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r33");
                }
                if (syn.getBinokulart().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r33");
                }
                // R34
            } else if (CollectionUtils.containsAny(IntygAvserKategori.getNormalCategories(), utlatande.getIntygAvser().getKorkortstyp())
                && (syn.getHogerOga().getUtanKorrektion() != null && syn.getVansterOga().getUtanKorrektion() != null)
                && (syn.getHogerOga().getUtanKorrektion() < 0.8 && syn.getVansterOga().getUtanKorrektion() < 0.8)
                && (syn.getHogerOga().getUtanKorrektion() >= 0.1 && syn.getVansterOga().getUtanKorrektion() >= 0.1)) {
                if (syn.getHogerOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hogerOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r34");
                }
                if (syn.getVansterOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vansterOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r34");
                }
                if (syn.getBinokulart().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r34");
                }
                // R35
            } else if (CollectionUtils.containsAny(IntygAvserKategori.getNormalCategories(), utlatande.getIntygAvser().getKorkortstyp())
                && (syn.getHogerOga().getUtanKorrektion() != null && syn.getVansterOga().getUtanKorrektion() != null)
                && (syn.getHogerOga().getUtanKorrektion() < 0.1 || syn.getVansterOga().getUtanKorrektion() < 0.1)) {
                if (syn.getHogerOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hogerOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r35");
                }
                if (syn.getVansterOga().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vansterOga.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r35");
                }
                if (syn.getBinokulart().getMedKorrektion() == null) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                        ValidationMessageType.EMPTY, "ts-bas.validation.syn.r35");
                }
            }
        }
        // CHECKSTYLE:ON MagicNumber*/
    }
}
