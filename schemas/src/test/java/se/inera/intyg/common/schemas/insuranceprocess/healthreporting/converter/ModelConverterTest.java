/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter;

import org.junit.Test;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.LakarutlatandeEnkelType;
import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.VardAdresseringsType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeType;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.inera.intyg.schemas.contract.Personnummer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelConverterTest {

    @Test
    public void testToCertificateMetaType() {
        String certificateId = "certificateId";
        String certificateType = "certificateType";
        String validFromDate = "2016-10-11";
        String validToDate = "2016-10-14";
        String signingDoctorName = "signingDoctorName";
        String careUnitName = "careUnitName";
        LocalDateTime signedDate = LocalDateTime.now();
        CertificateHolder source = buildCertificateHolder(certificateId, certificateType, validFromDate, validToDate, signingDoctorName,
            careUnitName,
            signedDate, false);

        CertificateMetaType res = ModelConverter.toCertificateMetaType(source);

        assertNotNull(res);
        assertEquals(certificateId, res.getCertificateId());
        assertEquals(certificateType, res.getCertificateType());
        assertEquals(validFromDate, res.getValidFrom().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(validToDate, res.getValidTo().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(signingDoctorName, res.getIssuerName());
        assertEquals(careUnitName, res.getFacilityName());
        assertEquals(signedDate.format(DateTimeFormatter.ISO_LOCAL_DATE), res.getSignDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals("true", res.getAvailable());
        assertEquals(1, res.getStatus().size());
        assertEquals(StatusType.RECEIVED, res.getStatus().get(0).getType());
    }

    @Test
    public void testToCertificateMetaTypeDateMissing() {
        CertificateHolder source = buildCertificateHolder("certificateId", "certificateType", "2016-10-11", null, "signingDoctorName",
            "careUnitName",
            LocalDateTime.now(), false);

        CertificateMetaType res = ModelConverter.toCertificateMetaType(source);

        assertNotNull(res);
        assertNull(res.getValidTo());
    }

    @Test
    public void testToCertificateMetaTypeDeleted() {
        CertificateHolder source = buildCertificateHolder("certificateId", "certificateType", "2016-10-11", "2016-10-14",
            "signingDoctorName", "careUnitName",
            LocalDateTime.now(), true);

        CertificateMetaType res = ModelConverter.toCertificateMetaType(source);

        assertNotNull(res);
        assertEquals("false", res.getAvailable());
    }

    @Test
    public void testToCertificateMetaTypeSignedDateMissing() {
        CertificateHolder source = buildCertificateHolder("certificateId", "certificateType", "2016-10-11", "2016-10-14",
            "signingDoctorName", "careUnitName",
            null, false);

        CertificateMetaType res = ModelConverter.toCertificateMetaType(source);

        assertNotNull(res);
        assertNull(res.getSignDate());
    }

    @Test
    public void testToVardAdresseringsType() {
        final String enhetsId = "enhetsId";
        final String enhetsnamn = "enhetsnamn";
        final String arbetsplatskod = "arbetsplatskod";
        final String vardgivarid = "vardgivarid";
        final String vardgivarnamn = "vardgivarnamn";
        final String fullstandigtNamn = "fullstandigt namn";
        final String personalId = "personalId";
        GrundData source = buildGrundData(enhetsId, enhetsnamn, arbetsplatskod, vardgivarid, vardgivarnamn, fullstandigtNamn, personalId);

        VardAdresseringsType res = ModelConverter.toVardAdresseringsType(source);

        assertNotNull(res);
        assertEquals("1.2.752.129.2.1.4.1", res.getHosPersonal().getEnhet().getEnhetsId().getRoot());
        assertEquals(enhetsId, res.getHosPersonal().getEnhet().getEnhetsId().getExtension());
        assertEquals(enhetsnamn, res.getHosPersonal().getEnhet().getEnhetsnamn());
        assertEquals("1.2.752.29.4.71", res.getHosPersonal().getEnhet().getArbetsplatskod().getRoot());
        assertEquals(arbetsplatskod, res.getHosPersonal().getEnhet().getArbetsplatskod().getExtension());
        assertEquals("1.2.752.129.2.1.4.1", res.getHosPersonal().getEnhet().getVardgivare().getVardgivareId().getRoot());
        assertEquals(vardgivarid, res.getHosPersonal().getEnhet().getVardgivare().getVardgivareId().getExtension());
        assertEquals(vardgivarnamn, res.getHosPersonal().getEnhet().getVardgivare().getVardgivarnamn());
        assertEquals(fullstandigtNamn, res.getHosPersonal().getFullstandigtNamn());
        assertEquals("1.2.752.129.2.1.4.1", res.getHosPersonal().getPersonalId().getRoot());
        assertEquals(personalId, res.getHosPersonal().getPersonalId().getExtension());
    }

    @Test
    public void testToVardAdresseringsTypeNoArbetsplatskod() {
        GrundData source = buildGrundData("enhetsId", "enhetsnamn", null, "vardgivarid", "vardgivarnamn", "fullstandigt namn",
            "personalId");

        VardAdresseringsType res = ModelConverter.toVardAdresseringsType(source);

        assertNotNull(res);
        assertNull(res.getHosPersonal().getEnhet().getArbetsplatskod());
    }

    @Test
    public void testBuildRevokeTypeFromUtlatande() {
        final String certificateId = "certificateId";
        final String personId = "191212121212";
        final String personalId = "personalId";
        final String vardgivarid = "vardgivarid";
        final String revokeMessage = "revoke message";

        Utlatande source = buildUtlatande(certificateId, LocalDateTime.now(), personId, "fullstandigt namn");
        source.getGrundData().setSkapadAv(buildGrundData("enhetsId", "enhetsnamn", null, vardgivarid, "vardgivarnamn", "fullstandigt namn",
            personalId).getSkapadAv());

        RevokeType res = ModelConverter.buildRevokeTypeFromUtlatande(source, revokeMessage);

        assertNotNull(res);
        assertEquals(certificateId, res.getLakarutlatande().getLakarutlatandeId());
        assertEquals(personId, res.getLakarutlatande().getPatient().getPersonId().getExtension());
        assertEquals(personalId, res.getAdressVard().getHosPersonal().getPersonalId().getExtension());
        assertEquals(vardgivarid, res.getAdressVard().getHosPersonal().getEnhet().getVardgivare().getVardgivareId().getExtension());
        assertEquals(revokeMessage, res.getMeddelande());
        assertTrue(res.getVardReferensId().startsWith("REVOKE-" + certificateId + "-"));
        assertNotNull(res.getAvsantTidpunkt());
    }

    @Test
    public void testBuildRevokeTypeFromUtlatandeRevokeMessageNull() {
        final String revokeMessage = null;
        Utlatande source = buildUtlatande("certificateId", LocalDateTime.now(), "191212121212", "fullstandigt namn");
        source.getGrundData()
            .setSkapadAv(buildGrundData("enhetsId", "enhetsnamn", null, "vardgivarid", "vardgivarnamn", "fullstandigt namn",
                "personalId").getSkapadAv());

        RevokeType res = ModelConverter.buildRevokeTypeFromUtlatande(source, revokeMessage);

        assertNotNull(res);
        assertNull(res.getMeddelande());
    }

    @Test
    public void testVardreferensId() {
        String intygId = "INTYGID";
        LocalDateTime time = LocalDateTime.of(2011, 01, 02, 23, 59, 01, 1);
        String res = ModelConverter.buildVardReferensId(intygId, time);
        assertEquals("REVOKE-" + intygId + "-20110102T235901.000", res);
    }

    @Test
    public void testToLakarutlatandeEnkelType() {
        final String certificateId = "certificateId";
        final LocalDateTime signedDate = LocalDateTime.now();
        final String personId = "191212121212";
        // This never happen in a real situation after Webcert 5.3. However, we want to check that we do not copy the information if it
        // did exist
        final String fullstandigtNamn = "fullstandigt namn";
        Utlatande source = buildUtlatande(certificateId, signedDate, personId, fullstandigtNamn);

        LakarutlatandeEnkelType res = ModelConverter.toLakarutlatandeEnkelType(source);

        assertNotNull(res);
        assertEquals(certificateId, res.getLakarutlatandeId());
        assertEquals(signedDate, res.getSigneringsTidpunkt());
        assertEquals("1.2.752.129.2.1.3.1", res.getPatient().getPersonId().getRoot());
        assertEquals(personId, res.getPatient().getPersonId().getExtension());
        assertNull(res.getPatient().getFullstandigtNamn());
    }

    @Test
    public void testToLakarutlatandeEnkelTypeSamordningsnummer() {
        final String personId = "999999-9999";
        Utlatande source = buildUtlatande("certificateId", LocalDateTime.now(), personId, "fullstandigt namn");

        LakarutlatandeEnkelType res = ModelConverter.toLakarutlatandeEnkelType(source);

        assertNotNull(res);
        assertEquals("1.2.752.129.2.1.3.3", res.getPatient().getPersonId().getRoot());
        assertEquals(personId, res.getPatient().getPersonId().getExtension());
    }

    private CertificateHolder buildCertificateHolder(String certificateId, String certificateType, String validFromDate, String validToDate,
        String signingDoctorName, String careUnitName, LocalDateTime signedDate, boolean isDeleted) {
        CertificateHolder source = new CertificateHolder();
        source.setId(certificateId);
        source.setType(certificateType);
        source.setValidFromDate(validFromDate);
        source.setValidToDate(validToDate);
        source.setSigningDoctorName(signingDoctorName);
        source.setCareUnitName(careUnitName);
        source.setSignedDate(signedDate);
        source.setCertificateStates(new ArrayList<>());
        source.getCertificateStates()
            .add(new CertificateStateHolder("target", CertificateState.RECEIVED, LocalDateTime.now().minusDays(2)));
        source.setDeleted(isDeleted);
        return source;
    }

    private GrundData buildGrundData(final String enhetsId, final String enhetsnamn, final String arbetsplatskod, final String vardgivarid,
        final String vardgivarnamn, final String fullstandigtNamn, final String personalId) {
        GrundData source = new GrundData();
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivarid);
        vardgivare.setVardgivarnamn(vardgivarnamn);
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        vardenhet.setEnhetsnamn(enhetsnamn);
        vardenhet.setArbetsplatsKod(arbetsplatskod);
        vardenhet.setVardgivare(vardgivare);
        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setFullstandigtNamn(fullstandigtNamn);
        skapadAv.setPersonId(personalId);
        source.setSkapadAv(skapadAv);
        return source;
    }

    private Utlatande buildUtlatande(final String certificateId, final LocalDateTime signedDate, final String personId,
        final String fullstandigtNamn) {

        Personnummer personnummer = Personnummer.createPersonnummer(personId).get();

        Patient patient = new Patient();
        patient.setPersonId(personnummer);
        patient.setFullstandigtNamn(fullstandigtNamn);

        GrundData grunddata = new GrundData();
        grunddata.setSigneringsdatum(signedDate);
        grunddata.setPatient(patient);

        Utlatande source = mock(Utlatande.class);
        when(source.getId()).thenReturn(certificateId);
        when(source.getGrundData()).thenReturn(grunddata);

        return source;
    }
}
