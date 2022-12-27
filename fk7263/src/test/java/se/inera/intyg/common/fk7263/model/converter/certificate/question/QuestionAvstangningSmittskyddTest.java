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

import static se.inera.intyg.common.fk7263.model.converter.RespConstants.AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_SVAR_ID;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigViewText;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueViewTextTest;

class QuestionAvstangningSmittskyddTest {

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionAvstangningSmittskydd.toCertificate(null, 0);
        }

        @Override
        protected String getId() {
            return AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigViewTextTests extends ConfigViewText {


        @Override
        protected CertificateDataElement getElement() {
            return QuestionAvstangningSmittskydd.toCertificate(null, 0);
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
    class IncludeValueViewTextTests extends ValueViewTextTest<Boolean> {

        @Override
        protected CertificateDataElement getElement(Boolean expectedValue) {
            return QuestionAvstangningSmittskydd.toCertificate(expectedValue, 0);
        }

        @Override
        protected List<InputExpectedValuePair<Boolean, CertificateDataValueViewText>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(null, CertificateDataValueViewText.builder().text("Ej angivet").build()),
                new InputExpectedValuePair<>(false, CertificateDataValueViewText.builder().text("Nej").build()),
                new InputExpectedValuePair<>(true, CertificateDataValueViewText.builder().text("Ja").build())
            );
        }
    }
}