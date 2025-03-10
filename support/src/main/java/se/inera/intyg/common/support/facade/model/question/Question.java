/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.facade.model.question;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.common.support.facade.model.link.ResourceLink;
import se.inera.intyg.common.support.facade.model.metadata.CertificateRelation;
import se.inera.intyg.common.support.facade.model.question.Question.QuestionBuilder;

@JsonDeserialize(builder = QuestionBuilder.class)
@Value
@Builder
public class Question {

    String id;
    QuestionType type;
    String subject;
    String message;
    String author;
    LocalDateTime sent;
    Complement[] complements;
    boolean isHandled;
    boolean isForwarded;
    Answer answer;
    CertificateRelation answeredByCertificate;
    Reminder[] reminders;
    LocalDateTime lastUpdate;
    LocalDate lastDateToReply;
    List<ResourceLink> links;
    String[] contactInfo;
    String certificateId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class QuestionBuilder {

    }
}
