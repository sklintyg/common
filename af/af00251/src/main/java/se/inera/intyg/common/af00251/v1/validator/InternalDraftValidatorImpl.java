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
package se.inera.intyg.common.af00251.v1.validator;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.af00251.v1.model.internal.ArbetsmarknadspolitisktProgram;
import se.inera.intyg.common.af00251.v1.model.internal.PrognosAtergang;
import se.inera.intyg.common.af00251.v1.model.internal.Sjukfranvaro;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.*;
import static se.inera.intyg.common.support.validate.ValidatorUtil.addValidationError;

@Component("af00251.v1.InternalDraftValidatorImpl")
public class InternalDraftValidatorImpl implements InternalDraftValidator<AF00251UtlatandeV1> {


    public static final String CATEGORY_MEDICINSKT_UNDERLAG = "medicinsktUnderlag";
    public static final String CATEGORY_ARBETSMARKNADS_PROGRAM = "arbetsmarknadsPolitisktProgram";
    public static final String CATEGORY_KONSEKVENSER = "konsekvenser";
    public static final String CATEGORY_BEDOMNING = "bedomning";
    private static final int OMFATTNING_DELTID_MIN_HOURS = 1;
    private static final int OMFATTNING_DELTID_MAX_HOURS = 40;

    @Override
    public ValidateDraftResponse validateDraft(AF00251UtlatandeV1 utlatande) {

        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 - Grund för medicinskt underlag
        validateMedicinsktUnderlag(utlatande, validationMessages);

        // Kategori 2 - Arbetsmarknadspolitiskt program
        validateArbetsmasknadsPolitisktProgram(utlatande, validationMessages);

        // Kategori 3 - Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);

        // Kategori 4 - Aktivitetsbegränsning
        validateAktivitetsbegransning(utlatande, validationMessages);

        // Kategori 5 - Förhinder
        validateForhinder(utlatande, validationMessages);

        // Kategori 6 - Sjukfrånvaro
        validateSjukfranvaro(utlatande, validationMessages);

        // Kategori 7 - Begränsning sjukfrånvaro
        validateBegransningSjukfranvaro(utlatande, validationMessages);

        // Kategori 8 - Prognos återgång
        validatePrognosAtergang(utlatande, validationMessages);


        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateMedicinsktUnderlag(AF00251UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUndersokningsDatum() == null && utlatande.getAnnatDatum() == null) {
            addValidationError(validationMessages, CATEGORY_MEDICINSKT_UNDERLAG, MEDICINSKUNDERLAG_SVAR_JSON_UNDERSOKNINGS_DATUM,
                ValidationMessageType.EMPTY);
            return;
        }

        // Regel R1
        if (utlatande.getAnnatDatum() != null
            && utlatande.getAnnatBeskrivning() == null) {
            addValidationError(validationMessages, CATEGORY_MEDICINSKT_UNDERLAG, MEDICINSKUNDERLAG_SVAR_JSON_ANNAT_BESKRIVNING,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateArbetsmasknadsPolitisktProgram(AF00251UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        final ArbetsmarknadspolitisktProgram arbetsmarknadspolitisktProgram = utlatande.getArbetsmarknadspolitisktProgram();
        if (arbetsmarknadspolitisktProgram == null) {
            return;
        }

        if (arbetsmarknadspolitisktProgram.getOmfattning() == ArbetsmarknadspolitisktProgram.Omfattning.DELTID) {
            if (arbetsmarknadspolitisktProgram.getOmfattningDeltid() == null) {
                addValidationError(validationMessages, CATEGORY_ARBETSMARKNADS_PROGRAM,
                    createCompositeFieldKey(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2,
                        ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23),
                    ValidationMessageType.INCORRECT_COMBINATION, createMessageKey(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2,
                        "parttime-no-extent"));
            } else if (arbetsmarknadspolitisktProgram.getOmfattningDeltid()
                                                     .intValue() <= OMFATTNING_DELTID_MIN_HOURS
                || arbetsmarknadspolitisktProgram.getOmfattningDeltid()
                                                 .intValue() >= OMFATTNING_DELTID_MAX_HOURS) {
                final String fieldKey = createCompositeFieldKey(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2,
                    ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23);
                addValidationError(validationMessages, CATEGORY_ARBETSMARKNADS_PROGRAM,
                    fieldKey, ValidationMessageType.INVALID_FORMAT,
                    createMessageKey(fieldKey, "invalid-range"));
            }
        }
    }


    public static void validateFunktionsnedsattning(AF00251UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Yes or no must be specified.
        if (Strings.nullToEmpty(utlatande.getFunktionsnedsattning())
                   .trim()
                   .isEmpty()) {
            addValidationError(validationMessages, CATEGORY_KONSEKVENSER, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_31,
                ValidationMessageType.EMPTY);
        }
    }

    public static void validateAktivitetsbegransning(AF00251UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getAktivitetsbegransning())
                   .trim()
                   .isEmpty()) {
            addValidationError(validationMessages, CATEGORY_KONSEKVENSER, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_41,
                ValidationMessageType.EMPTY);
        }
    }

    private void validateForhinder(AF00251UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getHarForhinder() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, FORHINDER_SVAR_JSON_ID_51,
                ValidationMessageType.EMPTY);
            return;
        } else {
            if (utlatande.getHarForhinder()) {
                if (nullToEmpty(utlatande.getSjukfranvaro())
                    .stream()
                    .filter(sjukfranvaro -> nullToFalse(sjukfranvaro.getChecked()))
                    .count() == 0) {
                    addValidationError(validationMessages, CATEGORY_BEDOMNING, SJUKFRANVARO_SVAR_JSON_ID_6,
                        ValidationMessageType.EMPTY,
                        createMessageKey(FORHINDER_SVAR_JSON_ID_51, "required-" + SJUKFRANVARO_SVAR_JSON_ID_6));
                }
            } else {
                if (!nullToEmpty(utlatande.getSjukfranvaro()).isEmpty()) {
                    addValidationError(validationMessages, CATEGORY_BEDOMNING, FORHINDER_SVAR_JSON_ID_51,
                        ValidationMessageType.INCORRECT_COMBINATION,
                        createMessageKey(FORHINDER_SVAR_JSON_ID_51, "forbidden-" + SJUKFRANVARO_SVAR_JSON_ID_6));
                }
            }
        }
    }

    private void validateSjukfranvaro(AF00251UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        final List<Sjukfranvaro> sjukfranvaroList = nullToEmpty(utlatande.getSjukfranvaro());
        if (sjukfranvaroList.size() > 4) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, SJUKFRANVARO_SVAR_JSON_ID_6,
                ValidationMessageType.OTHER, createMessageKey(SJUKFRANVARO_SVAR_JSON_ID_6, "too-many"));
        }

        for (int index = 0; index < sjukfranvaroList.size(); index++) {
            final Sjukfranvaro sjukfranvaro = sjukfranvaroList.get(index);
            if (sjukfranvaro.getChecked() != null && sjukfranvaro.getChecked()
                                                                 .booleanValue()) {
                validateSjukfranvaro(sjukfranvaro, index, validationMessages);
                checkSjukskrivningPeriodOverlapAgainstList(validationMessages, index, sjukfranvaro, utlatande.getSjukfranvaro());
            }
        }
    }

    private boolean checkSjukskrivningPeriodOverlapAgainstList(List<ValidationMessage> validationMessages, int index, Sjukfranvaro sjukfranvaro,
                                                               ImmutableList<Sjukfranvaro> sjukfranvaros) {

        Optional<Sjukfranvaro> optionalSjukfranvaro = getPeriodIntervalsOverlapping(sjukfranvaro, sjukfranvaros);
        if (optionalSjukfranvaro.isPresent()) {
            final InternalLocalDateInterval overlappingPeriod = optionalSjukfranvaro.get()
                                                                                    .getPeriod();

            final String keyWithIndex = String.format("%s[%d]", SJUKFRANVARO_SVAR_JSON_ID_6, index);
            final InternalLocalDateInterval currentPeriod = sjukfranvaro.getPeriod();
            if (currentPeriod.getFrom()
                             .equals(overlappingPeriod.getFrom())) {
                addValidationError(validationMessages,
                    CATEGORY_BEDOMNING,
                    createCompositeFieldKey(keyWithIndex, SJUKFRANVARO_SVAR_JSON_ID_62, "from"),
                    ValidationMessageType.PERIOD_OVERLAP);
                addValidationError(validationMessages,
                    CATEGORY_BEDOMNING,
                    createCompositeFieldKey(keyWithIndex, SJUKFRANVARO_SVAR_JSON_ID_62, "tom"),
                    ValidationMessageType.PERIOD_OVERLAP);
            } else if (currentPeriod.getFrom()
                                    .asLocalDate()
                                    .isBefore(overlappingPeriod
                                        .getFrom()
                                        .asLocalDate())) {
                addValidationError(validationMessages,
                    CATEGORY_BEDOMNING,
                    createCompositeFieldKey(keyWithIndex, SJUKFRANVARO_SVAR_JSON_ID_62, "tom"),
                    ValidationMessageType.PERIOD_OVERLAP);
            } else {
                addValidationError(validationMessages,
                    CATEGORY_BEDOMNING,
                    createCompositeFieldKey(keyWithIndex, SJUKFRANVARO_SVAR_JSON_ID_62, "from"),
                    ValidationMessageType.PERIOD_OVERLAP);
            }
            return true;
        }
        return false;
    }

    private Optional<Sjukfranvaro> getPeriodIntervalsOverlapping(Sjukfranvaro sjukfranvaro,
                                                                 ImmutableList<Sjukfranvaro> sjukfranvaros) {
        return sjukfranvaros
            .stream()
            .filter(Objects::nonNull)
            .filter(e -> e != sjukfranvaro)
            .filter(e -> e.getChecked() != null && e.getChecked()
                                                    .booleanValue())
            .filter(e -> e.getPeriod() != null && e.getPeriod()
                                                   .overlaps(sjukfranvaro.getPeriod()))
            .findFirst();
    }

    private void validateSjukfranvaro(Sjukfranvaro sjukfranvaro, int index, List<ValidationMessage> validationMessages) {

        final String indexedKey = String.format("%s[%d]", SJUKFRANVARO_SVAR_JSON_ID_6, index);

        if (sjukfranvaro.getPeriod() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                createCompositeFieldKey(indexedKey, SJUKFRANVARO_SVAR_JSON_ID_62),
                ValidationMessageType.EMPTY);
        } else if (sjukfranvaro.getPeriod()
                               .getFrom() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                createCompositeFieldKey(indexedKey, SJUKFRANVARO_SVAR_JSON_ID_62, SJUKFRANVARO_SVAR_JSON_ID_62_FROM),
                ValidationMessageType.EMPTY);
        } else if (sjukfranvaro.getPeriod()
                               .getTom() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                createCompositeFieldKey(indexedKey, SJUKFRANVARO_SVAR_JSON_ID_62, SJUKFRANVARO_SVAR_JSON_ID_62_TOM),
                ValidationMessageType.EMPTY);
        } else {
            if (!sjukfranvaro.getPeriod()
                             .isValid()) {
                addValidationError(validationMessages, CATEGORY_BEDOMNING,
                    createCompositeFieldKey(indexedKey,
                        SJUKFRANVARO_SVAR_JSON_ID_62), ValidationMessageType.INVALID_FORMAT);
            }
        }

        if (sjukfranvaro.getNiva() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                createCompositeFieldKey(indexedKey, SJUKFRANVARO_SVAR_JSON_ID_61),
                ValidationMessageType.EMPTY);
        } else {
            final int niva = sjukfranvaro.getNiva();
            if (niva < 1 || niva > 100) {
                final String indexedFieldKey = createCompositeFieldKey(indexedKey,
                    SJUKFRANVARO_SVAR_JSON_ID_61);
                final String fieldKey = createCompositeFieldKey(SJUKFRANVARO_SVAR_JSON_ID_6,
                    SJUKFRANVARO_SVAR_JSON_ID_61);
                addValidationError(validationMessages, CATEGORY_BEDOMNING,
                    indexedFieldKey, ValidationMessageType.INVALID_FORMAT,
                    createMessageKey(fieldKey, "invalid-range"));
            }
        }

    }

    private void validateBegransningSjukfranvaro(AF00251UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getBegransningSjukfranvaro() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_7,
                ValidationMessageType.EMPTY);
            return;
        }

        if (utlatande.getBegransningSjukfranvaro()
                     .getKanBegransas() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                createCompositeFieldKey(BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_7, BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_71),
                ValidationMessageType.EMPTY);
        } else {
            if (utlatande.getBegransningSjukfranvaro()
                         .getKanBegransas()
                         .booleanValue()) {
                if (utlatande.getBegransningSjukfranvaro()
                             .getBeskrivning() == null ||
                    utlatande.getBegransningSjukfranvaro()
                             .getBeskrivning()
                             .isEmpty()) {

                    addValidationError(validationMessages, CATEGORY_BEDOMNING,
                        createCompositeFieldKey(BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_7, BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_72),
                        ValidationMessageType.INCORRECT_COMBINATION,
                        createMessageKey(BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_7, "kanBegransas-no-beskrivning"));
                }
            }
        }
    }


    private void validatePrognosAtergang(AF00251UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getPrognosAtergang() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING, PROGNOS_ATERGANG_SVAR_JSON_ID_8,
                ValidationMessageType.EMPTY);
            return;
        }

        if (utlatande.getPrognosAtergang()
                     .getPrognos() == null) {
            addValidationError(validationMessages, CATEGORY_BEDOMNING,
                createCompositeFieldKey(PROGNOS_ATERGANG_SVAR_JSON_ID_8, PROGNOS_ATERGANG_SVAR_JSON_ID_81),
                ValidationMessageType.EMPTY);
        } else {
            if (utlatande.getPrognosAtergang()
                         .getPrognos() == PrognosAtergang.Prognos.MED_ANPASSNING) {
                if (utlatande.getPrognosAtergang()
                             .getAnpassningar() == null ||
                    utlatande.getPrognosAtergang()
                             .getAnpassningar()
                             .isEmpty()) {

                    addValidationError(validationMessages, CATEGORY_BEDOMNING,
                        createCompositeFieldKey(PROGNOS_ATERGANG_SVAR_JSON_ID_8, PROGNOS_ATERGANG_SVAR_JSON_ID_82),
                        ValidationMessageType.INCORRECT_COMBINATION,
                        createMessageKey(PROGNOS_ATERGANG_SVAR_JSON_ID_8, "prognos-no-anpassning"));
                }
            }
        }
    }

    <T> List<T> nullToEmpty(List<T> collection) {
        if (collection == null) {
            return new ArrayList<>();
        }
        return collection;
    }

    boolean nullToFalse(Boolean value) {
        if (value == null) {
            return false;
        }
        return value;
    }

    String createCompositeFieldKey(String... fields) {
        return String.join(".", fields);
    }

    String createMessageKey(String fieldName, String messageKey) {
        return String.format("af00251.validation.%s.%s", fieldName, messageKey);
    }
}
