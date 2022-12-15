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

package se.inera.intyg.common.ts_bas.v7.model.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryHorselOchBalanssinne;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryIntygetAvser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategorySynfunktioner;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBalansrubbningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDubbelseende;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKorrektionsglasensStyrka;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKorrektionsglasensStyrkaMessage;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionLakarintygAvOgonspecialistMessage;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNattblindhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNystagmus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionProgressivOgonsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynfaltsdefekter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpaSkickasSeparat;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTidigareUtfordUndersokningMessage;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;

@Component(value = "internalToCertificateTsBas")
public class InternalToCertificate {

    public Certificate convert(TsBasUtlatandeV7 internalCertificate, CertificateTextProvider texts) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
            .addElement(
                CategoryIntygetAvser.toCertificate(index++, texts)
            )
            .addElement(
                QuestionIntygetAvser.toCertificate(internalCertificate.getIntygAvser(), index++, texts)
            )
            .addElement(
                CategorySynfunktioner.toCertificate(index++, texts)
            )
            .addElement(
                QuestionSynfaltsdefekter.toCertificate(
                    internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionNattblindhet.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionProgressivOgonsjukdom.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionLakarintygAvOgonspecialistMessage.toCertificate(index++, texts)
            )
            .addElement(
                QuestionDubbelseende.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionNystagmus.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionSynskarpaSkickasSeparat.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionTidigareUtfordUndersokningMessage.toCertificate(index++, texts)
            )
            .addElement(
                QuestionKorrektionsglasensStyrka.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionKorrektionsglasensStyrkaMessage.toCertificate(index++, texts)
            )
            .addElement(
                CategoryHorselOchBalanssinne.toCertificate(index++, texts)
            )
            .addElement(
                QuestionBalansrubbningar.toCertificate(internalCertificate.getHorselBalans(), index, texts)
            )
            .build();
    }
}
