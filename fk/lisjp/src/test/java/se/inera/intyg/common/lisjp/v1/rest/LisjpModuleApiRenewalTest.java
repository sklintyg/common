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
package se.inera.intyg.common.lisjp.v1.rest;

import static com.helger.commons.mock.CommonsAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Objects;
import org.apache.cxf.helpers.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.lisjp.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
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
@ExtendWith(MockitoExtension.class)
public class LisjpModuleApiRenewalTest {

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private LisjpModuleApiV1 moduleApi;

    public static final String TESTFILE_UTLATANDE = "v1/LisjpModelCompareUtil/utlatande.json";

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testRenewalTransfersAppropriateFieldsToNewDraft() throws ModuleException, IOException {
        IOUtils.toString(new ClassPathResource(TESTFILE_UTLATANDE).getInputStream());
        LisjpUtlatandeV1 original = getUtlatandeFromFile();
        String renewalFromTemplate = moduleApi.createRenewalFromTemplate(createCopyHolder(), getUtlatandeFromFile());
        assertNotNull(renewalFromTemplate);

        // Create two instances to compare field by field.
        LisjpUtlatandeV1 renewCopy = new CustomObjectMapper().readValue(renewalFromTemplate, LisjpUtlatandeV1.class);

        // Blanked out values:
        assertNotEquals(Boolean.TRUE, renewCopy.getKontaktMedFk());
        assertEquals(0, renewCopy.getSjukskrivningar().size());
        assertNull(renewCopy.getAnledningTillKontakt());
        assertNull(renewCopy.getUndersokningAvPatienten());
        assertNull(renewCopy.getTelefonkontaktMedPatienten());
        assertNull(renewCopy.getJournaluppgifter());
        assertNull(renewCopy.getAnnatGrundForMU());
        assertNull(renewCopy.getAnnatGrundForMUBeskrivning());
        assertNull(renewCopy.getMotiveringTillInteBaseratPaUndersokning());
        assertNull(renewCopy.getMotiveringTillTidigtStartdatumForSjukskrivning());
        assertNull(renewCopy.getPrognos());
        assertNull(renewCopy.getArbetstidsforlaggning());
        assertNull(renewCopy.getArbetstidsforlaggningMotivering());

        // Retained values
        assertEquals(original.getAktivitetsbegransning(), renewCopy.getAktivitetsbegransning());
        assertEquals(original.getArbetslivsinriktadeAtgarder(), renewCopy.getArbetslivsinriktadeAtgarder());
        assertEquals(original.getArbetslivsinriktadeAtgarderBeskrivning(), renewCopy.getArbetslivsinriktadeAtgarderBeskrivning());
        assertEquals(original.getArbetsresor(), renewCopy.getArbetsresor());
        assertEquals(original.getNuvarandeArbete(), renewCopy.getNuvarandeArbete());
        assertEquals(original.getOvrigt(), renewCopy.getOvrigt());
        assertEquals(original.getPagaendeBehandling(), renewCopy.getPagaendeBehandling());
        assertEquals(original.getPlaneradBehandling(), renewCopy.getPlaneradBehandling());
        assertEquals(original.getForsakringsmedicinsktBeslutsstod(), renewCopy.getForsakringsmedicinsktBeslutsstod());
        assertEquals(original.getSysselsattning(), renewCopy.getSysselsattning());
        assertEquals(original.getTextVersion(), renewCopy.getTextVersion());

        // Relation
        assertEquals(Objects.requireNonNull(original.getSjukskrivningar().get(0).getPeriod()).getTom().asLocalDate(),
            renewCopy.getGrundData().getRelation().getSistaGiltighetsDatum());
        assertEquals(Objects.requireNonNull(original.getSjukskrivningar().get(0).getSjukskrivningsgrad()).getLabel(),
            renewCopy.getGrundData().getRelation().getSistaSjukskrivningsgrad());
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

    private LisjpUtlatandeV1 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
            TESTFILE_UTLATANDE).getFile(), LisjpUtlatandeV1.class);
    }

}
