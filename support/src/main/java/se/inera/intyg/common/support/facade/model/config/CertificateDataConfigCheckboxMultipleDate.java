/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleDate.CertificateDataConfigCheckboxMultipleDateBuilder;

@JsonDeserialize(builder = CertificateDataConfigCheckboxMultipleDateBuilder.class)
@Value
@Builder
public class CertificateDataConfigCheckboxMultipleDate implements CertificateDataConfig {

    @Getter(onMethod = @__(@Override))
    CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_DATE;
    @Getter(onMethod = @__(@Override))
    String header;
    @Getter(onMethod = @__(@Override))
    String label;
    @Getter(onMethod = @__(@Override))
    String icon;
    @Getter(onMethod = @__(@Override))
    String text;
    @Getter(onMethod = @__(@Override))
    String description;
    List<CheckboxMultipleDate> list;


    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateDataConfigCheckboxMultipleDateBuilder {

    }
}

