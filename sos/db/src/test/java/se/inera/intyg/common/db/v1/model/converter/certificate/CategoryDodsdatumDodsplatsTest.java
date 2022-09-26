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

package se.inera.intyg.common.db.v1.model.converter.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DODSPLATS_CATEGORY_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.services.texts.CertificateTextProvider;

class CategoryDodsdatumDodsplatsTest {

    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Test
    void shouldIncludeId() {
        final var category = CategoryDodsdatumDodsplats.toCertificate(0, texts);
        assertEquals(DODSDATUM_DODSPLATS_CATEGORY_ID, category.getId());
    }

    @Test
    void shouldIncludeIndex() {
        final var expectedIndex = 3;
        final var category = CategoryDodsdatumDodsplats.toCertificate(expectedIndex, texts);
        assertEquals(expectedIndex, category.getIndex());
    }

    @Test
    void shouldIncludeCategoryConfig() {
        final var category = CategoryDodsdatumDodsplats.toCertificate(0, texts);
        assertTrue(category.getConfig().getText().trim().length() > 0, "Missing text");
    }
}