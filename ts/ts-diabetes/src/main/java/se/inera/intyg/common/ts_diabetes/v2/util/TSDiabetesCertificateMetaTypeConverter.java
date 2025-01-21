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
package se.inera.intyg.common.ts_diabetes.v2.util;

import com.google.common.base.Splitter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;
import se.inera.intygstjanster.ts.services.v1.IntygStatus;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public final class TSDiabetesCertificateMetaTypeConverter {

    private TSDiabetesCertificateMetaTypeConverter() {
    }

    public static CertificateMetaData toCertificateMetaData(IntygMeta intygMeta, TSDiabetesIntyg tsDiabetesIntyg) {
        CertificateMetaData metaData = new CertificateMetaData();
        metaData.setCertificateId(tsDiabetesIntyg.getIntygsId());
        metaData.setCertificateType(tsDiabetesIntyg.getIntygsTyp());
        metaData.setIssuerName(tsDiabetesIntyg.getGrundData().getSkapadAv().getFullstandigtNamn());
        metaData.setFacilityName(tsDiabetesIntyg.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        metaData.setSignDate(
            LocalDateTime.parse(tsDiabetesIntyg.getGrundData().getSigneringsTidstampel(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        metaData.setAdditionalInfo(convertFromKodverkValues(intygMeta.getAdditionalInfo()));
        metaData.setAvailable("true".equals(intygMeta.getAvailable().toLowerCase()));
        List<Status> statuses = toStatusList(intygMeta.getStatus());
        metaData.setStatus(statuses);
        return metaData;
    }

    /**
     * TsDiabetes 2.x IntygMeta.additionalInfo is stored when registered in Intygstjansten by
     * se.inera.intyg.common.ts_diabetes.v2.util.ConverterUtil#toCertificateHolder as a list of raw KV values
     * (se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvserKategori), and not their displaynames.
     * To be backwards compatible with existing data, we keep that behaviour and transform the values here when fetching
     * data.
     *
     * @return Commaseparated string of intygAvser descriptions
     */
    private static String convertFromKodverkValues(String additionalInfo) {
        if (additionalInfo != null) {
            List<String> codes = Splitter.on(',').trimResults().splitToList(additionalInfo);
            return codes.stream().map(c -> getIntygAvserDescription(c)).collect(Collectors.joining(", "));
        }
        return "";
    }

    private static String getIntygAvserDescription(String intygAvserCode) {
        try {
            return IntygAvserKod.valueOf(intygAvserCode).getDescription();
        } catch (IllegalArgumentException iie) {
            // Fall back: return code as-is.
            return intygAvserCode;
        }
    }

    public static List<Status> toStatusList(List<IntygStatus> certificateStatuses) {
        List<Status> statuses = certificateStatuses != null ? new ArrayList<>(certificateStatuses.size())
            : Collections.<Status>emptyList();
        if (certificateStatuses != null) {
            for (IntygStatus certificateStatus : certificateStatuses) {
                if (certificateStatus != null) {
                    statuses.add(toStatus(certificateStatus));
                }
            }
        }
        return statuses;
    }

    public static Status toStatus(IntygStatus certificateStatus) {
        return new Status(CertificateState.valueOf(certificateStatus.getType().value()),
            certificateStatus.getTarget(),
            LocalDateTime.parse(certificateStatus.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
