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
package se.inera.intyg.common.support.facade.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import se.inera.intyg.common.support.facade.model.Patient.PatientBuilder;

@JsonDeserialize(builder = PatientBuilder.class)
@Value
@Builder
public class Patient {

    @With
    PersonId personId;
    @With
    PersonId previousPersonId;
    String firstName;
    String lastName;
    String middleName;
    String fullName;
    String street;
    String city;
    String zipCode;
    boolean coordinationNumber;
    boolean testIndicated;
    boolean protectedPerson;
    boolean deceased;
    @With
    boolean differentNameFromEHR;
    @With
    boolean personIdChanged;
    @With
    boolean reserveId;
    boolean addressFromPU;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PatientBuilder {

    }


    /**
     * Handles logic for deep integration when alternateSSN is set to a reserve id.
     *
     * @return PersonId of type 'PERSON_NUMMER'
     */
    public PersonId getActualPersonId() {
        return reserveId ? previousPersonId : personId;
    }
}
