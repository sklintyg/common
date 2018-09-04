/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes_2.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.ts_diabetes_2.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_diabetes_2.model.internal.TsDiabetes2Utlatande;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * Specifically tests the renewal of LISJP where certain fields are nulled out.
 */
@RunWith(MockitoJUnitRunner.class)
public class TsDiabetes2ModuleApiRenewalTest {

    public static final String TESTFILE_UTLATANDE = "TsDiabetes2ModelCompareUtil/utlatande.json";

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @InjectMocks
    private TsDiabetes2ModuleApi moduleApi;

    @Test
    public void testRenewalTransfersAppropriateFieldsToNewDraft() throws ModuleException, IOException {
        String internalModelHolder = IOUtils.toString(new ClassPathResource(
                TESTFILE_UTLATANDE).getInputStream());
        TsDiabetes2Utlatande original = getUtlatandeFromFile();
        String renewalFromTemplate = moduleApi.createRenewalFromTemplate(createCopyHolder(), getUtlatandeFromFile());
        assertNotNull(renewalFromTemplate);

        // Create two instances to compare field by field.
        TsDiabetes2Utlatande renewCopy = new CustomObjectMapper().readValue(renewalFromTemplate, TsDiabetes2Utlatande.class);

        // Blanked out values:
        assertNull(renewCopy.getSignature());

        // Retained values
        //TODO: only tests ovrigt for now
        assertEquals(original.getOvrigt(), renewCopy.getOvrigt());
        assertEquals(original.getTextVersion(), renewCopy.getTextVersion());

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

    private TsDiabetes2Utlatande getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
                TESTFILE_UTLATANDE).getFile(), TsDiabetes2Utlatande.class);
    }

}
