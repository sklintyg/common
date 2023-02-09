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

package se.inera.intyg.common.fk7263.model.converter.certificate;

import static se.inera.intyg.common.fk7263.support.Fk7263EntryPoint.MODULE_DETAILED_DESCRIPTION;
import static se.inera.intyg.common.fk7263.support.Fk7263EntryPoint.MODULE_NAME;

import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.util.MetaDataToolkit;

public class MetaDataGrundData {

    public static CertificateMetadata toCertificate(Fk7263Utlatande internalCertificate) {
        return CertificateMetadata.builder()
            .id(internalCertificate.getId())
            .type(internalCertificate.getTyp())
            .typeName(Fk7263EntryPoint.ISSUER_TYPE_ID)
            .typeVersion(internalCertificate.getTextVersion())
            .name(MODULE_NAME)
            .description(MODULE_DETAILED_DESCRIPTION)
            .unit(
                MetaDataToolkit.toCertificate(internalCertificate.getGrundData().getSkapadAv().getVardenhet())
            )
            .issuedBy(
                MetaDataToolkit.toCertificate(internalCertificate.getGrundData().getSkapadAv())
            )
            .patient(
                MetaDataToolkit.toCertificate(internalCertificate.getGrundData().getPatient())
            )
            .build();
    }
}
