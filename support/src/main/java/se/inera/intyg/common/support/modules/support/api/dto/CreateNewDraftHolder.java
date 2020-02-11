/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import static org.springframework.util.Assert.notNull;

import java.util.Optional;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;


public class CreateNewDraftHolder {

    private final String certificateId;

    private final String intygTypeVersion;

    private final HoSPersonal skapadAv;

    private final Patient patient;

    private final Optional<Forifyllnad> forifyllnad;

    private final boolean isTestIntyg;

    public CreateNewDraftHolder(String certificateId, String intygTypeVersion, HoSPersonal skapadAv, Patient patient) {
        this(certificateId, intygTypeVersion, skapadAv, patient, Optional.empty(), false);
    }

    public CreateNewDraftHolder(String certificateId, String intygTypeVersion, HoSPersonal skapadAv, Patient patient,
        Optional<Forifyllnad> forifyllnad, boolean isTestIntyg) {
        notNull(certificateId, "'certificateId' must not be null");
        notNull(intygTypeVersion, "'intygTypeVersion' must not be null");
        notNull(skapadAv, "'skapadAv' must not be null");
        notNull(patient, "'patient' must not be null");
        this.certificateId = certificateId;
        this.intygTypeVersion = intygTypeVersion;
        this.skapadAv = skapadAv;
        this.patient = patient;
        this.forifyllnad = forifyllnad;
        this.isTestIntyg = isTestIntyg;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public HoSPersonal getSkapadAv() {
        return skapadAv;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getIntygTypeVersion() {
        return intygTypeVersion;
    }

    public Optional<Forifyllnad> getForifyllnad() {
        return forifyllnad;
    }

    public boolean isTestIntyg() {
        return isTestIntyg;
    }
}
