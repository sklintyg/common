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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.DEMENS_KOGNITIV_FUNKTION_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.DEMENS_KOGNITIV_FUNKTION_CATEGORY_TEXT_ID;

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
class CategoryDemensOchAndraKognitivaStorningarTest {

    @Mock
    CertificateTextProvider certificateTextProvider;

    @BeforeEach
    void setUp() {
        when(certificateTextProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return CategoryDemensOchAndraKognitivaStorningar.toCertificate(
                0, certificateTextProvider);
        }

        @Override
        protected String getId() {
            return DEMENS_KOGNITIV_FUNKTION_CATEGORY_ID;
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
    class IncludeConfigCategoryTests extends ConfigCategoryTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return certificateTextProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return CategoryDemensOchAndraKognitivaStorningar.toCertificate(0, certificateTextProvider);
        }

        @Override
        protected String getTextId() {
            return DEMENS_KOGNITIV_FUNKTION_CATEGORY_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }
}
