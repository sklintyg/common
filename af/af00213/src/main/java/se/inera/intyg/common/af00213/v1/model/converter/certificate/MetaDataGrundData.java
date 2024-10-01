/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.DESCRIPTION;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.grundData;

import se.inera.intyg.common.af00213.support.Af00213EntryPoint;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.util.MetaDataToolkit;
import se.inera.intyg.common.support.model.common.internal.GrundData;

public class MetaDataGrundData {

    public static CertificateMetadata toCertificate(Af00213UtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        return CertificateMetadata.builder()
            .id(internalCertificate.getId())
            .type(internalCertificate.getTyp())
            .typeName(Af00213EntryPoint.ISSUER_TYPE_ID)
            .typeVersion(internalCertificate.getTextVersion())
            .name("Arbetsförmedlingens medicinska utlåtande")
            .description(texts.get(DESCRIPTION))
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

    public static GrundData toInternal(CertificateMetadata metadata, GrundData grundData) {
        return grundData(metadata, grundData);
    }
}
