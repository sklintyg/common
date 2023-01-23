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
package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.KOMPLETTERANDE_PATIENTUPPGIFTER_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionLand {

    public static final short LIMIT = (short) 24;

    public static CertificateDataElement toCertificate(String omLand, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(LAND_DELSVAR_ID)
            .parent(KOMPLETTERANDE_PATIENTUPPGIFTER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextField.builder()
                    .text(texts.get(LAND_QUESTION_TEXT_ID))
                    .id(LAND_JSON_ID)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(LAND_JSON_ID)
                    .text(omLand)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(LAND_JSON_ID)
                        .limit(LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), LAND_DELSVAR_ID, LAND_JSON_ID);
    }
}
