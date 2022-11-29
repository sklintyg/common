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

package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_DELSVAR_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_DATUM_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_INFORMATION_SOURCE_DESCRIPTION_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_INFORMATION_SOURCE_TEXT_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_JSON_ID_4;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_TYPE_TEXT_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_TYP_DELSVAR_ID_4;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.OVRIGT;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRANFORETAGSHALSOVARD;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRANSKOLHALSOVARD;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK;
import static se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleAndExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.time.LocalDate;
import java.util.List;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMedicalInvestigationList;
import se.inera.intyg.common.support.facade.model.config.CodeItem;
import se.inera.intyg.common.support.facade.model.config.MedicalInvestigation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.common.support.facade.model.value.MedicalInvestigationValue;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionUnderlag {

    private static final short LIMIT = 0;

    public static CertificateDataElement toCertificate(int index, CertificateTextProvider texts, List<Underlag> underlag) {
        return CertificateDataElement.builder()
            .id(UNDERLAG_TYP_DELSVAR_ID_4)
            .parent(GRUNDFORMU_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigMedicalInvestigationList.builder()
                    .typeText(texts.get(UNDERLAG_TYPE_TEXT_ID))
                    .dateText(UNDERLAG_DATUM_TEXT)
                    .informationSourceText(texts.get(UNDERLAG_INFORMATION_SOURCE_TEXT_ID))
                    .informationSourceDescription(texts.get(UNDERLAG_INFORMATION_SOURCE_DESCRIPTION_ID))
                    .typeOptions(
                        getTypeOptions()
                    )
                    .list(
                        List.of(
                            MedicalInvestigation.builder()
                                .typeId(UNDERLAG_SVAR_JSON_ID_4 + "[0].typ")
                                .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[0].datum")
                                .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran")
                                .build(),
                            MedicalInvestigation.builder()
                                .typeId(UNDERLAG_SVAR_JSON_ID_4 + "[1].typ")
                                .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[1].datum")
                                .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[1].hamtasFran")
                                .build(),
                            MedicalInvestigation.builder()
                                .typeId(UNDERLAG_SVAR_JSON_ID_4 + "[2].typ")
                                .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[2].datum")
                                .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[2].hamtasFran")
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueMedicalInvestigationList.builder()
                    .list(List.of(
                        getMedicalInvestigationValue(underlag, 0),
                        getMedicalInvestigationValue(underlag, 1),
                        getMedicalInvestigationValue(underlag, 2)
                    ))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(UNDERLAG_TYP_DELSVAR_ID_4)
                        .expression(multipleAndExpression(UNDERLAG_SVAR_JSON_ID_4 + "[0].typ", UNDERLAG_SVAR_JSON_ID_4 + "[0].datum",
                            UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran"))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(UNDERLAGFINNS_DELSVAR_ID_3)
                        .expression(singleExpression(UNDERLAGFINNS_SVAR_JSON_ID_3))
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(UNDERLAG_SVAR_JSON_ID_4 + "[0].datum")
                        .numberOfDays(LIMIT)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(UNDERLAG_SVAR_JSON_ID_4 + "[1].datum")
                        .numberOfDays(LIMIT)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(UNDERLAG_SVAR_JSON_ID_4 + "[2].datum")
                        .numberOfDays(LIMIT)
                        .build(),
                }
            )
            .build();
    }

    private static MedicalInvestigationValue getMedicalInvestigationValue(List<Underlag> underlag, int id) {
        return MedicalInvestigationValue.builder()
            .typeId(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].typ")
            .informationSourceId(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].hamtasFran")
            .dateId(UNDERLAG_SVAR_JSON_ID_4 + "[" + id + "].datum")
            .datum(id < underlag.size() ? toLocalDate(underlag.get(id).getDatum()) : null)
            .hamtasFran(id < underlag.size() ? underlag.get(id).getHamtasFran() : null)
            .typ(id < underlag.size() && underlag.get(id).getTyp() != null ? underlag.get(id).getTyp().getId() : null)
            .build();
    }

    private static List<CodeItem> getTypeOptions() {
        return List.of(
            getCodeItem(NEUROPSYKIATRISKT_UTLATANDE),
            getCodeItem(UNDERLAG_FRAN_HABILITERINGEN),
            getCodeItem(UNDERLAG_FRAN_ARBETSTERAPEUT),
            getCodeItem(UNDERLAG_FRAN_FYSIOTERAPEUT),
            getCodeItem(UNDERLAG_FRAN_LOGOPED),
            getCodeItem(UNDERLAG_FRANPSYKOLOG),
            getCodeItem(UNDERLAG_FRANFORETAGSHALSOVARD),
            getCodeItem(UNDERLAG_FRANSKOLHALSOVARD),
            getCodeItem(UTREDNING_AV_ANNAN_SPECIALISTKLINIK),
            getCodeItem(UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS),
            getCodeItem(OVRIGT));
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
}
