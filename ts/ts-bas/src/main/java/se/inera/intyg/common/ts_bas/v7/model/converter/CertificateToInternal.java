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
package se.inera.intyg.common.ts_bas.v7.model.converter;

import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULINBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KOSTBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TABLETTBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.BINOCULAR;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.LEFT_EYE;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.RIGHT_EYE;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBalansrubbningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBedomningKorkortsTyp;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBedomningLakareSpecialKompetens;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBeskrivningRiskfaktorer;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAdhdAddDampAsbergersTourettes;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaJournaluppgifter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaLakarordinerat;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaOrdineratLakamedel;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaProvtagning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaVardinsatser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDubbelseende;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionFunktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionFunktionsnedsattningBeskrivning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHarDiabetes;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHjarnskadaEfterTrauma;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHjartOchKarlsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKognitivFormoga;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKorrektionsglasensStyrka;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionMedvetandestorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionMedvetandestorningBeskrivning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNattblindhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNedsattNjurfunktion;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNystagmus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionOtillrackligRorelseFormoga;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionProgressivOgonsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionRiskfaktorerForStroke;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionPsykiskSjukdomStorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionPsykiskUtvecklingsstorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSomnOchVakenhetsstorningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionStadigvarandeMedicinering;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionStadigvarandeMedicineringBeskrivning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynfaltsdefekter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpaSkickasSeparat;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTeckenPaNeurologiskSjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTidpunktVardPaSjukhus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionUppfattaSamtal4Meter;
import se.inera.intyg.common.ts_bas.v7.model.internal.Bedomning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardatsPaSjukhus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardatsPaSjukhusOrsak;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardinrattningensNamn;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_bas.v7.model.internal.Funktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.internal.HjartKarl;
import se.inera.intyg.common.ts_bas.v7.model.internal.HorselBalans;
import se.inera.intyg.common.ts_bas.v7.model.internal.Kognitivt;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medicinering;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medvetandestorning;
import se.inera.intyg.common.ts_bas.v7.model.internal.Neurologi;
import se.inera.intyg.common.ts_bas.v7.model.internal.Njurar;
import se.inera.intyg.common.ts_bas.v7.model.internal.NarkotikaLakemedel;
import se.inera.intyg.common.ts_bas.v7.model.internal.Psykiskt;
import se.inera.intyg.common.ts_bas.v7.model.internal.Sjukhusvard;
import se.inera.intyg.common.ts_bas.v7.model.internal.SomnVakenhet;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.common.ts_bas.v7.model.internal.Utvecklingsstorning;

@Component(value = "certificateToInternalTsBas")
public class CertificateToInternal {

    public TsBasUtlatandeV7 convert(Certificate certificate, TsBasUtlatandeV7 internalCertificate) {
        return TsBasUtlatandeV7.builder()
            .setId(internalCertificate.getId())
            .setTextVersion(internalCertificate.getTextVersion())
            .setGrundData(internalCertificate.getGrundData())
            .setIntygAvser(QuestionIntygetAvser.toInternal(certificate))
            .setSyn(Syn.builder()
                .setSynfaltsdefekter(QuestionSynfaltsdefekter.toInternal(certificate))
                .setNattblindhet(QuestionNattblindhet.toInternal(certificate))
                .setProgressivOgonsjukdom(QuestionProgressivOgonsjukdom.toInternal(certificate))
                .setDiplopi(QuestionDubbelseende.toInternal(certificate))
                .setNystagmus(QuestionNystagmus.toInternal(certificate))
                .setSynskarpaSkickasSeparat(QuestionSynskarpaSkickasSeparat.toInternal(certificate))
                .setKorrektionsglasensStyrka(QuestionKorrektionsglasensStyrka.toInternal(certificate))
                .setHogerOga(QuestionSynskarpa.toInternal(certificate, RIGHT_EYE))
                .setVansterOga(QuestionSynskarpa.toInternal(certificate, LEFT_EYE))
                .setBinokulart(QuestionSynskarpa.toInternal(certificate, BINOCULAR))
                .build())
            .setSomnVakenhet(SomnVakenhet.create(QuestionSomnOchVakenhetsstorningar.toInternal(certificate)))
            .setNarkotikaLakemedel(NarkotikaLakemedel.builder()
                .setTeckenMissbruk(QuestionAlkoholNarkotikaJournaluppgifter.toInternal(certificate))
                .setForemalForVardinsats(QuestionAlkoholNarkotikaVardinsatser.toInternal(certificate))
                .setProvtagningBehovs(QuestionAlkoholNarkotikaProvtagning.toInternal(certificate))
                .setLakarordineratLakemedelsbruk(QuestionAlkoholNarkotikaLakarordinerat.toInternal(certificate))
                .setLakemedelOchDos(QuestionAlkoholNarkotikaOrdineratLakamedel.toInternal(certificate))
                .build())
            .setPsykiskt(Psykiskt.create(QuestionPsykiskSjukdomStorning.toInternal(certificate)))
            .setUtvecklingsstorning(Utvecklingsstorning.builder()
                .setPsykiskUtvecklingsstorning(QuestionPsykiskUtvecklingsstorning.toInternal(certificate))
                .setHarSyndrom(QuestionAdhdAddDampAsbergersTourettes.toInternal(certificate))
                .build())
            .setVardkontakt(QuestionIdentitetStyrktGenom.toInternal(certificate))
            .setHorselBalans(
                HorselBalans.builder()
                    .setBalansrubbningar(QuestionBalansrubbningar.toInternal(certificate))
                    .setSvartUppfattaSamtal4Meter(QuestionUppfattaSamtal4Meter.toInternal(certificate))
                    .build()
            )
            .setFunktionsnedsattning(
                Funktionsnedsattning.builder()
                    .setFunktionsnedsattning(QuestionFunktionsnedsattning.toInternal(certificate))
                    .setBeskrivning(QuestionFunktionsnedsattningBeskrivning.toInternal(certificate))
                    .setOtillrackligRorelseformaga(QuestionOtillrackligRorelseFormoga.toInternal(certificate))
                    .build()
            )
            .setHjartKarl(
                HjartKarl.builder()
                    .setHjartKarlSjukdom(QuestionHjartOchKarlsjukdom.toInternal(certificate))
                    .setHjarnskadaEfterTrauma(QuestionHjarnskadaEfterTrauma.toInternal(certificate))
                    .setRiskfaktorerStroke(QuestionRiskfaktorerForStroke.toInternal(certificate))
                    .setBeskrivningRiskfaktorer(QuestionBeskrivningRiskfaktorer.toInternal(certificate))
                    .build()
            )
            .setDiabetes(
                Diabetes.builder()
                    .setHarDiabetes(QuestionHarDiabetes.toInternal(certificate))
                    .setDiabetesTyp(QuestionDiabetesTyp.toInternal(certificate))
                    .setKost(QuestionDiabetesBehandling.toInternal(certificate, KOSTBEHANDLING_DELSVAR_JSON_ID))
                    .setTabletter(QuestionDiabetesBehandling.toInternal(certificate, TABLETTBEHANDLING_DELSVAR_JSON_ID))
                    .setInsulin(QuestionDiabetesBehandling.toInternal(certificate, INSULINBEHANDLING_DELSVAR_JSON_ID))
                    .build()
            )
            .setNeurologi(
                Neurologi.create(QuestionTeckenPaNeurologiskSjukdom.toInternal(certificate))
            )
            .setMedvetandestorning(
                Medvetandestorning.builder()
                    .setMedvetandestorning(QuestionMedvetandestorning.toInternal(certificate))
                    .setBeskrivning(QuestionMedvetandestorningBeskrivning.toInternal(certificate))
                    .build()
            )
            .setNjurar(
                Njurar.create(QuestionNedsattNjurfunktion.toInternal(certificate))
            )
            .setKognitivt(
                Kognitivt.create(QuestionKognitivFormoga.toInternal(certificate))
            )
            .setBedomning(
                Bedomning.builder()
                    .setKorkortstyp(QuestionBedomningKorkortsTyp.toInternal(certificate))
                    .setLakareSpecialKompetens(QuestionBedomningLakareSpecialKompetens.toInternal(certificate))
                    .build()
            )
            .setSjukhusvard(
                Sjukhusvard.builder()
                    .setSjukhusEllerLakarkontakt(QuestionVardatsPaSjukhus.toInternal(certificate))
                    .setTidpunkt(QuestionTidpunktVardPaSjukhus.toInternal(certificate))
                    .setVardinrattning(QuestionVardinrattningensNamn.toInternal(certificate))
                    .setAnledning(QuestionVardatsPaSjukhusOrsak.toInternal(certificate))
                    .build()
            )
            .setMedicinering(
                Medicinering.builder()
                    .setStadigvarandeMedicinering(QuestionStadigvarandeMedicinering.toInternal(certificate))
                    .setBeskrivning(QuestionStadigvarandeMedicineringBeskrivning.toInternal(certificate))
                    .build()
            )
            .setKommentar(
                QuestionOvrigt.toInternal(certificate)
            )
            .build();
    }
}
