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
package se.inera.intyg.common.tstrk1009.v1.model.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import java.util.EnumSet;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.tstrk1009.v1.model.internal.AnmalanAvser;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IdKontroll;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IdentitetStyrktGenom;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IntygetAvser;
import se.inera.intyg.common.tstrk1009.v1.model.internal.KorkortBehorighetGrupp;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsolamplighet;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TransportToInternalTest {

    private Tstrk1009UtlatandeV1 getUtlatande() {
        return Tstrk1009UtlatandeV1.builder()
            .setId("1234567")
            .setGrundData(IntygTestDataBuilder.getGrundData())
            .setTextVersion("1.0")
            .setIdentitetStyrktGenom(IdentitetStyrktGenom.create(IdKontroll.ID_KORT))
            .setAnmalanAvser(AnmalanAvser.create(Korkortsolamplighet.OLAMPLIGHET))
            .setMedicinskaForhallanden("sjukt sjuk")
            .setSenasteUndersokningsdatum(new InternalDate("2018-11-11"))
            .setIntygetAvserBehorigheter(IntygetAvser.create(EnumSet.of(KorkortBehorighetGrupp.A_B_TRAKTOR)))
            .build();
    }

    @Test
    public void endToEnd() throws Exception {
        Tstrk1009UtlatandeV1 originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        Tstrk1009UtlatandeV1 convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }
}
