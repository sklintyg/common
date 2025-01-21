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

package se.inera.intyg.common.support.modules.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component
public class SummaryConverter {

    private static final Logger LOG = LoggerFactory.getLogger(SummaryConverter.class);

    public CertificateSummary convert(ModuleApi moduleApi, Intyg intyg) {
        try {
            final var summaryLabel = moduleApi.getAdditionalInfoLabel();
            final var summaryValue = moduleApi.getAdditionalInfo(intyg);

            if (summaryLabelIsNullOrEmpty(summaryLabel) || summaryValueIsNullOrEmpty(summaryValue)) {
                return CertificateSummary.builder().build();
            }

            return CertificateSummary.builder()
                .value(summaryValue)
                .label(summaryLabel)
                .build();

        } catch (Exception e) {
            LOG.error("Could not convert to summary", e);
            return CertificateSummary.builder().build();
        }
    }

    private static boolean summaryLabelIsNullOrEmpty(String summaryLabel) {
        return summaryLabel == null || summaryLabel.isEmpty();
    }

    private static boolean summaryValueIsNullOrEmpty(String summaryValue) {
        return summaryValue == null || summaryValue.isEmpty();
    }
}
