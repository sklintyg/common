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
package se.inera.intyg.common.ts_diabetes.v2.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioFinder;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioNotFoundException;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class InternalToTransportConverterTest {

    private static final String ENHETSNAMN = "enhetsnamn";
    private static final String ENHETSID = "enhetsid";
    private static final String VARDGIVARNAMN = "vardgivarnamn";
    private static final String POSTADRESS = "postadress";
    private static final String POSTNUMMER = "postnummer";
    private static final String POSTORT = "postort";
    private static final String TELEFONNUMMER = "telefonnummer";
    private static final String VARDGIVARID = "vardgivarid";
    private static final List<String> SPECIALIST_KOMPETENS = Arrays.asList("a", "b", "c");
    private static final String FULLSTANDIGT_NAMN = "test testorsson";
    private static final String PERSONID = "personid";

    @Test
    public void testConvert() throws ScenarioNotFoundException {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().setSkapadAv(buildHosPersonal(SPECIALIST_KOMPETENS));

        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(ENHETSNAMN, skapadAv.getVardenhet().getEnhetsnamn());
        assertEquals(ENHETSID, skapadAv.getVardenhet().getEnhetsId().getExtension());
        assertEquals(VARDGIVARNAMN, skapadAv.getVardenhet().getVardgivare().getVardgivarnamn());
        assertEquals(POSTADRESS, skapadAv.getVardenhet().getPostadress());
        assertEquals(POSTNUMMER, skapadAv.getVardenhet().getPostnummer());
        assertEquals(POSTORT, skapadAv.getVardenhet().getPostort());
        assertEquals(TELEFONNUMMER, skapadAv.getVardenhet().getTelefonnummer());
        assertEquals(VARDGIVARID, skapadAv.getVardenhet().getVardgivare().getVardgivarid().getExtension());
        assertEquals(FULLSTANDIGT_NAMN, skapadAv.getFullstandigtNamn());
        assertEquals(PERSONID, skapadAv.getPersonId().getExtension());

        assertEquals(SPECIALIST_KOMPETENS, skapadAv.getSpecialiteter());
    }

    @Test
    public void testConvertWithMillisInTimestamp() throws ScenarioNotFoundException {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().setSigneringsdatum(LocalDateTime.of(2012, 8, 2, 10, 9, 0, 123));

        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        assertEquals("2012-08-02T10:09:00", res.getIntyg().getGrundData().getSigneringsTidstampel()); // millis is not valid in transport
    }

    @Test
    public void testConvertNullSpecialistkompetens() throws ScenarioNotFoundException {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().setSkapadAv(buildHosPersonal(null));
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertTrue(skapadAv.getSpecialiteter().isEmpty());
    }

    @Test
    public void testConvertWithSpecialistkompetens() throws ScenarioNotFoundException {
        String specialistkompetens1 = "Kirurgi";
        String specialistkompetens2 = "Allergi";
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens1);
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens2);
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(2, skapadAv.getSpecialiteter().size());
        assertEquals(specialistkompetens1, skapadAv.getSpecialiteter().get(0));
        assertEquals(specialistkompetens2, skapadAv.getSpecialiteter().get(1));
    }

    @Test
    public void testConvertMapsBefattningCodeToDescriptionIfPossible() throws ScenarioNotFoundException {
        final String befattning = "203010";
        final String description = "Läkare legitimerad, specialiseringstjänstgöring";
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattning);
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(description, skapadAv.getBefattningar().get(0));
    }

    @Test
    public void testConvertKeepBefattningCodeIfDescriptionNotFound() throws ScenarioNotFoundException {
        String befattningskod = "kod";
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattningskod);
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattningskod, skapadAv.getBefattningar().get(0));
    }

    @Test
    public void testConvertSetsVersionAndUtgavaFromTextVersion() throws ScenarioNotFoundException, ConverterException {
        final String version = "03";
        final String utgava = "07";
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.setTextVersion(version + "." + utgava);
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        assertEquals(version, res.getIntyg().getVersion());
        assertEquals(utgava, res.getIntyg().getUtgava());
    }

    @Test
    public void testConvertSetsDefaultVersionAndUtgavaIfTextVersionIsNullOrEmpty() throws ScenarioNotFoundException, ConverterException {
        final String defaultVersion = "02";
        final String defaultUtgava = "06";
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.setTextVersion(null);
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        assertEquals(defaultVersion, res.getIntyg().getVersion());
        assertEquals(defaultUtgava, res.getIntyg().getUtgava());

        utlatande.setTextVersion("");
        res = InternalToTransportConverter.convert(utlatande);
        assertEquals(defaultVersion, res.getIntyg().getVersion());
        assertEquals(defaultUtgava, res.getIntyg().getUtgava());
    }

    private HoSPersonal buildHosPersonal(List<String> specialistKompetens) {
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId(PERSONID);
        hosPersonal.setFullstandigtNamn(FULLSTANDIGT_NAMN);
        if (specialistKompetens != null) {
            hosPersonal.getSpecialiteter().addAll(specialistKompetens);
        }

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
        hosPersonal.setVardenhet(vardenhet);

        return hosPersonal;
    }
}
