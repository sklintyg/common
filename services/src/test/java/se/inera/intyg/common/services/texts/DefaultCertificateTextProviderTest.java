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
package se.inera.intyg.common.services.texts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.services.texts.model.IntygTexts;

class DefaultCertificateTextProviderTest {

    private CertificateTextProvider defaultCertificateTextProvider;
    private final String KEY_WITH_DASH_AS_LIST = "key_with_dash_as_list";
    private final String KEY_WITH_DASH_AS_TEXT = "key_with_dash_as_text";
    private final String KEY_WITH_DOT = "key_with_dot";
    private final String KEY_WITHOUT_SYMBOLS = "key";
    private final String VALUE_WITHOUT_SYMBOLS = "This is a text without symbols";
    private final String VALUE_WITH_DASH_AS_LIST = "\n\nThis is \n     - a text with \n- symbols";
    private final String VALUE_WITH_DASH_AS_TEXT = "This is- a text with- symbols";
    private final String VALUE_WITH_DOT = "\nThis is • a text with • symbols";
    private final String VALUE_WITH_LIST_TAGS = "This is <ul><li> a text with </li><li> symbols</li></ul>";
    private final String VALUE_STARTING_WITH_DASH_AS_LIST = "- This is \n - My list\n\nWith an extra paragraph";
    private final String KEY_WITH_LIST_TAGS_IN_FRONT = "key_front";
    private final String VALUE_WITH_LIST_TAGS_IN_FRONT = "<ul><li> This is </li><li> My list</li></ul>With an extra paragraph";
    private final String VALUE_WITH_SEVERAL_NEW_LINES = "  Testing\n\n\n\nTesting";
    private final String EXPECTED_VALUE_WITH_SEVERAL_NEW_LINES = "Testing\n\nTesting";
    private final String KEY_SEVERAL_NEW_LINES = "key_lines";
    private final String VALUE_WITH_TAB = "This\tis\tmy\ttext";
    private final String VALUE_WITHOUT_TAB = "Thisismytext";
    private final String KEY_TAB = "key_tab";
    private final String VALUE_WITH_EXTRA_SPACE = "This       is      extra   spacing";
    private final String VALUE_WITHOUT_EXTRA_SPACE = "This is extra spacing";
    private final String KEY_SPACING = "key_spacing";
    private final String HEADER_TEXT = "Header \n Text";
    private final String HEADER_KEY = "key.RBK";


    @BeforeEach
    void setUp() {
        final SortedMap<String, String> texts = new TreeMap<>();
        texts.put("FRG_6.RBK", "Detta är fråga nr 6!");
        texts.put("DFR_6.1.RBK", "Detta är delfråga nr 6.1!");
        texts.put(KEY_WITHOUT_SYMBOLS, VALUE_WITHOUT_SYMBOLS);
        texts.put(KEY_WITH_DASH_AS_LIST, VALUE_WITH_DASH_AS_LIST);
        texts.put(KEY_WITH_DASH_AS_TEXT, VALUE_WITH_DASH_AS_TEXT);
        texts.put(KEY_WITH_DOT, VALUE_WITH_DOT);
        texts.put(KEY_WITH_LIST_TAGS_IN_FRONT, VALUE_STARTING_WITH_DASH_AS_LIST);
        texts.put(KEY_SEVERAL_NEW_LINES, VALUE_WITH_SEVERAL_NEW_LINES);
        texts.put(KEY_TAB, VALUE_WITH_TAB);
        texts.put(KEY_SPACING, VALUE_WITH_EXTRA_SPACE);
        texts.put(HEADER_KEY, HEADER_TEXT);

        final var intygTexts = new IntygTexts(
            "1.0",
            "lisjp",
            LocalDate.now().minus(1L, ChronoUnit.DAYS),
            LocalDate.now().plus(1L, ChronoUnit.DAYS),
            texts,
            Collections.emptyList(),
            null
        );

        defaultCertificateTextProvider = DefaultCertificateTextProvider.create(intygTexts);
    }

    @Test
    void shallReturnTextForKey() {
        final var actualText = defaultCertificateTextProvider.get("FRG_6.RBK");
        assertEquals("Detta är fråga nr 6!", actualText);
    }

    @Test
    void shallReturnTextForQuestionHeaderIfKeyMissing() {
        final var actualText = defaultCertificateTextProvider.get("6");
        assertEquals("Detta är fråga nr 6!", actualText);
    }

    @Test
    void shallReturnTextForQuestionSubHeaderIfKeyMissing() {
        final var actualText = defaultCertificateTextProvider.get("6.1");
        assertEquals("Detta är delfråga nr 6.1!", actualText);
    }

    @Test
    void shallReturnKeyIfKeyAndQuestionHeaderMissing() {
        final var actualText = defaultCertificateTextProvider.get("FRG_99.RBK");
        assertEquals("FRG_99.RBK", actualText);
    }

    @Test
    void shallReturnOriginalValueIfTextHasNoSymbolsToParse() {
        final var actualText = defaultCertificateTextProvider.get(KEY_WITHOUT_SYMBOLS);
        assertEquals(VALUE_WITHOUT_SYMBOLS, actualText);

    }

    @Test
    void shallRemoveDashAndAddListItems() {
        final var actualText = defaultCertificateTextProvider.get(KEY_WITH_DASH_AS_LIST);
        assertEquals(VALUE_WITH_LIST_TAGS, actualText);
    }

    @Test
    void shallNotRemoveDashWhenInsideText() {
        final var actualText = defaultCertificateTextProvider.get(KEY_WITH_DASH_AS_TEXT);
        assertEquals(VALUE_WITH_DASH_AS_TEXT, actualText);

    }

    @Test
    void shallRemoveDotAndAddListItems() {
        final var actualText = defaultCertificateTextProvider.get(KEY_WITH_DOT);
        assertEquals(VALUE_WITH_LIST_TAGS, actualText);
    }

    @Test
    void shallRemoveDashAsFirstCharAndAddListItems() {
        final var actualText = defaultCertificateTextProvider.get(KEY_WITH_LIST_TAGS_IN_FRONT);
        assertEquals(VALUE_WITH_LIST_TAGS_IN_FRONT, actualText);
    }

    @Test
    void shallRemoveSeveralNewLinesAndAddOneNewLine() {
        final var actualText = defaultCertificateTextProvider.get(KEY_SEVERAL_NEW_LINES);
        assertEquals(EXPECTED_VALUE_WITH_SEVERAL_NEW_LINES, actualText);
    }

    @Test
    void shallRemoveTab() {
        final var actualText = defaultCertificateTextProvider.get(KEY_TAB);
        assertEquals(VALUE_WITHOUT_TAB, actualText);
    }

    @Test
    void shallRemoveExtraSpacing() {
        final var actualText = defaultCertificateTextProvider.get(KEY_SPACING);
        assertEquals(VALUE_WITHOUT_EXTRA_SPACE, actualText);
    }

    @Test
    void shallNotParseExtraSpacingOfHeaderText() {
        final var actualText = defaultCertificateTextProvider.get(HEADER_KEY);
        assertEquals(HEADER_TEXT, actualText);
    }
}