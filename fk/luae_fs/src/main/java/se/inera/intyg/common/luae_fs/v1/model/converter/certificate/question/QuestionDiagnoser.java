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
package se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question;


import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_SVAR_TEXT_ID;

import java.util.List;
import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionDiagnoser;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

public class QuestionDiagnoser extends AbstractQuestionDiagnoser {

    public static CertificateDataElement toCertificate(List<Diagnos> diagnoser, int index, CertificateTextProvider textProvider) {
        return toCertificate(diagnoser, DIAGNOS_SVAR_ID_6, DIAGNOS_CATEGORY_ID, DIAGNOS_SVAR_TEXT_ID, null, null, index, textProvider);
    }

    public static List<Diagnos> toInternal(Certificate certificate, WebcertModuleService webcertModuleService) {
        return toInternal(certificate, DIAGNOS_SVAR_ID_6, webcertModuleService);
    }
}
