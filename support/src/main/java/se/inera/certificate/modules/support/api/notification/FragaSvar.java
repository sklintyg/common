package se.inera.certificate.modules.support.api.notification;

public class FragaSvar {

    private final int antalFragor;

    private final int antalSvar;

    private final int antalHanteradeFragor;

    private final int antalHanteradeSvar;

    public FragaSvar(int antalFragor, int antalSvar, int antalHanteradeFragor, int antalHanteradeSvar) {
        super();
        this.antalFragor = antalFragor;
        this.antalSvar = antalSvar;
        this.antalHanteradeFragor = antalHanteradeFragor;
        this.antalHanteradeSvar = antalHanteradeSvar;
    }

    public int getAntalFragor() {
        return antalFragor;
    }

    public int getAntalSvar() {
        return antalSvar;
    }

    public int getAntalHanteradeFragor() {
        return antalHanteradeFragor;
    }

    public int getAntalHanteradeSvar() {
        return antalHanteradeSvar;
    }

}
