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

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAKS_UPPGIFTER_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_QUESTION_TEXT_ID;

import java.util.Arrays;
import java.util.List;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;

public class QuestionGrunderDodsorsaksuppgifter {


    public static CertificateDataElement toCertificate(List<Dodsorsaksgrund> dodsorsaksgrund, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(GRUNDER_DELSVAR_ID)
            .parent(DODSORSAKS_UPPGIFTER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(GRUNDER_QUESTION_TEXT_ID))
                    .list(
                        Arrays.asList(
                            CheckboxMultipleCode.builder()
                                .id(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN.name())
                                .label(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN.getBeskrivning())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(Dodsorsaksgrund.UNDERSOKNING_EFTER_DODEN.name())
                                .label(Dodsorsaksgrund.UNDERSOKNING_EFTER_DODEN.getBeskrivning())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(Dodsorsaksgrund.KLINISK_OBDUKTION.name())
                                .label(Dodsorsaksgrund.KLINISK_OBDUKTION.getBeskrivning())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION.name())
                                .label(Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION.getBeskrivning())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(Dodsorsaksgrund.RATTSMEDICINSK_BESIKTNING.name())
                                .label(Dodsorsaksgrund.RATTSMEDICINSK_BESIKTNING.getBeskrivning())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                null
//                dodsorsaksgrund != null ? (CertificateDataValue) dodsorsaksgrund.stream().map(orsak -> getValueCode(orsak)).collect(
//                    Collectors.toList())
//                    : CertificateDataValueCode.builder().build()
            )
            .validation(
                null
            )
            .build();
    }

    private static CertificateDataValue getValueCode(Dodsorsaksgrund orsak) {
        return CertificateDataValueCode.builder()
            .id(orsak.name())
            .code(orsak.name())
            .build();
    }
}
