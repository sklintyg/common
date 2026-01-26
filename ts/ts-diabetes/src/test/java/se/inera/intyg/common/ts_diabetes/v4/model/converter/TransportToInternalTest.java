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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TransportToInternalTest {

    public static TsDiabetesUtlatandeV4 getUtlatande() {
        TsDiabetesUtlatandeV4.Builder utlatande = TsDiabetesUtlatandeV4.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("4.0");

        utlatande.setAllmant(Allmant.builder().build());
        utlatande.setBedomning(Bedomning.builder().build());
        utlatande.setHypoglykemi(Hypoglykemi.builder().build());
        utlatande.setOvrigt(Ovrigt.builder().build());

        return utlatande.build();
    }

    @BeforeClass
    public static void setUp() {
        final var mapper = mock(UnitMapperUtil.class);

        when(mapper.getMappedUnit(any(), any(), any(), any(), any()))
            .thenAnswer(inv -> new MappedUnit(
                inv.getArgument(0, String.class),
                inv.getArgument(1, String.class),
                inv.getArgument(2, String.class),
                inv.getArgument(3, String.class)
            ));

        new InternalConverterUtil(mapper).initialize();
        new TransportConverterUtil(mapper).initialize();
    }

    @Test
    public void endToEnd() throws Exception {
        TsDiabetesUtlatandeV4 originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        TsDiabetesUtlatandeV4 convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        Assert.assertEquals(originalUtlatande, convertedIntyg);
    }

}