/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;


import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;

import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionIntygetBaseratPa;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionIntygetBaseratPa extends AbstractQuestionIntygetBaseratPa {

    public static CertificateDataElement toCertificate(LisjpUtlatandeV1 internalCertificate, int index,
        CertificateTextProvider textProvider) {
        return toCertificate(internalCertificate.getUndersokningAvPatienten(), internalCertificate.getTelefonkontaktMedPatienten(),
            internalCertificate.getJournaluppgifter(), internalCertificate.getAnnatGrundForMU(), GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1,
            GRUNDFORMU_CATEGORY_ID, index, GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID, GRUNDFORMEDICINSKTUNDERLAG_SVAR_DESCRIPTION_ID,
            textProvider);
    }

    public static InternalDate toInternal(Certificate certificate, String itemId) {
        return toInternal(certificate, GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, itemId);
    }
}
