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
package se.inera.intyg.common.services.texts;

import se.inera.intyg.common.services.texts.model.IntygTexts;

public final class DefaultCertificateTextProvider implements CertificateTextProvider {

    private final IntygTexts intygTexts;
    private static final String ANGULAR_TRANSPORTSTYRELSEN_LINK =
        "<a href=\"https://www.transportstyrelsen.se\" target=\"_blank\">Vägtrafik -"
            + " transportstyrelsen.se</a>\t<i class=\"material-icons md-18\">launch</i>";
    private static final String REACT_TRANSPORTSTYRELSEN_LINK_KEY =
        "<LINK:transportstyrelsenVagtrafik>";

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

        return parseText(value, key);
    }

    @Override
    public String getOrNull(String key) {
        final var value = get(key);
        return value.equals(key) ? null : value;
    }

    private boolean isHeadline(String key) {
        return key.contains(".RBK");
    }

    private String parseText(String value, String key) {
        StringBuilder stringBuilder = new StringBuilder();
        var parts = value.split("\n\n+");
        var result = "";

        for (int i = 0; i < parts.length; i++) {
            parseList(stringBuilder, parts[i], i == parts.length - 1);
        }

        if (!isHeadline(key) || stringBuilder.toString().contains(ANGULAR_TRANSPORTSTYRELSEN_LINK)) {
            result = fixSpacingOfText(stringBuilder);
        } else {
            result = stringBuilder.toString();
        }
        return result;
    }

    private String fixSpacingOfText(StringBuilder stringBuilder) {
        String result;
        result = stringBuilder.toString().replaceAll("^\n+|^\\s+", "");
        result = result.replaceAll(ANGULAR_TRANSPORTSTYRELSEN_LINK, REACT_TRANSPORTSTYRELSEN_LINK_KEY);
        result = result.replaceAll("\n\n+", "**");
        result = result.replaceAll("\t", "");
        result = result.replaceAll("\\s\\s+", " ");
        result = result.replaceAll("\\*\\*", "\n\n");
        result = result.replaceAll("\n\n\\s", "\n\n");
        result = result.replaceAll("\n\\s+<ul>|\n<ul>", "<ul>");
        result = result.replaceAll(".\\sTill\\sexempel", ".\n\nTill exempel");
        result = result.replaceAll("obehag\nvid", "obehag vid");
        result = result.replaceAll("utgöra\nbåde", "utgöra både");
        result = result.replaceAll("C1E, C och CE. ", "C1E, C och CE.\n");
        return result;
    }

    private void parseList(StringBuilder stringBuilder, String value, boolean isLast) {
        var parts = value.split("(\n+\\s+-)|\n+-|•|^-");
        if (parts.length <= 1) {
            stringBuilder.append(value);
            appendNewLine(stringBuilder, isLast);
            return;
        }
        var count = 0;
        while (count < parts.length) {
            if (count == 0) {
                stringBuilder.append(parts[count++]).append("<ul><li>");
            } else if (count == parts.length - 1) {
                stringBuilder.append(parts[count++]).append("</li></ul>");
            } else {
                stringBuilder.append(parts[count++]).append("</li><li>");
            }
        }
    }

    private void appendNewLine(StringBuilder stringBuilder, boolean isLast) {
        if (!isLast) {
            stringBuilder.append("\n\n");
        }
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
