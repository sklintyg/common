package se.inera.intyg.common.support.modules.support.api.notification;

public enum HandelseType {

    /**
     * HAN1: Intygsutkast skapat.
     */
    INTYGSUTKAST_SKAPAT,

    /**
     * HAN2: Intyg signerat.
     */
    INTYGSUTKAST_SIGNERAT,

    /**
     * HAN3: Intyg skickat till FK.
     */
    INTYG_SKICKAT_FK,

    /**
     * HAN4: Intygsutkast raderat.
     */
    INTYGSUTKAST_RADERAT,

    /**
     * HAN5: Intyg makulerat.
     */
    INTYG_MAKULERAT,

    /**
     * HAN6: Ny fråga från FK.
     */
    FRAGA_FRAN_FK,

    /**
     * HAN7: Nytt svar från FK.
     */
    SVAR_FRAN_FK,

    /**
     * HAN8: Ny fråga till FK.
     */
    FRAGA_TILL_FK,

    /**
     * HAN9: Hanterad fråga från FK.
     */
    FRAGA_FRAN_FK_HANTERAD,

    /**
     * HAN10: Hanterat svar från FK.
     */
    SVAR_FRAN_FK_HANTERAD,

    /**
     * HAN11: Intygsutkast ändrat.
     */
    INTYGSUTKAST_ANDRAT;

    public String value() {
        return name();
    }

    public static HandelseType fromValue(String v) {
        return valueOf(v);
    }

}
