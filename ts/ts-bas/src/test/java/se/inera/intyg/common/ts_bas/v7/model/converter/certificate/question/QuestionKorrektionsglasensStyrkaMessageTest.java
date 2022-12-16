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

package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFUNKTIONER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigMessageTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;

@ExtendWith(MockitoExtension.class)
class QuestionKorrektionsglasensStyrkaMessageTest {

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
            return QuestionKorrektionsglasensStyrkaMessage.toCertificate(0, textProvider);

        }

        @Override
        protected String getId() {
            return UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_ID;
        }

        @Override
        protected String getParent() {
            return SYNFUNKTIONER_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigMessageTest extends ConfigMessageTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKorrektionsglasensStyrkaMessage.toCertificate(0, textProvider);
        }

        @Override
        protected String getTextId() {
            return UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_TEXT_ID;
        }

        @Override
        protected MessageLevel getMessageLevel() {
            return MessageLevel.INFO;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    class IncludeValidationShowTests extends ValidationShowTest {

        @Override
        protected String getQuestionId() {
            return UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKorrektionsglasensStyrkaMessage.toCertificate(0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }
}