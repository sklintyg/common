package se.inera.intyg.common.support.modules.support.api.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class PersonnummerDeserializer extends JsonDeserializer<Personnummer> {

    @Override
    public Personnummer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return new Personnummer(jsonParser.getValueAsString());
    }

    @Override
    public Personnummer getNullValue() {
        return new Personnummer(null);
    }

    @Override
    public Personnummer getEmptyValue() {
        return new Personnummer("");
    }

}
