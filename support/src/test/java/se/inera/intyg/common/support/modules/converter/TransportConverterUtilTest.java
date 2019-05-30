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
package se.inera.intyg.common.support.modules.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Node;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.ArbetsplatsKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Befattning;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.HsaId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PQType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateTypeFormatEnum;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.IntygsStatus;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Vardgivare;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class TransportConverterUtilTest {

    /* Exception messages */
    private static final String UNEXPECTED_CONVERSION_ERROR = "Unexpected error while converting data type, mandatory data is missing";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @XmlRootElement(namespace = "urn:riv:clinicalprocess:healthcond:certificate:types:3")
    public static class XmlRoot<T> {
        @XmlElement
        private T data;

        public T getData() {
            return data;
        }

        static <T> XmlRoot<T> of(T data) {
            XmlRoot r = new XmlRoot();
            r.data = data;
            return r;
        }
    }

    JAXBContext jaxbContext;

    {
        try {
            jaxbContext = JAXBContext.newInstance(XmlRoot.class, PQType.class, CVType.class, DatePeriodType.class,
                        PartialDateType.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

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
    public void testCVTypeContentSuccess() throws Exception {
        CVType expected = new CVType();
        expected.setCode("code");
        expected.setCodeSystem("codeSystem");
        expected.setCodeSystemName("codeSystemName");
        expected.setDisplayName("displayName");
        expected.setOriginalText("originalText");
        CVType actual = TransportConverterUtil.getCVSvarContent(buildDelsvar(marshal(XmlRoot.of(expected))));
        assertReflectionEquals(expected, actual);
    }

    @Test(expected = ConverterException.class)
    public void testCVTypeContentMissingCodeSystem() throws Exception {
        CVType cvType = new CVType();
        cvType.setCode("code");
        TransportConverterUtil.getCVSvarContent(buildDelsvar(marshal(XmlRoot.of(cvType))));
    }

    @Test
    public void testPQTypeContentSuccess() throws Exception {
        PQType expected = new PQType();
        expected.setUnit("cm");
        expected.setValue(100);
        PQType actual = TransportConverterUtil.getPQSvarContent(buildDelsvar(marshal(XmlRoot.of(expected))));
        assertReflectionEquals(expected, actual);
    }

    @Test
    public void testDatePeriodSuccess() throws Exception {
        DatePeriodType expected = new DatePeriodType();
        expected.setStart(LocalDate.now());
        expected.setEnd(LocalDate.now().plusDays(2));
        DatePeriodType actual = TransportConverterUtil.getDatePeriodTypeContent(buildDelsvar(marshal(XmlRoot.of(expected))));
        assertReflectionEquals(expected, actual);
    }

    @Test
    public void testPartialDateYearSuccess() throws Exception {
        PartialDateType expected = new PartialDateType();
        expected.setValue(Year.parse("2019"));
        expected.setFormat(PartialDateTypeFormatEnum.YYYY);
        PartialDateType actual = TransportConverterUtil.getPartialDateContent(buildDelsvar(marshal(XmlRoot.of(expected))));
        assertReflectionEquals(expected, actual);
    }

    @Test
    public void testPartialDateYearMonthSuccess() throws Exception {
        PartialDateType expected = new PartialDateType();
        expected.setValue(YearMonth.parse("2019-01"));
        expected.setFormat(PartialDateTypeFormatEnum.YYYY_MM);
        PartialDateType actual = TransportConverterUtil.getPartialDateContent(buildDelsvar(marshal(XmlRoot.of(expected))));
        assertReflectionEquals(expected, actual);
    }

    @Test
    public void testPartialDateFullSuccess() throws Exception {
        PartialDateType expected = new PartialDateType();
        expected.setValue(LocalDate.parse("2019-02-02", DateTimeFormatter.ISO_LOCAL_DATE));
        expected.setFormat(PartialDateTypeFormatEnum.YYYY_MM_DD);
        PartialDateType actual = TransportConverterUtil.getPartialDateContent(buildDelsvar(marshal(XmlRoot.of(expected))));
        assertReflectionEquals(expected, actual);
    }

    @Test(expected = ConverterException.class)
    public void testPartialDateMissingFormat() throws Exception {
        PartialDateType type = new PartialDateType();
        type.setValue(YearMonth.parse("2019-01"));
        TransportConverterUtil.getPartialDateContent(buildDelsvar(marshal(XmlRoot.of(type))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialDateInvalidData() throws Exception {
        PartialDateType type = new PartialDateType();
        type.setValue(Year.parse("2019"));
        type.setFormat(PartialDateTypeFormatEnum.YYYY);
        Node pd = marshal(XmlRoot.of(type));
        for (Node n = pd.getFirstChild(); n != null; n = n.getNextSibling()) {
            n.setTextContent("XXX");
        }
        TransportConverterUtil.getPartialDateContent(buildDelsvar(pd));
    }


    @Test
    public void testGetStringContentWithWhitespaceSuccess() throws ParserConfigurationException {
        String expected = "String with freetext";
        Delsvar delsvar = buildDelsvar(Arrays.asList("\n", "    ", expected, "\n", "    "));
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
        expectedException.expectMessage(UNEXPECTED_CONVERSION_ERROR);
        Delsvar delsvar = buildCVTypeDelsvar(null, "ANOTHER", null, null, null, null);
        TransportConverterUtil.getCVSvarContent(delsvar);
    }

    @Test
    public void shouldThrowConverterExceptionWithUnexpectedOutComeMessage() throws Exception {
        expectedException.expect(ConverterException.class);
        expectedException.expectMessage(UNEXPECTED_CONVERSION_ERROR);
        Delsvar delsvar = new Delsvar();
        delsvar.getContent().add(new Integer(1));
        TransportConverterUtil.getCVSvarContent(delsvar);
    }

    @Test
    public void shouldThrowConverterExceptionWithUnexpectedOutComeMessage2() throws Exception {
        expectedException.expect(ConverterException.class);
        expectedException.expectMessage(UNEXPECTED_CONVERSION_ERROR);
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

    // returns data node
    Node marshal(XmlRoot xmlRoot) throws JAXBException {
        DOMResult res = new DOMResult();
        jaxbContext.createMarshaller().marshal(xmlRoot, res);
        return res.getNode().getFirstChild().getFirstChild();
    }

    private Delsvar buildDelsvar(List<Object> content) {
        Delsvar delsvar = new Delsvar();
        delsvar.getContent().addAll(content);
        return delsvar;
    }

    private Delsvar buildDelsvar(Object content) {
        return buildDelsvar(Arrays.asList(content));
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
        CVType cvType = buildCVType(code, codeSystem, codeSystemName, codeSystemVersion, displayName, originalText);
        return buildDelsvar(marshal(XmlRoot.of(cvType)));
    }

}
