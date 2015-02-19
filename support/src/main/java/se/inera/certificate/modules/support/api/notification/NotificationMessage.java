package se.inera.certificate.modules.support.api.notification;

import org.joda.time.LocalDateTime;

import se.inera.certificate.modules.support.api.notification.FragaSvar;
import se.inera.certificate.modules.support.api.notification.HandelseType;

public class NotificationMessage {

    private final String intygsId;
    
    private final String intygsTyp;
    
    private final String logiskAdress;

    private final LocalDateTime handelseTid;

    private final HandelseType handelse;

    private final String utkast;

    private final FragaSvar fragaSvar;

    public NotificationMessage(String intygsId, String intygsTyp, LocalDateTime handelseTid, HandelseType handelse, String logiskAdress,
            String utkast, FragaSvar fragaSvar) {
        super();
        this.intygsId = intygsId;
        this.intygsTyp = intygsTyp;
        this.handelseTid = handelseTid;
        this.handelse = handelse;
        this.logiskAdress = logiskAdress;
        this.utkast = utkast;
        this.fragaSvar = fragaSvar;
    }

    public String getIntygsId() {
        return intygsId;
    }

    public String getIntygsTyp() {
        return intygsTyp;
    }

    public String getLogiskAdress() {
        return logiskAdress;
    }

    public LocalDateTime getHandelseTid() {
        return handelseTid;
    }

    public HandelseType getHandelse() {
        return handelse;
    }

    public String getUtkast() {
        return utkast;
    }

    public FragaSvar getFragaSvar() {
        return fragaSvar;
    }

}
