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

package se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question;

import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.IDENTITET_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID;

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
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Vardkontakt;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;

class QuestionIdentitetStyrktGenomTest {

    @Nested
    class ToCertificate {

        @Nested
        class IncludeCommonElementTest extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionIdentitetStyrktGenom.toCertificate(null, 0);
            }

            @Override
            protected String getId() {
                return IDENTITET_STYRKT_GENOM_SVAR_ID;
            }

            @Override
            protected String getParent() {
                return IDENTITET_CATEGORY_ID;
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
                return QuestionIdentitetStyrktGenom.toCertificate(null, 0);
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
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueViewTextTests extends ValueViewTextTest<Vardkontakt> {

        @Override
        protected CertificateDataElement getElement(Vardkontakt expectedValue) {
            return QuestionIdentitetStyrktGenom.toCertificate(expectedValue, 0);
        }

        @Override
        protected List<InputExpectedValuePair<Vardkontakt, CertificateDataValueViewText>> inputExpectedValuePairList() {
            final var vardKontakt = new Vardkontakt();
            vardKontakt.setIdkontroll(IdKontrollKod.KORKORT.name());
            return List.of(
                new InputExpectedValuePair<>(
                    new Vardkontakt(), CertificateDataValueViewText.builder().text("Ej angivet").build()),
                new InputExpectedValuePair<>(vardKontakt,
                    CertificateDataValueViewText.builder().text(IdKontrollKod.KORKORT.getDescription()).build())
            );
        }
    }
}
