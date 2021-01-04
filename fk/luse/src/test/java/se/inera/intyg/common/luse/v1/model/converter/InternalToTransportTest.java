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
package se.inera.intyg.common.luse.v1.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.luse.v1.model.internal.LuseUtlatandeV1;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class InternalToTransportTest {

    private WebcertModuleService webcertModuleService;

    @Before
    public void setup() {
        webcertModuleService = Mockito.mock(WebcertModuleService.class);
        when(webcertModuleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);
    }


    public static LuseUtlatandeV1 getUtlatande() {
        return getUtlatande(null, null, null);
    }

    public static LuseUtlatandeV1 getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        LuseUtlatandeV1.Builder utlatande = LuseUtlatandeV1.builder();
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
        utlatande.setAnnatGrundForMU(new InternalDate("2015-12-07"));
        utlatande.setAnnatGrundForMUBeskrivning("Barndomsvän");
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra")),
            Diagnos.create("S48", "ICD_10_SE", "Klämskada arm", "Klämskada arm")));
        utlatande.setAktivitetsbegransning("Kommer inte in i bilen");
        utlatande.setFormagaTrotsBegransning("Är bra på att dansa!");

        return utlatande.build();
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        LuseUtlatandeV1 expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected, webcertModuleService);
        LuseUtlatandeV1 actual = TransportToInternal.convert(transport.getIntyg());

        // Get diagnos-related svar
        Svar diagnos = transport.getIntyg().getSvar().stream()
            .filter(e -> e.getId().equals("6")).collect(Collectors.toList()).get(0);

        CVType huvuddiagnosKod = extractCVFromSvar(diagnos, "6.2");
        String huvuddiagnosBeskrivning = extractStringFromSvar(diagnos, "6.1");
        CVType bidiagnosKod = extractCVFromSvar(diagnos, "6.4");
        String bidiagnosBeskrivning = extractStringFromSvar(diagnos, "6.3");

        // Ensure huvuddiagnos and bidiagnos are converted as expected
        assertEquals("S47", huvuddiagnosKod.getCode());
        assertEquals("Klämskada skuldra", huvuddiagnosBeskrivning);
        assertEquals("S48", bidiagnosKod.getCode());
        assertEquals("Klämskada arm", bidiagnosBeskrivning);

        assertEquals(expected, actual);
    }

    private CVType extractCVFromSvar(Svar svar, String id) {
        Delsvar delsvar = svar.getDelsvar().stream()
            .filter(e -> e.getId().equals(id)).collect(Collectors.toList()).get(0);

        @SuppressWarnings("unchecked")
        JAXBElement<CVType> meh = (JAXBElement<CVType>) delsvar.getContent().stream().collect(Collectors.toList()).get(0);
        CVType code = meh.getValue();
        return code;
    }

    private String extractStringFromSvar(Svar svar, String id) {
        Delsvar delsvar = svar.getDelsvar().stream()
            .filter(e -> e.getId().equals(id)).collect(Collectors.toList()).get(0);
        return (String) delsvar.getContent().stream().collect(Collectors.toList()).get(0);
    }

    @Test
    public void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        LuseUtlatandeV1 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande, webcertModuleService);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(referensId, transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        LuseUtlatandeV1 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande, webcertModuleService);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertNull(transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaNoRelationTest() throws Exception {
        LuseUtlatandeV1 utlatande = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande, webcertModuleService);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void convertDecorateSvarPaNotKompltTest() throws Exception {
        LuseUtlatandeV1 utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande, webcertModuleService);
        assertNull(transport.getSvarPa());
    }
}
