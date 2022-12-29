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

package se.inera.intyg.common.ts_bas.v6.model.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryAlkoholNarkotikaOchLakamedel;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryBedomning;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryDemensOchAndraKognitivaStorningar;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryDiabetes;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryFunktionsnedsattning;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryHjartOchKarlsjukdom;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryHorselOchBalanssinne;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryIdentitet;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryIntygetAvser;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryMedvetandestorning;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryNeurologiskaSjukdomar;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryNjursjukdomar;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryOvrigMedicinering;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryOvrigt;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryPsykiskSjukdomStorning;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategoryPsykiskUtvecklingsstorning;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategorySjukhusvard;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategorySomnOchVakenhetsstorningar;
import se.inera.intyg.common.ts_bas.v6.model.converter.certificate.category.CategorySynfunktioner;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;

@Component(value = "internalToCertificateTsBasV6")
public class InternalToCertificate {

    public Certificate convert(TsBasUtlatandeV6 internalCertificate, CertificateTextProvider texts) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
            .addElement(
                CategoryIntygetAvser.toCertificate(index++, texts)
            )
            .addElement(
                CategoryIdentitet.toCertificate(index++, texts)
            )
            .addElement(
                CategorySynfunktioner.toCertificate(index++, texts)
            )
            .addElement(
                CategoryHorselOchBalanssinne.toCertificate(index++, texts)
            )
            .addElement(
                CategoryFunktionsnedsattning.toCertificate(index++, texts)
            )
            .addElement(
                CategoryHjartOchKarlsjukdom.toCertificate(index++, texts)
            )
            .addElement(
                CategoryDiabetes.toCertificate(index++, texts)
            )
            .addElement(
                CategoryNeurologiskaSjukdomar.toCertificate(index++, texts)
            )
            .addElement(
                CategoryMedvetandestorning.toCertificate(index++, texts)
            )
            .addElement(
                CategoryNjursjukdomar.toCertificate(index++, texts)
            )
            .addElement(
                CategoryDemensOchAndraKognitivaStorningar.toCertificate(index++, texts)
            )
            .addElement(
                CategorySomnOchVakenhetsstorningar.toCertificate(index++, texts)
            )
            .addElement(
                CategoryAlkoholNarkotikaOchLakamedel.toCertificate(index++, texts)
            )
            .addElement(
                CategoryPsykiskSjukdomStorning.toCertificate(index++, texts)
            )
            .addElement(
                CategoryPsykiskUtvecklingsstorning.toCertificate(index++, texts)
            )
            .addElement(
                CategorySjukhusvard.toCertificate(index++, texts)
            )
            .addElement(
                CategoryOvrigMedicinering.toCertificate(index++, texts)
            )
            .addElement(
                CategoryOvrigt.toCertificate(index++, texts)
            )
            .addElement(
                CategoryBedomning.toCertificate(index, texts)
            )
            .build();
    }
}
