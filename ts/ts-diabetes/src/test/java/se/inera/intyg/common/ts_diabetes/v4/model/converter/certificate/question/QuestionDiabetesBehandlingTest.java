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

package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxMultipleCodeTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeListTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Behandling;

@ExtendWith(MockitoExtension.class)
class QuestionDiabetesBehandlingTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesBehandling.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return ALLMANT_BEHANDLING_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return ALLMANT_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCheckboxMultipleCodeTest extends ConfigCheckboxMultipleCodeTest {

        @Override
        protected List<CheckboxMultipleCode> getExpectedListOfCodes() {
            return List.of(
                CheckboxMultipleCode.builder()
                    .id(ALLMANT_BEHANDLING_INSULIN_JSON_ID)
                    .label(texts.get(ALLMANT_BEHANDLING_INSULIN_TEXT_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(ALLMANT_BEHANDLING_TABLETTER_JSON_ID)
                    .label(texts.get(ALLMANT_BEHANDLING_TABLETTER_TEXT_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(ALLMANT_BEHANDLING_ANNAN_JSON_ID)
                    .label(texts.get(ALLMANT_BEHANDLING_ANNAN_TEXT_ID))
                    .build()
            );
        }

        @Override
        protected Layout getLayout() {
            return null;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesBehandling.toCertificate(null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return ALLMANT_BEHANDLING_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueCodeListTest extends ValueCodeListTest<Allmant> {

        @Override
        protected CertificateDataElement getElement(Allmant expectedValue) {
            if (expectedValue != null) {
                final var allmant = Allmant.builder()
                    .setBehandling(
                        Behandling.builder()
                            .setInsulin(expectedValue.getBehandling().getInsulin())
                            .setTabletter(expectedValue.getBehandling().getTabletter())
                            .setAnnan(expectedValue.getBehandling().getAnnan())
                            .build()
                    )
                    .build();
                return QuestionDiabetesBehandling.toCertificate(allmant, 0, texts);
            } else {
                return QuestionDiabetesBehandling.toCertificate(null, 0, texts);
            }
        }

        @Override
        protected List<InputExpectedValuePair<Allmant, CertificateDataValueCodeList>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(
                    Allmant.builder().setBehandling(
                            Behandling.builder()
                                .setInsulin(true)
                                .setTabletter(true)
                                .setAnnan(true)
                                .build()
                        )
                        .build(),
                    CertificateDataValueCodeList.builder().list(
                            List.of(
                                CertificateDataValueCode.builder()
                                    .id(ALLMANT_BEHANDLING_INSULIN_JSON_ID)
                                    .code(ALLMANT_BEHANDLING_INSULIN_JSON_ID)
                                    .build(),
                                CertificateDataValueCode.builder()
                                    .id(ALLMANT_BEHANDLING_TABLETTER_JSON_ID)
                                    .code(ALLMANT_BEHANDLING_TABLETTER_JSON_ID)
                                    .build(),
                                CertificateDataValueCode.builder()
                                    .id(ALLMANT_BEHANDLING_ANNAN_JSON_ID)
                                    .code(ALLMANT_BEHANDLING_ANNAN_JSON_ID)
                                    .build()
                            ))
                        .build()
                ));
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return ALLMANT_BEHANDLING_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + ALLMANT_BEHANDLING_INSULIN_JSON_ID + ") || exists(" + ALLMANT_BEHANDLING_TABLETTER_JSON_ID + ") || exists("
                + ALLMANT_BEHANDLING_ANNAN_JSON_ID + ")";
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesBehandling.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationShowTest extends ValidationShowTest {

        @Override
        protected String getQuestionId() {
            return ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesBehandling.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }
    }

    @Nested
    class ToInternal {

        @Nested
        class Insulin {

            @Nested
            @TestInstance(Lifecycle.PER_CLASS)
            class IncludeInternalValuePairTest extends InternalValueTest<Allmant, Boolean> {

                @Override
                protected CertificateDataElement getElement(Allmant input) {
                    return QuestionDiabetesBehandling.toCertificate(input, 0, texts);
                }

                @Override
                protected Boolean toInternalValue(Certificate certificate) {
                    return QuestionDiabetesBehandling.toInternal(certificate, ALLMANT_BEHANDLING_INSULIN_JSON_ID);
                }

                @Override
                protected List<InputExpectedValuePair<Allmant, Boolean>> inputExpectedValuePairList() {
                    return
                        List.of(
                            new InputExpectedValuePair<>(
                                Allmant.builder().build(),
                                false),
                            new InputExpectedValuePair<>(
                                Allmant.builder()
                                    .setBehandling(
                                        Behandling.builder()
                                            .setInsulin(false)
                                            .build()
                                    )
                                    .build(),
                                false),
                            new InputExpectedValuePair<>(
                                Allmant.builder()
                                    .setBehandling(
                                        Behandling.builder()
                                            .setInsulin(true)
                                            .build()
                                    )
                                    .build(),
                                true)
                        );
                }
            }
        }

        @Nested
        class Tabletter {

            @Nested
            @TestInstance(Lifecycle.PER_CLASS)
            class IncludeInternalValuePairTest extends InternalValueTest<Allmant, Boolean> {

                @Override
                protected CertificateDataElement getElement(Allmant input) {
                    return QuestionDiabetesBehandling.toCertificate(input, 0, texts);
                }

                @Override
                protected Boolean toInternalValue(Certificate certificate) {
                    return QuestionDiabetesBehandling.toInternal(certificate, ALLMANT_BEHANDLING_TABLETTER_JSON_ID);
                }

                @Override
                protected List<InputExpectedValuePair<Allmant, Boolean>> inputExpectedValuePairList() {
                    return
                        List.of(
                            new InputExpectedValuePair<>(
                                Allmant.builder().build(),
                                false),
                            new InputExpectedValuePair<>(
                                Allmant.builder()
                                    .setBehandling(
                                        Behandling.builder()
                                            .setTabletter(false)
                                            .build()
                                    )
                                    .build(),
                                false),
                            new InputExpectedValuePair<>(
                                Allmant.builder()
                                    .setBehandling(
                                        Behandling.builder()
                                            .setTabletter(true)
                                            .build()
                                    )
                                    .build(),
                                true)
                        );
                }
            }
        }

        @Nested
        class Annan {

            @Nested
            @TestInstance(Lifecycle.PER_CLASS)
            class IncludeInternalValuePairTest extends InternalValueTest<Allmant, Boolean> {

                @Override
                protected CertificateDataElement getElement(Allmant input) {
                    return QuestionDiabetesBehandling.toCertificate(input, 0, texts);
                }

                @Override
                protected Boolean toInternalValue(Certificate certificate) {
                    return QuestionDiabetesBehandling.toInternal(certificate, ALLMANT_BEHANDLING_ANNAN_JSON_ID);
                }

                @Override
                protected List<InputExpectedValuePair<Allmant, Boolean>> inputExpectedValuePairList() {
                    return
                        List.of(
                            new InputExpectedValuePair<>(
                                Allmant.builder().build(),
                                false),
                            new InputExpectedValuePair<>(
                                Allmant.builder()
                                    .setBehandling(
                                        Behandling.builder()
                                            .setAnnan(false)
                                            .build()
                                    )
                                    .build(),
                                false),
                            new InputExpectedValuePair<>(
                                Allmant.builder()
                                    .setBehandling(
                                        Behandling.builder()
                                            .setAnnan(true)
                                            .build()
                                    )
                                    .build(),
                                true)
                        );
                }
            }
        }
    }
}
