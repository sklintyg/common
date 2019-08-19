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
package se.inera.intyg.common.tstrk1009.v1.model.converter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INTYGET_AVSER_BEHORIGHET_DELSVAR_ID;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioFinder;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class InternalToTransportTest {

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    private static final String ENHETSNAMN = "enhetsnamn";
    private static final String ENHETSID = "enhetsid";
    private static final String VARDGIVARNAMN = "vardgivarnamn";
    private static final String POSTADRESS = "postadress";
    private static final String POSTNUMMER = "postnummer";
    private static final String POSTORT = "postort";
    private static final String TELEFONNUMMER = "telefonnummer";
    private static final String ARBETSPLATSKOD = "0000000";
    private static final String VARDGIVARID = "vardgivarid";
    private static final List<String> SPECIALIST_KOMPETENS = Arrays.asList("a", "b", "c");
    private static final String FULLSTANDIGT_NAMN = "test testorsson";
    private static final String PERSONID = "personid";

    public static Tstrk1009UtlatandeV1 getUtlatande() throws Exception {
        return getUtlatande(null, null, null);
    }

    public static Tstrk1009UtlatandeV1 getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId)
        throws Exception {
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-max").asInternalModel();
        utlatande.getGrundData().setSkapadAv(buildHosPersonal());

        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationKod(relationKod);
            relation.setMeddelandeId(relationMeddelandeId);
            relation.setReferensId(referensId);
            utlatande.getGrundData().setRelation(relation);
        }

        return utlatande;
    }

    @Test
    public void testInternalToTransportConversion() throws Exception {
        Tstrk1009UtlatandeV1 expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        Tstrk1009UtlatandeV1 actual = TransportToInternal.convert(transport.getIntyg());

        ObjectMapper objectMapper = new CustomObjectMapper();
        assertEquals(objectMapper.writeValueAsString(expected), objectMapper.writeValueAsString(actual));
    }

    @Test
    public void testInternalToTransportSourceNullShouldThrow() {
        assertThatThrownBy(() -> InternalToTransport.convert(null))
            .isExactlyInstanceOf(ConverterException.class)
            .hasMessage("Source utlatande was null, cannot convert");
    }

    @Test
    public void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        Tstrk1009UtlatandeV1 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(referensId, transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        Tstrk1009UtlatandeV1 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertNull(transport.getSvarPa().getReferensId());
    }

    @Test
    public void convertDecorateSvarPaNoRelationTest() throws Exception {
        Tstrk1009UtlatandeV1 utlatande = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void convertDecorateSvarPaNotKompltTest() throws Exception {
        Tstrk1009UtlatandeV1 utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    public void testConvertWithSpecialistkompetens() throws ScenarioNotFoundException, ConverterException {
        String specialistkompetens1 = "Kirurgi";
        String specialistkompetens2 = "Allergi";
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-specialitet").asInternalModel();
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        HosPersonal skapadAv = res.getIntyg().getSkapadAv();
        assertEquals(2, skapadAv.getSpecialistkompetens().size());
        assertEquals(specialistkompetens1, skapadAv.getSpecialistkompetens().get(0).getDisplayName());
        assertEquals(specialistkompetens2, skapadAv.getSpecialistkompetens().get(1).getDisplayName());
    }

    @Test
    public void testConvertMapsBefattningCodeToDescriptionIfPossible() throws ScenarioNotFoundException, ConverterException {
        final String befattning = "203010";
        final String description = "Läkare legitimerad, specialiseringstjänstgöring";
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-befattning").asInternalModel();
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        HosPersonal skapadAv = res.getIntyg().getSkapadAv();
        assertEquals(1, skapadAv.getBefattning().size());
        assertEquals(befattning, skapadAv.getBefattning().get(0).getCode());
        assertEquals(description, skapadAv.getBefattning().get(0).getDisplayName());
    }

    @Test
    public void testConvertSetsVersionAndUtgavaFromTextVersion() throws ScenarioNotFoundException, ConverterException {
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-max").asInternalModel();
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        assertEquals("1.0", res.getIntyg().getVersion());
    }

    @Test
    public void testConvertToTransportSetsCorrectBehorighetDisplayName() throws ScenarioNotFoundException, ConverterException {
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-min").asInternalModel();
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        final Svar.Delsvar taxiDelsvar = res.getIntyg().getSvar().stream()
            .flatMap(svar -> svar.getDelsvar().stream())
            .filter(delsvar -> {
                try {
                    return delsvar.getId().equals(INTYGET_AVSER_BEHORIGHET_DELSVAR_ID)
                        && getCVSvarContent(delsvar).getCode().equals(Korkortsbehorighet.TAXI.getCode());
                } catch (ConverterException e) {
                    throw new RuntimeException(e);
                }
            })
            .findAny().orElse(null);
        assertNotNull(taxiDelsvar);
        assertEquals(Korkortsbehorighet.TAXI.getValue(), getCVSvarContent(taxiDelsvar).getDisplayName());
    }

    @Test
    public void testConvertSetsDefaultVersionAndUtgavaIfTextVersionIsNullOrEmpty() throws ScenarioNotFoundException, ConverterException {
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-max").asInternalModel();
        utlatande = utlatande.toBuilder().setTextVersion(null).build();
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        assertEquals("1.0", res.getIntyg().getVersion());

        utlatande = utlatande.toBuilder().setTextVersion("").build();
        res = InternalToTransport.convert(utlatande);
        assertEquals("1.0", res.getIntyg().getVersion());
    }

    private static HoSPersonal buildHosPersonal() {
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId(PERSONID);
        hosPersonal.setFullstandigtNamn(FULLSTANDIGT_NAMN);
        hosPersonal.getSpecialiteter().addAll(SPECIALIST_KOMPETENS);

        Vardenhet vardenhet = new Vardenhet();

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(VARDGIVARID);
        vardgivare.setVardgivarnamn(VARDGIVARNAMN);
        vardenhet.setVardgivare(vardgivare);

        vardenhet.setEnhetsid(ENHETSID);
        vardenhet.setEnhetsnamn(ENHETSNAMN);
        vardenhet.setPostadress(POSTADRESS);
        vardenhet.setPostnummer(POSTNUMMER);
        vardenhet.setPostort(POSTORT);
        vardenhet.setTelefonnummer(TELEFONNUMMER);
        vardenhet.setArbetsplatsKod(ARBETSPLATSKOD);
        hosPersonal.setVardenhet(vardenhet);

        return hosPersonal;
    }
}
