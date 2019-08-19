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
package se.inera.intyg.common.support.model.converter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder;

import org.junit.Test;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;

public class WebcertModelFactoryUtilTest {

    private static final String TOLVAN = "19121212-1212";
    private static final String LILL_TOLVAN = "20121212-1212";
    private static final String SAMORDNINGSNUMMER = "19121272-1212";

    @Test
    public void testPopulateGrunddataFromCreateDraftCopyHolderBaseCase() throws ConverterException {
        final String personId = "personId";
        final String enhetsId = "enhetsId";
        final String meddelandeId = "relationMeddelandeId";

        GrundData target = new GrundData();
        HoSPersonal targetPerson = new HoSPersonal();
        Vardenhet targetVardenhet = new Vardenhet();
        targetVardenhet.setEnhetsid("targetEnhetsid");
        targetPerson.setVardenhet(targetVardenhet);
        target.setSkapadAv(targetPerson);

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setPersonId(personId);
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        skapadAv.setVardenhet(vardenhet);
        CreateDraftCopyHolder source = new CreateDraftCopyHolder("certificateId", skapadAv);
        Relation relation = new Relation();
        relation.setMeddelandeId(meddelandeId);
        source.setRelation(relation);

        populateGrunddataFromCreateDraftCopyHolder(target, source);

        assertEquals(personId, target.getSkapadAv().getPersonId());
        assertEquals(enhetsId, target.getSkapadAv().getVardenhet().getEnhetsid());
        assertEquals(meddelandeId, target.getRelation().getMeddelandeId());
    }

    @Test
    public void testPopulateGrunddataFromCreateDraftCopyHolderCopiesOverMissingInfo() throws ConverterException {
        final String enhetsId = "enhetsId";
        final String postadress = "postadress";
        final String postnummer = "postnummer";
        final String postort = "postort";
        final String telefonnummer = "telefonnummer";

        GrundData target = new GrundData();
        HoSPersonal targetPerson = new HoSPersonal();
        Vardenhet targetVardenhet = new Vardenhet();
        targetVardenhet.setEnhetsid(enhetsId);
        targetVardenhet.setPostadress(postadress);
        targetVardenhet.setPostnummer(postnummer);
        targetVardenhet.setPostort(postort);
        targetVardenhet.setTelefonnummer(telefonnummer);
        targetPerson.setVardenhet(targetVardenhet);
        target.setSkapadAv(targetPerson);

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setPersonId("personId");
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        skapadAv.setVardenhet(vardenhet);
        CreateDraftCopyHolder source = new CreateDraftCopyHolder("certificateId", skapadAv);

        populateGrunddataFromCreateDraftCopyHolder(target, source);

        assertEquals(enhetsId, target.getSkapadAv().getVardenhet().getEnhetsid());
        assertEquals(postadress, target.getSkapadAv().getVardenhet().getPostadress());
        assertEquals(postnummer, target.getSkapadAv().getVardenhet().getPostnummer());
        assertEquals(postort, target.getSkapadAv().getVardenhet().getPostort());
        assertEquals(telefonnummer, target.getSkapadAv().getVardenhet().getTelefonnummer());
    }

    @Test
    public void testPopulateGrunddataFromCreateDraftCopyHolderDoesntOverwriteHsaInfo() throws ConverterException {
        final String enhetsId = "enhetsId";
        final String postadress = "postadress";
        final String postnummer = "postnummer";
        final String postort = "postort";
        final String telefonnummer = "telefonnummer";

        GrundData target = new GrundData();
        HoSPersonal targetPerson = new HoSPersonal();
        Vardenhet targetVardenhet = new Vardenhet();
        targetVardenhet.setEnhetsid(enhetsId);
        targetVardenhet.setPostadress("otherPostadress");
        targetVardenhet.setPostnummer("otherPostnummer");
        targetVardenhet.setPostort("otherPostort");
        targetVardenhet.setTelefonnummer("otherTelefonnummer");
        targetPerson.setVardenhet(targetVardenhet);
        target.setSkapadAv(targetPerson);

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setPersonId("personId");
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setPostadress(postadress);
        vardenhet.setPostnummer(postnummer);
        vardenhet.setPostort(postort);
        vardenhet.setTelefonnummer(telefonnummer);
        vardenhet.setEnhetsid(enhetsId);
        skapadAv.setVardenhet(vardenhet);
        CreateDraftCopyHolder source = new CreateDraftCopyHolder("certificateId", skapadAv);

        populateGrunddataFromCreateDraftCopyHolder(target, source);

        assertEquals(enhetsId, target.getSkapadAv().getVardenhet().getEnhetsid());
        assertEquals(postadress, target.getSkapadAv().getVardenhet().getPostadress());
        assertEquals(postnummer, target.getSkapadAv().getVardenhet().getPostnummer());
        assertEquals(postort, target.getSkapadAv().getVardenhet().getPostort());
        assertEquals(telefonnummer, target.getSkapadAv().getVardenhet().getTelefonnummer());
    }

    @Test
    public void testPopulateGrunddataFromCreateDraftCopyHolderDifferentVardenhet() throws ConverterException {
        final String enhetsId = "enhetsId";
        GrundData target = new GrundData();
        HoSPersonal targetPerson = new HoSPersonal();
        Vardenhet targetVardenhet = new Vardenhet();
        targetVardenhet.setEnhetsid("differentEnhetsId");
        targetVardenhet.setPostadress("postadress");
        targetVardenhet.setPostnummer("postnummer");
        targetVardenhet.setPostort("postort");
        targetVardenhet.setTelefonnummer("telefonnummer");
        targetPerson.setVardenhet(targetVardenhet);
        target.setSkapadAv(targetPerson);

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setPersonId("personId");
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);

        skapadAv.setVardenhet(vardenhet);
        CreateDraftCopyHolder source = new CreateDraftCopyHolder("certificateId", skapadAv);

        populateGrunddataFromCreateDraftCopyHolder(target, source);

        assertEquals(enhetsId, target.getSkapadAv().getVardenhet().getEnhetsid());
        assertNull(target.getSkapadAv().getVardenhet().getPostadress());
        assertNull(target.getSkapadAv().getVardenhet().getPostnummer());
        assertNull(target.getSkapadAv().getVardenhet().getPostort());
        assertNull(target.getSkapadAv().getVardenhet().getTelefonnummer());
    }

//    @Test
//    public void testBuildNewEffectivePatientAppliesAllNewValid() throws ConverterException {
//        Patient oldPatient = createPatient();
//        Patient newPatient = createNewPatient();
//
//        final Patient mergedPatient = WebcertModelFactoryUtil.buildNewEffectivePatient(oldPatient, newPatient);
//        assertEquals(newPatient, mergedPatient);
//    }
//
//    @Test
//    public void testBuildNewEffectivePatientSkipsInvalidParameters() throws ConverterException {
//        Patient oldPatient = createPatient();
//        Patient newPatient = new Patient();
//
//        final Patient mergedPatient = WebcertModelFactoryUtil.buildNewEffectivePatient(oldPatient, newPatient);
//        assertEquals(oldPatient, mergedPatient);
//    }
//
//    @Test
//    public void testBuildNewEffectivePatientSkipsInvalidPersonnummer() throws ConverterException {
//        Patient oldPatient = createPatient();
//        Patient newPatient = createNewPatient();
//        newPatient.setPersonId(new Personnummer("abc123"));
//
//        final Patient mergedPatient = WebcertModelFactoryUtil.buildNewEffectivePatient(oldPatient, newPatient);
//        assertNotEquals(newPatient, mergedPatient);
//        assertEquals(TOLVAN, mergedPatient.getPersonId().getPersonnummer());
//
//    }
//
//    @Test
//    public void testBuildNewEffectivePatientAllowsSamordningnummer() throws ConverterException {
//        Patient oldPatient = createPatient();
//        Patient newPatient = createNewPatient();
//        newPatient.setPersonId(new Personnummer(SAMORDNINGSNUMMER));
//
//        final Patient mergedPatient = WebcertModelFactoryUtil.buildNewEffectivePatient(oldPatient, newPatient);
//        assertEquals(SAMORDNINGSNUMMER, mergedPatient.getPersonId().getPersonnummer());
//
//    }
//
//    private Patient createNewPatient() {
//        Patient newPatient = new Patient();
//        newPatient.setFornamn("updated firstName");
//        newPatient.setEfternamn("updated lastName");
//        newPatient.setMellannamn("updated middle-name");
//        newPatient.setFullstandigtNamn("updated full name");
//        newPatient.setPersonId(new Personnummer(LILL_TOLVAN));
//        newPatient.setPostadress("updated postal address");
//        newPatient.setPostnummer("1111111");
//        newPatient.setPostort("updated post city");
//        return newPatient;
//    }
//
//    private Patient createPatient() {
//        Patient patient = new Patient();
//        patient.setFornamn("firstname");
//        patient.setMellannamn("middlename");
//        patient.setEfternamn("lastname");
//        patient.setFullstandigtNamn("firstname middlename lastname");
//        patient.setPersonId(new Personnummer(TOLVAN));
//        patient.setPostadress("postal address");
//        patient.setPostnummer("000000");
//        patient.setPostort("post city");
//        return patient;
//    }

}
