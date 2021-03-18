package se.inera.intyg.common.support.modules.support.facade.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

public class CertificateDataValueDeserializer extends JsonDeserializer<CertificateDataValueDTO> {

    @Override
    public CertificateDataValueDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        final ObjectNode root = mapper.readTree(p);
        final JsonNode field = root.get(CertificateDataValueDTO.TYPE_FIELD);
        final CertificateDataValueTypeDTO type = CertificateDataValueTypeDTO.valueOf(field.asText());
        switch (type) {
            case BOOLEAN:
                final var booleanValue = new CertificateDataBooleanValueDTO();
                if (root.hasNonNull("unselectedText")) {
                    booleanValue.setUnselectedText(root.get("unselectedText").asText());
                }
                if (root.hasNonNull("selectedText")) {
                    booleanValue.setSelectedText(root.get("selectedText").asText());
                }
                if (root.hasNonNull("selected")) {
                    booleanValue.setSelected(root.get("selected").asBoolean());
                }
                return booleanValue;
            case TEXT:
                final var textValue = new CertificateDataTextValueDTO();
                if (root.hasNonNull("text")) {
                    textValue.setText(root.get("text").asText());
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