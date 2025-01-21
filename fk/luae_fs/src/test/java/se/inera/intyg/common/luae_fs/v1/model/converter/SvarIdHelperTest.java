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
package se.inera.intyg.common.luae_fs.v1.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.converter.util.ConverterException;

@RunWith(MockitoJUnitRunner.class)
public class SvarIdHelperTest {

    private final InternalDate INTERNAL_DATE = new InternalDate();
    private SvarIdHelperImpl svarIdHelper = new SvarIdHelperImpl();

    @Test
    public void testCalculateFrageIdHandleForGrundForMUNoValues() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(null, null, null, null));
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(0));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUUndersokningAvPatienten() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(INTERNAL_DATE, null, null, null));
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1, res.get(1));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUJournalUppgifter() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(null, INTERNAL_DATE, null, null));
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1, res.get(1));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUAnhorigsBeskrivningAvPatienten() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(null, null, INTERNAL_DATE, null));
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1, res.get(1));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUAnnatGrundForMU() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(null, null, null, INTERNAL_DATE));
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, res.get(1));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUAll() throws ConverterException {
        List<String> res = svarIdHelper
            .calculateFrageIdHandleForGrundForMU(buildUtlatande(INTERNAL_DATE, INTERNAL_DATE, INTERNAL_DATE, INTERNAL_DATE));
        assertNotNull(res);
        assertEquals(5, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1, res.get(1));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1, res.get(2));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1, res.get(3));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, res.get(4));
    }

    private LuaefsUtlatandeV1 buildUtlatande(InternalDate undersokningAvPatienten, InternalDate journaluppgifter,
        InternalDate anhorigsBeskrivningAvPatienten, InternalDate annatGrundForMU) {
        return LuaefsUtlatandeV1.builder()
            .setId("intygId")
            .setGrundData(new GrundData())
            .setTextVersion("v1.0")
            .setUndersokningAvPatienten(undersokningAvPatienten)
            .setJournaluppgifter(journaluppgifter)
            .setAnhorigsBeskrivningAvPatienten(anhorigsBeskrivningAvPatienten)
            .setAnnatGrundForMU(annatGrundForMU)
            .build();
    }
}
