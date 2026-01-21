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
package se.inera.intyg.common.ts_bas.v7.model.converter;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.common.ts_bas.v7.utils.ScenarioFinder;
import se.inera.intyg.common.ts_bas.v7.utils.ScenarioNotFoundException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;

/**
 * Unit test for InternalToExternalConverter.
 *
 * @author erik
 */

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {BefattningService.class, UnitMappingConfigLoader.class, UnitMapperUtil.class,
    InternalConverterUtil.class})
class InternalToTransportTest {

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

    static TsBasUtlatandeV7 getUtlatande() throws Exception {
        return getUtlatande(null, null, null);
    }

    static TsBasUtlatandeV7 getUtlatande(RelationKod relationKod, String relationMeddelandeId, String referensId) throws Exception {
        TsBasUtlatandeV7 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        utlatande.getGrundData().setSkapadAv(buildHosPersonal(SPECIALIST_KOMPETENS));

        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationKod(relationKod);
            relation.setMeddelandeId(relationMeddelandeId);
            relation.setReferensId(referensId);
            utlatande.getGrundData().setRelation(relation);
        }

        return utlatande;
    }

    @BeforeAll
    static void initUtils() {
        final var mapper = mock(UnitMapperUtil.class);

        when(mapper.getMappedUnit(any(), any(), any(), any()))
            .thenAnswer(inv -> new MappedUnit(
                inv.getArgument(0, String.class),
                inv.getArgument(1, String.class),
                inv.getArgument(2, String.class),
                inv.getArgument(3, String.class)
            ));

        new TransportConverterUtil(mapper).initialize();
    }

    /*
        Waiting for INTYG-6650

        @Test
         void doSchematronValidationTsBas() throws Exception {
            String xmlContents = Resources.toString(getResource("transport/ts-bas-max.xml"), Charsets.UTF_8);

            RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
            assertTrue(generalValidator.validateGeneral(xmlContents));

            RegisterCertificateValidator validator = new RegisterCertificateValidator("ts_bas.sch");
            SchematronOutputType result = validator
                    .validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

            assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
        }
    */
    @Test
    void testInternalToTransportConversion() throws Exception {
        TsBasUtlatandeV7 expected = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(expected);
        TsBasUtlatandeV7 actual = TransportToInternal.convert(transport.getIntyg());

//        assertEquals(expected, actual); // Switch after INTYG-6980
        ObjectMapper objectMapper = new CustomObjectMapper();
        assertEquals(objectMapper.writeValueAsString(expected), objectMapper.writeValueAsString(actual));
    }

    @Test
    void testInternalToTransportSourceNull() throws Exception {
        assertThrows(ConverterException.class, () -> InternalToTransport.convert(null));
    }

    @Test
    void convertDecorateSvarPaTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        TsBasUtlatandeV7 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, referensId);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertEquals(referensId, transport.getSvarPa().getReferensId());
    }

    @Test
    void convertDecorateSvarPaReferensIdNullTest() throws Exception {
        final String meddelandeId = "meddelandeId";
        TsBasUtlatandeV7 utlatande = getUtlatande(RelationKod.KOMPLT, meddelandeId, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNotNull(transport.getSvarPa());
        assertEquals(meddelandeId, transport.getSvarPa().getMeddelandeId());
        assertNull(transport.getSvarPa().getReferensId());
    }

    @Test
    void convertDecorateSvarPaNoRelationTest() throws Exception {
        TsBasUtlatandeV7 utlatande = getUtlatande();
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    void convertDecorateSvarPaNotKompltTest() throws Exception {
        TsBasUtlatandeV7 utlatande = getUtlatande(RelationKod.FRLANG, null, null);
        RegisterCertificateType transport = InternalToTransport.convert(utlatande);
        assertNull(transport.getSvarPa());
    }

    @Test
    void testConvertWithSpecialistkompetens() throws ScenarioNotFoundException, ConverterException {
        String specialistkompetens1 = "Kirurgi";
        String specialistkompetens2 = "Allergi";
        TsBasUtlatandeV7 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens1);
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens2);
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        HosPersonal skapadAv = res.getIntyg().getSkapadAv();
        assertEquals(2, skapadAv.getSpecialistkompetens().size());
        assertEquals(specialistkompetens1, skapadAv.getSpecialistkompetens().get(0).getDisplayName());
        assertEquals(specialistkompetens2, skapadAv.getSpecialistkompetens().get(1).getDisplayName());
    }

    @Test
    void testConvertMapsBefattningCodeToDescriptionIfPossible() throws ScenarioNotFoundException, ConverterException {
        final String befattning = "203010";
        final String description = "Läkare legitimerad, specialiseringstjänstgöring";
        TsBasUtlatandeV7 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattning);
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        HosPersonal skapadAv = res.getIntyg().getSkapadAv();
        assertEquals(1, skapadAv.getBefattning().size());
        assertEquals(befattning, skapadAv.getBefattning().get(0).getCode());
        assertEquals(description, skapadAv.getBefattning().get(0).getDisplayName());
    }

    @Test
    void testConvertKeepBefattningCodeIfDescriptionNotFound() throws ScenarioNotFoundException, ConverterException {
        String befattningskod = "kod";
        TsBasUtlatandeV7 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattningskod);
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        HosPersonal skapadAv = res.getIntyg().getSkapadAv();
        assertEquals(1, skapadAv.getBefattning().size());
        assertEquals(befattningskod, skapadAv.getBefattning().get(0).getCode());
    }

    @Test
    void testConvertSetsVersionAndUtgavaFromTextVersion() throws ScenarioNotFoundException, ConverterException {
        final String version = "07";
        final String utgava = "08";
        TsBasUtlatandeV7 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande = utlatande.toBuilder().setTextVersion(version + "." + utgava).build();
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        assertEquals(version + "." + utgava, res.getIntyg().getVersion());
    }

    @Test
    void testConvertSetsDefaultVersionAndUtgavaIfTextVersionIsNullOrEmpty() throws ScenarioNotFoundException, ConverterException {
        final String defaultVersion = "7";
        final String defaultUtgava = "0";
        TsBasUtlatandeV7 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande = utlatande.toBuilder().setTextVersion(null).build();
        RegisterCertificateType res = InternalToTransport.convert(utlatande);
        assertEquals(defaultVersion + "." + defaultUtgava, res.getIntyg().getVersion());

        utlatande = utlatande.toBuilder().setTextVersion("").build();
        res = InternalToTransport.convert(utlatande);
        assertEquals(defaultVersion + "." + defaultUtgava, res.getIntyg().getVersion());
    }

    private static HoSPersonal buildHosPersonal(List<String> specialistKompetens) {
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