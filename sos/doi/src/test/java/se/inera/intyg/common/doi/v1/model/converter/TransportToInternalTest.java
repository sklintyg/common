/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.doi.v1.model.converter;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.doi.model.internal.Specifikation;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.model.InternalDate;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TransportToInternalTest {
    @Test
    public void testConvert() throws Exception {
        String xmlContents = Resources.toString(Resources.getResource("v1/doi.xml"), Charsets.UTF_8);
        Intyg intyg = JAXB.unmarshal(new StringReader(xmlContents), RegisterCertificateType.class).getIntyg();
        DoiUtlatandeV1 res = TransportToInternal.convert(intyg);

        assertEquals("1234567", res.getId());
        assertEquals("Olivia", res.getGrundData().getPatient().getFornamn());
        assertEquals("Olsson", res.getGrundData().getPatient().getEfternamn());
        assertEquals("Testgatan 1", res.getGrundData().getPatient().getPostadress());
        assertEquals("111 11", res.getGrundData().getPatient().getPostnummer());
        assertEquals("Teststaden", res.getGrundData().getPatient().getPostort());
        assertEquals("19270310-4321", res.getGrundData().getPatient().getPersonId().getPersonnummerWithDash());
        assertEquals(LocalDateTime.of(2015, 12, 7, 15, 48, 5), res.getGrundData().getSigneringsdatum());
        assertEquals("Karl Karlsson", res.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals("SE2321000016-6G5R", res.getGrundData().getSkapadAv().getPersonId());
        assertNull(res.getGrundData().getRelation());

        assertEquals("körkort", res.getIdentitetStyrkt());
        assertEquals(false, res.getDodsdatumSakert());
        assertEquals(new InternalDate(LocalDate.of(2017, 4, 1)), res.getDodsdatum());
        assertEquals(new InternalDate(LocalDate.of(2017, 4, 2)), res.getAntraffatDodDatum());
        assertEquals("kommun", res.getDodsplatsKommun());
        assertEquals(DodsplatsBoende.SJUKHUS, res.getDodsplatsBoende());
        assertEquals(true, res.getBarn());
        assertEquals("terminal dödsorsak", res.getTerminalDodsorsak().getBeskrivning());
        assertEquals(new InternalDate(LocalDate.of(2017, 4, 1)), res.getTerminalDodsorsak().getDatum());
        assertEquals(Specifikation.KRONISK, res.getTerminalDodsorsak().getSpecifikation());
        assertEquals(Dodsorsak.create("foljd 1", new InternalDate(LocalDate.of(2016, 5, 1)), Specifikation.KRONISK), res.getFoljd().get(0));
        assertEquals(Dodsorsak.create("foljd 2", new InternalDate(LocalDate.of(2016, 4, 1)), Specifikation.PLOTSLIG),
                res.getFoljd().get(1));
        assertEquals(Dodsorsak.create("foljd 3", new InternalDate(LocalDate.of(2016, 3, 1)), Specifikation.KRONISK), res.getFoljd().get(2));
        assertEquals(Dodsorsak.create("bidragande sjukdom 1", new InternalDate(LocalDate.of(2017, 3, 1)), Specifikation.KRONISK),
                res.getBidragandeSjukdomar().get(0));
        assertEquals(Dodsorsak.create("bidragande sjukdom 2", new InternalDate(LocalDate.of(2017, 3, 2)), Specifikation.PLOTSLIG),
                res.getBidragandeSjukdomar().get(1));
        assertEquals(Dodsorsak.create("bidragande sjukdom 3", new InternalDate(LocalDate.of(2017, 3, 3)), Specifikation.KRONISK),
                res.getBidragandeSjukdomar().get(2));
        assertEquals(Dodsorsak.create("bidragande sjukdom 4", new InternalDate(LocalDate.of(2017, 3, 4)), Specifikation.PLOTSLIG),
                res.getBidragandeSjukdomar().get(3));
        assertEquals(Dodsorsak.create("bidragande sjukdom 5", new InternalDate(LocalDate.of(2017, 3, 5)), Specifikation.KRONISK),
                res.getBidragandeSjukdomar().get(4));
        assertEquals(Dodsorsak.create("bidragande sjukdom 6", new InternalDate(LocalDate.of(2017, 3, 6)), Specifikation.PLOTSLIG),
                res.getBidragandeSjukdomar().get(5));
        assertEquals(Dodsorsak.create("bidragande sjukdom 7", new InternalDate(LocalDate.of(2017, 3, 7)), Specifikation.KRONISK),
                res.getBidragandeSjukdomar().get(6));
        assertEquals(Dodsorsak.create("bidragande sjukdom 8", new InternalDate(LocalDate.of(2017, 3, 8)), Specifikation.PLOTSLIG),
                res.getBidragandeSjukdomar().get(7));
        assertEquals(OmOperation.JA, res.getOperation());
        assertEquals(new InternalDate(LocalDate.of(2016, 2, 1)), res.getOperationDatum());
        assertEquals("anledning", res.getOperationAnledning());
        assertTrue(res.getForgiftning());
        assertEquals(ForgiftningOrsak.SJALVMORD, res.getForgiftningOrsak());
        assertEquals(new InternalDate(LocalDate.of(2017, 1, 1)), res.getForgiftningDatum());
        assertEquals("uppkommelse", res.getForgiftningUppkommelse());
        assertEquals(Dodsorsaksgrund.KLINISK_OBDUKTION, res.getGrunder().get(0));
        assertEquals(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN, res.getGrunder().get(1));
        assertEquals("Sverige", res.getLand());
    }
}
