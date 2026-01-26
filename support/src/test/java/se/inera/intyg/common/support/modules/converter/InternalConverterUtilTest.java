/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.Constants.ADDRESS_DETAILS_SOURCE_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.ADDRESS_DETAILS_SOURCE_USER_CODE;
import static se.inera.intyg.common.support.Constants.ADDRESS_DETAILS_SOURCE_USER_NAME;

import jakarta.xml.bind.JAXBElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.PaTitle;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PersonId;
import se.riv.clinicalprocess.healthcond.certificate.v3.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.MeddelandeReferens;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BefattningService.class, UnitMappingConfigLoader.class, UnitMapperUtil.class,
    InternalConverterUtil.class})
class InternalConverterUtilTest {

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext.getBean(InternalConverterUtil.class).initialize();
    }

    @Test
    void testConvert() {
        final String intygsId = "intygsid";
        final String enhetsId = "enhetsid";
        final String enhetsnamn = "enhetsnamn";
        final String patientPersonId = "191212121212";
        final String skapadAvFullstandigtNamn = "fullständigt namn";
        final String skapadAvPersonId = "skapad av pid";
        final LocalDateTime signeringsdatum = LocalDateTime.now();
        final String arbetsplatsKod = "arbetsplatsKod";
        final String postadress = "postadress";
        final String postNummer = "postNummer";
        final String postOrt = "postOrt";
        final String epost = "epost";
        final String telefonNummer = "telefonNummer";
        final String vardgivarid = "vardgivarid";
        final String vardgivarNamn = "vardgivarNamn";
        final String forskrivarKod = "forskrivarKod";
        final String fornamn = "fornamn";
        final String efternamn = "efternamn";
        final String mellannamn = "mellannamn";
        final String patientPostadress = "patientPostadress";
        final String patientPostnummer = "patientPostnummer";
        final String patientPostort = "patientPostort";

        Utlatande utlatande = buildUtlatande(intygsId, enhetsId, enhetsnamn, patientPersonId,
            skapadAvFullstandigtNamn, skapadAvPersonId, signeringsdatum, arbetsplatsKod,
            postadress, postNummer, postOrt, epost, telefonNummer, vardgivarid, vardgivarNamn,
            forskrivarKod, fornamn, efternamn, mellannamn, patientPostadress, patientPostnummer,
            patientPostort, null, null);

        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.EXTENDED_WITH_ADDRESS_DETAILS_SOURCE);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertNotNull(intyg.getVersion());
        assertEquals(signeringsdatum, intyg.getSigneringstidpunkt());
        assertEquals("1.2.752.129.2.1.3.1", intyg.getPatient().getPersonId().getRoot());
        assertEquals(patientPersonId, intyg.getPatient().getPersonId().getExtension());
        assertEquals(skapadAvFullstandigtNamn, intyg.getSkapadAv().getFullstandigtNamn());
        assertNotNull(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getRoot());
        assertEquals(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getRoot());
        assertEquals(enhetsId, intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertEquals(enhetsnamn, intyg.getSkapadAv().getEnhet().getEnhetsnamn());
        assertNotNull(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getRoot());
        assertEquals(arbetsplatsKod, intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension());
        assertEquals(postadress, intyg.getSkapadAv().getEnhet().getPostadress());
        assertEquals(postNummer, intyg.getSkapadAv().getEnhet().getPostnummer());
        assertEquals(postOrt, intyg.getSkapadAv().getEnhet().getPostort());
        assertEquals(epost, intyg.getSkapadAv().getEnhet().getEpost());
        assertEquals(telefonNummer, intyg.getSkapadAv().getEnhet().getTelefonnummer());
        assertNotNull(intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getRoot());
        assertEquals(vardgivarid, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getExtension());
        assertEquals(vardgivarNamn, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivarnamn());
        assertEquals(forskrivarKod, intyg.getSkapadAv().getForskrivarkod());
        assertEquals(fornamn, intyg.getPatient().getFornamn());
        assertEquals(efternamn, intyg.getPatient().getEfternamn());
        assertEquals(mellannamn, intyg.getPatient().getMellannamn());
        assertEquals(patientPostadress, intyg.getPatient().getPostadress());
        assertEquals(patientPostnummer, intyg.getPatient().getPostnummer());
        assertEquals(patientPostort, intyg.getPatient().getPostort());
        assertEquals(ADDRESS_DETAILS_SOURCE_CODE_SYSTEM, intyg.getPatient().getKallaAdressuppgifter().getCodeSystem());
        assertEquals(ADDRESS_DETAILS_SOURCE_USER_CODE, intyg.getPatient().getKallaAdressuppgifter().getCode());
        assertEquals(ADDRESS_DETAILS_SOURCE_USER_NAME, intyg.getPatient().getKallaAdressuppgifter().getDisplayName());
        assertTrue(intyg.getRelation().isEmpty());
    }

    @Test
    void testConvertNoPatientInfo() {
        final String intygsId = "intygsid";
        final String enhetsId = "enhetsid";
        final String enhetsnamn = "enhetsnamn";
        final String patientPersonId = "191212121212";
        final String skapadAvFullstandigtNamn = "fullständigt namn";
        final String skapadAvPersonId = "skapad av pid";
        final LocalDateTime signeringsdatum = LocalDateTime.now();
        final String arbetsplatsKod = "arbetsplatsKod";
        final String postadress = "postadress";
        final String postNummer = "postNummer";
        final String postOrt = "postOrt";
        final String epost = "epost";
        final String telefonNummer = "telefonNummer";
        final String vardgivarid = "vardgivarid";
        final String vardgivarNamn = "vardgivarNamn";
        final String forskrivarKod = "forskrivarKod";
        final String fornamn = "fornamn";
        final String efternamn = "efternamn";
        final String mellannamn = "mellannamn";
        final String patientPostadress = "patientPostadress";
        final String patientPostnummer = "patientPostnummer";
        final String patientPostort = "patientPostort";

        Utlatande utlatande = buildUtlatande(intygsId, enhetsId, enhetsnamn, patientPersonId,
            skapadAvFullstandigtNamn, skapadAvPersonId, signeringsdatum, arbetsplatsKod, postadress, postNummer, postOrt, epost,
            telefonNummer,
            vardgivarid, vardgivarNamn, forskrivarKod, fornamn, efternamn, mellannamn, patientPostadress, patientPostnummer, patientPostort,
            null, null);

        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC);

        assertEquals(enhetsId, intyg.getIntygsId().getRoot());
        assertEquals(intygsId, intyg.getIntygsId().getExtension());
        assertNotNull(intyg.getVersion());
        assertEquals(signeringsdatum, intyg.getSigneringstidpunkt());
        assertEquals("1.2.752.129.2.1.3.1", intyg.getPatient().getPersonId().getRoot());
        assertEquals(patientPersonId, intyg.getPatient().getPersonId().getExtension());
        assertEquals(skapadAvFullstandigtNamn, intyg.getSkapadAv().getFullstandigtNamn());
        assertNotNull(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getRoot());
        assertEquals(skapadAvPersonId, intyg.getSkapadAv().getPersonalId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getRoot());
        assertEquals(enhetsId, intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertNotNull(intyg.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertEquals(enhetsnamn, intyg.getSkapadAv().getEnhet().getEnhetsnamn());
        assertNotNull(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getRoot());
        assertEquals(arbetsplatsKod, intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension());
        assertEquals(postadress, intyg.getSkapadAv().getEnhet().getPostadress());
        assertEquals(postNummer, intyg.getSkapadAv().getEnhet().getPostnummer());
        assertEquals(postOrt, intyg.getSkapadAv().getEnhet().getPostort());
        assertEquals(epost, intyg.getSkapadAv().getEnhet().getEpost());
        assertEquals(telefonNummer, intyg.getSkapadAv().getEnhet().getTelefonnummer());
        assertNotNull(intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getRoot());
        assertEquals(vardgivarid, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getExtension());
        assertEquals(vardgivarNamn, intyg.getSkapadAv().getEnhet().getVardgivare().getVardgivarnamn());
        assertEquals(forskrivarKod, intyg.getSkapadAv().getForskrivarkod());
        assertEquals("", intyg.getPatient().getFornamn());
        assertEquals("", intyg.getPatient().getEfternamn());
        assertNull(intyg.getPatient().getMellannamn());
        assertEquals("", intyg.getPatient().getPostadress());
        assertEquals("", intyg.getPatient().getPostnummer());
        assertEquals("", intyg.getPatient().getPostort());
        assertTrue(intyg.getRelation().isEmpty());
    }

    @Test
    void testConvertWithRelation() {
        RelationKod relationKod = RelationKod.FRLANG;
        String relationIntygsId = "relationIntygsId";
        Utlatande utlatande = buildUtlatande(relationKod, relationIntygsId);

        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC);
        assertNotNull(intyg.getRelation());
        assertEquals(1, intyg.getRelation().size());
        assertEquals(relationKod.value(), intyg.getRelation().get(0).getTyp().getCode());
        assertNotNull(intyg.getRelation().get(0).getTyp().getCodeSystem());
        assertEquals(relationIntygsId, intyg.getRelation().get(0).getIntygsId().getExtension());
        assertNotNull(intyg.getRelation().get(0).getIntygsId().getRoot());
    }

    @Test
    void testConvertUnitAdddressInformationMissing() {
        Utlatande utlatande = buildUtlatande("intygsid", "enhetsid", "enhetsnamn", "191212121212",
            "fullständigt namn", "skapad av pid", LocalDateTime.now(), "arbetsplatsKod", null, null, null, "epost", null,
            "vardgivarid", "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn", "patientPostadress", "patientPostnummer",
            "patientPostort",
            null, null);

        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC);

        // empty string if missing in input
        assertEquals("", intyg.getSkapadAv().getEnhet().getPostadress());
        assertEquals("", intyg.getSkapadAv().getEnhet().getPostnummer());
        assertEquals("", intyg.getSkapadAv().getEnhet().getPostort());
        assertEquals("", intyg.getSkapadAv().getEnhet().getTelefonnummer());
    }

    @Test
    void getMeddelandeReferensOfTypeTest() {
        final RelationKod type = RelationKod.KOMPLT;
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        Utlatande utlatande = buildUtlatande(type, "relationIntygsId");
        utlatande.getGrundData().getRelation().setMeddelandeId(meddelandeId);
        utlatande.getGrundData().getRelation().setReferensId(referensId);
        MeddelandeReferens result = InternalConverterUtil.getMeddelandeReferensOfType(utlatande, type);
        assertNotNull(result);
        assertEquals(meddelandeId, result.getMeddelandeId());
        assertEquals(referensId, result.getReferensId());
    }

    @Test
    void getMeddelandeReferensOfTypeReferensIdNullTest() {
        final RelationKod type = RelationKod.KOMPLT;
        final String meddelandeId = "meddelandeId";
        Utlatande utlatande = buildUtlatande(type, "relationIntygsId");
        utlatande.getGrundData().getRelation().setMeddelandeId(meddelandeId);
        MeddelandeReferens result = InternalConverterUtil.getMeddelandeReferensOfType(utlatande, type);
        assertNotNull(result);
        assertEquals(meddelandeId, result.getMeddelandeId());
        assertNull(result.getReferensId());
    }

    @Test
    void getMeddelandeReferensOfTypeNoRelationTest() {
        final RelationKod type = RelationKod.KOMPLT;
        Utlatande utlatande = buildUtlatande(null, null);
        MeddelandeReferens result = InternalConverterUtil.getMeddelandeReferensOfType(utlatande, type);
        assertNull(result);
    }

    @Test
    void getMeddelandeReferensOfTypeWrongTypeTest() {
        final String meddelandeId = "meddelandeId";
        final String referensId = "referensId";
        Utlatande utlatande = buildUtlatande(RelationKod.FRLANG, "relationIntygsId");
        utlatande.getGrundData().getRelation().setMeddelandeId(meddelandeId);
        utlatande.getGrundData().getRelation().setReferensId(referensId);
        MeddelandeReferens result = InternalConverterUtil.getMeddelandeReferensOfType(utlatande, RelationKod.KOMPLT);
        assertNull(result);
    }

    @Test
    void addIfNotBlankTest() {
        List<Svar> svars = new ArrayList<>();
        String svarsId = "1";
        String delsvarsId = "1.2";
        String content = "content";
        InternalConverterUtil.addIfNotBlank(svars, svarsId, delsvarsId, content);

        assertEquals(1, svars.size());
        assertEquals(svarsId, svars.get(0).getId());
        assertEquals(1, svars.get(0).getDelsvar().size());
        assertEquals(delsvarsId, svars.get(0).getDelsvar().get(0).getId());
        assertEquals(content, TransportConverterUtil.getStringContent(svars.get(0).getDelsvar().get(0)));
    }

    @Test
    void addIfNotBlankContentNullTest() {
        List<Svar> svars = new ArrayList<>();
        String content = null;
        InternalConverterUtil.addIfNotBlank(svars, "svarsId", "delsvarsId", content);

        assertTrue(svars.isEmpty());
    }

    @Test
    void addIfNotBlankContentEmptyStringTest() {
        List<Svar> svars = new ArrayList<>();
        String content = "";
        InternalConverterUtil.addIfNotBlank(svars, "svarsId", "delsvarsId", content);

        assertTrue(svars.isEmpty());
    }

    @Test
    void aDatePeriodTest() {
        LocalDate from = LocalDate.now();
        LocalDate tom = LocalDate.now().plusDays(4);
        JAXBElement<DatePeriodType> result = InternalConverterUtil.aDatePeriod(from, tom);

        assertNotNull(result);
        assertEquals(from, result.getValue().getStart());
        assertEquals(tom, result.getValue().getEnd());
    }

    @Test
    void aCVTest() {
        String codeSystem = "codesystem";
        String code = "code";
        String displayName = "displayname";
        JAXBElement<CVType> result = InternalConverterUtil.aCV(codeSystem, code, displayName);

        assertNotNull(result);
        assertEquals(code, result.getValue().getCode());
        assertEquals(codeSystem, result.getValue().getCodeSystem());
        assertEquals(displayName, result.getValue().getDisplayName());
    }

    @Test
    void testSpecialistkompetensAppendsDisplayName() {
        final String specialistkompetens = "Hörselrubbningar";
        Utlatande utlatande = buildUtlatande(null, null);
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens);
        HosPersonal skapadAv = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC).getSkapadAv();
        assertEquals(1, skapadAv.getSpecialistkompetens().size());
        assertEquals("N/A", skapadAv.getSpecialistkompetens().get(0).getCode());
        assertEquals(specialistkompetens, skapadAv.getSpecialistkompetens().get(0).getDisplayName());
    }

    @Test
    void testBefattningAppendsDisplayName() {
        final String befattningskod = "203010";
        final String description = "Läkare legitimerad, specialiseringstjänstgöring";
        Utlatande utlatande = buildUtlatande(null, null);
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattningskod);
        HosPersonal skapadAv = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC).getSkapadAv();
        assertEquals(1, skapadAv.getBefattning().size());
        assertEquals(befattningskod, skapadAv.getBefattning().get(0).getCode());
        assertEquals(description, skapadAv.getBefattning().get(0).getDisplayName());
    }

    @Test
    void testBefattningDoNotAppendDisplayNameIfNoSpecialistkompetensKodMatch() {
        String befattning = "kod";
        Utlatande utlatande = buildUtlatande(null, null);
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattning);
        HosPersonal skapadAv = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC).getSkapadAv();
        assertEquals(1, skapadAv.getBefattning().size());
        assertEquals(befattning, skapadAv.getBefattning().get(0).getCode());
        assertNull(skapadAv.getBefattning().get(0).getDisplayName());
    }

    @Test
    void testPersonnummerRoot() {
        final Personnummer pnr = Personnummer.createPersonnummer("19121212-1212").get();
        PersonId res = InternalConverterUtil.getPersonId(pnr);
        assertEquals(pnr.getPersonnummer(), res.getExtension());
        assertEquals("1.2.752.129.2.1.3.1", res.getRoot());
    }

    @Test
    void testSamordningsRoot() {
        final Personnummer pnr = Personnummer.createPersonnummer("19800191-0002").get();
        PersonId res = InternalConverterUtil.getPersonId(pnr);
        assertEquals(pnr.getPersonnummer(), res.getExtension());
        assertEquals("1.2.752.129.2.1.3.3", res.getRoot());
    }

    @Test
    void testNullFillWithZeros() {
        String testString = InternalConverterUtil.getInternalDateContentFillWithZeros(null);
        assertEquals("0000-00-00", testString);
    }

    @ParameterizedTest
    @MethodSource("fillWithZerosArguments")
    void testFillWithZeros(InternalDate input, String expected) {
        String result = InternalConverterUtil.getInternalDateContentFillWithZeros(input);
        assertEquals(expected, result);
    }

    @Nested
    class GetBefattningsList {

        @Test
        void shouldReturnEmptyListWhenBothListsAreEmpty() {
            final var hoSPersonal = buildHoSPerson();
            final var result = InternalConverterUtil.getSkapadAv(hoSPersonal, LocalDateTime.now().minusDays(1));
            assertTrue(result.getBefattning().isEmpty());
        }

        @Test
        void shouldUseBefattningsKoderWhenPresent() {
            final var hoSPersonal = buildHoSPerson();
            hoSPersonal.getBefattningsKoder().add(buildPaTitle("203010", "Läkare legitimerad, specialiseringstjänstgöring"));
            hoSPersonal.getBefattningsKoder().add(buildPaTitle("222100", "Sjuksköterska legitimerad"));
            final var result = InternalConverterUtil.getSkapadAv(hoSPersonal, LocalDateTime.now().minusDays(1));
            assertEquals(2, result.getBefattning().size());
        }

        @Test
        void shouldUseBefattningsKoderOverBefattningarWhenBothPresent() {
            final var hoSPersonal = buildHoSPerson();
            hoSPersonal.getBefattningsKoder().add(buildPaTitle("203010", "Läkare legitimerad, specialiseringstjänstgöring"));
            hoSPersonal.getBefattningar().add("222100");
            final var result = InternalConverterUtil.getSkapadAv(hoSPersonal, LocalDateTime.now().minusDays(1));
            assertEquals("203010", result.getBefattning().getFirst().getCode());
        }

        @Test
        void shouldUseBefattningarWhenBefattningsKoderIsEmpty() {
            final var hoSPersonal = buildHoSPerson();
            hoSPersonal.getBefattningar().add("222100");
            final var result = InternalConverterUtil.getSkapadAv(hoSPersonal, LocalDateTime.now().minusDays(1));
            assertEquals("222100", result.getBefattning().getFirst().getCode());
        }

        @Test
        void shouldHandleDuplicateBefattningsKoder() {
            final var hoSPersonal = buildHoSPerson();
            final var paTitle = buildPaTitle("203010", "Läkare legitimerad, specialiseringstjänstgöring");
            hoSPersonal.getBefattningsKoder().addAll(List.of(paTitle, paTitle));
            final var result = InternalConverterUtil.getSkapadAv(hoSPersonal, LocalDateTime.now().minusDays(1));
            assertEquals(1, result.getBefattning().size());
        }

        @Test
        void shouldHandleDuplicateBefattningar() {
            final var hoSPersonal = buildHoSPerson();
            hoSPersonal.getBefattningar().addAll(List.of("203010", "203010"));
            final var result = InternalConverterUtil.getSkapadAv(hoSPersonal, LocalDateTime.now().minusDays(1));
            assertEquals(1, result.getBefattning().size());
        }

        private HoSPersonal buildHoSPerson() {
            final var hoSPersonal = new HoSPersonal();
            final var vardenhet = new Vardenhet();
            final var vardgivare = new Vardgivare();
            vardgivare.setVardgivarid("vardgivarid");
            vardenhet.setEnhetsid("enhetsid");
            vardenhet.setVardgivare(vardgivare);
            hoSPersonal.setVardenhet(vardenhet);
            hoSPersonal.setForskrivarKod("kod");
            return hoSPersonal;
        }

        private PaTitle buildPaTitle(String code, String klartext) {
            final var paTitle = new PaTitle();
            paTitle.setKod(code);
            paTitle.setKlartext(klartext);
            return paTitle;
        }
    }

    static Stream<Arguments> fillWithZerosArguments() {
        return Stream.of(
            Arguments.of(null, "0000-00-00"),
            Arguments.of(new InternalDate(""), "0000-00-00"),
            Arguments.of(new InternalDate("2017"), "2017-00-00"),
            Arguments.of(new InternalDate("2017-01"), "2017-01-00"),
            Arguments.of(new InternalDate("2017-01-01"), "2017-01-01")
        );
    }


    private Utlatande buildUtlatande(RelationKod relationKod, String relationIntygsId) {
        return buildUtlatande("intygsId", "enhetsId", "enhetsnamn", "19121212-1212",
            "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now(), "arbetsplatsKod",
            "postadress", "postNummer", "postOrt", "epost", "telefonNummer", "vardgivarid",
            "vardgivarNamn", "forskrivarKod", "fornamn", "efternamn", "mellannamn",
            "patientPostadress", "patientPostnummer", "patientPostort", relationKod, relationIntygsId);
    }

    private Utlatande buildUtlatande(String intygsId, String enhetsId, String enhetsnamn,
        String patientPersonId, String skapadAvFullstandigtNamn, String skapadAvPersonId, LocalDateTime signeringsdatum,
        String arbetsplatsKod,
        String postadress, String postNummer, String postOrt, String epost, String telefonNummer, String vardgivarid, String vardgivarNamn,
        String forskrivarKod, String fornamn, String efternamn, String mellannamn, String patientPostadress, String patientPostnummer,
        String patientPostort, RelationKod relationKod, String relationIntygsId) {
        Utlatande utlatande = mock(Utlatande.class);
        when(utlatande.getId()).thenReturn(intygsId);
        GrundData grundData = new GrundData();
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        vardenhet.setEnhetsnamn(enhetsnamn);
        vardenhet.setArbetsplatsKod(arbetsplatsKod);
        vardenhet.setPostadress(postadress);
        vardenhet.setPostnummer(postNummer);
        vardenhet.setPostort(postOrt);
        vardenhet.setEpost(epost);
        vardenhet.setTelefonnummer(telefonNummer);
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivarid);
        vardgivare.setVardgivarnamn(vardgivarNamn);
        vardenhet.setVardgivare(vardgivare);
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setFullstandigtNamn(skapadAvFullstandigtNamn);
        skapadAv.setPersonId(skapadAvPersonId);
        skapadAv.setForskrivarKod(forskrivarKod);
        grundData.setSkapadAv(skapadAv);
        Patient patient = new Patient();
        Personnummer personId = Personnummer.createPersonnummer(patientPersonId).get();
        patient.setPersonId(personId);
        patient.setFornamn(fornamn);
        patient.setEfternamn(efternamn);
        patient.setMellannamn(mellannamn);
        patient.setPostadress(patientPostadress);
        patient.setPostnummer(patientPostnummer);
        patient.setPostort(patientPostort);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(signeringsdatum);
        if (relationKod != null) {
            Relation relation = new Relation();
            relation.setRelationIntygsId(relationIntygsId);
            relation.setRelationKod(relationKod);
            grundData.setRelation(relation);
        }
        when(utlatande.getGrundData()).thenReturn(grundData);
        return utlatande;
    }
}