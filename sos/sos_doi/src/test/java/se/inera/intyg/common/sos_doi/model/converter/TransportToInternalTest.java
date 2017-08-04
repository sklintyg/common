package se.inera.intyg.common.sos_doi.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.bind.JAXB;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.intyg.common.sos_doi.model.internal.BidragandeSjukdom;
import se.inera.intyg.common.sos_doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.sos_doi.model.internal.Foljd;
import se.inera.intyg.common.sos_doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.sos_doi.model.internal.Specifikation;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

public class TransportToInternalTest {
    @Test
    public void testConvert() throws Exception {
        String xmlContents = Resources.toString(Resources.getResource("doi.xml"), Charsets.UTF_8);
        Intyg intyg = JAXB.unmarshal(new StringReader(xmlContents), RegisterCertificateType.class).getIntyg();
        DoiUtlatande res = TransportToInternal.convert(intyg);

        assertEquals("1234567", res.getId());
        assertEquals("Olivia", res.getGrundData().getPatient().getFornamn());
        assertEquals("Olsson", res.getGrundData().getPatient().getEfternamn());
        assertEquals("Testgatan 1", res.getGrundData().getPatient().getPostadress());
        assertEquals("111 11", res.getGrundData().getPatient().getPostnummer());
        assertEquals("Teststaden", res.getGrundData().getPatient().getPostort());
        assertEquals("19270310-4321", res.getGrundData().getPatient().getPersonId().getPersonnummer());
        assertEquals(LocalDateTime.of(2015, 12, 7, 15, 48, 5), res.getGrundData().getSigneringsdatum());
        assertEquals("Karl Karlsson", res.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals("SE2321000016-6G5R", res.getGrundData().getSkapadAv().getPersonId());
        assertNull(res.getGrundData().getRelation());

        assertEquals("körkort", res.getIdentitetStyrkt());
        assertEquals(true, res.getDodsdatumSakert());
        assertEquals(new InternalDate(LocalDate.of(2017, 1, 1)), res.getDodsdatum());
        assertEquals(new InternalDate(LocalDate.of(2017, 1, 2)), res.getAntraffatDodDatum());
        assertEquals("kommun", res.getDodsplatsKommun());
        assertEquals(DodsplatsBoende.SJUKHUS, res.getDodsplatsBoende());
        assertEquals(true, res.getBarn());
        assertEquals("terminal dödsorsak", res.getDodsorsak());
        assertEquals(new InternalDate(LocalDate.of(2017, 4, 1)), res.getDodsorsakDatum());
        assertEquals(Specifikation.KRONISK, res.getDodsorsakSpecifikation());
        assertEquals(Foljd.create("foljd 1", new InternalDate(LocalDate.of(2017, 5,1)), Specifikation.KRONISK), res.getFoljd().get(0));
        assertEquals(Foljd.create("foljd 2", new InternalDate(LocalDate.of(2017, 5,2)), Specifikation.PLOTSLIG), res.getFoljd().get(1));
        assertEquals(Foljd.create("foljd 3", new InternalDate(LocalDate.of(2017, 5,3)), Specifikation.KRONISK), res.getFoljd().get(2));
        assertEquals(BidragandeSjukdom.create("bidragande sjukdom 1", new InternalDate(LocalDate.of(2017, 3,1)), Specifikation.KRONISK), res.getBidragandeSjukdomar().get(0));
        assertEquals(BidragandeSjukdom.create("bidragande sjukdom 2", new InternalDate(LocalDate.of(2017, 3,2)), Specifikation.PLOTSLIG), res.getBidragandeSjukdomar().get(1));
        assertEquals(BidragandeSjukdom.create("bidragande sjukdom 3", new InternalDate(LocalDate.of(2017, 3,3)), Specifikation.KRONISK), res.getBidragandeSjukdomar().get(2));
        assertEquals(BidragandeSjukdom.create("bidragande sjukdom 4", new InternalDate(LocalDate.of(2017, 3,4)), Specifikation.PLOTSLIG), res.getBidragandeSjukdomar().get(3));
        assertEquals(BidragandeSjukdom.create("bidragande sjukdom 5", new InternalDate(LocalDate.of(2017, 3,5)), Specifikation.KRONISK), res.getBidragandeSjukdomar().get(4));
        assertEquals(BidragandeSjukdom.create("bidragande sjukdom 6", new InternalDate(LocalDate.of(2017, 3,6)), Specifikation.PLOTSLIG), res.getBidragandeSjukdomar().get(5));
        assertEquals(BidragandeSjukdom.create("bidragande sjukdom 7", new InternalDate(LocalDate.of(2017, 3,7)), Specifikation.KRONISK), res.getBidragandeSjukdomar().get(6));
        assertEquals(BidragandeSjukdom.create("bidragande sjukdom 8", new InternalDate(LocalDate.of(2017, 3,8)), Specifikation.PLOTSLIG), res.getBidragandeSjukdomar().get(7));
        assertTrue(res.getOperation());
        assertEquals(new InternalDate(LocalDate.of(2017, 2, 1)), res.getOperationDatum());
        assertEquals("anledning", res.getOperationAnledning());
        assertTrue(res.getForgiftning());
        assertEquals(ForgiftningOrsak.SJALVMORD, res.getForgiftningOrsak());
        assertEquals(new InternalDate(LocalDate.of(2017, 1, 1)), res.getForgiftningDatum());
        assertEquals("uppkommelse", res.getForgiftningUppkommelse());
        assertEquals(Dodsorsaksgrund.KLINISK_OBDUKTION, res.getGrunder().get(0));
        assertEquals(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN, res.getGrunder().get(1));
        assertEquals("Sverige", res.getLand());
        assertEquals(Tillaggsfraga.create("9001", "tillaggsfraga1"), res.getTillaggsfragor().get(0));
        assertEquals(Tillaggsfraga.create("9002", "tillaggsfraga2"), res.getTillaggsfragor().get(1));
    }
}
