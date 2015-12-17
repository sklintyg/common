/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.common.support.modules.support.api.notification;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public class NotificationMessage {

    private String intygsId;

    private String intygsTyp;

    private String logiskAdress;

    private LocalDateTime handelseTid;

    private HandelseType handelse;

    // The reason why this is an Object is that when serializing with @JsonRawValue (below), it works as intended even
    // if this is a string. However, deserializing doesn't work without the deserialized json in this attribute being
    // quoted. A String getter and an JsonNode setter, both working with an Object attribute, works.
    private Object utkastJson;

    private FragorOchSvar fragaSvar;

    public NotificationMessage(String intygsId, String intygsTyp, LocalDateTime handelseTid, HandelseType handelse, String logiskAdress,
            String utkastJson, FragorOchSvar fragaSvar) {
        super();
        this.intygsId = intygsId;
        this.intygsTyp = intygsTyp;
        this.handelseTid = handelseTid;
        this.handelse = handelse;
        this.logiskAdress = logiskAdress;
        this.utkastJson = utkastJson;
        this.fragaSvar = fragaSvar;
    }

    public NotificationMessage() {
    }

    @Override
    public String toString() {
        return "NotificationMessage [intygsId=" + intygsId + ", intygsTyp=" + intygsTyp + ", logiskAdress=" + logiskAdress + ", handelseTid="
                + handelseTid + ", handelse=" + handelse + "]";
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

    @JsonRawValue
    public String getUtkast() {
        return utkastJson == null ? null : utkastJson.toString();
    }

    public FragorOchSvar getFragaSvar() {
        return fragaSvar;
    }

    public void setIntygsId(String intygsId) {
        this.intygsId = intygsId;
    }

    public void setIntygsTyp(String intygsTyp) {
        this.intygsTyp = intygsTyp;
    }

    public void setLogiskAdress(String logiskAdress) {
        this.logiskAdress = logiskAdress;
    }

    public void setHandelseTid(LocalDateTime handelseTid) {
        this.handelseTid = handelseTid;
    }

    public void setHandelse(HandelseType handelse) {
        this.handelse = handelse;
    }

    public void setUtkast(JsonNode utkastJson) {
        this.utkastJson = utkastJson;
    }

    public void setFragaSvar(FragorOchSvar fragaSvar) {
        this.fragaSvar = fragaSvar;
    }

}
