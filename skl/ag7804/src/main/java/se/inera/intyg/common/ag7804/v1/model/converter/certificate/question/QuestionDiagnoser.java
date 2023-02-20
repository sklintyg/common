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
package se.inera.intyg.common.ag7804.v1.model.converter.certificate.question;


import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_DIAGNOS;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NO_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.YES_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;

import java.util.List;
import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionDiagnoser;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationEnable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

public class QuestionDiagnoser extends AbstractQuestionDiagnoser {

    public static CertificateDataElement toCertificate(List<Diagnos> diagnoser, int index, CertificateTextProvider textProvider) {
        return toCertificate(diagnoser, DIAGNOS_SVAR_ID_6, CATEGORY_DIAGNOS, DIAGNOS_SVAR_TEXT, DIAGNOS_SVAR_BESKRIVNING,
            getAdditionalValidations(),
            index,
            textProvider);
    }

    private static List<CertificateDataValidation> getAdditionalValidations() {
        return List.of(
            CertificateDataValidationHide.builder()
                .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
                .expression(exists(NO_ID))
                .build(),
            CertificateDataValidationEnable.builder()
                .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
                .expression(multipleOrExpressionWithExists(YES_ID, NO_ID))
                .build()
        );
    }

    public static List<Diagnos> toInternal(Certificate certificate, WebcertModuleService webcertModuleService) {
        return toInternal(certificate, DIAGNOS_SVAR_ID_6, webcertModuleService);
    }
}
