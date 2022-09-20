package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.DESCRIPTION;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.grundData;

import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Staff;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.model.common.internal.GrundData;

public class MetaDataGrundData {

    public static CertificateMetadata toCertificate(Af00213UtlatandeV1 internalCertificate, CertificateTextProvider texts) {
        final var unit = internalCertificate.getGrundData().getSkapadAv().getVardenhet();
        return CertificateMetadata.builder()
            .id(internalCertificate.getId())
            .type(internalCertificate.getTyp())
            .typeVersion(internalCertificate.getTextVersion())
            .name("Arbetsförmedlingens medicinska utlåtande")
            .description(texts.get(DESCRIPTION))
            .unit(
                Unit.builder()
                    .unitId(unit.getEnhetsid())
                    .unitName(unit.getEnhetsnamn())
                    .address(unit.getPostadress())
                    .zipCode(unit.getPostnummer())
                    .city(unit.getPostort())
                    .email(unit.getEpost())
                    .phoneNumber(unit.getTelefonnummer())
                    .build()
            )
            .issuedBy(
                Staff.builder()
                    .personId(internalCertificate.getGrundData().getSkapadAv().getPersonId())
                    .fullName(internalCertificate.getGrundData().getSkapadAv().getFullstandigtNamn())
                    .build()
            )
            .build();
    }

    public static GrundData toInternal(CertificateMetadata metadata, GrundData grundData) {
        return grundData(metadata, grundData);
    }
}
