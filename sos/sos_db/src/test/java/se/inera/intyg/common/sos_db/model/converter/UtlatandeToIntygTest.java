package se.inera.intyg.common.sos_db.model.converter;

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

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.junit.Test;

import se.inera.intyg.common.sos_db.model.internal.DodsbevisUtlatande;
import se.inera.intyg.common.sos_db.model.internal.Undersokning;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public class UtlatandeToIntygTest {
    @Test
    public void testConvert() throws Exception {
        /*
         * PLEASE NOTE: This works because when content in delsvar retains information about which object has been saved
         * here. When using JAXB everything is Strings.
         */
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
        final Boolean explosivImplantat = true;
        final Boolean explosivAvlagsnat = true;
        final Boolean undersokningYttre = true;
        final Undersokning undersokningDetaljer = Undersokning.UNDERSOKNING_SKA_GORAS;
        final InternalDate undersokningDatum = new InternalDate(LocalDate.of(2017, 1, 3));
        final Boolean polisanmalan = true;

        DodsbevisUtlatande utlatande = DodsbevisUtlatande.builder()
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
                .setUndersokningDetaljer(undersokningDetaljer)
                .setUndersokningDatum(undersokningDatum)
                .setPolisanmalan(polisanmalan)
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
        assertEquals(7, intyg.getSvar().size());
        for (Svar svar : intyg.getSvar()) {
            switch (svar.getId()) {
            case IDENTITET_STYRKT_SVAR_ID:
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
                        assertEquals(undersokningYttre, Boolean.parseBoolean(getStringContent(delsvar)));
                        break;
                    case UNDERSOKNING_DETALJER_DELSVAR_ID:
                        assertEquals(undersokningDetaljer, Undersokning.valueOf(getCVSvarContent(delsvar).getCode()));
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
                assertEquals(POLISANMALAN_DELSVAR_ID, svar.getDelsvar().get(0).getId());
                assertEquals(polisanmalan, Boolean.parseBoolean(getStringContent(svar.getDelsvar().get(0))));
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

    private JAXBElement<?> wrapJaxb(RegisterCertificateType ws) {
        JAXBElement<?> jaxbElement = new JAXBElement<>(
                new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3", "RegisterCertificate"),
                RegisterCertificateType.class, ws);
        return jaxbElement;
    }
}
