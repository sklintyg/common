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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_BESKRIVNING_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigTextAreaTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalTextValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;

@ExtendWith(MockitoExtension.class)
class QuestionArbetsformagaTrotsSjukdomBeskrivningTest {

    @Mock
    CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        when(textProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class ToCertificate {


        @Nested
        class IncludeCommonElementTests extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate(null, 0, textProvider);
            }

            @Override
            protected String getId() {
                return ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID;
            }

            @Override
            protected String getParent() {
                return ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID;
            }

            @Override
            protected int getIndex() {
                return 0;
            }
        }

        @Nested
        class IncludeConfigTextAreaTests extends ConfigTextAreaTest {

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return textProvider;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate(null, 0, textProvider);
            }

            @Override
            protected String getTextId() {
                return ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return null;
            }

            @Override
            protected String getJsonId() {
                return ARBETSFORMAGA_TROTS_SJUKDOM_BESKRIVNING_SVAR_JSON_ID;
            }
        }

        @Nested
        class IncludeValueTextTest extends ValueTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate("arbetsformagaBeskrivning", 0, textProvider);
            }

            @Override
            protected String getJsonId() {
                return ARBETSFORMAGA_TROTS_SJUKDOM_BESKRIVNING_SVAR_JSON_ID;
            }

            @Override
            protected String getText() {
                return "arbetsformagaBeskrivning";
            }
        }

        @Nested
        class IncludeValidationMandatory extends ValidationMandatoryTest {

            @Override
            protected String getQuestionId() {
                return ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID;
            }

            @Override
            protected String getExpression() {
                return "$" + ARBETSFORMAGA_TROTS_SJUKDOM_BESKRIVNING_SVAR_JSON_ID;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate(null, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 0;
            }
        }

        @Nested
        class IncludeValidationShowTests extends ValidationShowTest {

            @Override
            protected String getQuestionId() {
                return ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID;
            }

            @Override
            protected String getExpression() {
                return "$" + ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate(null, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }
        }

        @Nested
        class IncludeValidationTextTests extends ValidationTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate(null, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 2;
            }

            @Override
            protected short getLimit() {
                return 3500;
            }
        }
    }

    @Nested
    class ToInternal {

        @Nested
        @TestInstance(PER_CLASS)
        class IncludeInternalBooleanValueTest extends InternalTextValueTest {

            @Override
            protected CertificateDataElement getElement(String expectedValue) {
                return QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate(expectedValue, 0, textProvider);
            }

            @Override
            protected String toInternalTextValue(Certificate certificate) {
                return QuestionArbetsformagaTrotsSjukdomBeskrivning.toInternal(certificate);
            }
        }

    }
}
