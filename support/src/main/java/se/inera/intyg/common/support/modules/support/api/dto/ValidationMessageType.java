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
package se.inera.intyg.common.support.modules.support.api.dto;

public enum ValidationMessageType {
    BLANK,
    EMPTY, // empty does not generate error unless "Visa vad som saknas" is pressed
    INVALID_FORMAT, // invalid_format DOES generate error even if "Visa vad som saknas" is NOT pressed
    INCORRECT_COMBINATION,
    OTHER,
    PERIOD_OVERLAP,
    WARN // Warn never generates error or stops signing. It's used for non-blocking info/warning messages
    // such as when user has specified a date which is in the future which may be weird but not invalid.
}
