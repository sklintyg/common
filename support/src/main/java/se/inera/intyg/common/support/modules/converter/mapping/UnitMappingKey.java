package se.inera.intyg.common.support.modules.converter.mapping;

import java.util.Objects;

public record UnitMappingKey(String id) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UnitMappingKey that = (UnitMappingKey) o;
        return Objects.equals(id.toUpperCase(), that.id.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}