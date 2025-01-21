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

package se.inera.intyg.common.fk7263.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_GAR_INTE_BEDOMA_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_GAR_INTE_BEDOMA_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSFORMAGA_PROGNOS_SVAR_ID;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigViewTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueViewTextTest;

@ExtendWith(MockitoExtension.class)
class QuestionArbetsformogaGarInteAttBedomaBeskrivningTest {

    @Mock
    private CertificateMessagesProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionArbetsformogaGarInteAttBedomaBeskrivning.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return ARBETSFORMAGA_GAR_INTE_BEDOMA_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return ARBETSFORMAGA_PROGNOS_SVAR_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigViewTextTests extends ConfigViewTextTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return null;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionArbetsformogaGarInteAttBedomaBeskrivning.toCertificate(null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return "Test string";
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected CertificateMessagesProvider getMessageProviderMock() {
            return texts;
        }

        @Override
        protected String getMessageId() {
            return ARBETSFORMAGA_GAR_INTE_BEDOMA_TEXT_ID;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueViewTextTests extends ValueViewTextTest<String> {

        @Override
        protected CertificateDataElement getElement(String expectedValue) {
            return QuestionArbetsformogaGarInteAttBedomaBeskrivning.toCertificate(expectedValue, 0, texts);
        }

        @Override
        protected List<InputExpectedValuePair<String, CertificateDataValueViewText>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>("test", CertificateDataValueViewText.builder().text("test").build()),
                new InputExpectedValuePair<>(null, CertificateDataValueViewText.builder().text("Ej angivet").build())
            );
        }
    }
}
