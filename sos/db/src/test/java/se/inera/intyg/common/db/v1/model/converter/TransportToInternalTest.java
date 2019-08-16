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
package se.inera.intyg.common.db.v1.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.xml.bind.JAXB;
import org.junit.Test;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.model.InternalDate;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

public class TransportToInternalTest {

    @Test
    public void testConvert() throws Exception {
        String xmlContents = Resources.toString(Resources.getResource("v1/db.xml"), Charsets.UTF_8);
        Intyg intyg = JAXB.unmarshal(new StringReader(xmlContents), RegisterCertificateType.class).getIntyg();
        DbUtlatandeV1 res = TransportToInternal.convert(intyg);

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
        assertEquals(new InternalDate(LocalDate.of(2017, 1, 1)), res.getDodsdatum());
        assertEquals(new InternalDate(LocalDate.of(2017, 1, 2)), res.getAntraffatDodDatum());
        assertEquals("kommun", res.getDodsplatsKommun());
        assertEquals(DodsplatsBoende.SJUKHUS, res.getDodsplatsBoende());
        assertEquals(true, res.getBarn());
        assertEquals(true, res.getExplosivImplantat());
        assertEquals(true, res.getExplosivAvlagsnat());
        assertEquals(Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN, res.getUndersokningYttre());
        assertEquals(new InternalDate(LocalDate.of(2016, 12, 31)), res.getUndersokningDatum());
        assertEquals(true, res.getPolisanmalan());
    }

}
