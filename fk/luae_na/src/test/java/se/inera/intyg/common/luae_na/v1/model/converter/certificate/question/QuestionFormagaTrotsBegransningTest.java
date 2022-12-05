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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FORMAGATROTSBEGRANSNING_DELSVAR_ID_23;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FORMAGATROTSBEGRANSNING_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FORMAGATROTSBEGRANSNING_TEXT_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalTextValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;

@ExtendWith(MockitoExtension.class)
class QuestionFormagaTrotsBegransningTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        doReturn("Lite text!").when(textProvider).get(anyString());
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionFormagaTrotsBegransning.toCertificate(null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return FORMAGATROTSBEGRANSNING_DELSVAR_ID_23;
        }

        @Override
        protected String getParent() {
            return CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE;
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
            return QuestionFormagaTrotsBegransning.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return FORMAGATROTSBEGRANSNING_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return FORMAGATROTSBEGRANSNING_DESCRIPTION_ID;
        }

        @Override
        protected String getJsonId() {
            return FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23;
        }
    }

    @Nested
    class IncludeValueTextTest extends ValueTextTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionFormagaTrotsBegransning.toCertificate(getText(), 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23;
        }

        @Override
        protected String getText() {
            return "Lite text som värde!";
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IncludeInternalTextValueTest extends InternalTextValueTest {

        @Override
        protected CertificateDataElement getElement(String expectedValue) {
            return QuestionFormagaTrotsBegransning.toCertificate(expectedValue, 0, textProvider);
        }

        @Override
        protected String toInternalTextValue(Certificate certificate) {
            return QuestionFormagaTrotsBegransning.toInternal(certificate);
        }
    }
}