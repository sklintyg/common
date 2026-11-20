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
package se.inera.intyg.common.support.facade.model.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

/** The UE_DOUBLE config body as certificate-service emits it (K1J-2650, decisions D3 and D3a). */
class CertificateDataConfigDoubleJsonTest {

  private static final String CONFIG_WITH_UNIT =
      """
      {
        "type": "UE_DOUBLE",
        "header": null, "icon": null, "accordion": null, "message": null, "description": null,
        "text": "Hur många timmar per dygn?",
        "label": null,
        "id": "timmar",
        "unitOfMeasurement": "timmar",
        "min": 0.1,
        "max": 12.0,
        "maxDecimals": 1
      }
      """;

  private static final String CONFIG_WITHOUT_UNIT =
      """
      {
        "type": "UE_DOUBLE",
        "header": null, "icon": null, "accordion": null, "message": null, "description": null,
        "text": "Ange värdet",
        "label": null,
        "id": "varde",
        "min": 0.1,
        "max": 12.0,
        "maxDecimals": 1
      }
      """;

  private final JsonMapper mapper = JsonMapper.builder().build();

  @Test
  void shouldDeserializeConfigWithUnitAsDouble() {
    final var config =
        (CertificateDataConfigDouble)
            mapper.readValue(CONFIG_WITH_UNIT, CertificateDataConfig.class);

    assertThat(config.getType()).isEqualTo(CertificateDataConfigType.UE_DOUBLE);
    assertThat(config.getText()).isEqualTo("Hur många timmar per dygn?");
    assertThat(config.getId()).isEqualTo("timmar");
    assertThat(config.getUnitOfMeasurement()).isEqualTo("timmar");
    assertThat(config.getMin()).isEqualTo(0.1);
    assertThat(config.getMax()).isEqualTo(12.0);
    assertThat(config.getMaxDecimals()).isEqualTo(1);
  }

  @Test
  void shouldDeserializeConfigWithoutUnitAsDoubleWithNullUnit() {
    final var config =
        (CertificateDataConfigDouble)
            mapper.readValue(CONFIG_WITHOUT_UNIT, CertificateDataConfig.class);

    assertThat(config.getType()).isEqualTo(CertificateDataConfigType.UE_DOUBLE);
    assertThat(config.getId()).isEqualTo("varde");
    assertThat(config.getUnitOfMeasurement()).isNull();
    assertThat(config.getMin()).isEqualTo(0.1);
    assertThat(config.getMax()).isEqualTo(12.0);
    assertThat(config.getMaxDecimals()).isEqualTo(1);
  }
}
