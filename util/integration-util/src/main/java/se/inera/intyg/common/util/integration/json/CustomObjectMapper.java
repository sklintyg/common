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
package se.inera.intyg.common.util.integration.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.model.InternalDate;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * Customized Jackson ObjectMapper for the inera-certificate projects.
 *
 * <p>-registers additional serializers and deserializers for date and time types -registers a
 * specialized serializer to represent certificate-specific data as JSON
 *
 * @author andreaskaltenbach
 */
@Component("objectMapper")
public class CustomObjectMapper extends JsonMapper {

  public CustomObjectMapper() {
    super(configure(JsonMapper.builder()));
  }

  private static JsonMapper.Builder configure(JsonMapper.Builder builder) {
    return builder
        // NON_NULL indicates that only properties with non-null values are to be included.
        .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL))
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .addModule(new CustomModule());
  }

  private static final class CustomModule extends SimpleModule {

    private CustomModule() {
      addSerializer(Temporal.class, new TemporalSerializer());
      addDeserializer(Temporal.class, new TemporalDeserializer());

      addSerializer(InternalDate.class, new InternalDateSerializer());
      addDeserializer(InternalDate.class, new InternalDateDeserializer());

      addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
      addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());

      addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
      /*
       * Using a custom crafted deserializer that handles dates
       * on the UTC format. The original LocalDateDeserializer class do not
       * handle the UTC format.
       */
      addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
    }
  }
}
