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
package se.inera.intyg.common.support.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.google.common.base.Strings;

public final class BefattningService {

    private static final Logger LOG = LoggerFactory.getLogger(BefattningService.class);
    private static final String CSV_SEPARATOR = ";";

    private static Map<String, String> codeToDescription;
    private static Map<String, String> descriptionToCode;

    private BefattningService() {
    }

    static {
        codeToDescription = new HashMap<>();
        descriptionToCode = new HashMap<>();

        String fileUrl = "codes/befattningskoder.csv";

        LOG.debug("Loading file '{}'", fileUrl);

        BufferedReader br = null;

        try {
            Resource resource = new DefaultResourceLoader().getResource(fileUrl);

            if (!resource.exists()) {
                String message = "Could not load file since the resource '" + fileUrl + "' does not exists";
                LOG.error(message);
                throw new RuntimeException(message);
            }

            br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));
            br.readLine(); // skip headers
            int i = 1;

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(CSV_SEPARATOR);

                if (values.length < 2) {
                    LOG.error("Invalid row {}", i);
                } else if (codeToDescription.containsKey(values[0])) {
                    LOG.error("Ignoring code " + values[0] + " with description " + values[1] + " since code already exists in file.");
                } else if (descriptionToCode.containsKey(values[1])) {
                    LOG.error("Ignoring code " + values[0] + " with description "
                            + values[1] + " since description already exists in file.");
                } else {
                    codeToDescription.put(values[0], values[1]);
                    descriptionToCode.put(values[1], values[0]);
                }
                i++;
            }
            LOG.debug("Initialized {} codes with descriptions", codeToDescription.keySet().size());
        } catch (IOException ioe) {
            LOG.error("IOException occured when loading file '{}'", fileUrl);
            throw new RuntimeException("Error occured when loading diagnosis file", ioe);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOG.error("Error closing buffered reader {}", e);
                }
            }
        }
    }

    public static Optional<String> getDescriptionFromCode(String code) {
        return !Strings.nullToEmpty(code).trim().isEmpty()
                ? Optional.ofNullable(codeToDescription.get(code.trim()))
                : Optional.empty();
    }

    public static Optional<String> getCodeFromDescription(String description) {
        return !Strings.nullToEmpty(description).trim().isEmpty()
                ? Optional.ofNullable(descriptionToCode.get(description.trim()))
                : Optional.empty();
    }
}
