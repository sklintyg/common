/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.facade.model.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.common.support.facade.model.CareProvider;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.facade.model.user.User.UserBuilder;

@JsonDeserialize(builder = UserBuilder.class)
@Value
@Builder
public class User {

    String hsaId;
    String name;
    String role;
    SigningMethod signingMethod;
    LoginMethod loginMethod;
    Unit loggedInUnit;
    Unit loggedInCareUnit;
    Unit loggedInCareProvider;
    Map<String, String> preferences;
    boolean protectedPerson;
    List<CareProvider> careProviders;
    String launchId;
    String launchFromOrigin;
    String origin;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserBuilder {

    }
}
