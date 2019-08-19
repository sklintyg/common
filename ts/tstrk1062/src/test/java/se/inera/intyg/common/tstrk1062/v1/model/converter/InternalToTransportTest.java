/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

import static junit.framework.TestCase.*;

import java.time.LocalDateTime;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(MockitoJUnitRunner.class)
public class InternalToTransportTest {

    @Mock
    private WebcertModuleService webcertModuleService;

    @Test(expected = ConverterException.class)
    public void testConvertSourceNull() throws Exception {
        InternalToTransport.convert(null, webcertModuleService);
    }

    @Test
    public void testConvertSourceWithoutMessage() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = getUtlatande();

        RegisterCertificateType tsTsrk1062 = InternalToTransport.convert(utlatande, webcertModuleService);

        assertNotNull("RegisterCertificateType should not be null", tsTsrk1062);
        assertNotNull("Intyg should not be null", tsTsrk1062.getIntyg());
        assertNull("SvarPa should be null", tsTsrk1062.getSvarPa());
    }

    @Test
    public void testConvertSourceWithMessage() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = getUtlatande();

        final Relation relation = new Relation();
        relation.setRelationKod(RelationKod.KOMPLT);
        relation.setMeddelandeId("MeddelandeId");
        relation.setRelationIntygsId("RelationsId");
        relation.setReferensId("ReferensId");

        utlatande.getGrundData().setRelation(relation);

        RegisterCertificateType tsTsrk1062 = InternalToTransport.convert(utlatande, webcertModuleService);

        assertNotNull("RegisterCertificateType should not be null", tsTsrk1062);
        assertNotNull("Intyg should not be null", tsTsrk1062.getIntyg());
        assertNotNull("SvarPa should not be null", tsTsrk1062.getSvarPa());
        assertEquals("MeddelandeId not equal", relation.getMeddelandeId(), tsTsrk1062.getSvarPa().getMeddelandeId());
        assertEquals("ReferensId not equal", relation.getReferensId(), tsTsrk1062.getSvarPa().getReferensId());
    }

    private TsTrk1062UtlatandeV1 getUtlatande() {
        final TsTrk1062UtlatandeV1.Builder builderTemplate = TsTrk1062UtlatandeV1.builder()
            .setGrundData(buildGrundData(LocalDateTime.now()));
        return builderTemplate.build();
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("vardgivareNamn");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("0112312313");

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId("HSAID_123");
        skapadAv.setFullstandigtNamn("Torsten Ericsson");

        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        patient.setPostadress("postadress");
        patient.setPostnummer("11111");
        patient.setPostort("postort");

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }
}
