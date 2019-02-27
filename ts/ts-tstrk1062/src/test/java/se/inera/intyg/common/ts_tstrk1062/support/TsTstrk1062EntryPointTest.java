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

package se.inera.intyg.common.ts_tstrk1062.support;

import static junit.framework.TestCase.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;

@RunWith(MockitoJUnitRunner.class)
public class TsTstrk1062EntryPointTest {

    @InjectMocks
    private TsTstrk1062EntryPoint tsTstrk1062EntryPoint;

    @Test
    public void testGetModuleId() throws Exception {
        final String actualModuleId = tsTstrk1062EntryPoint.getModuleId();
        assertNotNull("ModuleId should not be null", actualModuleId);
        assertFalse("ModuleId should not be empty", actualModuleId.isEmpty());
    }

    @Test
    public void testGetModuleName() throws Exception {
        final String actualModuleName = tsTstrk1062EntryPoint.getModuleName();
        assertNotNull("ModuleName should not be null", actualModuleName);
        assertFalse("ModuleName should not be empty", actualModuleName.isEmpty());
    }

    @Test
    public void testGetModuleDescription() throws Exception {
        final String actualModuleDescription = tsTstrk1062EntryPoint.getModuleDescription();
        assertNotNull("ModuleDescription should not be null", actualModuleDescription);
        assertFalse("ModuleDescription should not be empty", actualModuleDescription.isEmpty());
    }

    @Test
    public void testGetModuleCssPathMinaIntyg() throws Exception {
        final String actualPath = tsTstrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.MINA_INTYG);
        assertNotNull("Mina Intyg cssPath should not be null", actualPath);
        assertTrue("Mina Intyg cssPath should be empty", actualPath.isEmpty());
    }

    @Test
    public void testGetModuleCssPathWebCert() throws Exception {
        final String expectedPath = "/web/webjars/ts-tstrk1062/webcert/css/ts-tstrk1062.css";
        final String actualPath = tsTstrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.WEBCERT);
        assertNotNull("WebCert cssPath should not be null", actualPath);
        assertEquals("WebCert cssPath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleCssPathIntygstjansten() throws Exception {
        final String actualPath = tsTstrk1062EntryPoint.getModuleCssPath(ApplicationOrigin.INTYGSTJANST);
        assertNull("Intygstjanst cssPath should be null", actualPath);
    }

    @Test
    public void testGetModuleScriptPathMinaIntyg() throws Exception {
        final String expectedPath = "/web/webjars/ts-tstrk1062/minaintyg/js/module";
        final String actualPath = tsTstrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.MINA_INTYG);
        assertNotNull("Mina intyg scriptpath should not be null", actualPath);
        assertEquals("Mina intyg scriptpath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleScriptPathWebCert() throws Exception {
        final String expectedPath = "/web/webjars/ts-tstrk1062/webcert/module";
        final String actualPath = tsTstrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.WEBCERT);
        assertNotNull("WebCert scriptpath should not be null", actualPath);
        assertEquals("WebCert scriptpath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleScriptPathIntygstjansten() throws Exception {
        final String actualPath = tsTstrk1062EntryPoint.getModuleScriptPath(ApplicationOrigin.INTYGSTJANST);
        assertNull("Intygstjanst scriptpath should be null", actualPath);
    }

    @Test
    public void testGetModuleDependencyPathMinaIntyg() throws Exception {
        final String expectedPath = "/web/webjars/ts-tstrk1062/minaintyg/js/module-deps.json";
        final String actualPath = tsTstrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.MINA_INTYG);
        assertNotNull("Mina intyg dependencyPath should not be null", actualPath);
        assertEquals("Mina intyg dependencyPath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleDependencyPathWebCert() throws Exception {
        final String expectedPath = "/web/webjars/ts-tstrk1062/webcert/module-deps.json";
        final String actualPath = tsTstrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.WEBCERT);
        assertNotNull("WebCert dependencyPath should not be null", actualPath);
        assertEquals("WebCert dependencyPath not equal", expectedPath, actualPath);
    }

    @Test
    public void testGetModuleDependencyPathIntygstjansten() throws Exception {
        final String actualPath = tsTstrk1062EntryPoint.getModuleDependencyDefinitionPath(ApplicationOrigin.INTYGSTJANST);
        assertNull("Intygstjanst dependencyPath should be null", actualPath);
    }

    @Test
    public void testGetDefaultRecipient() throws Exception {
        final String expectedRecipient = "TRANSP";
        final String actualRecipient = tsTstrk1062EntryPoint.getDefaultRecipient();
        assertNotNull("Recipient should not be null", actualRecipient);
        assertEquals("Recipient not equal", expectedRecipient, actualRecipient);
    }

    @Test
    public void testGetExternalId() throws Exception {
        final String expectedExternalId = "TSTRK1062";
        final String actualExternalId = tsTstrk1062EntryPoint.getExternalId();
        assertNotNull("ExternalId should not be null", actualExternalId);
        assertEquals("ExternalId not equal", expectedExternalId, actualExternalId);
    }

    @Test
    public void testGetIssuerTypeId() throws Exception {
        final String expectedIssuerTypeId = "TSTRK1062";
        final String actualIssuerTypeId = tsTstrk1062EntryPoint.getIssuerTypeId();
        assertNotNull("IssuerTypeId should not be null", actualIssuerTypeId);
        assertEquals("IssuerTypeId not equal", expectedIssuerTypeId, actualIssuerTypeId);
    }
}
