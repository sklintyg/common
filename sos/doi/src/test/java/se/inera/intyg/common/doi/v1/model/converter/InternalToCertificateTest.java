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

package se.inera.intyg.common.doi.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_AUTOFILL_AFTER_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_AUTOFILL_WITHIN_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DODSPLATS_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_OSAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAKS_UPPGIFTER_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_DELSVAR_ID;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypeAhead;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadEnum;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.schemas.contract.Personnummer;

class InternalToCertificateTest {

    private InternalToCertificate internalToCertificate;

    private GrundData grundData;
    private DoiUtlatandeV1 internalCertificate;
    private CertificateTextProvider texts;
    private TypeAheadProvider typeAheadProvider;

    private List<String> kommuner = List.of("Östersund", "Strömsund", "Stockholm");

    @BeforeEach
    void setUp() {
        internalToCertificate = new InternalToCertificate();

        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);

        internalCertificate = DoiUtlatandeV1.builder()
            .setId("certificateId")
            .setTextVersion("1.0")
            .setGrundData(grundData)
            .setIdentitetStyrkt("IdentitetStyrkt")
            .setLand("Land")
            .setDodsdatumSakert(true)
            .setDodsdatum(new InternalDate(LocalDate.now()))
            .setDodsplatsKommun("DodsplatsKommun")
            .setDodsplatsBoende(DodsplatsBoende.SJUKHUS)
            .setBarn(false)
            .setOperation(OmOperation.JA)
            .setOperationDatum(new InternalDate(LocalDate.now()))
            .setOperationAnledning("OperationAnledning")
            .setForgiftning(true)
            .setForgiftningOrsak(ForgiftningOrsak.OLYCKSFALL)
            .setGrunder(Collections.emptyList())
            .build();

        texts = mock(CertificateTextProvider.class);
        typeAheadProvider = mock(TypeAheadProvider.class);
        doReturn(kommuner).when(typeAheadProvider).getValues(TypeAheadEnum.MUNICIPALITIES);
    }

    @Test
    void shallIncludeMetadata() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertNotNull(actualCertificate.getMetadata(), "Shall contain metadata");
    }

    @Test
    void shallIncludeQuestionIdentitetStyrkt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(1, actualCertificate.getData().get(IDENTITET_STYRKT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionLand() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(2, actualCertificate.getData().get(LAND_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryDodsdatumDodsplats() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(3, actualCertificate.getData().get(DODSDATUM_DODSPLATS_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsdatumSakert() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(4, actualCertificate.getData().get(DODSDATUM_SAKERT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsdatum() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(5, actualCertificate.getData().get(DODSDATUM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOsakertDodsdatum() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(6, actualCertificate.getData().get(DODSDATUM_OSAKERT_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAntraffadDod() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(7, actualCertificate.getData().get(ANTRAFFAT_DOD_DATUM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsplats() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(8, actualCertificate.getData().get(DODSPLATS_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsplatsKommun() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(9, actualCertificate.getData().get(DODSPLATS_KOMMUN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDodsplatsKommunWithListOfKommun() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        final var certificateDataElement = actualCertificate.getData().get(DODSPLATS_KOMMUN_DELSVAR_ID);
        final var certificateDataConfigTypeAhead = (CertificateDataConfigTypeAhead) certificateDataElement.getConfig();
        assertEquals(kommuner, certificateDataConfigTypeAhead.getTypeAhead());
    }

    @Test
    void shallIncludeQuestionDodsplatsBoende() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(10, actualCertificate.getData().get(DODSPLATS_BOENDE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryBarn() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(11, actualCertificate.getData().get(BARN_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBarn() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(12, actualCertificate.getData().get(BARN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAutoFillWithinBarn() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(13, actualCertificate.getData().get(BARN_AUTOFILL_WITHIN_MESSAGE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAutoFillAfterBarn() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(14, actualCertificate.getData().get(BARN_AUTOFILL_AFTER_MESSAGE_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryOperation() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(15, actualCertificate.getData().get(OPERATION_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOperation() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(16, actualCertificate.getData().get(OPERATION_OM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOperationDatum() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(17, actualCertificate.getData().get(OPERATION_DATUM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOperationDatumNull() {
        final var internalCertificeOperationDatumNull = internalCertificate.toBuilder().setOperationDatum(null).build();
        final var actualCertificate = internalToCertificate.convert(internalCertificeOperationDatumNull, texts, typeAheadProvider);
        assertEquals(17, actualCertificate.getData().get(OPERATION_DATUM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOperationDatumInvalidDate() {
        final var internalCertificeOperationDatumNull = internalCertificate.toBuilder()
            .setOperationDatum(new InternalDate("2020"))
            .build();
        final var actualCertificate = internalToCertificate.convert(internalCertificeOperationDatumNull, texts, typeAheadProvider);
        assertEquals(17, actualCertificate.getData().get(OPERATION_DATUM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOperationAnledning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(18, actualCertificate.getData().get(OPERATION_ANLEDNING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategorySkadaForgiftning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(19, actualCertificate.getData().get(FORGIFTNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSkadaForgiftning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(20, actualCertificate.getData().get(FORGIFTNING_OM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionForgiftningOrsak() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(21, actualCertificate.getData().get(FORGIFTNING_ORSAK_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryDodsorsaksUppgifter() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(22, actualCertificate.getData().get(DODSORSAKS_UPPGIFTER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionGrunderDodsorsaksUppgifter() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, texts, typeAheadProvider);
        assertEquals(23, actualCertificate.getData().get(GRUNDER_DELSVAR_ID).getIndex());
    }
}
