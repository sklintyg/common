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

package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionPatientenFoljsAv;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;

@Component(value = "certificateToInternalTsDiabetesV4")
public class CertificateToInternal {

    public TsDiabetesUtlatandeV4 convert(Certificate certificate, TsDiabetesUtlatandeV4 internalCertificate) {
        return TsDiabetesUtlatandeV4.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(MetaDataGrundData.toInternal(certificate.getMetadata(), internalCertificate.getGrundData()))
            .setIntygAvser(QuestionIntygetAvser.toInternal(certificate))
            .setIdentitetStyrktGenom(QuestionIdentitetStyrktGenom.toInternal(certificate))
            .setAllmant(
                Allmant.builder()
                    .setPatientenFoljsAv(QuestionPatientenFoljsAv.toInternal(certificate))
                    .setTypAvDiabetes(QuestionDiabetesTyp.toInternal(certificate))
                    .build()
            )
            .build();
    }
}
