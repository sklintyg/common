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
package se.inera.intyg.common.ts_diabetes.v3.model.kodverk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum KvIdKontroll {

    ID_KORT("IDK1", "ID-kort"),
    FORETAG_ELLER_TJANSTEKORT("IDK2", "Företagskort eller tjänstekort"),
    KORKORT("IDK3", "Svenskt körkort"),
    PERS_KANNEDOM("IDK4", "Personlig kännedom"),
    FORSAKRAN_KAP18("IDK5", "Försäkran enligt 18 kap. 4 §"),
    PASS("IDK6", "Pass");

    public static final String CODE_SYSTEM = "e7cc8f30-a353-4c42-b17a-a189b6876647";
    final String code;
    final String description;

    KvIdKontroll(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static KvIdKontroll fromId(String id) {
        String normId = id != null ? id.trim() : null;
        for (KvIdKontroll typ : values()) {
            if (typ.getCode().equals(normId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException(id);
    }

    public static KvIdKontroll fromCode(String code) {
        return Stream.of(KvIdKontroll.values()).filter(s -> code.equals(s.getCode())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(code));
    }

    @JsonValue
    public String getId() {
        return this.code;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
