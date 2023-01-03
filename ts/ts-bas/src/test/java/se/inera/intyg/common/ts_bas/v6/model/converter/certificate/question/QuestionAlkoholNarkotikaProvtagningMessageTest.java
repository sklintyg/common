/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
 *
 * This file is part of sklint (https://github.com/sklintyg).
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
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.MISSBRUK_BEROENDE_LAKEMEDEL_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_TEXT_ID;

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
import se.inera.intyg.common.ts_bas.v6.model.internal.NarkotikaLakemedel;

@ExtendWith(MockitoExtension.class)
class QuestionAlkoholNarkotikaProvtagningMessageTest {

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
            return QuestionAlkoholNarkotikaProvtagningMessage.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getId() {
            return PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_ID;
        }

        @Override
        protected String getParent() {
            return MISSBRUK_BEROENDE_LAKEMEDEL_CATEGORY_ID;
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
            return PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_TEXT_ID;
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
            return QuestionAlkoholNarkotikaProvtagningMessage.toCertificate(null, 0, textProvider);
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
    class MessageVisbilityTests {

        @Test
        void shouldBeVisibleIfProvtagningIsTrue() {
            final var narkotikaLakemedel = NarkotikaLakemedel.builder().setProvtagningBehovs(true).build();
            final var element = QuestionAlkoholNarkotikaProvtagningMessage.toCertificate(narkotikaLakemedel, 0, textProvider);
            assertTrue(element.getVisible());
        }

        @Test
        void shouldNotBeVisibleIfProvtagningIsFalse() {
            final var narkotikaLakemedel = NarkotikaLakemedel.builder().setProvtagningBehovs(false).build();
            final var element = QuestionAlkoholNarkotikaProvtagningMessage.toCertificate(narkotikaLakemedel, 0, textProvider);
            assertFalse(element.getVisible());
        }

        @Test
        void shouldNotBeVisibleIfProvtagningIsNull() {
            final var narkotikaLakemedel = NarkotikaLakemedel.builder().build();
            final var element = QuestionAlkoholNarkotikaProvtagningMessage.toCertificate(narkotikaLakemedel, 0, textProvider);
            assertFalse(element.getVisible());
        }
    }
}
