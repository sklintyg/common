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
package se.inera.intyg.common.af00213.v1.model.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TransportToInternalTest {

    public static Af00213UtlatandeV1 getUtlatande() {
        Af00213UtlatandeV1.Builder utlatande = Af00213UtlatandeV1.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");
        utlatande.setHarFunktionsnedsattning(true);
        utlatande.setFunktionsnedsattning("Funktionsnedsättning");

        utlatande.setHarAktivitetsbegransning(true);
        utlatande.setAktivitetsbegransning("Väldigt sjuk");

        utlatande.setHarUtredningBehandling(true);
        utlatande.setUtredningBehandling("Medicin");

        utlatande.setHarArbetetsPaverkan(true);
        utlatande.setArbetetsPaverkan("Halt");

        utlatande.setOvrigt("Trevlig kille");
        return utlatande.build();
    }

    @Test
    public void endToEnd() throws Exception {
        Af00213UtlatandeV1 originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        Af00213UtlatandeV1 convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

}
