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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;

import java.util.List;
import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionUnderlag;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionUnderlag extends AbstractQuestionUnderlag {

    public static CertificateDataElement toCertificate(List<Underlag> underlag, int index, CertificateTextProvider texts) {
        return toCertificate(underlag, null, UNDERLAG_SVAR_ID_4, GRUNDFORMU_CATEGORY_ID, index,
            texts);
    }

    public static List<Underlag> toInternal(Certificate certificate) {
        return toInternal(certificate, UNDERLAG_SVAR_ID_4);
    }
}
