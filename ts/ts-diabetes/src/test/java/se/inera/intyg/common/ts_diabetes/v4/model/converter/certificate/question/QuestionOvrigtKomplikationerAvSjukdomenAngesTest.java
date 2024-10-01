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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigTextAreaTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalTextValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;

@ExtendWith(MockitoExtension.class)
class QuestionOvrigtKomplikationerAvSjukdomenAngesTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        doReturn("Text!").when(textProvider).get(anyString());
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID;
        }

        @Override
        protected String getParent() {
            return OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID;
        }

        @Override
        protected int getIndex() {
            return 3;
        }
    }

    @Nested
    class IncludeConfigTextAreaTest extends ConfigTextAreaTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DESCRIPTION_ID;
        }

        @Override
        protected String getJsonId() {
            return OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
        }
    }

    @Nested
    class IncludeValueTextTest extends ValueTextTest {

        @Override
        protected CertificateDataElement getElement() {
            final var ovrigt = Ovrigt.builder().setKomplikationerAvSjukdomenAnges(getText()).build();
            return QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(ovrigt, 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
        }

        @Override
        protected String getText() {
            return "Detta är ett text värde!";
        }
    }

    @Nested
    class IncludeValidationShowTest extends ValidationShowTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getQuestionId() {
            return OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID;
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected String getQuestionId() {
            return OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
        }
    }

    @Nested
    class IncludeValidationTextTest extends ValidationTextTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 2;
        }

        @Override
        protected short getLimit() {
            return 189;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalTextValueTest extends InternalTextValueTest {

        @Override
        protected CertificateDataElement getElement(String expectedValue) {
            final var ovrigt = Ovrigt.builder().setKomplikationerAvSjukdomenAnges(expectedValue).build();
            return QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(ovrigt, 0, textProvider);
        }

        @Override
        protected String toInternalTextValue(Certificate certificate) {
            return QuestionOvrigtKomplikationerAvSjukdomenAnges.toInternal(certificate);
        }
    }
}
