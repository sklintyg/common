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

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ATGARDER_CATEGORY_ID;

import java.util.List;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.AbstractQuestionAtgarder;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionAtgarder extends AbstractQuestionAtgarder {

    public static CertificateDataElement toCertificate(List<ArbetslivsinriktadeAtgarder> atgarder, int index,
        CertificateTextProvider texts) {
        return toCertificate(atgarder, ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, ATGARDER_CATEGORY_ID, index, texts);
    }

    public static List<ArbetslivsinriktadeAtgarder> toInternal(Certificate certificate) {
        return toInternal(certificate, ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
    }
}
