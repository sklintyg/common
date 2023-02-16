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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_TEXT;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionAtgarderBeskrivning {

    public static CertificateDataElement toCertificate(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44)
            .index(index)
            .parent(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_TEXT))
                    .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpression(
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId()),
                                singleExpression(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                            )
                        )
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44,
            ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44);
    }
}
