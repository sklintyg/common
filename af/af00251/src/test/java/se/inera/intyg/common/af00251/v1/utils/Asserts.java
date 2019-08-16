/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00251.v1.utils;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.List;
import java.util.stream.Collectors;
import org.hamcrest.Matcher;
import org.junit.Assert;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

/**
 *
 */
public class Asserts {

    public static void assertValidationMessage(ValidationMessage message, Matcher<String> categoryMatcher,
        Matcher<String> fieldMatcher, Matcher<ValidationMessageType> typeMatcher) {
        assertValidationMessage(message, categoryMatcher, fieldMatcher, typeMatcher, anyOf(nullValue(), any(String.class)));
    }

    public static void assertValidationMessage(ValidationMessage message, Matcher<String> categoryMatcher,
        Matcher<String> fieldMatcher, Matcher<ValidationMessageType> typeMatcher,
        Matcher<String> messageMatcher) {
        assertValidationMessage(message, categoryMatcher, fieldMatcher, typeMatcher, messageMatcher, anyOf(nullValue(), any(String.class)));
    }

    public static void assertValidationMessage(ValidationMessage message, Matcher<String> categoryMatcher,
        Matcher<String> fieldMatcher, Matcher<ValidationMessageType> typeMatcher,
        Matcher<String> messageMatcher, Matcher<String> dynamicKeyMatcher) {
        Assert.assertThat("Invalid ValidationMessage, Category", message.getCategory(), categoryMatcher);
        Assert.assertThat("Invalid ValidationMessage, Field", message.getField(), fieldMatcher);
        Assert.assertThat("Invalid ValidationMessage, Validation Message Type", message.getType(), typeMatcher);
        Assert.assertThat("Invalid ValidationMessage, Validation Message", message.getMessage(), messageMatcher);
        Assert.assertThat("Invalid ValidationMessage, Validation Dynamic Key", message.getDynamicKey(),
            dynamicKeyMatcher);
    }

    public static void assertValidationMessages(List<ValidationMessage> messages, int expectedCount) {

        assertThat(toString(messages), messages, hasSize(expectedCount));

    }

    public static String toString(List<ValidationMessage> messages) {

        return messages.stream().map(ValidationMessage::toString).collect(Collectors.joining("\n"));
    }

}
