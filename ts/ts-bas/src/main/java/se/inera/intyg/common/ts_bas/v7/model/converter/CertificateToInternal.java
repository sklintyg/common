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

import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULINBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KOSTBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TABLETTBEHANDLING_DELSVAR_JSON_ID;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBalansrubbningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBeskrivningRiskfaktorer;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDubbelseende;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionFunktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionFunktionsnedsattningBeskrivning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHarDiabetes;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHjarnskadaEfterTrauma;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHjartOchKarlsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKorrektionsglasensStyrka;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNattblindhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNystagmus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionOtillrackligRorelseFormoga;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionProgressivOgonsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionRiskfaktorerForStroke;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynfaltsdefekter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpaSkickasSeparat;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionUppfattaSamtal4Meter;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_bas.v7.model.internal.Funktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.internal.HjartKarl;
import se.inera.intyg.common.ts_bas.v7.model.internal.HorselBalans;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;

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
                .build())
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
            .build();
    }
}
