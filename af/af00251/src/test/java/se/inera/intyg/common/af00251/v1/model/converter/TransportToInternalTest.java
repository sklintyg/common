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
package se.inera.intyg.common.af00251.v1.model.converter;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.af00251.v1.model.internal.ArbetsmarknadspolitisktProgram;
import se.inera.intyg.common.af00251.v1.model.internal.BegransningSjukfranvaro;
import se.inera.intyg.common.af00251.v1.model.internal.PrognosAtergang;
import se.inera.intyg.common.af00251.v1.model.internal.Sjukfranvaro;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TransportToInternalTest {

    public static AF00251UtlatandeV1 getUtlatande() {
        AF00251UtlatandeV1.Builder utlatande = AF00251UtlatandeV1.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");
        utlatande.setUndersokningsDatum(new InternalDate(LocalDate.now()));

        utlatande.setArbetsmarknadspolitisktProgram(ArbetsmarknadspolitisktProgram.builder()
            .setMedicinskBedomning("Arbetsprov")
            .setOmfattning(ArbetsmarknadspolitisktProgram.Omfattning.DELTID)
            .setOmfattningDeltid(4)
            .build());
        utlatande.setFunktionsnedsattning("Funktionsnedsättning");
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setHarForhinder(true);

        utlatande.setSjukfranvaro(Lists.newArrayList(
            Sjukfranvaro.builder()
                .setChecked(true)
                .setNiva(4)
                .setPeriod(new InternalLocalDateInterval(
                    new InternalDate(LocalDate.now()),
                    new InternalDate(LocalDate.now()
                        .plusDays(5))))
                .build()));

        utlatande.setBegransningSjukfranvaro(
            BegransningSjukfranvaro.builder()
                .setKanBegransas(true)
                .setBeskrivning("Använd hjälpmedel")
                .build());

        utlatande.setPrognosAtergang(
            PrognosAtergang.builder()
                .setPrognos(PrognosAtergang.Prognos.EJ_MOJLIGT_AVGORA)
                .setAnpassningar("Jobba halvtid")
                .build());

        return utlatande.build();
    }

    @Test
    public void endToEnd() throws Exception {
        AF00251UtlatandeV1 originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);

        AF00251UtlatandeV1 convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

}
