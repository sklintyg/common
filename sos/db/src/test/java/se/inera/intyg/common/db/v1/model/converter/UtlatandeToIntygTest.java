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
package se.inera.intyg.common.db.v1.model.converter;

import org.junit.Test;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_AVLAGSNAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DETALJER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_DELSVAR_ID;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

public class UtlatandeToIntygTest {

    private final String intygsId = "intygsid";
    private final String textVersion = "textversion";
    private final String enhetsId = "enhetsid";
    private final String enhetsnamn = "enhetsnamn";
    private final String patientPersonId = "191212121212";
    private final String skapadAvFullstandigtNamn = "fullständigt namn";
    private final String skapadAvPersonId = "skapad av pid";
    private final LocalDateTime signeringsdatum = LocalDateTime.now();
    private final String arbetsplatsKod = "arbetsplatsKod";
    private final String postadress = "postadress";
    private final String postNummer = "postNummer";
    private final String postOrt = "postOrt";
    private final String epost = "epost";
    private final String telefonNummer = "telefonNummer";
    private final String vardgivarid = "vardgivarid";
    private final String vardgivarNamn = "vardgivarNamn";
    private final String forskrivarKod = "forskrivarKod";
    private final String fornamn = "fornamn";
    private final String efternamn = "efternamn";
    private final String mellannamn = "mellannamn";
    private final String patientPostadress = "patientPostadress";
    private final String patientPostnummer = "patientPostnummer";
    private final String patientPostort = "patientPostort";
    private final String identitetStyrkt = "identitetStyrkt";
    private final Boolean dodsdatumSakert = true;
    private final InternalDate dodsdatum = new InternalDate(LocalDate.of(2017, 1, 1));
    private final InternalDate antraffatDod = new InternalDate(LocalDate.of(2017, 1, 2));
    private final String kommun = "kommun";
    private final DodsplatsBoende boende = DodsplatsBoende.ORDINART_BOENDE;
    private final Boolean barn = true;
    private final Boolean explosivImplantat = true;
    private final Boolean explosivAvlagsnat = true;
    private final Undersokning undersokningYttre = Undersokning.UNDERSOKNING_SKA_GORAS;
    private final InternalDate undersokningDatum = new InternalDate(LocalDate.of(2017, 1, 3));
    private final Boolean polisanmalan = true;

    @Test
    public void testConvert() throws Exception {
        DbUtlatandeV1 utlatande = DbUtlatandeV1.builder()
            .setId(intygsId)
            .setTextVersion(textVersion)
            .setGrundData(createGrundData(enhetsId, enhetsnamn, arbetsplatsKod, postadress, postNummer, postOrt, epost, telefonNummer,
                vardgivarid, vardgivarNamn, skapadAvFullstandigtNamn, skapadAvPersonId, forskrivarKod, patientPersonId, fornamn,
                efternamn, mellannamn, patientPostadress, patientPostnummer, patientPostort, signeringsdatum))
            .setIdentitetStyrkt(identitetStyrkt)
            .setDodsdatumSakert(dodsdatumSakert)
            .setDodsdatum(dodsdatum)
            .setAntraffatDodDatum(antraffatDod)
            .setDodsplatsKommun(kommun)
            .setDodsplatsBoende(boende)
            .setBarn(barn)
            .setExplosivImplantat(explosivImplantat)
            .setExplosivAvlagsnat(explosivAvlagsnat)
            .setUndersokningYttre(undersokningYttre)
            .setUndersokningDatum(undersokningDatum)
            .setPolisanmalan(polisanmalan)
            .build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertEquals(textVersion, intyg.getVersion());
        assertEquals(KvIntygstyp.DB.getCodeValue(), intyg.getTyp().getCode());
        assertEquals(KvIntygstyp.DB.getCodeSystem(), intyg.getTyp().getCodeSystem());
        assertEquals(KvIntygstyp.DB.getDisplayName(), intyg.getTyp().getDisplayName());
        assertEquals(signeringsdatum, intyg.getSigneringstidpunkt());
        assertEquals(signeringsdatum, intyg.getSkickatTidpunkt());
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
        assertEquals(fornamn, intyg.getPatient().getFornamn());
        assertEquals(efternamn, intyg.getPatient().getEfternamn());
        assertEquals(mellannamn, intyg.getPatient().getMellannamn());
        assertEquals(patientPostadress, intyg.getPatient().getPostadress());
        assertEquals(patientPostnummer, intyg.getPatient().getPostnummer());
        assertEquals(patientPostort, intyg.getPatient().getPostort());
        assertTrue(intyg.getRelation().isEmpty());
        assertEquals(7, intyg.getSvar().size());
        for (Svar svar : intyg.getSvar()) {
            switch (svar.getId()) {
                case IDENTITET_STYRKT_SVAR_ID:
                    assertEquals(1, svar.getDelsvar().size());
                    assertEquals(IDENTITET_STYRKT_DELSVAR_ID, svar.getDelsvar().get(0).getId());
                    assertEquals(identitetStyrkt, getStringContent(svar.getDelsvar().get(0)));
                    break;
                case DODSDATUM_SVAR_ID:
                    for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                        switch (delsvar.getId()) {
                            case DODSDATUM_SAKERT_DELSVAR_ID:
                                assertEquals(dodsdatumSakert, Boolean.parseBoolean(getStringContent(delsvar)));
                                break;
                            case DODSDATUM_DELSVAR_ID:
                                assertEquals(dodsdatum, new InternalDate(getStringContent(delsvar)));
                                break;
                            case ANTRAFFAT_DOD_DATUM_DELSVAR_ID:
                                assertEquals(antraffatDod, new InternalDate(getStringContent(delsvar)));
                                break;
                            default:
                                fail();
                        }
                    }
                    break;
                case DODSPLATS_SVAR_ID:
                    for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                        switch (delsvar.getId()) {
                            case DODSPLATS_KOMMUN_DELSVAR_ID:
                                assertEquals(kommun, getStringContent(delsvar));
                                break;
                            case DODSPLATS_BOENDE_DELSVAR_ID:
                                assertEquals(boende, DodsplatsBoende.valueOf(getCVSvarContent(delsvar).getCode()));
                                break;
                            default:
                                fail();
                        }
                    }
                    break;
                case BARN_SVAR_ID:
                    assertEquals(1, svar.getDelsvar().size());
                    assertEquals(BARN_DELSVAR_ID, svar.getDelsvar().get(0).getId());
                    assertEquals(barn, Boolean.parseBoolean(getStringContent(svar.getDelsvar().get(0))));
                    break;
                case EXPLOSIV_IMPLANTAT_SVAR_ID:
                    for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                        switch (delsvar.getId()) {
                            case EXPLOSIV_IMPLANTAT_DELSVAR_ID:
                                assertEquals(explosivImplantat, Boolean.parseBoolean(getStringContent(delsvar)));
                                break;
                            case EXPLOSIV_AVLAGSNAT_DELSVAR_ID:
                                assertEquals(explosivAvlagsnat, Boolean.parseBoolean(getStringContent(delsvar)));
                                break;
                            default:
                                fail();
                        }
                    }
                    break;
                case UNDERSOKNING_SVAR_ID:
                    for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                        switch (delsvar.getId()) {
                            case UNDERSOKNING_YTTRE_DELSVAR_ID:
                                assertEquals(false, Boolean.parseBoolean(getStringContent(delsvar)));
                                break;
                            case UNDERSOKNING_DETALJER_DELSVAR_ID:
                                assertEquals(undersokningYttre, Undersokning.valueOf(getCVSvarContent(delsvar).getCode()));
                                break;
                            case UNDERSOKNING_DATUM_DELSVAR_ID:
                                assertEquals(undersokningDatum, new InternalDate(getStringContent(delsvar)));
                                break;
                            default:
                                fail();
                        }
                    }
                    break;
                case POLISANMALAN_SVAR_ID:
                    assertEquals(1, svar.getDelsvar().size());
                    assertEquals(POLISANMALAN_DELSVAR_ID, svar.getDelsvar().get(0).getId());
                    assertEquals(polisanmalan, Boolean.parseBoolean(getStringContent(svar.getDelsvar().get(0))));
                    break;
                default:
                    fail();
            }
        }
    }

    @Test
    public void testOsakertDodsdatum() throws Exception {
        InternalDate zeroFilledDodsdatum = new InternalDate("2017-01-00");
        DbUtlatandeV1 utlatande = DbUtlatandeV1.builder()
            .setId(intygsId)
            .setTextVersion(textVersion)
            .setGrundData(createGrundData(enhetsId, enhetsnamn, arbetsplatsKod, postadress, postNummer, postOrt,
                epost, telefonNummer, vardgivarid, vardgivarNamn, skapadAvFullstandigtNamn, skapadAvPersonId,
                forskrivarKod, patientPersonId, fornamn, efternamn, mellannamn, patientPostadress,
                patientPostnummer, patientPostort, signeringsdatum))
            .setIdentitetStyrkt(identitetStyrkt)
            .setDodsdatumSakert(false)
            .setDodsdatum(new InternalDate("2017-01"))
            .setAntraffatDodDatum(antraffatDod)
            .setDodsplatsKommun(kommun)
            .setDodsplatsBoende(boende)
            .setBarn(barn)
            .setExplosivImplantat(explosivImplantat)
            .setExplosivAvlagsnat(explosivAvlagsnat)
            .setUndersokningYttre(undersokningYttre)
            .setUndersokningDatum(undersokningDatum)
            .setPolisanmalan(polisanmalan)
            .build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);
        Svar svar = intyg.getSvar().stream().filter(it ->
            it.getId().equals(DODSDATUM_SVAR_ID)).findFirst().orElseThrow(RuntimeException::new);
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case DODSDATUM_SAKERT_DELSVAR_ID:
                    assertEquals(false, Boolean.parseBoolean(getStringContent(delsvar)));
                    break;
                case DODSDATUM_DELSVAR_ID:
                    assertEquals(zeroFilledDodsdatum, new InternalDate(getStringContent(delsvar)));
                    break;
                case ANTRAFFAT_DOD_DATUM_DELSVAR_ID:
                    assertEquals(antraffatDod, new InternalDate(getStringContent(delsvar)));
                    break;
                default:
                    fail();
            }
        }
    }

    private GrundData createGrundData(String enhetsId, String enhetsnamn, String arbetsplatsKod, String postadress,
        String postNummer, String postOrt, String epost, String telefonNummer, String vardgivarid, String vardgivarNamn,
        String skapadAvFullstandigtNamn, String skapadAvPersonId, String forskrivarKod, String patientPersonId,
        String fornamn, String efternamn, String mellannamn, String patientPostadress, String patientPostnummer,
        String patientPostort, LocalDateTime signeringsdatum) {

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
        Patient patient = new Patient();
        Personnummer personId = Personnummer.createPersonnummer(patientPersonId).get();
        patient.setPersonId(personId);
        patient.setFornamn(fornamn);
        patient.setEfternamn(efternamn);
        patient.setMellannamn(mellannamn);
        patient.setPostadress(patientPostadress);
        patient.setPostnummer(patientPostnummer);
        patient.setPostort(patientPostort);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(signeringsdatum);
        return grundData;
    }
}

