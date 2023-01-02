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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.DUBBELSEENDE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.DUBBELSEENDE_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNFUNKTIONER_CATEGORY_ID;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
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
import se.inera.intyg.common.ts_bas.v6.model.internal.Syn;

@ExtendWith(MockitoExtension.class)
class QuestionDubbelseendeTest {

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
            return QuestionDubbelseende.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getId() {
            return DUBBELSEENDE_SVAR_ID;
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
    class IncludeConfigViewText extends ConfigViewTextTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDubbelseende.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return DUBBELSEENDE_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected CertificateMessagesProvider getMessageProviderMock() {
            return null;
        }

        @Override
        protected String getMessageId() {
            return null;
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    class IncludeValueViewText extends ValueViewTextTest<Syn> {

        @Override
        protected CertificateDataElement getElement(Syn expectedValue) {
            return QuestionDubbelseende.toCertificate(expectedValue, 0, textProvider);
        }

        @Override
        protected List<InputExpectedValuePair<Syn, CertificateDataValueViewText>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(Syn.builder().build(), CertificateDataValueViewText.builder().text("Ej Angivet").build()),
                new InputExpectedValuePair<>(Syn.builder().setDiplopi(false).build(),
                    CertificateDataValueViewText.builder().text("Nej").build()),
                new InputExpectedValuePair<>(Syn.builder().setDiplopi(true).build(),
                    CertificateDataValueViewText.builder().text("Ja").build())
            );
        }
    }
}