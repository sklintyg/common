/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.model.util;

import java.util.Collection;
import java.util.List;

public final class Iterables {

    private Iterables() {
    }

    public static <T> T find(List<? extends T> list, Predicate<T> predicate, T defaultResult) {
        if (list != null) {
            for (T item : list) {
                if (predicate.apply(item)) {
                    return item;
                }
            }
        }
        return defaultResult;
    }

    public static <T> void addAll(Collection<T> targetCollection, Collection<T> toAdd) {
        if (targetCollection != null && toAdd != null) {
            targetCollection.addAll(toAdd);
        }
    }

    /**
     * Adds a value to a collection if the collection if both collection and value is non-null.
     *
     * @param targetCollection targetCollection
     * @param toAdd            toAdd
     */
    public static <T> void addExisting(Collection<T> targetCollection, T toAdd) {
        if (targetCollection != null && toAdd != null) {
            targetCollection.add(toAdd);
        }
    }
}
