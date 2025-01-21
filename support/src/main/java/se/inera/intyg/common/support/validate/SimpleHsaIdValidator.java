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
package se.inera.intyg.common.support.validate;

import static se.inera.intyg.common.support.Constants.HSA_ID_OID;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs a lesser strict validation of a HSA id. The following rules are checked:
 * <ul>
 * <li>The id starts with 2-4 uppercase letters. like SE or IFV.
 * <li>The organisationsnummer is either 10 digits or 12 digits and starting with '16'
 * <li>The local id ('nnnn') only contains characters from the PRINTABLE_STRING class
 * <li>The total length of the HSA id doesn't exceed 31 characters.
 * </ul>
 */
public class SimpleHsaIdValidator implements RootValidator {

    /**
     * Regex pattern HSA-ids should conform to.
     */
    private static final Pattern HSA_VALID_PATTERN = Pattern.compile("[A-Z]{2,4}(?:16)?([0-9]{10})\\-(.*)");

    /**
     * Regex validating that a local id only got characters from the PRINTABLE_STRING class.
     */
    private static final Pattern LOCAL_ID_VALID_PATTERN = Pattern
        .compile("[0-9A-Za-z \\'\\(\\)\\+\\,\\-\\.\\/\\:\\=\\?]*");
    public static final int MAX_EXTENSION_LENGTH = 31;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoot() {
        return HSA_ID_OID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> validateExtension(String extension) {
        List<String> result = new ArrayList<>();

        Matcher m = HSA_VALID_PATTERN.matcher(extension);

        if (!m.matches()) {
            result.add(String.format("HSA id '%s' doesn't match pattern [16]xxxxxxxxxx-n*", extension));
            return result;

        } else {
            String localId = m.group(2);
            checkLocalId(localId, result);
            checkHSALength(extension, result);
        }

        return result;
    }

    /**
     * Check that the total length of the HSA id doesn't exceed 31 characters.
     *
     * @param extension The id to check.
     * @param result List that validation messages are added to.
     */
    private void checkHSALength(String extension, List<String> result) {
        if (extension.length() > MAX_EXTENSION_LENGTH) {
            result.add(String.format("HSA id '%s' exceeded the maximum length of 31 charcters", extension));
        }
    }

    /**
     * Check that the local id only contain accepted characters.
     *
     * @param localId The local id to check.
     * @param result List that validation messages are added to.
     */
    private void checkLocalId(String localId, List<String> result) {
        if (!LOCAL_ID_VALID_PATTERN.matcher(localId).matches()) {
            result.add(String.format("Non valid character found in local id '%s'", localId));
        }
    }
}
