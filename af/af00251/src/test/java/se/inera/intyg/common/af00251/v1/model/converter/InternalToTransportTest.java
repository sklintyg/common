/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00251.v1.model.converter;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLFailedAssert;
import com.helger.schematron.svrl.SVRLHelper;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.transform.stream.StreamSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.af00251.v1.model.internal.ArbetsmarknadspolitisktProgram;
import se.inera.intyg.common.af00251.v1.model.internal.BegransningSjukfranvaro;
import se.inera.intyg.common.af00251.v1.model.internal.PrognosAtergang;
import se.inera.intyg.common.af00251.v1.model.internal.Sjukfranvaro;
import se.inera.intyg.common.af00251.v1.rest.AF00251ModuleApiV1;
import se.inera.intyg.common.af_parent.model.converter.RegisterCertificateTestValidator;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class InternalToTransportTest {

    private static URL getResource(String href) {
        return Thread.currentThread()
            .getContextClassLoader()
            .getResource(href);
    }

    public static AF00251UtlatandeV1.Builder getUtlatande() {
        return getUtlatande(null, null, null);
    }

    public static AF00251UtlatandeV1.Builder getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) {
        AF00251UtlatandeV1.Builder utlatande = AF00251UtlatandeV1.builder();
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

        utlatande.setUndersokningsDatum(new InternalDate(LocalDate.now()));

        utlatande.setArbetsmarknadspolitisktProgram(ArbetsmarknadspolitisktProgram.builder()
            .setMedicinskBedomning("Arbetsprov")
            .setOmfattning(ArbetsmarknadspolitisktProgram.Omfattning.DELTID)
            .setOmfattningDeltid(4)
            .build());
        utlatande.setFunktionsnedsattning("Funktionsnedsättning");
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setHarForhinder(true);

        utlatande.setSjukfranvaro(Lists.newArrayList(
            Sjukfranvaro.builder()
                .setChecked(true)
                .setNiva(4)
                .setPeriod(new InternalLocalDateInterval(
                    new InternalDate(LocalDate.now()),
                    new InternalDate(LocalDate.now()
                        .plusDays(5))))
                .build()));

        utlatande.setBegransningSjukfranvaro(
            BegransningSjukfranvaro.builder()
                .setKanBegransas(true)
                .setBeskrivning("Använd hjälpmedel")
                .build());

        utlatande.setPrognosAtergang(
            PrognosAtergang.builder()
                .setPrognos(PrognosAtergang.Prognos.EJ_MOJLIGT_AVGORA)
                .setAnpassningar("Jobba halvtid")
                .build());

        return utlatande;
    }

    @Test
    public void doSchematronValidationAF00251() throws Exception {
        String xmlContents = Resources.toString(getResource("transport/af00251.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator(AF00251ModuleApiV1.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        final List<SVRLFailedAssert> failedAssertions = SVRLHelper.getAllFailedAssertions(result);
        assertThat(failedAssertions
            .stream()
            .map(SVRLFailedAssert::toString)
            .collect(Collectors.joining("\n")), failedAssertions, hasSize(0));
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        AF00251UtlatandeV1 expected = getUtlatande().build();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        AF00251UtlatandeV1 actual = TransportToInternal.convert(transport.getIntyg());

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
        AF00251UtlatandeV1 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId).build();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa()
            .getMeddelandeId());
        assertEquals(referensId, transport.getSvarPa()
            .getReferensId());
    }

    @Test
    public void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        AF00251UtlatandeV1 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null).build();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa()
            .getMeddelandeId());
        assertNull(transport.getSvarPa()
            .getReferensId());
    }

    @Test
    public void convertDecorateSvarPaNoRelationTest() throws Exception {
        AF00251UtlatandeV1 utlatande = getUtlatande().build();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void convertDecorateSvarPaNotKompltTest() throws Exception {
        AF00251UtlatandeV1 utlatande = getUtlatande(RelationKod.FRLANG, null, null).build();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }
}
