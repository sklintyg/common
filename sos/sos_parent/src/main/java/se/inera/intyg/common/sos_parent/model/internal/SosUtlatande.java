/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.sos_parent.model.internal;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

/**
 * Shared fields between Dödsbevis and Dödsorsaksintyg.
 */
public interface SosUtlatande extends Utlatande {

    String getIdentitetStyrkt();

    Boolean getDodsdatumSakert();

    InternalDate getDodsdatum();

    InternalDate getAntraffatDodDatum();

    String getDodsplatsKommun();

    DodsplatsBoende getDodsplatsBoende();

    Boolean getBarn();
}
