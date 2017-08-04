package se.inera.intyg.common.sos_doi.model.converter;

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
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import autovalue.shaded.com.google.common.common.collect.ImmutableList;
import se.inera.intyg.common.sos_doi.model.internal.BidragandeSjukdom;
import se.inera.intyg.common.sos_doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.sos_doi.model.internal.Foljd;
import se.inera.intyg.common.sos_doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.sos_doi.model.internal.Specifikation;
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

public class UtlatandeToIntygTest {
    @Test
    public void testConvert() throws Exception {
        final String intygsId = "intygsid";
        final String textVersion = "textversion";
        final String enhetsId = "enhetsid";
        final String enhetsnamn = "enhetsnamn";
        final String patientPersonId = "pid";
        final String skapadAvFullstandigtNamn = "fullständigt namn";
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
        final String identitetStyrkt = "identitetStyrkt";
        final Boolean dodsdatumSakert = true;
        final InternalDate dodsdatum = new InternalDate(LocalDate.of(2017, 1, 1));
        final InternalDate antraffatDod = new InternalDate(LocalDate.of(2017, 1, 2));
        final String kommun = "kommun";
        final DodsplatsBoende boende = DodsplatsBoende.ORDINART_BOENDE;
        final Boolean barn = true;
        final String dodsorsak = "dodsorsak";
        final InternalDate dodsorsakDatum = new InternalDate(LocalDate.of(2017, 1, 3));
        final Specifikation dodsorsakSpecifikation = Specifikation.KRONISK;
        final List<Foljd> foljd = ImmutableList
                .<Foljd> of(Foljd.create("beskrivning", new InternalDate(LocalDate.of(2017, 1, 4)), Specifikation.KRONISK));
        final List<BidragandeSjukdom> bidragandeSjukdomar = ImmutableList.<BidragandeSjukdom> of(
                BidragandeSjukdom.create("beskrivning", new InternalDate(LocalDate.of(2017, 1, 5)), Specifikation.PLOTSLIG));
        final boolean operation = true;
        final InternalDate operationDatum = new InternalDate(LocalDate.of(2017, 1, 6));
        final String operationAnledning = "anledning";
        final boolean forgiftning = true;
        final ForgiftningOrsak forgiftningOrsak = ForgiftningOrsak.AVSIKTLIGT_VALLAD;
        final InternalDate forgiftningDatum = new InternalDate(LocalDate.of(2017, 1, 5));
        final String forgiftningUppkommelse = "uppkommelse";
        final List<Dodsorsaksgrund> grunder = ImmutableList.<Dodsorsaksgrund> of(Dodsorsaksgrund.KLINISK_OBDUKTION);
        final String land = "land";

        DoiUtlatande utlatande = DoiUtlatande.builder()
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
                .setDodsorsak(dodsorsak)
                .setDodsorsakDatum(dodsorsakDatum)
                .setDodsorsakSpecifikation(dodsorsakSpecifikation)
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
                        assertEquals(dodsorsak, getStringContent(delsvar));
                        break;
                    case DODSORSAK_DATUM_DELSVAR_ID:
                        assertEquals(dodsorsakDatum, new InternalDate(getStringContent(delsvar)));
                        break;
                    case DODSORSAK_SPECIFIKATION_DELSVAR_ID:
                        assertEquals(dodsorsakSpecifikation, Specifikation.fromId(getCVSvarContent(delsvar).getCode()));
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
                        assertEquals(operation, Boolean.parseBoolean(getStringContent(delsvar)));
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
        Personnummer personId = new Personnummer(patientPersonId);
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
