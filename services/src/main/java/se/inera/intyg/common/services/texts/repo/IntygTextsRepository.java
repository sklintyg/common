/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.services.texts.repo;

import se.inera.intyg.common.services.texts.model.IntygTexts;

/**
 * Repository containing the texts used in the certificates.
 */
public interface IntygTextsRepository {

    /**
     * Lookup if the version and type of intyg is supported.
     *
     * @param intygsTyp
     *              the type of intyg
     * @param version
     *              the version of the intyg, described in [major.minor]
     * @return boolean if the specific intyg is supported
     */
    boolean isVersionSupported(String intygsTyp, String version);

    /**
     * Returns the latest version for intyg of type <code>intygsType</code>.
     *
     * @param intygsTyp
     *            the type of intyg
     * @return the latest version
     */
    String getLatestVersion(String intygsTyp);

    /**
     * Returns the latest version for intyg of type <code>intygsType</code> having the same majorVersion as
     * expressed by <code>version</code>.
     *
     * @param intygsTyp
     *            the type of intyg
     * @return the latest version
     */
    String getLatestVersionForSameMajorVersion(String intygsTyp, String version);

    /**
     * Returns a map of format Key -> Text for the intyg of specified type and version.
     *
     * @param intygsTyp
     *            the type
     * @param version
     *            the version
     * @return the mapping of Key -> Text
     */
    IntygTexts getTexts(String intygsTyp, String version);

}
