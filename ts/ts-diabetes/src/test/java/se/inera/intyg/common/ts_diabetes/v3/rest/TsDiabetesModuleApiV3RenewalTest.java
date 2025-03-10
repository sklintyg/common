/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v3.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

/**
 * Specifically tests the renewal of LISJP where certain fields are nulled out.
 */
@ExtendWith(MockitoExtension.class)
public class TsDiabetesModuleApiV3RenewalTest {

    public static final String TESTFILE_UTLATANDE = "v3/TsDiabetesV3ModelCompareUtil/utlatande.json";

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @InjectMocks
    private TsDiabetesModuleApiV3 moduleApi;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
    }

    @Test
    void testRenewalTransfersAppropriateFieldsToNewDraft() throws ModuleException, IOException {
        final var original = getUtlatandeFromFile();
        final var renewalFromTemplate = moduleApi.createRenewalFromTemplate(createCopyHolder(), getUtlatandeFromFile());
        assertNotNull(renewalFromTemplate);

        // Create two instances to compare field by field.
        final var renewCopy = new CustomObjectMapper().readValue(renewalFromTemplate, TsDiabetesUtlatandeV3.class);

        // Blanked out values:
        assertNull(renewCopy.getSignature());

        // Retained values
        // TODO: only tests ovrigt for now
        assertEquals(original.getOvrigt(), renewCopy.getOvrigt());
        assertEquals(original.getTextVersion(), renewCopy.getTextVersion());

    }

    private CreateDraftCopyHolder createCopyHolder() {
        final var draftCopyHolder = new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
        draftCopyHolder.setRelation(new Relation());
        return draftCopyHolder;
    }

    private HoSPersonal createHosPersonal() {
        final var hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId("hsaId");
        hosPersonal.setFullstandigtNamn("namn");
        hosPersonal.setVardenhet(new Vardenhet());
        hosPersonal.getVardenhet().setVardgivare(new Vardgivare());
        return hosPersonal;
    }

    private TsDiabetesUtlatandeV3 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
            TESTFILE_UTLATANDE).getFile(), TsDiabetesUtlatandeV3.class);
    }
}
