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
package se.inera.intyg.common.support.modules.support.api.exception;

public class ModuleConverterException extends ModuleException {

    private static final long serialVersionUID = 4055150962655732547L;

    public ModuleConverterException() {
        super();
    }

    public ModuleConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleConverterException(String message) {
        super(message);
    }

    public ModuleConverterException(Throwable cause) {
        super(cause);
    }
}
