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

package se.inera.intyg.common.fk7263.model.converter.certificate.question;

import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_ARBETSLOSHET_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_FORALDRALEDIGHET_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_NUVARANDE_ARBETE_TEXT_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETSFORMAGA_SVAR_ID;

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;

public class QuestionPatientensArbetsformogaBedoms {

    public static CertificateDataElement toCertificate(Boolean nuvarandeArbete, Boolean foraldraledighet, Boolean arbetssokande,
        String nuvarandeArbetsuppgifter, int index, CertificateMessagesProvider messagesProvider) {

        return CertificateDataElement.builder()
            .id(PATIENTENS_ARBETSFORMAGA_SVAR_ID)
            .parent(PATIENTENS_ARBETSFORMAGA_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewList.builder()
                    .build()
            )
            .value(
                CertificateDataValueViewList.builder()
                    .list(
                        certificateDataValueViewTextList(nuvarandeArbete, foraldraledighet, arbetssokande, nuvarandeArbetsuppgifter,
                            messagesProvider)
                    )
                    .build()
            )
            .build();
    }

    private static List<CertificateDataValueViewText> certificateDataValueViewTextList(Boolean nuvarandeArbete, Boolean foraldraledighet,
        Boolean arbetssokande, String nuvarandeArbetsuppgifter, CertificateMessagesProvider messagesProvider) {

        List<CertificateDataValueViewText> certificateDataValueViewTextList = new ArrayList<>();

        if (nuvarandeArbete != null && nuvarandeArbete) {
            certificateDataValueViewTextList.add(viewTextWithDynamicValue(nuvarandeArbetsuppgifter, messagesProvider));
        }

        if (arbetssokande != null && arbetssokande) {
            certificateDataValueViewTextList.add(
                CertificateDataValueViewText.builder()
                    .text(messagesProvider.get(PATIENTENS_ARBETSFORMAGA_ARBETSLOSHET_TEXT_ID))
                    .build()
            );
        }

        if (foraldraledighet != null && foraldraledighet) {
            certificateDataValueViewTextList.add(
                CertificateDataValueViewText.builder()
                    .text(messagesProvider.get(PATIENTENS_ARBETSFORMAGA_FORALDRALEDIGHET_TEXT_ID))
                    .build()
            );
        }

        return certificateDataValueViewTextList;
    }

    private static CertificateDataValueViewText viewTextWithDynamicValue(String nuvarandeArbetsuppgifter,
        CertificateMessagesProvider messagesProvider) {

        if (nuvarandeArbetsuppgifter == null) {
            return CertificateDataValueViewText.builder()
                .text(messagesProvider.get(PATIENTENS_ARBETSFORMAGA_NUVARANDE_ARBETE_TEXT_ID))
                .build();
        }

        return CertificateDataValueViewText.builder()
            .text(messagesProvider.get(PATIENTENS_ARBETSFORMAGA_NUVARANDE_ARBETE_TEXT_ID) + " - " + nuvarandeArbetsuppgifter)
            .build();
    }
}
