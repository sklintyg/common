package se.inera.intyg.common.lisjp.pdf;

import java.util.List;

public enum LisjpEmployeeOptionalFields {
    PLACEHOLDER;

    public boolean isPresent(List<String> values) {
        return values.stream().filter(s -> toString().equals(s)).findAny().isPresent();
    }
}
