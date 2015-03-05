package se.inera.certificate.common.enumerations;

public enum Diagnoskodverk {

    //TODO Move snomed_CT to its own enum
    SNOMED_CT("1.2.752.116.2.1.1.1", "SNOMED-CT", null),

    ICD_10_SE("1.2.752.116.1.1.1.1.3", "ICD-10", null),

    KSH_97_P("1.2.752.116.1.3.1.4.1", "KSH97-P", null);

    private Diagnoskodverk(String codeSystem, String codeSystemName, String codeSystemVersion) {
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.codeSystemVersion = codeSystemVersion;
    }

    private final String codeSystem;

    private final String codeSystemName;

    private final String codeSystemVersion;

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public static Diagnoskodverk getEnumByCodeSystem(String oid) {
        for (Diagnoskodverk kodverk : Diagnoskodverk.values()) {
            if (kodverk.getCodeSystem().equals(oid)) {
                return kodverk;
            }
        }
        return null;
    }
}
