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

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ATGARDER_CATEGORY_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;

public class QuestionAtgarder extends AbstractQuestionAtgarder {

    public static CertificateDataElement toCertificate(List<ArbetslivsinriktadeAtgarder> atgarder, int index,
        CertificateTextProvider texts) {
        final var questionAtgarderValueManager = new QuestionAtgarderConfigProvider(
            getCheckboxMultipleCodeList(),
            getMandatoryValidationIds(),
            getDisableAndSubElementValidationIds(),
            getArbetslivsinriktadeAtgarderValue(atgarder),
            ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId());
        return toCertificate(questionAtgarderValueManager, ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, ATGARDER_CATEGORY_ID, index, texts);
    }


    public static List<ArbetslivsinriktadeAtgarder> toInternal(Certificate certificate) {
        return toInternal(certificate, ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
    }

    private static List<QuestionAtgarderValueProvider> getArbetslivsinriktadeAtgarderValue(List<ArbetslivsinriktadeAtgarder> atgarder) {
        if (atgarder == null) {
            return Collections.emptyList();
        }
        return atgarder.stream()
            .map(QuestionAtgarder::atgardToArbetslivsinriktadeAtgarderValue)
            .collect(Collectors.toList());
    }

    private static QuestionAtgarderValueProvider atgardToArbetslivsinriktadeAtgarderValue(ArbetslivsinriktadeAtgarder atgarder) {
        return new QuestionAtgarderValueProvider(atgarder.getTyp().getId());
    }

    private static String[] getDisableAndSubElementValidationIds() {
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

    private static String[] getMandatoryValidationIds() {
        return new String[]{
            ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId(),
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

    private static List<CheckboxMultipleCode> getCheckboxMultipleCodeList() {
        return Arrays.asList(
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId())
                .label(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId())
                .label(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId())
                .label(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getId())
                .label(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId())
                .label(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId())
                .label(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId())
                .label(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getId())
                .label(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId())
                .label(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId())
                .label(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getLabel())
                .build(),
            CheckboxMultipleCode.builder()
                .id(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                .label(ArbetslivsinriktadeAtgarderVal.OVRIGT.getLabel())
                .build()
        );
    }
}
