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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;

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
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalTextValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;

@ExtendWith(MockitoExtension.class)
class QuestionAktivitetsbegransningarTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Nested
        class IncludeTestCommon extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionAktivitetsbegransningar.toCertificate(null, getIndex(), texts);
            }

            @Override
            protected String getId() {
                return AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
            }

            @Override
            protected String getParent() {
                return AKTIVITETSBEGRANSNING_SVAR_ID_17;
            }

            @Override
            protected int getIndex() {
                return 5;
            }
        }

        @Nested
        class IncludeConfigTextAreaTest extends ConfigTextAreaTest {

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return texts;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionAktivitetsbegransningar.toCertificate(null, 0, getTextProviderMock());
            }

            @Override
            protected String getTextId() {
                return AKTIVITETSBEGRANSNING_DELSVAR_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return null;
            }

            @Override
            protected String getJsonId() {
                return AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
            }
        }

        @Nested
        class IncludeValueTextTest extends ValueTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionAktivitetsbegransningar.toCertificate(getText(), 0, texts);
            }

            @Override
            protected String getJsonId() {
                return AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
            }

            @Override
            protected String getText() {
                return "Här är en text";
            }
        }

        @Nested
        class IncludeValidationTextTest extends ValidationTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionAktivitetsbegransningar.toCertificate(null, 0, texts);
            }

            @Override
            protected int getValidationIndex() {
                return 0;
            }

            @Override
            protected short getLimit() {
                return 3500;
            }
        }

        @Nested
        class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionAktivitetsbegransningar.toCertificate(null, 0, texts);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }

            @Override
            protected String getQuestionId() {
                return AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
            }

            @Override
            protected String getExpression() {
                return "$" + AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
            }
        }
    }

    @Nested
    class ToInternal {

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class IncludeInternalTextValue extends InternalTextValueTest {

            @Override
            protected CertificateDataElement getElement(String expectedValue) {
                return QuestionAktivitetsbegransningar.toCertificate(expectedValue, 0, texts);
            }

            @Override
            protected String toInternalTextValue(Certificate certificate) {
                return QuestionAktivitetsbegransningar.toInternal(certificate);
            }
        }
    }
}
