/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag114.v1.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.BEDOMNING_SVAR_ID_7;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getPQSvarContent;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1.Builder;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PQType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

@RunWith(MockitoJUnitRunner.class)
public class UtlatandeToIntygTest {

    private final String PNR_TOLVAN = "191212121212";

    @Mock
    private WebcertModuleService webcertModuleService;

    @Test
    public void testConvert() throws Exception {
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

        Ag114UtlatandeV1 utlatande = buildUtlatandeTemplate(intygsId, textVersion, enhetsId, enhetsnamn, patientPersonId,
            skapadAvFullstandigtNamn, skapadAvPersonId, signeringsdatum, arbetsplatsKod, postadress, postNummer, postOrt, epost,
            telefonNummer,
            vardgivarid, vardgivarNamn, forskrivarKod, fornamn, efternamn, mellannamn, patientPostadress, patientPostnummer,
            patientPostort,
            null, null).build();

        Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertEquals(textVersion, intyg.getVersion());
        assertEquals(KvIntygstyp.AG1_14.getCodeValue(), intyg.getTyp().getCode());
        assertEquals(KvIntygstyp.AG1_14.getCodeSystem(), intyg.getTyp().getCodeSystem());
        assertEquals(KvIntygstyp.AG1_14.getDisplayName(), intyg.getTyp().getDisplayName());
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
    public void testConvertWithRelation() throws Exception {
        RelationKod relationKod = RelationKod.FRLANG;
        String relationIntygsId = "relationIntygsId";
        Ag114UtlatandeV1 utlatande = buildUtlatande(relationKod, relationIntygsId);

        Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);
        assertNotNull(intyg.getRelation());
        assertEquals(1, intyg.getRelation().size());
        assertEquals(relationKod.value(), intyg.getRelation().get(0).getTyp().getCode());
        assertNotNull(intyg.getRelation().get(0).getTyp().getCodeSystem());
        assertEquals(relationIntygsId, intyg.getRelation().get(0).getIntygsId().getExtension());
        assertNotNull(intyg.getRelation().get(0).getIntygsId().getRoot());
    }

    @Test
    public void testConvertWithSjukskrivningsGrad() throws Exception {
        final String expectedSjukskrivningsGrad = "80";
        final String expectedSjukskrivningsGradUnit = "%";
        final InternalLocalDateInterval sjukskrivningsPeriod = new InternalLocalDateInterval();
        sjukskrivningsPeriod.setFrom(new InternalDate("2019-01-01"));
        sjukskrivningsPeriod.setTom(new InternalDate("2019-01-31"));

        Builder utlatandeBuilder = buildUtlatandeTemplate();
        utlatandeBuilder.setSjukskrivningsgrad(expectedSjukskrivningsGrad);
        utlatandeBuilder.setSjukskrivningsperiod(sjukskrivningsPeriod);

        Intyg intyg = UtlatandeToIntyg.convert(utlatandeBuilder.build(), webcertModuleService);

        final Svar.Delsvar actualSjukskrivningsGrad = getDelsvar(intyg.getSvar(), BEDOMNING_SVAR_ID_7, SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1);
        assertNotNull(actualSjukskrivningsGrad);
        final PQType actualSjukskrivningPQType = getPQSvarContent(actualSjukskrivningsGrad);
        assertEquals(Double.parseDouble(expectedSjukskrivningsGrad), actualSjukskrivningPQType.getValue(), 0);
        assertEquals(expectedSjukskrivningsGradUnit, actualSjukskrivningPQType.getUnit());
        final Svar.Delsvar actualSjukskrivningsPeriod = getDelsvar(intyg.getSvar(), BEDOMNING_SVAR_ID_7,
            SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2);
        assertNotNull(actualSjukskrivningsPeriod);
    }

    @Test
    public void testConvertWithSjukskrivningsGradNull() throws Exception {
        final InternalLocalDateInterval sjukskrivningsPeriod = new InternalLocalDateInterval();
        sjukskrivningsPeriod.setFrom(new InternalDate("2019-01-01"));
        sjukskrivningsPeriod.setTom(new InternalDate("2019-01-31"));

        Builder utlatandeBuilder = buildUtlatandeTemplate();
        utlatandeBuilder.setSjukskrivningsperiod(sjukskrivningsPeriod);

        Intyg intyg = UtlatandeToIntyg.convert(utlatandeBuilder.build(), webcertModuleService);

        final Svar.Delsvar actualSjukskrivningsGrad = getDelsvar(intyg.getSvar(), BEDOMNING_SVAR_ID_7, SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1);
        assertNull(actualSjukskrivningsGrad);
        final Svar.Delsvar actualSjukskrivningsPeriod = getDelsvar(intyg.getSvar(), BEDOMNING_SVAR_ID_7,
            SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2);
        assertNotNull(actualSjukskrivningsPeriod);
    }

    @Test
    public void testConvertWithSjukskrivningsGradEmpty() throws Exception {
        final String expectedSjukskrivningsGrad = " ";
        final InternalLocalDateInterval sjukskrivningsPeriod = new InternalLocalDateInterval();
        sjukskrivningsPeriod.setFrom(new InternalDate("2019-01-01"));
        sjukskrivningsPeriod.setTom(new InternalDate("2019-01-31"));

        Builder utlatandeBuilder = buildUtlatandeTemplate();
        utlatandeBuilder.setSjukskrivningsgrad(expectedSjukskrivningsGrad);
        utlatandeBuilder.setSjukskrivningsperiod(sjukskrivningsPeriod);

        Intyg intyg = UtlatandeToIntyg.convert(utlatandeBuilder.build(), webcertModuleService);

        final Svar.Delsvar actualSjukskrivningsGrad = getDelsvar(intyg.getSvar(), BEDOMNING_SVAR_ID_7, SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1);
        assertNull(actualSjukskrivningsGrad);
        final Svar.Delsvar actualSjukskrivningsPeriod = getDelsvar(intyg.getSvar(), BEDOMNING_SVAR_ID_7,
            SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2);
        assertNotNull(actualSjukskrivningsPeriod);
    }

    private Svar.Delsvar getDelsvar(List<Svar> svarList, String svarId, String delsvarId) {
        for (Svar svar : svarList) {
            if (svar.getId().equalsIgnoreCase(svarId)) {
                final List<Svar.Delsvar> delsvarList = svar.getDelsvar();
                for (Svar.Delsvar delsvar : delsvarList) {
                    if (delsvar.getId().equalsIgnoreCase(delsvarId)) {
                        return delsvar;
                    }
                }
            }
        }
        return null;
    }

    private Ag114UtlatandeV1 buildUtlatande() {
        return buildUtlatande(null, null);
    }

    private Builder buildUtlatandeTemplate() {
        return buildUtlatandeTemplate(null, null);
    }

    private Ag114UtlatandeV1 buildUtlatande(RelationKod relationKod, String relationIntygsId) {
        return buildUtlatandeTemplate(relationKod, relationIntygsId).build();
    }

    private Builder buildUtlatandeTemplate(RelationKod relationKod, String relationIntygsId) {
        return buildUtlatandeTemplate("intygsId", "textVersion", "enhetsId", "enhetsnamn", PNR_TOLVAN,
            "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now(), "arbetsplatsKod", "postadress", "postNummer",
            "postOrt",
            "epost", "telefonNummer", "vardgivarid", "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn",
            "patientPostadress",
            "patientPostnummer", "patientPostort", relationKod, relationIntygsId);
    }

    private Builder buildUtlatandeTemplate(String intygsId, String textVersion, String enhetsId, String enhetsnamn,
        String patientPersonId, String skapadAvFullstandigtNamn, String skapadAvPersonId, LocalDateTime signeringsdatum,
        String arbetsplatsKod,
        String postadress, String postNummer, String postOrt, String epost, String telefonNummer, String vardgivarid,
        String vardgivarNamn,
        String forskrivarKod, String fornamn, String efternamn, String mellannamn, String patientPostadress, String patientPostnummer,
        String patientPostort, RelationKod relationKod, String relationIntygsId) {
        Builder template = Ag114UtlatandeV1.builder();
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
