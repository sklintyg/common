/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.facade.builder.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

abstract class AbstractCertificateDataConfigBuilderTest {

    abstract protected AbstractCertificateDataConfigBuilder getBuilder();

    @Test
    public void shallIncludeHeaderInConfig() {
        final var expectedHeader = "HeaderText";

        final var actualConfig =
            getBuilder()
                .header(expectedHeader)
                .build();

        assertEquals(expectedHeader, actualConfig.getHeader());
    }

    @Test
    public void shallIncludeIconInConfig() {
        final var expectedIcon = "IconName";

        final var actualConfig =
            getBuilder()
                .icon(expectedIcon)
                .build();

        assertEquals(expectedIcon, actualConfig.getIcon());
    }

    @Test
    public void shallIncludeTextInConfig() {
        final var expectedText = "Text";

        final var actualConfig =
            getBuilder()
                .text(expectedText)
                .build();

        assertEquals(expectedText, actualConfig.getText());
    }

    @Test
    public void shallIncludeDescriptionInConfig() {
        final var expectedDescription = "Description";

        final var actualConfig =
            getBuilder()
                .description(expectedDescription)
                .build();

        assertEquals(expectedDescription, actualConfig.getDescription());
    }
}