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
package se.inera.intyg.common.fk7263.model.converter.util;

import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;

public final class ConverterUtil {

    private ConverterUtil() {
    }

    public static CertificateHolder toCertificateHolder(Fk7263Utlatande utlatande) throws ModuleException {
        CertificateHolder certificateHolder = new CertificateHolder();
        certificateHolder.setId(utlatande.getId());
        certificateHolder.setCareUnitId(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        certificateHolder.setCareUnitName(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        certificateHolder.setCareGiverId(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        certificateHolder.setSigningDoctorId(utlatande.getGrundData().getSkapadAv().getPersonId());
        certificateHolder.setSigningDoctorName(utlatande.getGrundData().getSkapadAv().getFullstandigtNamn());
        certificateHolder.setCivicRegistrationNumber(utlatande.getGrundData().getPatient().getPersonId());
        certificateHolder.setSignedDate(utlatande.getGrundData().getSigneringsdatum());
        certificateHolder.setType(Fk7263EntryPoint.MODULE_ID);
        certificateHolder.setTypeVersion(Fk7263EntryPoint.DEFAULT_LOCKED_TYPE_VERSION);
        certificateHolder.setValidFromDate(utlatande.getGiltighet().getFrom().toString());
        certificateHolder.setValidToDate(utlatande.getGiltighet().getTom().toString());
        certificateHolder
            .setAdditionalInfo(utlatande.getGiltighet().getFrom().toString() + " - " + utlatande.getGiltighet().getTom().toString());
        return certificateHolder;
    }

}
