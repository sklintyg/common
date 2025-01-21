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
package se.inera.intyg.common.support.validate;

import java.util.List;

/**
 * Validator for a given <code>root</code> of an {@link se.inera.intyg.common.support.model} object.
 *
 * @author Gustav Norbäcker, R2M
 */
public interface RootValidator {

    /**
     * The root that this validator supports.
     *
     * @return The name of the {@link se.inera.intyg.common.support.model} <code>root</code>.
     */
    String getRoot();

    /**
     * Performs validation of the {@link se.inera.intyg.common.support.model} <code>extension</code> of the <code>root</code>
     * that this validator supports.
     *
     * @param extension The extension to validate.
     * @return A list of validation messages. An empty string if validation was successful.
     */
    List<String> validateExtension(String extension);
}
