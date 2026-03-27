/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.modules.support.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.util.Assert;

@Value
@Builder
@AllArgsConstructor
public class ValidationMessage {

  String category;
  String field;
  String message;
  ValidationMessageType type;
  String dynamicKey;
  String questionId;

  public ValidationMessage(String category, String field, ValidationMessageType type) {
    Assert.hasText(category, "'category' must not be empty");
    Assert.hasText(field, "'field' must not be empty");
    Assert.notNull(type, "'type' must not be empty");
    this.category = category;
    this.field = field;
    this.type = type;
    this.message = null;
    this.dynamicKey = null;
    this.questionId = null;
  }

  public ValidationMessage(
      String category, String field, ValidationMessageType type, String message) {
    Assert.hasText(category, "'category' must not be empty");
    Assert.hasText(field, "'field' must not be empty");
    Assert.notNull(type, "'type' must not be empty");
    Assert.hasText(message, "'message' must not be empty");
    this.category = category;
    this.field = field;
    this.type = type;
    this.message = message;
    this.dynamicKey = null;
    this.questionId = null;
  }

  public ValidationMessage(
      String category,
      String field,
      ValidationMessageType type,
      String message,
      String dynamicKey) {
    Assert.hasText(category, "'category' must not be empty");
    Assert.hasText(field, "'field' must not be empty");
    Assert.notNull(type, "'type' must not be empty");
    Assert.hasText(message, "'message' must not be empty");
    Assert.hasText(dynamicKey, "'dynamicLabel' must not be empty");
    this.category = category;
    this.field = field;
    this.type = type;
    this.message = message;
    this.dynamicKey = dynamicKey;
    this.questionId = null;
  }

  public static ValidationMessage create(
      String category, String field, ValidationMessageType type, String questionId) {
    return ValidationMessage.builder()
        .category(category)
        .field(field)
        .type(type)
        .questionId(questionId)
        .build();
  }

  public static ValidationMessage create(
      String category,
      String field,
      ValidationMessageType type,
      String message,
      String questionId) {
    return ValidationMessage.builder()
        .category(category)
        .field(field)
        .type(type)
        .message(message)
        .questionId(questionId)
        .build();
  }

  public static ValidationMessage create(
      String category,
      String field,
      ValidationMessageType type,
      String message,
      String dynamicKey,
      String questionId) {
    return ValidationMessage.builder()
        .category(category)
        .field(field)
        .type(type)
        .message(message)
        .dynamicKey(dynamicKey)
        .questionId(questionId)
        .build();
  }
}
