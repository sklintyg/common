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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNFUNKTIONER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_TEXT_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigMessageTest;
import se.inera.intyg.common.ts_bas.v6.model.internal.Syn;

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
            return QuestionKorrektionsglasensStyrkaMessage.toCertificate(null, 0, textProvider);
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
    class IncludeConfigViewText extends ConfigMessageTest {


        @Override
        protected String getMessageId() {
            return UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_MESSAGE_TEXT_ID;
        }

        @Override
        protected MessageLevel getMessageLevel() {
            return MessageLevel.INFO;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKorrektionsglasensStyrkaMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return null;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    class IncludeValueMessageTests {

        @Test
        void shouldBeVisibleIfNattblindhetIsTrue() {
            final var syn = Syn.builder().setKorrektionsglasensStyrka(true).build();
            final var element = QuestionKorrektionsglasensStyrkaMessage.toCertificate(syn, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldBeVisibleIfProgressivOgonsjukdomIsTrue() {
            final var syn = Syn.builder().setKorrektionsglasensStyrka(true).build();
            final var element = QuestionKorrektionsglasensStyrkaMessage.toCertificate(syn, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldBeVisibleIfSynfaltsdefekterIsTrue() {
            final var syn = Syn.builder().setKorrektionsglasensStyrka(true).build();
            final var element = QuestionKorrektionsglasensStyrkaMessage.toCertificate(syn, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldNotBeVisibile() {
            final var syn = Syn.builder().build();
            final var element = QuestionKorrektionsglasensStyrkaMessage.toCertificate(syn, 0, textProvider);
            assertFalse(element.getVisible());
        }
    }
}
