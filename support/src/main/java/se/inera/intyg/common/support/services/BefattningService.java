/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

import java.io.*;
import java.util.*;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class BefattningService {

    private static final Logger LOG = LoggerFactory.getLogger(BefattningService.class);
    private static final String CSV_SEPARATOR = ";";

    private static BefattningService instance;

    private Map<String, String> codeToDescription;
    private Map<String, String> descriptionToCode;

    @PostConstruct
    public void init() {
        instance = this;
        codeToDescription = new HashMap<>();
        descriptionToCode = new HashMap<>();

        String fileUrl = "codes/befattningskoder.csv";

        LOG.debug("Loading file '{}'", fileUrl);

        BufferedReader br = null;

        try {
            Resource resource = new DefaultResourceLoader().getResource(fileUrl);

            if (!resource.exists()) {
                LOG.error("Could not load file since the resource '{}' does not exists", fileUrl);
                return;
            }

            br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));
            String line = br.readLine(); // skip headers
            int i = 1;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(CSV_SEPARATOR);

                if (values.length < 2) {
                    LOG.error("Invalid row {}", i);
                } else if (codeToDescription.containsKey(values[0])) {
                    LOG.error("Ignoring code " + values[0] + " with description " + values[1] + " since code already exists in file.");
                } else if (descriptionToCode.containsKey(values[1])) {
                    LOG.error("Ignoring code " + values[0] + " with description " + values[1] + " since description already exists in file.");
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
        return instance != null && StringUtils.isNotBlank(code) ? Optional.ofNullable(instance.codeToDescription.get(code.trim())) : Optional.empty();
    }

    public static Optional<String> getCodeFromDescription(String description) {
        return instance != null && StringUtils.isNotBlank(description) ? Optional.ofNullable(instance.descriptionToCode.get(description.trim())) : Optional.empty();
    }
}
