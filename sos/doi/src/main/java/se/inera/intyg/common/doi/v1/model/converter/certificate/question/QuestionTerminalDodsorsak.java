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

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_A_LABEL;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_KRONISK;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_PLOTSLIG;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.time.LocalDate;
import java.util.List;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CodeItem;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCauseOfDeath;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionTerminalDodsorsak {

    public static final short LIMIT = (short) 120;
    public static final short NUMBER_OF_DAYS_IN_FUTURE = (short) 0;

    public static CertificateDataElement toCertificate(Dodsorsak dodsorsak, int index, CertificateTextProvider texts) {

        return CertificateDataElement.builder()
            .id(DODSORSAK_DELSVAR_ID)
            .index(index)
            .parent(DODSORSAK_SVAR_ID)
            .config(
                CertificateDataConfigCauseOfDeath.builder()
                    .text(texts.get(TERMINAL_DODSORSAK_QUESTION_TEXT_ID))
                    .description(texts.get(TERMINAL_DODSORSAK_DESCRIPTION_TEXT_ID))
                    .label(FOLJD_OM_DELSVAR_A_LABEL)
                    .causeOfDeath(
                        CauseOfDeath.builder()
                            .id(TERMINAL_DODSORSAK_JSON_ID)
                            .descriptionId(DODSORSAK_DELSVAR_ID)
                            .debutId(DODSORSAK_DATUM_DELSVAR_ID)
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
                            .build()
                    )
                    .build()
            )
            .value(
                CertificateDataValueCauseOfDeath.builder()
                    .id(TERMINAL_DODSORSAK_JSON_ID)
                    .description(
                        CertificateDataTextValue.builder()
                            .id(DODSORSAK_DELSVAR_ID)
                            .text(dodsorsak.getBeskrivning())
                            .build()
                    )
                    .debut(
                        CertificateDataValueDate.builder()
                            .id(DODSORSAK_DATUM_DELSVAR_ID)
                            .date(toLocalDate(dodsorsak.getDatum()))
                            .build()
                    )
                    .specification(
                        dodsorsak.getSpecifikation() != null
                            ? CertificateDataValueCode.builder()
                            .id(dodsorsak.getSpecifikation().name())
                            .code(dodsorsak.getSpecifikation().name())
                            .build()
                            : CertificateDataValueCode.builder().build()
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DODSORSAK_DELSVAR_ID)
                        .expression(singleExpression(TERMINAL_DODSORSAK_JSON_ID))
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(DODSORSAK_DELSVAR_ID)
                        .limit(LIMIT)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(DODSORSAK_DATUM_DELSVAR_ID)
                        .numberOfDays(NUMBER_OF_DAYS_IN_FUTURE)
                        .build()
                }
            )
            .build();
    }

    public static Dodsorsak toInternal(Certificate certificate) {
        if (certificate.getData().get(DODSORSAK_DELSVAR_ID) == null) {
            return null;
        }

        final var terminalCauseOfDeath = (CertificateDataValueCauseOfDeath) certificate.getData().get(DODSORSAK_DELSVAR_ID)
            .getValue();
        final var description = terminalCauseOfDeath.getDescription().getText();
        final var debut = terminalCauseOfDeath.getDebut().getDate() != null
            ? new InternalDate(terminalCauseOfDeath.getDebut().getDate()) : null;
        final var specifikation = terminalCauseOfDeath.getSpecification().getCode() != null
            ? Specifikation.fromValue(terminalCauseOfDeath.getSpecification().getCode()) : null;

        return Dodsorsak.create(description, debut, specifikation);
    }

    private static LocalDate toLocalDate(InternalDate internalDate) {
        return (internalDate != null && internalDate.isValidDate()) ? internalDate.asLocalDate() : null;
    }
}
