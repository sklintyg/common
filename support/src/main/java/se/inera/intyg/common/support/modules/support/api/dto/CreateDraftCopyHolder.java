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
package se.inera.intyg.common.support.modules.support.api.dto;

import static org.springframework.util.Assert.notNull;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.schemas.contract.Personnummer;

public class CreateDraftCopyHolder {

    private final String certificateId;

    private final HoSPersonal skapadAv;

    private Patient patient;

    private Personnummer newPersonnummer;

    private Relation relation;

    private String intygTypeVersion;

    private boolean isTestIntyg;

    public CreateDraftCopyHolder(String certificateId, HoSPersonal skapadAv, Relation relation) {
        this(certificateId, skapadAv);
        this.relation = relation;
    }

    public CreateDraftCopyHolder(String certificateId, HoSPersonal skapadAv) {
        notNull(certificateId, "'certificateId' must not be null");
        notNull(skapadAv, "'skapadAv' must not be null");
        this.certificateId = certificateId;
        this.skapadAv = skapadAv;
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

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Personnummer getNewPersonnummer() {
        return newPersonnummer;
    }

    public void setNewPersonnummer(Personnummer newPersonnummer) {
        this.newPersonnummer = newPersonnummer;
    }

    public boolean hasPatient() {
        return this.patient != null;
    }

    public boolean hasNewPersonnummer() {
        return this.newPersonnummer != null;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public String getIntygTypeVersion() {
        return intygTypeVersion;
    }

    public void setIntygTypeVersion(String intygTypeVersion) {
        this.intygTypeVersion = intygTypeVersion;
    }

    public boolean isTestIntyg() {
        return this.isTestIntyg;
    }

    public void setTestIntyg(boolean isTestIntyg) {
        this.isTestIntyg = isTestIntyg;
    }
}
