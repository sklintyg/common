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

package se.inera.intyg.common.af00213.v1.model.converter;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_QUESTION_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_QUESTION_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_CATEGORY_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_QUESTION_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_QUESTION_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_QUESTION_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_QUESTION_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_CATEGORY_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_QUESTION_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_QUESTION_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_CATEGORY_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_QUESTION_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_QUESTION_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.util.SortedMap;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public final class InternalToCertificate {

    private InternalToCertificate() {

    }


    public static Certificate convert(Af00213UtlatandeV1 internalCertificate, SortedMap<String, String> texts) {
        int index = 0;

        return CertificateBuilder.create()
            .metadata(createMetadata(internalCertificate, texts))
            .addElement(createFunktionsnedsattningsCategory(index++, texts))
            .addElement(createHarFunktionsnedsattningsQuestion(internalCertificate.getHarFunktionsnedsattning(), index++, texts))
            .addElement(createFunktionsnedsattningsQuestion(internalCertificate.getFunktionsnedsattning(), index++, texts))
            .addElement(createAktivitetsbegransningsCategory(index++, texts))
            .addElement(createHarAktivitetsbegransningsQuestion(internalCertificate.getHarAktivitetsbegransning(), index++, texts))
            .addElement(createAktivitetsbegransningsQuestion(internalCertificate.getAktivitetsbegransning(), index++, texts))
            .addElement(createUtredningBehandlingsCategory(index++, texts))
            .addElement(createHarUtredningBehandlingsQuestion(internalCertificate.getHarUtredningBehandling(), index++, texts))
            .addElement(createUtredningBehandlingsQuestion(internalCertificate.getUtredningBehandling(), index++, texts))
            .addElement(createArbetspaverkansCategory(index++, texts))
            .addElement(createHarArbetspaverkansQuestion(internalCertificate.getHarArbetetsPaverkan(), index++, texts))
            .addElement(createArbetspaverkansQuestion(internalCertificate.getArbetetsPaverkan(), index++, texts))
            .addElement(createOvrigtCategory(index++, texts))
            .addElement(createOvrigtQuestion(internalCertificate.getOvrigt(), index, texts))
            .build();
    }

    private static CertificateMetadata createMetadata(Af00213UtlatandeV1 internalCertificate,
        SortedMap<String, String> texts) {
        final var unit = internalCertificate.getGrundData().getSkapadAv().getVardenhet();
        return CertificateMetadata.builder()
            .id(internalCertificate.getId())
            .type(internalCertificate.getTyp())
            .typeVersion(internalCertificate.getTextVersion())
            .name("Arbetsförmedlingens medicinska utlåtande")
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

    private static CertificateDataElement createFunktionsnedsattningsCategory(int index, SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(FUNKTIONSNEDSATTNING_CATEGORY_TEXT))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createHarFunktionsnedsattningsQuestion(Boolean harFunktionsnedsattning, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigBoolean.builder()
                    .text(texts.get(FUNKTIONSNEDSATTNING_QUESTION_TEXT))
                    .description(texts.get(FUNKTIONSNEDSATTNING_QUESTION_DESCRIPTION))
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11)
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11)
                    .selected(harFunktionsnedsattning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createFunktionsnedsattningsQuestion(String funktionsnedsattning, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_DELSVAR_ID_12)
            .index(index)
            .parent(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_TEXT))
                    .description(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_DESCRIPTION))
                    .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12)
                    .text(funktionsnedsattning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_12)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createAktivitetsbegransningsCategory(int index, SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(AKTIVITETSBEGRANSNING_CATEGORY_TEXT))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createHarAktivitetsbegransningsQuestion(Boolean harAktivitetsbegransning, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
            .parent(AKTIVITETSBEGRANSNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigBoolean.builder()
                    .text(texts.get(AKTIVITETSBEGRANSNING_QUESTION_TEXT))
                    .description(texts.get(AKTIVITETSBEGRANSNING_QUESTION_DESCRIPTION))
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21)
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21)
                    .selected(harAktivitetsbegransning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createAktivitetsbegransningsQuestion(String aktivitetsbegransning, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_DELSVAR_ID_22)
            .parent(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_TEXT))
                    .description(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_DESCRIPTION))
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22)
                    .text(aktivitetsbegransning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_22)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11)
                        .expression(singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createUtredningBehandlingsCategory(int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(UTREDNING_BEHANDLING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(UTREDNING_BEHANDLING_CATEGORY_TEXT))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createHarUtredningBehandlingsQuestion(Boolean harUtredningBehandling, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(UTREDNING_BEHANDLING_DELSVAR_ID_31)
            .index(index)
            .parent(UTREDNING_BEHANDLING_CATEGORY_ID)
            .config(
                CertificateDataConfigBoolean.builder()
                    .text(texts.get(UTREDNING_BEHANDLING_QUESTION_TEXT))
                    .description(texts.get(UTREDNING_BEHANDLING_QUESTION_DESCRIPTION))
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_31)
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_31)
                    .selected(harUtredningBehandling)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_31)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_31))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createUtredningBehandlingsQuestion(String utredningBehandling, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(UTREDNING_BEHANDLING_DELSVAR_ID_32)
            .index(index)
            .parent(UTREDNING_BEHANDLING_DELSVAR_ID_31)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(UTREDNING_BEHANDLING_DELSVAR_TEXT))
                    .description(texts.get(UTREDNING_BEHANDLING_DELSVAR_DESCRIPTION))
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_32)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(UTREDNING_BEHANDLING_SVAR_JSON_ID_32)
                    .text(utredningBehandling)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_32)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_32))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(UTREDNING_BEHANDLING_DELSVAR_ID_31)
                        .expression(singleExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_31))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createArbetspaverkansCategory(int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(ARBETETS_PAVERKAN_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(ARBETETS_PAVERKAN_CATEGORY_TEXT))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createHarArbetspaverkansQuestion(Boolean harArbetspaverkan, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(ARBETETS_PAVERKAN_DELSVAR_ID_41)
            .index(index)
            .parent(ARBETETS_PAVERKAN_CATEGORY_ID)
            .config(
                CertificateDataConfigBoolean.builder()
                    .text(texts.get(ARBETETS_PAVERKAN_QUESTION_TEXT))
                    .description(texts.get(ARBETETS_PAVERKAN_QUESTION_DESCRIPTION))
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_41)
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NO))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_41)
                    .selected(harArbetspaverkan)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_41)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_41))
                        .build()
                }
            )
            .build();
    }

    public static CertificateDataElement createArbetspaverkansQuestion(String arbetspaverkan, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(ARBETETS_PAVERKAN_DELSVAR_ID_42)
            .index(index)
            .parent(ARBETETS_PAVERKAN_DELSVAR_ID_41)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(ARBETETS_PAVERKAN_DELSVAR_TEXT))
                    .description(texts.get(ARBETETS_PAVERKAN_DELSVAR_DESCRIPTION))
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_42)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ARBETETS_PAVERKAN_SVAR_JSON_ID_42)
                    .text(arbetspaverkan)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_42)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_42))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(ARBETETS_PAVERKAN_DELSVAR_ID_41)
                        .expression(singleExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_41))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataElement createOvrigtCategory(int index, SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(OVRIGT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(texts.get(OVRIGT_CATEGORY_TEXT))
                    .build()
            )
            .build();
    }

    public static CertificateDataElement createOvrigtQuestion(String ovrigt, int index,
        SortedMap<String, String> texts) {
        return CertificateDataElement.builder()
            .id(OVRIGT_DELSVAR_ID_5)
            .index(index)
            .parent(OVRIGT_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(OVRIGT_SVAR_JSON_ID_5)
                    .text(texts.get(OVRIGT_QUESTION_TEXT))
                    .description(texts.get(OVRIGT_QUESTION_DESCRIPTION))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(OVRIGT_SVAR_JSON_ID_5)
                    .text(ovrigt)
                    .build()
            )
            .build();
    }
}
