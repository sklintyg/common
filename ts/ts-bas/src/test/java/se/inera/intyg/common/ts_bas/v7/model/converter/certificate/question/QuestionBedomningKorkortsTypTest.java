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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_ANNAT_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_C1E_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_C1_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_CE_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_C_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_D1E_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_D1_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_DE_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_D_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_INTE_TA_STALLNING_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KORKORT_TAXI_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_TEXT_ID;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
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
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationDisableSubElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeListTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.Bedomning;
import se.inera.intyg.common.ts_bas.v7.model.internal.BedomningKorkortstyp;

@ExtendWith(MockitoExtension.class)
class QuestionBedomningKorkortsTypTest {

    @Mock
    CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        when(textProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningKorkortsTyp.toCertificate(Bedomning.builder().build(), 0, textProvider);
        }

        @Override
        protected String getId() {
            return LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return BEDOMNING_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCheckboxMultipleCodeTests extends ConfigCheckboxMultipleCodeTest {

        @Override
        protected List<CheckboxMultipleCode> getExpectedListOfCodes() {
            return List.of(
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR1.name())
                    .label(textProvider.get(KORKORT_C1_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR2.name())
                    .label(textProvider.get(KORKORT_C1E_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR3.name())
                    .label(textProvider.get(KORKORT_C_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR4.name())
                    .label(textProvider.get(KORKORT_CE_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR5.name())
                    .label(textProvider.get(KORKORT_D1_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR6.name())
                    .label(textProvider.get(KORKORT_D1E_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR7.name())
                    .label(textProvider.get(KORKORT_D_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR8.name())
                    .label(textProvider.get(KORKORT_DE_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR9.name())
                    .label(textProvider.get(KORKORT_TAXI_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR10.name())
                    .label(textProvider.get(KORKORT_ANNAT_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR11.name())
                    .label(textProvider.get(KORKORT_INTE_TA_STALLNING_LABEL_ID))
                    .build()
            );
        }

        @Override
        protected Layout getLayout() {
            return Layout.INLINE;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            final var bedomning = Bedomning.builder().build();
            return QuestionBedomningKorkortsTyp.toCertificate(bedomning, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueCodeListTests extends ValueCodeListTest<Bedomning> {

        @Override
        protected CertificateDataElement getElement(Bedomning expectedValue) {
            return QuestionBedomningKorkortsTyp.toCertificate(expectedValue, 0, textProvider);
        }

        @Override
        protected List<InputExpectedValuePair<Bedomning, CertificateDataValueCodeList>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(
                    Bedomning.builder()
                        .setKorkortstyp(EnumSet.of(BedomningKorkortstyp.VAR1, BedomningKorkortstyp.VAR2))
                        .build(),
                    CertificateDataValueCodeList.builder().list(
                            List.of(
                                CertificateDataValueCode.builder()
                                    .id(BedomningKorkortstyp.VAR1.name())
                                    .code(BedomningKorkortstyp.VAR1.name())
                                    .build(),
                                CertificateDataValueCode.builder()
                                    .id(BedomningKorkortstyp.VAR2.name())
                                    .code(BedomningKorkortstyp.VAR2.name())
                                    .build()
                            ))
                        .build()
                ));
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return
                "exists(" + BedomningKorkortstyp.VAR1.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR2.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR3.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR4.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR5.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR6.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR7.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR8.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR9.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR10.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR11.name() + ")";
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningKorkortsTyp.toCertificate(Bedomning.builder().build(), 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationDisableSubElementTests extends ValidationDisableSubElementTest {

        @Override
        protected String getQuestionId() {
            return LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + BedomningKorkortstyp.VAR11.name() + ")";
        }

        @Override
        protected List<String> getListOfIds() {
            return List.of(
                BedomningKorkortstyp.VAR1.name(),
                BedomningKorkortstyp.VAR2.name(),
                BedomningKorkortstyp.VAR3.name(),
                BedomningKorkortstyp.VAR4.name(),
                BedomningKorkortstyp.VAR5.name(),
                BedomningKorkortstyp.VAR6.name(),
                BedomningKorkortstyp.VAR7.name(),
                BedomningKorkortstyp.VAR8.name(),
                BedomningKorkortstyp.VAR9.name(),
                BedomningKorkortstyp.VAR10.name()
            );
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningKorkortsTyp.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }
    }

    @Nested
    class IncludeSecondValidationDisableSubElementTests extends ValidationDisableSubElementTest {

        @Override
        protected String getQuestionId() {
            return LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return
                "exists(" + BedomningKorkortstyp.VAR1.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR2.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR3.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR4.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR5.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR6.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR7.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR8.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR9.name()
                    + ") || exists(" + BedomningKorkortstyp.VAR10.name() + ")";
        }

        @Override
        protected List<String> getListOfIds() {
            return List.of(BedomningKorkortstyp.VAR11.name());
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningKorkortsTyp.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 2;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalValueTests extends InternalValueTest<Bedomning, Set<BedomningKorkortstyp>> {

        @Override
        protected CertificateDataElement getElement(Bedomning input) {
            return QuestionBedomningKorkortsTyp.toCertificate(input, 0, textProvider);
        }

        @Override
        protected Set<BedomningKorkortstyp> toInternalValue(Certificate certificate) {
            return QuestionBedomningKorkortsTyp.toInternal(certificate);
        }

        @Override
        protected List<InputExpectedValuePair<Bedomning, Set<BedomningKorkortstyp>>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(
                    null, Collections.emptySet()
                ),
                new InputExpectedValuePair<>(
                    Bedomning.builder().build(), Collections.emptySet()
                ),
                new InputExpectedValuePair<>(
                    Bedomning.builder().setKorkortstyp(EnumSet.of(BedomningKorkortstyp.VAR1)).build(), EnumSet.of(BedomningKorkortstyp.VAR1)
                ),
                new InputExpectedValuePair<>(
                    Bedomning.builder().setKorkortstyp(EnumSet.of(BedomningKorkortstyp.VAR1, BedomningKorkortstyp.VAR2)).build(),
                    EnumSet.of(BedomningKorkortstyp.VAR1, BedomningKorkortstyp.VAR2))
            );
        }
    }
}
