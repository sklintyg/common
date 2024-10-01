/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Possible statuses for a Utkast entity in Webcert.
 *
 * @author marced
 */
public enum UtkastStatus {

    /**
     * Work in progress, invalid.
     */
    DRAFT_INCOMPLETE("DRAFT_INCOMPLETE", "Utkast, uppgifter saknas"),

    /**
     * Valid and ready for signing.
     */
    DRAFT_COMPLETE("DRAFT_COMPLETE", "Utkast, kan signeras"),

    /**
     * Locked journalhandling.
     */
    DRAFT_LOCKED("DRAFT_LOCKED", "Utkast, låst"),

    /**
     * Signed and valid.
     */
    SIGNED("SIGNED", "Signerat");

    private final String value;
    private final String klartext;

    UtkastStatus(String value, String description) {
        this.value = value;
        this.klartext = description;
    }

    public static Set<UtkastStatus> getDraftStatuses() {
        return Stream.of(DRAFT_INCOMPLETE, DRAFT_COMPLETE, DRAFT_LOCKED).collect(Collectors.toSet());
    }

    public static Set<UtkastStatus> getEditableDraftStatuses() {
        return Stream.of(DRAFT_INCOMPLETE, DRAFT_COMPLETE).collect(Collectors.toSet());
    }


    public String value() {
        return value;
    }

    public String getKlartext() {
        return this.klartext;
    }

    public static UtkastStatus fromValue(String value) {
        return Stream.of(UtkastStatus.values()).filter(s -> value.equals(s.value())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(value));
    }
}
