package se.inera.intyg.common.tstrk1009.v1.model.internal;

import java.util.stream.Stream;

public enum AnmalanAvser {
    OLAMPLIGHET("OLAMPLIGHET", "Anmälan om olämplighet"),
    SANNOLIK_OLAMPLIGHET("SANNOLIK_OLAMPLIGHET", "Anmälan om sannolik olämplighet");

    private final String code;
    private final String description;

    AnmalanAvser(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AnmalanAvser fromCode(final String code) {
        return Stream.of(AnmalanAvser.values()).filter(s -> code.equals(s.getCode())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(code));
    }
}
