/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import tools.jackson.core.JacksonException;

@ExtendWith(MockitoExtension.class)
public class IntygTextsServiceImplTest {

  @Mock private IntygTextsRepository repo;

  @Mock private CustomObjectMapper mapper;

  @InjectMocks private final IntygTextsServiceImpl service = new IntygTextsServiceImpl();

  @Test
  public void testGetVersion() {
    when(repo.getLatestVersion(any(String.class))).thenReturn("1.0");
    String result = service.getLatestVersion("LISJP");
    verify(repo, times(1)).getLatestVersion("LISJP");
    assertEquals("1.0", result, "result should be what repo returns");
  }

  @Test
  public void testGetVersionNull() {
    when(repo.getLatestVersion(any(String.class))).thenReturn(null);
    String result = service.getLatestVersion("LISJP");
    verify(repo, times(1)).getLatestVersion("LISJP");
    assertNull(result, "result should be what repo returns");
  }

  @Test
  public void testGetTexts() throws JacksonException {
    when(repo.getTexts(any(String.class), any(String.class))).thenReturn(null);
    when(mapper.writeValueAsString(any())).thenReturn("null");
    String result = service.getIntygTexts("LISJP", "0.9");
    verify(repo, times(1)).getTexts("LISJP", "0.9");
    verify(mapper, times(1)).writeValueAsString(null);
    assertEquals("null", result, "result should be what mapper returns");
  }

  @Test
  public void shallReturnFalseIfNotLatestMajorVersion() {
    doReturn("2.0").when(repo).getLatestVersion("LISJP");
    final var actual = service.isLatestMajorVersion("LISJP", "1.2");
    assertFalse(actual);
  }

  @Test
  public void shallReturnTrueIfLatestMajorVersion() {
    doReturn("2.0").when(repo).getLatestVersion("LISJP");
    final var actual = service.isLatestMajorVersion("LISJP", "2.0");
    assertTrue(actual);
  }

  @Test
  public void shallReturnTrueIfLatestMajorVersionButDifferentMinorVersion() {
    doReturn("2.1").when(repo).getLatestVersion("LISJP");
    final var actual = service.isLatestMajorVersion("LISJP", "2.0");
    assertTrue(actual);
  }

  @Test
  public void shallReturnTrueForIsLatestMajorVersionWhenGetLatestVersionReturnsNull() {
    doReturn(null).when(repo).getLatestVersion("fk7263");
    final var actual = service.isLatestMajorVersion("fk7263", "1.0");
    assertTrue(actual);
  }
}
