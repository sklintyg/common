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
package se.inera.intyg.common.ts_bas.v7.model.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryAlkoholNarkotikaOchLakamedel;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryBedomning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryDemensOchAndraKognitivaStorningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryDiabetes;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryFunktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryHjartOchKarlsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryHorselOchBalanssinne;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryIdentitet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryIntygetAvser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryMedvetandestorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryNeurologiskaSjukdomar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryNjursjukdomar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryOvrigMedicinering;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryOvrigt;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryPsykiskSjukdomStorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategoryPsykiskUtvecklingsstorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategorySjukhusvard;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategorySomnOchVakenhetsstorningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.category.CategorySynfunktioner;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAdhdAddDampAsbergersTourettes;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaJournaluppgifter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaLakarordinerat;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaOrdineratLakamedel;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaProvtagning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaProvtagningMessage;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaVardinsatser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBalansrubbningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBedomningKorkortsTyp;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBedomningLakareSpecialKompetens;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBeskrivningRiskfaktorer;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDiabetesTyp;
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
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKorrektionsglasensStyrkaMessage;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionLakarintygAvOgonspecialistMessage;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionMedvetandestorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionMedvetandestorningBeskrivning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNattblindhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNedsattNjurfunktion;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNystagmus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionOtillrackligRorelseFormoga;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionProgressivOgonsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionPsykiskSjukdomStorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionPsykiskUtvecklingsstorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionRiskfaktorerForStroke;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSomnOchVakenhetsstorningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionStadigvarandeMedicinering;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionStadigvarandeMedicineringBeskrivning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynfaltsdefekter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpaSkickasSeparat;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpaSkickasSeparatHeader;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTablettEllerInsulinMessage;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTeckenPaNeurologiskSjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTidigareUtfordUndersokningMessage;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTidpunktVardPaSjukhus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionUppfattaSamtalFyraMeter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardatsPaSjukhus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardatsPaSjukhusOrsak;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardinrattningensNamn;
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
                CategoryIdentitet.toCertificate(index++, texts)
            )
            .addElement(
                QuestionIdentitetStyrktGenom.toCertificate(internalCertificate.getVardkontakt(), index++, texts)
            )
            .addElement(
                CategorySynfunktioner.toCertificate(index++, texts)
            )
            .addElement(
                QuestionSynfaltsdefekter.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionNattblindhet.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionProgressivOgonsjukdom.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionLakarintygAvOgonspecialistMessage.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionDubbelseende.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionNystagmus.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionSynskarpaSkickasSeparatHeader.toCertificate(index++, texts)
            )
            .addElement(
                QuestionSynskarpaSkickasSeparat.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionSynskarpa.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionTidigareUtfordUndersokningMessage.toCertificate(index++, texts)
            )
            .addElement(
                QuestionKorrektionsglasensStyrka.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                QuestionKorrektionsglasensStyrkaMessage.toCertificate(internalCertificate.getSyn(), index++, texts)
            )
            .addElement(
                CategoryHorselOchBalanssinne.toCertificate(index++, texts)
            )
            .addElement(
                QuestionBalansrubbningar.toCertificate(internalCertificate.getHorselBalans(), index++, texts)
            )
            .addElement(
                QuestionUppfattaSamtalFyraMeter.toCertificate(internalCertificate.getHorselBalans(), index++, texts)
            )
            .addElement(
                CategoryFunktionsnedsattning.toCertificate(index++, texts)
            )
            .addElement(
                QuestionFunktionsnedsattning.toCertificate(internalCertificate.getFunktionsnedsattning(), index++, texts)
            )
            .addElement(
                QuestionFunktionsnedsattningBeskrivning.toCertificate(internalCertificate.getFunktionsnedsattning(), index++, texts)
            )
            .addElement(
                QuestionOtillrackligRorelseFormoga.toCertificate(internalCertificate.getFunktionsnedsattning(), index++, texts)
            )
            .addElement(
                CategoryHjartOchKarlsjukdom.toCertificate(index++, texts)
            )
            .addElement(
                QuestionHjartOchKarlsjukdom.toCertificate(internalCertificate.getHjartKarl(), index++, texts)
            )
            .addElement(
                QuestionHjarnskadaEfterTrauma.toCertificate(internalCertificate.getHjartKarl(), index++, texts)
            )
            .addElement(
                QuestionRiskfaktorerForStroke.toCertificate(internalCertificate.getHjartKarl(), index++, texts)
            )
            .addElement(
                QuestionBeskrivningRiskfaktorer.toCertificate(internalCertificate.getHjartKarl(), index++, texts)
            )
            .addElement(
                CategoryDiabetes.toCertificate(index++, texts)
            )
            .addElement(
                QuestionHarDiabetes.toCertificate(internalCertificate.getDiabetes(), index++, texts)
            )
            .addElement(
                QuestionDiabetesTyp.toCertificate(internalCertificate.getDiabetes(), index++, texts)
            )
            .addElement(
                QuestionDiabetesBehandling.toCertificate(internalCertificate.getDiabetes(), index++, texts)
            )
            .addElement(
                QuestionTablettEllerInsulinMessage.toCertificate(internalCertificate.getDiabetes(), index++, texts)
            )
            .addElement(
                CategoryNeurologiskaSjukdomar.toCertificate(index++, texts)
            )
            .addElement(
                QuestionTeckenPaNeurologiskSjukdom.toCertificate(internalCertificate.getNeurologi(), index++, texts)
            )
            .addElement(
                CategoryMedvetandestorning.toCertificate(index++, texts)
            )
            .addElement(
                QuestionMedvetandestorning.toCertificate(internalCertificate.getMedvetandestorning(), index++, texts)
            )
            .addElement(
                QuestionMedvetandestorningBeskrivning.toCertificate(internalCertificate.getMedvetandestorning(), index++, texts)
            )
            .addElement(
                CategoryNjursjukdomar.toCertificate(index++, texts)
            )
            .addElement(
                QuestionNedsattNjurfunktion.toCertificate(internalCertificate.getNjurar(), index++, texts)
            )
            .addElement(
                CategoryDemensOchAndraKognitivaStorningar.toCertificate(index++, texts)
            )
            .addElement(
                QuestionKognitivFormoga.toCertificate(internalCertificate.getKognitivt(), index++, texts)
            )
            .addElement(
                CategorySomnOchVakenhetsstorningar.toCertificate(index++, texts)
            )
            .addElement(
                QuestionSomnOchVakenhetsstorningar.toCertificate(internalCertificate.getSomnVakenhet(), index++, texts)
            )
            .addElement(
                CategoryAlkoholNarkotikaOchLakamedel.toCertificate(index++, texts)
            )
            .addElement(
                QuestionAlkoholNarkotikaJournaluppgifter.toCertificate(internalCertificate.getNarkotikaLakemedel(), index++, texts)
            )
            .addElement(
                QuestionAlkoholNarkotikaVardinsatser.toCertificate(internalCertificate.getNarkotikaLakemedel(), index++, texts)
            )
            .addElement(
                QuestionAlkoholNarkotikaProvtagning.toCertificate(internalCertificate.getNarkotikaLakemedel(), index++, texts)
            )
            .addElement(
                QuestionAlkoholNarkotikaProvtagningMessage.toCertificate(internalCertificate.getNarkotikaLakemedel(), index++, texts)
            )
            .addElement(
                QuestionAlkoholNarkotikaLakarordinerat.toCertificate(internalCertificate.getNarkotikaLakemedel(), index++, texts)
            )
            .addElement(
                QuestionAlkoholNarkotikaOrdineratLakamedel.toCertificate(internalCertificate.getNarkotikaLakemedel(), index++, texts)
            )
            .addElement(
                CategoryPsykiskSjukdomStorning.toCertificate(index++, texts)
            )
            .addElement(
                QuestionPsykiskSjukdomStorning.toCertificate(internalCertificate.getPsykiskt(), index++, texts)
            )
            .addElement(
                CategoryPsykiskUtvecklingsstorning.toCertificate(index++, texts)
            )
            .addElement(
                QuestionPsykiskUtvecklingsstorning.toCertificate(internalCertificate.getUtvecklingsstorning(), index++, texts)
            )
            .addElement(
                QuestionAdhdAddDampAsbergersTourettes.toCertificate(internalCertificate.getUtvecklingsstorning(), index++, texts)
            )
            .addElement(
                CategorySjukhusvard.toCertificate(index++, texts)
            )
            .addElement(
                QuestionVardatsPaSjukhus.toCertificate(internalCertificate.getSjukhusvard(), index++, texts)
            )
            .addElement(
                QuestionTidpunktVardPaSjukhus.toCertificate(internalCertificate.getSjukhusvard(), index++, texts)
            )
            .addElement(
                QuestionVardinrattningensNamn.toCertificate(internalCertificate.getSjukhusvard(), index++, texts)
            )
            .addElement(
                QuestionVardatsPaSjukhusOrsak.toCertificate(internalCertificate.getSjukhusvard(), index++, texts)
            )
            .addElement(
                CategoryOvrigMedicinering.toCertificate(index++, texts)
            )
            .addElement(
                QuestionStadigvarandeMedicinering.toCertificate(internalCertificate.getMedicinering(), index++, texts)
            )
            .addElement(
                QuestionStadigvarandeMedicineringBeskrivning.toCertificate(internalCertificate.getMedicinering(), index++, texts)
            )
            .addElement(
                CategoryOvrigt.toCertificate(index++, texts)
            )
            .addElement(
                QuestionOvrigt.toCertificate(internalCertificate.getKommentar(), index++, texts)
            )
            .addElement(
                CategoryBedomning.toCertificate(index++, texts)
            )
            .addElement(
                QuestionBedomningKorkortsTyp.toCertificate(internalCertificate.getBedomning(), index++, texts)
            )
            .addElement(
                QuestionBedomningLakareSpecialKompetens.toCertificate(internalCertificate.getBedomning(), index, texts)
            )
            .build();
    }
}
