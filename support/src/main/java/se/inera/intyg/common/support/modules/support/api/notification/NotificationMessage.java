/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import se.inera.intyg.common.support.common.enumerations.HandelsekodEnum;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Amneskod;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public class NotificationMessage {

    private String intygsId;

    private String intygsTyp;

    private String logiskAdress;

    private LocalDateTime handelseTid;

    private HandelsekodEnum handelse;

    private SchemaVersion version;

    private String reference;

    // The reason why this is an Object is that when serializing with
    // @JsonRawValue (below), it works as intended even
    // if this is a string. However, deserializing doesn't work without the
    // deserialized json in this attribute being
    // quoted. A String getter and an JsonNode setter, both working with an
    // Object attribute, works.
    private Object utkastJson;

    private FragorOchSvar fragaSvar;

    private ArendeCount skickadeFragor;

    private ArendeCount mottagnaFragor;

    private Amneskod amne;

    private LocalDate sistaSvarsDatum;

    public NotificationMessage() {
        // Needed for deserialization
    }

    // CHECKSTYLE:OFF ParameterNumber
    public NotificationMessage(String intygsId, String intygsTyp, LocalDateTime handelseTid, HandelsekodEnum handelse,
        String logiskAdress, String utkastJson, FragorOchSvar fragaSvar, ArendeCount skickadeFragor,
        ArendeCount mottagnaFragor, SchemaVersion version, String reference) {

        this(intygsId, intygsTyp, handelseTid, handelse,
            logiskAdress, utkastJson, fragaSvar, skickadeFragor,
            mottagnaFragor, version, reference, null, null);
    }

    public NotificationMessage(String intygsId, String intygsTyp, LocalDateTime handelseTid, HandelsekodEnum handelse,
        String logiskAdress, String utkastJson, FragorOchSvar fragaSvar, ArendeCount skickadeFragor,
        ArendeCount mottagnaFragor, SchemaVersion version, String reference, Amneskod amne,
        LocalDate sistaSvarsDatum) {

        super();
        this.intygsId = intygsId;
        this.intygsTyp = intygsTyp;
        this.handelseTid = handelseTid;
        this.handelse = handelse;
        this.logiskAdress = logiskAdress;
        this.utkastJson = utkastJson;
        this.fragaSvar = fragaSvar;
        this.skickadeFragor = skickadeFragor;
        this.mottagnaFragor = mottagnaFragor;
        this.version = version;
        this.reference = reference;
        this.amne = amne;
        this.sistaSvarsDatum = sistaSvarsDatum;
    }
    // CHECKSTYLE:ON ParameterNumber

    @Override
    public String toString() {
        return "NotificationMessage{"
            + "intygsId='" + intygsId + '\''
            + ", intygsTyp='" + intygsTyp + '\''
            + ", logiskAdress='" + logiskAdress + '\''
            + ", handelseTid=" + handelseTid
            + ", handelse=" + handelse
            + ", version=" + version
            + ", reference='" + reference + '\''
            + ", amne=" + amne
            + ", sistaSvarsDatum=" + sistaSvarsDatum
            + '}';
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

    public HandelsekodEnum getHandelse() {
        return handelse;
    }

    @JsonRawValue
    public String getUtkast() {
        return utkastJson == null ? null : utkastJson.toString();
    }

    public FragorOchSvar getFragaSvar() {
        return fragaSvar;
    }

    public ArendeCount getSkickadeFragor() {
        return skickadeFragor;
    }

    public ArendeCount getMottagnaFragor() {
        return mottagnaFragor;
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

    public void setHandelse(HandelsekodEnum handelse) {
        this.handelse = handelse;
    }

    public void setUtkast(JsonNode utkastJson) {
        this.utkastJson = utkastJson;
    }

    public void setFragaSvar(FragorOchSvar fragaSvar) {
        this.fragaSvar = fragaSvar;
    }

    public void setSkickadeFragor(ArendeCount skickadeFragor) {
        this.skickadeFragor = skickadeFragor;
    }

    public void setMottagnaFragor(ArendeCount mottagnaFragor) {
        this.mottagnaFragor = mottagnaFragor;
    }

    public SchemaVersion getVersion() {
        return version;
    }

    public void setVersion(SchemaVersion version) {
        this.version = version;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Amneskod getAmne() {
        return amne;
    }

    public void setAmne(Amneskod amne) {
        this.amne = amne;
    }

    public LocalDate getSistaSvarsDatum() {
        return sistaSvarsDatum;
    }

    public void setSistaSvarsDatum(LocalDate sistaSvarsDatum) {
        this.sistaSvarsDatum = sistaSvarsDatum;
    }

}
