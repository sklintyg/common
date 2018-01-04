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
package se.inera.intyg.common.support.modules.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.ArbetsplatsKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Befattning;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.HsaId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.IntygsStatus;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Vardgivare;

public class TransportConverterUtilTest {

    /* Exception messages */
    private static final String ERROR_WHILE_CONVERTING_CV_TYPE_MISSING_MANDATORY_FIELD = "Error while converting CVType, missing mandatory field";
    private static final String UNEXPECTED_OUTCOME_WHILE_CONVERTING_CV_TYPE = "Unexpected outcome while converting CVType";

    private static final String NAMESPACE = "urn:riv:clinicalprocess:healthcond:certificate:types:2";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGetMetaData() {
        final String intygId = "intygId";
        final String intygstyp = "LUSE";
        String skapadAvFullstandigtNamn = "skapad av namn";
        String enhetsnamn = "enhetsnamn";
        final LocalDateTime signeringstidpunkt = LocalDateTime.now().minusDays(1);
        final LocalDateTime statustidpunkt = LocalDateTime.now();
        final String additionalInfo = "Additional Info";
        IntygsStatus status = new IntygsStatus();
        status.setPart(new Part());
        status.getPart().setCode("FKASSA");
        status.setStatus(new Statuskod());
        status.getStatus().setCode(StatusKod.SENTTO.name());
        status.setTidpunkt(statustidpunkt);

        Intyg intyg = buildIntyg(intygId, intygstyp, skapadAvFullstandigtNamn, enhetsnamn, signeringstidpunkt, Arrays.asList(status));


        CertificateMetaData res = TransportConverterUtil.getMetaData(intyg, additionalInfo);
        assertNotNull(res);
        assertEquals(intygId, res.getCertificateId());
        assertEquals(intygstyp.toLowerCase(), res.getCertificateType());
        assertEquals(skapadAvFullstandigtNamn, res.getIssuerName());
        assertEquals(enhetsnamn, res.getFacilityName());
        assertEquals(signeringstidpunkt, res.getSignDate());
        assertEquals(1, res.getStatus().size());
        assertEquals(CertificateState.SENT, res.getStatus().get(0).getType());
        assertEquals("FKASSA", res.getStatus().get(0).getTarget());
        assertEquals(statustidpunkt, res.getStatus().get(0).getTimestamp());
        assertEquals(additionalInfo, res.getAdditionalInfo());
        assertTrue(res.isAvailable());
    }
    @Test
    public void testGetMetaDataAvailableStatuses() {
        final LocalDateTime time = LocalDateTime.now();
        IntygsStatus skickad = buildStatus(time, "", StatusKod.RECEIV);
        IntygsStatus arkiverad = buildStatus(time.plusHours(1), "", StatusKod.DELETE);
        IntygsStatus nullDate = buildStatus(null, "", StatusKod.SENTTO);

        Intyg intyg = buildIntyg(Arrays.asList(skickad, nullDate, arkiverad));

        CertificateMetaData res = TransportConverterUtil.getMetaData(intyg, "");
        assertFalse("Should have been unavailable with just a single DELETE", res.isAvailable());

        //Add a restored event
        intyg.getStatus().add(buildStatus(time.plusHours(2),"", StatusKod.RESTOR));
        res = TransportConverterUtil.getMetaData(intyg, "");
        assertTrue("Should have been available with a later RESTOR", res.isAvailable());

        //Add another archive  event
        intyg.getStatus().add(buildStatus(time.plusHours(3),"", StatusKod.DELETE));
        res = TransportConverterUtil.getMetaData(intyg, "");
        assertFalse("Should have been unavailable with a later DELETE", res.isAvailable());

        //Add another (to early) restored event
        intyg.getStatus().add(buildStatus(time.minusHours(2),"", StatusKod.RESTOR));
        res = TransportConverterUtil.getMetaData(intyg, "");
        assertFalse("Should have been unavailable with a to early RESTOR", res.isAvailable());

        //Finally, add add another restored event
        intyg.getStatus().add(buildStatus(time.plusHours(5),"", StatusKod.RESTOR));
        res = TransportConverterUtil.getMetaData(intyg, "");
        assertTrue("Should have been available with a RESTOR as lastest event", res.isAvailable());
    }

    private IntygsStatus buildStatus(LocalDateTime statustidpunkt, String partCode, StatusKod statusKod) {
        final IntygsStatus s = new IntygsStatus();
        s.setPart(new Part());
        s.getPart().setCode(partCode);
        s.setStatus(new Statuskod());
        s.getStatus().setCode(statusKod.name());
        s.setTidpunkt(statustidpunkt);
        return s;
    }


    private Intyg buildIntyg(List<IntygsStatus> statuses) {
        return buildIntyg("1","fk7263", "Doctor No", "enhet1", LocalDateTime.now(), statuses);
    }

    private Intyg buildIntyg(String intygId, String intygstyp, String skapadAvFullstandigtNamn, String enhetsnamn, LocalDateTime signeringstidpunkt, List<IntygsStatus> statuses) {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension(intygId);
        intyg.setTyp(new TypAvIntyg());
        intyg.getTyp().setCode(intygstyp);
        intyg.setSkapadAv(new HosPersonal());
        intyg.getSkapadAv().setFullstandigtNamn(skapadAvFullstandigtNamn);
        intyg.getSkapadAv().setEnhet(new Enhet());
        intyg.getSkapadAv().getEnhet().setEnhetsnamn(enhetsnamn);
        intyg.setSigneringstidpunkt(signeringstidpunkt);
        intyg.getStatus().addAll(statuses);
        return intyg;
    }

    @Test
    public void testGetCVTypeContentSuccess() throws Exception {
        String code = "1";
        String codeSystem = "TEST";
        Delsvar delsvar = buildCVTypeDelsvar(code, codeSystem, null, null, null, null);
        CVType actual = TransportConverterUtil.getCVSvarContent(delsvar);
        CVType expected = buildCVType(code, codeSystem, null, null, null, null);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getCodeSystem(), actual.getCodeSystem());
    }

    @Test
    public void testGetStringContentWithWhitespaceSuccess() throws ParserConfigurationException {
        String expected = "String with freetext";
        Delsvar delsvar = buildStringDelsvar(Arrays.asList("\n", "    ", expected, "\n", "    "));
        String actual = TransportConverterUtil.getStringContent(delsvar);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCVTypeContentWithOptionalSuccess() throws Exception {
        String code = "1";
        String codeSystem = "TEST";
        String codeSystemName = "NAME";
        String codeSystemVersion = "VERSION";
        String displayName = "DISPLAYNAME";
        String originalText = "ORIGINAL TEXT";
        Delsvar delsvar = buildCVTypeDelsvar(code, codeSystem, codeSystemName, codeSystemVersion, displayName, originalText);
        CVType actual = TransportConverterUtil.getCVSvarContent(delsvar);
        CVType expected = buildCVType(code, codeSystem, codeSystemName, codeSystemVersion, displayName, originalText);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getCodeSystem(), actual.getCodeSystem());
        assertEquals(expected.getCodeSystemName(), actual.getCodeSystemName());
        assertEquals(expected.getCodeSystemVersion(), actual.getCodeSystemVersion());
        assertEquals(expected.getDisplayName(), actual.getDisplayName());
        assertEquals(expected.getOriginalText(), actual.getOriginalText());
    }

    @Test
    public void testGetCVTypeDifferentContentFails() throws Exception {
        String code = "1";

        Delsvar delsvar = buildCVTypeDelsvar(code, "ANOTHER", null, null, null, null);
        CVType actual = TransportConverterUtil.getCVSvarContent(delsvar);

        CVType expected = buildCVType(code, "TEST", null, null, null, null);
        assertEquals(expected.getCode(), actual.getCode());
        assertFalse(expected.getCodeSystem().equals(actual.getCodeSystem()));
    }

    /* Breaking tests */
    @Test
    public void shouldThrowConverterExceptionWithMissingMandatoryFieldMessage() throws Exception {
        expectedException.expect(ConverterException.class);
        expectedException.expectMessage(ERROR_WHILE_CONVERTING_CV_TYPE_MISSING_MANDATORY_FIELD);
        Delsvar delsvar = buildCVTypeDelsvar(null, "ANOTHER", null, null, null, null);
        TransportConverterUtil.getCVSvarContent(delsvar);
    }

    @Test
    public void shouldThrowConverterExceptionWithUnexpectedOutComeMessage() throws Exception {
        expectedException.expect(ConverterException.class);
        expectedException.expectMessage(UNEXPECTED_OUTCOME_WHILE_CONVERTING_CV_TYPE);
        Delsvar delsvar = new Delsvar();
        delsvar.getContent().add(new Integer(1));
        TransportConverterUtil.getCVSvarContent(delsvar);
    }

    @Test
    public void shouldThrowConverterExceptionWithUnexpectedOutComeMessage2() throws Exception {
        expectedException.expect(ConverterException.class);
        expectedException.expectMessage(UNEXPECTED_OUTCOME_WHILE_CONVERTING_CV_TYPE);
        Delsvar delsvar = new Delsvar();
        delsvar.getContent().add(null);
        TransportConverterUtil.getCVSvarContent(delsvar);
    }

    @Test
    public void testSpecialistkompetensUsesDisplayName() {
        final String specialistkompetensKlartext = "klartext";
        HosPersonal source = new HosPersonal();
        source.setPersonalId(new HsaId());
        source.setEnhet(new Enhet());
        source.getEnhet().setEnhetsId(new HsaId());
        source.getEnhet().setArbetsplatskod(new ArbetsplatsKod());
        source.getEnhet().setVardgivare(new Vardgivare());
        source.getEnhet().getVardgivare().setVardgivareId(new HsaId());
        Specialistkompetens specialistkompetens = new Specialistkompetens();
        specialistkompetens.setCode("kod");
        specialistkompetens.setDisplayName(specialistkompetensKlartext);
        source.getSpecialistkompetens().add(specialistkompetens);
        Specialistkompetens specialistkompetensWithNullKlartext = new Specialistkompetens();
        specialistkompetensWithNullKlartext.setCode("kod2");
        specialistkompetensWithNullKlartext.setDisplayName(null); // will not be used
        source.getSpecialistkompetens().add(specialistkompetensWithNullKlartext);
        HoSPersonal skapadAv = TransportConverterUtil.getSkapadAv(source);
        assertEquals(1, skapadAv.getSpecialiteter().size());
        assertEquals(specialistkompetensKlartext, skapadAv.getSpecialiteter().get(0));
    }

    @Test
    public void testBefattningUsesCode() {
        final String befattningKod = "kod";
        HosPersonal source = new HosPersonal();
        source.setPersonalId(new HsaId());
        source.setEnhet(new Enhet());
        source.getEnhet().setEnhetsId(new HsaId());
        source.getEnhet().setArbetsplatskod(new ArbetsplatsKod());
        source.getEnhet().setVardgivare(new Vardgivare());
        source.getEnhet().getVardgivare().setVardgivareId(new HsaId());
        Befattning befattning = new Befattning();
        befattning.setCode(befattningKod);
        befattning.setDisplayName("klartext");
        source.getBefattning().add(befattning);
        HoSPersonal skapadAv = TransportConverterUtil.getSkapadAv(source);
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattningKod, skapadAv.getBefattningar().get(0));
    }

    private Delsvar buildStringDelsvar(List<String> content) {
        Delsvar delsvar = new Delsvar();
        delsvar.getContent().addAll(content);
        return delsvar;
    }

    private CVType buildCVType(String code, String codeSystem, String codeSystemName, String codeSystemVersion, String displayName,
            String originalText) {
        CVType cvType = new CVType();
        cvType.setCode(code);
        cvType.setCodeSystem(codeSystem);
        if (codeSystemName != null)
            cvType.setCodeSystemName(codeSystemName);
        if (codeSystemVersion != null)
            cvType.setCodeSystemVersion(codeSystemVersion);
        if (displayName != null)
            cvType.setDisplayName(displayName);
        if (originalText != null)
            cvType.setOriginalText(originalText);
        return cvType;
    }

    private Delsvar buildCVTypeDelsvar(String code, String codeSystem, String codeSystemName, String codeSystemVersion, String displayName,
            String originalText) throws Exception {
        Delsvar delsvar = new Delsvar();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document xmlDoc = factory.newDocumentBuilder().newDocument();

        Node cv = null;
        cv = xmlDoc.createElementNS(NAMESPACE, "ns3:cv");

        if (code != null) {
            Node nodeCode = xmlDoc.createElementNS(NAMESPACE, "ns3:code");
            nodeCode.setTextContent(code);
            cv.appendChild(nodeCode);
        }
        if (codeSystem != null) {
            Node nodeCodeSystem = xmlDoc.createElementNS(NAMESPACE, "ns3:codeSystem");
            nodeCodeSystem.setTextContent(codeSystem);
            cv.appendChild(nodeCodeSystem);
        }

        if (codeSystemName != null) {
            Node nodeCodeSystemName = xmlDoc.createElementNS(NAMESPACE, "ns3:codeSystemName");
            nodeCodeSystemName.setTextContent(codeSystemName);
            cv.appendChild(nodeCodeSystemName);
        }
        if (codeSystemVersion != null) {
            Node nodeCodeSystemVersion = xmlDoc.createElementNS(NAMESPACE, "ns3:codeSystemVersion");
            nodeCodeSystemVersion.setTextContent(codeSystemVersion);
            cv.appendChild(nodeCodeSystemVersion );
        }
        if (displayName != null) {
            Node nodeDisplayName = xmlDoc.createElementNS(NAMESPACE, "ns3:displayName");
            nodeDisplayName.setTextContent(displayName);
            cv.appendChild(nodeDisplayName);
        }
        if (originalText != null) {
            Node nodeOriginalText = xmlDoc.createElementNS(NAMESPACE, "ns3:originalText");
            nodeOriginalText.setTextContent(originalText);
            cv.appendChild(nodeOriginalText);
        }


        delsvar.getContent().add(cv);

        return delsvar;
    }

}
