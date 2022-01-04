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
package se.inera.intyg.common.fk7263.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.fk7263.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Specifically tests the renewal of FK7263 where certain fields are nulled out.
 */
@RunWith(MockitoJUnitRunner.class)
public class Fk7263ModuleApiRenewalTest {

    public static final String TESTFILE_UTLATANDE = "Fk7263ModuleApiTest/utlatande.json";

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @InjectMocks
    private Fk7263ModuleApi moduleApi;

    @Test
    public void testRenewalTransfersAppropriateFieldsToNewDraft() throws ModuleException, IOException {
        // This modifies the template for some bizarre reason.
        String renewalFromTemplate = moduleApi.createRenewalFromTemplate(createCopyHolder(), getUtlatandeFromFile());

        // Create two instances to compare field by field.
        Fk7263Utlatande original = getUtlatandeFromFile();
        Fk7263Utlatande renewCopy = new CustomObjectMapper().readValue(renewalFromTemplate, Fk7263Utlatande.class);

        assertNotNull(renewalFromTemplate);

        // Blanked out values:
        assertFalse(renewCopy.isKontaktMedFk());
        assertNull(renewCopy.getNedsattMed25());
        assertNull(renewCopy.getNedsattMed25Beskrivning());
        assertNull(renewCopy.getNedsattMed50());
        assertNull(renewCopy.getNedsattMed50Beskrivning());
        assertNull(renewCopy.getNedsattMed75());
        assertNull(renewCopy.getNedsattMed75Beskrivning());
        assertNull(renewCopy.getNedsattMed100());
        assertNull(renewCopy.getTjanstgoringstid());
        assertNull(renewCopy.getUndersokningAvPatienten());
        assertNull(renewCopy.getTelefonkontaktMedPatienten());
        assertNull(renewCopy.getJournaluppgifter());
        assertNull(renewCopy.getAnnanReferens());
        assertNull(renewCopy.getAnnanReferensBeskrivning());

        // Retained values
        assertEquals(original.isArbetsloshet(), renewCopy.isArbetsloshet());
        assertEquals(original.isAvstangningSmittskydd(), renewCopy.isAvstangningSmittskydd());
        assertEquals(original.isForaldrarledighet(), renewCopy.isForaldrarledighet());
        assertEquals(original.isNuvarandeArbete(), renewCopy.isNuvarandeArbete());
        assertEquals(original.isRekommendationKontaktArbetsformedlingen(), renewCopy.isRekommendationKontaktArbetsformedlingen());
        assertEquals(original.isRekommendationKontaktForetagshalsovarden(), renewCopy.isRekommendationKontaktForetagshalsovarden());
        assertEquals(original.isRekommendationOvrigtCheck(), renewCopy.isRekommendationOvrigtCheck());
        assertEquals(original.isRessattTillArbeteAktuellt(), renewCopy.isRessattTillArbeteAktuellt());
        assertEquals(original.isRessattTillArbeteEjAktuellt(), renewCopy.isRessattTillArbeteEjAktuellt());
        assertEquals(original.getKommentar(), renewCopy.getKommentar());
        assertEquals(original.getAktivitetsbegransning(), renewCopy.getAktivitetsbegransning());
        assertEquals(original.getArbetsformagaPrognos(), renewCopy.getArbetsformagaPrognos());
        assertEquals(original.getArbetsformagaPrognosGarInteAttBedomaBeskrivning(),
            renewCopy.getArbetsformagaPrognosGarInteAttBedomaBeskrivning());
        assertEquals(original.getAnnanAtgard(), renewCopy.getAnnanAtgard());
        assertEquals(original.getAtgardInomSjukvarden(), renewCopy.getAtgardInomSjukvarden());
        assertEquals(original.getNuvarandeArbetsuppgifter(), renewCopy.getNuvarandeArbetsuppgifter());
        assertEquals(original.getDiagnosBeskrivning(), renewCopy.getDiagnosBeskrivning());
        assertEquals(original.getDiagnosBeskrivning1(), renewCopy.getDiagnosBeskrivning1());
        assertEquals(original.getDiagnosBeskrivning2(), renewCopy.getDiagnosBeskrivning2());
        assertEquals(original.getDiagnosBeskrivning3(), renewCopy.getDiagnosBeskrivning3());
        assertEquals(original.getDiagnosKod(), renewCopy.getDiagnosKod());
        assertEquals(original.getDiagnosKod2(), renewCopy.getDiagnosKod2());
        assertEquals(original.getDiagnosKod3(), renewCopy.getDiagnosKod3());
        assertEquals(original.getDiagnosKodsystem1(), renewCopy.getDiagnosKodsystem1());
        assertEquals(original.getDiagnosKodsystem2(), renewCopy.getDiagnosKodsystem2());
        assertEquals(original.getDiagnosKodsystem3(), renewCopy.getDiagnosKodsystem3());
        assertEquals(original.getFunktionsnedsattning(), renewCopy.getFunktionsnedsattning());
        assertEquals(original.getPrognosBedomning(), renewCopy.getPrognosBedomning());
        assertEquals(original.getRehabilitering(), renewCopy.getRehabilitering());
        assertEquals(original.getRekommendationOvrigt(), renewCopy.getRekommendationOvrigt());
        assertEquals(original.getSamsjuklighet(), renewCopy.getSamsjuklighet());
        assertEquals(original.getSjukdomsforlopp(), renewCopy.getSjukdomsforlopp());
        assertEquals(original.getTextVersion(), renewCopy.getTextVersion());

        original.getNedsattMed25().getTom().asLocalDate();
        // Relation
        assertEquals(original.getNedsattMed25().getTom().asLocalDate(), renewCopy.getGrundData().getRelation().getSistaGiltighetsDatum());
        assertEquals("NEDSATT_MED_1_4", renewCopy.getGrundData().getRelation().getSistaSjukskrivningsgrad());
    }

    private CreateDraftCopyHolder createCopyHolder() {
        CreateDraftCopyHolder draftCopyHolder = new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
        draftCopyHolder.setRelation(new Relation());
        return draftCopyHolder;
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId("hsaId");
        hosPersonal.setFullstandigtNamn("namn");
        hosPersonal.setVardenhet(new Vardenhet());
        hosPersonal.getVardenhet().setVardgivare(new Vardgivare());
        return hosPersonal;
    }

    private Fk7263Utlatande getUtlatandeFromFile() throws IOException {
        String internalModelHolder = IOUtils.toString(new ClassPathResource(
            TESTFILE_UTLATANDE).getInputStream());
        return new CustomObjectMapper().readValue(internalModelHolder, Fk7263Utlatande.class);
    }

}
