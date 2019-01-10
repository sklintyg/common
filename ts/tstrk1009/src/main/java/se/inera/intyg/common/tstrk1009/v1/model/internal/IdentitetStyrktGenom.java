package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum IdentitetStyrktGenom {
    ID_KORT("IDK1", "ID-kort"),
    FORETAG_ELLER_TJANSTEKORT("IDK2", "Företagskort eller tjänstekort"),
    KORKORT("IDK3", "Svenskt körkort"),
    PERS_KANNEDOM("IDK4", "Personlig kännedom"),
    FORSAKRAN_KAP18("IDK5", "Försäkran enligt 18 kap. 4 §"),
    PASS("IDK6", "Pass");

    final String code;
    final String description;

    IdentitetStyrktGenom(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static IdentitetStyrktGenom fromId(@JsonProperty("id") String id) {
        final String normId = id != null ? id.trim() : null;
        for (IdentitetStyrktGenom typ : values()) {
            if (typ.name().equals(normId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException(id);
    }

    public static IdentitetStyrktGenom fromCode(final String code) {
        return Stream.of(IdentitetStyrktGenom.values()).filter(s -> code.equals(s.getCode())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(code));
    }

    @JsonValue
    public String getId() {
        return this.name();
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
