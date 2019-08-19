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
package se.inera.intyg.common.doi.v1.model.converter;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import se.inera.intyg.common.doi.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UPPGIFT_SAKNAS_CODE;
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
    private Boolean dodsdatumSakert = true;
    private final InternalDate dodsdatum = new InternalDate(LocalDate.of(2017, 1, 1));
    private final InternalDate antraffatDod = new InternalDate(LocalDate.of(2017, 1, 2));
    private final String kommun = "kommun";
    private final DodsplatsBoende boende = DodsplatsBoende.ORDINART_BOENDE;
    private final Boolean barn = true;
    private final Dodsorsak dodsorsak = Dodsorsak.create("dodsorsak", new InternalDate(LocalDate.of(2017, 1, 3)), Specifikation.KRONISK);
    private final List<Dodsorsak> foljd = ImmutableList
            .<Dodsorsak>of(Dodsorsak.create("beskrivning", new InternalDate(LocalDate.of(2017, 1, 4)), Specifikation.KRONISK));
    private final List<Dodsorsak> bidragandeSjukdomar = ImmutableList.<Dodsorsak>of(
            Dodsorsak.create("beskrivning", new InternalDate(LocalDate.of(2017, 1, 5)), Specifikation.PLOTSLIG));
    private final OmOperation operation = OmOperation.UPPGIFT_SAKNAS;
    private final InternalDate operationDatum = new InternalDate(LocalDate.of(2017, 1, 6));
    private final String operationAnledning = "anledning";
    private final boolean forgiftning = true;
    private final ForgiftningOrsak forgiftningOrsak = ForgiftningOrsak.AVSIKTLIGT_VALLAD;
    private final InternalDate forgiftningDatum = new InternalDate(LocalDate.of(2017, 1, 5));
    private final String forgiftningUppkommelse = "uppkommelse";
    private final List<Dodsorsaksgrund> grunder = ImmutableList.<Dodsorsaksgrund>of(Dodsorsaksgrund.KLINISK_OBDUKTION);
    private final String land = "land";

    @Test
    public void testConvert() throws Exception {
        DoiUtlatandeV1 utlatande = DoiUtlatandeV1.builder()
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
                .setTerminalDodsorsak(dodsorsak)
                .setFoljd(foljd)
                .setBidragandeSjukdomar(bidragandeSjukdomar)
                .setOperation(operation)
                .setOperationDatum(operationDatum)
                .setOperationAnledning(operationAnledning)
                .setForgiftning(forgiftning)
                .setForgiftningOrsak(forgiftningOrsak)
                .setForgiftningDatum(forgiftningDatum)
                .setForgiftningUppkommelse(forgiftningUppkommelse)
                .setGrunder(grunder)
                .setLand(land)
                .build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertEquals(textVersion, intyg.getVersion());
        assertEquals(utlatande.getTyp().toUpperCase(), intyg.getTyp().getCode());
        assertEquals("b64ea353-e8f6-4832-b563-fc7d46f29548", intyg.getTyp().getCodeSystem());
        assertNotNull(intyg.getTyp().getDisplayName());
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
        assertEquals(11, intyg.getSvar().size());
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
            case DODSORSAK_SVAR_ID:
                for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                    switch (delsvar.getId()) {
                    case DODSORSAK_DELSVAR_ID:
                        assertEquals(dodsorsak.getBeskrivning(), getStringContent(delsvar));
                        break;
                    case DODSORSAK_DATUM_DELSVAR_ID:
                        assertEquals(dodsorsak.getDatum(), new InternalDate(getStringContent(delsvar)));
                        break;
                    case DODSORSAK_SPECIFIKATION_DELSVAR_ID:
                        assertEquals(dodsorsak.getSpecifikation(), Specifikation.fromId(getCVSvarContent(delsvar).getCode()));
                        break;
                    default:
                        fail();
                    }
                }
                break;
            case FOLJD_SVAR_ID:
                for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                    switch (delsvar.getId()) {
                    case FOLJD_OM_DELSVAR_ID:
                        assertEquals(foljd.get(0).getBeskrivning(), getStringContent(delsvar));
                        break;
                    case FOLJD_DATUM_DELSVAR_ID:
                        assertEquals(foljd.get(0).getDatum(), new InternalDate(getStringContent(delsvar)));
                        break;
                    case FOLJD_SPECIFIKATION_DELSVAR_ID:
                        assertEquals(foljd.get(0).getSpecifikation(), Specifikation.fromId(getCVSvarContent(delsvar).getCode()));
                        break;
                    default:
                        fail();
                    }
                }
                break;
            case BIDRAGANDE_SJUKDOM_SVAR_ID:
                for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                    switch (delsvar.getId()) {
                    case BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID:
                        assertEquals(bidragandeSjukdomar.get(0).getBeskrivning(), getStringContent(delsvar));
                        break;
                    case BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID:
                        assertEquals(bidragandeSjukdomar.get(0).getDatum(), new InternalDate(getStringContent(delsvar)));
                        break;
                    case BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID:
                        assertEquals(bidragandeSjukdomar.get(0).getSpecifikation(),
                                Specifikation.fromId(getCVSvarContent(delsvar).getCode()));
                        break;
                    default:
                        fail();
                    }
                }
                break;
            case OPERATION_SVAR_ID:
                for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                    switch (delsvar.getId()) {
                    case OPERATION_OM_DELSVAR_ID:
                        assertEquals(UPPGIFT_SAKNAS_CODE, getCVSvarContent(delsvar).getCode());
                        break;
                    case OPERATION_DATUM_DELSVAR_ID:
                        assertEquals(operationDatum, new InternalDate(getStringContent(delsvar)));
                        break;
                    case OPERATION_ANLEDNING_DELSVAR_ID:
                        assertEquals(operationAnledning, getStringContent(delsvar));
                        break;
                    default:
                        fail();
                    }
                }
                break;
            case FORGIFTNING_SVAR_ID:
                for (Svar.Delsvar delsvar : svar.getDelsvar()) {
                    switch (delsvar.getId()) {
                    case FORGIFTNING_OM_DELSVAR_ID:
                        assertEquals(forgiftning, Boolean.parseBoolean(getStringContent(delsvar)));
                        break;
                    case FORGIFTNING_ORSAK_DELSVAR_ID:
                        assertEquals(forgiftningOrsak, ForgiftningOrsak.valueOf(getCVSvarContent(delsvar).getCode()));
                        break;
                    case FORGIFTNING_DATUM_DELSVAR_ID:
                        assertEquals(forgiftningDatum, new InternalDate(getStringContent(delsvar)));
                        break;
                    case FORGIFTNING_UPPKOMMELSE_DELSVAR_ID:
                        assertEquals(forgiftningUppkommelse, getStringContent(delsvar));
                        break;
                    default:
                        fail();
                    }
                }
                break;
            case GRUNDER_SVAR_ID:
                assertEquals(1, svar.getDelsvar().size());
                assertEquals(GRUNDER_DELSVAR_ID, svar.getDelsvar().get(0).getId());
                assertEquals(grunder.get(0), Dodsorsaksgrund.valueOf(getCVSvarContent(svar.getDelsvar().get(0)).getCode()));
                break;
            case LAND_SVAR_ID:
                assertEquals(1, svar.getDelsvar().size());
                assertEquals(LAND_DELSVAR_ID, svar.getDelsvar().get(0).getId());
                assertEquals(land, getStringContent(svar.getDelsvar().get(0)));
                break;
            default:
                fail();
            }
        }
    }

    @Test
    public void testConvertOsakertDodsdatum() throws Exception {
        InternalDate zeroFilledDodsdatum = new InternalDate("2017-01-00");

        DoiUtlatandeV1 utlatande = DoiUtlatandeV1.builder()
                .setId(intygsId)
                .setTextVersion(textVersion)
                .setGrundData(createGrundData(enhetsId, enhetsnamn, arbetsplatsKod, postadress, postNummer, postOrt, epost, telefonNummer,
                        vardgivarid, vardgivarNamn, skapadAvFullstandigtNamn, skapadAvPersonId, forskrivarKod, patientPersonId, fornamn,
                        efternamn, mellannamn, patientPostadress, patientPostnummer, patientPostort, signeringsdatum))
                .setIdentitetStyrkt(identitetStyrkt)
                .setDodsdatumSakert(false)
                .setDodsdatum(new InternalDate("2017-01"))
                .setAntraffatDodDatum(antraffatDod)
                .setDodsplatsKommun(kommun)
                .setDodsplatsBoende(boende)
                .setBarn(barn)
                .setTerminalDodsorsak(dodsorsak)
                .setFoljd(foljd)
                .setBidragandeSjukdomar(bidragandeSjukdomar)
                .setOperation(operation)
                .setOperationDatum(operationDatum)
                .setOperationAnledning(operationAnledning)
                .setForgiftning(forgiftning)
                .setForgiftningOrsak(forgiftningOrsak)
                .setForgiftningDatum(forgiftningDatum)
                .setForgiftningUppkommelse(forgiftningUppkommelse)
                .setGrunder(grunder)
                .setLand(land)
                .build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        for (Svar svar : intyg.getSvar()) {
            switch (svar.getId()) {
                case DODSDATUM_SVAR_ID:
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
                    break;
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
