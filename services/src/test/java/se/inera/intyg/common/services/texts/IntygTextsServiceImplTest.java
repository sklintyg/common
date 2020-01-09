/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.services.texts;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class IntygTextsServiceImplTest {

    @Mock
    private IntygTextsRepository repo;

    @Mock
    private CustomObjectMapper mapper;

    @InjectMocks
    private IntygTextsServiceImpl service = new IntygTextsServiceImpl();

    @Test
    public void testGetVersion() {
        when(repo.getLatestVersion(any(String.class))).thenReturn("1.0");
        String result = service.getLatestVersion("LISJP");
        verify(repo, times(1)).getLatestVersion("LISJP");
        assertEquals("result should be what repo returns", result, "1.0");
    }

    @Test
    public void testGetVersionNull() {
        when(repo.getLatestVersion(any(String.class))).thenReturn(null);
        String result = service.getLatestVersion("LISJP");
        verify(repo, times(1)).getLatestVersion("LISJP");
        assertEquals("result should be what repo returns", result, null);
    }

    @Test
    public void testGetTexts() throws JsonProcessingException {
        when(repo.getTexts(any(String.class), any(String.class))).thenReturn(null);
        when(mapper.writeValueAsString(any())).thenReturn("null");
        String result = service.getIntygTexts("LISJP", "0.9");
        verify(repo, times(1)).getTexts("LISJP", "0.9");
        verify(mapper, times(1)).writeValueAsString(null);
        assertEquals("result should be what mapper returns", result, "null");
    }
}
