/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.model.converter.prefill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public class PrefillResult {

    private final String intygsId;
    private final String intygsTyp;
    private final String intygsVersion;
    private final List<SvarResult> messages = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    PrefillResult(String intygsId, String intygsTyp, String intygsVersion) {
        this.intygsId = intygsId;
        this.intygsTyp = intygsTyp;
        this.intygsVersion = intygsVersion;
    }


    public String getIntygsTyp() {
        return intygsTyp;
    }

    public String getIntygsVersion() {
        return intygsVersion;
    }

    public String getIntygsId() {
        return intygsId;
    }

    public List<SvarResult> getMessages() {
        return messages;
    }

    public String toJsonReport() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return this.toString();
        }
    }

    void addMessage(PrefillEventType eventType, Svar svar, String message) {
        if (svar == null) {
            addMessage(eventType, "", message, "");
        } else {
            addMessage(eventType, svar.getId(), message, svar);
        }

    }

    void addMessage(PrefillEventType eventType, Delsvar delsvar, String message) {
        if (delsvar == null) {
            addMessage(eventType, "", message, "");
        } else {
            addMessage(eventType, delsvar.getId(), message, delsvar);
        }
    }


    void addMessage(PrefillEventType eventType, String svarsId, String message, Serializable input) {
        messages.add(new SvarResult(eventType, svarsId, message, input));
    }


    enum PrefillEventType {
        INFO, WARNING
    }

    static class SvarResult {

        private final String svarId;
        private PrefillEventType eventType;
        private String message;
        private Serializable input;

        SvarResult(PrefillEventType eventType, String svarId, String message, Serializable input) {
            this.eventType = eventType;
            this.svarId = svarId;
            this.message = message;
            this.input = input;
        }

        public String getSvarId() {
            return svarId;
        }

        public String getMessage() {
            return message;
        }

        // Input could potentially contain sensitive information that we dont't want to risk ending up in logs etc, so we ignore
        // them when serializing SvarResult.
        @JsonIgnore
        public Serializable getInput() {
            return input;
        }

        public PrefillEventType getEventType() {
            return eventType;
        }
    }
}
