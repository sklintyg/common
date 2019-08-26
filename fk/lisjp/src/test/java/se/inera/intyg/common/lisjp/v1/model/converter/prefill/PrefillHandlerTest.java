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
package se.inera.intyg.common.lisjp.v1.model.converter.prefill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID_6;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillResult.PrefillEventType;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillResult.SvarResult;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1.Builder;
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
        testee = new PrefillHandler(webcertModuleService, INTYGSID, INTYGSTYPE, INTYGSVERSION);
    }

    @Test
    public void testPrefillNoFields() {
        PrefillScenario scenario = new PrefillScenario("lisjp-empty");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        assertTrue(result.getMessages().isEmpty());
        Assertions.assertThat(scenario.getUtlatande()).isEqualTo(template.build());
    }

    @Test
    public void testPrefillAllFields() {
        PrefillScenario scenario = new PrefillScenario("lisjp-full");
        LisjpUtlatandeV1.Builder template = getEmptyUtlatande();

        final PrefillResult result = testee.prefill(template, scenario.getForifyllnad());

        assertTrue(result.getMessages().isEmpty());
        Assertions.assertThat(scenario.getUtlatande()).isEqualTo(template.build());

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
        Assertions.assertThat(scenario.getUtlatande()).isEqualToIgnoringGivenFields(utlatande, "sjukskrivningar");

        assertEquals(1, result.getMessages().size());
        final SvarResult svarResult = result.getMessages().get(0);
        assertEquals(PrefillEventType.INFO, svarResult.getEventType());
        assertEquals(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, svarResult.getSvarId());
        final Sjukskrivning sjukskrivning = utlatande.getSjukskrivningar().stream()
            .filter(ss -> ss.getSjukskrivningsgrad().equals(SjukskrivningsGrad.HELT_NEDSATT)).findFirst().get();
        assertEquals(LocalDate.now(), sjukskrivning.getPeriod().getFrom().asLocalDate());

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
        Assertions.assertThat(scenario.getUtlatande()).isEqualTo(utlatande);

        assertEquals(1, result.getMessages().size());
        final SvarResult svarResult = result.getMessages().get(0);
        assertEquals(PrefillEventType.INFO, svarResult.getEventType());
        assertEquals(DIAGNOS_BESKRIVNING_DELSVAR_ID_6, svarResult.getSvarId());

        assertEquals(1, utlatande.getDiagnoser().size());
        final Diagnos diagnos = utlatande.getDiagnoser().get(0);
        assertEquals(UPPSLAGEN_DIAGNOSKODBESKRIVNING, diagnos.getDiagnosBeskrivning());

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
