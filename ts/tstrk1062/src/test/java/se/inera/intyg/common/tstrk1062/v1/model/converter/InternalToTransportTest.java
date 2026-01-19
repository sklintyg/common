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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedCareProvider;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {BefattningService.class, UnitMappingConfigLoader.class, UnitMapperUtil.class,
    InternalConverterUtil.class})
class InternalToTransportTest {

    @Mock
    private WebcertModuleService webcertModuleService;

    @BeforeAll
    static void initUtils() {
        final var mapper = mock(UnitMapperUtil.class);

        when(mapper.getMappedCareprovider(any(), any()))
            .thenAnswer(inv -> new MappedCareProvider(
                inv.getArgument(0, String.class),
                inv.getArgument(1, String.class)
            ));

        new TransportConverterUtil(mapper).initialize();
    }

    @Test
    void testConvertSourceNull() {
        assertThrows(ConverterException.class, () -> InternalToTransport.convert(null, webcertModuleService));
    }

    @Test
    void testConvertSourceWithoutMessage() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = getUtlatande();

        RegisterCertificateType tsTsrk1062 = InternalToTransport.convert(utlatande, webcertModuleService);

        assertNotNull(tsTsrk1062, "RegisterCertificateType should not be null");
        Assertions.assertNotNull(tsTsrk1062.getIntyg(), "Intyg should not be null");
        assertNull(tsTsrk1062.getSvarPa(), "SvarPa should be null");
    }

    @Test
    void testConvertSourceWithMessage() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = getUtlatande();

        final Relation relation = new Relation();
        relation.setRelationKod(RelationKod.KOMPLT);
        relation.setMeddelandeId("MeddelandeId");
        relation.setRelationIntygsId("RelationsId");
        relation.setReferensId("ReferensId");

        utlatande.getGrundData().setRelation(relation);

        RegisterCertificateType tsTsrk1062 = InternalToTransport.convert(utlatande, webcertModuleService);

        assertNotNull(tsTsrk1062, "RegisterCertificateType should not be null");
        assertNotNull(tsTsrk1062.getIntyg(), "Intyg should not be null");
        assertNotNull(tsTsrk1062.getSvarPa(), "SvarPa should not be null");
        assertEquals(relation.getMeddelandeId(), tsTsrk1062.getSvarPa().getMeddelandeId(), "MeddelandeId not equal");
        assertEquals(relation.getReferensId(), tsTsrk1062.getSvarPa().getReferensId(), "ReferensId not equal");
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