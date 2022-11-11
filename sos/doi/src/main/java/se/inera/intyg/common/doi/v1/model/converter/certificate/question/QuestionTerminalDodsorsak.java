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

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.time.LocalDate;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTerminalCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeath;
import se.inera.intyg.common.support.facade.model.TerminalCauseOfDeathSpecificationEnum;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueTerminalCauseOfDeath;
import se.inera.intyg.common.support.facade.util.SpecificationToolkit;
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
                CertificateDataConfigTerminalCauseOfDeath.builder()
                    .id(TERMINAL_DODSORSAK_JSON_ID)
                    .text(texts.get(TERMINAL_DODSORSAK_QUESTION_TEXT_ID))
                    .description(texts.get(TERMINAL_DODSORSAK_DESCRIPTION_TEXT_ID))
                    .terminalCauseOfDeath(
                            TerminalCauseOfDeath.builder()
                                .id("A")
                                .specifications(SpecificationToolkit.getAll())
                                .label("A")
                                .text("Beskrivning")
                                .build()
                    )
                    .build()
            )
            .value(
                CertificateDataValueTerminalCauseOfDeath.builder()
                    .id(TERMINAL_DODSORSAK_JSON_ID)
                    .description(dodsorsak.getBeskrivning())
                    .debut(toLocalDate(dodsorsak.getDatum()))
                    .specification(dodsorsak.getSpecifikation() != null
                        ? SpecificationToolkit.get(dodsorsak.getSpecifikation().name()) : null)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DODSORSAK_DELSVAR_ID)
                        .expression(singleExpression(DODSORSAK_DATUM_JSON_ID))
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(TERMINAL_DODSORSAK_QUESTION_TEXT_ID)
                        .limit(LIMIT)
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(DODSORSAK_DATUM_JSON_ID)
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

        final var terminalCauseOfDeath = (CertificateDataValueTerminalCauseOfDeath) certificate.getData().get(DODSORSAK_DELSVAR_ID)
            .getValue();
        final var description = terminalCauseOfDeath.getDescription();
        final var debut = terminalCauseOfDeath.getDebut() != null ? new InternalDate(terminalCauseOfDeath.getDebut()) : null;
        final var specifikation = terminalCauseOfDeath.getSpecification() != null
            ? getSpecifikation(TerminalCauseOfDeathSpecificationEnum.fromValue(terminalCauseOfDeath.getSpecification().getId())) : null;

        return Dodsorsak.create(description, debut, specifikation);
    }

    private static Specifikation getSpecifikation(TerminalCauseOfDeathSpecificationEnum specification) {
        switch (specification) {
            case PLOTSLIG:
                return Specifikation.PLOTSLIG;
            case KRONISK:
                return Specifikation.KRONISK;
            case UPPGIFT_SAKNAS:
                return Specifikation.UPPGIFT_SAKNAS;
            default:
                return null;
        }
    }

    private static LocalDate toLocalDate(InternalDate internalDate) {
        return (internalDate != null && internalDate.isValidDate()) ? internalDate.asLocalDate() : null;
    }
}
