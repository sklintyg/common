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

import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_JSON_ID;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionBedomningOvrigaKommentarer;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionBedomningUppfyllerBehorighetskrav;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBehandlingAnnan;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBeskrivningAnnanTyp;
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
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;
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
                    .setBeskrivningAnnanTypAvDiabetes(QuestionDiabetesBeskrivningAnnanTyp.toInternal(certificate))
                    .setMedicineringForDiabetes(QuestionDiabetesHarMedicinering.toInternal(certificate))
                    .setMedicineringMedforRiskForHypoglykemi(QuestionDiabetesMedicineringHypoglykemiRisk.toInternal(certificate))
                    .setBehandling(
                        Behandling.builder()
                            .setInsulin(QuestionDiabetesBehandling.toInternal(certificate, ALLMANT_BEHANDLING_INSULIN_JSON_ID))
                            .setTabletter(QuestionDiabetesBehandling.toInternal(certificate, ALLMANT_BEHANDLING_TABLETTER_JSON_ID))
                            .setAnnan(QuestionDiabetesBehandling.toInternal(certificate, ALLMANT_BEHANDLING_ANNAN_JSON_ID))
                            .setAnnanAngeVilken(QuestionDiabetesBehandlingAnnan.toInternal(certificate))
                            .build()
                    )
                    .setMedicineringMedforRiskForHypoglykemiTidpunkt(
                        QuestionDiabetesMedicineringHypoglykemiRiskDatum.toInternal(certificate))
                    .build()
            )
            .setHypoglykemi(
                Hypoglykemi.builder()
                    .setKontrollSjukdomstillstand(QuestionHypoglykemiKontrollSjukdomstillstand.toInternal(certificate))
                    .setKontrollSjukdomstillstandVarfor(QuestionHypoglykemiKontrollSjukdomstillstandVarfor.toInternal(certificate))
                    .setForstarRiskerMedHypoglykemi(QuestionHypoglykemiForstarRiskerMedHypoglykemi.toInternal(certificate))
                    .setFormagaKannaVarningstecken(QuestionHypoglykemiFormagaKannaVarningstecken.toInternal(certificate))
                    .setVidtaAdekvataAtgarder(QuestionHypoglykemiVidtaAdekvataAtgarder.toInternal(certificate))
                    .setAterkommandeSenasteAret(QuestionHypoglykemiAterkommandeSenasteAret.toInternal(certificate))
                    .setAterkommandeSenasteAretTidpunkt(QuestionHypoglykemiAterkommandeSenasteAretTidpunkt.toInternal(certificate))
                    .setAterkommandeSenasteAretKontrolleras(QuestionHypoglykemiAterkommandeSenasteAretKontrolleras.toInternal(certificate))
                    .setAterkommandeSenasteAretTrafik(QuestionHypoglykemiAterkommandeSenasteAretTrafik.toInternal(certificate))
                    .setAterkommandeVaketSenasteTolv(QuestionHypoglykemiAterkommandeVaketSenasteTolv.toInternal(certificate))
                    .setAterkommandeVaketSenasteTre(QuestionHypoglykemiAterkommandeVaketSenasteTre.toInternal(certificate))
                    .setAterkommandeVaketSenasteTreTidpunkt(QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toInternal(certificate))
                    .setAllvarligSenasteTolvManaderna(QuestionHypoglykemiAllvarligSenasteTolvManaderna.toInternal(certificate))
                    .setAllvarligSenasteTolvManadernaTidpunkt(
                        QuestionHypoglykemiAllvarligSenasteTolvManadernaTidpunkt.toInternal(certificate))
                    .setRegelbundnaBlodsockerkontroller(QuestionHypoglykemiRegelbundnaBlodsockerkontroller.toInternal(certificate))
                    .build()
            )
            .setOvrigt(
                Ovrigt.builder()
                    .setKomplikationerAvSjukdomen(QuestionOvrigtKomplikationerAvSjukdomen.toInternal(certificate))
                    .setKomplikationerAvSjukdomenAnges(QuestionOvrigtKomplikationerAvSjukdomenAnges.toInternal(certificate))
                    .setBorUndersokasAvSpecialist(QuestionOvrigtBorUndersokasAvSpecialist.toInternal(certificate))
                    .build()

            )
            .setBedomning(
                Bedomning.builder()
                    .setUppfyllerBehorighetskrav(QuestionBedomningUppfyllerBehorighetskrav.toInternal(certificate))
                    .setOvrigaKommentarer(QuestionBedomningOvrigaKommentarer.toInternal(certificate))
                    .build()
            )
            .build();
    }
}
