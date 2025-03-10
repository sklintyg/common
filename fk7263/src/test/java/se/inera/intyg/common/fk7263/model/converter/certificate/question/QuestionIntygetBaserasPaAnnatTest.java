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

import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ANNAT_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ANNAT_DELSVAR_TEXT;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.INTYGET_BASERAS_PA_CATEGORY_ID;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigViewTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueViewTextTest;
import se.inera.intyg.common.support.model.InternalDate;

class QuestionIntygetBaserasPaAnnatTest {

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetBaserasPaAnnat.toCertificate(null, 0);
        }

        @Override
        protected String getId() {
            return ANNAT_DELSVAR_ID;
        }

        @Override
        protected String getParent() {
            return INTYGET_BASERAS_PA_CATEGORY_ID;
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
            return QuestionIntygetBaserasPaAnnat.toCertificate(null, 0);
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

        @Override
        protected String getTextId() {
            return ANNAT_DELSVAR_TEXT;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueViewTextTests extends ValueViewTextTest<InternalDate> {

        @Override
        protected CertificateDataElement getElement(InternalDate expectedValue) {
            return QuestionIntygetBaserasPaAnnat.toCertificate(expectedValue, 0);
        }

        @Override
        protected List<InputExpectedValuePair<InternalDate, CertificateDataValueViewText>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(null, CertificateDataValueViewText.builder().text("Ej angivet").build()),
                new InputExpectedValuePair<>(new InternalDate(LocalDate.now()),
                    CertificateDataValueViewText.builder().text(LocalDate.now().toString()).build()),
                new InputExpectedValuePair<>(new InternalDate("1234"), CertificateDataValueViewText.builder().text("Ej angivet").build())
            );
        }
    }
}
