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
package se.inera.intyg.common.ts_bas.v6.model.converter;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioFinder;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioNotFoundException;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.ArbetsplatsKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Befattning;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.HsaId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.v3.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v3.Vardgivare;

/**
 * Test class for TransportToExternal, contains methods for setting up Utlatande using both the transport model and the
 * external model, and populating each with mock data.
 *
 * @author erik
 *
 */
public class TransportToInternalTest {

    private static final String ENHETSNAMN = "enhetsnamn";
    private static final String ENHETSID = "enhetsid";
    private static final String VARDGIVARNAMN = "vardgivarnamn";
    private static final String POSTADRESS = "postadress";
    private static final String POSTNUMMER = "postnummer";
    private static final String POSTORT = "postort";
    private static final String TELEFONNUMMER = "telefonnummer";
    private static final String ARBETSPLATSKOD = "0000000";
    private static final String VARDGIVARID = "vardgivarid";
    private static final List<Specialistkompetens> SPECIALIST_KOMPETENS = buildSpecialistkompetens();
    private static final String FULLSTANDIGT_NAMN = "test testorsson";
    private static final String PERSONID = "personid";

    private static List<Specialistkompetens> buildSpecialistkompetens() {
        Specialistkompetens a = new Specialistkompetens();
        a.setDisplayName("a");
        Specialistkompetens b = new Specialistkompetens();
        b.setDisplayName("b");
        Specialistkompetens c = new Specialistkompetens();
        c.setDisplayName("c");

        return Arrays.asList(a, b ,c);
    }

    @Test
    public void testConvert() throws Exception {
        RegisterCertificateType transportModel = ScenarioFinder.getTransportScenario("valid-minimal").asRivtaV3TransportModel();
        transportModel.getIntyg().setSkapadAv(buildHosPersonal());
        TsBasUtlatandeV6 res = TransportToInternal.convert(transportModel.getIntyg());
        assertEquals(LocalDateTime.of(2013, 8, 12, 15, 57, 0), res.getGrundData().getSigneringsdatum());
        HoSPersonal hosPersonal = res.getGrundData().getSkapadAv();
        assertEquals(ENHETSNAMN, hosPersonal.getVardenhet().getEnhetsnamn());
        assertEquals(ENHETSID, hosPersonal.getVardenhet().getEnhetsid());
        assertEquals(VARDGIVARNAMN, hosPersonal.getVardenhet().getVardgivare().getVardgivarnamn());
        assertEquals(POSTADRESS, hosPersonal.getVardenhet().getPostadress());
        assertEquals(POSTNUMMER, hosPersonal.getVardenhet().getPostnummer());
        assertEquals(POSTORT, hosPersonal.getVardenhet().getPostort());
        assertEquals(TELEFONNUMMER, hosPersonal.getVardenhet().getTelefonnummer());
        assertEquals(VARDGIVARID, hosPersonal.getVardenhet().getVardgivare().getVardgivarid());
        assertEquals(FULLSTANDIGT_NAMN, hosPersonal.getFullstandigtNamn());
        assertEquals(PERSONID, hosPersonal.getPersonId());
        assertEquals(SPECIALIST_KOMPETENS.size(), hosPersonal.getSpecialiteter().size());
        assertEquals(SPECIALIST_KOMPETENS.get(0).getDisplayName(), hosPersonal.getSpecialiteter().get(0));
        assertEquals(SPECIALIST_KOMPETENS.get(1).getDisplayName(), hosPersonal.getSpecialiteter().get(1));
        assertEquals(SPECIALIST_KOMPETENS.get(2).getDisplayName(), hosPersonal.getSpecialiteter().get(2));
    }

    @Test
    public void testConvertMapsSpecialistkompetens() throws ScenarioNotFoundException, ConverterException {
        final Specialistkompetens specialistkompetens = new Specialistkompetens();
        specialistkompetens.setCode("kod");
        specialistkompetens.setDisplayName("Hörselrubbningar");
        RegisterCertificateType transportModel = ScenarioFinder.getTransportScenario("valid-minimal").asRivtaV3TransportModel();
        transportModel.getIntyg().getSkapadAv().getSpecialistkompetens().clear();
        transportModel.getIntyg().getSkapadAv().getSpecialistkompetens().add(specialistkompetens);
        TsBasUtlatandeV6 res = TransportToInternal.convert(transportModel.getIntyg());
        HoSPersonal skapadAv = res.getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getSpecialiteter().size());
        assertEquals(specialistkompetens.getDisplayName(), skapadAv.getSpecialiteter().get(0));
    }

    @Test
    public void testConvertMapsBefattningDescriptionToCodeIfPossible() throws ScenarioNotFoundException, ConverterException {
        final Befattning befattning = new Befattning();
        befattning.setCode("203010");
        befattning.setDisplayName("Läkare legitimerad, specialiseringstjänstgöring");
        RegisterCertificateType transportModel = ScenarioFinder.getTransportScenario("valid-minimal").asRivtaV3TransportModel();
        transportModel.getIntyg().getSkapadAv().getBefattning().clear();
        transportModel.getIntyg().getSkapadAv().getBefattning().add(befattning);
        TsBasUtlatandeV6 res = TransportToInternal.convert(transportModel.getIntyg());
        HoSPersonal skapadAv = res.getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattning.getCode(), skapadAv.getBefattningar().get(0));
    }

    @Test
    public void testConvertKeepBefattningCodeIfDescriptionNotFound() throws ScenarioNotFoundException, ConverterException {
        final Befattning befattning = new Befattning();
        befattning.setCode("kod");
        RegisterCertificateType transportModel = ScenarioFinder.getTransportScenario("valid-minimal").asRivtaV3TransportModel();
        transportModel.getIntyg().getSkapadAv().getBefattning().clear();
        transportModel.getIntyg().getSkapadAv().getBefattning().add(befattning);
        TsBasUtlatandeV6 res = TransportToInternal.convert(transportModel.getIntyg());
        HoSPersonal skapadAv = res.getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattning.getCode(), skapadAv.getBefattningar().get(0));
    }

    private HosPersonal buildHosPersonal() {
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setPersonalId(buildHsaId(PERSONID));
        hosPersonal.setFullstandigtNamn(FULLSTANDIGT_NAMN);
        hosPersonal.getSpecialistkompetens().addAll(SPECIALIST_KOMPETENS);

        Enhet vardenhet = new Enhet();

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivareId(buildHsaId(VARDGIVARID));
        vardgivare.setVardgivarnamn(VARDGIVARNAMN);
        vardenhet.setVardgivare(vardgivare);

        vardenhet.setEnhetsId(buildHsaId(ENHETSID));
        vardenhet.setEnhetsnamn(ENHETSNAMN);
        vardenhet.setPostadress(POSTADRESS);
        vardenhet.setPostnummer(POSTNUMMER);
        vardenhet.setPostort(POSTORT);
        vardenhet.setTelefonnummer(TELEFONNUMMER);
        ArbetsplatsKod arbetsplatskod = new ArbetsplatsKod();
        arbetsplatskod.setExtension(ARBETSPLATSKOD);
        vardenhet.setArbetsplatskod(arbetsplatskod);
        hosPersonal.setEnhet(vardenhet);

        return hosPersonal;
    }

    private HsaId buildHsaId(String extension) {
        HsaId hsaId = new HsaId();
        hsaId.setExtension(extension);
        return hsaId;
    }

    private II buildII(String extension) {
        II ii = new II();
        ii.setExtension(extension);
        return ii;
    }
}
