package se.inera.certificate.model.common.internal;

public class Utlatande {

    private String id;
    private String typ;

    private IntygMetadata intygMetadata = new IntygMetadata();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public IntygMetadata getIntygMetadata() {
        return intygMetadata;
    }

    public void setIntygMetadata(IntygMetadata intygMetadata) {
        this.intygMetadata = intygMetadata;
    }

}
