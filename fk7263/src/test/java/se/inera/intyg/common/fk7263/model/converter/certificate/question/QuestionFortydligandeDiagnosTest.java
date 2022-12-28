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

package se.inera.intyg.common.fk7263.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.DIAGNOS_FORTYDLIGANDE_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.DIAGNOS_FORTYDLIGANDE_SVAR_TEXT_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigViewTextTest;

@ExtendWith(MockitoExtension.class)
class QuestionFortydligandeDiagnosTest {

    @Mock
    CertificateMessagesProvider messagesProvider;

    @BeforeEach
    void setUp() {
        when(messagesProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionFortydligandeDiagnos.toCertificate(null, null, null, null, 0, messagesProvider);
        }

        @Override
        protected String getId() {
            return DIAGNOS_FORTYDLIGANDE_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return DIAGNOS_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigViewTextTests extends ConfigViewTextTest {


        @Override
        protected CertificateDataElement getElement() {
            return QuestionFortydligandeDiagnos.toCertificate(null, null, null, null, 0, messagesProvider);
        }


        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected CertificateMessagesProvider getMessageProviderMock() {
            return messagesProvider;
        }

        @Override
        protected String getMessageId() {
            return DIAGNOS_FORTYDLIGANDE_SVAR_TEXT_ID;
        }
    }

    @Nested
    class IncludeValueViewText {

        @Test
        void shouldIncludeValueType() {
            final var element = QuestionFortydligandeDiagnos.toCertificate(null,
                null, null, null, 0, messagesProvider);
            final var type = element.getValue().getType();
            assertEquals(CertificateDataValueType.VIEW_TEXT, type);
        }

        @Test
        void shouldIncludeTextValue() {
            final var expectedResult1 = "test1 test2 test3 test4";
            final var expectedResult2 = "test1 test2";
            final var expectedResult3 = "Ej angivet";

            assertAll(
                () -> assertEquals(
                    CertificateDataValueViewText.builder().text(expectedResult1).build(),
                    QuestionFortydligandeDiagnos.toCertificate("test1",
                        "test2", "test3", "test4", 0, messagesProvider).getValue()
                ),
                () -> assertEquals(
                    CertificateDataValueViewText.builder().text(expectedResult2).build(),
                    QuestionFortydligandeDiagnos.toCertificate("test1",
                        "test2", null, null, 0, messagesProvider).getValue()
                ),
                () -> assertEquals(
                    CertificateDataValueViewText.builder().text(expectedResult3).build(),
                    QuestionFortydligandeDiagnos.toCertificate(null,
                        null, null, null, 0, messagesProvider).getValue()
                )
            );
        }
    }
}