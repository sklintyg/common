package se.inera.intyg.common.support.facade.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;

public class CertificateDataValueDeserializer extends JsonDeserializer<CertificateDataValue> {

    @Override
    public CertificateDataValue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        final ObjectNode root = mapper.readTree(p);
        final JsonNode field = root.get(CertificateDataValue.TYPE_FIELD);
        final CertificateDataValueType type = CertificateDataValueType.valueOf(field.asText());
        switch (type) {
            case BOOLEAN:
                final var booleanValue = new CertificateDataValueBoolean();
                booleanValue.setType(type);
                if (root.hasNonNull("selected")) {
                    booleanValue.setSelected(root.get("selected").asBoolean());
                }
                if (root.hasNonNull("id")) {
                    booleanValue.setId(root.get("id").asText());
                }
                return booleanValue;
            case TEXT:
                final var textValue = new CertificateDataTextValue();
                textValue.setType(type);
                if (root.hasNonNull("text")) {
                    textValue.setText(root.get("text").asText());
                }
                if (root.hasNonNull("id")) {
                    textValue.setId(root.get("id").asText());
                }
                return textValue;
            case UNKOWN:
            default:
                return null;
        }
    }
}
/**
 * @Override public CertificateDataValueDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 * ObjectMapper mapper = (ObjectMapper) p.getCodec();
 * final ObjectNode root = mapper.readTree(p);
 * final JsonNode field = root.get(CertificateDataValueDTO.TYPE_FIELD);
 * final CertificateDataValueTypeDTO type = CertificateDataValueTypeDTO.valueOf(field.asText());
 * Class<? extends CertificateDataValueDTO> instanceClass;
 * switch (type) {
 * case BOOLEAN:
 * instanceClass = CertificateDataBooleanValueDTO.class;
 * break;
 * case TEXT:
 * instanceClass = CertificateDataTextValueDTO.class;
 * break;
 * case UNKOWN:
 * default:
 * return null;
 * }
 *
 * return mapper.readValue(p, instanceClass);
 * }
 */