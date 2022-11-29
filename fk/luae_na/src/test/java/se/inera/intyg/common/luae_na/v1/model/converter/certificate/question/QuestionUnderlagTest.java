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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMedicalInvestigationList;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.model.InternalDate;

@ExtendWith(MockitoExtension.class)
class QuestionUnderlagTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");

    }

    @Nested
    class ToCertificate {

        @Test
        void shouldIncludeId() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());

            assertEquals(UNDERLAG_TYP_DELSVAR_ID_4, question.getId());
        }

        @Test
        void shouldIncludeParent() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());

            assertEquals(GRUNDFORMU_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionUnderlag.toCertificate(expectedIndex, texts, List.of());

            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeConfigTypeMedicalInvestigationList() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();

            assertEquals(CertificateDataConfigTypes.UE_MEDICINSK_UTREDNING_LIST, config.getType());
        }


        @Test
        void shouldIncludeTypeText() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());

            verify(texts, atLeastOnce()).get(UNDERLAG_TYPE_TEXT_ID);
        }

        @Test
        void shouldIncludeDateText() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();
            assertEquals(UNDERLAG_DATUM_TEXT, config.getDateText());
        }

        @Test
        void shouldIncludeInformationSourceText() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());

            verify(texts, atLeastOnce()).get(UNDERLAG_INFORMATION_SOURCE_TEXT_ID);
        }

        @Test
        void shouldIncludeTypeInformationSourceDescription() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());

            verify(texts, atLeastOnce()).get(UNDERLAG_INFORMATION_SOURCE_DESCRIPTION_ID);
        }

        @Test
        void shouldIncludeConfigListOfMedicalInvestigation() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();

            assertNotNull(config.getList());
        }

        @Test
        void shouldIncludeConfigListOfMedicalInvestigationTypeIds() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();

            final var firstElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[0].typ";
            final var secondElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[1].typ";
            final var thirdElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[2].typ";

            assertAll(
                () -> assertEquals(firstElementInformationSource, config.getList().get(0).getTypeId()),
                () -> assertEquals(secondElementInformationSource, config.getList().get(1).getTypeId()),
                () -> assertEquals(thirdElementInformationSource, config.getList().get(2).getTypeId())
            );
        }

        @Test
        void shouldIncludeConfigListOfMedicalInvestigationInformationSourceId() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();
            final var firstElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran";
            final var secondElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[1].hamtasFran";
            final var thirdElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[2].hamtasFran";

            assertAll(
                () -> assertEquals(firstElementInformationSource, config.getList().get(0).getInformationSourceId()),
                () -> assertEquals(secondElementInformationSource, config.getList().get(1).getInformationSourceId()),
                () -> assertEquals(thirdElementInformationSource, config.getList().get(2).getInformationSourceId())
            );
        }

        @Test
        void shouldIncludeConfigListOfMedicalInvestigationDateId() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();
            final var firstElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[0].datum";
            final var secondElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[1].datum";
            final var thirdElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[2].datum";

            assertAll(
                () -> assertEquals(firstElementInformationSource, config.getList().get(0).getDateId()),
                () -> assertEquals(secondElementInformationSource, config.getList().get(1).getDateId()),
                () -> assertEquals(thirdElementInformationSource, config.getList().get(2).getDateId())
            );
        }

        @Test
        void shouldIncludeConfigMedicalInvestigationTypeOptions() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();
            assertNotNull(config.getTypeOptions());
        }

        @Test
        void shouldIncludeConfigOfMedicalInvestigationTypeOptionsWithId() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();
            assertAll(
                () -> assertEquals(NEUROPSYKIATRISKT_UTLATANDE.getId(), config.getTypeOptions().get(0).getId()),
                () -> assertEquals(UNDERLAG_FRAN_HABILITERINGEN.getId(), config.getTypeOptions().get(1).getId()),
                () -> assertEquals(UNDERLAG_FRAN_ARBETSTERAPEUT.getId(), config.getTypeOptions().get(2).getId()),
                () -> assertEquals(UNDERLAG_FRAN_FYSIOTERAPEUT.getId(), config.getTypeOptions().get(3).getId()),
                () -> assertEquals(UNDERLAG_FRAN_LOGOPED.getId(), config.getTypeOptions().get(4).getId()),
                () -> assertEquals(UNDERLAG_FRANPSYKOLOG.getId(), config.getTypeOptions().get(5).getId()),
                () -> assertEquals(UNDERLAG_FRANFORETAGSHALSOVARD.getId(), config.getTypeOptions().get(6).getId()),
                () -> assertEquals(UNDERLAG_FRANSKOLHALSOVARD.getId(), config.getTypeOptions().get(7).getId()),
                () -> assertEquals(UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId(), config.getTypeOptions().get(8).getId()),
                () -> assertEquals(UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId(),
                    config.getTypeOptions().get(9).getId()),
                () -> assertEquals(OVRIGT.getId(), config.getTypeOptions().get(10).getId())
            );
        }

        @Test
        void shouldIncludeConfigOfMedicalInvestigationTypeOptionsWithLabel() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var config = (CertificateDataConfigMedicalInvestigationList) question.getConfig();
            assertAll(
                () -> assertEquals(NEUROPSYKIATRISKT_UTLATANDE.getLabel(), config.getTypeOptions().get(0).getLabel()),
                () -> assertEquals(UNDERLAG_FRAN_HABILITERINGEN.getLabel(), config.getTypeOptions().get(1).getLabel()),
                () -> assertEquals(UNDERLAG_FRAN_ARBETSTERAPEUT.getLabel(), config.getTypeOptions().get(2).getLabel()),
                () -> assertEquals(UNDERLAG_FRAN_FYSIOTERAPEUT.getLabel(), config.getTypeOptions().get(3).getLabel()),
                () -> assertEquals(UNDERLAG_FRAN_LOGOPED.getLabel(), config.getTypeOptions().get(4).getLabel()),
                () -> assertEquals(UNDERLAG_FRANPSYKOLOG.getLabel(), config.getTypeOptions().get(5).getLabel()),
                () -> assertEquals(UNDERLAG_FRANFORETAGSHALSOVARD.getLabel(), config.getTypeOptions().get(6).getLabel()),
                () -> assertEquals(UNDERLAG_FRANSKOLHALSOVARD.getLabel(), config.getTypeOptions().get(7).getLabel()),
                () -> assertEquals(UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getLabel(), config.getTypeOptions().get(8).getLabel()),
                () -> assertEquals(UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getLabel(),
                    config.getTypeOptions().get(9).getLabel()),
                () -> assertEquals(OVRIGT.getLabel(), config.getTypeOptions().get(10).getLabel())
            );
        }

        @Test
        void shouldIncludeValueMedicalInvestigation() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            assertEquals(CertificateDataValueType.MEDICAL_INVESTIGATION, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueMedicalInvestigationList() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var value = (CertificateDataValueMedicalInvestigationList) question.getValue();
            assertEquals(3, value.getList().size());
        }

        @Test
        void shouldIncludeListOfMedicalInvestigationWithValues() {
            final var expectedResult = ImmutableList.of(
                Underlag.create(
                    UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(LocalDate.now()), "hamtasFran"),
                Underlag.create(
                    UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(LocalDate.now()), "hamtasFran"),
                Underlag.create(
                    UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(LocalDate.now()), "hamtasFran"));

            final var question = QuestionUnderlag.toCertificate(0, texts, expectedResult);
            final var value = (CertificateDataValueMedicalInvestigationList) question.getValue();

            assertAll(
                () -> {
                    for (int i = 0; i < value.getList().size(); i++) {
                        assertEquals(expectedResult.get(i).getDatum().asLocalDate(), value.getList().get(i).getDatum());
                        assertEquals(expectedResult.get(i).getHamtasFran(), value.getList().get(i).getHamtasFran());
                        assertEquals(expectedResult.get(i).getTyp().getId(), value.getList().get(i).getTyp());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ", value.getList().get(i).getTypeId());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].hamtasFran", value.getList().get(i).getInformationSourceId());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum", value.getList().get(i).getDateId());
                    }
                }
            );
        }

        @Test
        void shouldIncludeListOfMedicalInvestigationWithNullValues() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var value = (CertificateDataValueMedicalInvestigationList) question.getValue();

            assertAll(
                () -> {
                    for (int i = 0; i < value.getList().size(); i++) {
                        assertNull(value.getList().get(i).getDatum());
                        assertNull(value.getList().get(i).getHamtasFran());
                        assertNull(value.getList().get(i).getTyp());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ", value.getList().get(i).getTypeId());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].hamtasFran", value.getList().get(i).getInformationSourceId());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum", value.getList().get(i).getDateId());
                    }
                }
            );
        }

        @Test
        void shouldIncludeMandatoryValidation() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());

            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeMandatoryValidationQuestionId() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];

            assertEquals(UNDERLAG_TYP_DELSVAR_ID_4, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeMandatoryValidationExpression() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var expectedExpression =
                UNDERLAG_SVAR_JSON_ID_4 + "[0].typ" + " && " + UNDERLAG_SVAR_JSON_ID_4 + "[0].datum" + " && "
                    + UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran";
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];

            assertEquals(expectedExpression, certificateDataValidationMandatory.getExpression());
        }

        @Test
        void shouldIncludeShowValidation() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());

            assertEquals(CertificateDataValidationType.SHOW_VALIDATION, question.getValidation()[1].getType());
        }

        @Test
        void shouldIncludeShowValidationQuestionId() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
            assertEquals(UNDERLAGFINNS_DELSVAR_ID_3, certificateDataValidationShow.getQuestionId());
        }

        @Test
        void shouldIncludeShowValidationExpression() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
            final var expectedExpression = "$" + UNDERLAGFINNS_SVAR_JSON_ID_3;

            assertEquals(expectedExpression, certificateDataValidationShow.getExpression());
        }

        @Test
        void shouldIncludeMaxDateValidation() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());

            assertAll(
                () -> assertEquals(CertificateDataValidationType.MAX_DATE_VALIDATION, question.getValidation()[2].getType()),
                () -> assertEquals(CertificateDataValidationType.MAX_DATE_VALIDATION, question.getValidation()[3].getType()),
                () -> assertEquals(CertificateDataValidationType.MAX_DATE_VALIDATION, question.getValidation()[4].getType())
            );
        }

        @Test
        void shouldIncludeMaxDateValidationLimit() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var firstMaxDateValidation = (CertificateDataValidationMaxDate) question.getValidation()[2];
            final var secondMaxDateValidation = (CertificateDataValidationMaxDate) question.getValidation()[3];
            final var thirdMaxDateValidation = (CertificateDataValidationMaxDate) question.getValidation()[4];
            final var limit = (short) 0;

            assertAll(
                () -> assertEquals(limit, firstMaxDateValidation.getNumberOfDays()),
                () -> assertEquals(limit, secondMaxDateValidation.getNumberOfDays()),
                () -> assertEquals(limit, thirdMaxDateValidation.getNumberOfDays())
            );
        }

        @Test
        void shouldIncludeMaxDateValidationId() {
            final var question = QuestionUnderlag.toCertificate(0, texts, List.of());
            final var firstMaxDateValidation = (CertificateDataValidationMaxDate) question.getValidation()[2];
            final var secondMaxDateValidation = (CertificateDataValidationMaxDate) question.getValidation()[3];
            final var thirdMaxDateValidation = (CertificateDataValidationMaxDate) question.getValidation()[4];
            final var firstExpectedId = UNDERLAG_SVAR_JSON_ID_4 + "[0].datum";
            final var secondExpectedId = UNDERLAG_SVAR_JSON_ID_4 + "[1].datum";
            final var thirdExpectedId = UNDERLAG_SVAR_JSON_ID_4 + "[2].datum";

            assertAll(
                () -> assertEquals(firstExpectedId, firstMaxDateValidation.getId()),
                () -> assertEquals(secondExpectedId, secondMaxDateValidation.getId()),
                () -> assertEquals(thirdExpectedId, thirdMaxDateValidation.getId())
            );
        }
    }
}