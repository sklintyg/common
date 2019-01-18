/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_tstrk1062.v1.model.transformer;

import se.inera.intyg.common.support.modules.transformer.XslTransformerType;

public enum TsTstrk1062TransformerType implements XslTransformerType {

    TRANSPORT_TO_V1("transport-to-v1");

    private final String name;

    TsTstrk1062TransformerType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
