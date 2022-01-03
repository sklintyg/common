/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.transformer;

import java.util.regex.Pattern;

/**
 * @author Magnus Ekstrand on 2018-09-21.
 */
public final class XslTransformerUtil {

    private XslTransformerUtil() {
        // It's a utility class, make constructor private
    }

    private static final Pattern REGISTER_TSBAS_REGEX = Pattern.compile(
            "=[\"|']urn:local:se:intygstjanster:services:RegisterTSBasResponder:1[\"|']");

    private static final Pattern REGISTER_CERTIFICATE_V1_REGEX = Pattern.compile(
            "=[\"|']urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1[\"|']");

    private static final Pattern REGISTER_CERTIFICATE_V3_REGEX = Pattern.compile(
            "=[\"|']urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3[\"|']");

    public static boolean isRegisterTsBas(String xml) {
        return find(xml, REGISTER_TSBAS_REGEX);
    }

    public static boolean isRegisterCertificateV1(String xml) {
        return find(xml, REGISTER_CERTIFICATE_V1_REGEX);
    }

    public static boolean isRegisterCertificateV3(String xml) {
        return find(xml, REGISTER_CERTIFICATE_V3_REGEX);
    }

    private static boolean find(final String xml, final Pattern pattern) {
        return pattern.matcher(xml).find();
    }
}
