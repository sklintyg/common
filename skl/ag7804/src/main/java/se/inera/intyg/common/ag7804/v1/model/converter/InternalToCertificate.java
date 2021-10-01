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

package se.inera.intyg.common.ag7804.v1.model.converter;

import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_DESCRIPTION;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_QUESTION_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DESCRIPTION;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_TELEFONKONTAKT_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import se.inera.intyg.common.ag7804.support.Ag7804EntryPoint;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;

public final class InternalToCertificate {

    private InternalToCertificate() {

    }

    public static Certificate convert(Ag7804UtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        var index = 0;
        return CertificateBuilder.create()
            .metadata(createMetadata(internalCertificate, texts))
            .addElement(createSmittbararpenningCategory(index++, texts))
            .addElement(createAvstangningSmittskyddQuestion(internalCertificate.getAvstangningSmittskydd(), index++, texts))
            .addElement(createGrundForMUCategory(index++, texts))
            .addElement(createIntygetBaseratPa(internalCertificate, index++, texts))
            .build();
    }

    private static CertificateMetadata createMetadata(Ag7804UtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        final var unit = internalCertificate.getGrundData().getSkapadAv().getVardenhet();
        return CertificateMetadata.builder()
            .id(internalCertificate.getId())
            .type(internalCertificate.getTyp())
            .typeVersion(internalCertificate.getTextVersion())
            .name(Ag7804EntryPoint.MODULE_NAME)
            .description(texts.get(DESCRIPTION))
            .unit(
                Unit.builder()
                    .unitId(unit.getEnhetsid())
                    .unitName(unit.getEnhetsnamn())
                    .address(unit.getPostadress())
                    .zipCode(unit.getPostnummer())
                    .city(unit.getPostort())
                    .email(unit.getEpost())
                    .phoneNumber(unit.getTelefonnummer())
                    .build()
            )
            .build();
    }

    private static CertificateDataElement createSmittbararpenningCategory(int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(AVSTANGNING_SMITTSKYDD_CATEGORY_TEXT))
                    .description(texts.get(AVSTANGNING_SMITTSKYDD_CATEGORY_DESCRIPTION))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createAvstangningSmittskyddQuestion(Boolean value, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
            .index(index)
            .parent(RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
                    .label(texts.get(AVSTANGNING_SMITTSKYDD_QUESTION_LABEL))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NOT_SELECTED))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
                    .selected(value)
                    .build()
            )
            .build();
    }

    private static CertificateDataElement createGrundForMUCategory(int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMU_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createIntygetBaseratPa(Ag7804UtlatandeV1 internalCertificate, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .index(index)
            .parent(RespConstants.GRUNDFORMU_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleDate.builder()
                    .text(texts.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT))
                    .description(texts.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                                .label(texts.get(GRUNDFORMU_UNDERSOKNING_LABEL))
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                                .label(texts.get(GRUNDFORMU_TELEFONKONTAKT_LABEL))
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                                .label(texts.get(GRUNDFORMU_JOURNALUPPGIFTER_LABEL))
                                .build(),
                            CheckboxMultipleDate.builder()
                                .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                                .label(texts.get(GRUNDFORMU_ANNAT_LABEL))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueDateList.builder()
                    .list(createIntygetBaseratPaValue(internalCertificate))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(
                            multipleOrExpression(
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1),
                                singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                            ))
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                        .numberOfDays((short) 0)
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueDate> createIntygetBaseratPaValue(Ag7804UtlatandeV1 internalCertificate) {
        final List<CertificateDataValueDate> values = new ArrayList<>();

        if (internalCertificate.getUndersokningAvPatienten() != null) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getUndersokningAvPatienten().asLocalDate())
                    .build()
            );
        }

        if (internalCertificate.getTelefonkontaktMedPatienten() != null) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getTelefonkontaktMedPatienten().asLocalDate())
                    .build()
            );
        }

        if (internalCertificate.getJournaluppgifter() != null) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
                    .date(internalCertificate.getJournaluppgifter().asLocalDate())
                    .build()
            );
        }

        if (internalCertificate.getAnnatGrundForMU() != null) {
            values.add(
                CertificateDataValueDate.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                    .date(internalCertificate.getAnnatGrundForMU().asLocalDate())
                    .build()
            );
        }
        return values;
    }
}
