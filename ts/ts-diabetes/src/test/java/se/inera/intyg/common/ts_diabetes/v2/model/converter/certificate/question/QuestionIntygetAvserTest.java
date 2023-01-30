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

package se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question;

import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;

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
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvserKategori;

class QuestionIntygetAvserTest {

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetAvser.toCertificate(null, 0);
        }

        @Override
        protected String getId() {
            return INTYG_AVSER_SVAR_ID;
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
            return null;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionIntygetAvser.toCertificate(null, 0);
        }

        @Override
        protected String getTextId() {
            return null;
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
            return QuestionIntygetAvser.toCertificate(expectedValue, 0);
        }

        @Override
        protected List<InputExpectedValuePair<IntygAvser, CertificateDataValueViewText>> inputExpectedValuePairList() {
            final var intygAvserWithOneValue = new IntygAvser();
            intygAvserWithOneValue.getKorkortstyp().add(IntygAvserKategori.C1);

            final var intygAvserWithMultipleValues = new IntygAvser();
            intygAvserWithMultipleValues.getKorkortstyp().add(IntygAvserKategori.C1);
            intygAvserWithMultipleValues.getKorkortstyp().add(IntygAvserKategori.D1E);
            
            return List.of(
                new InputExpectedValuePair<>(new IntygAvser(),
                    CertificateDataValueViewText.builder().text("Ej angivet").build()),
                new InputExpectedValuePair<>(intygAvserWithOneValue, CertificateDataValueViewText.builder().text("C1").build()),
                new InputExpectedValuePair<>(intygAvserWithMultipleValues, CertificateDataValueViewText.builder()
                    .text("C1, D1E").build())
            );
        }
    }
}
