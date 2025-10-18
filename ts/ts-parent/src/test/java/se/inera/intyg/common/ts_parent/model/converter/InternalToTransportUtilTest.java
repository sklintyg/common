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
package se.inera.intyg.common.ts_parent.model.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.PaTitle;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypVarden;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BefattningService.class})
class InternalToTransportUtilTest {

    private static final String PNR_TOLVAN = "19121212-1212";

    @Test
    void testConvertWithMillisInTimestamp() {
        final var grundData = buildGrundData(PNR_TOLVAN);
        grundData.setSigneringsdatum(LocalDateTime.of(2012, 8, 12, 12, 10, 42, 543));

        se.inera.intygstjanster.ts.services.v1.GrundData res = InternalToTransportUtil.buildGrundData(grundData);

        assertEquals("2012-08-12T12:10:42", res.getSigneringsTidstampel()); // millis is not valid in transport
    }

    @Test
    void testPersonnummerRoot() {
        final var grundData = buildGrundData(PNR_TOLVAN);

        se.inera.intygstjanster.ts.services.v1.GrundData res = InternalToTransportUtil.buildGrundData(grundData);

        assertEquals(Constants.PERSON_ID_OID, res.getPatient().getPersonId().getRoot());
        assertEquals(PNR_TOLVAN, res.getPatient().getPersonId().getExtension());
    }

    @Test
    void testSamordningRoot() {
        final var personnummer = "19800191-0002";
        final var grundData = buildGrundData(personnummer);
        se.inera.intygstjanster.ts.services.v1.GrundData res = InternalToTransportUtil.buildGrundData(grundData);

        assertEquals(Constants.SAMORDNING_ID_OID, res.getPatient().getPersonId().getRoot());
        assertEquals(personnummer, res.getPatient().getPersonId().getExtension());
    }

    @Test
    void testGetVersion() {
        assertFalse(InternalToTransportUtil.getVersion(setupUtlatandeWithTextVersion(null)).isPresent());
        assertEquals("6.7", InternalToTransportUtil.getVersion(setupUtlatandeWithTextVersion("6.7")).get());
        assertEquals("4.2", InternalToTransportUtil.getVersion(setupUtlatandeWithTextVersion("4.2")).get());
        assertEquals("1.0", InternalToTransportUtil.getVersion(setupUtlatandeWithTextVersion("1.0")).get());
    }

    @Test
    void testConvertDiabetesTyp() {
        final var res1 = InternalToTransportUtil.convertDiabetesTyp(DiabetesKod.DIABETES_TYP_1);
        final var res2 = InternalToTransportUtil.convertDiabetesTyp(DiabetesKod.DIABETES_TYP_2);

        assertEquals(DiabetesTypVarden.TYP_1, res1);
        assertEquals(DiabetesTypVarden.TYP_2, res2);
    }

    @Test
    void testBuildSkapadAvBefattningskodAndSpecialisering() {
        final var specialisering1 = "spec1";
        final var specialisering2 = "spec2";
        final var befattning1 = "befattning1";
        final var befattning2 = "befattning2";

        final var grundData = buildGrundData(PNR_TOLVAN);
        grundData.getSkapadAv().getSpecialiteter().addAll(List.of(specialisering1, specialisering2));
        grundData.getSkapadAv().getBefattningar().addAll(List.of(befattning1, befattning2));

        se.inera.intygstjanster.ts.services.v1.GrundData res = InternalToTransportUtil.buildGrundData(grundData);

        assertEquals(2, res.getSkapadAv().getSpecialiteter().size());
        assertEquals(specialisering1, res.getSkapadAv().getSpecialiteter().get(0));
        assertEquals(specialisering2, res.getSkapadAv().getSpecialiteter().get(1));
        assertEquals(2, res.getSkapadAv().getBefattningar().size());
        assertEquals(befattning1, res.getSkapadAv().getBefattningar().get(0));
        assertEquals(befattning2, res.getSkapadAv().getBefattningar().get(1));
    }

    @Nested
    class GetBefattningsKoder {

        @Test
        void shallReturnEmptyListIfNoBefattning() {
            final var grundData = buildGrundData(PNR_TOLVAN);

            final var result = InternalToTransportUtil.buildGrundData(grundData);

            assertEquals(0, result.getSkapadAv().getBefattningar().size());
        }

        @Test
        void shallReturnBefattningsKoderIfPresent() {
            final var grundData = buildGrundData(PNR_TOLVAN);
            final var befattning1 = createPaTitle("204010", "sjuksköterska");
            final var befattning2 = createPaTitle("401020", "läkare - AT");
            grundData.getSkapadAv().getBefattningsKoder().addAll(List.of(befattning1, befattning2));
            final var result = InternalToTransportUtil.buildGrundData(grundData);
            assertEquals(2, result.getSkapadAv().getBefattningar().size());
        }

        @Test
        void shallReturnBefattningIfBefattningsKoderIsEmpty() {
            final var grundData = buildGrundData(PNR_TOLVAN);
            final var befattning1 = "204010";
            grundData.getSkapadAv().getBefattningar().add(befattning1);
            final var result = InternalToTransportUtil.buildGrundData(grundData);
            assertAll(
                () -> assertEquals(1, result.getSkapadAv().getBefattningar().size()),
                () -> assertEquals("Läkare ej legitimerad, allmäntjänstgöring", result.getSkapadAv().getBefattningar().getFirst())
            );
        }

        @Test
        void shallNotReturnDuplicateBefattningsKoder() {
            final var grundData = buildGrundData(PNR_TOLVAN);
            final var befattning1 = createPaTitle("204010", "sjuksköterska");
            grundData.getSkapadAv().getBefattningsKoder().addAll(List.of(befattning1, befattning1));
            final var result = InternalToTransportUtil.buildGrundData(grundData);
            assertEquals(1, result.getSkapadAv().getBefattningar().size());
        }

        @Test
        void shallNotReturnDuplicateBefattning() {
            final var grundData = buildGrundData(PNR_TOLVAN);
            final var befattning1 = "204010";
            grundData.getSkapadAv().getBefattningar().addAll(List.of(befattning1, befattning1));
            final var result = InternalToTransportUtil.buildGrundData(grundData);
            assertEquals(1, result.getSkapadAv().getBefattningar().size());
        }

        @Test
        void shallPrioritizeBefattningsKoderOverBefattning() {
            final var grundData = buildGrundData(PNR_TOLVAN);
            final var befattning1 = createPaTitle("206090", "sjuksköterska");
            grundData.getSkapadAv().getBefattningsKoder().add(befattning1);
            grundData.getSkapadAv().getBefattningar().add("204010"); // AT - läkare
            final var result = InternalToTransportUtil.buildGrundData(grundData);
            assertEquals("sjuksköterska", result.getSkapadAv().getBefattningar().getFirst());
        }

    }

    private Personnummer createPnr(String pnr) {
        return Personnummer.createPersonnummer(pnr).get();
    }

    private Utlatande setupUtlatandeWithTextVersion(String textVersion) {
        final var source = mock(Utlatande.class);
        when(source.getTextVersion()).thenReturn(textVersion);
        return source;
    }

    private GrundData buildGrundData(String personNummer) {

        final var patient = new Patient();
        patient.setPersonId(createPnr(personNummer));

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);

        final var grundData = new GrundData();
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(LocalDateTime.now());
        grundData.setSkapadAv(skapadAv);
        return grundData;
    }

    private PaTitle createPaTitle(String kod, String klartext) {
        final var paTitle = new PaTitle();
        paTitle.setKod(kod);
        paTitle.setKlartext(klartext);
        return paTitle;
    }
}
