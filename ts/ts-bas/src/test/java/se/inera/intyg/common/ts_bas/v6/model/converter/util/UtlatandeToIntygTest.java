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
package se.inera.intyg.common.ts_bas.v6.model.converter.util;

import org.junit.Test;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.ts_bas.v6.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.v6.model.internal.*;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

import javax.xml.bind.JAXBElement;
import java.time.LocalDateTime;
import java.util.EnumSet;

import static org.junit.Assert.*;

public class UtlatandeToIntygTest {

    private final String PNR_TOLVAN = "19121212-1212";
    private final String PNR_TOLVAN_EXPECTED = "191212121212";

    @Test
    public void testConvert() throws Exception {
        final String intygsId = "intygsid";
        final String enhetsId = "enhetsid";
        final String enhetsnamn = "enhetsnamn";
        final String patientPersonId = PNR_TOLVAN;
        final String skapadAvFullstandigtNamn = "fullstandigt namn";
        final String skapadAvPersonId = "skapad av pid";
        final LocalDateTime signeringsdatum = LocalDateTime.now();
        final String arbetsplatsKod = "arbetsplatsKod";
        final String postadress = "postadress";
        final String postNummer = "postNummer";
        final String postOrt = "postOrt";
        final String epost = "epost";
        final String telefonNummer = "telefonNummer";
        final String vardgivarid = "vardgivarid";
        final String vardgivarNamn = "vardgivarNamn";
        final String forskrivarKod = "forskrivarKod";
        final String fornamn = "fornamn";
        final String efternamn = "efternamn";
        final String mellannamn = "mellannamn";
        final String patientPostadress = "patientPostadress";
        final String patientPostnummer = "patientPostnummer";
        final String patientPostort = "patientPostort";

        TsBasUtlatandeV6 utlatande = buildUtlatande(intygsId, enhetsId, enhetsnamn, patientPersonId,
            skapadAvFullstandigtNamn, skapadAvPersonId, signeringsdatum, arbetsplatsKod, postadress, postNummer, postOrt, epost,
            telefonNummer,
            vardgivarid, vardgivarNamn, forskrivarKod, fornamn, efternamn, mellannamn, patientPostadress, patientPostnummer, patientPostort,
            null, null);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertNotNull(intyg.getVersion());
        assertEquals("TSTRK1007", intyg.getTyp().getCode());
        assertEquals("f6fb361a-e31d-48b8-8657-99b63912dd9b", intyg.getTyp().getCodeSystem());
        assertEquals("Transportstyrelsens läkarintyg", intyg.getTyp().getDisplayName());
        assertEquals(signeringsdatum, intyg.getSigneringstidpunkt());
        assertNotNull(patientPersonId, intyg.getPatient().getPersonId().getRoot());
        assertEquals(PNR_TOLVAN_EXPECTED, intyg.getPatient().getPersonId().getExtension());
        assertEquals(skapadAvFullstandigtNamn, intyg.getSkapadAv().getFullstandigtNamn());
        assertNotNull(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getRoot());
        assertEquals(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getRoot());
        assertEquals(enhetsId, intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertEquals(enhetsnamn, intyg.getSkapadAv().getEnhet().getEnhetsnamn());
        assertNotNull(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getRoot());
        assertEquals(arbetsplatsKod, intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension());
        assertEquals(postadress, intyg.getSkapadAv().getEnhet().getPostadress());
        assertEquals(postNummer, intyg.getSkapadAv().getEnhet().getPostnummer());
        assertEquals(postOrt, intyg.getSkapadAv().getEnhet().getPostort());
        assertEquals(epost, intyg.getSkapadAv().getEnhet().getEpost());
        assertEquals(telefonNummer, intyg.getSkapadAv().getEnhet().getTelefonnummer());
        assertNotNull(intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getRoot());
        assertEquals(vardgivarid, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getExtension());
        assertEquals(vardgivarNamn, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivarnamn());
        assertEquals(forskrivarKod, intyg.getSkapadAv().getForskrivarkod());
        assertEquals(fornamn, intyg.getPatient().getFornamn());
        assertEquals(efternamn, intyg.getPatient().getEfternamn());
        assertEquals(mellannamn, intyg.getPatient().getMellannamn());
        assertEquals(patientPostadress, intyg.getPatient().getPostadress());
        assertEquals(patientPostnummer, intyg.getPatient().getPostnummer());
        assertEquals(patientPostort, intyg.getPatient().getPostort());
        assertTrue(intyg.getRelation().isEmpty());
    }

    @Test
    public void testConvertWithRelation() {
        RelationKod relationKod = RelationKod.FRLANG;
        String relationIntygsId = "relationIntygsId";
        TsBasUtlatandeV6 utlatande = buildUtlatande(relationKod, relationIntygsId);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertNotNull(intyg.getRelation());
        assertEquals(1, intyg.getRelation().size());
        assertEquals(relationKod.value(), intyg.getRelation().get(0).getTyp().getCode());
        assertNotNull(intyg.getRelation().get(0).getTyp().getCodeSystem());
        assertEquals(relationIntygsId, intyg.getRelation().get(0).getIntygsId().getExtension());
        assertNotNull(intyg.getRelation().get(0).getIntygsId().getRoot());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddIntygAvserSvar() {
        TsBasUtlatandeV6 utlatande = buildUtlatande();
        EnumSet<IntygAvserKategori> intygAvserKategorier = EnumSet.of(IntygAvserKategori.C1, IntygAvserKategori.TAXI);
        utlatande = utlatande.toBuilder().setIntygAvser(IntygAvser.create(intygAvserKategorier)).build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertEquals(2, intyg.getSvar().size());
        assertEquals("1", intyg.getSvar().get(0).getId());
        assertEquals(1, intyg.getSvar().get(0).getDelsvar().size());
        assertEquals("1.1", intyg.getSvar().get(0).getDelsvar().get(0).getId());
        JAXBElement<CVType> o = (JAXBElement<CVType>) intyg.getSvar().get(0).getDelsvar().get(0).getContent().get(0);
        assertEquals(IntygAvserKod.C1.getCode(), o.getValue().getCode());
        assertNotNull(o.getValue().getCodeSystem());
        assertEquals(IntygAvserKod.C1.getDescription(), o.getValue().getDisplayName());
        assertEquals("1", intyg.getSvar().get(1).getId());
        assertEquals(1, intyg.getSvar().get(1).getDelsvar().size());
        assertEquals("1.1", intyg.getSvar().get(1).getDelsvar().get(0).getId());
        o = (JAXBElement<CVType>) intyg.getSvar().get(1).getDelsvar().get(0).getContent().get(0);
        assertEquals(IntygAvserKod.TAXI.getCode(), o.getValue().getCode());
        assertNotNull(o.getValue().getCodeSystem());
        assertEquals(IntygAvserKod.TAXI.getDescription(), o.getValue().getDisplayName());
    }

    @Test
    public void testConvertComplementsArbetsplatskodIfNull() {
        final String arbetsplatskod = null;
        TsBasUtlatandeV6 utlatande = buildUtlatande(arbetsplatskod);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertFalse(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension().trim().isEmpty());
    }

    @Test
    public void testConvertComplementsArbetsplatskodIfBlank() {
        final String arbetsplatskod = " ";
        TsBasUtlatandeV6 utlatande = buildUtlatande(arbetsplatskod);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertFalse(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension().trim().isEmpty());
    }

    @Test
    public void testConvertComplementsArbetsplatskodDoesNotOverride() {
        final String arbetsplatskod = "000000";
        TsBasUtlatandeV6 utlatande = buildUtlatande(arbetsplatskod);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertEquals(arbetsplatskod, intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension());
    }

    @Test
    public void testConvertSetsVersionFromTextVersion() {
        final String textVersion = "7.8";
        TsBasUtlatandeV6 utlatande = buildUtlatande();
        utlatande = utlatande.toBuilder().setTextVersion(textVersion).build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertEquals(textVersion, intyg.getVersion());
    }

    @Test
    public void testConvertSetsDefaultVersionIfTextVersionIsNullOrEmpty() {
        final String defaultVersion = "6.7";
        TsBasUtlatandeV6 utlatande = buildUtlatande();
        utlatande = utlatande.toBuilder().setTextVersion(null).build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        assertEquals(defaultVersion, intyg.getVersion());

        utlatande = utlatande.toBuilder().setTextVersion("").build();
        intyg = UtlatandeToIntyg.convert(utlatande);
        assertEquals(defaultVersion, intyg.getVersion());
    }

    private TsBasUtlatandeV6 buildUtlatande() {
        return buildUtlatande(null, null);
    }

    private TsBasUtlatandeV6 buildUtlatande(String arbetsplatskod) {
        return buildUtlatande("intygsId", "enhetsId", "enhetsnamn", PNR_TOLVAN,
            "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now(), arbetsplatskod, "postadress", "postNummer", "postOrt",
            "epost", "telefonNummer", "vardgivarid", "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn",
            "patientPostadress",
            "patientPostnummer", "patientPostort", null, null);
    }

    private TsBasUtlatandeV6 buildUtlatande(RelationKod relationKod, String relationIntygsId) {
        return buildUtlatande("intygsId", "enhetsId", "enhetsnamn", PNR_TOLVAN,
            "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now(), "arbetsplatsKod", "postadress", "postNummer", "postOrt",
            "epost", "telefonNummer", "vardgivarid", "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn",
            "patientPostadress",
            "patientPostnummer", "patientPostort", relationKod, relationIntygsId);
    }

    private TsBasUtlatandeV6 buildUtlatande(String intygsId, String enhetsId, String enhetsnamn,
        String patientPersonId, String skapadAvFullstandigtNamn, String skapadAvPersonId, LocalDateTime signeringsdatum,
        String arbetsplatsKod,
        String postadress, String postNummer, String postOrt, String epost, String telefonNummer, String vardgivarid, String vardgivarNamn,
        String forskrivarKod, String fornamn, String efternamn, String mellannamn, String patientPostadress, String patientPostnummer,
        String patientPostort, RelationKod relationKod, String relationIntygsId) {

        TsBasUtlatandeV6.Builder utlatandeBuilder = TsBasUtlatandeV6.builder();
        utlatandeBuilder.setId(intygsId);
        utlatandeBuilder.setBedomning(Bedomning.builder().build());

        GrundData grundData = new GrundData();
        HoSPersonal skapadAv = new HoSPersonal();

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivarid);
        vardgivare.setVardgivarnamn(vardgivarNamn);

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        vardenhet.setEnhetsnamn(enhetsnamn);
        vardenhet.setArbetsplatsKod(arbetsplatsKod);
        vardenhet.setPostadress(postadress);
        vardenhet.setPostnummer(postNummer);
        vardenhet.setPostort(postOrt);
        vardenhet.setEpost(epost);
        vardenhet.setTelefonnummer(telefonNummer);
        vardenhet.setVardgivare(vardgivare);

        skapadAv.setVardenhet(vardenhet);
        skapadAv.setFullstandigtNamn(skapadAvFullstandigtNamn);
        skapadAv.setPersonId(skapadAvPersonId);
        skapadAv.setForskrivarKod(forskrivarKod);

        grundData.setSkapadAv(skapadAv);

        Personnummer personId = Personnummer.createPersonnummer(patientPersonId).get();

        Patient patient = new Patient();
        patient.setPersonId(personId);
        patient.setFornamn(fornamn);
        patient.setEfternamn(efternamn);
        patient.setMellannamn(mellannamn);
        patient.setPostadress(patientPostadress);
        patient.setPostnummer(patientPostnummer);
        patient.setPostort(patientPostort);

        grundData.setPatient(patient);
        grundData.setSigneringsdatum(signeringsdatum);
        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationIntygsId(relationIntygsId);
            relation.setRelationKod(relationKod);
            grundData.setRelation(relation);
        }

        TsBasUtlatandeV6 utlatande = utlatandeBuilder.setGrundData(grundData).build();
        return utlatande;
    }
}
