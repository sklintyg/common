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
package se.inera.intyg.common.ts_diabetes.v3.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synfunktion;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {BefattningService.class, UnitMappingConfigLoader.class, UnitMapperUtil.class,
    InternalConverterUtil.class})
class InternalToTransportTest {

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    static TsDiabetesUtlatandeV3 getUtlatande() {
        return getUtlatande(null, null, null);
    }

    static TsDiabetesUtlatandeV3 getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        TsDiabetesUtlatandeV3.Builder utlatande = TsDiabetesUtlatandeV3.builder();
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
        utlatande.setHypoglykemier(Hypoglykemier.builder().build());
        utlatande.setSynfunktion(Synfunktion.builder().build());

        utlatande.setOvrigt("övrigt");

        return utlatande.build();
    }

    @BeforeAll
    static void initUtils() {
        final var mapper = mock(UnitMapperUtil.class);

        when(mapper.getMappedUnit(any(), any(), any(), any(), any()))
            .thenAnswer(inv -> new MappedUnit(
                inv.getArgument(0, String.class),
                inv.getArgument(1, String.class),
                inv.getArgument(2, String.class),
                inv.getArgument(3, String.class)
            ));

        new TransportConverterUtil(mapper).initialize();
    }

    @Test
    void testInternalToTransportConversion() throws Exception {
        TsDiabetesUtlatandeV3 expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        TsDiabetesUtlatandeV3 actual = TransportToInternal.convert(transport.getIntyg());

        Assert.assertEquals(expected, actual);
    }

    @Test
    void testInternalToTransportSourceNull() throws Exception {
        assertThrows(ConverterException.class, () -> InternalToTransport.convert(null));
    }

    @Test
    void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        TsDiabetesUtlatandeV3 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(referensId, transport.getSvarPa().getReferensId());
    }

    @Test
    void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        TsDiabetesUtlatandeV3 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertNull(transport.getSvarPa().getReferensId());
    }

    @Test
    void convertDecorateSvarPaNoRelationTest() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    void convertDecorateSvarPaNotKompltTest() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }
}