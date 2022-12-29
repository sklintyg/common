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

package se.inera.intyg.common.fk7263.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.AKTIVITETSBEGRANSNINGAR_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.AKTIVITETSBEGRANSNINGAR_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ANNAN_ATGARD_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ANNAT_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETFORMAGA_BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETFORMAGA_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETFORMAGA_FORSAKRINGSMEDICINSKA_BESLUTSTODET_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETFORMAGA_FORSAKRINGSMEDICINSKA_BESLUTSTODET_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETFORMAGA_PROGRNOS_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETFORMAGA_PROGRNOS_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSLIVSINRIKTAD_REHABILITERING_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ARBETSLIVSINRIKTAD_REHABILITERING_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.BEHANDLING_ELLER_ATGARD_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.DIAGNOS_FORTYDLIGANDE_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.HUVUDDIAGNOSKOD_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.INOM_SJUKVARD_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.INTYGET_BASERAS_PA_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.JOURNALUPPGIFTER_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETFORMAGA_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.PATIENTENS_ARBETFORMAGA_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.REKOMMENDATIONER_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.REKOMMENDATIONER_KONTAKT_MED_AF_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.REKOMMENDATIONER_KONTAKT_MED_FHV_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.REKOMMENDATIONER_OVRIGT_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.SJUKDOMSFORLOPP_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.TELEFONKONTAKT_MED_PATIENTEN_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.UNDERSOKNING_AV_PATIENTEN_DELSVAR_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class InternalToCertificateTest {

    @Mock
    private CertificateMessagesProvider messagesProvider;

    private Fk7263Utlatande internalCertificate;

    @BeforeEach
    void setUp() {
        internalCertificate = new Fk7263Utlatande();
        internalCertificate.setId("certificateId");
        internalCertificate.setGrundData(getGrundData());
        when(messagesProvider.get(any(String.class))).thenReturn("test string!");
    }

    private static GrundData getGrundData() {
        final var unit = new Vardenhet();
        final var skapadAv = new HoSPersonal();
        final var patient = new Patient();
        final var grundData = new GrundData();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        skapadAv.setVardenhet(unit);
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        return grundData;
    }

    @Test
    void shallIncludeMetadata() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertNotNull(actualCertificate.getMetadata(), "Shall contain metadata");
    }

    @Test
    void shallIncludeCategoryAvstangningEnligtSmittskyddslagen() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(0, actualCertificate.getData().get(AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAvstangningEnligtSmittskyddslagen() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(1, actualCertificate.getData().get(AVSTANGNING_ENLIGT_SMITTSKYDDSLAGEN_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryDiagnos() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(2, actualCertificate.getData().get(DIAGNOS_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHuvuddiagnoskod() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(3, actualCertificate.getData().get(HUVUDDIAGNOSKOD_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFortydligandeDiagnos() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(4, actualCertificate.getData().get(DIAGNOS_FORTYDLIGANDE_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategorySjukdomforlopp() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(5, actualCertificate.getData().get(SJUKDOMSFORLOPP_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSjukdomforlopp() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(6, actualCertificate.getData().get(SJUKDOMSFORLOPP_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryFunktionsnedsattningar() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(7, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningar() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(8, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryIntygetBaserasPa() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(9, actualCertificate.getData().get(INTYGET_BASERAS_PA_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIntygetBaserasPaUndersokning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(10, actualCertificate.getData().get(UNDERSOKNING_AV_PATIENTEN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIntygetBaserasPaTelefonkontakt() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(11, actualCertificate.getData().get(TELEFONKONTAKT_MED_PATIENTEN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIntygetBaserasPaJournaluppgifter() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(12, actualCertificate.getData().get(JOURNALUPPGIFTER_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIntygetBaserasPaAnnat() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(13, actualCertificate.getData().get(ANNAT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryAktivitetsbegransningar() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(14, actualCertificate.getData().get(AKTIVITETSBEGRANSNINGAR_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAktivitetsbegransningar() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(15, actualCertificate.getData().get(AKTIVITETSBEGRANSNINGAR_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryRekommendationer() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(16, actualCertificate.getData().get(REKOMMENDATIONER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionRekommendationerKontaktMedAf() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(17, actualCertificate.getData().get(REKOMMENDATIONER_KONTAKT_MED_AF_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionRekommendationerKontaktMedFhv() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(18, actualCertificate.getData().get(REKOMMENDATIONER_KONTAKT_MED_FHV_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionRekommendationerOvrigt() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(19, actualCertificate.getData().get(REKOMMENDATIONER_OVRIGT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryBehandlingEllerAtgard() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(20, actualCertificate.getData().get(BEHANDLING_ELLER_ATGARD_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBehandlingEllerAtgardSjukvard() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(21, actualCertificate.getData().get(INOM_SJUKVARD_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBehandlingEllerAtgardAnnanAtgard() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(22, actualCertificate.getData().get(ANNAN_ATGARD_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryArbetslivsinriktadRehabilitering() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(23, actualCertificate.getData().get(ARBETSLIVSINRIKTAD_REHABILITERING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetslivsinriktadRehabilitering() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(24, actualCertificate.getData().get(ARBETSLIVSINRIKTAD_REHABILITERING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryPatientensArbetsformagaBedoms() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(25, actualCertificate.getData().get(PATIENTENS_ARBETFORMAGA_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionPatientensArbetsformagaBedoms() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(26, actualCertificate.getData().get(PATIENTENS_ARBETFORMAGA_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryArbetsformogaBedomning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(27, actualCertificate.getData().get(ARBETFORMAGA_BEDOMNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetsformogaBedomning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(28, actualCertificate.getData().get(ARBETFORMAGA_BEDOMNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryArbetsformogaForsakringsmedicinskaBeslutstodet() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(29, actualCertificate.getData().get(ARBETFORMAGA_FORSAKRINGSMEDICINSKA_BESLUTSTODET_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetsformogaForsakringsmedicinskaBeslutstodet() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(30, actualCertificate.getData().get(ARBETFORMAGA_FORSAKRINGSMEDICINSKA_BESLUTSTODET_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryArbetsformogaPrognos() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(31, actualCertificate.getData().get(ARBETFORMAGA_PROGRNOS_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetsformogaPrognos() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, messagesProvider);
        assertEquals(32, actualCertificate.getData().get(ARBETFORMAGA_PROGRNOS_SVAR_ID).getIndex());
    }
}