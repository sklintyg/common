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
package se.inera.intyg.common.pdf.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HtmlParserTest {

    @Test
    public void testParserTextContainingHtmlRemoveAllPTags() {

        final var text = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "    <head>\n"
            + "        <!-- head definitions go here -->\n"
            + "    </head>\n"
            + "    <body>\n"
            + "        <!-- the content goes here -->\n"
            + "       <p>Hello World!</p>\n"
            + "       <p>Hello World!</p>\n"
            + "    </body>\n"
            + "</html>";

        final var result = HtmlParser.toTextExcludeElement(text, "p");

        Assertions.assertEquals("", result);
    }

    @Test
    public void testParserTextContainingHtmlNoPTag() {

        final var text = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "    <head>\n"
            + "        <!-- head definitions go here -->\n"
            + "    </head>\n"
            + "    <body>\n"
            + "        <!-- the content goes here -->\n"
            + "        Hello World!\n"
            + "    </body>\n"
            + "</html>";

        final var result = HtmlParser.toTextExcludeElement(text, "p");

        Assertions.assertEquals("Hello World!", result);
    }

    @Test
    public void testParserTextContainingNoHtml() {

        final var text = "Just a regular string";

        final var result = HtmlParser.toTextExcludeElement(text, "p");

        Assertions.assertEquals(text, result);
    }

    @Test
    public void testParserTextContainingNotCompleteHtmlRemoveAllPTags() {

        final var text = "<p>Hello World!</p>Hello World!";

        final var result = HtmlParser.toTextExcludeElement(text, "p");

        Assertions.assertEquals("Hello World!", result);
    }

}
