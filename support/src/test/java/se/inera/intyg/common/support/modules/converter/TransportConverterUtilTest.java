package se.inera.intyg.common.support.modules.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

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
        IntygsStatus status = new IntygsStatus();
        status.setPart(new Part());
        status.getPart().setCode(PartKod.FKASSA.name());
        status.setStatus(new Statuskod());
        status.getStatus().setCode(StatusKod.SENTTO.name());
        status.setTidpunkt(statustidpunkt);
        intyg.getStatus().add(status);

        CertificateMetaData res = TransportConverterUtil.getMetaData(intyg);
        assertNotNull(res);
        assertEquals(intygId, res.getCertificateId());
        assertEquals(intygstyp.toLowerCase(), res.getCertificateType());
        assertEquals(skapadAvFullstandigtNamn, res.getIssuerName());
        assertEquals(enhetsnamn, res.getFacilityName());
        assertEquals(signeringstidpunkt, res.getSignDate());
        assertEquals(1, res.getStatus().size());
        assertEquals(CertificateState.SENT, res.getStatus().get(0).getType());
        assertEquals("FK", res.getStatus().get(0).getTarget());
        assertEquals(statustidpunkt, res.getStatus().get(0).getTimestamp());
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
