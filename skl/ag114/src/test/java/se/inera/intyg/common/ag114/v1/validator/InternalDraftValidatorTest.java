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
package se.inera.intyg.common.ag114.v1.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.schemas.contract.Personnummer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    private static final String PATIENT_PERSON_ID = "19121212-1212";
    private static final String SKAPADAV_PERSON_ID = "19010101-9801";
    private static final String SKAPADAV_PERSON_NAMN = "Torsten Ericsson";
    private static final String VARDGIVARE_ID = "vardgivareId";
    private static final String VARDGIVARE_NAMN = "vardgivareNamn";
    private static final String ENHET_ID = "enhetId";
    private static final String ENHET_NAMN = "enhetNamn";
    private static final String INTYG_ID = "intyg-1";

    @Mock
    ValidatorUtilSKL validatorUtilSKL;

    @InjectMocks
    InternalDraftValidatorImpl validator;

    List<ValidationMessage> validationMessages;

    Ag114UtlatandeV1.Builder builderTemplate;

    @Before
    public void setUp() throws Exception {
        validationMessages = new ArrayList<>();

        builderTemplate = Ag114UtlatandeV1.builder()
                .setId(INTYG_ID)
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setTextVersion("1.0");
    }

    @Test
    public void testHasGrundData() {
        Ag114UtlatandeV1 utlatandeV1 = builderTemplate.build();
        ValidateDraftResponse validateDraftResponse = validator.validateDraft(utlatandeV1);
        assertNotNull(validateDraftResponse);
    }

    // Kategori 1 – Grund för medicinskt underlag

    // @Test
    // public void validateGrundForMU_Ok() throws Exception {
    // Ag114UtlatandeV1 utlatande = builderTemplate
    // .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
    // .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(2)))
    // .setMotiveringTillInteBaseratPaUndersokning("behövs, ty ingen undersökning")
    // .build();
    //
    // validator.validateGrundForMU(utlatande, validationMessages);
    //
    // assertTrue(validationMessages.isEmpty());
    // }

    // - - - Private scope - - -

    private void assertValidationMessage(String expectedMessage, int index) {
        assertEquals(expectedMessage, validationMessages.get(index).getMessage());
    }

    private void assertValidationMessageDynamicKey(String expectedDynamicKey, int index) {
        assertEquals(expectedDynamicKey, validationMessages.get(index).getDynamicKey());
    }

    private void assertValidationMessageCategory(String expectedCategory, int index) {
        assertEquals(expectedCategory, validationMessages.get(index).getCategory());
    }

    private void assertValidationMessageField(String expectedField, int index) {
        assertEquals(expectedField, validationMessages.get(index).getField());
    }

    private void assertValidationMessageType(ValidationMessageType expectedType, int index) {
        assertTrue(expectedType == validationMessages.get(index).getType());
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(VARDGIVARE_ID);
        vardgivare.setVardgivarnamn(VARDGIVARE_NAMN);

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(ENHET_ID);
        vardenhet.setEnhetsnamn(ENHET_NAMN);
        vardenhet.setVardgivare(vardgivare);

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId(SKAPADAV_PERSON_ID);
        skapadAv.setFullstandigtNamn(SKAPADAV_PERSON_NAMN);

        Patient patient = new Patient();
        patient.setPersonId(createPnr(PATIENT_PERSON_ID));

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }

    private Personnummer createPnr(String civicRegistrationNumber) {
        return Personnummer.createPersonnummer(civicRegistrationNumber).get();
    }

}
