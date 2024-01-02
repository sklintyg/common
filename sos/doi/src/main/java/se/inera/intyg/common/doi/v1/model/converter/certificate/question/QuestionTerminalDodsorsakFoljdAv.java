/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_AV_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_B_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_C_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_D_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_KRONISK;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_PLOTSLIG;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_CATEGORY_ID;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CodeItem;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionTerminalDodsorsakFoljdAv {

    public static final short LIMIT = (short) 80;

    public static CertificateDataElement toCertificate(
        Dodsorsak terminalDodsorsak,
        int index,
        CertificateTextProvider texts,
        String questionId,
        String label
    ) {
        return CertificateDataElement.builder()
            .id(questionId)
            .index(index)
            .parent(TERMINAL_DODSORSAK_CATEGORY_ID)
            .config(CertificateDataConfigCauseOfDeath.builder()
                .text(texts.get(FOLJD_AV_QUESTION_TEXT_ID))
                .label(label)
                .causeOfDeath(
                    CauseOfDeath.builder()
                        .id(FOLJD_JSON_ID)
                        .maxDate(LocalDate.now())
                        .descriptionId(FOLJD_JSON_ID + "[" + getElementId(questionId) + "].beskrivning")
                        .debutId(FOLJD_JSON_ID + "[" + getElementId(questionId) + "].datum")
                        .specifications(List.of(
                            CodeItem.builder()
                                .id(Specifikation.PLOTSLIG.name())
                                .label(FOLJD_OM_DELSVAR_PLOTSLIG)
                                .code(Specifikation.PLOTSLIG.name())
                                .build(),
                            CodeItem.builder()
                                .id(Specifikation.KRONISK.name())
                                .label(FOLJD_OM_DELSVAR_KRONISK)
                                .code(Specifikation.KRONISK.name())
                                .build(),
                            CodeItem.builder()
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
                            .id(FOLJD_JSON_ID + "[" + getElementId(questionId) + "].beskrivning")
                            .text(terminalDodsorsak.getBeskrivning())
                            .build()
                    )
                    .debut(
                        CertificateDataValueDate.builder()
                            .id(FOLJD_JSON_ID + "[" + getElementId(questionId) + "].datum")
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
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(FOLJD_JSON_ID + "[" + getElementId(questionId) + "].beskrivning")
                        .limit(LIMIT)
                        .build()
                }
            )
            .build();
    }

    private static LocalDate toLocalDate(InternalDate internalDate) {
        return (internalDate != null && internalDate.isValidDate()) ? internalDate.asLocalDate() : null;
    }

    private static int getElementId(String questionId) {
        switch (questionId) {
            case FOLJD_OM_DELSVAR_B_ID:
                return 0;
            case FOLJD_OM_DELSVAR_C_ID:
                return 1;
            case FOLJD_OM_DELSVAR_D_ID:
                return 2;
            default:
                throw new RuntimeException("No questionId match the given id: " + questionId);
        }
    }

    public static List<Dodsorsak> toInternal(Certificate certificate, List<String> questionIds) {
        final List<Dodsorsak> dodsorsakList = questionIds.stream()
            .filter(questionId -> certificate.getData().containsKey(questionId))
            .map(questionId -> (CertificateDataValueCauseOfDeath) certificate.getData().get(questionId).getValue())
            .map(causeOfDeath -> Dodsorsak.create(
                causeOfDeath.getDescription().getText(),
                getDebut(causeOfDeath),
                getSpecification(causeOfDeath)
            ))
            .collect(Collectors.toList());

        removeEmptyValuesIfAtEndOfList(dodsorsakList);

        return dodsorsakList;
    }

    private static Specifikation getSpecification(CertificateDataValueCauseOfDeath causeOfDeath) {
        return causeOfDeath.getSpecification().getCode() != null && !causeOfDeath.getSpecification().getCode().isEmpty()
            ? Specifikation.fromValue(causeOfDeath.getSpecification().getCode()) : null;
    }

    private static InternalDate getDebut(CertificateDataValueCauseOfDeath causeOfDeath) {
        return causeOfDeath.getDebut().getDate() != null ? new InternalDate(causeOfDeath.getDebut().getDate()) : null;
    }

    private static void removeEmptyValuesIfAtEndOfList(List<Dodsorsak> dodsorsakList) {
        for (int i = dodsorsakList.size() - 1; i >= 0; i--) {
            if (hasValue(dodsorsakList.get(i))) {
                break;
            } else {
                dodsorsakList.remove(i);
            }
        }
    }

    private static boolean hasValue(Dodsorsak dodsorsak) {
        return (dodsorsak.getBeskrivning() != null && !dodsorsak.getBeskrivning().isEmpty())
            || dodsorsak.getDatum() != null || dodsorsak.getSpecifikation() != null;
    }
}
