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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INTYG_AVSER_SVAR_TEXT_ID;

import java.util.EnumSet;
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
import se.inera.intyg.common.ts_bas.v6.model.internal.IntygAvser;
import se.inera.intyg.common.ts_bas.v6.model.internal.IntygAvserKategori;

@ExtendWith(MockitoExtension.class)
class QuestionIntygetAvserTest {

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
            return QuestionIntygetAvser.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getId() {
            return INTYG_AVSER_SVAR_ID_1;
        }

        @Override
        protected String getParent() {
            return INTYG_AVSER_CATEGORY_ID;
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
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetAvser.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return INTYG_AVSER_SVAR_TEXT_ID;
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
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueViewTextTests extends ValueViewTextTest<IntygAvser> {

        @Override
        protected CertificateDataElement getElement(IntygAvser expectedValue) {
            return QuestionIntygetAvser.toCertificate(expectedValue, 0, textProvider);
        }

        @Override
        protected List<InputExpectedValuePair<IntygAvser, CertificateDataValueViewText>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(IntygAvser.create(null), CertificateDataValueViewText.builder().text("Ej angivet").build()),
                new InputExpectedValuePair<>(IntygAvser.create(EnumSet.of(IntygAvserKategori.C1)),
                    CertificateDataValueViewText.builder().text(IntygAvserKategori.C1.name()).build()),
                new InputExpectedValuePair<>(IntygAvser.create(EnumSet.of(IntygAvserKategori.C1, IntygAvserKategori.D1E)),
                    CertificateDataValueViewText.builder().text(IntygAvserKategori.C1.name() + ", " + IntygAvserKategori.D1E.name())
                        .build())
            );
        }
    }
}
