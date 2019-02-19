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
package se.inera.intyg.common.af00251.v1.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.af00251.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Specifically tests the renewal of AF00251 where certain fields are nulled out.
 */
@RunWith(MockitoJUnitRunner.class)
public class AF00251ModuleApiV1RenewalTest {

    public static final String TESTFILE_UTLATANDE = "renewal/utlatande.json";

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @InjectMocks
    private AF00251ModuleApiV1 moduleApi;

    @Test
    public void testRenewalTransfersAppropriateFieldsToNewDraft() throws ModuleException, IOException {

        AF00251UtlatandeV1 original = getUtlatandeFromFile();
        String renewalFromTemplate = moduleApi.createRenewalFromTemplate(createCopyHolder(), getUtlatandeFromFile());
        assertNotNull(renewalFromTemplate);

        // Create two instances to compare field by field.
        AF00251UtlatandeV1 renewCopy = new CustomObjectMapper().readValue(renewalFromTemplate, AF00251UtlatandeV1.class);

        // Blanked out values:
        assertNull(renewCopy.getSjukfranvaro());
        assertNull(renewCopy.getUndersokningsDatum());
        assertNull(renewCopy.getAnnatDatum());
        assertNull(renewCopy.getAnnatBeskrivning());
        assertNull(renewCopy.getSignature());

        // Retained values
        assertEquals(original.getArbetsmarknadspolitisktProgram(), renewCopy.getArbetsmarknadspolitisktProgram());
        assertEquals(original.getFunktionsnedsattning(), renewCopy.getFunktionsnedsattning());
        assertEquals(original.getAktivitetsbegransning(), renewCopy.getAktivitetsbegransning());
        assertEquals(original.getHarForhinder(), renewCopy.getHarForhinder());
        assertEquals(original.getBegransningSjukfranvaro(), renewCopy.getBegransningSjukfranvaro());
        assertEquals(original.getPrognosAtergang(), renewCopy.getPrognosAtergang());
        assertEquals(original.getTextVersion(), renewCopy.getTextVersion());

        // Relation should contain last one
        assertEquals(original.getSjukfranvaro().get(3).getPeriod().getTom().asLocalDate(), renewCopy.getGrundData().getRelation().getSistaGiltighetsDatum());
        assertEquals(original.getSjukfranvaro().get(3).getNiva().toString(), renewCopy.getGrundData().getRelation().getSistaSjukskrivningsgrad());
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
        hosPersonal.getVardenhet()
                   .setVardgivare(new Vardgivare());
        return hosPersonal;
    }

    private AF00251UtlatandeV1 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
            TESTFILE_UTLATANDE).getFile(), AF00251UtlatandeV1.class);
    }

}
