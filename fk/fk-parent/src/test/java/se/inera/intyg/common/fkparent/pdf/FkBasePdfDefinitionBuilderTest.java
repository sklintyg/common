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
package se.inera.intyg.common.fkparent.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.fkparent.pdf.model.FkPage;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;

/**
 * Created by marced on 2016-10-25.
 */
@RunWith(MockitoJUnitRunner.class)
public class FkBasePdfDefinitionBuilderTest {

    private static final String KEY_MISSING_TEXT = "missingtext";
    private static final String KEY_TEXT_FOUND_BASE = "textfound";
    private static final String KEY_TEXT_FOUND = "DFR_" + KEY_TEXT_FOUND_BASE + ".1.RBK";

    private static final String FKASSA_RECIPIENT_ID = "FKASSA";

    private IntygTexts intygTexts = getIntygTexts();

    private class FkBasePdfDefinitionBuilderForTest extends FkBasePdfDefinitionBuilder {

        public FkBasePdfDefinitionBuilderForTest(IntygTexts intygTexts) {
            this.intygTexts = intygTexts;
        }
    }

    @InjectMocks
    private FkBasePdfDefinitionBuilder builder = new FkBasePdfDefinitionBuilderForTest(intygTexts);

    @Test
    public void testIsSentToFk() throws Exception {

        assertFalse(builder.isSentToFk(null));

        List<Status> statuses = new ArrayList<>();
        assertFalse(builder.isSentToFk(statuses));

        statuses.add(new Status(null, null, LocalDateTime.now()));
        assertFalse(builder.isSentToFk(statuses));

        statuses.add(new Status(CertificateState.SENT, null, LocalDateTime.now()));
        statuses.add(new Status(CertificateState.RECEIVED, null, LocalDateTime.now()));
        assertFalse(builder.isSentToFk(statuses));

        statuses.add(new Status(CertificateState.SENT, FKASSA_RECIPIENT_ID, LocalDateTime.now()));
        assertTrue(builder.isSentToFk(statuses));

    }

    @Test
    public void testIsMakulerad() throws Exception {

        assertFalse(builder.isMakulerad(null));

        List<Status> statuses = new ArrayList<>();
        assertFalse(builder.isMakulerad(statuses));

        statuses.add(new Status(null, null, LocalDateTime.now()));
        assertFalse(builder.isMakulerad(statuses));

        statuses.add(new Status(CertificateState.SENT, null, LocalDateTime.now()));
        statuses.add(new Status(CertificateState.RECEIVED, null, LocalDateTime.now()));
        assertFalse(builder.isMakulerad(statuses));

        statuses.add(new Status(CertificateState.CANCELLED, FKASSA_RECIPIENT_ID, LocalDateTime.now()));
        assertTrue(builder.isMakulerad(statuses));

    }

    @Test
    public void testNullSafeString() throws Exception {
        InternalDate date = null;
        assertEquals("", builder.nullSafeString(date));

        date = new InternalDate();
        assertEquals(date.getDate(), builder.nullSafeString(date));

        String str = null;
        assertEquals("", builder.nullSafeString(str));

        str = "test";
        assertEquals(str, builder.nullSafeString(str));

    }

    @Test
    public void buildTillagsfragorPageNull() {
        FkPage page = builder.buildTillagsfragorPage(null);

        assertNull(page);
    }

    @Test
    public void buildTillagsfragorPageEmptyList() {
        ImmutableList<Tillaggsfraga> tillaggsfragas = ImmutableList.of();
        FkPage page = builder.buildTillagsfragorPage(tillaggsfragas);

        assertNull(page);
    }

    @Test
    public void buildTillagsfragorPageMissingText() {

        Tillaggsfraga tillaggsfraga = Tillaggsfraga.create(KEY_MISSING_TEXT, "");

        ImmutableList<Tillaggsfraga> tillaggsfragas = ImmutableList.of(tillaggsfraga);
        FkPage page = builder.buildTillagsfragorPage(tillaggsfragas);

        assertNull(page);
    }

    @Test
    public void buildTillagsfragorPage() {

        Tillaggsfraga tillaggsfraga = Tillaggsfraga.create(KEY_TEXT_FOUND_BASE, "svar");

        ImmutableList<Tillaggsfraga> tillaggsfragas = ImmutableList.of(tillaggsfraga);
        FkPage page = builder.buildTillagsfragorPage(tillaggsfragas);

        assertNotNull(page);
    }

    @Test
    public void testGetText() {
        String text = builder.getText(KEY_TEXT_FOUND);

        assertEquals("text", text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTextMissingText() throws Exception {
        builder.getText(KEY_MISSING_TEXT);
    }

    @Test
    public void testGetTextMissingAllowed() {
        String text = builder.getText(KEY_MISSING_TEXT, true);

        assertNull(text);
    }

    private static IntygTexts getIntygTexts() {
        SortedMap<String, String> texts = new TreeMap<>();

        texts.put(KEY_TEXT_FOUND, "text");

        return new IntygTexts("1.0.0.0", "", null, null, texts, null, null);
    }

}
