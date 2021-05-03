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

package se.inera.intyg.common.support.facade.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

public class CertificateDataConfigDeserializer extends JsonDeserializer<CertificateDataConfig> {

    @Override
    public CertificateDataConfig deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        final ObjectNode root = mapper.readTree(p);
        final JsonNode field = root.get(CertificateDataConfig.TYPE_FIELD);
        final var type = CertificateDataConfigTypes.valueOf(field.asText());
        switch (type) {
            case CATEGORY:
                final var categoryConfig = new CertificateDataConfigCategory();
                categoryConfig.setType(type);
                return categoryConfig;
            case UE_RADIO_BOOLEAN:
                final var booleanConfig = new CertificateDataConfigBoolean();
                booleanConfig.setType(type);
                if (root.hasNonNull("id")) {
                    booleanConfig.setId(root.get("id").asText());
                }
                if (root.hasNonNull("selectedText")) {
                    booleanConfig.setSelectedText(root.get("selectedText").asText());
                }
                if (root.hasNonNull("unselectedText")) {
                    booleanConfig.setUnselectedText(root.get("unselectedText").asText());
                }
                return booleanConfig;
            case UE_TEXTAREA:
                final var textAreaConfig = new CertificateDataConfigTextArea();
                textAreaConfig.setType(type);
                if (root.hasNonNull("id")) {
                    textAreaConfig.setId(root.get("id").asText());
                }
                return textAreaConfig;
            default:
                return null;
        }
    }
}