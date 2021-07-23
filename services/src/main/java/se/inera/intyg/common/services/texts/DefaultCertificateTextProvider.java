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

import se.inera.intyg.common.services.texts.model.IntygTexts;

public final class DefaultCertificateTextProvider implements CertificateTextProvider {

    private final IntygTexts intygTexts;

    private DefaultCertificateTextProvider(IntygTexts intygTexts) {
        this.intygTexts = intygTexts;
    }

    public static CertificateTextProvider create(IntygTexts intygTexts) {
        return new DefaultCertificateTextProvider(intygTexts);
    }

    @Override
    public String get(String key) {
        final var value = intygTexts.getTexter().get(key);
        if (value == null) {
            return getQuestionHeader(key);
        }
        return value;
    }

    private String getQuestionHeader(String key) {
        final var value = intygTexts.getTexter().get(questionHeader(key));
        if (value == null) {
            return getQuestionSubHeader(key);
        }
        return value;
    }

    private String getQuestionSubHeader(String key) {
        final var value = intygTexts.getTexter().get(questionSubHeader(key));
        if (value == null) {
            return key;
        }
        return value;
    }

    private String questionHeader(String key) {
        return String.format("FRG_%s.RBK", key);
    }

    private String questionSubHeader(String key) {
        return String.format("DFR_%s.RBK", key);
    }
}
