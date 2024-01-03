/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_KONTAKT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_LABEL_ID;

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
class QuestionKontaktMedArbetsgivarenTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKontaktMedArbetsgivaren.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return KONTAKT_ONSKAS_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return CATEGORY_KONTAKT_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCheckboxBooleanTests extends ConfigCheckboxBooleanTest {

        @Override
        protected String getJsonId() {
            return KONTAKT_ONSKAS_SVAR_JSON_ID;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKontaktMedArbetsgivaren.toCertificate(null, 0, texts);
        }

        @Override
        protected String getLabelId() {
            return KONTAKT_ONSKAS_SVAR_LABEL_ID;
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
        protected String getSelectedTextId() {
            return ANSWER_YES;
        }

        @Override
        protected String getUnselectedTextId() {
            return ANSWER_NOT_SELECTED;
        }
    }

    @Nested
    class IncludeValueBooleanTests extends ValueBooleanTest {

        private final Boolean expectedBoolean = true;

        @Override
        protected String getJsonId() {
            return KONTAKT_ONSKAS_SVAR_JSON_ID;
        }

        @Override
        protected Boolean getBoolean() {
            return expectedBoolean;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionKontaktMedArbetsgivaren.toCertificate(expectedBoolean, 0, texts);
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalBooleanValueTests extends InternalBooleanValueTest {

        @Override
        protected CertificateDataElement getElement(Boolean expectedValue) {
            return QuestionKontaktMedArbetsgivaren.toCertificate(expectedValue, 0, texts);
        }

        @Override
        protected Boolean toInternalBooleanValue(Certificate certificate) {
            return QuestionKontaktMedArbetsgivaren.toInternal(certificate);
        }
    }
}
