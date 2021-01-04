/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.luse.v1.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.luse.v1.model.internal.LuseUtlatandeV1;
import se.inera.intyg.common.luse.v1.rest.LuseModuleApiV1;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TransportToInternalTest {

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;
    private RegisterCertificateValidator validator = new RegisterCertificateValidator(LuseModuleApiV1.SCHEMATRON_FILE);

    private WebcertModuleService webcertModuleService;

    @Before
    public void setup() {
        webcertModuleService = Mockito.mock(WebcertModuleService.class);
        when(webcertModuleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);
    }

    private static LuseUtlatandeV1 getUtlatande() {
        LuseUtlatandeV1.Builder utlatande = LuseUtlatandeV1.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");
        utlatande.setUndersokningAvPatienten(new InternalDate(LocalDate.now()));
        utlatande.setKannedomOmPatient(new InternalDate(LocalDate.now()));
        utlatande.setUnderlagFinns(true);
        utlatande.setUnderlag(asList(Underlag.create(Underlag.UnderlagsTyp.OVRIGT, new InternalDate(LocalDate.now()), "plats 1"),
            Underlag.create(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(LocalDate.now().plusWeeks(2)),
                "plats 2")));
        utlatande.setSjukdomsforlopp("Snabbt");
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra")),
            Diagnos.create("S48", "ICD_10_SE", "Klämskada arm", "Klämskada arm")));
        utlatande.setDiagnosgrund("Ingen som vet");
        utlatande.setNyBedomningDiagnosgrund(true);
        utlatande.setDiagnosForNyBedomning("Diagnos för ny bedömning");
        utlatande.setFunktionsnedsattningIntellektuell("Bra");
        utlatande.setFunktionsnedsattningKommunikation("Tyst");
        utlatande.setFunktionsnedsattningKoncentration("Noll");
        utlatande.setFunktionsnedsattningPsykisk("Lite ledsen");
        utlatande.setFunktionsnedsattningSynHorselTal("Vitt");
        utlatande.setFunktionsnedsattningBalansKoordination("Tyst");
        utlatande.setFunktionsnedsattningAnnan("Kan inte smida");
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setAvslutadBehandling("Gammal medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setMedicinskaForutsattningarForArbete("Svårt");
        utlatande.setFormagaTrotsBegransning("Dansa");
        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(true);
        utlatande.setAnledningTillKontakt("Känner mig ensam");
        utlatande.setTillaggsfragor(asList(Tillaggsfraga.create("9001", "Svar text 1"), Tillaggsfraga.create("9002", "Svar text 2")));
        return utlatande.build();
    }

    @Before
    public void suitSetup() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class);
        objectFactory = new ObjectFactory();
    }

    @Test
    public void endToEnd() throws Exception {
        LuseUtlatandeV1 originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande, webcertModuleService);
        LuseUtlatandeV1 convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());

        String xml = xmlToString(transportCertificate);
        SchematronOutputType valResult = validator.validateSchematron(new StreamSource(new StringReader(xml)));

        assertTrue(SVRLHelper.getAllFailedAssertions(valResult).size() == 0);
        assertEquals(originalUtlatande, convertedIntyg);
    }

    private String xmlToString(RegisterCertificateType registerCertificate) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(registerCertificate);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        return stringWriter.toString();
    }

}
