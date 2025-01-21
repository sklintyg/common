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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_SYSSELSATTNING_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_LABEL_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_TEXT_ID;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxMultipleCodeTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationDisableTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeListTest;

class QuestionSysselsattningTypTest {

    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(any(String.class))).thenReturn(TYP_AV_SYSSELSATTNING_SVAR_LABEL_ID);
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }

        @Override
        protected String getId() {
            return TYP_AV_SYSSELSATTNING_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return CATEGORY_SYSSELSATTNING_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCheckboxBooleanTests extends ConfigCheckboxMultipleCodeTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }

        @Override
        protected String getTextId() {
            return TYP_AV_SYSSELSATTNING_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected List<CheckboxMultipleCode> getExpectedListOfCodes() {
            return List.of(
                CheckboxMultipleCode.builder()
                    .id(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                    .label(TYP_AV_SYSSELSATTNING_SVAR_LABEL_ID)
                    .build()
            );
        }

        @Override
        protected Layout getLayout() {
            return null;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueBooleanTests extends ValueCodeListTest<Null> {

        @Override
        protected CertificateDataElement getElement(Null expectedValue) {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }

        @Override
        protected List<InputExpectedValuePair<Null, CertificateDataValueCodeList>> inputExpectedValuePairList() {
            return List.of(new InputExpectedValuePair<>(null, CertificateDataValueCodeList.builder()
                .list(
                    List.of(
                        CertificateDataValueCode.builder()
                            .id(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                            .code(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                            .build()
                    )
                ).build()));
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }
    }

    @Nested
    class IncludeValidationDisableTests extends ValidationDisableTest {

        @Override
        protected String getQuestionId() {
            return TYP_AV_SYSSELSATTNING_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + SysselsattningsTyp.NUVARANDE_ARBETE.getId() + ")";
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSysselsattningTyp.toCertificate(0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeInternalSysselsattningValueTests {

        @Test
        void shallReturnSysselsattningNuvarandeArbete() {
            final var result = QuestionSysselsattningTyp.toInternal();
            assertEquals(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE), result.get(0));
        }
    }
}
