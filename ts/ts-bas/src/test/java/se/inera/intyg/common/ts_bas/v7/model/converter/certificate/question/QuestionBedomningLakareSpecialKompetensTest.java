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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHORIGHET_LAKARE_SPECIALKOMPETENS_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHORIGHET_LAKARE_SPECIALKOMPETENS_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHORIGHET_LAKARE_SPECIALKOMPETENS_SVAR_TEXT_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigTextAreaTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.Bedomning;

@ExtendWith(MockitoExtension.class)
class QuestionBedomningLakareSpecialKompetensTest {

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
            return QuestionBedomningLakareSpecialKompetens.toCertificate(Bedomning.builder().build(), 0, textProvider);
        }

        @Override
        protected String getId() {
            return BEHORIGHET_LAKARE_SPECIALKOMPETENS_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return BEDOMNING_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigTextAreaTests extends ConfigTextAreaTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningLakareSpecialKompetens.toCertificate(Bedomning.builder().build(), 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return BEHORIGHET_LAKARE_SPECIALKOMPETENS_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected String getJsonId() {
            return BEHORIGHET_LAKARE_SPECIALKOMPETENS_JSON_ID;
        }
    }

    @Nested
    class IncludeValueTextTests extends ValueTextTest {

        private static final String TEXT = "text";

        @Override
        protected CertificateDataElement getElement() {
            final var bedomning = Bedomning.builder()
                .setLakareSpecialKompetens(TEXT)
                .build();
            return QuestionBedomningLakareSpecialKompetens.toCertificate(bedomning, 0, textProvider);
        }

        @Override
        protected String getJsonId() {
            return BEHORIGHET_LAKARE_SPECIALKOMPETENS_JSON_ID;
        }

        @Override
        protected String getText() {
            return TEXT;
        }
    }

    @Nested
    class IncludeValidationTextTests extends ValidationTextTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningLakareSpecialKompetens.toCertificate(Bedomning.builder().build(), 0, textProvider);

        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected short getLimit() {
            return 130;
        }
    }
}