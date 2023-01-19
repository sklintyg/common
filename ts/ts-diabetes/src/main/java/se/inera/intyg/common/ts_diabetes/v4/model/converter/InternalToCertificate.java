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
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.category.CategoryAllmant;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.category.CategoryBedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.category.CategoryHypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.category.CategoryIdentitet;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.category.CategoryIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.category.CategoryOvrigt;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionBedomningOvrigaKommentarer;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionBedomningUppfyllerBehorighetskrav;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBehandlingAnnan;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBeskrivningAnnanTyp;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesDiagnosAr;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesHarMedicinering;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesMedicineringHypoglykemiRisk;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesMedicineringHypoglykemiRiskDatum;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAllvarligSenasteTolvManaderna;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAllvarligSenasteTolvManadernaTidpunkt;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAret;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretKontrolleras;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretTidpunkt;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretTrafik;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeVaketSenasteTolv;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeVaketSenasteTre;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiFormagaKannaVarningstecken;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiForstarRiskerMedHypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiKontrollSjukdomstillstand;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiKontrollSjukdomstillstandVarfor;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiRegelbundnaBlodsockerkontroller;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiVidtaAdekvataAtgarder;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionOvrigtBorUndersokasAvSpecialist;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionOvrigtKomplikationerAvSjukdomen;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionOvrigtKomplikationerAvSjukdomenAnges;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionPatientenFoljsAv;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;

@Component(value = "internalToCertificateTsDiabetesV4")
public class InternalToCertificate {

    public Certificate convert(TsDiabetesUtlatandeV4 internalCertificate, CertificateTextProvider textProvider) {
        var index = 0;
        return CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(internalCertificate, textProvider))
            .addElement(CategoryIntygetAvser.toCertificate(index++, textProvider))
            .addElement(QuestionIntygetAvser.toCertificate(internalCertificate.getIntygAvser(), index++, textProvider))
            .addElement(CategoryIdentitet.toCertificate(index++, textProvider))
            .addElement(QuestionIdentitetStyrktGenom.toCertificate(internalCertificate.getIdentitetStyrktGenom(), index++, textProvider))
            .addElement(CategoryAllmant.toCertificate(index++, textProvider))
            .addElement(QuestionPatientenFoljsAv.toCertificate(internalCertificate.getAllmant(), index++, textProvider))
            .addElement(QuestionDiabetesDiagnosAr.toCertificate(internalCertificate.getAllmant(),
                internalCertificate.getGrundData().getPatient().getPersonId(), index++, textProvider))
            .addElement(QuestionDiabetesTyp.toCertificate(internalCertificate.getAllmant(), index++, textProvider))
            .addElement(QuestionDiabetesBeskrivningAnnanTyp.toCertificate(internalCertificate.getAllmant(), index++, textProvider))
            .addElement(QuestionDiabetesHarMedicinering.toCertificate(internalCertificate.getAllmant(), index++, textProvider))
            .addElement(QuestionDiabetesMedicineringHypoglykemiRisk.toCertificate(internalCertificate.getAllmant(), index++, textProvider))
            .addElement(QuestionDiabetesBehandling.toCertificate(internalCertificate.getAllmant(), index++, textProvider))
            .addElement(QuestionDiabetesBehandlingAnnan.toCertificate(internalCertificate.getAllmant(), index++, textProvider))
            .addElement(
                QuestionDiabetesMedicineringHypoglykemiRiskDatum.toCertificate(internalCertificate.getAllmant(), index++, textProvider))
            .addElement(CategoryHypoglykemi.toCertificate(index++, textProvider))
            .addElement(QuestionHypoglykemiKontrollSjukdomstillstand.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiKontrollSjukdomstillstandVarfor.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiForstarRiskerMedHypoglykemi.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiFormagaKannaVarningstecken.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiVidtaAdekvataAtgarder.toCertificate(internalCertificate.getHypoglykemi(), index++, textProvider))
            .addElement(QuestionHypoglykemiAterkommandeSenasteAret.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiAterkommandeSenasteAretTidpunkt.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiAterkommandeSenasteAretKontrolleras.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiAterkommandeSenasteAretTrafik.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiAterkommandeVaketSenasteTolv.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiAterkommandeVaketSenasteTre.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiAllvarligSenasteTolvManaderna.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(QuestionHypoglykemiAllvarligSenasteTolvManadernaTidpunkt.toCertificate(internalCertificate.getHypoglykemi(),
                index++, textProvider))
            .addElement(QuestionHypoglykemiRegelbundnaBlodsockerkontroller.toCertificate(internalCertificate.getHypoglykemi(), index++,
                textProvider))
            .addElement(CategoryOvrigt.toCertificate(index++, textProvider))
            .addElement(QuestionOvrigtKomplikationerAvSjukdomen.toCertificate(internalCertificate.getOvrigt(), index++, textProvider))
            .addElement(QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(internalCertificate.getOvrigt(), index++, textProvider))
            .addElement(QuestionOvrigtBorUndersokasAvSpecialist.toCertificate(internalCertificate.getOvrigt(), index++, textProvider))
            .addElement(CategoryBedomning.toCertificate(index++, textProvider))
            .addElement(QuestionBedomningUppfyllerBehorighetskrav.toCertificate(internalCertificate.getBedomning(), index++, textProvider))
            .addElement(QuestionBedomningOvrigaKommentarer.toCertificate(internalCertificate.getBedomning(), index, textProvider))
            .build();
    }
}
