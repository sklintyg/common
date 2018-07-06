/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00213.model.converter;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;
import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import se.inera.intyg.common.af_parent.model.converter.RegisterCertificateTestValidator;
import se.inera.intyg.common.af00213.model.internal.Af00213Utlatande;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class InternalToTransportTest {

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    public static Af00213Utlatande getUtlatande() {
        return getUtlatande(null, null, null);
    }

    public static Af00213Utlatande getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        Af00213Utlatande.Builder utlatande = Af00213Utlatande.builder();
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

        utlatande.setHarFunktionsnedsattning(true);
        utlatande.setFunktionsnedsattning("Haltar när han dansar");

        utlatande.setHarAktivitetsbegransning(true);
        utlatande.setAktivitetsbegransning("Kommer inte in i bilen");

        utlatande.setHarUtredningBehandling(true);
        utlatande.setUtredningBehandling("Utredningen är under behandling");

        utlatande.setHarArbetetsPaverkan(true);
        utlatande.setArbetetsPaverkan("Påverkas ja");

        return utlatande.build();
    }

    @Test
    public void doSchematronValidationAf00213() throws Exception {
        String xmlContents = Resources.toString(getResource("transport/af00213.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator("af00213.sch");
        SchematronOutputType result = validator
                .validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        Af00213Utlatande expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        Af00213Utlatande actual = TransportToInternal.convert(transport.getIntyg());

        assertEquals(expected, actual);
    }

    @Test(expected = ConverterException.class)
    public void testInternalToTransportSourceNull() throws Exception {
        InternalToTransport.convert(null);
    }

    @Test
    public void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        Af00213Utlatande utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(referensId, transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        Af00213Utlatande utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertNull(transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaNoRelationTest() throws Exception {
        Af00213Utlatande utlatande = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void convertDecorateSvarPaNotKompltTest() throws Exception {
        Af00213Utlatande utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }
}
