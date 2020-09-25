/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v7.model.internal;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public enum IntygAvserKategori {
    IAV1, IAV2, IAV3, IAV4, IAV5, IAV6, IAV7, IAV8, IAV9, IAV10;

    public static Set<IntygAvserKategori> getNormalCategories() {
        return ImmutableSet.of(IAV1, IAV2, IAV3, IAV4, IAV5, IAV6, IAV7, IAV8, IAV9);
    }
}
