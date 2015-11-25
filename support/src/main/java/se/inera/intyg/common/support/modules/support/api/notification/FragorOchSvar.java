package se.inera.certificate.modules.support.api.notification;

public class FragorOchSvar {

    private int antalFragor;

    private int antalSvar;

    private int antalHanteradeFragor;

    private int antalHanteradeSvar;

    public FragorOchSvar(int antalFragor, int antalSvar, int antalHanteradeFragor, int antalHanteradeSvar) {
        super();
        this.antalFragor = antalFragor;
        this.antalSvar = antalSvar;
        this.antalHanteradeFragor = antalHanteradeFragor;
        this.antalHanteradeSvar = antalHanteradeSvar;
    }

    public FragorOchSvar() {
    }

    public static FragorOchSvar getEmpty() {
        return new FragorOchSvar(0, 0, 0, 0);
    }

    @Override
    public String toString() {
        return "antalFragor=" + antalFragor + ", antalSvar=" + antalSvar + ", antalHanteradeFragor=" + antalHanteradeFragor
                + ", antalHanteradeSvar=" + antalHanteradeSvar;
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

    public void setAntalFragor(int antalFragor) {
        this.antalFragor = antalFragor;
    }

    public void setAntalSvar(int antalSvar) {
        this.antalSvar = antalSvar;
    }

    public void setAntalHanteradeFragor(int antalHanteradeFragor) {
        this.antalHanteradeFragor = antalHanteradeFragor;
    }

    public void setAntalHanteradeSvar(int antalHanteradeSvar) {
        this.antalHanteradeSvar = antalHanteradeSvar;
    }

}
