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

import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_HEADER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_HEADER_TEXT;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigHeaderTest;

@ExtendWith(MockitoExtension.class)
class QuestionBedomningKorkortstypHeaderTest {

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningKorkortstypHeader.toCertificate(0);
        }

        @Override
        protected String getId() {
            return BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_HEADER_SVAR_ID;
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
    class IncludeConfigHeaderTests extends ConfigHeaderTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return null;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningKorkortstypHeader.toCertificate(0);
        }

        @Override
        protected String getTextId() {
            return BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_HEADER_TEXT;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }
}
