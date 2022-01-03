/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v4.model.kodverk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum KvTypAvDiabetes {

    TYP1("E10", "Diabetes mellitus typ 1"),
    TYP2("E11", "Diabetes mellitus typ 2"),
    LADA("E109", "Diabetes mellitus typ 1-Utan komplikationer"),
    ANNAN("E13", "Annan typ av diabetes");

    final String code;
    final String description;

    KvTypAvDiabetes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static KvTypAvDiabetes fromId(@JsonProperty("id") String id) {
        String normId = id != null ? id.trim() : null;
        for (KvTypAvDiabetes typ : values()) {
            if (typ.name().equals(normId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

    public static KvTypAvDiabetes fromCode(String code) {
        return Stream.of(KvTypAvDiabetes.values()).filter(s -> code.equals(s.getCode())).findFirst()
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
