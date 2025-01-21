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
package se.inera.intyg.common.support.services;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.Closeables;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.csv.CSVFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class BefattningService {

    private static final Logger LOG = LoggerFactory.getLogger(BefattningService.class);
    private static final char CSV_DELIMITER = ';';

    private ImmutableBiMap<String, String> codeMap = ImmutableBiMap.of();

    // FIXME: A very dirty construction to be backward compatible and yet be able to externalize the configuration.
    // This class has to be initialized as a spring component in order to function properly.
    static BefattningService instance = null;

    @Value("${befattningskoder.file:classpath:codes/befattningskoder-default.csv}")
    Resource resource;

    @PostConstruct
    @SuppressFBWarnings
    void initialize() throws IOException {
        final Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        try {
            final ImmutableBiMap.Builder<String, String> codeBuilder = ImmutableBiMap.builder();

            CSVFormat.DEFAULT.withDelimiter(CSV_DELIMITER).parse(reader).forEach(r -> {
                codeBuilder.put(r.get(0), r.get(1));
            });

            this.codeMap = codeBuilder.build();

            LOG.info("{} codes loaded from {}", this.codeMap.size() - 1, this.resource);

            instance = this;
        } finally {
            Closeables.closeQuietly(reader);
        }
    }

    public Map<String, String> codeMap() {
        return this.codeMap;
    }

    private Optional<String> lookup(final String key, final boolean inverse) {
        if (Strings.isNullOrEmpty(key)) {
            return Optional.empty();
        }
        final Map<String, String> map = inverse ? this.codeMap.inverse() : this.codeMap;
        return Optional.ofNullable(map.get(key.trim()));
    }

    // for legacy compatibility only
    static BefattningService instance() {
        if (Objects.isNull(instance)) {
            throw new IllegalStateException("Not properly initialized");
        }
        return instance;
    }

    // static due to legacy compatibility
    public static Optional<String> getDescriptionFromCode(String code) {
        return instance().lookup(code, false);
    }

    // static due to legacy compatibility
    public static Optional<String> getCodeFromDescription(String description) {
        return instance().lookup(description, true);
    }
}
