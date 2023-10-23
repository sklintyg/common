/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.fkparent.model.converter.certificate;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_DATUM_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_INFORMATION_SOURCE_DESCRIPTION;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_INFORMATION_SOURCE_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_JSON_ID_4;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_TYPE_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleAndExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.withCitation;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithNotEmpty;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMedicalInvestigation;
import se.inera.intyg.common.support.facade.model.config.CodeItem;
import se.inera.intyg.common.support.facade.model.config.MedicalInvestigation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigation;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.common.support.model.InternalDate;

public abstract class AbstractQuestionUnderlag {

    public static CertificateDataElement toCertificate(List<Underlag> underlag, UnderlagsTyp ignoredUnderlagsTyp, String questionId,
        String parentId, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(questionId)
            .parent(parentId)
            .index(index)
            .config(
                CertificateDataConfigMedicalInvestigation.builder()
                    .text((texts.get(UNDERLAG_TYPE_TEXT_ID)))
                    .typeText(texts.get(UNDERLAG_TYPE_TEXT_ID))
                    .dateText(UNDERLAG_DATUM_TEXT)
                    .informationSourceText(UNDERLAG_INFORMATION_SOURCE_TEXT)
                    .informationSourceDescription(UNDERLAG_INFORMATION_SOURCE_DESCRIPTION)
                    .list(
                        List.of(
                            MedicalInvestigation.builder()
                                .investigationTypeId(UNDERLAG_SVAR_JSON_ID_4 + "[0].typ")
                                .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[0].datum")
                                .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran")
                                .typeOptions(
                                    getTypeOptions(ignoredUnderlagsTyp)
                                )
                                .maxDate(LocalDate.now())
                                .build(),
                            MedicalInvestigation.builder()
                                .investigationTypeId(UNDERLAG_SVAR_JSON_ID_4 + "[1].typ")
                                .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[1].datum")
                                .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[1].hamtasFran")
                                .typeOptions(
                                    getTypeOptions(ignoredUnderlagsTyp)
                                )
                                .maxDate(LocalDate.now())
                                .build(),
                            MedicalInvestigation.builder()
                                .investigationTypeId(UNDERLAG_SVAR_JSON_ID_4 + "[2].typ")
                                .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[2].datum")
                                .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[2].hamtasFran")
                                .typeOptions(
                                    getTypeOptions(ignoredUnderlagsTyp)
                                )
                                .maxDate(LocalDate.now())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueMedicalInvestigationList.builder()
                    .list(
                        List.of(
                            getMedicalInvestigationValue(underlag, 0),
                            getMedicalInvestigationValue(underlag, 1),
                            getMedicalInvestigationValue(underlag, 2)
                        )
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UNDERLAG_SVAR_ID_4)
                        .expression(
                            multipleAndExpression(
                                wrapWithNotEmpty(withCitation(UNDERLAG_SVAR_JSON_ID_4 + "[0].typ")),
                                wrapWithNotEmpty(withCitation(UNDERLAG_SVAR_JSON_ID_4 + "[0].datum")),
                                wrapWithNotEmpty(withCitation(UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran"))))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(UNDERLAGFINNS_SVAR_ID_3)
                        .expression(singleExpression(UNDERLAGFINNS_SVAR_JSON_ID_3))
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataValueMedicalInvestigation getMedicalInvestigationValue(List<Underlag> underlag, int id) {
        return CertificateDataValueMedicalInvestigation.builder()
            .date(
                CertificateDataValueDate.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].datum")
                    .date(
                        id < underlag.size() ? toLocalDate(underlag.get(id).getDatum()) : null)
                    .build())
            .investigationType(
                CertificateDataValueCode.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].typ")
                    .code(
                        id < underlag.size() && underlag.get(id).getTyp() != null ? underlag.get(id).getTyp().getId() : null)
                    .build())
            .informationSource(
                CertificateDataTextValue.builder()
                    .id(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].hamtasFran")
                    .text(
                        id < underlag.size() ? underlag.get(id).getHamtasFran() : null)
                    .build())
            .build();
    }

    private static List<CodeItem> getTypeOptions(UnderlagsTyp ignoredUnderlagsTyp) {
        return Arrays.stream(UnderlagsTyp.values())
            .filter(value -> value != ignoredUnderlagsTyp)
            .map(AbstractQuestionUnderlag::getCodeItem)
            .collect(Collectors.toList());
    }

    private static CodeItem getCodeItem(UnderlagsTyp underlagsTyp) {
        return CodeItem.builder()
            .id(underlagsTyp.getId())
            .code(underlagsTyp.getId())
            .label(underlagsTyp.getLabel())
            .build();
    }

    private static LocalDate toLocalDate(InternalDate internalDate) {
        return (internalDate != null && internalDate.isValidDate()) ? internalDate.asLocalDate() : null;
    }

    public static List<Underlag> toInternal(Certificate certificate, String questionId) {
        if (certificate.getData().get(questionId).getValue() == null) {
            return Collections.emptyList();
        }
        final var value = (CertificateDataValueMedicalInvestigationList) certificate.getData().get(questionId).getValue();
        final var underlagList = value.getList().stream()
            .map(underlag -> Underlag.create(
                underlag.getInvestigationType().getCode() != null && !underlag.getInvestigationType().getCode().isEmpty()
                    ? UnderlagsTyp.fromId(underlag.getInvestigationType().getCode()) : null,
                underlag.getDate().getDate() != null ? new InternalDate(underlag.getDate().getDate()) : null,
                underlag.getInformationSource().getText()
            ))
            .collect(Collectors.toList());

        removeEmptyValuesIfAtEndOfList(underlagList);

        return underlagList;
    }

    private static void removeEmptyValuesIfAtEndOfList(List<Underlag> underlagList) {
        for (int i = underlagList.size() - 1; i >= 0; i--) {
            if (hasValue(underlagList.get(i))) {
                break;
            } else {
                underlagList.remove(i);
            }
        }
    }

    private static boolean hasValue(Underlag underlag) {
        return underlag.getHamtasFran() != null || underlag.getDatum() != null || underlag.getTyp() != null;
    }
}

