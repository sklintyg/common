/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.facade.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.modules.support.facade.FillType;

public class TestabilityToolkit {

    private static final int DEFAULT_SICK_LEAVE_LENGTH = 14;
    private static final int DEFAULT_SHORT_SICK_LEAVE_LENGTH = 4;

    public static void fillCertificateWithTestData(Certificate certificate, FillType fillType,
        TestabilityTestdataToolkit testabilityTestdataToolkit) {
        if (FillType.MINIMAL.equals(fillType)) {
            final var minimumValuesLisjp = testabilityTestdataToolkit.getMinimumValues();
            updateCertificate(certificate, minimumValuesLisjp);
        } else if (FillType.MAXIMAL.equals(fillType)) {
            final var maximumValuesLisjp = testabilityTestdataToolkit.getMaximumValues();
            updateCertificate(certificate, maximumValuesLisjp);
        }
    }

    public static void updateCertificate(Certificate certificate, Map<String, CertificateDataValue> valueMap) {
        valueMap.forEach((key, value) -> {
            final var certificateDataElement = certificate.getData().get(key);
            final var updatedCertificateDataElement = CertificateDataElement.builder()
                .id(certificateDataElement.getId())
                .parent(certificateDataElement.getParent())
                .index(certificateDataElement.getIndex())
                .config(certificateDataElement.getConfig())
                .validation(certificateDataElement.getValidation())
                .value(value)
                .build();
            certificate.getData().put(key, updatedCertificateDataElement);
        });
    }

    public static CertificateDataValueDiagnosisList getCertificateDataValueDiagnosisList() {
        return CertificateDataValueDiagnosisList.builder()
            .list(
                Collections.singletonList(
                    CertificateDataValueDiagnosis.builder()
                        .id("1")
                        .terminology("ICD_10_SE")
                        .code("J09")
                        .description("Influensa orsakad av identifierat zoonotiskt eller pandemiskt influensavirus")
                        .build()
                )
            )
            .build();
    }

    public static CertificateDataValueDateRangeList getCertificateDataValueDateRangeList(boolean shortPeriod) {
        return CertificateDataValueDateRangeList.builder()
            .list(
                Collections.singletonList(
                    CertificateDataValueDateRange.builder()
                        .id("HELT_NEDSATT")
                        .from(LocalDate.now())
                        .to(LocalDate.now().plusDays(shortPeriod
                            ? DEFAULT_SHORT_SICK_LEAVE_LENGTH : DEFAULT_SICK_LEAVE_LENGTH)
                        )
                        .build()
                )
            )
            .build();
    }

    public static CertificateDataValueDateRangeList getCertificateDataValueDateRangeListWithSeveralPeriods(boolean shortPeriod) {
        return CertificateDataValueDateRangeList.builder()
            .list(
                Arrays.asList(
                    CertificateDataValueDateRange.builder()
                        .id("HELT_NEDSATT")
                        .from(LocalDate.now())
                        .to(LocalDate.now().plusDays(shortPeriod
                            ? DEFAULT_SHORT_SICK_LEAVE_LENGTH : DEFAULT_SICK_LEAVE_LENGTH)
                        )
                        .build(),
                    CertificateDataValueDateRange.builder()
                        .id("TRE_FJARDEDEL")
                        .from(LocalDate.now().plusWeeks(1))
                        .to(LocalDate.now().plusWeeks(1).plusDays(1))
                        .build()
                )
            )
            .build();
    }
}
