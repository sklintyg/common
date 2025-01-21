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

package se.inera.intyg.common.fk7263.model.converter.certificate.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_CATEGORY_TEXT_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCategoryWithMessageProviderTest;

@ExtendWith(MockitoExtension.class)
class CategoryPatientensArbetsformagaBedomsTest {

    @Mock
    CertificateMessagesProvider messagesProvider;

    @BeforeEach
    void setUp() {
        when(messagesProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return CategoryPatientensArbetsformagaBedoms.toCertificate(0, messagesProvider);
        }

        @Override
        protected String getId() {
            return PATIENTENS_ARBETSFORMAGA_CATEGORY_ID;
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
    class IncludeConfigCatagoryTests extends ConfigCategoryWithMessageProviderTest {

        @Override
        protected CertificateMessagesProvider getMessageProviderMock() {
            return messagesProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return CategoryPatientensArbetsformagaBedoms.toCertificate(0, messagesProvider);
        }

        @Override
        protected String getTextId() {
            return PATIENTENS_ARBETSFORMAGA_CATEGORY_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }
}
