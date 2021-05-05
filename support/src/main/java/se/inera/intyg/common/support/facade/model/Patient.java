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

package se.inera.intyg.common.support.facade.model;

public class Patient {
    private PersonId personId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String fullName;
    private boolean coordinationNumber;
    private boolean testIndicated;
    private boolean protectedPerson;
    private boolean deceased;

    public PersonId getPersonId() {
        return personId;
    }

    public void setPersonId(PersonId personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isCoordinationNumber() {
        return coordinationNumber;
    }

    public void setCoordinationNumber(boolean coordinationNumber) {
        this.coordinationNumber = coordinationNumber;
    }

    public boolean isTestIndicated() {
        return testIndicated;
    }

    public void setTestIndicated(boolean testIndicated) {
        this.testIndicated = testIndicated;
    }

    public boolean isProtectedPerson() {
        return protectedPerson;
    }

    public void setProtectedPerson(boolean protectedPerson) {
        this.protectedPerson = protectedPerson;
    }

    public boolean isDeceased() {
        return deceased;
    }

    public void setDeceased(boolean deceased) {
        this.deceased = deceased;
    }
}
