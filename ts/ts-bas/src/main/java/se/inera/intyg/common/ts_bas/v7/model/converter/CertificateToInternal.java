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
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBalansrubbningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDubbelseende;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKorrektionsglasensStyrka;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNattblindhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNystagmus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionProgressivOgonsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynfaltsdefekter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpaSkickasSeparat;
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
                    .build()
            )
            .build();
    }
}
