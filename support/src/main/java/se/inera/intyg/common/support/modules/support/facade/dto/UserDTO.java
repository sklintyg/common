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
package se.inera.intyg.common.support.modules.support.facade.dto;

public class UserDTO {

    private String hsaId;
    private String name;
    private String role;
    private String loggedInUnit;
    private String loggedInCareProvider;

    public String getHsaId() {
        return hsaId;
    }

    public void setHsaId(String hsaId) {
        this.hsaId = hsaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLoggedInUnit() {
        return loggedInUnit;
    }

    public void setLoggedInUnit(String loggedInUnit) {
        this.loggedInUnit = loggedInUnit;
    }

    public String getLoggedInCareProvider() {
        return loggedInCareProvider;
    }

    public void setLoggedInCareProvider(String loggedInCareProvider) {
        this.loggedInCareProvider = loggedInCareProvider;
    }
}
