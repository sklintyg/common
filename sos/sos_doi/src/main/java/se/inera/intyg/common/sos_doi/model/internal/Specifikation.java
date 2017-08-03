package se.inera.intyg.common.sos_doi.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Specifikation {
    KRONISK("90734009", "kronisk"),
    PLOTSLIG("424124008", "plötslig debut och/eller kort duration");

    private final String id;
    private final String label;

    Specifikation(String id, String label) {
        this.id = id;
        this.label = label;
    }

    @JsonCreator
    public static Specifikation fromId(@JsonProperty("id") String id) {
        String normId = id != null ? id.trim() : null;
        for (Specifikation typ : values()) {
            if (typ.id.equals(normId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

    @JsonValue
    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
