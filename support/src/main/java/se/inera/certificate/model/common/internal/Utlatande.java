package se.inera.certificate.model.common.internal;

public class Utlatande {

    private String id;
    private String typ;

    private GrundData grundData = new GrundData();


    // Getter and setters

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

    public GrundData getGrundData() {
        return grundData;
    }

    public void setGrundData(GrundData grundData) {
        this.grundData = grundData;
    }

}
