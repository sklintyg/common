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
package se.inera.intyg.common.lisjp.v1.model.converter.prefill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_1_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_2_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillResult.PrefillEventType;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillResult.SvarResult;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1.Builder;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;

public class PrefillHandlerTest {

    protected static final String UPPSLAGEN_DIAGNOSKODBESKRIVNING = "uppslagen-diagnoskodbeskrivning";

    private static final String INTYGSID = "abc-1";
    private static final String INTYGSTYPE = LisjpEntryPoint.MODULE_ID;
    private static final String INTYGSVERSION = "1.0";
    @Mock
    private WebcertModuleService webcertModuleService;
    private PrefillHandler testee;


    @Before
    public void setup() {
        webcertModuleService = mock(WebcertModuleService.class);
        when(webcertModuleService.getDescriptionFromDiagnosKod(anyString(), anyString())).thenReturn(UPPSLAGEN_DIAGNOSKODBESKRIVNING);
        when(webcertModuleService.validateDiagnosisCode(matches("J22|M46|S22|U07.1"), any(Diagnoskodverk.class)))
            .thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(matches("J22|M46|MX46|S22"))).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(matches("U07.1"))).thenReturn(false);
        testee = new PrefillHandler(webcertModuleService, INTYGSID, INTYGSTYPE, INTYGSVERSION);
    }

    @Test
    public void testPrefillNoFields() {
        PrefillScenario scenario = new PrefillScenario("lisjp-empty");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        assertTrue(result.getMessages().isEmpty());
        Assertions.assertThat(template.build()).isEqualTo(scenario.getUtlatande());
    }

    @Test
    public void testPrefillAllFields() {
        PrefillScenario scenario = new PrefillScenario("lisjp-full");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        assertTrue(result.getMessages().isEmpty());
        Assertions.assertThat(template.build()).isEqualTo(scenario.getUtlatande());

    }

    /**
     * Verify that when a Forifylland of a sjukskrivningsgrad's dateperiod is not present - a default startdate is set as todays date and
     * that a INFO message is returned.
     */
    @Test
    public void testPrefillSjukskrivningStartDateDefaultValue() {

        PrefillScenario scenario = new PrefillScenario("lisjp-defaulting-sjukskrivning-startdate");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        LisjpUtlatandeV1 utlatande = template.build();

        Assertions.assertThat(utlatande).isEqualToIgnoringGivenFields(scenario.getUtlatande(), "sjukskrivningar");
        assertEquals(1, result.getMessages().size());
        final SvarResult svarResult = result.getMessages().get(0);
        assertEquals(PrefillEventType.INFO, svarResult.getEventType());
        assertEquals(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, svarResult.getSvarId());
        final Sjukskrivning sjukskrivning = utlatande.getSjukskrivningar().stream()
            .filter(ss -> ss.getSjukskrivningsgrad().equals(SjukskrivningsGrad.HELT_NEDSATT)).findFirst().get();
        assertEquals(LocalDate.now(), sjukskrivning.getPeriod().getFrom().asLocalDate());

    }

    /**
     * Verify that when a field (funktionsnedsattning) is longer than maxlength - it's ignored with a warning message
     */
    @Test
    public void testPrefillHandlesMaxStringLength() {

        PrefillScenario scenario = new PrefillScenario("lisjp-freetext-maxlength");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        LisjpUtlatandeV1 utlatande = template.build();

        Assertions.assertThat(utlatande).isEqualTo(scenario.getUtlatande());
        assertEquals(1, result.getMessages().size());
        final SvarResult svarResult = result.getMessages().get(0);
        assertEquals(PrefillEventType.WARNING, svarResult.getEventType());
        assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_35, svarResult.getSvarId());

    }

    /**
     * Verify that when a Forifylland of a GrundForMu date is not present or invalid- a default date is set as todays date and
     * that a INFO message of the fallback handling is returned.
     */
    @Test
    public void testPrefillGrundForMUDateDefaultValue() {

        PrefillScenario scenario = new PrefillScenario("lisjp-defaulting-grundformu-date");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());
        LisjpUtlatandeV1 utlatande = template.build();
        Assertions.assertThat(utlatande)
            .isEqualToIgnoringGivenFields(scenario.getUtlatande(), "undersokningAvPatienten", "telefonkontaktMedPatienten",
                "journaluppgifter",
                "annatGrundForMU");

        final List<SvarResult> infos = result.getMessages().stream().filter(sr -> sr.getEventType().equals(PrefillEventType.INFO)).collect(
            Collectors.toList());
        assertEquals(4, infos.size());
        for (SvarResult item : infos) {
            assertTrue(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1.equals(item.getSvarId()));
        }

        final List<SvarResult> warnings = result.getMessages().stream().filter(sr -> sr.getEventType().equals(PrefillEventType.WARNING))
            .collect(
                Collectors.toList());
        assertEquals(3, warnings.size());
        for (SvarResult item : warnings) {
            assertTrue(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1.equals(item.getSvarId()));
        }

    }

    /**
     * Verify that when a Forifylland of a diagnose's description - a default value is set and
     * that a INFO message is returned.
     */
    @Test
    public void testPrefillDiagnosDescriptionDefaultValue() {

        PrefillScenario scenario = new PrefillScenario("lisjp-defaulting-diagnose-beskrivning");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        LisjpUtlatandeV1 utlatande = template.build();
        Assertions.assertThat(utlatande).isEqualTo(scenario.getUtlatande());

        assertEquals(1, result.getMessages().size());
        final SvarResult svarResult = result.getMessages().get(0);
        assertEquals(PrefillEventType.INFO, svarResult.getEventType());
        assertEquals(DIAGNOS_BESKRIVNING_DELSVAR_ID_6, svarResult.getSvarId());

        assertEquals(1, utlatande.getDiagnoser().size());
        final Diagnos diagnos = utlatande.getDiagnoser().get(0);
        assertEquals(UPPSLAGEN_DIAGNOSKODBESKRIVNING, diagnos.getDiagnosBeskrivning());

    }

    /**
     * Verify that when a Forifylland of diagnose - the actual diagnose code must be known to us or it will be ignored
     */
    @Test
    public void testPrefillDiagnosInvalidIcd10Code() {

        PrefillScenario scenario = new PrefillScenario("lisjp-ignored-diagnose-code");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        LisjpUtlatandeV1 utlatande = template.build();
        Assertions.assertThat(utlatande).isEqualTo(scenario.getUtlatande());

        assertEquals(2, result.getMessages().size());
        for (SvarResult message : result.getMessages()) {
            final SvarResult svarResult = message;
            assertEquals(PrefillEventType.WARNING, svarResult.getEventType());
            assertTrue(Arrays.asList(BIDIAGNOS_1_DELSVAR_ID_6, BIDIAGNOS_2_DELSVAR_ID_6).contains(svarResult.getSvarId()));
        }
    }

    @Test
    public void testPrefillDiagnosInvalidIcd10CodeFormat() {

        PrefillScenario scenario = new PrefillScenario("lisjp-ignored-diagnose-code-invalid-format");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        LisjpUtlatandeV1 utlatande = template.build();
        Assertions.assertThat(utlatande).isEqualTo(scenario.getUtlatande());

        assertEquals(1, result.getMessages().size());
        for (SvarResult message : result.getMessages()) {
            final SvarResult svarResult = message;
            assertEquals(PrefillEventType.WARNING, svarResult.getEventType());
            assertTrue(Arrays.asList(BIDIAGNOS_1_DELSVAR_ID_6).contains(svarResult.getSvarId()));
        }
    }

    @Test
    public void shouldIgnoreDuplicatedPrefillValuesForSysselsattning() {
        final var scenario = new PrefillScenario("lisjp-duplicated-values");
        final var template = getEmptyUtlatande();
        testee.prefill(template, scenario.getForifyllnad());
        final var utlatande = template.build();
        assertEquals(4, Objects.requireNonNull(utlatande.getSysselsattning()).size());
        assertEquals(1,
            utlatande.getSysselsattning()
                .stream()
                .filter(Objects::nonNull)
                .map(Sysselsattning::getTyp)
                .filter(typ -> typ == SysselsattningsTyp.ARBETSSOKANDE)
                .count());
    }

    @Test
    public void shouldIgnoreDuplicatedPrefillValuesForDiagnos() {
        final var scenario = new PrefillScenario("lisjp-duplicated-values");
        final var template = getEmptyUtlatande();
        testee.prefill(template, scenario.getForifyllnad());
        final var utlatande = template.build();
        assertEquals(3, Objects.requireNonNull(utlatande.getDiagnoser()).size());
        assertEquals(1,
            utlatande.getDiagnoser()
                .stream()
                .filter(Objects::nonNull)
                .map(Diagnos::getDiagnosKod)
                .filter(Objects::nonNull)
                .filter(typ -> typ.equals("J22"))
                .count());
    }

    @Test
    public void shouldIgnoreDuplicatedPrefillValuesForSjukskrivningar() {
        final var scenario = new PrefillScenario("lisjp-duplicated-values");
        final var template = getEmptyUtlatande();
        testee.prefill(template, scenario.getForifyllnad());
        final var utlatande = template.build();
        assertEquals(4, Objects.requireNonNull(utlatande.getSjukskrivningar()).size());
        assertEquals(1,
            utlatande.getSjukskrivningar()
                .stream()
                .filter(Objects::nonNull)
                .map(Sjukskrivning::getSjukskrivningsgrad)
                .filter(typ -> typ == SjukskrivningsGrad.HELT_NEDSATT)
                .count());
    }

    @Test
    public void shouldIgnoreDuplicatedPrefillValuesForAktivitetskategorier() {
        final var scenario = new PrefillScenario("lisjp-duplicated-values");
        final var template = getEmptyUtlatande();
        testee.prefill(template, scenario.getForifyllnad());
        final var utlatande = template.build();
        assertEquals(10, Objects.requireNonNull(utlatande.getArbetslivsinriktadeAtgarder()).size());
        assertEquals(1,
            utlatande.getArbetslivsinriktadeAtgarder()
                .stream()
                .filter(Objects::nonNull)
                .map(ArbetslivsinriktadeAtgarder::getTyp)
                .filter(typ -> typ == ArbetslivsinriktadeAtgarderVal.ARBETSTRANING)
                .count());
    }

    @Test
    public void shouldLogIgnoredDuplicatedPrefillValues() {
        final var expectedSvarId = List.of("28", "32", "40");
        final var scenario = new PrefillScenario("lisjp-duplicated-values");
        final var template = getEmptyUtlatande();
        final var result = testee.prefill(template, scenario.getForifyllnad());
        for (SvarResult message : result.getMessages()) {
            assertEquals(message.getEventType(), PrefillEventType.INFO);
            assertTrue(expectedSvarId.contains(message.getSvarId()));
            assertEquals(message.getMessage(), "Value already exists and will be ignored");
        }
    }


    private Builder getEmptyUtlatande() {
        Builder template = LisjpUtlatandeV1.builder();
        //Set the minimal properties that are required for a valid utlatande to be built().
        template.setId("id1");
        template.setTextVersion("1.0");
        template.setGrundData(new GrundData());
        return template;
    }
}
