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
package se.inera.intyg.common.ag7804.v1.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDateTime;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.ag7804.v1.rest.Ag7804ModuleApiV1;
import se.inera.intyg.common.agparent.model.converter.RegisterCertificateTestValidator;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class InternalToTransportTest {

    private WebcertModuleService webcertModuleService;

    @Before
    public void setup() {
        webcertModuleService = Mockito.mock(WebcertModuleService.class);
        when(webcertModuleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(anyString())).thenReturn(true);
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    public static Ag7804UtlatandeV1 getUtlatande() {
        return getUtlatande(null, null, null);
    }

    public static Ag7804UtlatandeV1 getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        Ag7804UtlatandeV1.Builder utlatande = Ag7804UtlatandeV1.builder();
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

        utlatande.setTelefonkontaktMedPatienten(new InternalDate("2015-12-08"));
        utlatande.setAnnatGrundForMU(new InternalDate("2015-12-07"));
        utlatande.setAnnatGrundForMUBeskrivning("Barndomsvän");

        return utlatande.build();
    }

    @Test
    public void doSchematronValidationag7804() throws Exception {
        String xmlContents = Resources.toString(getResource("v1/transport/ag7804.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator(Ag7804ModuleApiV1.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        Ag7804UtlatandeV1 expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected, webcertModuleService);
        Ag7804UtlatandeV1 actual = TransportToInternal.convert(transport.getIntyg());

        assertEquals(expected, actual);
    }

    @Test(expected = ConverterException.class)
    public void testInternalToTransportSourceNull() throws Exception {
        InternalToTransport.convert(null, webcertModuleService);
    }

}
