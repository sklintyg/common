package se.inera.intyg.common.support.model.common.internal;

import org.joda.time.LocalDateTime;

import java.util.Objects;

public class GrundData {
    private LocalDateTime signeringsdatum;
    private HoSPersonal skapadAv;
    private Patient patient;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final GrundData that = (GrundData) object;
        return Objects.equals(this.signeringsdatum, that.signeringsdatum) &&
                Objects.equals(this.skapadAv, that.skapadAv) &&
                Objects.equals(this.patient, that.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.signeringsdatum, this.skapadAv, this.patient);
    }

    public LocalDateTime getSigneringsdatum() {
        return signeringsdatum;
    }

    public void setSigneringsdatum(LocalDateTime signeringsdatum) {
        this.signeringsdatum = signeringsdatum;
    }

    public HoSPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HoSPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
