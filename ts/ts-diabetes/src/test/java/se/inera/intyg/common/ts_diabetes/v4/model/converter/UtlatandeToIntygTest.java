/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

public class UtlatandeToIntygTest {

    private static final String PNR_TOLVAN = "191212121212";

    @Test
    public void testConvert() {
        final String intygsId = "intygsid";
        final String textVersion = "textversion";
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

        TsDiabetesUtlatandeV4 utlatande = buildUtlatande(intygsId, textVersion, enhetsId, enhetsnamn, patientPersonId,
            skapadAvFullstandigtNamn, skapadAvPersonId, signeringsdatum, arbetsplatsKod, postadress, postNummer, postOrt, epost,
            telefonNummer,
            vardgivarid, vardgivarNamn, forskrivarKod, fornamn, efternamn, mellannamn, patientPostadress, patientPostnummer,
            patientPostort,
            null, null).build();

        Intyg intyg = se.inera.intyg.common.ts_diabetes.v4.model.converter.UtlatandeToIntyg.convert(utlatande);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertEquals(textVersion, intyg.getVersion());
        assertEquals(TsDiabetesEntryPoint.KV_UTLATANDETYP_INTYG_CODE, intyg.getTyp().getCode());
        assertEquals("f6fb361a-e31d-48b8-8657-99b63912dd9b", intyg.getTyp().getCodeSystem());
        Assert.assertEquals(TsDiabetesEntryPoint.ISSUER_MODULE_NAME, intyg.getTyp().getDisplayName());
        assertEquals(signeringsdatum, intyg.getSigneringstidpunkt());
        assertNotNull(patientPersonId, intyg.getPatient().getPersonId().getRoot());
        assertEquals(patientPersonId, intyg.getPatient().getPersonId().getExtension());
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
        assertEquals("", intyg.getPatient().getFornamn());
        assertEquals("", intyg.getPatient().getEfternamn());
        assertNull(intyg.getPatient().getMellannamn());
        assertEquals("", intyg.getPatient().getPostadress());
        assertEquals("", intyg.getPatient().getPostnummer());
        assertEquals("", intyg.getPatient().getPostort());
        assertTrue(intyg.getRelation().isEmpty());
    }

    @Test
    public void emptyUtlatandeShouldHaveNoIncompleteSvar() {
        // Given
        TsDiabetesUtlatandeV4 utlatande = buildUtlatande().build();

        // When
        Intyg intyg = se.inera.intyg.common.ts_diabetes.v4.model.converter.UtlatandeToIntyg.convert(utlatande);

        // Then
        assertThat(intyg.getSvar()).allMatch(svar -> svar.getDelsvar().size() != 0);
    }

    @Test
    public void svarWithoutDelsvarInJsonShouldNotPropagateToXml() {
        // Given
        TsDiabetesUtlatandeV4 utlatande = buildUtlatande()
            .setIntygAvser(IntygAvser.create(null))
            .setIdentitetStyrktGenom(IdKontroll.create(null))
            .setAllmant(Allmant.builder()
                .setBehandling(Behandling.builder().build())
                .build())
            .setHypoglykemi(Hypoglykemi.builder().build())
            .setBedomning(Bedomning.builder().build())
            .build();

        // When
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        // Then
        assertThat(intyg.getSvar()).allMatch(svar -> svar.getDelsvar().size() != 0);
    }

    private static TsDiabetesUtlatandeV4.Builder buildUtlatande() {
        return buildUtlatande(null, null);
    }

    private static TsDiabetesUtlatandeV4.Builder buildUtlatande(RelationKod relationKod, String relationIntygsId) {
        return buildUtlatande("intygsId", "textVersion", "enhetsId", "enhetsnamn", PNR_TOLVAN,
            "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now(), "arbetsplatsKod", "postadress", "postNummer",
            "postOrt",
            "epost", "telefonNummer", "vardgivarid", "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn",
            "patientPostadress",
            "patientPostnummer", "patientPostort", relationKod, relationIntygsId);
    }

    private static TsDiabetesUtlatandeV4.Builder buildUtlatande(String intygsId, String textVersion, String enhetsId, String enhetsnamn,
        String patientPersonId, String skapadAvFullstandigtNamn, String skapadAvPersonId, LocalDateTime signeringsdatum,
        String arbetsplatsKod,
        String postadress, String postNummer, String postOrt, String epost, String telefonNummer, String vardgivarid,
        String vardgivarNamn,
        String forskrivarKod, String fornamn, String efternamn, String mellannamn, String patientPostadress, String patientPostnummer,
        String patientPostort, RelationKod relationKod, String relationIntygsId) {

        TsDiabetesUtlatandeV4.Builder template = TsDiabetesUtlatandeV4.builder();
        template.setId(intygsId);
        template.setTextVersion(textVersion);

        GrundData grundData = new GrundData();
        HoSPersonal skapadAv = new HoSPersonal();

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        vardenhet.setEnhetsnamn(enhetsnamn);
        vardenhet.setArbetsplatsKod(arbetsplatsKod);
        vardenhet.setPostadress(postadress);
        vardenhet.setPostnummer(postNummer);
        vardenhet.setPostort(postOrt);
        vardenhet.setEpost(epost);
        vardenhet.setTelefonnummer(telefonNummer);

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivarid);
        vardgivare.setVardgivarnamn(vardgivarNamn);
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
        template.setGrundData(grundData);

        return template;
    }
}
