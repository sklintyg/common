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
package se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_1_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_2_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSES_LIST_ITEM_3_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_ICD_10_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_ICD_10_LABEL;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_KSH_97_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_KSH_97_LABEL;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_SVAR_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.withCitation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigDiagnosTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueDiagnosListTest;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

@ExtendWith(MockitoExtension.class)
class QuestionDiagnoserTest {

    protected static final String DIAGNOSIS_DESCRIPTION = "Beskrivning med egen text";
    protected static final String DIAGNOSIS_DISPLAYNAME = "Namn att visa upp";

    @Mock
    private CertificateTextProvider textProvider;

    @Mock
    private WebcertModuleService webcertModuleService;

    @BeforeEach
    void setUp() {
        doReturn("Text!").when(textProvider).get(anyString());
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiagnoser.toCertificate(null, getIndex(), textProvider);
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
            return 3;
        }
    }

    @Nested
    class IncludeConfigDiagnosTest extends ConfigDiagnosTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiagnoser.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return DIAGNOS_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected Map<String, String> getTerminologies() {
            return Map.of(
                DIAGNOS_ICD_10_ID, DIAGNOS_ICD_10_LABEL,
                DIAGNOS_KSH_97_ID, DIAGNOS_KSH_97_LABEL
            );
        }

        @Override
        protected List<String> getDiagnosListItemIds() {
            return List.of(DIAGNOSES_LIST_ITEM_1_ID, DIAGNOSES_LIST_ITEM_2_ID, DIAGNOSES_LIST_ITEM_3_ID);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IncludeValueDiagnosListTest extends ValueDiagnosListTest<List<Diagnos>> {

        @Override
        protected CertificateDataElement getElement(List<Diagnos> input) {
            return QuestionDiagnoser.toCertificate(input, 0, textProvider);
        }

        @Override
        protected List<InputExpectedValuePair<List<Diagnos>, CertificateDataValueDiagnosisList>> inputExpectedValuePairList() {
            return List.of(
                inputWhenDiagnoserIsNull(),
                inputWhenDiagnoserIsEmpty(),
                inputWhenDiagnoserIncludesOne(),
                inputWhenDiagnoserIncludesThree(),
                inputWhenDiagnoserIncludes3ButThe1stIsNull()
            );
        }

        private InputExpectedValuePair<List<Diagnos>, CertificateDataValueDiagnosisList> inputWhenDiagnoserIsNull() {
            return new InputExpectedValuePair<>(null, CertificateDataValueDiagnosisList.builder().list(Collections.emptyList()).build());
        }

        private InputExpectedValuePair<List<Diagnos>, CertificateDataValueDiagnosisList> inputWhenDiagnoserIsEmpty() {
            return new InputExpectedValuePair<>(Collections.emptyList(),
                CertificateDataValueDiagnosisList.builder().list(Collections.emptyList()).build());
        }

        private InputExpectedValuePair<List<Diagnos>, CertificateDataValueDiagnosisList> inputWhenDiagnoserIncludesOne() {
            return new InputExpectedValuePair<>(
                List.of(Diagnos.create("F500", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)),
                CertificateDataValueDiagnosisList.builder()
                    .list(
                        List.of(
                            CertificateDataValueDiagnosis.builder()
                                .id(DIAGNOSES_LIST_ITEM_1_ID)
                                .code("F500")
                                .terminology(DIAGNOS_ICD_10_ID)
                                .description(DIAGNOSIS_DESCRIPTION)
                                .build()
                        )
                    )
                    .build()
            );
        }

        private InputExpectedValuePair<List<Diagnos>, CertificateDataValueDiagnosisList> inputWhenDiagnoserIncludesThree() {
            return new InputExpectedValuePair<>(
                List.of(
                    Diagnos.create("", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F501", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                ),
                CertificateDataValueDiagnosisList.builder()
                    .list(
                        List.of(
                            CertificateDataValueDiagnosis.builder()
                                .id(DIAGNOSES_LIST_ITEM_1_ID)
                                .code("")
                                .terminology(DIAGNOS_ICD_10_ID)
                                .description(DIAGNOSIS_DESCRIPTION)
                                .build(),
                            CertificateDataValueDiagnosis.builder()
                                .id(DIAGNOSES_LIST_ITEM_2_ID)
                                .code("F501")
                                .terminology(DIAGNOS_ICD_10_ID)
                                .description(DIAGNOSIS_DESCRIPTION)
                                .build(),
                            CertificateDataValueDiagnosis.builder()
                                .id(DIAGNOSES_LIST_ITEM_3_ID)
                                .code("F502")
                                .terminology(DIAGNOS_ICD_10_ID)
                                .description(DIAGNOSIS_DESCRIPTION)
                                .build()
                        )
                    )
                    .build()
            );
        }

        private InputExpectedValuePair<List<Diagnos>, CertificateDataValueDiagnosisList> inputWhenDiagnoserIncludes3ButThe1stIsNull() {
            return new InputExpectedValuePair<>(
                List.of(
                    Diagnos.create(null, DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F501", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                ),
                CertificateDataValueDiagnosisList.builder()
                    .list(
                        List.of(
                            CertificateDataValueDiagnosis.builder()
                                .id(DIAGNOSES_LIST_ITEM_2_ID)
                                .code("F501")
                                .terminology(DIAGNOS_ICD_10_ID)
                                .description(DIAGNOSIS_DESCRIPTION)
                                .build(),
                            CertificateDataValueDiagnosis.builder()
                                .id(DIAGNOSES_LIST_ITEM_3_ID)
                                .code("F502")
                                .terminology(DIAGNOS_ICD_10_ID)
                                .description(DIAGNOSIS_DESCRIPTION)
                                .build()
                        )
                    )
                    .build()
            );
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return DIAGNOS_SVAR_ID_6;
        }

        @Override
        protected String getExpression() {
            return withCitation(singleExpression(DIAGNOSES_LIST_ITEM_1_ID));
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiagnoser.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationTextTest extends ValidationTextTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiagnoser.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected short getLimit() {
            return (short) 81;
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IncludeInternalValueTest extends InternalValueTest<List<Diagnos>, List<Diagnos>> {

        @Override
        protected CertificateDataElement getElement(List<Diagnos> input) {
            if (input != null && !input.isEmpty()) {
                doReturn(DIAGNOSIS_DISPLAYNAME).when(webcertModuleService).getDescriptionFromDiagnosKod(anyString(), anyString());
            }
            return QuestionDiagnoser.toCertificate(input, 0, textProvider);
        }

        @Override
        protected List<Diagnos> toInternalValue(Certificate certificate) {
            return QuestionDiagnoser.toInternal(certificate, webcertModuleService);
        }

        @Override
        protected List<InputExpectedValuePair<List<Diagnos>, List<Diagnos>>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(null, Collections.emptyList()),
                new InputExpectedValuePair<>(Collections.emptyList(), Collections.emptyList()),
                new InputExpectedValuePair<>(
                    List.of(
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    ),
                    List.of(
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        Diagnos.create("F500", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    ),
                    List.of(
                        Diagnos.create("F500", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        Diagnos.create("F500", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F501", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    ),
                    List.of(
                        Diagnos.create("F500", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F501", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        Diagnos.create("F500", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create(null, null, null, null),
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    ),
                    List.of(
                        Diagnos.create("F500", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create(null, null, null, null),
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    )
                ),
                new InputExpectedValuePair<>(
                    List.of(
                        Diagnos.create("", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create(null, null, null, null),
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    ),
                    List.of(
                        Diagnos.create("", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create(null, null, null, null),
                        Diagnos.create("F502", DIAGNOS_ICD_10_ID, DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)
                    )
                )
            );
        }
    }
}