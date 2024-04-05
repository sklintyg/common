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
package se.inera.intyg.common.fkparent.testability;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosis;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueMedicalInvestigation;

public final class FkDataValueUtil {

    public static CertificateDataValueDiagnosisList getDataValueMinimalDiagnosisListFk(String id, Diagnos diagnos) {
        final var certificateDataValueDiagnosis = CertificateDataValueDiagnosis.builder()
            .id(id)
            .code(diagnos.getDiagnosKod())
            .terminology(diagnos.getDiagnosKodSystem())
            .description(diagnos.getDiagnosBeskrivning())
            .build();
        return CertificateDataValueDiagnosisList.builder()
            .list(
                List.of(
                    certificateDataValueDiagnosis
                )
            )
            .build();
    }

    public static CertificateDataValueDiagnosisList getDataValueMaximalDiagnosisListFk(List<String> ids, List<Diagnos> diagnosis) {
        final var certificateDataValueDiagnoses = new ArrayList<CertificateDataValueDiagnosis>();
        for (int i = 0; i < diagnosis.size(); i++) {
            certificateDataValueDiagnoses.add(
                CertificateDataValueDiagnosis.builder()
                    .id(ids.get(i))
                    .code(diagnosis.get(i).getDiagnosKod())
                    .terminology(diagnosis.get(i).getDiagnosKodSystem())
                    .description(diagnosis.get(i).getDiagnosBeskrivning())
                    .build()
            );
        }
        return CertificateDataValueDiagnosisList.builder()
            .list(
                certificateDataValueDiagnoses
            )
            .build();
    }

    public static CertificateDataValueMedicalInvestigation getValueMedicalInvestigation(String jsonId, int id, String code, String text) {
        return CertificateDataValueMedicalInvestigation.builder()
            .date(
                CertificateDataValueDate.builder()
                    .id(jsonId + "[" + id + "].datum")
                    .date(LocalDate.now()).build()
            )
            .investigationType(
                CertificateDataValueCode.builder()
                    .id(jsonId + "[" + id + "].typ")
                    .code(code)
                    .build()
            )
            .informationSource(
                CertificateDataValueText.builder()
                    .id(jsonId + "[" + id + "].hamtasFran")
                    .text(text)
                    .build()
            )
            .build();
    }
}
