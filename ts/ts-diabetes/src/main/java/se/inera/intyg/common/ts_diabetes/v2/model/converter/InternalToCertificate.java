/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategoryAllmant;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategoryBedomning;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategoryHypoglykemi;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategoryIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategoryIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategoryOvrigt;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.category.CategorySyn;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionBedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionBedomningKorkortstypHeader;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionBedomningLakareSpecialKompetens;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionBedomningLamplighetInnehaBehorighet;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionDiabetesAnnanBehandlingBeskrivning;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionDiabetesDiagnosAr;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionDiabetesInsulinBehandlingsperiod;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiAllvarligForekomst;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiAllvarligForekomstBeskrivning;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiAllvarligForekomstTrafiken;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiAllvarligForekomstTrafikenBeskrivning;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiAllvarligForekomstVakenTid;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiAllvarligForekomstVakenTidObservationstid;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiEgenkontrollBlodsocker;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiKunskapOmAtgarder;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiSaknarFormagaKannaVarningstecken;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionHypoglykemiTeckenNedsattHjarnfunktion;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionOvrigtKommentarer;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionSynDubbelseende;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionSynSeparatOgonlakarintyg;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionSynSynskarpa;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question.QuestionSynUtanAnmarkning;
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
            .addElement(
                CategoryAllmant.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionDiabetesDiagnosAr.toCertificate(internalCertificate.getDiabetes().getObservationsperiod(), index++, textProvider)
            )
            .addElement(
                QuestionDiabetesTyp.toCertificate(internalCertificate.getDiabetes().getDiabetestyp(), index++, textProvider)
            )
            .addElement(
                QuestionDiabetesBehandling.toCertificate(internalCertificate.getDiabetes().getEndastKost(),
                    internalCertificate.getDiabetes().getTabletter(), internalCertificate.getDiabetes().getInsulin(), index++, textProvider)
            )
            .addElement(
                QuestionDiabetesInsulinBehandlingsperiod.toCertificate(internalCertificate.getDiabetes().getInsulinBehandlingsperiod(),
                    index++, textProvider)
            )
            .addElement(
                QuestionDiabetesAnnanBehandlingBeskrivning.toCertificate(internalCertificate.getDiabetes().getAnnanBehandlingBeskrivning(),
                    index++, textProvider)
            )
            .addElement(
                CategoryHypoglykemi.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiKunskapOmAtgarder.toCertificate(internalCertificate.getHypoglykemier().getKunskapOmAtgarder(), index++,
                    textProvider)
            )
            .addElement(
                QuestionHypoglykemiTeckenNedsattHjarnfunktion.toCertificate(
                    internalCertificate.getHypoglykemier().getTeckenNedsattHjarnfunktion(), index++,
                    textProvider)
            )
            .addElement(
                QuestionHypoglykemiSaknarFormagaKannaVarningstecken.toCertificate(
                    internalCertificate.getHypoglykemier().getSaknarFormagaKannaVarningstecken(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiAllvarligForekomst.toCertificate(internalCertificate.getHypoglykemier().getAllvarligForekomst(), index++,
                    textProvider)
            )
            .addElement(
                QuestionHypoglykemiAllvarligForekomstBeskrivning.toCertificate(
                    internalCertificate.getHypoglykemier().getAllvarligForekomstBeskrivning(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiAllvarligForekomstTrafiken.toCertificate(
                    internalCertificate.getHypoglykemier().getAllvarligForekomstTrafiken(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiAllvarligForekomstTrafikenBeskrivning.toCertificate(
                    internalCertificate.getHypoglykemier().getAllvarligForekomstTrafikBeskrivning(),
                    index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiEgenkontrollBlodsocker.toCertificate(internalCertificate.getHypoglykemier().getEgenkontrollBlodsocker(),
                    index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiAllvarligForekomstVakenTid.toCertificate(
                    internalCertificate.getHypoglykemier().getAllvarligForekomstVakenTid(), index++, textProvider)
            )
            .addElement(
                QuestionHypoglykemiAllvarligForekomstVakenTidObservationstid.toCertificate(
                    internalCertificate.getHypoglykemier().getAllvarligForekomstVakenTidObservationstid(), index++, textProvider)
            )
            .addElement(
                CategorySyn.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionSynSeparatOgonlakarintyg.toCertificate(internalCertificate.getSyn().getSeparatOgonlakarintyg(), index++,
                    textProvider)
            )
            .addElement(
                QuestionSynUtanAnmarkning.toCertificate(internalCertificate.getSyn().getSynfaltsprovningUtanAnmarkning(), index++,
                    textProvider)
            )
            .addElement(
                QuestionSynSynskarpa.toCertificate(internalCertificate.getSyn().getHoger(), internalCertificate.getSyn().getVanster(),
                    internalCertificate.getSyn().getBinokulart(), index++, textProvider)
            )
            .addElement(
                QuestionSynDubbelseende.toCertificate(internalCertificate.getSyn().getDiplopi(), index++, textProvider)
            )
            .addElement(
                CategoryBedomning.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionBedomningKorkortstypHeader.toCertificate(index++)
            )
            .addElement(
                QuestionBedomningKorkortstyp.toCertificate(internalCertificate.getBedomning(), index++, textProvider)
            )
            .addElement(
                QuestionBedomningLakareSpecialKompetens.toCertificate(internalCertificate.getBedomning().getLakareSpecialKompetens(),
                    index++, textProvider)
            )
            .addElement(
                QuestionBedomningLamplighetInnehaBehorighet.toCertificate(
                    internalCertificate.getBedomning().getLamplighetInnehaBehorighet(), index++, textProvider)
            )
            .addElement(
                CategoryOvrigt.toCertificate(index++, textProvider)
            )
            .addElement(
                QuestionOvrigtKommentarer.toCertificate(internalCertificate.getKommentarer(), index)
            )
            .build();
    }

}
