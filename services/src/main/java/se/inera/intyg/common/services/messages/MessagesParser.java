/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.services.messages;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MessagesParser {

    private static final Logger LOG = LoggerFactory.getLogger(MessagesParser.class);
    private static final Pattern START_POSITION_PATTERN = Pattern.compile("^\\s*[\\'\\\"]sv[\\'\\\"]\\s?:\\s?\\{");
    private static final Pattern END_POSITION_PATTERN = Pattern.compile("^\\s*\\},");
    private static final String STRING_CONCAT_REGEXP = "[\\'\\\"]\\s*\\+\\s*[\\'\\\"]";

    private final List<Map<String, String>> list;

    private MessagesParser() {
        list = new ArrayList<>();
    }

    public static MessagesParser create() {
        return new MessagesParser();
    }

    public MessagesParser parse(InputStream inputStream) {
        list.add(toMap(inputStream));
        return this;
    }

    public Map<String, String> collect() {
        Map<String, String> combinedMap = new HashMap<>();
        try {
            for (final var map : list) {
                combinedMap = Stream.concat(combinedMap.entrySet().stream(), map.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            return combinedMap;
        } catch (IllegalStateException exception) {
            LOG.error("Maps contains same keys. This need to be resolved.");
            throw new IllegalStateException("Maps contains same keys. This need to be resolved.", exception);
        }
    }

    private Map<String, String> toMap(InputStream inputStream) {
        String json = parseAsJson(inputStream);

        json = removeStringConcatinations(json);

        return jsonToMap(json);
    }

    private String parseAsJson(InputStream inputStream) {
        final var json = new StringBuilder();
        boolean startPositionFound = false;
        boolean endPositionFound = false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (findEndPosition(line)) {
                    endPositionFound = true;
                    json.append("}");
                    break;
                }
                if (startPositionFound) {
                    json.append(line).append("\n");
                }
                if (findStartPosition(line)) {
                    startPositionFound = true;
                    json.append("{\n");
                }
            }
        } catch (IOException exception) {
            LOG.error("Could not parse message file as json.");
            throw new RuntimeException("Could not parse message file as json.", exception);
        }

        validateParsedResult(startPositionFound, endPositionFound);

        return json.toString();
    }

    private void validateParsedResult(boolean startPositionFound, boolean endPositionFound) {
        if (!startPositionFound || !endPositionFound) {
            final var message = String.format("No start or end position found. Start position found: %s. End position found: %s",
                startPositionFound, endPositionFound);
            LOG.error(message);
            throw new RuntimeException(message);
        }
    }

    private boolean findStartPosition(String line) {
        return START_POSITION_PATTERN.matcher(line).matches();
    }

    private boolean findEndPosition(String line) {
        return END_POSITION_PATTERN.matcher(line).matches();
    }

    private String removeStringConcatinations(String json) {
        return json.replaceAll(STRING_CONCAT_REGEXP, "");
    }

    private Map<String, String> jsonToMap(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {
            };
            return mapper.readValue(json, typeRef);
        } catch (JsonProcessingException exception) {
            LOG.error("Could not convert json to map");
            throw new RuntimeException("Could not convert json to map", exception);
        }
    }

}
