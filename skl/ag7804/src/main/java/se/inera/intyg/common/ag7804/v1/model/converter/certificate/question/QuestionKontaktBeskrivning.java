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

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;

import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.AbstractQuestionKontaktBeskrivning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionKontaktBeskrivning extends AbstractQuestionKontaktBeskrivning {

    public static CertificateDataElement toCertificate(String value, int index,
        CertificateTextProvider texts) {
        return toCertificate(value, ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26, KONTAKT_ONSKAS_SVAR_ID_26,
            ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26, ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT, index, texts);
    }

    public static String toInternal(Certificate certificate) {
        return toInternal(certificate, ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26, ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26);
    }
}
