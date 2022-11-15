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

package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_AV_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_KRONISK;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_PLOTSLIG;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionTerminalDodsorsakFoljdAv {

    public static CertificateDataElement toCertificate(
        Dodsorsak terminalDodsorsak,
        int index,
        CertificateTextProvider texts,
        String questionId,
        String label,
        String dodsOrsakDatumDelsvarId
        ) {
        return CertificateDataElement.builder()
            .id(questionId)
            .index(index)
            .parent(DODSORSAK_SVAR_ID)
            .config(CertificateDataConfigCauseOfDeath.builder()
                .text(texts.get(FOLJD_AV_QUESTION_TEXT_ID))
                .label(label)
                .causeOfDeath(
                    CauseOfDeath.builder()
                    .id(FOLJD_JSON_ID)
                    .descriptionId(questionId)
                    .debutId(dodsOrsakDatumDelsvarId)
                    .specifications(List.of(
                        CertificateDataConfigCode.builder()
                            .id(Specifikation.PLOTSLIG.name())
                            .label(FOLJD_OM_DELSVAR_PLOTSLIG)
                            .code(Specifikation.PLOTSLIG.name())
                            .build(),
                        CertificateDataConfigCode.builder()
                            .id(Specifikation.KRONISK.name())
                            .label(FOLJD_OM_DELSVAR_KRONISK)
                            .code(Specifikation.KRONISK.name())
                            .build(),
                        CertificateDataConfigCode.builder()
                            .id(Specifikation.UPPGIFT_SAKNAS.name())
                            .label(FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS)
                            .code(Specifikation.UPPGIFT_SAKNAS.name())
                            .build()
                    ))
                    .build())
                .build())
            .value(
                CertificateDataValueCauseOfDeath.builder()
                    .id(FOLJD_JSON_ID)
                    .description(
                        CertificateDataTextValue.builder()
                            .id(questionId)
                            .text(terminalDodsorsak.getBeskrivning())
                            .build()
                    )
                    .debut(
                        CertificateDataValueDate.builder()
                            .id(dodsOrsakDatumDelsvarId)
                            .date(toLocalDate(terminalDodsorsak.getDatum()))
                            .build()
                    )
                    .specification(
                        terminalDodsorsak.getSpecifikation() != null
                            ? CertificateDataValueCode.builder()
                            .id(terminalDodsorsak.getSpecifikation().name())
                            .code(terminalDodsorsak.getSpecifikation().name())
                            .build()
                            : CertificateDataValueCode.builder().build()
                    )
                    .build()
            )
            .build();
    }
    private static LocalDate toLocalDate(InternalDate internalDate) {
        return (internalDate != null && internalDate.isValidDate()) ? internalDate.asLocalDate() : null;
    }


    public static List<Dodsorsak> toInternal(Certificate certificate, List<String> questionIds) {
        List<Dodsorsak> dodsorsakList = new ArrayList<>();
        for (String questionId : questionIds) {
            if (certificate.getData().get(questionId) != null){
                final var terminalCauseOfDeath = (CertificateDataValueCauseOfDeath) certificate.getData().get(questionId)
                    .getValue();
                final var description = terminalCauseOfDeath.getDescription().getText();
                final var debut = terminalCauseOfDeath.getDebut().getDate() != null
                    ? new InternalDate(terminalCauseOfDeath.getDebut().getDate()) : null;
                final var specifikation = terminalCauseOfDeath.getSpecification().getCode() != null
                    ? Specifikation.fromValue(terminalCauseOfDeath.getSpecification().getCode()) : null;

                dodsorsakList.add(Dodsorsak.create(description, debut, specifikation));
            }
        }
        return dodsorsakList;
    }
}
