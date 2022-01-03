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
package se.inera.intyg.common.support.modules.support.api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.validate.SamordningsnummerValidator;
import se.inera.intyg.schemas.contract.Personnummer;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests the {@link Personnummer} class in a context where test dependencies to common-support are required.
 */
public class PersonnummerCommonTest {

    @BeforeClass
    public static void setUp() throws Exception {
        ClassLoader.getSystemClassLoader().setClassAssertionStatus("se.inera.intyg.schemas.contract.Personnummer", false);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ClassLoader.getSystemClassLoader().setClassAssertionStatus("se.inera.intyg.schemas.contract.Personnummer", true);
    }

    @Test
    public void testIsSamordningsNummer() throws Exception {
        assertFalse(SamordningsnummerValidator.isSamordningsNummer(createPnr("000000-0000")));
        assertTrue(SamordningsnummerValidator.isSamordningsNummer(createPnr("999999-9999")));
        assertFalse(SamordningsnummerValidator.isSamordningsNummer(createPnr("0000000000")));
        assertTrue(SamordningsnummerValidator.isSamordningsNummer(createPnr("9999999999")));
        assertFalse(SamordningsnummerValidator.isSamordningsNummer(createPnr("000000000000")));
        assertTrue(SamordningsnummerValidator.isSamordningsNummer(createPnr("199999999999")));
        assertFalse(SamordningsnummerValidator.isSamordningsNummer(createPnr("99999999999912345")));
    }

    @Test
    public void testSerializeDeserializePersonnummerAsPartOfComplexType() throws Exception {
        //Given
        final ObjectMapper objectMapper = new ObjectMapper();
        final Personnummer originalPnr = createPnr("191212121212").get();
        final CertificateHolder complexType = new CertificateHolder();
        complexType.setCivicRegistrationNumber(originalPnr);
        complexType.setAdditionalInfo("test text");

        //When
        final String json = objectMapper.writeValueAsString(complexType);

        //Then
        assertTrue(json.contains("\"civicRegistrationNumber\":\"191212121212\""));

        //When
        final CertificateHolder patient = objectMapper.readValue(json, CertificateHolder.class);

        //Then
        assertEquals(originalPnr.getPersonnummer(), patient.getCivicRegistrationNumber().getPersonnummer());
    }

    private Optional<Personnummer> createPnr(String pnr) {
        return Personnummer.createPersonnummer(pnr);
    }

}
