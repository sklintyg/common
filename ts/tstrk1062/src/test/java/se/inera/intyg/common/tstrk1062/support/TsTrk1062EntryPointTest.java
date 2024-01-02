/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint.MODULE_ID;

import java.util.Optional;
import java.util.SortedMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

@RunWith(MockitoJUnitRunner.class)
public class TsTrk1062EntryPointTest {

    private IntygTextsRepository mockIntygTextsRepository;

    private TsTrk1062EntryPoint tsTrk1062EntryPoint;

    private static String LATEST_VERSION = "1.0";

    @Before
    public void setUp() {
        mockIntygTextsRepository = mock(IntygTextsRepository.class);
        tsTrk1062EntryPoint = new TsTrk1062EntryPoint(Optional.of(mockIntygTextsRepository));
    }

    @Test
    public void testGetModuleId() throws Exception {
        final String actualModuleId = tsTrk1062EntryPoint.getModuleId();
        assertNotNull("ModuleId should not be null", actualModuleId);
        assertFalse("ModuleId should not be empty", actualModuleId.isEmpty());
    }

    @Test
    public void testGetModuleName() throws Exception {
        final String actualModuleName = tsTrk1062EntryPoint.getModuleName();
        assertNotNull("ModuleName should not be null", actualModuleName);
        assertFalse("ModuleName should not be empty", actualModuleName.isEmpty());
    }

    @Test
    public void testGetModuleDescription() throws Exception {
        final String actualModuleDescription = tsTrk1062EntryPoint.getModuleDescription();
        assertNotNull("ModuleDescription should not be null", actualModuleDescription);
        assertFalse("ModuleDescription should not be empty", actualModuleDescription.isEmpty());
    }

    @Test
    public void testGetDetailedModuleDescription() throws Exception {
        final String expectedDetailedModuleDescription = "Text";
        final SortedMap<String, String> mockedTextMap = mock(SortedMap.class);
        doReturn(expectedDetailedModuleDescription).when(mockedTextMap).get(any());

        final IntygTexts expectedIntygTexts = new IntygTexts("1.0", "tstrk1062", null, null, mockedTextMap, null, null);

        doReturn(LATEST_VERSION).when(mockIntygTextsRepository).getLatestVersion(MODULE_ID);
        doReturn(expectedIntygTexts).when(mockIntygTextsRepository).getTexts(MODULE_ID, LATEST_VERSION);

        final String actualDetailedModuleDescription = tsTrk1062EntryPoint.getDetailedModuleDescription();

        assertNotNull("DetailedModuleDescription should not be null", actualDetailedModuleDescription);
        assertEquals("DetailedModuleDescription not equal", expectedDetailedModuleDescription, actualDetailedModuleDescription);
    }

    @Test
    public void testGetDetailedModuleDescriptionMissingTexts() throws Exception {
        doReturn(LATEST_VERSION).when(mockIntygTextsRepository).getLatestVersion(MODULE_ID);
        doReturn(null).when(mockIntygTextsRepository).getTexts(MODULE_ID, LATEST_VERSION);

        final String actualDetailedModuleDescription = tsTrk1062EntryPoint.getDetailedModuleDescription();

        assertNull("DetailedModuleDescription should be null", actualDetailedModuleDescription);
    }

    @Test
    public void testGetDetailedModuleDescriptionMissingRepo() throws Exception {
        final TsTrk1062EntryPoint tsTrk1062EntryPointWithoutRepo = new TsTrk1062EntryPoint();
        final String actualDetailedModuleDescription = tsTrk1062EntryPointWithoutRepo.getDetailedModuleDescription();

        assertNull("DetailedModuleDescription should be null", actualDetailedModuleDescription);
    }

    @Test
    public void testGetModuleCssPathMinaIntyg() throws Exception {
        final String actualPath = tsTrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.MINA_INTYG);
        assertNotNull("Mina Intyg cssPath should not be null", actualPath);
        assertTrue("Mina Intyg cssPath should be empty", actualPath.isEmpty());
    }

    @Test
    public void testGetModuleCssPathWebCert() throws Exception {
        final String expectedPath = "/web/webjars/tstrk1062/webcert/css/tstrk1062.css";
        final String actualPath = tsTrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.WEBCERT);
        assertNotNull("WebCert cssPath should not be null", actualPath);
        assertEquals("WebCert cssPath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleCssPathIntygstjansten() throws Exception {
        final String actualPath = tsTrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.INTYGSTJANST);
        assertNull("Intygstjanst cssPath should be null", actualPath);
    }

    @Test
    public void testGetModuleScriptPathMinaIntyg() throws Exception {
        final String expectedPath = "/web/webjars/tstrk1062/minaintyg/js/module";
        final String actualPath = tsTrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.MINA_INTYG);
        assertNotNull("Mina intyg scriptpath should not be null", actualPath);
        assertEquals("Mina intyg scriptpath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleScriptPathWebCert() throws Exception {
        final String expectedPath = "/web/webjars/tstrk1062/webcert/module";
        final String actualPath = tsTrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.WEBCERT);
        assertNotNull("WebCert scriptpath should not be null", actualPath);
        assertEquals("WebCert scriptpath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleScriptPathIntygstjansten() throws Exception {
        final String actualPath = tsTrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.INTYGSTJANST);
        assertNull("Intygstjanst scriptpath should be null", actualPath);
    }

    @Test
    public void testGetModuleDependencyPathMinaIntyg() throws Exception {
        final String expectedPath = "/web/webjars/tstrk1062/minaintyg/js/module-deps.json";
        final String actualPath = tsTrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.MINA_INTYG);
        assertNotNull("Mina intyg dependencyPath should not be null", actualPath);
        assertEquals("Mina intyg dependencyPath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleDependencyPathWebCert() throws Exception {
        final String expectedPath = "/web/webjars/tstrk1062/webcert/module-deps.json";
        final String actualPath = tsTrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.WEBCERT);
        assertNotNull("WebCert dependencyPath should not be null", actualPath);
        assertEquals("WebCert dependencyPath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleDependencyPathIntygstjansten() throws Exception {
        final String actualPath = tsTrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.INTYGSTJANST);
        assertNull("Intygstjanst dependencyPath should be null", actualPath);
    }

    @Test
    public void testGetDefaultRecipient() throws Exception {
        final String expectedRecipient = "TRANSP";
        final String actualRecipient = tsTrk1062EntryPoint.getDefaultRecipient();
        assertNotNull("Recipient should not be null", actualRecipient);
        assertEquals("Recipient not equal", expectedRecipient, actualRecipient);
    }

    @Test
    public void testGetExternalId() throws Exception {
        final String expectedExternalId = "TSTRK1062";
        final String actualExternalId = tsTrk1062EntryPoint.getExternalId();
        assertNotNull("ExternalId should not be null", actualExternalId);
        assertEquals("ExternalId not equal", expectedExternalId, actualExternalId);
    }

    @Test
    public void testGetIssuerTypeId() throws Exception {
        final String expectedIssuerTypeId = "TSTRK1062";
        final String actualIssuerTypeId = tsTrk1062EntryPoint.getIssuerTypeId();
        assertNotNull("IssuerTypeId should not be null", actualIssuerTypeId);
        assertEquals("IssuerTypeId not equal", expectedIssuerTypeId, actualIssuerTypeId);
    }
}
