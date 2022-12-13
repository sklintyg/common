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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_ICD_10_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_ICD_10_LABEL;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_KSH_97_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_KSH_97_LABEL;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_SVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnoser.LIMIT_DIAGNOSIS_DESC;
import static se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes.UE_DIAGNOSES;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTest;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

@ExtendWith(MockitoExtension.class)
class QuestionDiagnoserTest {

    private final String DIAGNOSIS_DESCRIPTION = "Beskrivning med egen text";
    private final String DIAGNOSIS_DISPLAYNAME = "Namn att visa upp";
    @Mock
    private CertificateTextProvider textProvider;
    @Mock
    private WebcertModuleService moduleService;

    @BeforeEach
    void setup() {
        when(textProvider.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class toCertificate {
        @Nested
        class IncludeCommonElementTest extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnoser.toCertificate(List.of(), 0, textProvider);
            }

            @Override
            protected String getId() {
                return DIAGNOS_SVAR_ID_6;
            }

            @Override
            protected String getParent() {
                return DIAGNOS_CATEGORY_ID;
            }

            @Override
            protected int getIndex() {
                return 0;
            }
        }

        @Nested
        class IncludeConfigTest extends ConfigTest {

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return textProvider;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnoser.toCertificate(List.of(), 0, textProvider);
            }

            @Override
            protected CertificateDataConfigTypes getType() {
                return UE_DIAGNOSES;
            }

            @Override
            protected String getTextId() {
                return DIAGNOS_SVAR_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return DIAGNOS_SVAR_DESCRIPTION_ID;
            }

            @Test
            void terminology() {
                var config = (CertificateDataConfigDiagnoses) QuestionDiagnoser.toCertificate(List.of(), 0, textProvider).getConfig();
                var terminology = config.getTerminology();
                assertAll(
                    () -> {
                        assertEquals(terminology.get(0).getId(), DIAGNOS_ICD_10_ID);
                        assertEquals(terminology.get(0).getLabel(), DIAGNOS_ICD_10_LABEL);
                        assertEquals(terminology.get(1).getId(), DIAGNOS_KSH_97_ID);
                        assertEquals(terminology.get(1).getLabel(), DIAGNOS_KSH_97_LABEL);
                    }
                );
            }

            @Test
            void list() {
                var config = (CertificateDataConfigDiagnoses) QuestionDiagnoser.toCertificate(List.of(), 0, textProvider).getConfig();
                var list = config.getList();
                assertAll(
                    () -> {
                        assertEquals(list.get(0).getId(), "1");
                        assertEquals(list.get(1).getId(), "2");
                        assertEquals(list.get(2).getId(), "3");
                    }
                );
            }
        }

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class includeValueTest extends ValueTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnoser.toCertificate(List.of(), 0, textProvider);
            }

            @Override
            protected CertificateDataValueType getType() {
                return CertificateDataValueDiagnosisList.builder().build().getType();
            }

            Stream<List<Diagnos>> diagnosisListValues() {
                return Stream.of(Arrays.asList(
                    Diagnos.create("F500", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                ),
                    Arrays.asList(
                    Diagnos.create("", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                ),
                    Collections.emptyList());
            }

            @ParameterizedTest
            @MethodSource("diagnosisListValues")
            void shouldIncludeDiagnosValue(List<Diagnos> expectedValue) {
                final var index = 1;
                final var certificateDataElement = (CertificateDataValueDiagnosisList) QuestionDiagnoser
                    .toCertificate(expectedValue, index, textProvider).getValue();
                var resultList = certificateDataElement.getList();

                assertAll(
                    () -> {
                        for (int i = 0; i < expectedValue.size(); i++) {
                            assertEquals(expectedValue.get(i).getDiagnosBeskrivning(), resultList.get(i).getDescription());
                            assertEquals(expectedValue.get(i).getDiagnosKodSystem(), resultList.get(i).getTerminology());
                            assertEquals(expectedValue.get(i).getDiagnosKod(), resultList.get(i).getCode());

                        }
                    }
                );
            }

            @Test
            void shouldIncludeDiagnosValueNull() {
                final var certificateDataElement = (CertificateDataValueDiagnosisList) QuestionDiagnoser
                    .toCertificate(null, 1, textProvider).getValue();
                var resultList = certificateDataElement.getList();

                assertTrue(resultList.isEmpty());
            }

            @Test
            void shouldExcludeDiagnosKodNull() {
                var diagnoser = Arrays.asList(
                    Diagnos.create(null, "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME));
                final var certificateDataElement = (CertificateDataValueDiagnosisList) QuestionDiagnoser
                    .toCertificate(diagnoser, 1, textProvider).getValue();
                var resultList = certificateDataElement.getList();

                assertEquals(resultList.size(), 2);
            }
        }

        @Nested
        class includeValidationMandatoryTest extends ValidationMandatoryTest {

            @Override
            protected String getQuestionId() {
                return DIAGNOS_SVAR_ID_6;
            }

            @Override
            protected String getExpression() {
                return singleExpression("1");
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnoser.toCertificate(List.of(), 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 0;
            }
        }

        @Nested
        class includeValidationTextTest extends ValidationTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnoser.toCertificate(List.of(), 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }

            @Override
            protected short getLimit() {
                return LIMIT_DIAGNOSIS_DESC;
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class toInternal {

        Stream<List<Diagnos>> diagnosisListValues() {
            return Stream.of(Arrays.asList(
                    Diagnos.create("F500", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                ),
                Arrays.asList(
                    Diagnos.create("", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                ),
                Collections.emptyList());
        }

        @ParameterizedTest
        @MethodSource("diagnosisListValues")
        void shouldIncludeDiagnosValue(List<Diagnos> expectedValue) {
            if (!expectedValue.isEmpty()){
                when(moduleService.getDescriptionFromDiagnosKod(anyString(), anyString())).thenReturn(DIAGNOSIS_DISPLAYNAME);
            }

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDiagnoser.toCertificate(expectedValue, 0, textProvider))
                .build();

            final var actualValue = QuestionDiagnoser.toInternal(certificate, moduleService);

            Assertions.assertEquals(expectedValue, actualValue);
            verify(moduleService, times(expectedValue.size())).getDescriptionFromDiagnosKod(anyString(), anyString());
        }

        @Test
        void shouldReturnListWithLenghtBasedOnId() {
            when(moduleService.getDescriptionFromDiagnosKod(anyString(), anyString())).thenReturn(DIAGNOSIS_DISPLAYNAME);

            var expectedValue = Arrays.asList(
                Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                Diagnos.create(null, null, null, null),
                Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME));
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDiagnoser.toCertificate(expectedValue, 0, textProvider))
                .build();

            final var actualValue = QuestionDiagnoser.toInternal(certificate, moduleService);

            assertEquals(expectedValue, actualValue);
            assertEquals(actualValue.size(), 3);
        }
    }
}