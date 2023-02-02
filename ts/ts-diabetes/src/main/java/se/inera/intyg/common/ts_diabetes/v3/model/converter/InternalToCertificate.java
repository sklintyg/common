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

package se.inera.intyg.common.ts_diabetes.v3.model.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.category.CategoryAllmant;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.category.CategoryBedomning;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.category.CategoryHypoglykemi;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.category.CategoryIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.category.CategoryIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.category.CategoryOvrigt;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.category.CategorySyn;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionBedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionBedomningLakareSpecialKompetens;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionBedomningLamplighetInnehaBehorighet;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionDiabetesAnnanBehandlingBeskrivning;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionDiabetesBeskrivningAnnanTyp;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionDiabetesDiagnosAr;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionDiabetesInsulinBehandlingsperiod;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionDiabetesMedicineringHypoglykemiRisk;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAret;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretTidpunkt;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretTrafik;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretTrafikTidpunkt;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiAterkommandeVaketSenasteTre;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiEgenkontrollBlodsocker;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiFormagaVarningstecken;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiGodtagbarKontroll;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionHypoglykemiTeckenNedsattHjarnfunktion;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionOvrigtKommentarer;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionSynMisstankeOgonsjukdom;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionSynMisstankeOgonsjukdomMessage;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionSynSeparatOgonlakarintyg;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionSynSynskarpa;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question.QuestionSynSynskarpaMessage;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;

@Component(value = "internalToCertificateTsDiabetesV3")
public class InternalToCertificate {

    public Certificate convert(TsDiabetesUtlatandeV3 internalCertificate, CertificateTextProvider textProvider) {
        int index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, textProvider))
            .addElement(
                CategoryIntygetAvser.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionIntygetAvser.toCertificate(internalCertificate.getIntygAvser(), index++, textProvider)
            )
            .addElement(
                CategoryIdentitetStyrktGenom.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionIdentitetStyrktGenom.toCertificate(internalCertificate.getIdentitetStyrktGenom(), index++, textProvider)
            )
            .addElement(
                CategoryAllmant.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionDiabetesDiagnosAr.toCertificate(internalCertificate.getAllmant(), index++, textProvider)
            )
            .addElement(
                QuestionDiabetesTyp.toCertificate(internalCertificate.getAllmant(), index++, textProvider)
            )
            .addElement(
                QuestionDiabetesBeskrivningAnnanTyp.toCertificate(internalCertificate.getAllmant(), index++, textProvider)
            )
            .addElement(
                QuestionDiabetesBehandling.toCertificate(internalCertificate.getAllmant(), index++, textProvider)
            )
            .addElement(
                QuestionDiabetesInsulinBehandlingsperiod.toCertificate(internalCertificate.getAllmant(),
                    index++, textProvider)
            )
            .addElement(
                QuestionDiabetesAnnanBehandlingBeskrivning.toCertificate(internalCertificate.getAllmant(),
                    index++, textProvider)
            )
            .addElement(
                QuestionDiabetesMedicineringHypoglykemiRisk.toCertificate(internalCertificate.getAllmant(), index++, textProvider)
            )
            .addElement(
                CategoryHypoglykemi.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiEgenkontrollBlodsocker.toCertificate(internalCertificate.getHypoglykemier(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiTeckenNedsattHjarnfunktion.toCertificate(internalCertificate.getHypoglykemier(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiGodtagbarKontroll.toCertificate(internalCertificate.getHypoglykemier(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiFormagaVarningstecken.toCertificate(internalCertificate.getHypoglykemier(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiAterkommandeSenasteAret.toCertificate(internalCertificate.getHypoglykemier(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiAterkommandeSenasteAretTidpunkt.toCertificate(internalCertificate.getHypoglykemier(), index++,
                    textProvider)
            )
            .addElement(
                QuestionHypoglykemiAterkommandeVaketSenasteTre.toCertificate(internalCertificate.getHypoglykemier(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(internalCertificate.getHypoglykemier(), index++,
                    textProvider)
            )
            .addElement(
                QuestionHypoglykemiAterkommandeSenasteAretTrafik.toCertificate(internalCertificate.getHypoglykemier(), index++,
                    textProvider)
            )
            .addElement(
                QuestionHypoglykemiAterkommandeSenasteAretTrafikTidpunkt.toCertificate(internalCertificate.getHypoglykemier(), index++,
                    textProvider)
            )
            .addElement(
                CategorySyn.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionSynMisstankeOgonsjukdom.toCertificate(internalCertificate.getSynfunktion(), index++,
                    textProvider)
            )
            .addElement(
                QuestionSynMisstankeOgonsjukdomMessage.toCertificate(internalCertificate.getSynfunktion(), index++, textProvider)
            )
            .addElement(
                QuestionSynSynskarpaMessage.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionSynSeparatOgonlakarintyg.toCertificate(internalCertificate.getSynfunktion(), index++,
                    textProvider)
            )
            .addElement(
                QuestionSynSynskarpa.toCertificate(internalCertificate.getSynfunktion(), index++, textProvider)
            )
            .addElement(
                CategoryOvrigt.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionOvrigtKommentarer.toCertificate(internalCertificate.getOvrigt(), index++, textProvider)
            )
            .addElement(
                CategoryBedomning.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionBedomningKorkortstyp.toCertificate(internalCertificate.getBedomning(), index++, textProvider)
            )
            .addElement(
                QuestionBedomningLamplighetInnehaBehorighet.toCertificate(
                    internalCertificate.getBedomning(), index++, textProvider)
            )
            .addElement(
                QuestionBedomningLakareSpecialKompetens.toCertificate(internalCertificate.getBedomning(), index, textProvider)
            )
            .build();
    }
}
