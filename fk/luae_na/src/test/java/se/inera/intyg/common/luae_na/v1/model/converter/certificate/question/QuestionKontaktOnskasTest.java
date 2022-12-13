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

package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalBooleanValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueBooleanTest;

@ExtendWith(MockitoExtension.class)
class QuestionKontaktOnskasTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        doReturn("Text!").when(textProvider).get(anyString());
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKontaktOnskas.toCertificate(null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return KONTAKT_ONSKAS_SVAR_ID_26;
        }

        @Override
        protected String getParent() {
            return KONTAKT_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 3;
        }
    }

    @Nested
    class IncludeConfigCheckboxBooleanTest extends ConfigCheckboxBooleanTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKontaktOnskas.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return KONTAKT_ONSKAS_DELSVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected String getJsonId() {
            return KONTAKT_ONSKAS_SVAR_JSON_ID_26;
        }

        @Override
        protected String getLabelId() {
            return KONTAKT_ONSKAS_DELSVAR_TEXT_ID;
        }

        @Override
        protected String getSelectedTextId() {
            return ANSWER_YES;
        }

        @Override
        protected String getUnselectedTextId() {
            return ANSWER_NOT_SELECTED;
        }
    }

    @Nested
    class IncludeValueBooleanTest extends ValueBooleanTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKontaktOnskas.toCertificate(true, 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return KONTAKT_ONSKAS_SVAR_JSON_ID_26;
        }

        @Override
        protected Boolean getSelected() {
            return true;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalBooleanValueTest extends InternalBooleanValueTest {

        @Override
        protected CertificateDataElement getElement(Boolean expectedValue) {
            return QuestionKontaktOnskas.toCertificate(expectedValue, 0, textProvider);
        }

        @Override
        protected Boolean toInternalBooleanValue(Certificate certificate) {
            return QuestionKontaktOnskas.toInternal(certificate);
        }
    }
}
