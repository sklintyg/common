/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_INFORMATION_SOURCE_DESCRIPTION;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_INFORMATION_SOURCE_TEXT;
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
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAG_DATUM_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAG_SVAR_JSON_ID_4;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAG_TYPE_TEXT_ID;

import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMedicalInvestigation;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigation;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
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
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);

            assertEquals(UNDERLAG_SVAR_ID_4, question.getId());
        }

        @Test
        void shouldIncludeParent() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);

            assertEquals(GRUNDFORMU_CATEGORY_ID, question.getParent());
        }

        @Test
        void shouldIncludeIndex() {
            final var expectedIndex = 1;
            final var question = QuestionUnderlag.toCertificate(List.of(), expectedIndex, texts);

            assertEquals(expectedIndex, question.getIndex());
        }

        @Test
        void shouldIncludeConfigTypeMedicalInvestigationList() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();

            assertEquals(CertificateDataConfigType.UE_MEDICAL_INVESTIGATION, config.getType());
        }


        @Test
        void shouldIncludeTypeText() {
            QuestionUnderlag.toCertificate(List.of(), 0, texts);
            verify(texts, atLeastOnce()).get(UNDERLAG_TYPE_TEXT_ID);
        }

        @Test
        void shouldIncludeDateText() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
            assertEquals(UNDERLAG_DATUM_TEXT, config.getDateText());
        }

        @Test
        void shouldIncludeInformationSourceText() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
            assertEquals(UNDERLAG_INFORMATION_SOURCE_TEXT, config.getInformationSourceText());
        }

        @Test
        void shouldIncludeTypeInformationSourceDescription() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
            assertEquals(UNDERLAG_INFORMATION_SOURCE_DESCRIPTION, config.getInformationSourceDescription());
        }

        @Test
        void shouldIncludeConfigListOfMedicalInvestigation() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();

            assertNotNull(config.getList());
        }

        @Test
        void shouldIncludeConfigListOfMedicalInvestigationTypeIds() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();

            final var firstElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[0].typ";
            final var secondElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[1].typ";
            final var thirdElementInformationSource = UNDERLAG_SVAR_JSON_ID_4 + "[2].typ";

            assertAll(
                () -> assertEquals(firstElementInformationSource, config.getList().get(0).getInvestigationTypeId()),
                () -> assertEquals(secondElementInformationSource, config.getList().get(1).getInvestigationTypeId()),
                () -> assertEquals(thirdElementInformationSource, config.getList().get(2).getInvestigationTypeId())
            );
        }

        @Test
        void shouldIncludeConfigListOfMedicalInvestigationInformationSourceId() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
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
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
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
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
            assertAll(
                () -> assertNotNull(config.getList().get(0).getTypeOptions()),
                () -> assertNotNull(config.getList().get(1).getTypeOptions()),
                () -> assertNotNull(config.getList().get(2).getTypeOptions())
            );

        }

        @Test
        void shouldIncludeConfigMaxDate() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
            assertAll(
                () -> assertEquals(LocalDate.now(), config.getList().get(0).getMaxDate()),
                () -> assertEquals(LocalDate.now(), config.getList().get(1).getMaxDate()),
                () -> assertEquals(LocalDate.now(), config.getList().get(2).getMaxDate())
            );

        }

        @Test
        void shouldIncludeConfigOfMedicalInvestigationTypeOptionsWithId() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();

            assertAll(
                () -> {
                    for (int i = 0; i < config.getList().size(); i++) {
                        assertEquals(NEUROPSYKIATRISKT_UTLATANDE.getId(), config.getList().get(i).getTypeOptions().get(0).getId());
                        assertEquals(UNDERLAG_FRAN_HABILITERINGEN.getId(), config.getList().get(i).getTypeOptions().get(1).getId());
                        assertEquals(UNDERLAG_FRAN_ARBETSTERAPEUT.getId(), config.getList().get(i).getTypeOptions().get(2).getId());
                        assertEquals(UNDERLAG_FRAN_FYSIOTERAPEUT.getId(), config.getList().get(i).getTypeOptions().get(3).getId());
                        assertEquals(UNDERLAG_FRAN_LOGOPED.getId(), config.getList().get(i).getTypeOptions().get(4).getId());
                        assertEquals(UNDERLAG_FRANPSYKOLOG.getId(), config.getList().get(i).getTypeOptions().get(5).getId());
                        assertEquals(UNDERLAG_FRANFORETAGSHALSOVARD.getId(), config.getList().get(i).getTypeOptions().get(6).getId());
                        assertEquals(UNDERLAG_FRANSKOLHALSOVARD.getId(), config.getList().get(i).getTypeOptions().get(7).getId());
                        assertEquals(UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId(), config.getList().get(i).getTypeOptions().get(8).getId());
                        assertEquals(UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId(),
                            config.getList().get(i).getTypeOptions().get(9).getId());
                        assertEquals(OVRIGT.getId(), config.getList().get(i).getTypeOptions().get(10).getId());
                    }
                }
            );
        }


        @Test
        void shouldIncludeConfigOfMedicalInvestigationTypeOptionsWithLabel() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var config = (CertificateDataConfigMedicalInvestigation) question.getConfig();
            assertAll(
                () -> {
                    for (int i = 0; i < config.getList().size(); i++) {
                        assertEquals(NEUROPSYKIATRISKT_UTLATANDE.getLabel(),
                            config.getList().get(i).getTypeOptions().get(0).getLabel());
                        assertEquals(UNDERLAG_FRAN_HABILITERINGEN.getLabel(),
                            config.getList().get(i).getTypeOptions().get(1).getLabel());
                        assertEquals(UNDERLAG_FRAN_ARBETSTERAPEUT.getLabel(),
                            config.getList().get(i).getTypeOptions().get(2).getLabel());
                        assertEquals(UNDERLAG_FRAN_FYSIOTERAPEUT.getLabel(),
                            config.getList().get(i).getTypeOptions().get(3).getLabel());
                        assertEquals(UNDERLAG_FRAN_LOGOPED.getLabel(),
                            config.getList().get(i).getTypeOptions().get(4).getLabel());
                        assertEquals(UNDERLAG_FRANPSYKOLOG.getLabel(),
                            config.getList().get(i).getTypeOptions().get(5).getLabel());
                        assertEquals(UNDERLAG_FRANFORETAGSHALSOVARD.getLabel(),
                            config.getList().get(i).getTypeOptions().get(6).getLabel());
                        assertEquals(UNDERLAG_FRANSKOLHALSOVARD.getLabel(),
                            config.getList().get(i).getTypeOptions().get(7).getLabel());
                        assertEquals(UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getLabel(),
                            config.getList().get(i).getTypeOptions().get(8).getLabel());
                        assertEquals(UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getLabel(),
                            config.getList().get(i).getTypeOptions().get(9).getLabel());
                        assertEquals(OVRIGT.getLabel(), config.getList().get(i).getTypeOptions().get(10).getLabel());
                    }
                }
            );
        }

        @Test
        void shouldIncludeValueMedicalInvestigation() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            assertEquals(CertificateDataValueType.MEDICAL_INVESTIGATION_LIST, question.getValue().getType());
        }

        @Test
        void shouldIncludeValueMedicalInvestigationList() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
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

            final var question = QuestionUnderlag.toCertificate(expectedResult, 0, texts);
            final var value = (CertificateDataValueMedicalInvestigationList) question.getValue();

            assertAll(
                () -> {
                    for (int i = 0; i < value.getList().size(); i++) {
                        assertEquals(expectedResult.get(i).getDatum().asLocalDate(), value.getList().get(i).getDate().getDate());
                        assertEquals(expectedResult.get(i).getHamtasFran(), value.getList().get(i).getInformationSource().getText());
                        assertEquals(expectedResult.get(i).getTyp().getId(), value.getList().get(i).getInvestigationType().getCode());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ",
                            value.getList().get(i).getInvestigationType().getId());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].hamtasFran",
                            value.getList().get(i).getInformationSource().getId());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum",
                            value.getList().get(i).getDate().getId());
                    }
                }
            );
        }

        @Test
        void shouldIncludeListOfMedicalInvestigationWithNullValues() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var value = (CertificateDataValueMedicalInvestigationList) question.getValue();

            assertAll(
                () -> {
                    for (int i = 0; i < value.getList().size(); i++) {
                        assertNull(value.getList().get(i).getDate().getDate());
                        assertNull(value.getList().get(i).getInformationSource().getText());
                        assertNull(value.getList().get(i).getInvestigationType().getCode());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ",
                            value.getList().get(i).getInvestigationType().getId());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].hamtasFran",
                            value.getList().get(i).getInformationSource().getId());
                        assertEquals(UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum",
                            value.getList().get(i).getDate().getId());
                    }
                }
            );
        }

        @Test
        void shouldIncludeMandatoryValidation() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);

            assertEquals(CertificateDataValidationType.MANDATORY_VALIDATION, question.getValidation()[0].getType());
        }

        @Test
        void shouldIncludeMandatoryValidationQuestionId() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];

            assertEquals(UNDERLAG_SVAR_ID_4, certificateDataValidationMandatory.getQuestionId());
        }

        @Test
        void shouldIncludeMandatoryValidationExpression() {
            final var question = QuestionUnderlag.toCertificate(List.of(), 0, texts);
            final var expectedExpression =
                "!empty('" + UNDERLAG_SVAR_JSON_ID_4 + "[0].typ')" + " && " + "!empty('" + UNDERLAG_SVAR_JSON_ID_4 + "[0].datum')" + " && "
                    + "!empty('" + UNDERLAG_SVAR_JSON_ID_4 + "[0].hamtasFran')";
            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];

            assertEquals(expectedExpression, certificateDataValidationMandatory.getExpression());
        }

        @Nested
        class IncludeValidationShowTest extends ValidationShowTest {

            @Override
            protected String getQuestionId() {
                return UNDERLAGFINNS_SVAR_ID_3;
            }

            @Override
            protected String getExpression() {
                return "$" + UNDERLAGFINNS_SVAR_JSON_ID_3;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionUnderlag.toCertificate(List.of(), 0, texts);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        Stream<List<Underlag>> underlag() {
            return Stream.of(
                List.of(
                    Underlag.create(null, null, null),
                    Underlag.create(UNDERLAG_FRAN_HABILITERINGEN, null, "hamtasFran"),
                    Underlag.create(NEUROPSYKIATRISKT_UTLATANDE, new InternalDate(LocalDate.now()), "hamtasFran")
                ));
        }

        @ParameterizedTest
        @MethodSource("underlag")
        void shouldReturnListOfThreeUnderlagAllHasValue(List<Underlag> expectedValue) {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionUnderlag.toCertificate(
                    expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionUnderlag.toInternal(certificate);

            assertAll(
                () -> assertEquals(expectedValue.get(0), actualValue.get(0)),
                () -> assertEquals(expectedValue.get(1), actualValue.get(1)),
                () -> assertEquals(expectedValue.get(2), actualValue.get(2))
            );
        }

        @Nested
        class UnderlagHasNullValues {

            @Test
            void shouldReturnEmptyList() {
                final var expectedValue = List.of(
                    Underlag.create(null, null, null),
                    Underlag.create(null, null, null),
                    Underlag.create(null, null, null)
                );
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertTrue(actualValue.isEmpty());
            }
        }

        @Nested
        class UnderlagThirdElementHasValue {

            final List<Underlag> expectedValue = List.of(
                Underlag.create(null, null, null),
                Underlag.create(null, null, null),
                Underlag.create(null, null, "hamtasFran")
            );

            @Test
            void shouldReturnListWithTwoUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(3, actualValue.size());
            }

            @Test
            void shouldReturnFirstUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(expectedValue.get(0), actualValue.get(0));
            }

            @Test
            void shouldReturnSecondUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(expectedValue.get(1), actualValue.get(1));
            }

            @Test
            void shouldReturnThirdUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(expectedValue.get(2), actualValue.get(2));
            }
        }

        @Nested
        class UnderlagSecondElementHasValue {

            final List<Underlag> expectedValue = List.of(
                Underlag.create(null, null, null),
                Underlag.create(null, null, "hamtasFran"),
                Underlag.create(null, null, null)
            );

            @Test
            void shouldReturnListWithTwoUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(2, actualValue.size());
            }

            @Test
            void shouldReturnFirstUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(expectedValue.get(0), actualValue.get(0));
            }

            @Test
            void shouldReturnSecondUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(expectedValue.get(1), actualValue.get(1));
            }
        }

        @Nested
        class UnderlagFirstElementHasValue {

            final List<Underlag> expectedValue = List.of(
                Underlag.create(null, null, "hamtasFran"),
                Underlag.create(null, null, null),
                Underlag.create(null, null, null)
            );

            @Test
            void shouldReturnListWithOneUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(1, actualValue.size());
            }

            @Test
            void shouldReturnFirstUnderlag() {
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(expectedValue.get(0), actualValue.get(0));
            }
        }

        @Nested
        class UnderlagIsEmpty {

            @Test
            void shouldReturnEmptyListWithNoUnderlag() {
                final List<Underlag> expectedValue = Collections.emptyList();
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        expectedValue, 0, texts))
                    .build();

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(0, actualValue.size());
            }

            @Test
            void shouldReturnEmptyListWhenUnderlagsTypIsEmptyString() {
                final List<Underlag> emptyList = Collections.emptyList();
                final var certificate = CertificateBuilder.create()
                    .addElement(QuestionUnderlag.toCertificate(
                        emptyList, 0, texts))
                    .build();

                certificate.getData().put(UNDERLAG_SVAR_ID_4,
                    CertificateDataElement.builder()
                        .value(
                            CertificateDataValueMedicalInvestigationList.builder()
                                .list(
                                    List.of(
                                        CertificateDataValueMedicalInvestigation.builder()
                                            .investigationType(
                                                CertificateDataValueCode.builder().id("").code("").build()
                                            )
                                            .date(
                                                CertificateDataValueDate.builder().build()
                                            )
                                            .informationSource(
                                                CertificateDataValueText.builder().build()
                                            )
                                            .build()
                                    )
                                )
                                .build()
                        )
                        .build());

                final var actualValue = QuestionUnderlag.toInternal(certificate);

                assertEquals(0, actualValue.size());
            }
        }
    }
}
