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

import se.inera.intyg.common.services.texts.model.IntygTexts;

/**
 * Service used to access the texts in a certificate.
 */
public interface IntygTextsService {

    /**
     * Lookup if the version and type of intyg is supported.
     *
     * @param intygsTyp the type of intyg
     * @param version the version of the intyg, described in [major.minor]
     * @return boolean if the specific intyg is supported
     */
    boolean isVersionSupported(String intygsTyp, String version);

    /**
     * Returns the texts for a given type and version as JSON.
     *
     * @param intygsTyp the type
     * @param version the version
     * @return the texts as JSON
     */
    String getIntygTexts(String intygsTyp, String version);

    boolean isLatestMajorVersion(String certificateType, String version);

    /**
     * Returns an {@link IntygTexts} representation of the set of texts for the certificate with specified and version.
     *
     * @param intygsTyp the type
     * @param version the version
     * @return the texts as an {@link IntygTexts} object
     */
    IntygTexts getIntygTextsPojo(String intygsTyp, String version);

    /**
     * Returns the latest version for <code>intygsTyp</code>.
     *
     * @param intygsTyp the type
     * @return the latest version
     */
    String getLatestVersion(String intygsTyp);

    /**
     * Returns the latest minor version for <code>intygsTyp</code> and <code>major</code>.
     *
     * @param intygsTyp the type
     * @return the latest version
     */
    String getLatestVersionForSameMajorVersion(String intygsTyp, String version);
}
