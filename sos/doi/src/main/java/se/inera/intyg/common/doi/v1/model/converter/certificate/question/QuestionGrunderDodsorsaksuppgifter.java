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
package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAKS_UPPGIFTER_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;

public class QuestionGrunderDodsorsaksuppgifter {


    public static CertificateDataElement toCertificate(List<Dodsorsaksgrund> dodsorsaksgrundList, int index,
        CertificateTextProvider texts) {
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
                CertificateDataValueCodeList.builder()
                    .list(
                        dodsorsaksgrundList.stream()
                            .map(QuestionGrunderDodsorsaksuppgifter::getValueCode)
                            .collect(Collectors.toList())
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDER_DELSVAR_ID)
                        .expression(
                            multipleOrExpressionWithExists(
                                Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN.name(),
                                Dodsorsaksgrund.UNDERSOKNING_EFTER_DODEN.name(),
                                Dodsorsaksgrund.KLINISK_OBDUKTION.name(),
                                Dodsorsaksgrund.RATTSMEDICINSK_OBDUKTION.name(),
                                Dodsorsaksgrund.RATTSMEDICINSK_BESIKTNING.name()
                            )
                        )
                        .build()
                }
            )
            .build();
    }

    private static CertificateDataValueCode getValueCode(Dodsorsaksgrund orsak) {
        return CertificateDataValueCode.builder()
            .id(orsak.name())
            .code(orsak.name())
            .build();
    }

    public static List<Dodsorsaksgrund> toInternal(Certificate certificate) {
        return codeListValue(certificate.getData(), GRUNDER_DELSVAR_ID).stream()
            .map(certificateDataValueCode -> Dodsorsaksgrund.valueOf(certificateDataValueCode.getCode()))
            .collect(Collectors.toList());
    }
}
