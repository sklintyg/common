package se.inera.certificate.modules.support.api.builder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.CertificateState;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.modules.support.api.CertificateStateHolder;
import se.inera.certificate.modules.support.api.dto.Personnummer;

public class CertificateHolderBuilder {

    private CertificateHolder certificate;

    public CertificateHolderBuilder(String certificateId) {
        this(certificateId, "");
    }

    public CertificateHolderBuilder(String certificateId, String document) {
        this.certificate = new CertificateHolder();
        this.certificate.setId(certificateId);
        this.certificate.setDocument(document);
    }

    public CertificateHolderBuilder certificateType(String certificateType) {
        certificate.setType(certificateType);
        return this;
    }

    public CertificateHolderBuilder civicRegistrationNumber(Personnummer civicRegistrationNumber) {
        certificate.setCivicRegistrationNumber(civicRegistrationNumber);
        return this;
    }

    public CertificateHolderBuilder validity(String fromDate, String toDate) {
        certificate.setValidFromDate(fromDate);
        certificate.setValidToDate(toDate);
        return this;
    }

    public CertificateHolderBuilder careUnitId(String careUnitId) {
        certificate.setCareUnitId(careUnitId);
        return this;
    }

    public CertificateHolderBuilder careUnitName(String careUnitName) {
        certificate.setCareUnitName(careUnitName);
        return this;
    }

    public CertificateHolderBuilder signingDoctorName(String signingDoctorName) {
        certificate.setSigningDoctorName(signingDoctorName);
        return this;
    }

    public CertificateHolderBuilder signedDate(LocalDateTime signedDate) {
        certificate.setSignedDate(signedDate);
        return this;
    }

    public CertificateHolderBuilder deleted(boolean deleted) {
        certificate.setDeleted(deleted);
        return this;
    }

    public CertificateHolderBuilder state(CertificateState state, String target) {
        return state(state, target, null);
    }

    public CertificateHolderBuilder state(CertificateState state, String target, LocalDateTime timestamp) {
        List<CertificateStateHolder> states = this.certificate.getCertificateStates();
        if (states == null) {
            states = new ArrayList<CertificateStateHolder>();
            this.certificate.setCertificateStates(states);
        }
        states.add(new CertificateStateHolder(target, state, timestamp));
        return this;
    }

    public CertificateHolder build() {
        return certificate;
    }
}
