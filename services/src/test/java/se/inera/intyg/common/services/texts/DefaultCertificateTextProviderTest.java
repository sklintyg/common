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

package se.inera.intyg.common.services.texts;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setUp() {
        final SortedMap<String, String> texts = new TreeMap<>();
        texts.put("FRG_6.RBK", "Detta är fråga nr 6!");
        texts.put("DFR_6.1.RBK", "Detta är delfråga nr 6.1!");

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
}