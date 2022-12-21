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

package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_CATEGORY_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARD_SJUKHUS_KONTAKT_LAKARE_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARD_SJUKHUS_KONTAKT_LAKARE_CATEGORY_TEXT_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCategoryTest;

@ExtendWith(MockitoExtension.class)
class CategorySjukhusvardTest {

    @Mock
    CertificateTextProvider textProvider;

    @BeforeEach
    public void setup() {
        when(textProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return CategorySjukhusvard.toCertificate(0, textProvider);
        }

        @Override
        protected String getId() {
            return VARD_SJUKHUS_KONTAKT_LAKARE_CATEGORY_ID;
        }

        @Override
        protected String getParent() {
            return null;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCategoryTest extends ConfigCategoryTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return CategorySjukhusvard.toCertificate(1, textProvider);
        }

        @Override
        protected String getTextId() {
            return VARD_SJUKHUS_KONTAKT_LAKARE_CATEGORY_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }
}