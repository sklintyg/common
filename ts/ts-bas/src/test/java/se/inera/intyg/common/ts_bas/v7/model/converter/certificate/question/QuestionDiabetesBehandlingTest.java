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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHANDLING_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHANDLING_DIABETES_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULINBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULINBEHANDLING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KOSTBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KOSTBEHANDLING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TABLETTBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TABLETTBEHANDLING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_ID;

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
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;

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
            return BEHANDLING_DIABETES_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return HAR_DIABETES_CATEGORY_ID;
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
                    .id(KOSTBEHANDLING_DELSVAR_JSON_ID)
                    .label(texts.get(KOSTBEHANDLING_DELSVAR_TEXT_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(TABLETTBEHANDLING_DELSVAR_JSON_ID)
                    .label(texts.get(TABLETTBEHANDLING_DELSVAR_TEXT_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(INSULINBEHANDLING_DELSVAR_JSON_ID)
                    .label(texts.get(INSULINBEHANDLING_DELSVAR_TEXT_ID))
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
            return BEHANDLING_DIABETES_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueCodeListTest extends ValueCodeListTest<Diabetes> {

        @Override
        protected CertificateDataElement getElement(Diabetes expectedValue) {
            if (expectedValue != null) {
                final var diabetes = Diabetes.builder()
                    .setKost(expectedValue.getKost())
                    .setTabletter(expectedValue.getTabletter())
                    .setInsulin(expectedValue.getInsulin())
                    .build();

                return QuestionDiabetesBehandling.toCertificate(diabetes, 0, texts);
            } else {
                return QuestionDiabetesBehandling.toCertificate(null, 0, texts);
            }
        }

        @Override
        protected List<InputExpectedValuePair<Diabetes, CertificateDataValueCodeList>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(
                    Diabetes.builder()
                        .setKost(true)
                        .setTabletter(true)
                        .setInsulin(true)
                        .build(),
                    CertificateDataValueCodeList.builder().list(
                        List.of(
                            CertificateDataValueCode.builder()
                                .id(KOSTBEHANDLING_DELSVAR_JSON_ID)
                                .code(KOSTBEHANDLING_DELSVAR_JSON_ID)
                                .build(),
                            CertificateDataValueCode.builder()
                                .id(TABLETTBEHANDLING_DELSVAR_JSON_ID)
                                .code(TABLETTBEHANDLING_DELSVAR_JSON_ID)
                                .build(),
                            CertificateDataValueCode.builder()
                                .id(INSULINBEHANDLING_DELSVAR_JSON_ID)
                                .code(INSULINBEHANDLING_DELSVAR_JSON_ID)
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
            return BEHANDLING_DIABETES_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return KOSTBEHANDLING_DELSVAR_JSON_ID + " || " + TABLETTBEHANDLING_DELSVAR_JSON_ID + " || " + INSULINBEHANDLING_DELSVAR_JSON_ID;
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
            return TYP_AV_DIABETES_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + DiabetesKod.DIABETES_TYP_2.name();
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
        class Kost {

            @Nested
            @TestInstance(Lifecycle.PER_CLASS)
            class IncludeInternalValuePairTest extends InternalValueTest<Diabetes, Boolean> {

                @Override
                protected CertificateDataElement getElement(Diabetes input) {
                    return QuestionDiabetesBehandling.toCertificate(input, 0, texts);
                }

                @Override
                protected Boolean toInternalValue(Certificate certificate) {
                    return QuestionDiabetesBehandling.toInternal(certificate, KOSTBEHANDLING_DELSVAR_JSON_ID);
                }

                @Override
                protected List<InputExpectedValuePair<Diabetes, Boolean>> inputExpectedValuePairList() {
                    return
                        List.of(
                            new InputExpectedValuePair<>(
                                Diabetes.builder().build(),
                                false),
                            new InputExpectedValuePair<>(
                                Diabetes.builder()
                                    .setKost(false)
                                    .build(),
                                false),
                            new InputExpectedValuePair<>(
                                Diabetes.builder()
                                    .setKost(true)
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
            class IncludeInternalValuePairTest extends InternalValueTest<Diabetes, Boolean> {

                @Override
                protected CertificateDataElement getElement(Diabetes input) {
                    return QuestionDiabetesBehandling.toCertificate(input, 0, texts);
                }

                @Override
                protected Boolean toInternalValue(Certificate certificate) {
                    return QuestionDiabetesBehandling.toInternal(certificate, TABLETTBEHANDLING_DELSVAR_JSON_ID);
                }

                @Override
                protected List<InputExpectedValuePair<Diabetes, Boolean>> inputExpectedValuePairList() {
                    return
                        List.of(
                            new InputExpectedValuePair<>(
                                Diabetes.builder().build(),
                                false),
                            new InputExpectedValuePair<>(
                                Diabetes.builder()
                                    .setTabletter(false)
                                    .build(),
                                false),
                            new InputExpectedValuePair<>(
                                Diabetes.builder()
                                    .setTabletter(true)
                                    .build(),
                                true)
                        );
                }
            }
        }

        @Nested
        class Insulin {

            @Nested
            @TestInstance(Lifecycle.PER_CLASS)
            class IncludeInternalValuePairTest extends InternalValueTest<Diabetes, Boolean> {

                @Override
                protected CertificateDataElement getElement(Diabetes input) {
                    return QuestionDiabetesBehandling.toCertificate(input, 0, texts);
                }

                @Override
                protected Boolean toInternalValue(Certificate certificate) {
                    return QuestionDiabetesBehandling.toInternal(certificate, INSULINBEHANDLING_DELSVAR_JSON_ID);
                }

                @Override
                protected List<InputExpectedValuePair<Diabetes, Boolean>> inputExpectedValuePairList() {
                    return
                        List.of(
                            new InputExpectedValuePair<>(
                                Diabetes.builder().build(),
                                false),
                            new InputExpectedValuePair<>(
                                Diabetes.builder()
                                    .setInsulin(false)
                                    .build(),
                                false),
                            new InputExpectedValuePair<>(
                                Diabetes.builder()
                                    .setInsulin(true)
                                    .build(),
                                true)
                        );
                }
            }
        }
    }
}
