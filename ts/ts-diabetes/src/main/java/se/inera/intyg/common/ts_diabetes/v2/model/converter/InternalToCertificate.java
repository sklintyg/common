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

package se.inera.intyg.common.ts_diabetes.v2.model.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategoryIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategoryIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;

@Component(value = "internalToCertificateTsDiabetesV2")
public class InternalToCertificate {

    public Certificate convert(TsDiabetesUtlatandeV2 internalCertificate, CertificateTextProvider textProvider) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, textProvider))
            .addElement(
                CategoryIntygetAvser.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionIntygetAvser.toCertificate(internalCertificate.getIntygAvser(), index++)
            )
            .addElement(
                CategoryIdentitetStyrktGenom.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionIdentitetStyrktGenom.toCertificate(internalCertificate.getVardkontakt(), index++)
            )
            .build();
    }

}
