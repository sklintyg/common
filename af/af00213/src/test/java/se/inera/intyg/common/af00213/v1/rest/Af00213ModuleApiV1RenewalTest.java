/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00213.v1.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.af00213.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * Specifically tests the renewal of LISJP where certain fields are nulled out.
 */
@RunWith(MockitoJUnitRunner.class)
public class Af00213ModuleApiV1RenewalTest {

    public static final String TESTFILE_UTLATANDE = "v1/Af00213ModelCompareUtil/utlatande.json";

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @InjectMocks
    private Af00213ModuleApiV1 moduleApi;

    @Test
    public void testRenewalTransfersAppropriateFieldsToNewDraft() throws ModuleException, IOException {
        String internalModelHolder = IOUtils.toString(new ClassPathResource(
            TESTFILE_UTLATANDE).getInputStream());
        Af00213UtlatandeV1 original = getUtlatandeFromFile();
        String renewalFromTemplate = moduleApi.createRenewalFromTemplate(createCopyHolder(), getUtlatandeFromFile());
        assertNotNull(renewalFromTemplate);

        // Create two instances to compare field by field.
        Af00213UtlatandeV1 renewCopy = new CustomObjectMapper().readValue(renewalFromTemplate, Af00213UtlatandeV1.class);

        // Blanked out values:
        assertNull(renewCopy.getSignature());

        // Retained values
        assertEquals(original.getFunktionsnedsattning(), renewCopy.getFunktionsnedsattning());
        assertEquals(original.getAktivitetsbegransning(), renewCopy.getAktivitetsbegransning());
        assertEquals(original.getOvrigt(), renewCopy.getOvrigt());
        assertEquals(original.getUtredningBehandling(), renewCopy.getUtredningBehandling());
        assertEquals(original.getArbetetsPaverkan(), renewCopy.getArbetetsPaverkan());
        assertEquals(original.getTextVersion(), renewCopy.getTextVersion());

    }

    private CreateDraftCopyHolder createCopyHolder() {
        CreateDraftCopyHolder draftCopyHolder = new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
        draftCopyHolder.setIntygTypeVersion("1");
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

    private Af00213UtlatandeV1 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
            TESTFILE_UTLATANDE).getFile(), Af00213UtlatandeV1.class);
    }

}
