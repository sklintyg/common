/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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

class HtmlParserTest {

  @Test
  void testParserTextContainingHtmlRemoveAllPTags() {

    final var text =
        """
        <!DOCTYPE html>
        <html>
            <head>
                <!-- head definitions go here -->
            </head>
            <body>
                <!-- the content goes here -->
               <p>Hello World!</p>
               <p>Hello World!</p>
            </body>
        </html>""";

    final var result = HtmlParser.toTextExcludeElement(text, "p");

    Assertions.assertEquals("", result);
  }

  @Test
  void testParserTextContainingHtmlNoPTag() {

    final var text =
        """
        <!DOCTYPE html>
        <html>
            <head>
                <!-- head definitions go here -->
            </head>
            <body>
                <!-- the content goes here -->
                Hello World!
            </body>
        </html>""";

    final var result = HtmlParser.toTextExcludeElement(text, "p");

    Assertions.assertEquals("Hello World!", result);
  }

  @Test
  void testParserTextContainingNoHtml() {

    final var text = "Just a regular string";

    final var result = HtmlParser.toTextExcludeElement(text, "p");

    Assertions.assertEquals(text, result);
  }

  @Test
  void testParserTextContainingNotCompleteHtmlRemoveAllPTags() {

    final var text = "<p>Hello World!</p>Hello World!";

    final var result = HtmlParser.toTextExcludeElement(text, "p");

    Assertions.assertEquals("Hello World!", result);
  }
}
