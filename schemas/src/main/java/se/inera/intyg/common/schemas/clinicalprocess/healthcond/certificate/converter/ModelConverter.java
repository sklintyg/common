/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter;

import org.joda.time.LocalDate;

import se.inera.certificate.model.util.Iterables;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.builder.ClinicalProcessCertificateMetaTypeBuilder;
import se.riv.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;

/**
 * @author andreaskaltenbach
 */
public final class ModelConverter {

    private ModelConverter() {
    }

    public static CertificateMetaType toCertificateMetaType(CertificateHolder source) {

        ClinicalProcessCertificateMetaTypeBuilder builder = new ClinicalProcessCertificateMetaTypeBuilder()
                .certificateId(source.getId())
                .certificateType(source.getType())
                .validity(toLocalDate(source.getValidFromDate()), toLocalDate(source.getValidToDate()))
                .issuerName(source.getSigningDoctorName())
                .facilityName(source.getCareUnitName())
                .signDate(source.getSignedDate())
                .available(source.isDeleted() ? "false" : "true")
                .complemantaryInfo(source.getAdditionalInfo());

        CertificateMetaType meta = builder.build();

        Iterables.addAll(meta.getStatus(), CertificateStateHolderConverter.toCertificateStatusType(source.getCertificateStates()));
        return builder.build();
    }


    private static LocalDate toLocalDate(Object date) {
        if (date == null) {
            return null;
        }
        return new LocalDate(date);
    }

}
