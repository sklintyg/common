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

import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_DESCRIPTION;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_QUESTION_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DESCRIPTION;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_ICD_10_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_ICD_10_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_KSH_97_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_KSH_97_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_TELEFONKONTAKT_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL;
import static se.inera.intyg.common.ag7804.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_ARBETE;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_ARBETSSOKANDE;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_CATEGORY_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_FORALDRALEDIG;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_STUDIER;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag7804.support.Ag7804EntryPoint;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleDate;
import se.inera.intyg.common.support.facade.model.config.DiagnosesListItem;
import se.inera.intyg.common.support.facade.model.config.DiagnosesTerminology;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;

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
            .addElement(createAnnatGrundForMUBeskrivning(internalCertificate.getAnnatGrundForMUBeskrivning(), index++, texts))
            .addElement(createSysselsattningCategory(index++, texts))
            .addElement(createSysselsattningQuestion(internalCertificate.getSysselsattning(), index++, texts))
            .addElement(createSysselsattningYrkeQuestion(internalCertificate.getNuvarandeArbete(), index++, texts))
            .addElement(createDiagnosCategory(index++, texts))
            .addElement(createShouldIncludeDiagnosesQuestion(internalCertificate.getOnskarFormedlaDiagnos(), index++, texts))
            .addElement(createDiagnosQuestion(internalCertificate.getDiagnoser(), index++, texts))
            .addElement(createFunktionsnedsattningCategory(index++, texts))
            .addElement(createFunktionsnedsattningQuestion(internalCertificate.getFunktionsnedsattning(), index++, texts))
            .addElement(createAktivitetsbegransningQuestion(internalCertificate.getAktivitetsbegransning(), index++, texts))
            .addElement(createMedicinskaBehandlingarCategory(index++, texts))
            .addElement(createPagaendeBehandlingQuestion(internalCertificate.getPagaendeBehandling(), index++, texts))
            .addElement(createPlaneradBehandlingQuestion(internalCertificate.getPlaneradBehandling(), index++, texts))
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
            .parent(AVSTANGNING_SMITTSKYDD_CATEGORY_ID)
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
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
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
            .parent(GRUNDFORMU_CATEGORY_ID)
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
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
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

    public static CertificateDataElement createAnnatGrundForMUBeskrivning(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
                    .text(texts.get(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1)
                        .expression(singleExpression(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createSysselsattningCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(SYSSELSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(SYSSELSATTNING_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createSysselsattningQuestion(List<Sysselsattning> value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
            .index(index)
            .parent(SYSSELSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(SYSSELSATTNING_SVAR_TEXT))
                    .description(texts.get(SYSSELSATTNING_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                                .label(texts.get(SYSSELSATTNING_ARBETE))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.ARBETSSOKANDE.getId())
                                .label(texts.get(SYSSELSATTNING_ARBETSSOKANDE))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId())
                                .label(texts.get(SYSSELSATTNING_FORALDRALEDIG))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.STUDIER.getId())
                                .label(texts.get(SYSSELSATTNING_STUDIER))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(createSysselsattningCodeList(value))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
                        .expression(
                            multipleOrExpression(
                                singleExpression(SysselsattningsTyp.NUVARANDE_ARBETE.getId()),
                                singleExpression(SysselsattningsTyp.ARBETSSOKANDE.getId()),
                                singleExpression(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId()),
                                singleExpression(SysselsattningsTyp.STUDIER.getId())
                            )
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueCode> createSysselsattningCodeList(List<Sysselsattning> value) {
        if (value == null) {
            return Collections.emptyList();
        }

        return value.stream()
            .map(sysselsattning -> CertificateDataValueCode.builder()
                .id(Objects.requireNonNull(sysselsattning.getTyp()).getId())
                .code(sysselsattning.getTyp().getId())
                .build())
            .collect(Collectors.toList());
    }

    public static CertificateDataElement createSysselsattningYrkeQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(NUVARANDE_ARBETE_SVAR_ID_29)
            .index(index)
            .parent(SYSSELSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(NUVARANDE_ARBETE_SVAR_TEXT))
                    .id(NUVARANDE_ARBETE_SVAR_JSON_ID_29)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(NUVARANDE_ARBETE_SVAR_JSON_ID_29)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(NUVARANDE_ARBETE_SVAR_ID_29)
                        .expression(singleExpression(NUVARANDE_ARBETE_SVAR_JSON_ID_29))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
                        .expression(singleExpression(
                            se.inera.intyg.common.lisjp.model.internal.Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE.getId()))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createDiagnosCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DIAGNOS_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(DIAGNOS_CATEGORY_TEXT))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createShouldIncludeDiagnosesQuestion(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
            .index(index)
            .parent(DIAGNOS_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100)
                    .selectedText(texts.get(ANSWER_YES)) // change this!!
                    .unselectedText(texts.get(ANSWER_NO)) // change this!!
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100)
                    .selected(value)
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createDiagnosQuestion(List<Diagnos> value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DIAGNOS_SVAR_ID_6)
            .index(index)
            .parent(DIAGNOS_CATEGORY_ID)
            .config(
                CertificateDataConfigDiagnoses.builder()
                    .text(texts.get(DIAGNOS_SVAR_TEXT))
                    .description(texts.get(DIAGNOS_SVAR_BESKRIVNING))
                    .terminology(
                        Arrays.asList(
                            DiagnosesTerminology.builder()
                                .id(DIAGNOS_ICD_10_ID)
                                .label(DIAGNOS_ICD_10_LABEL)
                                .build(),
                            DiagnosesTerminology.builder()
                                .id(DIAGNOS_KSH_97_ID)
                                .label(DIAGNOS_KSH_97_LABEL)
                                .build()
                        )
                    )
                    .list(
                        Arrays.asList(
                            DiagnosesListItem.builder()
                                .id("1")
                                .build(),
                            DiagnosesListItem.builder()
                                .id("2")
                                .build(),
                            DiagnosesListItem.builder()
                                .id("3")
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueDiagnosisList.builder()
                    .list(createDiagnosValue(value))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DIAGNOS_SVAR_ID_6)
                        .expression(singleExpression("1"))
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(DIAGNOS_SVAR_ID_6)
                        .expression(singleExpression(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100))
                        .build(),
                    CertificateDataValidationDisable.builder()
                        .questionId(DIAGNOS_SVAR_ID_6)
                        .expression(singleExpression(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100)) // change this to look for null?
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueDiagnosis> createDiagnosValue(List<Diagnos> diagnoses) {
        if (diagnoses == null) {
            return Collections.emptyList();
        }

        final List<CertificateDataValueDiagnosis> newDiagnoses = new ArrayList<>();
        for (int i = 0; i < diagnoses.size(); i++) {
            final var diagnosis = diagnoses.get(i);
            if (isInvalid(diagnosis)) {
                continue;
            }

            newDiagnoses.add(createDiagnosis(Integer.toString(i + 1), diagnosis));
        }

        return newDiagnoses;
    }

    private static boolean isInvalid(Diagnos diagnos) {
        return diagnos.getDiagnosKod() == null;
    }

    private static CertificateDataValueDiagnosis createDiagnosis(String id, Diagnos diagnos) {
        return CertificateDataValueDiagnosis.builder()
            .id(id)
            .terminology(diagnos.getDiagnosKodSystem())
            .code(diagnos.getDiagnosKod())
            .description(diagnos.getDiagnosBeskrivning())
            .build();
    }

    private static CertificateDataElement createFunktionsnedsattningCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(FUNKTIONSNEDSATTNING_CATEGORY_TEXT))
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

    public static CertificateDataElement createFunktionsnedsattningQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_SVAR_ID_35)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(FUNKTIONSNEDSATTNING_SVAR_TEXT))
                    .text(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_TEXT))
                    .description(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING))
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FUNKTIONSNEDSATTNING_SVAR_ID_35)
                        .expression(
                            singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createAktivitetsbegransningQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_SVAR_ID_17)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(AKTIVITETSBEGRANSNING_SVAR_TEXT))
                    .text(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_TEXT))
                    .description(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING))
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(AKTIVITETSBEGRANSNING_SVAR_ID_17)
                        .expression(
                            singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createMedicinskaBehandlingarCategory(int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(MEDICINSKABEHANDLINGAR_CATEGORY_TEXT))
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

    public static CertificateDataElement createPagaendeBehandlingQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PAGAENDEBEHANDLING_SVAR_ID_19)
            .index(index)
            .parent(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(PAGAENDEBEHANDLING_SVAR_TEXT))
                    .text(texts.get(PAGAENDEBEHANDLING_DELSVAR_TEXT))
                    .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                    .text(value)
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

    public static CertificateDataElement createPlaneradBehandlingQuestion(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PLANERADBEHANDLING_SVAR_ID_20)
            .index(index)
            .parent(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(PLANERADBEHANDLING_SVAR_TEXT))
                    .text(texts.get(PLANERADBEHANDLING_DELSVAR_TEXT))
                    .id(PLANERADBEHANDLING_SVAR_JSON_ID_20)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(PLANERADBEHANDLING_SVAR_JSON_ID_20)
                    .text(value)
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
}
