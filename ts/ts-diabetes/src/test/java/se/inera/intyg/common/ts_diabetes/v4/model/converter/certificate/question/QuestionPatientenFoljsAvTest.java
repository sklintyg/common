/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_PRIMARVARD_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_SPECIALISTVARD_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_TEXT_ID;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigRadioMultipleCodeTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvVardniva;

@ExtendWith(MockitoExtension.class)
class QuestionPatientenFoljsAvTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setup() {
        when(textProvider.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionPatientenFoljsAv.toCertificate(null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return ALLMANT_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 3;
        }
    }

    @Nested
    class IncludeConfigRadioBooleanTest extends ConfigRadioMultipleCodeTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionPatientenFoljsAv.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return ALLMANT_PATIENTEN_FOLJS_AV_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return ALLMANT_PATIENTEN_FOLJS_AV_DESCRIPTION_ID;
        }

        @Override
        protected List<RadioMultipleCode> getExpectedRadioMultipleCodes() {
            return List.of(
                RadioMultipleCode.builder()
                    .id(KvVardniva.PRIMARVARD.getCode())
                    .label(textProvider.get(ALLMANT_PATIENTEN_FOLJS_AV_PRIMARVARD_LABEL_ID))
                    .build(),
                RadioMultipleCode.builder()
                    .id(KvVardniva.SPECIALISTVARD.getCode())
                    .label(textProvider.get(ALLMANT_PATIENTEN_FOLJS_AV_SPECIALISTVARD_LABEL_ID))
                    .build()
            );
        }

        @Override
        protected Layout getExpectedLayout() {
            return Layout.ROWS;
        }
    }

    @Nested
    class IncludeValueCodeTests extends ValueCodeTest {

        @Override
        protected CertificateDataElement getElement() {
            final var patientenFoljsAv = Allmant.builder().setPatientenFoljsAv(KvVardniva.SPECIALISTVARD).build();
            return QuestionPatientenFoljsAv.toCertificate(patientenFoljsAv, 0, textProvider);
        }

        @Override
        protected String getCodeId() {
            return KvVardniva.SPECIALISTVARD.getCode();
        }

        @Override
        protected String getCode() {
            return KvVardniva.SPECIALISTVARD.getCode();
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + KvVardniva.PRIMARVARD.getCode() + ") || exists(" + KvVardniva.SPECIALISTVARD.getCode() + ")";
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionPatientenFoljsAv.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class ToInternal {

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        class IncludeInternalValuePairTest extends InternalValueTest<Allmant, KvVardniva> {

            @Override
            protected CertificateDataElement getElement(Allmant input) {
                return QuestionPatientenFoljsAv.toCertificate(input, 0, textProvider);
            }

            @Override
            protected KvVardniva toInternalValue(Certificate certificate) {
                return QuestionPatientenFoljsAv.toInternal(certificate);
            }

            @Override
            protected List<InputExpectedValuePair<Allmant, KvVardniva>> inputExpectedValuePairList() {
                return List.of(
                    new InputExpectedValuePair(null, null),
                    new InputExpectedValuePair(Allmant.builder().setPatientenFoljsAv(null).build(), null),
                    new InputExpectedValuePair(
                        Allmant.builder().setPatientenFoljsAv(KvVardniva.SPECIALISTVARD).build(),
                        KvVardniva.SPECIALISTVARD
                    ),
                    new InputExpectedValuePair(
                        Allmant.builder().setPatientenFoljsAv(KvVardniva.PRIMARVARD).build(),
                        KvVardniva.PRIMARVARD
                    )
                );
            }
        }

        @Test
        void shouldHandleCodeWithEmptyStringValues() {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionPatientenFoljsAv.toCertificate(null, 0, textProvider))
                .build();

            certificate.getData().put(ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID, CertificateDataElement.builder()
                .value(
                    CertificateDataValueCode.builder()
                        .id("")
                        .code("")
                        .build()
                )
                .build()
            );

            final var actualValue = QuestionPatientenFoljsAv.toInternal(certificate);

            assertTrue(actualValue == null);
        }
    }
}