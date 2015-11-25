package se.inera.intyg.common.support.modules.support.api.dto;

public enum TransportModelVersion {
    /** */
    LEGACY_LAKARUTLATANDE("urn:riv:insuranceprocess:healthreporting:mu7263:3"),
    /** */
    UTLATANDE_V1("urn:riv:clinicalprocess:healthcond:certificate:1");

    private final String namespace;

    TransportModelVersion(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }
}
