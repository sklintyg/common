package se.inera.intyg.common.sos_doi.model.converter;

import se.inera.intyg.common.sos_doi.model.internal.DodsorsaksintygUtlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

public final class InternalToTransport {
    private InternalToTransport() {
    }

    public static RegisterCertificateType convert(DodsorsaksintygUtlatande utlatande) throws ConverterException {
        if (utlatande == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        RegisterCertificateType registerCertificate = new RegisterCertificateType();
        registerCertificate.setIntyg(UtlatandeToIntyg.convert(utlatande));
        return registerCertificate;
    }
}
