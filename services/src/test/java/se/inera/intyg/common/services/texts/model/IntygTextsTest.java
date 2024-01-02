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
package se.inera.intyg.common.services.texts.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntygTextsTest {

    @Test
    public void testValidVersionNumber() {
        IntygTexts test1 = new IntygTexts("1.0", null, null, null, null, null, null);
        Assertions.assertNotNull(test1);
        Assertions.assertEquals(test1.getVersion(), "1.0", "Version should be set to what is created");

        IntygTexts test2 = new IntygTexts("0", null, null, null, null, null, null);
        Assertions.assertNotNull(test2);
        Assertions.assertEquals(test2.getVersion(), "0", "Version should be set to what is created");
    }

    @Test
    public void testInvalidVersionNumber() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new IntygTexts("1.x", null, null, null, null, null, null);
        });
    }

    @Test
    public void testInvalidVersionNumberNull() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new IntygTexts(null, null, null, null, null, null, null);
        });
    }

    @Test
    public void testDeepCopy() {
        SortedMap<String, String> texts = Maps.newTreeMap();
        texts.put("text1", "value1");
        texts.put("text2", "value2");

        List<Tillaggsfraga> additionalQuestions = new ArrayList<>();
        additionalQuestions.add(new Tillaggsfraga("id1"));
        additionalQuestions.add(new Tillaggsfraga("id2"));

        final var properties = new Properties();
        properties.putAll(ImmutableMap.of("formId", "test", "blankettId", "test", "blankettVersion", "01"));

        final var original = new IntygTexts("1.0", "ts-diabetes", LocalDate.now(), LocalDate.now(), texts,
            additionalQuestions, properties);

        final var deepCopy = original.deepCopy();

        Assertions.assertNotSame(original, deepCopy);

        Assertions.assertNotSame(original.getTexter(), deepCopy.getTexter());
        Assertions.assertNotSame(original.getTillaggsfragor(), deepCopy.getTillaggsfragor());
        Assertions.assertNotSame(original.getProperties(), deepCopy.getProperties());

        Assertions.assertEquals(original.getVersion(), deepCopy.getVersion());
        Assertions.assertEquals(original.getIntygsTyp(), deepCopy.getIntygsTyp());
        Assertions.assertEquals(original.getValidFrom(), deepCopy.getValidFrom());
        Assertions.assertEquals(original.getValidTo(), deepCopy.getValidTo());
        Assertions.assertEquals(original.getTexter(), deepCopy.getTexter());
        Assertions.assertEquals(original.getTillaggsfragor(), deepCopy.getTillaggsfragor());
        Assertions.assertEquals(original.getProperties(), deepCopy.getProperties());
    }

}
