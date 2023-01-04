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

package se.inera.intyg.common.fk7263.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_SVAR_ID;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigViewListTest;

@ExtendWith(MockitoExtension.class)
class QuestionPatientensArbetsformogaBedomsTest {

    @Mock
    CertificateMessagesProvider messagesProvider;

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionPatientensArbetsformogaBedoms.toCertificate(null, null, null,
                null, 0, messagesProvider);
        }

        @Override
        protected String getId() {
            return PATIENTENS_ARBETSFORMAGA_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return PATIENTENS_ARBETSFORMAGA_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigViewListTests extends ConfigViewListTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionPatientensArbetsformogaBedoms.toCertificate(null, null, null,
                null, 0, messagesProvider);
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
            return null;
        }
    }

    @Nested
    class IncludePatientensArbetsformogaBedomsValueTest {

        @Test
        void allValuesShouldBeDisplayed() {
            when(messagesProvider.get(any())).thenReturn("test string");

            final var nuvarandeArbetsuppgifter = "test";
            final var expectedValue = List.of(
                CertificateDataValueViewText.builder()
                    .text("test string" + " - " + nuvarandeArbetsuppgifter)
                    .build(),
                CertificateDataValueViewText.builder()
                    .text("test string")
                    .build(),
                CertificateDataValueViewText.builder()
                    .text("test string")
                    .build()
            );

            final var element = QuestionPatientensArbetsformogaBedoms.toCertificate(true,
                true, true, nuvarandeArbetsuppgifter, 0, messagesProvider);

            final var actualValue = (CertificateDataValueViewList) element.getValue();

            assertEquals(expectedValue, actualValue.getList());
        }

        @Test
        void valuesShouldNotBeDisplayed() {

            final var expectedValue = Collections.emptyList();

            final var element = QuestionPatientensArbetsformogaBedoms.toCertificate(false,
                false, false, null, 0, messagesProvider);

            final var actualValue = (CertificateDataValueViewList) element.getValue();

            assertEquals(expectedValue, actualValue.getList());
        }

        @Test
        void firstValueShouldBeDisplayed() {
            when(messagesProvider.get(any())).thenReturn("test string");

            final var expectedValue = List.of(
                CertificateDataValueViewText.builder()
                    .text("test string")
                    .build()
            );

            final var element = QuestionPatientensArbetsformogaBedoms.toCertificate(true,
                false, false, null, 0, messagesProvider);

            final var actualValue = (CertificateDataValueViewList) element.getValue();

            assertEquals(expectedValue, actualValue.getList());
        }

        @Test
        void thirdValueShouldBeDisplayed() {
            when(messagesProvider.get(any())).thenReturn("test string");

            final var expectedValue = List.of(
                CertificateDataValueViewText.builder()
                    .text("test string")
                    .build()
            );

            final var element = QuestionPatientensArbetsformogaBedoms.toCertificate(false,
                true, false, null, 0, messagesProvider);

            final var actualValue = (CertificateDataValueViewList) element.getValue();

            assertEquals(expectedValue, actualValue.getList());
        }

        @Test
        void secondValueShouldBeDisplayed() {
            when(messagesProvider.get(any())).thenReturn("test string");

            final var expectedValue = List.of(
                CertificateDataValueViewText.builder()
                    .text("test string")
                    .build()
            );

            final var element = QuestionPatientensArbetsformogaBedoms.toCertificate(false,
                false, true, null, 0, messagesProvider);

            final var actualValue = (CertificateDataValueViewList) element.getValue();

            assertEquals(expectedValue, actualValue.getList());
        }
    }
}
