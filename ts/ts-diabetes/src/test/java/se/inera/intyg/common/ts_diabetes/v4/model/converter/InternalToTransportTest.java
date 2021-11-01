/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class InternalToTransportTest {

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    public static TsDiabetesUtlatandeV4 getUtlatande() {
        return getUtlatande(null, null, null);
    }

    public static TsDiabetesUtlatandeV4 getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        TsDiabetesUtlatandeV4.Builder utlatande = TsDiabetesUtlatandeV4.builder();
        utlatande.setId("1234567");
        utlatande.setTextVersion("1.0");
        GrundData grundData = IntygTestDataBuilder.getGrundData();

        grundData.setSigneringsdatum(LocalDateTime.parse("2015-12-07T15:48:05"));

        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationKod(relationKod);
            relation.setMeddelandeId(relationMeddelandeId);
            relation.setReferensId(referensId);
            grundData.setRelation(relation);
        }
        utlatande.setGrundData(grundData);

        utlatande.setAllmant(Allmant.builder().build());
        utlatande.setBedomning(Bedomning.builder().build());
        utlatande.setHypoglykemi(Hypoglykemi.builder().build());
        utlatande.setOvrigt("övrigt");

        return utlatande.build();
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        TsDiabetesUtlatandeV4 expected = getUtlatande();
        RegisterCertificateType transport = se.inera.intyg.common.ts_diabetes.v4.model.converter.InternalToTransport.convert(expected);
        TsDiabetesUtlatandeV4 actual = TransportToInternal.convert(transport.getIntyg());

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ConverterException.class)
    public void testInternalToTransportSourceNull() throws Exception {
        se.inera.intyg.common.ts_diabetes.v4.model.converter.InternalToTransport.convert(null);
    }

    @Test
    public void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        TsDiabetesUtlatandeV4 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = se.inera.intyg.common.ts_diabetes.v4.model.converter.InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(referensId, transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        TsDiabetesUtlatandeV4 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = se.inera.intyg.common.ts_diabetes.v4.model.converter.InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertNull(transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaNoRelationTest() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = getUtlatande();
        RegisterCertificateType transport = se.inera.intyg.common.ts_diabetes.v4.model.converter.InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void convertDecorateSvarPaNotKompltTest() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }
}
