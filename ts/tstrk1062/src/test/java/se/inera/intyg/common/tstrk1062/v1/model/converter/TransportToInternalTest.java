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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

import static junit.framework.TestCase.*;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.bind.JAXB;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint;
import se.inera.intyg.common.tstrk1062.v1.model.internal.Bedomning;
import se.inera.intyg.common.tstrk1062.v1.model.internal.IntygAvser;
import se.inera.intyg.common.tstrk1062.v1.model.internal.PrognosTillstand;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

public class TransportToInternalTest {

    @Test
    public void convertIntygsTyp() throws Exception {
        final String href = "v1/transport/scenarios/convert/intygAvser.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertEquals("", TsTrk1062EntryPoint.MODULE_ID, utlatande.getTyp());
    }

    @Test
    public void convertIntygAvser() throws Exception {
        final String href = "v1/transport/scenarios/convert/intygAvser.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertNotNull("IntygAvser should not be null", utlatande.getIntygAvser());
        assertEquals("IntygAvser size not equal", 1, utlatande.getIntygAvser().getBehorigheter().size());
        assertEquals("IntygAvser not equal", IntygAvser.BehorighetsTyp.IAV11,
            utlatande.getIntygAvser().getBehorigheter().iterator().next());
    }

    @Test
    public void convertIdKontroll() throws Exception {
        final String href = "v1/transport/scenarios/convert/idKontroll.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertNotNull("IdKontroll should not be null", utlatande.getIdKontroll());
        assertEquals("IdKontroll not equal", IdKontrollKod.ID_KORT, utlatande.getIdKontroll().getTyp());
    }

    @Test
    public void convertDiagnosFritext() throws Exception {
        final String href = "v1/transport/scenarios/convert/diagnosFritext.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertNotNull("DiagnosFritext should not be null", utlatande.getDiagnosFritext());
        assertEquals("DiagnosFritext not equal", "Test", utlatande.getDiagnosFritext().getDiagnosFritext());
        assertEquals("DiagnosFritextArtal not equal", "2018", utlatande.getDiagnosFritext().getDiagnosArtal());
    }

    @Test
    public void convertDiagnosKodad() throws Exception {
        final String href = "v1/transport/scenarios/convert/diagnosKodad.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertNotNull("DiagnosKodad should not be null", utlatande.getDiagnosKodad());
        assertEquals("DiagnosKodad size not equal", 4, utlatande.getDiagnosKodad().size());

        assertEquals("DiagnosKod not equal", "A01", utlatande.getDiagnosKodad().get(0).getDiagnosKod());
        assertEquals("DiagnosKodSystem not equal", "ICD_10_SE", utlatande.getDiagnosKodad().get(0).getDiagnosKodSystem());
        assertEquals("DiagnosBeskrivning not equal", "Tyfoidfeber och paratyfoidfeber",
            utlatande.getDiagnosKodad().get(0).getDiagnosBeskrivning());
        assertEquals("Artal not equal", "2018", utlatande.getDiagnosKodad().get(0).getDiagnosArtal());

        assertEquals("DiagnosKod not equal", "B02", utlatande.getDiagnosKodad().get(1).getDiagnosKod());
        assertEquals("DiagnosKodSystem not equal", "ICD_10_SE", utlatande.getDiagnosKodad().get(1).getDiagnosKodSystem());
        assertEquals("DiagnosBeskrivning not equal", "Bältros", utlatande.getDiagnosKodad().get(1).getDiagnosBeskrivning());
        assertEquals("Artal not equal", "2017", utlatande.getDiagnosKodad().get(1).getDiagnosArtal());

        assertEquals("DiagnosKod not equal", "C03", utlatande.getDiagnosKodad().get(2).getDiagnosKod());
        assertEquals("DiagnosKodSystem not equal", "ICD_10_SE", utlatande.getDiagnosKodad().get(2).getDiagnosKodSystem());
        assertEquals("DiagnosBeskrivning not equal", "Malign tumör i tandköttet",
            utlatande.getDiagnosKodad().get(2).getDiagnosBeskrivning());
        assertEquals("Artal not equal", "2018", utlatande.getDiagnosKodad().get(2).getDiagnosArtal());

        assertEquals("DiagnosKod not equal", "D04", utlatande.getDiagnosKodad().get(3).getDiagnosKod());
        assertEquals("DiagnosKodSystem not equal", "ICD_10_SE", utlatande.getDiagnosKodad().get(3).getDiagnosKodSystem());
        assertEquals("DiagnosBeskrivning not equal", "Cancer in situ i huden", utlatande.getDiagnosKodad().get(3).getDiagnosBeskrivning());
        assertEquals("Artal not equal", "2011", utlatande.getDiagnosKodad().get(3).getDiagnosArtal());
    }

    @Test
    public void convertLakemedelsbehandlingSaknas() throws Exception {
        final String href = "v1/transport/scenarios/convert/lakemedelsbehandlingSaknas.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertNotNull("Lakemedelsbehandling should not be null", utlatande.getLakemedelsbehandling());
        assertEquals("HarHaft not equal", Boolean.FALSE, utlatande.getLakemedelsbehandling().getHarHaft());
        assertNull("Pagar is not null", utlatande.getLakemedelsbehandling().getPagar());
        assertNull("Aktuell is not null", utlatande.getLakemedelsbehandling().getAktuell());
        assertNull("Pagatt is not null", utlatande.getLakemedelsbehandling().getPagatt());
        assertNull("Effekt is not null", utlatande.getLakemedelsbehandling().getEffekt());
        assertNull("Foljsamhet is not null", utlatande.getLakemedelsbehandling().getFoljsamhet());
        assertNull("AvslutadTidpunk is not null", utlatande.getLakemedelsbehandling().getAvslutadTidpunkt());
        assertNull("AvslutadOrsak is not null", utlatande.getLakemedelsbehandling().getAvslutadOrsak());
    }

    @Test
    public void convertLakemedelsbehandlingPagar() throws Exception {
        final String href = "v1/transport/scenarios/convert/lakemedelsbehandlingPagar.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertNotNull("Lakemedelsbehandling should not be null", utlatande.getLakemedelsbehandling());
        assertEquals("HarHaft not equal", Boolean.TRUE, utlatande.getLakemedelsbehandling().getHarHaft());
        assertEquals("Pagar not equal", Boolean.TRUE, utlatande.getLakemedelsbehandling().getPagar());
        assertEquals("Aktuell not equal", "Läkemedel a, b och c", utlatande.getLakemedelsbehandling().getAktuell());
        assertEquals("Pagatt not equal", Boolean.TRUE, utlatande.getLakemedelsbehandling().getPagatt());
        assertEquals("Effekt not equal", Boolean.FALSE, utlatande.getLakemedelsbehandling().getEffekt());
        assertEquals("Foljsamhet not equal", Boolean.TRUE, utlatande.getLakemedelsbehandling().getFoljsamhet());
        assertNull("AvslutadTidpunk is not null", utlatande.getLakemedelsbehandling().getAvslutadTidpunkt());
        assertNull("AvslutadOrsak is not null", utlatande.getLakemedelsbehandling().getAvslutadOrsak());
    }

    @Test
    public void convertLakemedelsbehandlingAvslutad() throws Exception {
        final String href = "v1/transport/scenarios/convert/lakemedelsbehandlingAvslutad.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertNotNull("Lakemedelsbehandling should not be null", utlatande.getLakemedelsbehandling());
        assertEquals("HarHaft not equal", Boolean.TRUE, utlatande.getLakemedelsbehandling().getHarHaft());
        assertEquals("Pagar not equal", Boolean.FALSE, utlatande.getLakemedelsbehandling().getPagar());
        assertNull("Aktuell is not null", utlatande.getLakemedelsbehandling().getAktuell());
        assertNull("Pagatt is not null", utlatande.getLakemedelsbehandling().getPagatt());
        assertNull("Effekt is not null", utlatande.getLakemedelsbehandling().getEffekt());
        assertNull("Foljsamhet is not null", utlatande.getLakemedelsbehandling().getFoljsamhet());
        assertNotNull("AvslutadTidpunk is null", utlatande.getLakemedelsbehandling().getAvslutadTidpunkt());
        assertEquals("AvslutadTidpunkt not equal", "2019-01-10", utlatande.getLakemedelsbehandling().getAvslutadTidpunkt());
        assertEquals("AvslutadOrsak not equal", "Behandlingen var fruktlös", utlatande.getLakemedelsbehandling().getAvslutadOrsak());
    }

    @Test
    public void convertSymptomBedomningTrue() throws Exception {
        final String href = "v1/transport/scenarios/convert/symptomBedomningTrue.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertEquals("BedomningAvSymptom not equal", "Bedömning av aktuella symptom", utlatande.getBedomningAvSymptom());
        assertEquals("PrognosTillstand not equal", PrognosTillstand.PrognosTillstandTyp.JA, utlatande.getPrognosTillstand().getTyp());
    }

    @Test
    public void convertSymptomBedomningFalse() throws Exception {
        final String href = "v1/transport/scenarios/convert/symptomBedomningFalse.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertEquals("BedomningAvSymptom not equal", "Bedömning av aktuella symptom", utlatande.getBedomningAvSymptom());
        assertEquals("PrognosTillstand not equal", PrognosTillstand.PrognosTillstandTyp.NEJ, utlatande.getPrognosTillstand().getTyp());
    }

    @Test
    public void convertSymptomBedomningNI() throws Exception {
        final String href = "v1/transport/scenarios/convert/symptomBedomningNI.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertEquals("BedomningAvSymptom not equal", "Bedömning av aktuella symptom", utlatande.getBedomningAvSymptom());
        assertEquals("PrognosTillstand not equal", PrognosTillstand.PrognosTillstandTyp.KANEJBEDOMA,
            utlatande.getPrognosTillstand().getTyp());
    }

    @Test
    public void convertOvrigt() throws Exception {
        final String href = "v1/transport/scenarios/convert/ovrigaKommentarer.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertEquals("OvrigaKommentarer not equal", "Inga övriga kommentarer", utlatande.getOvrigaKommentarer());
    }

    @Test
    public void convertBedomning() throws Exception {
        final String href = "v1/transport/scenarios/convert/bedomning.xml";
        final Intyg intyg = getIntyg(href);
        final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

        assertNotNull("Utlatande should not be null", utlatande);
        assertNotNull("Bedomning should not be null", utlatande.getBedomning());
        assertEquals("Bedomning size not equal", 1, utlatande.getBedomning().getUppfyllerBehorighetskrav().size());
        assertEquals("Bedomning not equal", Bedomning.BehorighetsTyp.VAR12,
            utlatande.getBedomning().getUppfyllerBehorighetskrav().iterator().next());
    }

    private Intyg getIntyg(String href) throws IOException {
        final String xml = Resources.toString(getResource(href), Charsets.UTF_8);
        return JAXB.unmarshal(new StringReader(xml), RegisterCertificateType.class).getIntyg();
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
