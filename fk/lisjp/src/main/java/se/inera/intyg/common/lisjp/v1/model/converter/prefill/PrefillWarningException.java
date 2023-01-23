/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.model.converter.prefill;

import java.io.Serializable;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public class PrefillWarningException extends Exception {

    private final Svar svar;
    private final Delsvar delsvar;

    public PrefillWarningException(Svar svar, String message) {
        super(message);
        this.svar = svar;
        this.delsvar = null;
    }

    public PrefillWarningException(Delsvar delsvar, String message) {
        super(message);
        this.svar = null;
        this.delsvar = delsvar;
    }

    public String getSvarId() {
        if (svar != null) {
            return svar.getId();
        }

        if (delsvar != null) {
            return delsvar.getId();
        }
        return "n/a";
    }

    public Serializable getSourceContent() {
        if (svar != null) {
            return svar;
        }

        if (delsvar != null) {
            return delsvar;
        }
        return "(no content)";
    }


}
