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

import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DESCRIPTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_KRONISK;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_PLOTSLIG;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_UPPGIFT_SAKNAS;

import java.util.List;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CauseOfDeath;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCauseOfDeathList;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCode;

public class QuestionTerminalDodsorsakAndraSjukdomarEllerSkador {

    public static CertificateDataElement toCertificate(Dodsorsak causeOfDeathEmpty, int index, CertificateTextProvider texts) {
        final var specifications = List.of(
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
                .build());

        return CertificateDataElement.builder()
            .id(BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID)
            .index(index)
            .parent(DODSORSAK_SVAR_ID)
            .config(
                CertificateDataConfigCauseOfDeathList.builder()
                    .text(texts.get(BIDRAGANDE_SJUKDOM_OM_QUESTION_TEXT_ID))
                    .description(texts.get(BIDRAGANDE_SJUKDOM_OM_DESCRIPTION_TEXT_ID))
                    .causeOfDeathList(
                        List.of(
                            CauseOfDeath.builder().id("0").descriptionId("description").debutId("debut").specifications(specifications).build(),
                            CauseOfDeath.builder().id("1").descriptionId("description").debutId("debut").specifications(specifications).build(),
                            CauseOfDeath.builder().id("2").descriptionId("description").debutId("debut").specifications(specifications).build(),
                            CauseOfDeath.builder().id("3").descriptionId("description").debutId("debut").specifications(specifications).build(),
                            CauseOfDeath.builder().id("4").descriptionId("description").debutId("debut").specifications(specifications).build(),
                            CauseOfDeath.builder().id("5").descriptionId("description").debutId("debut").specifications(specifications).build(),
                            CauseOfDeath.builder().id("6").descriptionId("description").debutId("debut").specifications(specifications).build(),
                            CauseOfDeath.builder().id("7").descriptionId("description").debutId("debut").specifications(specifications).build()
                        )
                    )
                    .build()
            )
            .build();
    }
}
