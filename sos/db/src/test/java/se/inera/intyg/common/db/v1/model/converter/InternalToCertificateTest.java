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

package se.inera.intyg.common.db.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_AUTOFILL_AFTER_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_AUTOFILL_WITHIN_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DODSPLATS_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_OSAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIVT_IMPLANTAT_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_AVLAGSNAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.KOMPLETTERANDE_PATIENTUPPGIFTER_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_PREFILL_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_PRINT_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_DELSVAR_ID;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class InternalToCertificateTest {

    private GrundData grundData;
    private CertificateTextProvider texts;
    private DbUtlatandeV1 internalCertificate;

    @BeforeEach
    void setup() {
        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);

        internalCertificate = DbUtlatandeV1.builder()
            .setGrundData(grundData)
            .setId("id")
            .setTextVersion("TextVersion")
            .setIdentitetStyrkt("IdentitetStyrkt")
            .setDodsdatumSakert(true)
            .setDodsdatum(new InternalDate(LocalDate.now()))
            .setDodsplatsKommun("DodsplatsKommun")
            .setDodsplatsBoende(DodsplatsBoende.SJUKHUS)
            .setBarn(false)
            .setExplosivImplantat(true)
            .setExplosivAvlagsnat(true)
            .setUndersokningYttre(Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN)
            .setUndersokningDatum(new InternalDate(LocalDate.now()))
            .setPolisanmalan(true)
            .build();

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Test
    void shallIncludeMetaData() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertNotNull(actualCertificate.getMetadata(), "Metadata is missing!");
    }

    @Test
    void shallIncludeCategoryKompletterandePatientuppgifterInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(0, actualCertificate.getData().get(KOMPLETTERANDE_PATIENTUPPGIFTER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIdentitetStyrkt() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(1, actualCertificate.getData().get(IDENTITET_STYRKT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryDodsdatumDodsplats() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(2, actualCertificate.getData().get(DODSDATUM_DODSPLATS_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsdatumSakert() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(3, actualCertificate.getData().get(DODSDATUM_SAKERT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsdatum() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(4, actualCertificate.getData().get(DODSDATUM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOsakertDodsdatum() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(5, actualCertificate.getData().get(DODSDATUM_OSAKERT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAntraffadDod() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(6, actualCertificate.getData().get(ANTRAFFAT_DOD_DATUM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsplatsKommun() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(7, actualCertificate.getData().get(DODSPLATS_KOMMUN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsplatsBoende() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(8, actualCertificate.getData().get(DODSPLATS_BOENDE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryBarn() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(9, actualCertificate.getData().get(BARN_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBarn() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(10, actualCertificate.getData().get(BARN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAutoFillWithinBarn() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(11, actualCertificate.getData().get(BARN_AUTOFILL_WITHIN_MESSAGE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAutoFillAfterBarn() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(12, actualCertificate.getData().get(BARN_AUTOFILL_AFTER_MESSAGE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryExplosivtImplantat() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(13, actualCertificate.getData().get(EXPLOSIVT_IMPLANTAT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionExplosivtImplantat() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(14, actualCertificate.getData().get(EXPLOSIV_IMPLANTAT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAvlagsnatImplantat() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(15, actualCertificate.getData().get(EXPLOSIV_AVLAGSNAT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryUndersokningYttre() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(16, actualCertificate.getData().get(UNDERSOKNING_YTTRE_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionUndersokningYttre() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(17, actualCertificate.getData().get(UNDERSOKNING_YTTRE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionUndersokningsdatum() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(18, actualCertificate.getData().get(UNDERSOKNING_DATUM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryPolisanmalan() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(19, actualCertificate.getData().get(POLISANMALAN_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionPolisanmalan() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(20, actualCertificate.getData().get(POLISANMALAN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionPrefillPolisanmalan() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(21, actualCertificate.getData().get(POLISANMALAN_PREFILL_MESSAGE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionPrintPolisanmalan() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(22, actualCertificate.getData().get(POLISANMALAN_PRINT_MESSAGE_DELSVAR_ID).getIndex());
    }
}