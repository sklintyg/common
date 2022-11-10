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
import static se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeathSpecification.KRONISK;
import static se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeathSpecification.PLOTSLIG;
import static se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeathSpecification.UPPGIFT_SAKNAS;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.time.LocalDate;
import java.util.Map;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTerminalCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.TerminalCauseOfDeathSpecification;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueTerminalCauseOfDeath;

public class QuestionTerminalDodsorsak {

    public static final short LIMIT = (short) 120;
    public static final short NUMBER_OF_DAYS_IN_FUTURE = (short) 0;
    public static final Map<Specifikation, TerminalCauseOfDeathSpecification> specificationMap = Map.of(Specifikation.KRONISK, KRONISK,
        Specifikation.PLOTSLIG, PLOTSLIG,
        Specifikation.UPPGIFT_SAKNAS, UPPGIFT_SAKNAS);

    public static CertificateDataElement toCertificate(String description, LocalDate date, Specifikation specifikation, int index,
        CertificateTextProvider texts) {

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
                                .specifications(Map.of(
                                    KRONISK, KRONISK.getDescription(),
                                    PLOTSLIG, PLOTSLIG.getDescription(),
                                    UPPGIFT_SAKNAS, UPPGIFT_SAKNAS.getDescription()))
                                .label("A")
                                .text("Beskrivning")
                                .build()
                    )
                    .build()
            )
            .value(
                CertificateDataValueTerminalCauseOfDeath.builder()
                    .id(TERMINAL_DODSORSAK_JSON_ID)
                    .description(description)
                    .date(date)
                    .specification(specifikation != null && specificationMap.containsKey(specifikation)
                        ? specificationMap.get(specifikation) : null)
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
}
