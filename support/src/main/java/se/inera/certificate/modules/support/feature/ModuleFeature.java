package se.inera.certificate.modules.support.feature;

public enum ModuleFeature {

    HANTERA_FRAGOR("hanteraFragor"),
    HANTERA_INTYGSUTKAST("hanteraIntygsutkast"),
    KOPIERA_INTYG("kopieraIntyg"),
    MAKULERA_INTYG("makuleraIntyg"),
    ARBETSGIVARUTSKRIFT("arbetsgivarUtskrift"),
    SKICKA_INTYG("skickaIntyg");

    private String name;

    ModuleFeature(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
