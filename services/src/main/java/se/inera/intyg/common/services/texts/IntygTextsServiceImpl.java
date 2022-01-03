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
package se.inera.intyg.common.services.texts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

@Component
public class IntygTextsServiceImpl implements IntygTextsService {

    private static final Logger LOG = LoggerFactory.getLogger(IntygTextsService.class);

    @Autowired
    private IntygTextsRepository repo;

    @Autowired
    @Qualifier("customObjectMapper")
    private CustomObjectMapper mapper;

    @Override
    public boolean isVersionSupported(final String intygsTyp, final String stringVersion) {
        final Integer integerVersion = Ints.tryParse(stringVersion);

        final String version = integerVersion != null
            ? integerVersion.toString() + ".0"
            : stringVersion;

        return repo.isVersionSupported(intygsTyp, version);
    }

    @Override
    public String getIntygTexts(String intygsTyp, String version) {
        try {
            return mapper.writeValueAsString(repo.getTexts(intygsTyp, version));
        } catch (JsonProcessingException e) {
            LOG.error("Could not write texts as JSON for certificate of type {} with version {}", intygsTyp, version);
        }
        return "";
    }

    @Override
    public String getLatestVersion(String intygsTyp) {
        return repo.getLatestVersion(intygsTyp);
    }

    @Override
    public String getLatestVersionForSameMajorVersion(String intygsTyp, String version) {
        return repo.getLatestVersionForSameMajorVersion(intygsTyp, version);
    }

    @Override
    public boolean isLatestMajorVersion(String certificateType, String versionToCompare) {
        final var latestVersion = getLatestVersion(certificateType);
        return sameMajorVersion(versionToCompare, latestVersion);
    }

    @Override
    public IntygTexts getIntygTextsPojo(String intygsTyp, String version) {
        return repo.getTexts(intygsTyp, version);
    }

    private String majorVersion(String version) {
        return version.split("\\.")[0];
    }

    private boolean sameMajorVersion(String versionToCompare, String latestVersion) {
        return latestVersion == null || majorVersion(latestVersion).equals(majorVersion(versionToCompare));
    }

}
