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
package se.inera.intyg.common.support.model.converter.util;

import static java.util.Comparator.comparing;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.CollectionUtils;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;

public final class CertificateStateHolderUtil {

    public static final List<CertificateState> ARCHIVED_STATUSES = Arrays.asList(CertificateState.DELETED, CertificateState.RESTORED);

    private CertificateStateHolderUtil() {
    }

    public static boolean isArchived(List<CertificateStateHolder> states) {
        if (CollectionUtils.isEmpty(states)) {
            return false;
        }
        return states.stream()
            .filter(s -> ARCHIVED_STATUSES.contains(s.getState()))
            .max(comparing(CertificateStateHolder::getTimestamp))
            .map(s -> CertificateState.DELETED.equals(s.getState()))
            .orElse(false);
    }
}
