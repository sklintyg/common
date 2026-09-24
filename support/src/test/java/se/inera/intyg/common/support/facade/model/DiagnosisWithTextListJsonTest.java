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
package se.inera.intyg.common.support.facade.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDiagnosesWithText;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.config.DiagnosesWithTextListItem;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisWithText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisWithTextList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import tools.jackson.databind.json.JsonMapper;

/**
 * The JSON bodies of certificate-service doc/design-diagnosis-component-fk7811.md § 3.2, as
 * certificate-service emits them for UE_DIAGNOSES_WITH_TEXT.
 */
class DiagnosisWithTextListJsonTest {

  private static final String CONFIG =
      """
      {
        "type": "UE_DIAGNOSES_WITH_TEXT",
        "header": null, "icon": null, "accordion": null, "message": null,
        "text": "Diagnos eller diagnoser för de besvär som patienten anser är en arbetsskada",
        "description": "Använd den mest specifika diagnosen som beskriver besvären baserat på de utredningar som gjorts.",
        "label": null,
        "terminology": [ { "id": "ICD_10_SE", "label": "ICD-10-SE" } ],
        "textLabel": "När och var ställdes diagnosen?",
        "textLimit": 50,
        "list": [
          { "id": "diagnos1", "diagnosisId": "diagnos1.diagnos", "textId": "diagnos1.text" },
          { "id": "diagnos2", "diagnosisId": "diagnos2.diagnos", "textId": "diagnos2.text" },
          { "id": "diagnos3", "diagnosisId": "diagnos3.diagnos", "textId": "diagnos3.text" }
        ]
      }
      """;

  private static final String VALUE =
      """
      {
        "type": "DIAGNOSIS_WITH_TEXT_LIST",
        "list": [
          { "type": "DIAGNOSIS_WITH_TEXT", "id": "diagnos1",
            "diagnosis": { "type": "DIAGNOSIS", "id": "diagnos1.diagnos", "terminology": "ICD_10_SE", "code": "M545", "description": "Ländryggssmärta" },
            "text":      { "type": "TEXT",      "id": "diagnos1.text", "text": "2024, vårdcentralen Solna" } },
          { "type": "DIAGNOSIS_WITH_TEXT", "id": "diagnos2",
            "diagnosis": { "type": "DIAGNOSIS", "id": "diagnos2.diagnos", "terminology": "ICD_10_SE", "code": null, "description": null },
            "text":      { "type": "TEXT",      "id": "diagnos2.text", "text": null } },
          { "type": "DIAGNOSIS_WITH_TEXT", "id": "diagnos3",
            "diagnosis": { "type": "DIAGNOSIS", "id": "diagnos3.diagnos", "terminology": "ICD_10_SE", "code": null, "description": null },
            "text":      { "type": "TEXT",      "id": "diagnos3.text", "text": null } }
        ]
      }
      """;

  private final JsonMapper mapper = JsonMapper.builder().build();

  @Test
  void shouldDeserializeConfigAsDiagnosesWithText() {
    final var config =
        (CertificateDataConfigDiagnosesWithText)
            mapper.readValue(CONFIG, CertificateDataConfig.class);

    assertThat(config.getType()).isEqualTo(CertificateDataConfigType.UE_DIAGNOSES_WITH_TEXT);
    assertThat(config.getTextLabel()).isEqualTo("När och var ställdes diagnosen?");
    assertThat(config.getTextLimit()).isEqualTo(50);
    assertThat(config.getTerminology()).hasSize(1);
    assertThat(config.getList())
        .extracting(
            DiagnosesWithTextListItem::getId,
            DiagnosesWithTextListItem::getDiagnosisId,
            DiagnosesWithTextListItem::getTextId)
        .containsExactly(
            tuple("diagnos1", "diagnos1.diagnos", "diagnos1.text"),
            tuple("diagnos2", "diagnos2.diagnos", "diagnos2.text"),
            tuple("diagnos3", "diagnos3.diagnos", "diagnos3.text"));
  }

  @Test
  void shouldDeserializeValueAsRowsOfDiagnosisAndText() {
    final var value =
        (CertificateDataValueDiagnosisWithTextList)
            mapper.readValue(VALUE, CertificateDataValue.class);

    assertThat(value.getType()).isEqualTo(CertificateDataValueType.DIAGNOSIS_WITH_TEXT_LIST);
    assertThat(value.getList())
        .extracting(CertificateDataValueDiagnosisWithText::getId)
        .containsExactly("diagnos1", "diagnos2", "diagnos3");
    assertThat(value.getList())
        .allSatisfy(
            row -> {
              assertThat(row.getType()).isEqualTo(CertificateDataValueType.DIAGNOSIS_WITH_TEXT);
              assertThat(row.getDiagnosis().getType())
                  .isEqualTo(CertificateDataValueType.DIAGNOSIS);
              assertThat(row.getText().getType()).isEqualTo(CertificateDataValueType.TEXT);
            });
    final CertificateDataValueDiagnosisWithText first = value.getList().getFirst();
    assertThat(first.getDiagnosis().getCode()).isEqualTo("M545");
    assertThat(first.getText().getText()).isEqualTo("2024, vårdcentralen Solna");
  }
}
