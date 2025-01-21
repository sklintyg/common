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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;

import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionAtgarderBeskrivning extends AbstractQuestionAtgarderBeskrivning {

    public static CertificateDataElement toCertificate(String value, int index,
        CertificateTextProvider texts) {
        final var questionAtgarderBeskrivningValidationProvider = new QuestionAtgarderBeskrivningValidationProvider(
            showValidationIds()
        );

        return toCertificate(value, questionAtgarderBeskrivningValidationProvider, ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44,
            ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40,
            ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44, index, texts);
    }

    private static String[] showValidationIds() {
        return new String[]{
            ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId(),
            ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId(),
            ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getId(),
            ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId(),
            ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId(),
            ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId(),
            ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getId(),
            ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId(),
            ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId(),
            ArbetslivsinriktadeAtgarderVal.OVRIGT.getId()
        };
    }

    public static String toInternal(Certificate certificate) {
        return toInternal(certificate, ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44,
            ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44);
    }
}
