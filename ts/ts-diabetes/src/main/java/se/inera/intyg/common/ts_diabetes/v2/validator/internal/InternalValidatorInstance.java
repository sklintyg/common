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
package se.inera.intyg.common.ts_diabetes.v2.validator.internal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Diabetes;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Syn;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Vardkontakt;

/**
 * Class for validating drafts of the internal model.
 *
 * @author erik
 */
public class InternalValidatorInstance {

    private static final Logger LOG = LoggerFactory.getLogger(InternalValidatorInstance.class);

    private static final StringValidator STRING_VALIDATOR = new StringValidator();
    private static final String CATEGORY_INTYG_AVSER = "intygAvser";
    private static final String CATEGORY_IDENTITET = "identitet";
    private static final String CATEGORY_DIABETES = "diabetes";
    private static final String CATEGORY_HYPOGLYKEMIER = "hypoglykemier";
    private static final String CATEGORY_SYN = "syn";
    private static final String CATEGORY_BEDOMNING = "bedomning";

    private List<ValidationMessage> validationMessages;

    private ValidationContext context;

    public InternalValidatorInstance() {
        validationMessages = new ArrayList<>();
    }

    /**
     * Validates an internal draft of an {@link TsDiabetesUtlatandeV2} (this means the object being validated is not
     * necessarily
     * complete).
     *
     * @param utlatande an internal {@link TsDiabetesUtlatandeV2}
     * @return a {@link ValidateDraftResponseHolder} with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(TsDiabetesUtlatandeV2 utlatande) {

        if (utlatande == null) {
            ValidatorUtil.addValidationError(validationMessages, "utlatande", "utlatande", ValidationMessageType.OTHER,
                "ts-diabetes.validation.utlatande.missing");
        } else {
            context = new ValidationContext(utlatande);
            PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);
            validateIntygAvser(utlatande.getIntygAvser());
            validateIdentitetStyrkt(utlatande.getVardkontakt());
            validateDiabetes(utlatande.getDiabetes(), utlatande.getGrundData().getPatient());
            validateHypoglykemi(utlatande.getHypoglykemier());
            validateSyn(utlatande.getSyn());
            validateBedomning(utlatande.getBedomning());
            ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);
        }

        return new ValidateDraftResponse(ValidatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateHypoglykemi(Hypoglykemier hypoglykemier) {
        if (hypoglykemier == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, "hypoglykemier", ValidationMessageType.EMPTY,
                "ts-diabetes.validation.hypoglykemier.missing");
            return;
        }

        if (hypoglykemier.getKunskapOmAtgarder() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                "hypoglykemier.kunskapOmAtgarder", ValidationMessageType.EMPTY);
        }

        if (hypoglykemier.getTeckenNedsattHjarnfunktion() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                "hypoglykemier.teckenNedsattHjarnfunktion", ValidationMessageType.EMPTY);
        }

        if (ValidatorUtil.isNotNullTrue(hypoglykemier.getTeckenNedsattHjarnfunktion())) {
            if (hypoglykemier.getSaknarFormagaKannaVarningstecken() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                    "hypoglykemier.saknarFormagaKannaVarningstecken", ValidationMessageType.EMPTY);
            }

            if (hypoglykemier.getAllvarligForekomst() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                    "hypoglykemier.allvarligForekomst", ValidationMessageType.EMPTY);
            }

            if (hypoglykemier.getAllvarligForekomstTrafiken() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                    "hypoglykemier.allvarligForekomstTrafiken", ValidationMessageType.EMPTY);
            }
        }

        if (ValidatorUtil.isNotNullTrue(hypoglykemier.getAllvarligForekomst())) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, hypoglykemier.getAllvarligForekomstBeskrivning(),
                CATEGORY_HYPOGLYKEMIER, "hypoglykemier.allvarligForekomstBeskrivning");
        }

        if (ValidatorUtil.isNotNullTrue(hypoglykemier.getAllvarligForekomstTrafiken())) {
            ValidatorUtil.assertDescriptionNotEmpty(validationMessages, hypoglykemier.getAllvarligForekomstTrafikBeskrivning(),
                CATEGORY_HYPOGLYKEMIER, "hypoglykemier.allvarligForekomstTrafikBeskrivning");
        }

        if (ValidatorUtil.isNotNullTrue(hypoglykemier.getAllvarligForekomstVakenTid())) {
            if (hypoglykemier.getAllvarligForekomstVakenTidObservationstid() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                    "hypoglykemier.allvarligForekomstVakenTidObservationstid",
                    ValidationMessageType.EMPTY);
            } else if (hypoglykemier.getAllvarligForekomstVakenTidObservationstid()
                .beforeMinDateOrInFuture(LocalDate.now().minusYears(1))) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER,
                    "hypoglykemier.allvarligForekomstVakenTidObservationstid",
                    ValidationMessageType.INVALID_FORMAT,
                    "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date");
            }
        }

        if (context.isHogreBehorighetContext()) {
            if (hypoglykemier.getEgenkontrollBlodsocker() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, "hypoglykemier.egenkontrollBlodsocker",
                    ValidationMessageType.EMPTY);
            }

            if (hypoglykemier.getAllvarligForekomstVakenTid() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_HYPOGLYKEMIER, "hypoglykemier.allvarligForekomstVakenTid",
                    ValidationMessageType.EMPTY);
            }
        }

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

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, "bedomning", ValidationMessageType.EMPTY);
            return;
        }
        if (bedomning.getKorkortstyp().isEmpty()
            && (bedomning.getKanInteTaStallning() == null || ValidatorUtil.isNotNullFalse(bedomning.getKanInteTaStallning()))) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, "bedomning", ValidationMessageType.EMPTY,
                "common.validation.b-04");
        }

        if (context.isHogreBehorighetContext()) {
            if (bedomning.getLamplighetInnehaBehorighet() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_BEDOMNING, "bedomning.lamplighetInnehaBehorighet",
                    ValidationMessageType.EMPTY);
            }
        }
    }

    private void validateDiabetes(final Diabetes diabetes, final Patient patient) {

        if (diabetes == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes", ValidationMessageType.EMPTY,
                "ts-diabetes.validation.diabetes.missing");
            return;
        }

        if (diabetes.getObservationsperiod() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.observationsperiod",
                ValidationMessageType.EMPTY);
        } else {
            if (!STRING_VALIDATOR.validateStringIsYear(diabetes.getObservationsperiod())) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.observationsperiod",
                    ValidationMessageType.INVALID_FORMAT,
                    "ts-diabetes.validation.diabetes.observationsperiod.incorrect-format");
            } else {
                if (ValidatorUtil.isYearBeforeBirth(diabetes.getObservationsperiod(), patient.getPersonId())) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.observationsperiod",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-diabetes.validation.diabetes.observationsperiod.incorrect-format");
                }
            }
        }

        if (diabetes.getDiabetestyp() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.diabetesTyp", ValidationMessageType.EMPTY);
        }

        boolean annanBehandling = diabetes.getAnnanBehandlingBeskrivning() != null
            && !diabetes.getAnnanBehandlingBeskrivning().isEmpty();
        if (!(ValidatorUtil.isNotNullTrue(diabetes.getEndastKost()) || ValidatorUtil.isNotNullTrue(diabetes.getTabletter())
            || ValidatorUtil.isNotNullTrue(diabetes.getInsulin()) || annanBehandling)) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.behandlingsTyp", ValidationMessageType.EMPTY,
                "ts-diabetes.validation.diabetes.behandling.missing");
        }

        if (ValidatorUtil.isNotNullTrue(diabetes.getInsulin())) {
            if (diabetes.getInsulinBehandlingsperiod() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.insulinBehandlingsperiod",
                    ValidationMessageType.EMPTY, "ts-diabetes.validation.diabetes.insulin.behandlingsperiod.missing");
            } else if (!STRING_VALIDATOR.validateStringIsYear(diabetes.getInsulinBehandlingsperiod())) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIABETES, "diabetes.insulinBehandlingsperiod",
                    ValidationMessageType.INVALID_FORMAT,
                    "ts-diabetes.validation.diabetes.insulin.behandlingsperiod.incorrect-format");
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

    private void validateSyn(final Syn syn) {

        if (syn == null) {
            return;
        }

        if (syn.getSeparatOgonlakarintyg() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.separatOgonlakarintyg", ValidationMessageType.EMPTY);

        } else if (!syn.getSeparatOgonlakarintyg()) {

            if (syn.getSynfaltsprovningUtanAnmarkning() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.synfaltsprovningUtanAnmarkning",
                    ValidationMessageType.EMPTY);
            }

            if (syn.getHoger() == null || syn.getHoger().getUtanKorrektion() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hoger.utanKorrektion",
                    ValidationMessageType.EMPTY);

            } else {
                if (syn.getHoger().getUtanKorrektion() < 0.0 || syn.getHoger().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hoger.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-diabetes.validation.syn.out-of-bounds");
                }

                if (syn.getHoger().getMedKorrektion() != null) {
                    if (syn.getHoger().getMedKorrektion() < 0.0 || syn.getHoger().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.hoger.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-diabetes.validation.syn.out-of-bounds");
                    }
                }
            }

            if (syn.getVanster() == null || syn.getVanster().getUtanKorrektion() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vanster.utanKorrektion",
                    ValidationMessageType.EMPTY);

            } else {

                if (syn.getVanster().getUtanKorrektion() < 0.0 || syn.getVanster().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vanster.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-diabetes.validation.syn.out-of-bounds");
                }

                if (syn.getVanster().getMedKorrektion() != null) {
                    if (syn.getVanster().getMedKorrektion() < 0.0 || syn.getVanster().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.vanster.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-diabetes.validation.syn.out-of-bounds");
                    }
                }
            }

            if (syn.getBinokulart() == null || syn.getBinokulart().getUtanKorrektion() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.utanKorrektion",
                    ValidationMessageType.EMPTY);

            } else {
                if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                    ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.utanKorrektion",
                        ValidationMessageType.INVALID_FORMAT,
                        "ts-diabetes.validation.syn.out-of-bounds");
                }

                if (syn.getBinokulart().getMedKorrektion() != null) {
                    if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                        ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.binokulart.medKorrektion",
                            ValidationMessageType.INVALID_FORMAT,
                            "ts-diabetes.validation.syn.out-of-bounds");
                    }
                }
            }

            if (syn.getDiplopi() == null) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYN, "syn.diplopi", ValidationMessageType.EMPTY);
            }
        }
    }
}
