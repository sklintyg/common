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
package se.inera.intyg.common.tstrk1062.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint.MODULE_ID;

import java.util.Optional;
import java.util.SortedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

@ExtendWith(MockitoExtension.class)
public class TsTrk1062EntryPointTest {

  private IntygTextsRepository mockIntygTextsRepository;

  private TsTrk1062EntryPoint tsTrk1062EntryPoint;

  private static String LATEST_VERSION = "1.0";

  @BeforeEach
  public void setUp() {
    mockIntygTextsRepository = mock(IntygTextsRepository.class);
    tsTrk1062EntryPoint = new TsTrk1062EntryPoint(Optional.of(mockIntygTextsRepository));
  }

  @Test
  public void testGetModuleId() throws Exception {
    final String actualModuleId = tsTrk1062EntryPoint.getModuleId();
    assertNotNull(actualModuleId, "ModuleId should not be null");
    assertFalse(actualModuleId.isEmpty(), "ModuleId should not be empty");
  }

  @Test
  public void testGetModuleName() throws Exception {
    final String actualModuleName = tsTrk1062EntryPoint.getModuleName();
    assertNotNull(actualModuleName, "ModuleName should not be null");
    assertFalse(actualModuleName.isEmpty(), "ModuleName should not be empty");
  }

  @Test
  public void testGetModuleDescription() throws Exception {
    final String actualModuleDescription = tsTrk1062EntryPoint.getModuleDescription();
    assertNotNull(actualModuleDescription, "ModuleDescription should not be null");
    assertFalse(actualModuleDescription.isEmpty(), "ModuleDescription should not be empty");
  }

  @Test
  public void testGetDetailedModuleDescription() throws Exception {
    final String expectedDetailedModuleDescription = "Text";
    final SortedMap<String, String> mockedTextMap = mock(SortedMap.class);
    doReturn(expectedDetailedModuleDescription).when(mockedTextMap).get(any());

    final IntygTexts expectedIntygTexts =
        new IntygTexts("1.0", "tstrk1062", null, null, mockedTextMap, null, null);

    doReturn(LATEST_VERSION).when(mockIntygTextsRepository).getLatestVersion(MODULE_ID);
    doReturn(expectedIntygTexts).when(mockIntygTextsRepository).getTexts(MODULE_ID, LATEST_VERSION);

    final String actualDetailedModuleDescription =
        tsTrk1062EntryPoint.getDetailedModuleDescription();

    assertNotNull(actualDetailedModuleDescription, "DetailedModuleDescription should not be null");
    assertEquals(
        expectedDetailedModuleDescription,
        actualDetailedModuleDescription,
        "DetailedModuleDescription not equal");
  }

  @Test
  public void testGetDetailedModuleDescriptionMissingTexts() throws Exception {
    doReturn(LATEST_VERSION).when(mockIntygTextsRepository).getLatestVersion(MODULE_ID);
    doReturn(null).when(mockIntygTextsRepository).getTexts(MODULE_ID, LATEST_VERSION);

    final String actualDetailedModuleDescription =
        tsTrk1062EntryPoint.getDetailedModuleDescription();

    assertNull(actualDetailedModuleDescription, "DetailedModuleDescription should be null");
  }

  @Test
  public void testGetDetailedModuleDescriptionMissingRepo() throws Exception {
    final TsTrk1062EntryPoint tsTrk1062EntryPointWithoutRepo = new TsTrk1062EntryPoint();
    final String actualDetailedModuleDescription =
        tsTrk1062EntryPointWithoutRepo.getDetailedModuleDescription();

    assertNull(actualDetailedModuleDescription, "DetailedModuleDescription should be null");
  }

  @Test
  public void testGetModuleCssPathMinaIntyg() throws Exception {
    final String actualPath = tsTrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.MINA_INTYG);
    assertNotNull(actualPath, "Mina Intyg cssPath should not be null");
    assertTrue(actualPath.isEmpty(), "Mina Intyg cssPath should be empty");
  }

  @Test
  public void testGetModuleCssPathWebCert() throws Exception {
    final String expectedPath = "/web/webjars/tstrk1062/webcert/css/tstrk1062.css";
    final String actualPath = tsTrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.WEBCERT);
    assertNotNull(actualPath, "WebCert cssPath should not be null");
    assertEquals(expectedPath, actualPath, "WebCert cssPath not equal");
  }

  @Test
  public void testGetModuleCssPathIntygstjansten() throws Exception {
    final String actualPath = tsTrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.INTYGSTJANST);
    assertNull(actualPath, "Intygstjanst cssPath should be null");
  }

  @Test
  public void testGetModuleScriptPathMinaIntyg() throws Exception {
    final String expectedPath = "/web/webjars/tstrk1062/minaintyg/js/module";
    final String actualPath = tsTrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.MINA_INTYG);
    assertNotNull(actualPath, "Mina intyg scriptpath should not be null");
    assertEquals(expectedPath, actualPath, "Mina intyg scriptpath not equal");
  }

  @Test
  public void testGetModuleScriptPathWebCert() throws Exception {
    final String expectedPath = "/web/webjars/tstrk1062/webcert/module";
    final String actualPath = tsTrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.WEBCERT);
    assertNotNull(actualPath, "WebCert scriptpath should not be null");
    assertEquals(expectedPath, actualPath, "WebCert scriptpath not equal");
  }

  @Test
  public void testGetModuleScriptPathIntygstjansten() throws Exception {
    final String actualPath =
        tsTrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.INTYGSTJANST);
    assertNull(actualPath, "Intygstjanst scriptpath should be null");
  }

  @Test
  public void testGetModuleDependencyPathMinaIntyg() throws Exception {
    final String expectedPath = "/web/webjars/tstrk1062/minaintyg/js/module-deps.json";
    final String actualPath =
        tsTrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.MINA_INTYG);
    assertNotNull(actualPath, "Mina intyg dependencyPath should not be null");
    assertEquals(expectedPath, actualPath, "Mina intyg dependencyPath not equal");
  }

  @Test
  public void testGetModuleDependencyPathWebCert() throws Exception {
    final String expectedPath = "/web/webjars/tstrk1062/webcert/module-deps.json";
    final String actualPath =
        tsTrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.WEBCERT);
    assertNotNull(actualPath, "WebCert dependencyPath should not be null");
    assertEquals(expectedPath, actualPath, "WebCert dependencyPath not equal");
  }

  @Test
  public void testGetModuleDependencyPathIntygstjansten() throws Exception {
    final String actualPath =
        tsTrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.INTYGSTJANST);
    assertNull(actualPath, "Intygstjanst dependencyPath should be null");
  }

  @Test
  public void testGetDefaultRecipient() throws Exception {
    final String expectedRecipient = "TRANSP";
    final String actualRecipient = tsTrk1062EntryPoint.getDefaultRecipient();
    assertNotNull(actualRecipient, "Recipient should not be null");
    assertEquals(expectedRecipient, actualRecipient, "Recipient not equal");
  }

  @Test
  public void testGetExternalId() throws Exception {
    final String expectedExternalId = "TSTRK1062";
    final String actualExternalId = tsTrk1062EntryPoint.getExternalId();
    assertNotNull(actualExternalId, "ExternalId should not be null");
    assertEquals(expectedExternalId, actualExternalId, "ExternalId not equal");
  }

  @Test
  public void testGetIssuerTypeId() throws Exception {
    final String expectedIssuerTypeId = "TSTRK1062";
    final String actualIssuerTypeId = tsTrk1062EntryPoint.getIssuerTypeId();
    assertNotNull(actualIssuerTypeId, "IssuerTypeId should not be null");
    assertEquals(expectedIssuerTypeId, actualIssuerTypeId, "IssuerTypeId not equal");
  }
}
